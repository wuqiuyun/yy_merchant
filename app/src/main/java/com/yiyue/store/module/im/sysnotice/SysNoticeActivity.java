package com.yiyue.store.module.im.sysnotice;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.Gravity;

import com.yiyue.store.R;
import com.yiyue.store.base.BaseAppCompatActivity;
import com.yiyue.store.databinding.ActivitySystemNoticeBinding;
import com.yiyue.store.widget.dialog.BaseEasyDialog;
import com.yiyue.store.widget.dialog.EasyDialog;
import com.yiyue.store.widget.dialog.ViewConvertListener;
import com.yiyue.store.widget.dialog.ViewHolder;
import com.yl.core.util.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangzz on 2018/9/28
 * 系统消息Activity
 */
public class SysNoticeActivity extends BaseAppCompatActivity {
    ActivitySystemNoticeBinding mBinding;
    private List<String> mTitles;
    private List<Fragment> mFragments;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_system_notice;
    }

    @Override
    protected void init() {
        StatusBarUtil.setLightMode(this);
        mBinding = (ActivitySystemNoticeBinding) viewDataBinding;
        mTitles = new ArrayList<>();
        mTitles.add(getString(R.string.system_order_title));
        mTitles.add(getString(R.string.interact_msg));
        mTitles.add(getString(R.string.system_notice_title));

        mFragments = new ArrayList<>();
        mFragments.add(OrderNoticeFragment.getInstance());
        mFragments.add(SysMsgFragment.getInstance());
        mFragments.add(SysNoticeFragment.getInstance());

        mBinding.vpContent.setOffscreenPageLimit(2);
        initEvent();
    }

    private void initEvent() {
        mBinding.titleView.setLeftClickListener(v -> {
            finish();
        });

        mBinding.titleView.setRightTextClickListener(v -> {
            showClearDialog();
        });

        mBinding.vpContent.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles.get(position);
            }
        });

        mBinding.tlTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mBinding.tlTab.setupWithViewPager(mBinding.vpContent);
    }

    private void showClearDialog() {
        EasyDialog.init()
                .setLayoutId(R.layout.dialog_base_easy)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseEasyDialog dialog) {
                        holder.setText(R.id.tv_title, "清空系统消息");
                        holder.setText(R.id.tv_content, "您正在清空系统消息，清空后所有通知都将清空，是否确认？");
                        holder.setText(R.id.tv_ok, "同意");
                        holder.setText(R.id.tv_cancel, "拒绝");
                        holder.getView(R.id.tv_ok).setOnClickListener(v -> {
                            ((OrderNoticeFragment) mFragments.get(0)).clearData();
                            ((SysMsgFragment) mFragments.get(1)).clearData();
                            ((SysNoticeFragment) mFragments.get(2)).clearData();
                            dialog.dismiss();
                        });
                        holder.getView(R.id.tv_cancel).setOnClickListener(v -> {
                            dialog.dismiss();
                        });
                    }
                })
                .setPosition(Gravity.CENTER)
                .setMargin(45)
                .setOutCancel(false)
                .show(getSupportFragmentManager());
    }
}
