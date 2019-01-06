package com.yiyue.store.module.mine.info.service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.base.adapter.BaseRecycleViewAdapter;
import com.yiyue.store.component.databind.ClickHandler;
import com.yiyue.store.component.recycleview.GridSpacingItemDecoration;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivityServiceScopeBinding;
import com.yiyue.store.model.vo.bean.CategoryBean;
import com.yiyue.store.util.FormatUtil;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务范围
 * <p>
 * Created by lvlong on 2018/10/13.
 */
@CreatePresenter(ServicesScopePresenter.class)
public class ServicesScopeActivity extends BaseMvpAppCompatActivity<ServicesScopeView, ServicesScopePresenter>
        implements ClickHandler, ServicesScopeView {
    private static final String EXTRAS_DATA = "data";

    ActivityServiceScopeBinding mBinding;

    private ServiceScopeAdapter mServiceAdapter;

    private ArrayList<CategoryBean> selectCategoryId; // 选中的类目

    /**
     *
     * @param activity
     * @param requestCode
     * @param categoryIds
     */
    public static void startForResultActivity(Activity activity, int requestCode, ArrayList<CategoryBean> categoryIds) {
        Intent intent = new Intent(activity, ServicesScopeActivity.class);
        if (categoryIds != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(EXTRAS_DATA, categoryIds);
            intent.putExtras(bundle);
        }
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_service_scope;
    }

    @Override
    protected void init() {
        StatusBarUtil.setLightMode(this);
        mBinding = (ActivityServiceScopeBinding) viewDataBinding;
        mBinding.setClick(this);

        hasExtras();
        initView();
        initData();

    }

    private void hasExtras() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            selectCategoryId = bundle.getParcelableArrayList(EXTRAS_DATA);
        }
    }

    private void initView() {

        mBinding.titleView.setLeftClickListener(view -> finish());

        RecyclerView serviceRecycle = mBinding.serviceRecycle;
        mServiceAdapter = new ServiceScopeAdapter(this);
        mServiceAdapter.setItemListener(new BaseRecycleViewAdapter.SimpleRecycleViewItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                mServiceAdapter.getDatas().get(position).setChecked(!mServiceAdapter.getDatas().get(position).isChecked());
                mServiceAdapter.notifyDataSetChanged();
            }
        });
        serviceRecycle.setLayoutManager(new GridLayoutManager(this, 4));
        serviceRecycle.setAdapter(mServiceAdapter);
        serviceRecycle.addItemDecoration(new GridSpacingItemDecoration(4, 50, false));

    }


    private void initData() {
        // 获取平台类目
        getMvpPresenter().getCategoryAll();
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_save: //保存
                getMvpPresenter().updateServices(mServiceAdapter.getSetleCategoryIds());
                break;
        }

    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(FormatUtil.Formatstring(message));
    }

    @Override
    public void onGetCategoryAllSuccess(List<CategoryBean> categoryBeans) {
        if (categoryBeans == null || categoryBeans.isEmpty()) return;
        if (selectCategoryId != null && !selectCategoryId.isEmpty()) {
            for (CategoryBean selectBean : selectCategoryId) {
                for (CategoryBean bean : categoryBeans) {
                    if (selectBean.getId() == bean.getId()) {
                        bean.setChecked(true);
                        break;
                    }
                }
            }
        }
        mServiceAdapter.setDatas((ArrayList<CategoryBean>) categoryBeans, true);
    }

    @Override
    public void onUpdateServicesSuccess() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(EXTRAS_DATA, mServiceAdapter.getSetleCategoryBean());
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
