package com.yiyue.store.module.mine;

import android.support.v4.app.Fragment;
import android.view.View;

import com.yiyue.store.R;
import com.yiyue.store.base.fragment.BaseMvpFragment;
import com.yiyue.store.component.databind.ClickHandler;
import com.yiyue.store.databinding.FragmentMeBinding;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.vo.bean.UserBean;
import com.yiyue.store.module.mine.collect.CollectActivity;
import com.yiyue.store.module.mine.footprint.FootprintActivity;
import com.yiyue.store.module.mine.info.UserInfoActivity;
import com.yiyue.store.module.mine.pplarz.PopularizeActivity;
import com.yiyue.store.module.mine.settings.SettingsActivity;
import com.yiyue.store.module.mine.wallet.WalletActivity;
import com.yiyue.store.util.FormatUtil;
import com.yl.core.component.image.ImageLoader;
import com.yl.core.component.image.ImageLoaderConfig;
import com.yl.core.component.mvp.factory.CreatePresenter;

/**
 * 我的
 * <p>
 * Created by zm on 2018/9/19.
 */
@CreatePresenter(MinePresenter.class)
public class MineFragment extends BaseMvpFragment<IMineView, MinePresenter> implements IMineView, ClickHandler {

    private FragmentMeBinding mineBinding;

    public static Fragment newInstance() {
        return new MineFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    public void onResume() {
        super.onResume();
        ImageLoaderConfig config = new ImageLoaderConfig.Builder().
                setCropType(ImageLoaderConfig.CENTER_INSIDE).
                setAsBitmap(true).
                setCropCircle(true).
                setPlaceHolderResId(R.drawable.icon_head_pic_def).
                setErrorResId(R.drawable.icon_head_pic_def).
                setDiskCacheStrategy(ImageLoaderConfig.DiskCache.SOURCE).
                setPrioriy(ImageLoaderConfig.LoadPriority.HIGH).build();
        UserBean userBean = AccountManager.getInstance().getAccount();
        if (userBean != null) {
            // 用户昵称
            mineBinding.tvUserName.setText(FormatUtil.Formatstring(userBean.getNickname()));
            // 用户Id
            mineBinding.tvUserId.setText(FormatUtil.Formatstring("ID:"+userBean.getIdNo()));
            ImageLoader.loadImage(mineBinding.ivPhoto, userBean.getHeadImg(), config, null);
        }
    }

    @Override
    protected void initView() {
        mineBinding = (FragmentMeBinding) viewDataBinding;
        mineBinding.setClick(this);
        mineBinding.titleView.setRightTextClickListener(v -> SettingsActivity.startActivity(getContext(), SettingsActivity.class));
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void showToast(String message) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mine_wallet: // 我的钱包
                WalletActivity.startActivity(getContext(), WalletActivity.class);
                break;
            case R.id.mine_collect: // 我的收藏
                CollectActivity.startActivity(getContext(), CollectActivity.class);
                break;
            case R.id.mine_footprint: // 我的足迹
                FootprintActivity.startActivity(getContext(), FootprintActivity.class);
                break;
            case R.id.mine_recommend: // 推荐用户
                PopularizeActivity.startActivity(getContext(), PopularizeActivity.class);
                break;
            case R.id.btn_user_info: // 个人资料
                UserInfoActivity.startActivity(getContext(), UserInfoActivity.class);
                break;
        }
    }
}
