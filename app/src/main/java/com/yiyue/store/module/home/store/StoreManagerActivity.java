package com.yiyue.store.module.home.store;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.component.amap.LocationPresenter;
import com.yiyue.store.component.databind.ClickHandler;
import com.yiyue.store.component.recycleview.SpaceItemHorizontalDecoration;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivityStoreManagerBinding;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.vo.bean.EventBean;
import com.yiyue.store.model.vo.bean.ReCodeBean;
import com.yiyue.store.model.vo.bean.StoreManageNexusStyScroolBean;
import com.yiyue.store.model.vo.bean.StoreManageScopeBean;
import com.yiyue.store.model.vo.bean.StoreManageScopeInfoBean;
import com.yiyue.store.module.home.evaluation.EvaluationManagerActivity;
import com.yiyue.store.module.home.join.JoinStylistActivity;
import com.yiyue.store.module.im.sharetofriend.ShareToFriendActivity;
import com.yiyue.store.module.mine.info.UserInfoActivity;
import com.yiyue.store.module.mine.info.service.ServicesScopeActivity;
import com.yiyue.store.util.ShareUtils;
import com.yiyue.store.util.StringUtil;
import com.yiyue.store.widget.banner.BannerConfig;
import com.yiyue.store.widget.banner.listener.OnBannerListener;
import com.yiyue.store.widget.banner.loader.ImageLoaders;
import com.yiyue.store.widget.dialog.BaseEasyDialog;
import com.yiyue.store.widget.dialog.EasyDialog;
import com.yiyue.store.widget.dialog.ViewConvertListener;
import com.yiyue.store.widget.dialog.ViewHolder;
import com.yl.core.component.image.ImageLoader;
import com.yl.core.component.image.ImageLoaderConfig;
import com.yl.core.component.log.DLog;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

/**
 * 门店管理
 * <p>
 * Create by zm on 2018/10/12
 */
@CreatePresenter(StoreManagerPresenter.class)
public class StoreManagerActivity extends BaseMvpAppCompatActivity<IStoreManagerView, StoreManagerPresenter>
        implements IStoreManagerView, ClickHandler, AMapLocationListener, OnBannerListener {

    private int PLAY_TIME = 5000;

    public static final int SERVICESCOPE = 0x003;
    public static final int SERVICEADDRESS = 0x004;

    ActivityStoreManagerBinding mBinding;

    private int storeType;     //来源类型  1 首页门店管理 2 我的收藏/我的足迹  3 美发师详情 更多待定义
    private String storeId;    //门店的id

    private String inviteCode = null;//邀请码

    private int isCollection;// 0 未收藏 1 已收藏

    private StylistAdapter mStylistAdapter;
    private ArrayList<StoreManageNexusStyScroolBean> mStylistBeans = new ArrayList<>();

    //服务范围list
    private ServiceScopeAdapter mServiceAdapter;
    private ArrayList<String> mScopeList = new ArrayList<>();
    private String lat, lnt;
    private LocationPresenter locationPresenter;
    private ImageLoaderConfig config;

    private String address;//门店位置
    private String myStoreId = AccountManager.getInstance().getStoreId();

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_store_manager;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void init() {
        EventBus.getDefault().register(this);

        StatusBarUtil.setLightMode(this);
        locationPresenter = new LocationPresenter(this);
        locationPresenter.setMapLocationListener(this);
        new RxPermissions(this)
                .request(Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(grant -> {
                    if (grant) {
                        locationPresenter.startLocation(true);
                    } else {
                        ToastUtils.shortToast("定位权限未开启.");
                    }
                });
        getBundle();

        config = new ImageLoaderConfig.Builder().
                setCropType(ImageLoaderConfig.CENTER_CROP).
                setAsBitmap(true).
                setPlaceHolderResId(R.drawable.home_bg).
                setErrorResId(R.drawable.home_bg).
                setDiskCacheStrategy(ImageLoaderConfig.DiskCache.SOURCE).
                setPrioriy(ImageLoaderConfig.LoadPriority.HIGH).build();

        //获取我的推荐码
        getMvpPresenter().findReCode();
    }

    private void getBundle() {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            storeType = bundle.getInt(Constants.STORE_TYPE);
            storeId = bundle.getString(Constants.STORE_ID);
            initView();
            loadData();
        } else {
            showToast("数据获取失败");
            finish();
        }

    }

    private void initView() {
        mBinding = (ActivityStoreManagerBinding) viewDataBinding;
        mBinding.setClick(this);

        mBinding.ratingBar.setEnabled(false);
        mBinding.mrScore.setEnabled(false);
        // titleview
        mBinding.titleView.setLeftClickListener(v -> finish());
        mBinding.titleView.setRightImgClickListener(v -> {
            // 分享
            new Thread() {
                public void run() {
                    showShareDialog();
                }
            }.start();
        });

        //进入他人的门店详情，则增加收藏按钮，隐藏编辑区域
        if (storeType == Constants.STORE_COLLECT_FOOTPRINT || (storeType == Constants.STORE_STYLIST_DETAIL && !myStoreId.equals(storeId))) {
            mBinding.titleView.setSubRightIcon(getResources().getDrawable(R.drawable.icon_collection));
            mBinding.titleView.setSubRightImgClickListener(v -> {
                // 收藏
                getMvpPresenter().updateCollectionType(String.valueOf(isCollection), storeId, AccountManager.getInstance().getUserId());
            });
            mBinding.tvAddressEdit.setVisibility(View.GONE);
            mBinding.tvStoreScope.setVisibility(View.GONE);
        }else {
            mBinding.titleView.setSubRightIconVisib(false);
        }

        // stylist list
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // 横向滚动
        mBinding.stylistList.setLayoutManager(linearLayoutManager);
        mBinding.stylistList.addItemDecoration(new SpaceItemHorizontalDecoration(20));
        mStylistAdapter = new StylistAdapter(getBaseContext());
        mBinding.stylistList.setAdapter(mStylistAdapter);
        // service scope list
        LinearLayoutManager LayoutManager = new LinearLayoutManager(getBaseContext());
        LayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // 横向滚动
        mBinding.serviceScopeList.setLayoutManager(LayoutManager);
        mBinding.serviceScopeList.addItemDecoration(new SpaceItemHorizontalDecoration(30));
        mServiceAdapter = new ServiceScopeAdapter(getBaseContext());
        mBinding.serviceScopeList.setAdapter(mServiceAdapter);
    }

    private void loadData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        getMvpPresenter().getStoreScore(storeId);
        getMvpPresenter().getNexusStyScrool(3, storeId);//入驻/签约美发师
        getMvpPresenter().getStoreServerScope(storeId);//门店范围
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(message);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBean.UpdateAddressBean bean) {
        getMvpPresenter().getStoreInfo(lat, lnt, AccountManager.getInstance().getStoreId());//门店信息
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_address_edit: { // 前往编辑个人资料
                UserInfoActivity.startActivity(this, UserInfoActivity.class);
            }
            break;
            case R.id.tv_look_more: // 查看更多
                JoinStylistActivity.startActivity(getBaseContext(), JoinStylistActivity.class);
                break;
            case R.id.tv_store_scope: {// 门店范围
                ServicesScopeActivity.startActivity(getBaseContext(), ServicesScopeActivity.class);
            }
            break;
            case R.id.tv_look_comment: // 查看全部评价
                Bundle EvaluationBundle = new Bundle();
                EvaluationBundle.putInt(Constants.EVALUATION_TYPE, 2);
                EvaluationBundle.putString(Constants.STORE_ID, storeId);
                EvaluationManagerActivity.startActivity(getBaseContext(), EvaluationManagerActivity.class, EvaluationBundle);
                break;
        }
    }

    @Override
    public void getStoreScoreSucceed(StoreManageScopeBean storeManageScopeBean) {
        if (storeManageScopeBean != null) {
            // 环境评分
            mBinding.tvSkillScore.setText(String.format(getString(R.string.environment_grade), storeManageScopeBean.getEnvironmentScore()));
            // 服务态度
            mBinding.tvServiceScore.setText(String.format(getString(R.string.service_attitude), storeManageScopeBean.getServerScore()));
            // 服务水平
            if (storeManageScopeBean.getPareServerAvg() == 10) {
                mBinding.tvServiceGrade.setText("等于平均水平");
            }
            if (storeManageScopeBean.getPareServerAvg() < 0) {
                mBinding.tvServiceGrade.setText("低于平均水平");
            } else if (storeManageScopeBean.getPareServerAvg() == 0) {
                mBinding.tvServiceGrade.setText("等于平均水平");
            } else if (storeManageScopeBean.getPareServerAvg() == 1) {
                mBinding.tvServiceGrade.setText("大于平均水平");
            }

            // 环境水平
            if (storeManageScopeBean.getPareEnvirtAvg() == 10) {
                mBinding.tvSkillGrade.setText("等于平均水平");
            }
            if (storeManageScopeBean.getPareEnvirtAvg() < 0) {
                mBinding.tvSkillGrade.setText("低于平均水平");
            } else if (storeManageScopeBean.getPareEnvirtAvg() == 0) {
                mBinding.tvSkillGrade.setText("等于平均水平");
            } else if (storeManageScopeBean.getPareEnvirtAvg() == 1) {
                mBinding.tvSkillGrade.setText("大于平均水平");
            }

            mBinding.mrScore.setRating((float) storeManageScopeBean.getScore());
            mBinding.tvLookComment.setText((String.format(getString(R.string.comment_look_all), String.valueOf(storeManageScopeBean.getScoretimes()))));
        }
    }

    @Override
    public void getStoreServerScoreSucceed(StoreManageScopeBean storeManageScopeBean) {
        if (storeManageScopeBean != null) {
            if (storeManageScopeBean.getCatergoryNames() != null && storeManageScopeBean.getCatergoryNames().size() > 0) {
                mScopeList.clear();
                mScopeList.addAll(storeManageScopeBean.getCatergoryNames());
                mServiceAdapter.setDatas(mScopeList, true);
            }

        }
    }

    @Override
    public void getNexusStyScroolSucceed(ArrayList<StoreManageNexusStyScroolBean> storeManageNexusStyScroolBean) {
        if (storeManageNexusStyScroolBean != null && storeManageNexusStyScroolBean.size() > 0) {
            mStylistBeans.clear();
            mStylistBeans.addAll(storeManageNexusStyScroolBean);
            mStylistAdapter.setDatas(mStylistBeans, true);
        }
    }

    @Override
    public void getStoreInfoSucceed(StoreManageScopeInfoBean storeManageScopeInfoBean) {
        if (storeManageScopeInfoBean != null) {
            address = storeManageScopeInfoBean.getLocation();
            mBinding.ratingBar.setRating((float) storeManageScopeInfoBean.getGrade());
            mBinding.tvRatingNum.setText(storeManageScopeInfoBean.getGrade() + "分");
            mBinding.tvStoreName.setText(storeManageScopeInfoBean.getStoreName());
            mBinding.tvAddressArea.setText(storeManageScopeInfoBean.getLocation());

            isCollection = storeManageScopeInfoBean.getIsCollection();
            if (storeType == Constants.STORE_COLLECT_FOOTPRINT || (storeType == Constants.STORE_STYLIST_DETAIL && !myStoreId.equals(storeId))) {
                setCollectionType();
            }else {
                mBinding.titleView.setSubRightIconVisib(false);
            }

            mBinding.baBanner.setImages(storeManageScopeInfoBean.getStorePhotos())
                    .setImageLoader(new ImageLoaders() {
                        @Override
                        public void displayImage(Context context, Object path, ImageView imageView) {
                            ImageLoader.loadImage(imageView, (String) path, config, null);
                        }
                    })
                    .setBannerStyle(BannerConfig.CIRCLE_INDICATOR_NOTITLE_INSIDE)
                    .isAutoPlay(true)
                    .setDelayTime(PLAY_TIME)
                    .setOnBannerListener(this)
                    .start();
        }
    }

    @Override
    public void updateCollectionTypeSuc() {
        if (isCollection == 0) {
            showToast("收藏成功");
            isCollection = 1;
        } else {
            showToast("取消收藏成功");
            isCollection = 0;
        }
        setCollectionType();
    }

    // 设置收藏/取消图片状态
    private void setCollectionType() {
        if (isCollection == 0) {
            mBinding.titleView.setSubRightIcon(getResources().getDrawable(R.drawable.icon_collection));
        } else {
            mBinding.titleView.setSubRightIcon(getResources().getDrawable(R.drawable.icon_collection_true));
        }
    }

    @Override
    public void findReCodeSuc(ReCodeBean recode) {
        inviteCode = recode.getInvitecode();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        lat = String.valueOf(aMapLocation.getLatitude());
        lnt = String.valueOf(aMapLocation.getLongitude());
        if (storeType == Constants.STORE_COLLECT_FOOTPRINT || (storeType == Constants.STORE_STYLIST_DETAIL && !myStoreId.equals(storeId))) {
            //他人门店信息
            getMvpPresenter().getStoreInfo(lat, lnt, storeId);
        } else {
            //我的门店信息
            getMvpPresenter().getStoreInfo(lat, lnt, AccountManager.getInstance().getStoreId());
        }

    }

    @Override
    public void OnBannerClick(int position) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case SERVICESCOPE: // 服务范围
                    getMvpPresenter().getStoreServerScope(storeId);//门店范围
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //分享弹框
    private void showShareDialog() {
        EasyDialog.init()
                .setLayoutId(R.layout.dialog_share)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseEasyDialog dialog) {
                        holder.getView(R.id.tv_share_cancel).setOnClickListener(v -> {
                            dialog.dismiss();
                        });
                        holder.getView(R.id.ll_share_wechat).setOnClickListener(v -> {
                            ShareUtils.shareWechat(
                                    getResources().getString(R.string.dialog_share_title_apprecommend),
                                    getShareParam(),
                                    getResources().getString(R.string.dialog_share_content),
                                    AccountManager.getInstance().getAccount().getHeadImg(),
                                    platformActionListener);
                            dialog.dismiss();
                            DLog.e("MBLog", getShareParam());
                        });
                        holder.getView(R.id.ll_share_wechatmoments).setOnClickListener(v -> {
                            ShareUtils.shareWechatMoments(
                                    getResources().getString(R.string.dialog_share_title_apprecommend),
                                    getShareParam(),
                                    getResources().getString(R.string.dialog_share_content),
                                    AccountManager.getInstance().getAccount().getHeadImg(),
                                    platformActionListener);
                            dialog.dismiss();
                        });
                        holder.getView(R.id.ll_share_qq).setOnClickListener(v -> {
                            ShareUtils.shareQQ(
                                    getResources().getString(R.string.dialog_share_title_apprecommend),
                                    getShareParam(),
                                    getResources().getString(R.string.dialog_share_content),
                                    AccountManager.getInstance().getAccount().getHeadImg(),
                                    platformActionListener);
                            dialog.dismiss();
                        });
                        holder.getView(R.id.ll_share_friend).setOnClickListener(v -> {
                            //分享的店铺的相关信息传递 没有传null 此页面门店的storeId必传
                            ShareToFriendActivity.startShareToFriendActivity(
                                    StoreManagerActivity.this,
                                    101,
                                    AccountManager.getInstance().getAccount().getImusername(),
                                    AccountManager.getInstance().getNickName(),
                                    AccountManager.getInstance().getStoreId(),
                                    AccountManager.getInstance().getUserId(),
                                    AccountManager.getInstance().getAccount().getHeadImg(),
                                    address);

                            dialog.dismiss();
                        });
                    }
                })
                .setPosition(Gravity.BOTTOM)
                .setMargin(0)
                .setOutCancel(true)
                .show(this.getSupportFragmentManager());
    }

    /**
     * 分享回调
     */
    PlatformActionListener platformActionListener = new PlatformActionListener() {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            DLog.e("kid", "分享成功");
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            DLog.e("kid", "分享失败");
        }

        @Override
        public void onCancel(Platform platform, int i) {
            DLog.e("kid", "分享取消");
        }
    };

    //生成分享附加参数
    private String getShareParam() {
        StringBuilder param = new StringBuilder();//分享附加参数
        String eName = AccountManager.getInstance().getNickName();
        //邀请码不为空
        if (!TextUtils.isEmpty(inviteCode)) {
            param.append("?").append(Constants.WEB_CODE).append(inviteCode);
            param.append("&").append(Constants.WEB_STORE_ID).append(storeId);
            param.append("&").append(Constants.WEB_NICKNAME).append(StringUtil.baseConvertStr(eName));
//            param.append("&").append(Constants.WEB_OPUS_ID).append("");
//            param.append("&").append(Constants.WEB_STYLIST_ID).append("");
        } else {
            param.append("?").append(Constants.WEB_STORE_ID).append(storeId);
            param.append("&").append(Constants.WEB_NICKNAME).append(StringUtil.baseConvertStr(eName));
//            param.append("?").append(Constants.WEB_OPUS_ID).append("");
//            param.append("?").append(Constants.WEB_STYLIST_ID).append("");
        }

        return Constants.WEB_STORE_DETAILS + param.toString();
    }

}
