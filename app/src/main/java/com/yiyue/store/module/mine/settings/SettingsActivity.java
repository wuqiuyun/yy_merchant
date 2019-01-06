package com.yiyue.store.module.mine.settings;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import com.yiyue.store.BuildConfig;
import com.yiyue.store.R;
import com.yiyue.store.YLApplication;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.component.databind.ClickHandler;
import com.yiyue.store.component.greendao.DaoManager;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivitySettingsBinding;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.vo.bean.ReCodeBean;
import com.yiyue.store.module.mine.settings.about.AboutActivity;
import com.yiyue.store.module.mine.settings.feedback.FeedbackActivity;
import com.yiyue.store.module.mine.settings.security.SecurityActivity;
import com.yiyue.store.util.DataCleanManager;
import com.yiyue.store.util.ShareUtils;
import com.yiyue.store.util.StringUtil;
import com.yiyue.store.widget.dialog.BaseEasyDialog;
import com.yiyue.store.widget.dialog.EasyDialog;
import com.yiyue.store.widget.dialog.ViewConvertListener;
import com.yiyue.store.widget.dialog.ViewHolder;
import com.yiyue.store.widget.dialog.YLDialog;
import com.yl.core.component.log.DLog;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;

import java.net.URLEncoder;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

/**
 * 设置
 * <p>
 * Create by zm on 2018/9/29
 */
@CreatePresenter(ISettingsPresenter.class)
public class SettingsActivity extends BaseMvpAppCompatActivity<ISettingsView, ISettingsPresenter> implements ClickHandler, ISettingsView {

    private ActivitySettingsBinding mBinding;
    private boolean isShow = true;//显示消息是否
    private int shutdown;//0通知，1不通知
    private String inviteCode = null;//邀请码

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_settings;
    }

    @Override
    protected void init() {

        initView();

        //设置消息通知的开关
        setSwitch();

        //获取我的推荐码
        getMvpPresenter().findReCode();
    }


    private void initView() {
        StatusBarUtil.setLightMode(this);
        mBinding = (ActivitySettingsBinding) viewDataBinding;
        // 返回
        mBinding.titleView.setLeftClickListener(v -> finish());
        mBinding.setClick(this);
        mBinding.tvCache.setText("(" + DataCleanManager.getTotalCacheSize(this) + ")");
        mBinding.tvVersionCode.setText("V" + BuildConfig.VERSION_NAME);
    }


    private void setSwitch() {
        isShow = AccountManager.getInstance().getUserShutdown() == 0;
        mBinding.imgMessageSwitch.setSelected(isShow);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.img_message_switch: //选择打开关闭message
                mBinding.imgMessageSwitch.setSelected(!mBinding.imgMessageSwitch.isSelected());
                if (mBinding.imgMessageSwitch.isSelected()) {
                    shutdown = 0;
                } else {
                    shutdown = 1;
                }
                getMvpPresenter().changeNotice(shutdown);
                break;

            case R.id.rl_account_security: //账号安全管理
                startActivity(SettingsActivity.this, SecurityActivity.class);
                break;

            case R.id.rl_about: //关于意约
                startActivity(this, AboutActivity.class);
                break;

            case R.id.rl_experience_feedback: //体验反馈
                startActivity(this, FeedbackActivity.class);
                break;

            case R.id.rl_share: //分享
                new Thread() {
                    public void run() {
                        showShareDialog();
                    }
                }.start();
                break;

            case R.id.ll_clean_cache: //清理缓存
                showDLDialog();
                break;

            case R.id.tv_exit: //退出当前账号
                showLogoutDialog();
                break;
        }
    }

    private void showDLDialog() {
        new YLDialog.Builder(this)
                .setType(YLDialog.DIALOG_TYPE_NORMAL)
                .setMessage("是否清除缓存")
                .setPositiveButton("确定", (dialog, which) -> {
                    new DaoManager().dropTables(YLApplication.getContext(), AccountManager.getInstance().getUserId(), true);//清空数据库表
                    DataCleanManager.clearAllCache(this, mBinding.tvCache);
                    dialog.dismiss();
                })
                .setNegativeButton("取消", (dialog, which) -> {
                    dialog.dismiss();
                })
                .create()
                .show();
    }

    private void showLogoutDialog() {
        new YLDialog.Builder(this)
                .setType(YLDialog.DIALOG_TYPE_NORMAL)
                .setMessage("是否退出当前帐号")
                .setPositiveButton("确定", (dialog, which) -> {
                    AccountManager.getInstance().insertingLogout();
                    dialog.dismiss();
                })
                .setNegativeButton("取消", (dialog, which) -> {
                    dialog.dismiss();
                })
                .create()
                .show();
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
                        holder.getView(R.id.tv_share_cancel).setOnClickListener(v -> {
                            dialog.dismiss();
                        });
                        holder.getView(R.id.ll_share_wechat).setOnClickListener(v -> {
                            ShareUtils.shareWechat(
                                    getResources().getString(R.string.dialog_share_title_appset),
                                    getShareParam(),
                                    getResources().getString(R.string.dialog_share_content),
                                    AccountManager.getInstance().getUserHeadImg(),
                                    platformActionListener);
                            DLog.e("MBLog",getShareParam());
                            dialog.dismiss();
                        });
                        holder.getView(R.id.ll_share_wechatmoments).setOnClickListener(v -> {
                            ShareUtils.shareWechatMoments(
                                    getResources().getString(R.string.dialog_share_title_appset),
                                    getShareParam(),
                                    getResources().getString(R.string.dialog_share_content),
                                    AccountManager.getInstance().getUserHeadImg(),
                                    platformActionListener);
                            dialog.dismiss();
                        });
                        holder.getView(R.id.ll_share_qq).setOnClickListener(v -> {
                            ShareUtils.shareQQ(
                                    getResources().getString(R.string.dialog_share_title_appset),
                                    getShareParam(),
                                    getResources().getString(R.string.dialog_share_content),
                                    AccountManager.getInstance().getUserHeadImg(),
                                    platformActionListener);
                            dialog.dismiss();
                        });
                        holder.getView(R.id.ll_share_friend).setVisibility(View.GONE);
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


    @Override
    public void changeNoticeSuc() {
        if (shutdown == 0) {
            showToast("新消息通知已打开");
        } else {
            showToast("新消息通知已关闭");
        }
        //更新本地状态
        AccountManager.getInstance().setUserShutdown(shutdown);
    }

    @Override
    public void findReCodeSuc(ReCodeBean recode) {
        inviteCode = recode.getInvitecode();
    }

    //生成分享附加参数
    private String getShareParam() {
        StringBuilder param = new StringBuilder();//分享附加参数
        String eName = AccountManager.getInstance().getNickName();
        //邀请码不为空
        if (!TextUtils.isEmpty(inviteCode)) {
            param.append("?").append(Constants.WEB_CODE).append(inviteCode);
            param.append("&").append(Constants.WEB_NICKNAME).append(StringUtil.baseConvertStr(eName));
//            param.append("&").append(Constants.WEB_OPUS_ID).append("");
//            param.append("&").append(Constants.WEB_STORE_ID).append("");
//            param.append("&").append(Constants.WEB_STYLIST_ID).append("");
        } else {
            param.append("?").append(Constants.WEB_NICKNAME).append(StringUtil.baseConvertStr(eName));
//            param.append("?").append(Constants.WEB_OPUS_ID).append("");
//            param.append("?").append(Constants.WEB_STORE_ID).append("");
//            param.append("?").append(Constants.WEB_STYLIST_ID).append("");
        }
        
        return Constants.WEB_REGISTER + param.toString();
    }
}
