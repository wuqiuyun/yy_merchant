package com.yiyue.store.module.home;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.yiyue.store.R;
import com.yiyue.store.base.fragment.BaseMvpFragment;
import com.yiyue.store.component.databind.ClickHandler;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.FragmentHomeBinding;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.constant.OrderStatus;
import com.yiyue.store.model.local.preferences.CommonSharedPreferences;
import com.yiyue.store.model.vo.bean.BannerBean;
import com.yiyue.store.model.vo.bean.EventBean;
import com.yiyue.store.model.vo.bean.StoreAuthApplyBean;
import com.yiyue.store.model.vo.bean.StoreOrderStatisticalBean;
import com.yiyue.store.module.certification.CertificationActivity;
import com.yiyue.store.module.common.h5.WebActivity;
import com.yiyue.store.module.home.evaluation.EvaluationManagerActivity;
import com.yiyue.store.module.home.invite.InviteStylistActivity;
import com.yiyue.store.module.home.join.JoinStylistActivity;
import com.yiyue.store.module.home.order.OrderCenterActivity;
import com.yiyue.store.module.home.orders.manager.OrderManagerActivity;
import com.yiyue.store.module.home.service.ServiceSettingActivity;
import com.yiyue.store.module.home.store.StoreManagerActivity;
import com.yiyue.store.module.home.time.TimeManageActivity;
import com.yiyue.store.util.FormatUtil;
import com.yiyue.store.widget.banner.BannerConfig;
import com.yiyue.store.widget.banner.listener.OnBannerListener;
import com.yiyue.store.widget.banner.loader.ImageLoaders;
import com.yl.core.component.image.ImageLoader;
import com.yl.core.component.image.ImageLoaderConfig;
import com.yl.core.component.mvp.factory.CreatePresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页
 * <p>
 * Created by zm on 2018/9/19.
 */
@CreatePresenter(HomePresenter.class)
public class HomeFragment extends BaseMvpFragment<IHomeView, HomePresenter>
        implements IHomeView, ClickHandler, OnBannerListener {
    private static final int PLAY_TIME = 5000;
    FragmentHomeBinding mBinding;
    private ImageLoaderConfig config;
    private ArrayList<BannerBean> data = new ArrayList<>();

    public static Fragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        // 认证信息
        getMvpPresenter().getStoreAuthApplyInfo();
        //banner信息
        getMvpPresenter().getBanner();
        // 订单统计
        getMvpPresenter().getStoreOrderStatistical();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }


    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        mBinding = (FragmentHomeBinding) viewDataBinding;
        mBinding.setClick(this);
    }

    /**
     * 收到新的推送 刷新显示
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBean.NewOrderMessage event) {
        if (event != null) {
            // 订单统计
            getMvpPresenter().getStoreOrderStatistical();
        }
    }


    @Override
    protected void loadData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_perfect_stores: //审核中,完善门店
                ServiceSettingActivity.startActivity(getContext(), ServiceSettingActivity.class);
                break;
            case R.id.tv_again_apply: //审核失败,重新申请
                CertificationActivity.startActivity(getContext(), CertificationActivity.class);
                break;
            case R.id.tv_ok: //审核通过,确认
                CommonSharedPreferences.getInstance().setCeritSuccessUiShow();
                InviteStylistActivity.startActivity(getContext(), InviteStylistActivity.class);
                break;
            case R.id.btn_order_confirm: // 待确认
                OrderCenterActivity.startActivity(getContext(), OrderStatus.ORDER_CONFIRM);
                break;
            case R.id.btn_order_service: // 待核销
                OrderCenterActivity.startActivity(getContext(), OrderStatus.ORDER_SERVICE);
                break;
            case R.id.btn_order_refund: // 已退款
                OrderCenterActivity.startActivity(getContext(), OrderStatus.ORDER_REFUND);
                break;
            case R.id.btn_order_complete: // 已完成
                OrderCenterActivity.startActivity(getContext(), OrderStatus.ORDER_COMPLETE);
                break;
            case R.id.btn_time_manager: // 时间管理
                TimeManageActivity.startActivity(getContext(), TimeManageActivity.class);
                break;
            case R.id.btn_comment_manager: // 评价管理
                Bundle EvaluationBundle = new Bundle();
                EvaluationBundle.putInt(Constants.EVALUATION_TYPE, 1);
                EvaluationManagerActivity.startActivity(getContext(), EvaluationManagerActivity.class, EvaluationBundle);
                break;
            case R.id.btn_store_manager: // 门店管理
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.STORE_TYPE, Constants.STORE_MANAGE);
                bundle.putString(Constants.STORE_ID, AccountManager.getInstance().getStoreId());
                StoreManagerActivity.startActivity(getContext(), StoreManagerActivity.class, bundle);
                break;
            case R.id.btn_stylist_invite: // 邀请美发师
                InviteStylistActivity.startActivity(getContext(), InviteStylistActivity.class);
                break;
            case R.id.btn_mine_stylist: // 我的美发师
                JoinStylistActivity.startActivity(getContext(), JoinStylistActivity.class);
                break;
            case R.id.btn_order_statistics: // 预约管理
                OrderManagerActivity.startActivity(getContext(),OrderManagerActivity.class);
                break;
        }
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(FormatUtil.Formatstring(message));
    }

    @Override
    public void onGetStoreAuthApplyInfoSuccess(StoreAuthApplyBean bean) {
        switch (bean.getStatus()) {
            case 0: // 待审核
                mBinding.llTheAudit.setVisibility(View.VISIBLE);
                mBinding.llAuditSucceed.setVisibility(View.GONE);
                mBinding.llAuditFailure.setVisibility(View.GONE);
                break;

            case 1: // 通过
                mBinding.llTheAudit.setVisibility(View.GONE);
                mBinding.llAuditFailure.setVisibility(View.GONE);

                if (CommonSharedPreferences.getInstance().isCeritSuccessUiShow()) {
                    mBinding.llAuditSucceed.setVisibility(View.GONE);
                } else {
                    mBinding.llAuditSucceed.setVisibility(View.VISIBLE);
                }
                break;

            case -1: // 驳回
                mBinding.llAuditFailure.setVisibility(View.VISIBLE);
                mBinding.llTheAudit.setVisibility(View.GONE);
                mBinding.llAuditSucceed.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onGetStoreOrderStatisticalSuccess(StoreOrderStatisticalBean bean) {
        mBinding.btnOrderConfirm.setNumber(bean.getPendingSum());
        mBinding.btnOrderService.setNumber(bean.getAcceptAndServiceSum());
        mBinding.btnOrderComplete.setNumber(bean.getSuccessSum());
        mBinding.btnOrderRefund.setNumber(bean.getRefundSum());
    }

    @Override
    public void getBannerSuccess(List<BannerBean> beans) {
        if (config == null) {
            config = new ImageLoaderConfig.Builder().
                    setCropType(ImageLoaderConfig.CENTER_CROP).
                    setAsBitmap(true).
                    setPlaceHolderResId(R.drawable.home_bg).
                    setErrorResId(R.drawable.home_bg).
                    setDiskCacheStrategy(ImageLoaderConfig.DiskCache.SOURCE).
                    setPrioriy(ImageLoaderConfig.LoadPriority.HIGH).build();
        }

        data = (ArrayList<BannerBean>) beans;
        ArrayList<String> photos = new ArrayList<>();
        for (BannerBean bean : beans) {
            photos.add(bean.getImage());
        }
        mBinding.banner.setImages(photos)
                .setImageLoader(new ImageLoaders() {
                    @Override
                    public void displayImage(Context context, Object path, ImageView imageView) {
                        ImageLoader.loadImage(imageView, (String) path, config, null);
                    }
                })
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                .isAutoPlay(true)
                .setDelayTime(PLAY_TIME)
                .setOnBannerListener(this)
                .start();
    }

    @Override
    public void OnBannerClick(int position) {
        BannerBean bean = data.get(position);
        String title = bean.getTitle();
        String url = bean.getUrl();
        if (!TextUtils.isEmpty(url.trim())) {
            WebActivity.startActivity(getActivity(), url, title);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
