package com.yiyue.store.module.mine.pplarz.details;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yiyue.store.R;
import com.yiyue.store.base.adapter.BaseRecycleViewAdapter;
import com.yiyue.store.base.fragment.BasePageMvpFragment;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.FragmentIncomeRoleBinding;
import com.yiyue.store.model.vo.bean.UserIncomeInfoBean;
import com.yiyue.store.model.vo.result.RecommendUserListResult;
import com.yiyue.store.module.mine.pplarz.role.IncomeRoleDetailsActivity;
import com.yiyue.store.util.FormatUtil;
import com.yiyue.store.util.RefreshLayoutUtil;

/**
 * Created by zm on 2019/1/4.
 */
@CreatePresenter(IncomeRolePresenter.class)
public class IncomeRoleFragment extends BasePageMvpFragment<IncomeRoleView, IncomeRolePresenter, UserIncomeInfoBean> implements IncomeRoleView{
    private int roletype; // 角色 1门店 2美发师 3用户
    private IncomeRoleAdapter adapter;

    private final String[] roleTitles = new String[]{"门店", "美发师", "用户"};
    FragmentIncomeRoleBinding mBinding;

    public static Fragment newInstance() {
        return new IncomeRoleFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            roletype = bundle.getInt(PopularizeDetailsActivity.EXTRAS_ROLE);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_income_role;
    }

    @Override
    protected void initView() {
        mBinding = (FragmentIncomeRoleBinding) viewDataBinding;
        initRefreshLayout();
        adapter = new IncomeRoleAdapter(getContext());
        adapter.setItemListener(new BaseRecycleViewAdapter.SimpleRecycleViewItemListener(){
            @Override
            public void onItemClick(View view, int position) {
                IncomeRoleDetailsActivity.actionStart(getContext(), roletype, adapter.getDatas().get(position));
            }
        });
        mBinding.recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.recycleView.setAdapter(adapter);
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        loadData();
    }

    @Override
    protected void loadData() {
        getMvpPresenter().recommendUserList(roletype);
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(FormatUtil.Formatstring(message));
    }

    @Override
    public void setData(RecommendUserListResult.Data data) {
        RefreshLayoutUtil.finishRefreshLayout(mBinding.refreshLayout);
        mBinding.incomeTitle.tvTitle.setText("共" + data.getInviteSize() + "家" + roleTitles[roletype-1]);
        mBinding.incomeTitle.tvContent.setText("累计收益:" + data.getAllIncome() + "元");
        adapter.setDatas(data.getUserIncomeInfos(), true);
    }

    @Override
    public void onLoadFail() {
        RefreshLayoutUtil.finishRefreshLayout(mBinding.refreshLayout);
    }
}
