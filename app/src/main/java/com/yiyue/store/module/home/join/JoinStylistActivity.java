package com.yiyue.store.module.home.join;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivityStylistJoinBinding;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.vo.bean.AreaBean;
import com.yiyue.store.model.vo.bean.StylistBean;
import com.yiyue.store.model.vo.bean.StylistNumBean;
import com.yiyue.store.module.home.invite.SearchStylistActivity;
import com.yiyue.store.module.mine.stylist.IStylistView;
import com.yiyue.store.module.mine.stylist.StylistFragment;
import com.yiyue.store.module.mine.stylist.StylistPresenter;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的美发师
 * <p>
 * Create by zm on 2018/10/10
 */
@CreatePresenter(StylistPresenter.class)
public class JoinStylistActivity extends BaseMvpAppCompatActivity<IStylistView, StylistPresenter> implements IStylistView{

    ActivityStylistJoinBinding binding;

    private String[] labels = new String[2];

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_stylist_join;
    }

    @Override
    protected void init() {

        initView();
    }

    private void initView() {
        StatusBarUtil.setLightMode(this);
        binding = (ActivityStylistJoinBinding) viewDataBinding;
        binding.titleView.setLeftClickListener(v -> finish());
        binding.titleView.setRightImgClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.STORE_TYPE,Constants.ACTIVITY_JOIN_STYLIST_1);
                startActivity(JoinStylistActivity.this,SearchStylistActivity.class,bundle);
            }
        });

    }


    private void initData() {
        getMvpPresenter().getStoreStylistNumber();
    }

    private void setTab(TabLayout tableLayout) {
        for (String label : labels) {
            TabLayout.Tab newTab = tableLayout.newTab();
            View tabView = getLayoutInflater().inflate(R.layout.tab_layout_home, null);
            newTab.setCustomView(tabView);
            TextView tvLabel = tabView.findViewById(R.id.tv_label);
            tvLabel.setText(label);
            tableLayout.addTab(newTab);
        }
    }

    private void setFragment() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(StylistFragment.newInstance(Constants.ACTIVITY_JOIN_STYLIST_1));
        fragments.add(StylistFragment.newInstance(Constants.ACTIVITY_JOIN_STYLIST_2));
        binding.viewPage.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                super.destroyItem(container, position, object);
            }
        });
        binding.viewPage.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tableLayout));
        binding.tableLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(binding.viewPage) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPage.setCurrentItem(tab.getPosition(), false);
            }
        });
        // 是否允许viewPage滑动切换
        binding.viewPage.setScanScroll(true);
        // viewPage预加载1个页面
        binding.viewPage.setOffscreenPageLimit(1);
        // 加载第几个页面
        binding.viewPage.setCurrentItem(0, false);
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(message);
    }


    @Override
    public void getStylistSuccess(List<StylistBean> stylistBeanList) {

    }

    @Override
    public void getAreaByStoreId(List<AreaBean> areaBeans) {

    }

    @Override
    public void getStylistFail() {

    }

    @SuppressLint("StringFormatMatches")
    @Override
    public void onGetStoreStylistNumber(StylistNumBean bean) {
        if (binding.tableLayout.getTabCount()>0){
            binding.tableLayout.removeAllTabs();//刷新时 移除标题
        }
        labels[0] = String.format(getString(R.string.stylist_join_num) , bean.getEnter());
        labels[1] = String.format(getString(R.string.stylist_signing_num) , bean.getSign());
        setTab(binding.tableLayout);
        setFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }
}
