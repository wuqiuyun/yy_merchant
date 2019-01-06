package com.yiyue.store.module.mine.pplarz.details;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.DateUtil;
import com.yiyue.store.R;
import com.yiyue.store.base.fragment.BasePageMvpFragment;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.FragmentIncomeRecordBinding;
import com.yiyue.store.model.vo.bean.IncomeRecordBean;
import com.yiyue.store.util.FormatUtil;
import com.yiyue.store.util.RefreshLayoutUtil;
import com.yiyue.store.widget.mytimepickview.CustomDatePicker;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by zm on 2019/1/4.
 */
@CreatePresenter(IncomeRecordPresenter.class)
public class IncomeRecordFragment extends BasePageMvpFragment<IncomeRecordView, IncomeRecordPresenter, IncomeRecordBean>
        implements IncomeRecordView, OnLoadMoreListener {
    private int roletype; // 角色 1门店 2美发师 3用户
    private boolean isByMonth = false; // 是否按月查询
    private String month; // 选择的月份
    private IncomeRecordAdapter adapter;
    private CustomDatePicker mDatePicker;

    private FragmentIncomeRecordBinding mBinding;

    public static Fragment newInstance() {
        return new IncomeRecordFragment();
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
        return R.layout.fragment_income_record;
    }

    @Override
    protected void initView() {
        mBinding = (FragmentIncomeRecordBinding) viewDataBinding;
        initLoadLayout();
        initDatePicker();
    }

    @Override
    protected void loadData() {
        if (isByMonth) {
            getMvpPresenter().recommendIncomeByMonth(month, roletype, pageIndx);
        }else {
            getMvpPresenter().recommendIncomeList(getContext(), roletype, pageIndx);
        }
    }

    private void initDatePicker() {
        String mNowDate = DateUtil.date2Str(new Date(), DateUtil.FORMAT_YMD);
        //刷新数据
        mDatePicker = new CustomDatePicker(getActivity(), "2018-01-01", mNowDate, time -> {
            adapter.clear();
            String[] split = time.split("-");
            month = split[0] + split[1];
            adapter.setMapData(split[0] + "年" + split[1] + "月");
            pageIndx = 1;
            isByMonth = true;
            loadData();
        });
        //隐藏Day
        mDatePicker.setHideDay(true);
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(FormatUtil.Formatstring(message));
    }

    @Override
    public void setData(ArrayList<IncomeRecordBean> datas) {
        RefreshLayoutUtil.finishRefreshLayout(mBinding.refreshLayout);
        if (adapter == null) {
            adapter = new IncomeRecordAdapter(getContext(), datas);
            adapter.setAssetListener(date -> {
                date = date.replace("年", "-").replace("月", "-");
                date += "01";
                mDatePicker.show(date);
            });
            mBinding.shLv.setAdapter(adapter);
        }else {
            adapter.addData(datas);
        }
        mBinding.refreshLayout.setNoMoreData(datas.size() < 10);
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        pageIndx++;
        loadData();
    }
}
