package com.yiyue.store.module.common.addr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.yiyue.store.R;
import com.yiyue.store.base.BaseAppCompatActivity;
import com.yl.core.util.StatusBarUtil;

/**
 * 地址选择
 * <p>
 * Created by zm on 2018/10/19.
 */
public class AddressSelectActivity extends BaseAppCompatActivity {
    private static final String BUNDLE_FRAGMENT = "fragment_address";

    Fragment addrSelectFragment;

    /**
     *
     * @param activity
     * @param requestCode
     */
    public static void startActivity(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, AddressSelectActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, BUNDLE_FRAGMENT, addrSelectFragment);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_address_select;
    }

    @Override
    protected void init() {
        StatusBarUtil.setLightMode(this);
        initFragment();
    }

    private void initFragment() {
        if (savedInstanceState != null) {
            addrSelectFragment = getSupportFragmentManager().getFragment(savedInstanceState, BUNDLE_FRAGMENT);
        }
        if (addrSelectFragment == null) {
            addrSelectFragment = AddrSelectFragment.newInstance();
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_content, addrSelectFragment)
                .commit();
    }
}
