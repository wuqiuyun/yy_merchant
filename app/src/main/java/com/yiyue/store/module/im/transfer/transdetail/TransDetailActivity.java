package com.yiyue.store.module.im.transfer.transdetail;

import android.content.Intent;
import android.view.View;

import com.yiyue.store.R;
import com.yiyue.store.base.BaseAppCompatActivity;
import com.yiyue.store.databinding.ActivityTransDetailBinding;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.vo.bean.SelfDefinedInfoBean;
import com.yiyue.store.module.im.redpacket.redrecords.RedRecordsActivity;
import com.yl.core.component.image.ImageLoader;
import com.yl.core.component.image.ImageLoaderConfig;
import com.yl.core.component.image.glide.GlideImageLoaderStrategy;
import com.yl.core.util.StatusBarUtil;

public class TransDetailActivity extends BaseAppCompatActivity {
    private ActivityTransDetailBinding mBinding;
    protected SelfDefinedInfoBean chatNoFriendBean;//红包 转账 页面间传递 自定义的包装对象的key

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_trans_detail;
    }

    @Override
    protected void init() {
        StatusBarUtil.setLightMode(this);
        mBinding = (ActivityTransDetailBinding) viewDataBinding;

        chatNoFriendBean = getIntent().getParcelableExtra(Constants.EXTRA_RED_ITEM_BEAN);
        mBinding.titleView.setLeftClickListener(v -> {
            finish();
        });

        if (chatNoFriendBean != null) {
            mBinding.tvRedContent.setText(chatNoFriendBean.getContent());
            mBinding.tvNick.setText(chatNoFriendBean.getNickname());
            mBinding.tvRpId.setText("ID: " + chatNoFriendBean.getUserId());

            if (chatNoFriendBean.getImusername().equals(AccountManager.getInstance().getAccount().getImusername())) {
                mBinding.layoutRedSending.setVisibility(View.VISIBLE);
                mBinding.layoutRedHad.setVisibility(View.GONE);
                //0 失效 1 发送中 2 已接收
                if (chatNoFriendBean.getRedPacketStatus() == 2) {
                    mBinding.tvMoneySelf.setText(String.format(getResources().getString(R.string.rp_have_trans_receive), chatNoFriendBean.getMoney()));
                } else if (chatNoFriendBean.getRedPacketStatus() == 0) {
                    mBinding.tvMoneySelf.setText(String.format(getResources().getString(R.string.rp_have_trans_outtime), chatNoFriendBean.getMoney()));
                } else if (chatNoFriendBean.getRedPacketStatus() == 1) {
                    mBinding.tvMoneySelf.setText(String.format(getResources().getString(R.string.rp_havenot_trans_receive), chatNoFriendBean.getMoney()));
                }
            } else {
                mBinding.tvRpMoney.setText(chatNoFriendBean.getMoney() + "");
                mBinding.layoutRedSending.setVisibility(View.GONE);
                mBinding.layoutRedHad.setVisibility(View.VISIBLE);
                if (chatNoFriendBean.getRedPacketStatus() == 2) {
                    mBinding.tvRpDesc.setText(getResources().getString(R.string.transfer_had_receive));
                } else if (chatNoFriendBean.getRedPacketStatus() == 0) {
                    mBinding.tvRpDesc.setText(getResources().getString(R.string.transfer_desc_outday));
                } else if (chatNoFriendBean.getRedPacketStatus() == 1) {
                    mBinding.tvRpDesc.setText(getResources().getString(R.string.transfer_desc_sending));
                }
            }
            ImageLoaderConfig config = null;
            if (config == null){
                config = new ImageLoaderConfig.Builder().
                        setAsBitmap(true).
                        setPlaceHolderResId(R.drawable.im_avatar).
                        setErrorResId(R.drawable.im_avatar).
                        setDiskCacheStrategy(ImageLoaderConfig.DiskCache.SOURCE).
                        setPrioriy(ImageLoaderConfig.LoadPriority.HIGH).build();
            }
            ImageLoader.loadImage(mBinding.ivAvatar,chatNoFriendBean.getPath(), config, null);

        }

        mBinding.tvWatchHistory.setOnClickListener(v -> {
            Intent intent = new Intent(TransDetailActivity.this, RedRecordsActivity.class);
            intent.putExtra("flag", 2);//1红包记录  2转账记录
            startActivity(intent);
        });
    }

}
