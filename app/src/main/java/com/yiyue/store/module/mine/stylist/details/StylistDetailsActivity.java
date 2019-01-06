package com.yiyue.store.module.mine.stylist.details;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.base.adapter.BaseRecycleViewAdapter;
import com.yiyue.store.component.databind.ClickHandler;
import com.yiyue.store.component.recycleview.GridSpacingItemDecoration;
import com.yiyue.store.component.recycleview.SpaceItemDecoration;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivityStylistDetailsBinding;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.vo.bean.ReCodeBean;
import com.yiyue.store.model.vo.bean.SelfDefinedInfoBean;
import com.yiyue.store.model.vo.bean.StoreCollectionBean;
import com.yiyue.store.model.vo.bean.StylistCardBean;
import com.yiyue.store.model.vo.result.CheckMsgResult;
import com.yiyue.store.module.home.evaluation.EvaluationManagerActivity;
import com.yiyue.store.module.home.store.StoreManagerActivity;
import com.yiyue.store.module.im.chat.ChatActivity;
import com.yiyue.store.module.im.sharetofriend.ShareToFriendActivity;
import com.yiyue.store.module.mine.stylist.invitejoin.InviteJoinActivity;
import com.yiyue.store.module.mine.works.many.ManyWorksActivity;
import com.yiyue.store.util.FormatUtil;
import com.yiyue.store.util.PhoneUtil;
import com.yiyue.store.util.ShareUtils;
import com.yiyue.store.util.StringUtil;
import com.yiyue.store.widget.dialog.BaseEasyDialog;
import com.yiyue.store.widget.dialog.EasyDialog;
import com.yiyue.store.widget.dialog.ViewConvertListener;
import com.yiyue.store.widget.dialog.ViewHolder;
import com.yiyue.store.widget.dialog.YLDialog;
import com.yl.core.component.image.ImageLoader;
import com.yl.core.component.image.ImageLoaderConfig;
import com.yl.core.component.log.DLog;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

/**
 * 美发师详情
 * <p>
 */
@CreatePresenter(StylistDetailsPresenter.class)
public class StylistDetailsActivity extends BaseMvpAppCompatActivity<IStylistDetailsView, StylistDetailsPresenter>
        implements IStylistDetailsView, ClickHandler {

    ActivityStylistDetailsBinding binding;

    private ServiceProjectAdapter projectAdapter;
    private ArrayList<StylistCardBean.CardServerItemsBean> projectBeans = new ArrayList<>();//服务项目列表

    private ServiceBundleAdapter bundleAdapter;
    private ArrayList<StylistCardBean.CardPackages> bundleBeans = new ArrayList<>();//套餐列表

    private StoreAdapter storeAdapter;
    private ArrayList<StylistCardBean.CardStoreDTOsBean> storeBeans = new ArrayList<>();//入驻门店列表

    private WorksAdapter worksAdapter;
    private ArrayList<StylistCardBean.CardOpusDTOsBean> worksBeans = new ArrayList<>();//作品列表
    private boolean isCollection = false;//是否为收藏
    private String stylistId = "";//美发师ID
    private int nexus = 0;

    private StylistCardBean stylistCardBean;//保存的详情页信息bean

    private String inviteCode = null;//邀请码

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_stylist_details;
    }

    @Override
    protected void init() {

        initView();
        loadData();
        getMvpPresenter().findReCode();
    }

    private void initView() {
        StatusBarUtil.setDarkMode(this);
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            stylistId = bundle.getString(Constants.STYLIST_ID);
        }

        binding = (ActivityStylistDetailsBinding) viewDataBinding;
        binding.setClick(this);

        // titleview
        binding.ivBack.setOnClickListener(v -> finish());

        // service project 服务项目
        binding.projectList.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        binding.projectList.addItemDecoration(new SpaceItemDecoration(20));
        projectAdapter = new ServiceProjectAdapter(getBaseContext());
        binding.projectList.setHasFixedSize(true);
        binding.projectList.setNestedScrollingEnabled(false);
        binding.projectList.setAdapter(projectAdapter);

        // service bundle
        binding.listService.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        binding.listService.addItemDecoration(new SpaceItemDecoration(30));
        bundleAdapter = new ServiceBundleAdapter(getBaseContext());
        binding.listService.setHasFixedSize(true);
        binding.listService.setNestedScrollingEnabled(false);
        binding.listService.setAdapter(bundleAdapter);

        // store 入驻门店列表
        binding.storeList.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        binding.storeList.addItemDecoration(new SpaceItemDecoration(20));
        binding.storeList.setHasFixedSize(true);
        binding.storeList.setNestedScrollingEnabled(false);
        storeAdapter = new StoreAdapter(getBaseContext());
        binding.storeList.setAdapter(storeAdapter);
        storeAdapter.setItemListener(new BaseRecycleViewAdapter.RecycleViewItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                StylistCardBean.CardStoreDTOsBean bean = storeAdapter.getDatas().get(position);
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.STORE_TYPE, Constants.STORE_STYLIST_DETAIL);
                bundle.putString(Constants.STORE_ID, String.valueOf(bean.getStoreId()));
                StoreManagerActivity.startActivity(StylistDetailsActivity.this, StoreManagerActivity.class, bundle);
            }

            @Override
            public void OnItemLongClickListener(View view, int position) {

            }
        });

        // works
        binding.worksList.setLayoutManager(new GridLayoutManager(getBaseContext(), 3));
        binding.worksList.addItemDecoration(new GridSpacingItemDecoration(3, 20, false));
        binding.worksList.setHasFixedSize(true);
        binding.worksList.setNestedScrollingEnabled(false);
        worksAdapter = new WorksAdapter(getBaseContext());
        binding.worksList.setAdapter(worksAdapter);

        binding.ratingStylist.setOnTouchListener((v, event) -> true);
        binding.ratingBar.setOnTouchListener((v, event) -> true);
    }

    private void loadData() {
        getMvpPresenter().getStylistCardList("0", AccountManager.getInstance().getStoreId(), stylistId);
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageView(this, 0, null);
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(message);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_collection://喜欢
                //0是取消收藏1收藏
                getMvpPresenter().getStoreCollection(isCollection ? 0 : 1, stylistId, this);
                break;
            case R.id.iv_share://分享
                new Thread() {
                    public void run() {
                        showShareDialog();
                    }
                }.start();
                break;
            case R.id.iv_look_more: // 查看更多作品
                if (stylistCardBean != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.STYLIST_ID, stylistId);
                    bundle.putString(Constants.HEAD_PORTRAIT, stylistCardBean.getHeadPortrait());
                    bundle.putString(Constants.NICK_NAME, stylistCardBean.getNickname());
                    ManyWorksActivity.startActivity(getBaseContext(), ManyWorksActivity.class, bundle);
                }
                break;
            case R.id.tv_look_comment: // 查看更多评论
                Bundle EvaluationBundle = new Bundle();
                EvaluationBundle.putInt(Constants.EVALUATION_TYPE, 3);
                EvaluationBundle.putString(Constants.STYLIST_ID, stylistId);
                EvaluationManagerActivity.startActivity(this, EvaluationManagerActivity.class, EvaluationBundle);
                break;
            case R.id.btn_tell_phone: // 电话
                PhoneUtil.call(this, stylistCardBean.getMobile());
                break;
            case R.id.btn_send_msg: // 咨询
                if (stylistCardBean != null) {
                    ChatActivity.startFromZIxunActivity(StylistDetailsActivity.this, stylistCardBean.getImusername(), stylistCardBean.getUserId() + "", stylistCardBean.getNickname(), stylistCardBean.getHeadPortrait());
                }
                break;
            case R.id.btn_comment_invite: // 解约
                if (stylistCardBean.getNexus() == 2) {//邀请加入
                    getMvpPresenter().checkStoreAuth(AccountManager.getInstance().getStoreId());
                } else {
                    showJYDialog();
                }
                break;
        }
    }

    @Override
    public void getStylistCardListSucceed(StylistCardBean stylistCardBean) {
        this.stylistCardBean = stylistCardBean;
        if (stylistCardBean != null) {
            if (stylistCardBean.getNexus() == 2) {
                binding.btnCommentInvite.setText(getString(R.string.invite_join));
            }
            isCollection = stylistCardBean.isIsCollection();
            if (isCollection) {
                binding.ivCollection.setImageResource(R.drawable.icon_collection_true);
            } else {
                binding.ivCollection.setImageResource(R.drawable.icon_collection);
            }
            // 头像
            ImageLoaderConfig config = new ImageLoaderConfig.Builder()
                    .setErrorResId(R.drawable.icon_head_pic_def)
                    .setPlaceHolderResId(R.drawable.icon_head_pic_def)
                    .setAsBitmap(true)
                    .setCropType(ImageLoaderConfig.CENTER_INSIDE)
                    .setDiskCacheStrategy(ImageLoaderConfig.DiskCache.SOURCE)
                    .setPrioriy(ImageLoaderConfig.LoadPriority.HIGH)
                    .build();
            ImageLoader.loadImage(binding.ivPhoto, stylistCardBean.getHeadPortrait(), config, null);
            // 背景
            ImageLoaderConfig configBg = new ImageLoaderConfig.Builder()
                    .setErrorResId(R.drawable.icon_head_pic_def)
                    .setPlaceHolderResId(R.drawable.icon_head_pic_def)
                    .setAsBitmap(true)
                    .setDiskCacheStrategy(ImageLoaderConfig.DiskCache.SOURCE)
                    .setPrioriy(ImageLoaderConfig.LoadPriority.HIGH)
                    .build();
            ImageLoader.loadImage(binding.ivBg, stylistCardBean.getBackGroundImg(), configBg, null);
            binding.tvName.setText(stylistCardBean.getNickname());
            int gender = stylistCardBean.getGender();
            if (gender == 2) {//1 男 2 女 3 人妖 ,
                binding.ivGender.setImageResource(R.drawable.icon_girl);//女
            } else {
                binding.ivGender.setImageResource(R.drawable.icon_boy);//男
            }
            binding.tvLabel.setText(stylistCardBean.getLable());
            binding.tvServiceType.setText("服务类别：" + stylistCardBean.getServerTypes());
            binding.ratingStylist.setRating(stylistCardBean.getStar());
            binding.ratingBar.setRating(stylistCardBean.getStar());
            binding.ratingNum.setText(stylistCardBean.getStar() + "");
            binding.tvSkillScore.setText(String.format(getString(R.string.comment_score_skill), FormatUtil.Formatstring(String.valueOf(stylistCardBean.getCardGradeDTOs().get(1).getPoint()))));
            binding.tvServiceScore.setText(String.format(getString(R.string.comment_score_service), FormatUtil.Formatstring(String.valueOf(stylistCardBean.getCardGradeDTOs().get(0).getPoint()))));
            binding.tvLookComment.setText(String.format(getString(R.string.comment_look_all), FormatUtil.Formatstring(String.valueOf(stylistCardBean.getEvaluatenumer()))));
            binding.tvLabel.setText(FormatUtil.Formatstring(stylistCardBean.getYearbirth()) + "/" + stylistCardBean.getPosition());

            if (stylistCardBean.getCardServerItems() != null && stylistCardBean.getCardServerItems().size() > 0) {
                projectBeans.clear();
                projectBeans.addAll(stylistCardBean.getCardServerItems());
                projectAdapter.setDatas(projectBeans, true);
            }
            if (stylistCardBean.getCardStoreDTOs() != null && stylistCardBean.getCardStoreDTOs().size() > 0) {
                storeBeans.clear();
                storeBeans.addAll(stylistCardBean.getCardStoreDTOs());
                storeAdapter.setDatas(storeBeans, true);
            }
            if (stylistCardBean.getCardOpusDTOs() != null && stylistCardBean.getCardOpusDTOs().size() > 0) {
                worksBeans.clear();
                worksBeans.addAll(stylistCardBean.getCardOpusDTOs());
                worksAdapter.setDatas(worksBeans, true);
            }
            if (stylistCardBean.getCardPackages() != null && stylistCardBean.getCardPackages().size() > 0) {
                bundleBeans.clear();
                bundleBeans.addAll(stylistCardBean.getCardPackages());
                bundleAdapter.setDatas(bundleBeans, true);
            }

        }
    }

    @Override
    public void getStylistCardListFail() {
        showToast("获取美发师详情失败");
        finish();
    }

    /**
     * 收藏操作
     *
     * @param storeCollectionSucceed
     */
    @Override
    public void setStoreCollectionSucceed(StoreCollectionBean storeCollectionSucceed) {
        setResult(Constants.RESULT_REFRESH_CODE);
        loadData();
        ToastUtils.shortToast(isCollection ? "取消收藏成功" : "收藏成功");
    }

    @Override
    public void breakStoreNexusSucceed() {
        setResult(Constants.RESULT_REFRESH_CODE);
        ToastUtils.shortToast("解约成功");
        finish();
    }

    @Override
    public void breakStoreNexusFail() {
        ToastUtils.shortToast("解约失败");

    }

    @Override
    public void nexusSuccess(boolean isAgree) {

    }

    @Override
    public void checkStoreAuthSuccess() {
        if (stylistCardBean != null) {
            SelfDefinedInfoBean chatNoFriendBeanInv = new SelfDefinedInfoBean();
            chatNoFriendBeanInv.setImusername(stylistCardBean.getImusername());
            chatNoFriendBeanInv.setNickname(stylistCardBean.getNickname());
            chatNoFriendBeanInv.setPath(stylistCardBean.getHeadPortrait());
            chatNoFriendBeanInv.setUserId(stylistCardBean.getUserId());


            Intent intent = new Intent(this, InviteJoinActivity.class);

            intent.putExtra(Constants.IM_SELF_BEAN, chatNoFriendBeanInv);
            intent.putExtra("stylistId", stylistCardBean.getStylistId()+"");
            startActivity(intent);
        } else {
            ToastUtils.shortToast("门店信息获取不完整");
        }
    }

    @Override
    public void checkStoreAuthFail() {
        ToastUtils.shortToast("您的门店未完成认证");
    }

    @Override
    public void findReCodeSuc(ReCodeBean reCode) {
        inviteCode = reCode.getInvitecode();
    }

    @Override
    public void checkMsg(CheckMsgResult checkMsgResult) {

    }

    //解约弹出框
    private void showJYDialog() {
        new YLDialog.Builder(this)
                .setType(YLDialog.DIALOG_TYPE_NORMAL)
                .setMessage("是否确认与美发师解约")
                .setPositiveButton("确定", (dialog, which) -> {
                    getMvpPresenter().breakStoreNexus(nexus, AccountManager.getInstance().getStoreId(), stylistId);
                    dialog.dismiss();
                })
                .setNegativeButton("取消", (dialog, which) -> {
                    dialog.dismiss();
                })
                .create()
                .show();
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
                                    getResources().getString(R.string.dialog_share_title_appteacher),
                                    getShareParam(),
                                    getResources().getString(R.string.dialog_share_content),
                                    stylistCardBean.getHeadPortrait(),
                                    platformActionListener);
                            dialog.dismiss();
                        });
                        holder.getView(R.id.ll_share_wechatmoments).setOnClickListener(v -> {
                            ShareUtils.shareWechatMoments(
                                    getResources().getString(R.string.dialog_share_title_appteacher),
                                    getShareParam(),
                                    getResources().getString(R.string.dialog_share_content),
                                    stylistCardBean.getHeadPortrait(),
                                    platformActionListener);
                            dialog.dismiss();
                        });
                        holder.getView(R.id.ll_share_qq).setOnClickListener(v -> {
                            ShareUtils.shareQQ(
                                    getResources().getString(R.string.dialog_share_title_appteacher),
                                    getShareParam(),
                                    getResources().getString(R.string.dialog_share_content),
                                    stylistCardBean.getHeadPortrait(),
                                    platformActionListener);
                            dialog.dismiss();
                        });
                        holder.getView(R.id.ll_share_friend).setOnClickListener(v -> {
                            //分享的店铺的相关信息传递 没有传null 此页面门店的storeId必传
                            if (stylistCardBean != null) {
                                ShareToFriendActivity.startShareToFriendActivity(
                                        StylistDetailsActivity.this,
                                        103,
                                        stylistCardBean.getImusername(),
                                        stylistCardBean.getNickname(),
                                        stylistCardBean.getStylistId() + "",
                                        stylistCardBean.getUserId() + "",
                                        stylistCardBean.getHeadPortrait(),
                                        stylistCardBean.getYearbirth());
                            }
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
            param.append("&").append(Constants.WEB_STYLIST_ID).append(stylistId);
            param.append("&").append(Constants.WEB_NICKNAME).append(StringUtil.baseConvertStr(eName));
//            param.append("&").append(Constants.WEB_OPUS_ID).append("");
//            param.append("&").append(Constants.WEB_STORE_ID).append("");
        } else {
            param.append("?").append(Constants.WEB_STYLIST_ID).append(stylistId);
            param.append("&").append(Constants.WEB_NICKNAME).append(StringUtil.baseConvertStr(eName));
//            param.append("?").append(Constants.WEB_OPUS_ID).append("");
//            param.append("?").append(Constants.WEB_STORE_ID).append("");
        }

        return Constants.WEB_HAIRDRESSER_DETAILS + param.toString();
    }

}
