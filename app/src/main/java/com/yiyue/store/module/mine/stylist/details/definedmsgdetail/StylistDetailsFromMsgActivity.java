package com.yiyue.store.module.mine.stylist.details.definedmsgdetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import com.hyphenate.chat.EMMessage;
import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.component.databind.ClickHandler;
import com.yiyue.store.component.recycleview.GridSpacingItemDecoration;
import com.yiyue.store.component.recycleview.SpaceItemDecoration;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivityStylistDetailsFromMsgBinding;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.vo.bean.ChatAdapterItemTypeBean;
import com.yiyue.store.model.vo.bean.CheckMsgBean;
import com.yiyue.store.model.vo.bean.EventBean;
import com.yiyue.store.model.vo.bean.ReCodeBean;
import com.yiyue.store.model.vo.bean.SelfDefinedInfoBean;
import com.yiyue.store.model.vo.bean.StoreCollectionBean;
import com.yiyue.store.model.vo.bean.StylistCardBean;
import com.yiyue.store.model.vo.result.CheckMsgResult;
import com.yiyue.store.module.home.evaluation.EvaluationManagerActivity;
import com.yiyue.store.module.im.sharetofriend.ShareToFriendActivity;
import com.yiyue.store.module.mine.stylist.details.IStylistDetailsView;
import com.yiyue.store.module.mine.stylist.details.ServiceBundleAdapter;
import com.yiyue.store.module.mine.stylist.details.ServiceProjectAdapter;
import com.yiyue.store.module.mine.stylist.details.StoreAdapter;
import com.yiyue.store.module.mine.stylist.details.StylistDetailsPresenter;
import com.yiyue.store.module.mine.stylist.details.WorksAdapter;
import com.yiyue.store.module.mine.stylist.invitejoin.InviteJoinActivity;
import com.yiyue.store.module.mine.works.many.ManyWorksActivity;
import com.yiyue.store.util.FormatUtil;
import com.yiyue.store.util.PhoneUtil;
import com.yiyue.store.util.ShareUtils;
import com.yiyue.store.util.StringUtil;
import com.yiyue.store.util.easyutils.EasyUtil;
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

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

/**
 * 美发师详情
 * <p>
 */
@CreatePresenter(StylistDetailsPresenter.class)
public class StylistDetailsFromMsgActivity extends BaseMvpAppCompatActivity<IStylistDetailsView, StylistDetailsPresenter>
        implements IStylistDetailsView, ClickHandler {

    ActivityStylistDetailsFromMsgBinding binding;

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
    private String storeId;

    private StylistCardBean stylistCardBean;//保存的详情页信息bean
    private String mMobile;
    private SelfDefinedInfoBean selfDefinedInfoBean;//自定义消息bean对象  用于在同意或者拒绝时再次发送自定义消息给对方

    private String inviteCode = null;//邀请码
    private String msgId;//申请记录的id, 聊天页面带入

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_stylist_details_from_msg;
    }

    @Override
    protected void init() {

        initView();
        loadData();
        getMvpPresenter().findReCode();
    }

    private void initView() {
        StatusBarUtil.setDarkMode(this);
        stylistId = getIntent().getStringExtra(Constants.STYLIST_ID);
        selfDefinedInfoBean = getIntent().getParcelableExtra(Constants.IM_SELF_BEAN);
        if (selfDefinedInfoBean!=null) {
            msgId = selfDefinedInfoBean.getEnterRecordID();
        }

        binding = (ActivityStylistDetailsFromMsgBinding) viewDataBinding;
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

        // store 入驻门店列表 ,
        binding.storeList.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        binding.storeList.addItemDecoration(new SpaceItemDecoration(20));
        binding.storeList.setHasFixedSize(true);
        binding.storeList.setNestedScrollingEnabled(false);
        storeAdapter = new StoreAdapter(getBaseContext());
        binding.storeList.setAdapter(storeAdapter);

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
            case R.id.btn_comment_invite: // 解约
                String etTextStr = binding.btnCommentInvite.getText().toString();
                if (!TextUtils.isEmpty(etTextStr) && ("已同意".equals(etTextStr) || "已拒绝".equals(etTextStr))) {
                    return;
                } else {
                    if (stylistCardBean.getNexus() == 2) {//邀请加入
                        SelfDefinedInfoBean chatNoFriendBeanInv = new SelfDefinedInfoBean();
                        chatNoFriendBeanInv.setImusername(stylistCardBean.getImusername());
                        chatNoFriendBeanInv.setNickname(stylistCardBean.getNickname());
                        chatNoFriendBeanInv.setPath(stylistCardBean.getHeadPortrait());
                        chatNoFriendBeanInv.setUserId(stylistCardBean.getUserId());

                        Intent intent = new Intent(this, InviteJoinActivity.class);

                        intent.putExtra(Constants.IM_SELF_BEAN, chatNoFriendBeanInv);
                        startActivity(intent);
                    } else {
                        showJYDialog();
                    }
                }
                break;
            case R.id.btn_comment_refuse:
                getMvpPresenter().nexus(null, AccountManager.getInstance().getUserId(), stylistCardBean.getUserId() + "", false, msgId, this);
                break;
            case R.id.btn_comment_agree:
                //111.邀请平台美发师加入 112.邀请门店美发师加入
                // 113平台美发师加入申请 114门店美发师加入申请 只有这种时候 门店端才能点击操作
                // 0 入驻 1 签约
                int nex = 0;
                if (selfDefinedInfoBean.getDefinedMsgType() == 113) {
                    nex = 0;
                } else if (selfDefinedInfoBean.getDefinedMsgType() == 114) {
                    nex = 1;
                }
                getMvpPresenter().nexus(nex + "", AccountManager.getInstance().getStoreId(), stylistCardBean.getStylistId() + "", true, msgId, this);
                break;
        }
    }

    @Override
    public void nexusSuccess(boolean isAgree) {
        if (isAgree) {
            selfDefinedInfoBean.setMsgStatus(2);//1为发送人发起加价状态 2为已回复 同意  3.为已回复 拒绝的状态
            ToastUtils.shortToast("已同意");
            binding.btnCommentInvite.setEnabled(false);
        } else {
            selfDefinedInfoBean.setMsgStatus(3);//1为发送人发起加价状态 2为已回复 同意  3.为已回复 拒绝的状态
            ToastUtils.shortToast("已拒绝");
            binding.btnCommentInvite.setEnabled(false);
        }
        loadData();
        EMMessage messageIv = null;
        String currUsername = EasyUtil.getEmManager().getCurrentUser();//获取环信本地登录账号id
        if (!selfDefinedInfoBean.getImusername().equals(currUsername)) {//如果这条消息之前不是自己发出的  现在把发出和接收方换位
            selfDefinedInfoBean.setImusername(AccountManager.getInstance().getAccount().getImusername());
            selfDefinedInfoBean.setPath(AccountManager.getInstance().getAccount().getHeadImg());
            selfDefinedInfoBean.setNickname(AccountManager.getInstance().getAccount().getNickname());
            String usid = AccountManager.getInstance().getUserId();
            if (!TextUtils.isEmpty(usid)) {
                selfDefinedInfoBean.setUserId(Long.parseLong(usid));
            }

            if (stylistCardBean != null) {
                selfDefinedInfoBean.setRecvImusername(stylistCardBean.getImusername());
                selfDefinedInfoBean.setRecvUserId(stylistCardBean.getUserId());
                selfDefinedInfoBean.setRecvPath(stylistCardBean.getHeadPortrait());
                selfDefinedInfoBean.setRecvNickname(stylistCardBean.getNickname());
            }
            messageIv = EasyUtil.getEmManager().sendOrderAddMoneyMes(selfDefinedInfoBean, selfDefinedInfoBean.getRecvImusername());
        } else {
            if (stylistCardBean != null) {
                messageIv = EasyUtil.getEmManager().sendOrderAddMoneyMes(selfDefinedInfoBean, stylistCardBean.getImusername());
            }
        }
        EventBus.getDefault().post(new EventBean.InviteMsgUpdate(0, selfDefinedInfoBean, messageIv, ChatAdapterItemTypeBean.CHAT_SEND_SHARE_MSG));
    }

    @Override
    public void checkStoreAuthSuccess() {

    }

    @Override
    public void checkStoreAuthFail() {

    }

    @Override
    public void findReCodeSuc(ReCodeBean reCode) {
        inviteCode = reCode.getInvitecode();
    }

    @Override
    public void checkMsg(CheckMsgResult checkMsgResult) {
        if (checkMsgResult != null) {
            CheckMsgBean checkMsgBean = checkMsgResult.getData();
            if (checkMsgBean != null) {
                switch (checkMsgBean.getStatus()) {//status -1 消息不存在 0未处理，1同意 2拒绝
                    case -1://老数据无id  不做处理
                        break;
                    case 0:
                        binding.layoutAgreeorrefuse.setVisibility(View.VISIBLE);
                        binding.btnCommentInvite.setVisibility(View.GONE);
                        break;
                    case 1:
                        binding.layoutAgreeorrefuse.setVisibility(View.GONE);
                        binding.btnCommentInvite.setVisibility(View.VISIBLE);
                        binding.btnCommentInvite.setText("已同意");
                        binding.btnCommentInvite.setBackgroundColor(this.getResources().getColor(R.color.color_999999));
                        break;
                    case 2:
                        binding.layoutAgreeorrefuse.setVisibility(View.GONE);
                        binding.btnCommentInvite.setVisibility(View.VISIBLE);
                        binding.btnCommentInvite.setText("已拒绝");
                        binding.btnCommentInvite.setBackgroundColor(this.getResources().getColor(R.color.color_999999));
                        break;
                }
            }
        }
    }

    @Override
    public void getStylistCardListSucceed(StylistCardBean stylistCardBean) {
        this.stylistCardBean = stylistCardBean;
        if (stylistCardBean != null) {
            isCollection = stylistCardBean.isIsCollection();
            if (isCollection) {
                binding.ivCollection.setImageResource(R.drawable.icon_collection_true);
            } else {
                binding.ivCollection.setImageResource(R.drawable.icon_collection);
            }
            ImageLoader.loadImage(binding.ivPhoto, stylistCardBean.getHeadPortrait());//头像
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
            mMobile = FormatUtil.Formatstring(stylistCardBean.getMobile());
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

            /**
             * nexus (string, optional): 入驻关系 0 入驻 1 签约，2没关系 ,

             聊天页面进入美发师详情：
             nexus为2   显示为同意、拒绝， 咨询也不显示
             nexuse为0或1  显示为解约 ， 咨询也不显示


             聊天页面进入门店详情：
             不展示底部操作栏 只看详情, 咨询、打电话 也不显示
             */

            if (stylistCardBean.getNexus() != 2) {
                binding.layoutAgreeorrefuse.setVisibility(View.GONE);
                binding.btnCommentInvite.setVisibility(View.VISIBLE);
            } else {
                binding.layoutAgreeorrefuse.setVisibility(View.VISIBLE);
                binding.btnCommentInvite.setVisibility(View.GONE);
            }
            if (selfDefinedInfoBean!=null && !TextUtils.isEmpty(selfDefinedInfoBean.getEnterRecordID())) {
                getMvpPresenter().checkMsg(selfDefinedInfoBean.getEnterRecordID(), this);//如果是聊天进入需要请求这个接口
            }
        }
    }

    @Override
    public void getStylistCardListFail() {
        ToastUtils.shortToast("加载失败!");
        finish();
    }

    @Override
    public void setStoreCollectionSucceed(StoreCollectionBean storeCollectionSucceed) {
        if (storeCollectionSucceed != null) {

        }
    }

    @Override
    public void breakStoreNexusSucceed() {
        ToastUtils.shortToast("解约成功");
        finish();
    }

    @Override
    public void breakStoreNexusFail() {
        ToastUtils.shortToast("解约失败");

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
                                        StylistDetailsFromMsgActivity.this,
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
