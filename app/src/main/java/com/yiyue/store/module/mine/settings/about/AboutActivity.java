package com.yiyue.store.module.mine.settings.about;

import android.graphics.Paint;
import android.view.View;

import com.yiyue.store.BuildConfig;
import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.component.databind.ClickHandler;
import com.yiyue.store.databinding.ActivityAboutBinding;
import com.yiyue.store.model.vo.bean.AppInfoBean;
import com.yiyue.store.util.FilePathUtil;
import com.yl.core.component.appupdate.config.UpdateConfiguration;
import com.yl.core.component.appupdate.manager.DownloadManager;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;

/**
 * Created by lvlong on 2018/10/8.
 *
 * 关于意约
 */
@CreatePresenter(AboutPresenter.class)
public class AboutActivity extends BaseMvpAppCompatActivity<IAboutView, AboutPresenter> implements IAboutView,ClickHandler {

    ActivityAboutBinding mBinding;
    private int newAppVersionCode,isUpdate;
    private String newAppVersionName,appUrl,appDescribe ;
    private AppInfoBean appInfoBeans;
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_about;
    }

    @Override
    protected void init() {
        StatusBarUtil.setLightMode(this);
        mBinding = (ActivityAboutBinding) viewDataBinding;
        mBinding.setClick(this);

        setTitleView();

        setVersionCode();

    }

    private void setVersionCode() {
        getMvpPresenter().getAppInfo(this);
        //设置立即更新的下划线
        mBinding.tvImmediatelyUpdate.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mBinding.tvCurrentVersionCode.setText("("+ BuildConfig.VERSION_NAME+")");
    }

    private void setTitleView() {

        mBinding.titleView.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.tv_immediately_update:

                if (null == appInfoBeans) return;
                if (appInfoBeans.getIsUpdate() == 0) return; // 0，不升级
                // 是否强制升级
                boolean forcedUpgrade = (appInfoBeans.getIsUpdate() == 1 ? false : true);
                UpdateConfiguration configuration = new UpdateConfiguration()
                        //下载完成自动跳动安装页面
                        .setJumpInstallPage(true)
                        //支持断点下载
                        .setBreakpointDownload(true)
                        //设置是否显示通知栏进度
                        .setShowNotification(true)
                        //设置强制更新
                        .setForcedUpgrade(forcedUpgrade);

                DownloadManager manager = DownloadManager.getInstance();
                manager.init(this)
                        .setApkName(appInfoBeans.getAppName() + ".apk")
                        .setApkUrl(appInfoBeans.getAppUrl())
                        .setSmallIcon(R.mipmap.logo)
                        .setShowNewerToast(true)
                        .setConfiguration(configuration)
                        .setDownloadPath(FilePathUtil.getAppUpdatePath())
                        .setApkVersionCode(appInfoBeans.getNewAppVersionCode())
                        .setApkVersionName(appInfoBeans.getNewAppVersionName())
                        .setApkSize(appInfoBeans.getAppSize())
                        .setApkDescription(appInfoBeans.getAppDescribe())
                        .download();

                break;

        }

    }

    @Override
    public void showToast(String message) {

    }

    @Override
    public void onAppInfoSuccess(AppInfoBean appInfoBean) {
        if (appInfoBean!=null){
            appInfoBeans = appInfoBean;
            int newAppVersionCode = Integer.valueOf(appInfoBean.getNewAppVersionName().replace(".", ""));
            isUpdate = appInfoBean.getIsUpdate();
            newAppVersionName = appInfoBean.getNewAppVersionName();
            appUrl = appInfoBean.getAppUrl();
            appDescribe = appInfoBean.getAppDescribe();
            if (newAppVersionCode>BuildConfig.VERSION_CODE){
                mBinding.llHaveNewVersion.setVisibility(View.VISIBLE);
                mBinding.llNotNewVersion.setVisibility(View.GONE);
                mBinding.tvNewVersionCode.setText(appInfoBean.getNewAppVersionName());
            }else {
                mBinding.llNotNewVersion.setVisibility(View.VISIBLE);
                mBinding.llHaveNewVersion.setVisibility(View.GONE);
            }
        }
    }
}
