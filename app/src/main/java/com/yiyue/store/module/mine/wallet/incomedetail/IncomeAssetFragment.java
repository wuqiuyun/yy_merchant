package com.yiyue.store.module.mine.wallet.incomedetail;

import android.support.v4.app.Fragment;
import android.view.View;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yiyue.store.R;
import com.yiyue.store.base.fragment.BaseMvpFragment;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.FragmentIncomeAssetBinding;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.vo.bean.IncomeBean;
import com.yiyue.store.util.RefreshLayoutUtil;
import com.yiyue.store.widget.mytimepickview.CustomDatePicker;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 收入明细- 余额
 * Created by wqy on 2018/12/27.
 */
@CreatePresenter(IncomeDetailsPresenter.class)
public class IncomeAssetFragment extends BaseMvpFragment<IIncomeDetailsView, IncomeDetailsPresenter>
        implements IIncomeDetailsView, OnRefreshListener, OnLoadMoreListener {
    private static final int TYPE_ASSET = 0;
    private FragmentIncomeAssetBinding mBinding;
    private List<IncomeBean> assetList = new ArrayList<>();
    private SectionAdapter sectionAdapter;
    private SmartRefreshLayout mRefreshLayout;
    private int page = 1;
    private int size = 20;
    private String userId;
    private String searchDate; //查询的月份，格式：201812
    private String searchYear;
    private String searchMonth;
    private String currentDate;//当前月份，格式：201812
    private boolean noMore = false;//不再加载下个月的数据
    private CustomDatePicker mDatePicker;
    private boolean isFilter = false;//当筛选月份时为true,否则为false


    public static Fragment newInstance() {
        return new IncomeAssetFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_income_asset;
    }

    @Override
    protected void initView() {
        getThisMonth();
        mBinding = (FragmentIncomeAssetBinding) viewDataBinding;
        userId = AccountManager.getInstance().getUserId();

        sectionAdapter = new SectionAdapter(getActivity(), assetList, TYPE_ASSET);
        mBinding.sectionList.setAdapter(sectionAdapter);
        sectionAdapter.setListener(new SectionAdapter.AssetListener() {
            @Override
            public void onClickTitle(String date) {
                date = date.replace("月", "-");
                date += "01";
                mDatePicker.show(date);
            }

            @Override
            public void onClickChild(IncomeBean bean) {
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("IncomeBean", bean);
//                BillDetailsActivity.startActivity(getActivity(), BillDetailsActivity.class, bundle);
            }

        });
        initRefreshLoadLayout();
        initDatePicker();
    }

    @Override
    protected void loadData() {
        getMvpPresenter().getAssetDetailList(searchDate, userId, page, size);
    }

    protected void initRefreshLoadLayout() {
        mRefreshLayout = mBinding.refreshLayout;
        if (mRefreshLayout != null) {
            mRefreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
            mRefreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()));
            mRefreshLayout.setOnLoadMoreListener(this);
            mRefreshLayout.setOnRefreshListener(this);
        }
    }

    // 本月
    private void getThisMonth() {
        String time = DateUtil.getCurDateStr(DateUtil.FORMAT_YMD);
        searchYear = String.valueOf(time.subSequence(0, 4));
        searchMonth = String.valueOf(time.subSequence(5, 7));
        searchDate = searchYear + searchMonth;
        currentDate = searchDate;
    }

    // 上个月
    private void getLastMonth() {
        int sMonth = Integer.parseInt(searchMonth);
        if (sMonth > 1) {
            --sMonth;
            searchMonth = String.valueOf(sMonth);
            if (searchMonth.length() == 1) {
                searchMonth = "0" + searchMonth;
            }

            searchDate = searchYear + searchMonth;
        } else {
            noMore = true;
        }
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        page++;
        getMvpPresenter().getAssetDetailList(searchDate, userId, page, size);
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        page = 1;
        noMore = false;
        getThisMonth();
        getMvpPresenter().getAssetDetailList(searchDate, userId, page, size);
    }

    private void initDatePicker() {
        String mNowDate = DateUtil.date2Str(new Date(), DateUtil.FORMAT_YMD);
        //刷新数据
        mDatePicker = new CustomDatePicker(getActivity(), "2018-01-01", mNowDate, time -> {
            String[] split = time.split("-");
            //刷新数据
            searchYear = split[0];
            searchMonth = split[1];
            searchDate = searchYear + searchMonth;

            page = 1;
            noMore = true;
            isFilter = true;

            getMvpPresenter().getAssetDetailList(searchDate, userId, page, size);
            mRefreshLayout.setNoMoreData(true);
        });
        //隐藏Day
        mDatePicker.setHideDay(true);
    }

    @Override
    public void getAssetDetailSuccess(List<IncomeBean> beanList) {
        RefreshLayoutUtil.finishRefreshLayout(mRefreshLayout);
        if (null != beanList && beanList.size() > 0) {
            if (isFilter || currentDate.equals(searchDate) && page == 1) {
                assetList.clear();
            }

            assetList.addAll(beanList);
            sectionAdapter.setData(assetList, false);

            if (beanList.size() < size) {// 加载的数据不够页面数量 则认为没有下一页
                page = 0;
                getLastMonth();
            }
            mBinding.noData.setVisibility(View.GONE);
        } else {
            if (!noMore) {//能查询下个月
                page = 1;
                getLastMonth();
                loadData();
            } else {
                mRefreshLayout.setNoMoreData(true);
            }

            if (isFilter) {//按月份筛选
                assetList.clear();
                IncomeBean bean = new IncomeBean();
                bean.setCreatetime(searchYear + "-" + searchMonth + "-" + "01");
                assetList.add(bean);
                sectionAdapter.setData(assetList, true);
            } else {
                if (page == 1 && searchDate.equals(currentDate)) {
                    assetList.clear();
                    IncomeBean bean = new IncomeBean();
                    bean.setCreatetime(searchYear + "-" + searchMonth + "-" + "01");
                    assetList.add(bean);
                    sectionAdapter.setData(assetList, true);
                }
            }
            mBinding.noData.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void getAssetDetailFail() {
        RefreshLayoutUtil.finishRefreshLayout(mRefreshLayout);
    }

    @Override
    public void getCoinDetailSuccess(List<IncomeBean> beanList) {
        RefreshLayoutUtil.finishRefreshLayout(mRefreshLayout);
    }

    @Override
    public void getCoinDetailFail() {
        RefreshLayoutUtil.finishRefreshLayout(mRefreshLayout);
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(message);
    }

}
