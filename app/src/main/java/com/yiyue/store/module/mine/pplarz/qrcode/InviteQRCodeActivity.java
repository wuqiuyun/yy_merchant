package com.yiyue.store.module.mine.pplarz.qrcode;

import android.Manifest;
import android.graphics.Bitmap;
import android.view.View;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.component.databind.ClickHandler;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivityInviteQrcodeBinding;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.local.preferences.CommonSharedPreferences;
import com.yiyue.store.util.FormatUtil;
import com.yl.core.component.image.ImageLoader;
import com.yl.core.component.image.ImageLoaderConfig;
import com.yl.core.component.image.LoaderListener;
import com.yl.core.component.image.util.ImageUtil;
import com.yl.core.component.mvp.factory.CreatePresenter;

@CreatePresenter(InviteQRCodePresenter.class)
public class InviteQRCodeActivity extends BaseMvpAppCompatActivity<InviteQRCodeView, InviteQRCodePresenter>
        implements InviteQRCodeView, ClickHandler {

    ActivityInviteQrcodeBinding mBinding;

    private Bitmap mBitmap;
    private String imageUrl;
    private String inviteCode;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_invite_qrcode;
    }

    @Override
    protected void init() {
        initView();
        loadData();
    }

    private void initView() {
        mBinding = (ActivityInviteQrcodeBinding) viewDataBinding;
        mBinding.setClick(this);
        // back
        mBinding.titleView.setLeftClickListener(view -> {
                finish();
        });
        // 头像
        ImageLoaderConfig config = new ImageLoaderConfig.Builder()
                .setAsBitmap(true)
                .setCropType(ImageLoaderConfig.CENTER_CROP)
                .setCropCircle(true)
                .setPlaceHolderResId(R.drawable.icon_head_pic_def)
                .setErrorResId(R.drawable.icon_head_pic_def)
                .setDiskCacheStrategy(ImageLoaderConfig.DiskCache.SOURCE)
                .build();
        ImageLoader.loadImage(mBinding.ivPhoto, AccountManager.getInstance().getUserHeadImg(), config, null);
        // 用户名
        mBinding.tvUserName.setText(FormatUtil.Formatstring(AccountManager.getInstance().getNickName()));
        // 注册奖励
//        mBinding.tvRegisterReward.setText(FormatUtil.Formatstring(CommonSharedPreferences.getInstance().getBasicDataBean().getRegisterReward()));
        mBinding.tvRegisterReward.setText("520");
        // 邀请规则
        mBinding.tvInviteReward.setText(String.format(getString(R.string.invite_reward_des), "520"));
        // 我的推荐码
        inviteCode = CommonSharedPreferences.getInstance().getInvitationCode();
        mBinding.tvRecommendCode.setText(FormatUtil.Formatstring(inviteCode));
    }

    private void loadData() {
        getMvpPresenter().getWXShareQrCode(inviteCode);
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(FormatUtil.Formatstring(message));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                if (mBitmap == null) {
                    showToast("保存失败，请稍后");
                    getMvpPresenter().getWXShareQrCode(inviteCode);
                    return;
                }
                showToast("正在保存中...");
                new RxPermissions(this)
                        .request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe( grant -> {
                            if (grant) {
                                ImageUtil.saveImageAndRefresh(InviteQRCodeActivity.this, null, mBitmap, imageUrl, "", "", true);
                            }else {
                                showToast("保存失败");
                            }
                        });
                break;
        }
    }

    @Override
    public void setShareQrCodeUrl(String imgUrl) {
        imageUrl = imgUrl;
        ImageLoaderConfig config = new ImageLoaderConfig.Builder()
                .setAsBitmap(true)
                .setCropType(ImageLoaderConfig.CENTER_INSIDE)
                .setDiskCacheStrategy(ImageLoaderConfig.DiskCache.SOURCE)
                .setPlaceHolderResId(R.drawable.bg_image_placeholder)
                .setErrorResId(R.drawable.bg_image_placeholder)
                .build();
        ImageLoader.loadImage(mBinding.ivQrCode, imgUrl, config, new LoaderListener() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                mBitmap = bitmap;
            }

            @Override
            public void onError() {

            }
        });
    }
}
