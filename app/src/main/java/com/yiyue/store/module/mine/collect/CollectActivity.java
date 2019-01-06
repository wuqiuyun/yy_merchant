package com.yiyue.store.module.mine.collect;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yiyue.store.R;
import com.yiyue.store.YLApplication;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.databinding.ActivityMineCollectBinding;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.module.mine.store.StoreFragment;
import com.yiyue.store.module.mine.stylist.StylistFragment;
import com.yiyue.store.module.mine.works.WorksFragment;
import com.yiyue.store.util.Utils;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的收藏
 * <p>
 * Create by zm on 2018/9/29
 */
@CreatePresenter(CollectPresenter.class)
public class CollectActivity extends BaseMvpAppCompatActivity<ICollectView, CollectPresenter> implements ICollectView {

    ActivityMineCollectBinding binding;
    private String[] labels = new String[] {
            YLApplication.getContext().getString(R.string.label_title_stylist),
            YLApplication.getContext().getString(R.string.label_title_store),
            YLApplication.getContext().getString(R.string.label_title_works)
    };

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_mine_collect;
    }

    @Override
    protected void init() {

        initView();
    }

    private void initView() {
        StatusBarUtil.setLightMode(this);
        binding = (ActivityMineCollectBinding) viewDataBinding;
        // 返回
        binding.titleView.setLeftClickListener(v -> finish());
        setTab(binding.tableLayout);
        setFragment();
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
        fragments.add(StylistFragment.newInstance(Constants.ACTIVITY_COLLECT));
        fragments.add(StoreFragment.newInstance(Constants.ACTIVITY_COLLECT));
        fragments.add(WorksFragment.newInstance(Constants.ACTIVITY_COLLECT));
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

    }
}
