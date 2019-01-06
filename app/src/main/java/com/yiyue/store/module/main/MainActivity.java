package com.yiyue.store.module.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiyue.store.R;
import com.yiyue.store.YLApplication;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.component.push.AliPushManager;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivityMainBinding;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.helper.AppManager;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.vo.bean.AppInfoBean;
import com.yiyue.store.module.home.HomeFragment;
import com.yiyue.store.module.home.evaluation.EvaluationManagerActivity;
import com.yiyue.store.module.home.order.details.OrderDetailsActivity;
import com.yiyue.store.module.im.IMFragment;
import com.yiyue.store.module.im.imlogin.ImLoginManager;
import com.yiyue.store.module.mall.MallFragment;
import com.yiyue.store.module.mine.MineFragment;
import com.yiyue.store.util.FilePathUtil;
import com.yiyue.store.util.NotificationsEnabledUtils;
import com.yiyue.store.widget.dialog.YLDialog;
import com.yl.core.component.appupdate.config.UpdateConfiguration;
import com.yl.core.component.appupdate.manager.DownloadManager;
import com.yl.core.component.log.DLog;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * 主页
 * <p>
 * Create by zm on 2018/9/19
 */
@CreatePresenter(MainPresenter.class)
public class MainActivity extends BaseMvpAppCompatActivity<IMainView, MainPresenter> implements IMainView {
    private static final String EXTRA_PAGE = "page";
    public static final int HOME = 0;
    public static final int MALL = 1;
    public static final int IM = 2;
    public static final int ME = 3;
    @IntDef({HOME, MALL, IM, ME})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Page {
    }

    private static long exitTime;
    @Page
    int page = HOME;
    private final List<TabMenu> tabMenus = new ArrayList<>();

    {
        tabMenus.add(new TabMenu(R.drawable.tab_home_selector, R.string.label_title_home, HomeFragment.newInstance()));
        tabMenus.add(new TabMenu(R.drawable.tab_mall_selector, R.string.label_title_mall, MallFragment.newInstance()));
        tabMenus.add(new TabMenu(R.drawable.tab_im_selector, R.string.label_title_msg, IMFragment.newInstance()));
        tabMenus.add(new TabMenu(R.drawable.tab_mine_selector, R.string.label_title_mine, MineFragment.newInstance()));
    }

    /**
     * start activity
     *
     * @param context
     * @param page    {@link #HOME,#ME,#IM}
     */
    public static void startActivity(Context context, @Page int page) {
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_PAGE, page);
        startActivity(context, MainActivity.class, bundle);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        hasExtras();
        initView();
        loadData();

        ImLoginManager.getInstance().loginIM();
        AliPushManager.getInstance().bindAccount(AccountManager.getInstance().getAccount().getImusername());
        initNotifications();
    }

    private void loadData() {
        getMvpPresenter().getAppInfo(this);
    }

    private void hasExtras() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.page = bundle.getInt(EXTRA_PAGE, HOME);
            String orderId = bundle.getString(Constants.NOTIFICATION_DATA);
            String msgType = bundle.getString(Constants.NOTIFICATION_MSG_TYPE);
            if (orderId!=null&&!TextUtils.isEmpty(orderId)){
                if (msgType.equals("15")){
                    Bundle EvaluationBundle = new Bundle();
                    EvaluationBundle.putInt(Constants.EVALUATION_TYPE, 1);
                    EvaluationManagerActivity.startActivity(this , EvaluationManagerActivity.class ,EvaluationBundle);
                }else {
                    OrderDetailsActivity.startActivity(YLApplication.getContext(),orderId);
                }
            }
        }
    }

    private void initNotifications() {
        if (!NotificationsEnabledUtils.areNotificationsEnabled(this)){//未打开通知栏
            new YLDialog.Builder(this)
                    .setTitle("通知栏权限提示")
                    .setMessage("检测到您还没有打开通知栏权限是否去打开")
                    .setPositiveButton("去打开", (dialog, which) -> {
                        NotificationsEnabledUtils.openNotificationSetting(this);
                        dialog.dismiss();
                    }).setNegativeButton("暂不打开", (dialog, which) -> dialog.dismiss())
                    .create().show();
        }
    }

    private void initView() {
        ActivityMainBinding mainBinding = (ActivityMainBinding) viewDataBinding;

        setTabs(mainBinding.tableLayout, getLayoutInflater());
        mainBinding.viewPage.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return tabMenus.get(position).fragment;
            }

            @Override
            public int getCount() {
                return tabMenus.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
//                super.destroyItem(container, position, object);
            }
        });
        mainBinding.viewPage.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mainBinding.tableLayout));
        mainBinding.tableLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mainBinding.viewPage) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // 取消viewPage切换动画
                mainBinding.viewPage.setCurrentItem(tab.getPosition(), false);
                switch (tab.getPosition()) {
                    case HOME:
                        StatusBarUtil.setFullDarkMode(MainActivity.this);
                        break;
                    case MALL:
                        StatusBarUtil.setFullLightMode(MainActivity.this);
                        break;
                    case IM:
                        StatusBarUtil.setFullLightMode(MainActivity.this);
                        break;
                    case ME:
                        StatusBarUtil.setFullDarkMode(MainActivity.this);
                        break;
                }
            }
        });
        mainBinding.viewPage.setCurrentItem(page, false);
        // 是否允许viewPage滑动切换
        mainBinding.viewPage.setScanScroll(false);
        // viewPage预加载1个页面
        mainBinding.viewPage.setOffscreenPageLimit(4);
    }

    /**
     * 设置Tab
     *
     * @param tabLayout
     * @param inflater
     */
    private void setTabs(TabLayout tabLayout, LayoutInflater inflater) {
        for (TabMenu tabMenu : tabMenus) {
            TabLayout.Tab tab = tabLayout.newTab();
            View tabView = inflater.inflate(R.layout.tab_layout_main, null);
            tab.setCustomView(tabView);

            ImageView imgTab = tabView.findViewById(R.id.img_tab);
            TextView tvTitle = tabView.findViewById(R.id.tv_title);
            imgTab.setImageResource(tabMenu.icon);
            tvTitle.setText(getString(tabMenu.title));
            tabLayout.addTab(tab);
        }
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(message);
    }

    private class TabMenu {
        @DrawableRes
        private int icon;
        @StringRes
        private int title;
        private Fragment fragment;

        public TabMenu(@DrawableRes int icon, @StringRes int title, Fragment fragment) {
            this.icon = icon;
            this.fragment = fragment;
            this.title = title;
        }
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(MainActivity.this, 0, null);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == 0) {
            onBackPressed(this);
            try {
                moveTaskToBack(false);
            } catch (Exception e) {
                DLog.e("返回键模拟HOME出错", e);
                return super.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 防止华为机型未加入白名单时按返回键回到桌面再锁屏后几秒钟进程被杀
     */
    private void onBackPressed(Activity a) {
        try {
            Intent launcherIntent = new Intent(Intent.ACTION_MAIN);
            launcherIntent.addCategory(Intent.CATEGORY_HOME);
            a.startActivity(launcherIntent);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateAppInfo(AppInfoBean infoBean) {
        if (null == infoBean) return;
        if (infoBean.getIsUpdate() == 0) return; // 0，不升级
        // 是否强制升级
        boolean forcedUpgrade = (infoBean.getIsUpdate() == 1 ? false : true);
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
                .setApkName(infoBean.getAppName() + ".apk")
                .setApkUrl(infoBean.getAppUrl())
                .setSmallIcon(R.mipmap.logo)
                .setShowNewerToast(true)
                .setConfiguration(configuration)
                .setDownloadPath(FilePathUtil.getAppUpdatePath())
                .setApkVersionCode(infoBean.getNewAppVersionCode())
                .setApkVersionName(infoBean.getNewAppVersionName())
                .setApkSize(infoBean.getAppSize())
                .setApkDescription(infoBean.getAppDescribe())
                .download();
    }
}
