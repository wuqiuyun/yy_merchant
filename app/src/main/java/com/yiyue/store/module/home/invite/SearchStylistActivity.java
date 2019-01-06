package com.yiyue.store.module.home.invite;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.yiyue.store.R;
import com.yiyue.store.base.BaseAppCompatActivity;
import com.yiyue.store.databinding.ActivityStylistSearchBinding;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.module.mine.stylist.StylistFragment;
import com.yl.core.util.StatusBarUtil;

/**
 * Created by lvlong on 2018/10/27.
 */
public class SearchStylistActivity extends BaseAppCompatActivity  {
    private static final String BUNDLE_FRAGMENT = "stylistFragment";
    private Fragment mFragment;
    private IUpDataFragment mIUpDataFragment;
    private ActivityStylistSearchBinding mBinding;
    private int mStoreType;

    public void setIUpDataFragment(IUpDataFragment IUpDataFragment) {
        mIUpDataFragment = IUpDataFragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_stylist_search;
    }


    @Override
    protected void init() {
        StatusBarUtil.setLightMode(this);
            initFragment();
        mBinding = (ActivityStylistSearchBinding) this.viewDataBinding;
        mBinding.titleView.setLeftClickListener(view -> finish());
        mStoreType = getIntent().getIntExtra(Constants.STORE_TYPE, 0);
        mBinding.etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    if (mIUpDataFragment!=null){
                        if (mStoreType==Constants.ACTIVITY_INVITE_STYLIST){
                            mIUpDataFragment.onUpData(Constants.ACTIVITY_FILTER_STYLIST_4,mBinding.etSearch.getText().toString().trim());
                        }else {
                            mIUpDataFragment.onUpData(Constants.ACTIVITY_FILTER_STYLIST_5,mBinding.etSearch.getText().toString().trim());
                        }
                    }
                    return true;
                }
                return false;
            }
        });

    }
    private void initFragment() {
        if (savedInstanceState != null) {
            mFragment = getSupportFragmentManager().getFragment(savedInstanceState, BUNDLE_FRAGMENT);
        }
        if (mFragment == null) {
            mFragment = StylistFragment.newInstance(Constants.ACTIVITY_INVITE_SEARCH);
            mFragment.setUserVisibleHint(true);
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_stylist_search, mFragment)
                .commit();

    }

}
