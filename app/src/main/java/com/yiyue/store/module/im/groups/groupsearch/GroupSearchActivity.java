package com.yiyue.store.module.im.groups.groupsearch;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;

import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivityGroupSearchBinding;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.vo.bean.daobean.GroupBean;
import com.yiyue.store.module.im.groupinfo.GroupInfoActivity;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;

import java.util.List;
import java.util.Objects;

@CreatePresenter(GroupSearchPresenter.class)
public class GroupSearchActivity extends BaseMvpAppCompatActivity<GroupSearchView, GroupSearchPresenter> implements GroupSearchView {

    private ActivityGroupSearchBinding mBinding;
    private GroupSearchAdapter mSearchAdapter;

    private int pageNo = 1;//页码
    private int pageSize = 10;//每页数量
    private boolean isLoadMore = true;//是否可以下拉加载更多

    private String param;//搜索的内容

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_group_search;
    }

    @Override
    protected void init() {
        mBinding = (ActivityGroupSearchBinding) viewDataBinding;

        initView();
    }

    private void initView() {
        StatusBarUtil.setLightMode(this);
        mBinding.searchView.setHint(getString(R.string.hint_search_group));
        mBinding.searchView.setLeftClickListener(view -> finish());
        mBinding.searchView.setRightTextClickListener(view ->
        {
            param = mBinding.searchView.getSearchText().trim();
            if (TextUtils.isEmpty(param)) {
                showToast("搜索内容不能为空");
            } else {
                firstSearch(param);
            }

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            Objects.requireNonNull(imm).toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        });

        mBinding.rvSearchGroup.setHasFixedSize(true);
        mBinding.rvSearchGroup.setNestedScrollingEnabled(false);
        mBinding.rvSearchGroup.setLayoutManager(new LinearLayoutManager(this));

        mSearchAdapter = new GroupSearchAdapter(R.layout.item_group);
        mSearchAdapter.openLoadAnimation();
        mSearchAdapter.setOnLoadMoreListener(() ->
        {
            pageNo++;//增加页码
            getMvpPresenter().searchGroupPage(param, pageNo, pageSize);//搜索下一页

        }, mBinding.rvSearchGroup);

        mSearchAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (adapter.getData() != null) {
                Intent intent = new Intent(this, GroupInfoActivity.class);
                intent.putExtra(Constants.EXTRA_GROUP, ((GroupBean) adapter.getData().get(position)));
                startActivity(intent);
            }
        });

        mBinding.rvSearchGroup.setAdapter(mSearchAdapter);
    }

    private void firstSearch(String param) {
        pageNo = 1;
        pageSize = 10;
        getMvpPresenter().searchGroupPage(param, pageNo, pageSize);
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(message);
    }

    @Override
    public void searchGroupSuccess(List<GroupBean> list) {
        if (null != list && list.size() > 0) {
            if (pageNo == 1)//说明是初次加载数据
            {
                mSearchAdapter.setNewData(list);
            } else {
                mSearchAdapter.addData(list);
            }

            if (list.size() < pageSize) {
                mSearchAdapter.setEnableLoadMore(false);
            } else {
                mSearchAdapter.loadMoreComplete();
                mSearchAdapter.setEnableLoadMore(true);
            }
        } else {
            if (pageNo == 1)//说明是初次加载数据
            {
                showToast("没有符合的搜索结果！");
            } else {
                showToast("没有更多了");
            }
            mSearchAdapter.setEnableLoadMore(false);
        }


    }

    @Override
    public void searchGroupFail() {

    }
}
