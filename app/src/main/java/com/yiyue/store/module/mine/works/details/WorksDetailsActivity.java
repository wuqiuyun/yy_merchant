package com.yiyue.store.module.mine.works.details;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;

import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivityWorksDetailsBinding;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.vo.bean.OpusDetailBean;
import com.yiyue.store.model.vo.bean.ReCodeBean;
import com.yiyue.store.module.im.sharetofriend.ShareToFriendActivity;
import com.yiyue.store.module.mine.stylist.details.StylistDetailsActivity;
import com.yiyue.store.util.ShareUtils;
import com.yiyue.store.util.StringUtil;
import com.yiyue.store.widget.dialog.BaseEasyDialog;
import com.yiyue.store.widget.dialog.EasyDialog;
import com.yiyue.store.widget.dialog.ViewConvertListener;
import com.yiyue.store.widget.dialog.ViewHolder;
import com.yl.core.component.log.DLog;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

/**
 * 作品详情
 * <p>
 * Create by zm on 2018/10/12Ø
 */

@CreatePresenter(WorksDetailsPresenter.class)
public class WorksDetailsActivity extends BaseMvpAppCompatActivity<IWorksDetailsView, WorksDetailsPresenter> implements IWorksDetailsView{

    ActivityWorksDetailsBinding mBinding;

    private WorksPageAdapter mWorksPageAdapter;
    private boolean isCollection = false;//是否已收藏
    private List<String> pictures;//作品图片集
    
    private String userId;//用户id
    private String opusId; //作品id
    private OpusDetailBean mDetail;//保存的作品详情bean
    private String headPortrait = "";//头像
    private String nickName = "";//昵称

    private String inviteCode = null;//邀请码

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_works_details;
    }

    @Override
    protected void init() {

        initView();
        loadData();
    }

    private void initView() {
        StatusBarUtil.setLightMode(this);
        mBinding = (ActivityWorksDetailsBinding) viewDataBinding;
        // titleview
        mBinding.titleView.setLeftClickListener(v -> {
            finish();
        });
        mBinding.titleView.setRightImgClickListener(v -> {
            showShareDialog();
        });
        mBinding.titleView.setSubRightImgClickListener(v -> {
            if (TextUtils.isEmpty(opusId)) {
                return;
            }
            else {
                int type = isCollection? 0:1;
                getMvpPresenter().oupsCollection(opusId, type, userId);
            }
        });
        
        mBinding.viewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                
            }

            @Override
            public void onPageSelected(int position) {
                if (pictures.size() == 0){
                    return;
                }
                mBinding.tvPageNum.setText(String.format(getString(R.string.opus_page), position+1, pictures.size()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                
            }
        });
    }

    private void loadData() {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            opusId = bundle.getString(Constants.OPUS_ID);
            this.headPortrait = bundle.getString(Constants.HEAD_PORTRAIT);
            this.nickName = bundle.getString(Constants.NICK_NAME);
        } else {
            showToast("获取作品信息出错");
            finish();
            return;
        }
        userId = AccountManager.getInstance().getUserId();
        getMvpPresenter().getOpusDetail(opusId);
        getMvpPresenter().findReCode();
    }

    @Override
    public void getDetailSuccess(OpusDetailBean detail) {
        this.mDetail = detail;
        pictures = detail.getPictrue();
        mBinding.tvDesc.setText(detail.getDescribe());
        mBinding.tvCollectionNum.setText(String.valueOf(detail.getCollection()));
        mBinding.tvForwardNum.setText(String.valueOf(detail.getReposted()));
        mBinding.tvLookNum.setText(String.valueOf(detail.getPageview()));
        mBinding.tvPageNum.setText(String.format(getString(R.string.opus_page), 1, pictures.size()));
        isCollection = detail.isCollection();

        if (isCollection) { //取消收藏
            mBinding.titleView.setSubRightIcon(getResources().getDrawable(R.drawable.icon_collection_true));
        } else {
            mBinding.titleView.setSubRightIcon(getResources().getDrawable(R.drawable.icon_collection));
        }
        mWorksPageAdapter = new WorksPageAdapter(getBaseContext(), pictures);
        mBinding.viewPage.setAdapter(mWorksPageAdapter);
        mBinding.viewPage.setCurrentItem(0);
    }

    @Override
    public void getDetailFail() {
        showToast("获取作品详情失败了");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void collectSuccess() {
        setResult(Constants.RESULT_REFRESH_CODE);
        if (isCollection) {
            showToast("取消收藏成功");
            mBinding.titleView.setSubRightIcon(getResources().getDrawable(R.drawable.icon_collection));
            isCollection = false;
            mBinding.titleView.setSubRightIcon(getDrawable(R.drawable.icon_collection));
        } else {
            showToast("收藏成功");
            mBinding.titleView.setSubRightIcon(getResources().getDrawable(R.drawable.icon_collection_true));
            isCollection = true;
            mBinding.titleView.setSubRightIcon(getDrawable(R.drawable.icon_collection_true));
        }
    }

    @Override
    public void collectFail() {
        if (isCollection) {
            showToast("取消收藏失败");
        } else {
            showToast("收藏失败");
        }
    }

    @Override
    public void findReCodeSuc(ReCodeBean reCode) {
        inviteCode = reCode.getInvitecode();
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(message);
    }

    //分享弹框
    private void showShareDialog() {
        EasyDialog.init()
                .setLayoutId(R.layout.dialog_share)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseEasyDialog dialog) {
                        String picImg = pictures.get(mBinding.viewPage.getCurrentItem());
                        String name = String.format(getString(R.string.dialog_share_title_appworks) ,nickName);
                        holder.getView(R.id.tv_share_cancel).setOnClickListener(v -> {
                            dialog.dismiss();
                        });
                        holder.getView(R.id.ll_share_wechat).setOnClickListener(v -> {
                            ShareUtils.shareWechat(
                                    name,
                                    getShareParam(),
                                    getResources().getString(R.string.dialog_share_content),
                                    picImg,
                                    platformActionListener);
                            dialog.dismiss();
                        });
                        holder.getView(R.id.ll_share_wechatmoments).setOnClickListener(v -> {
                            ShareUtils.shareWechatMoments(
                                    name,
                                    getShareParam(),
                                    getResources().getString(R.string.dialog_share_content),
                                    picImg,
                                    platformActionListener);
                            dialog.dismiss();
                        });
                        holder.getView(R.id.ll_share_qq).setOnClickListener(v -> {
                            ShareUtils.shareQQ(
                                    name,
                                    getShareParam(),
                                    getResources().getString(R.string.dialog_share_content),
                                    picImg,
                                    platformActionListener);
                            dialog.dismiss();
                        });
                        holder.getView(R.id.ll_share_friend).setOnClickListener(v -> {
//分享的店铺的相关信息传递 没有传null 此页面门店的storeId必传
                            if (mDetail!=null) {
                                ShareToFriendActivity.startShareToFriendActivity(
                                        WorksDetailsActivity.this,
                                        102,
                                        "",
                                        nickName,
                                        mDetail.getOpusId() + "",
                                        "",
                                        headPortrait,
                                        mDetail.getDescribe());
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
    PlatformActionListener platformActionListener=new PlatformActionListener() {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            DLog.e("kid","分享成功");
//            getMvpPresenter().opusRepost(opusId);//增加分享数
        }
        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            DLog.e("kid","分享失败");
        }
        @Override
        public void onCancel(Platform platform, int i) {
            DLog.e("kid","分享取消");
        }
    };

    //生成分享附加参数
    private String getShareParam() {
        StringBuilder param = new StringBuilder();//分享附加参数
        String eName = AccountManager.getInstance().getNickName();
        //邀请码不为空
        if (!TextUtils.isEmpty(inviteCode)) {
            param.append("?").append(Constants.WEB_CODE).append(inviteCode);
            param.append("&").append(Constants.WEB_OPUS_ID).append(opusId);
            param.append("&").append(Constants.WEB_NICKNAME).append(StringUtil.baseConvertStr(eName));
//            param.append("&").append(Constants.WEB_STYLIST_ID).append(stylistId);
//            param.append("&").append(Constants.WEB_STORE_ID).append("");
        } else {
            param.append("?").append(Constants.WEB_OPUS_ID).append(opusId);
            param.append("&").append(Constants.WEB_NICKNAME).append(StringUtil.baseConvertStr(eName));
//            param.append("?").append(Constants.WEB_STYLIST_ID).append(stylistId);
//            param.append("?").append(Constants.WEB_STORE_ID).append("");
        }

        return Constants.WEB_WORK_DETAILS + param.toString();
    }
}
