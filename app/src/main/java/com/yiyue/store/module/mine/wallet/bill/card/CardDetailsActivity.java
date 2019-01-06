package com.yiyue.store.module.mine.wallet.bill.card;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.component.databind.ClickHandler;
import com.yiyue.store.databinding.ActivityCardDetailsBinding;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.vo.bean.BankDetailsBean;
import com.yiyue.store.model.vo.bean.BankSumBean;
import com.yiyue.store.util.BankImgUtil;
import com.yiyue.store.util.RefreshLayoutUtil;
import com.yiyue.store.widget.mytimepickview.CustomDatePicker;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.DateUtil;
import com.yl.core.util.StatusBarUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

/**
 * 银行卡明细
 * Created by wqy on 2018/12/18.
 */
@CreatePresenter(CardDetailsPresenter.class)
public class CardDetailsActivity extends BaseMvpAppCompatActivity<ICardDetailsView, CardDetailsPresenter>
        implements ICardDetailsView, OnRefreshListener, OnLoadMoreListener, ClickHandler {
    private ActivityCardDetailsBinding mBinding;
    private RefreshLayout mRefreshLayout;
    private CardDetailsAdapter mAdapter;
    private String searchDate = "";//月份 2018-12
    private int page = 1;
    private int size = 10;
    private String userId = "";
    private BankSumBean bankSumBean;
    private CustomDatePicker mDatePicker;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_card_details;
    }

    @Override
    protected void init() {
        initView();
        hasExtras();

        String date = mBinding.tvDate.getText().toString();
        date = date.replace("年", "-");
        date = date.replace("月", "-");
        date += "01";
        searchDate = date;

        loadData();
    }

    private void hasExtras() {
        userId = AccountManager.getInstance().getUserId();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            bankSumBean = (BankSumBean) bundle.getSerializable("bankSumBean");
            String date = bundle.getString("date");

            // 设置logo/背景颜色
            setLogo(bankSumBean.getShortName());
            // 银行名称
            mBinding.tvBankName.setText(bankSumBean.getBankName());
            // 银行卡号
            int length = bankSumBean.getAccountno().length();
            if (length >= 4) {
                mBinding.tvAccountNo.setText("**** **** " + bankSumBean.getAccountno().substring(length - 4, length));
            }
            mAdapter.getBankName(bankSumBean.getShortName());
            mBinding.tvDate.setText(date);
        }
    }

    private void initView() {
        StatusBarUtil.setLightMode(this);
        mBinding = (ActivityCardDetailsBinding) viewDataBinding;
        mBinding.titleView.setLeftClickListener(view -> finish());
        mBinding.setClick(this);

        mAdapter = new CardDetailsAdapter(CardDetailsActivity.this);
        mBinding.recycleView.setLayoutManager(new LinearLayoutManager(CardDetailsActivity.this));
        // 添加自定义分割线
        DividerItemDecoration divider = new DividerItemDecoration(CardDetailsActivity.this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(CardDetailsActivity.this, R.drawable.shape_divider_line)));
        mBinding.recycleView.addItemDecoration(divider);
        mBinding.recycleView.setAdapter(mAdapter);
        //初始化日期选择
        initDatePicker();
        mBinding.tvDate.setText(DateUtil.date2Str(new Date(), DateUtil.FORMAT_YM_CN));
        initRefreshLoadLayout();
    }

    private void initDatePicker() {
        String mNowDate = DateUtil.date2Str(new Date(), DateUtil.FORMAT_YMD);
        //刷新数据
        mDatePicker = new CustomDatePicker(this, "1930-01-01", mNowDate, time -> {
            String[] split = time.split("-");
            mBinding.tvDate.setText(split[0] + "年" + split[1] + "月");
            //刷新数据
            searchDate = split[0] + "-" + split[1];
            page = 1;
            loadData();
        });
        //隐藏Day
        mDatePicker.setHideDay(true);
    }

    private void loadData() {
        getMvpPresenter().showBanks(searchDate, bankSumBean.getShortName(), bankSumBean.getAccountno(), page, size);
        getMvpPresenter().showBanksSum(searchDate, bankSumBean.getShortName(), bankSumBean.getAccountno(), page, size);
    }

    @Override
    public void showToast(String message) {

    }

    protected void initRefreshLoadLayout() {
        mRefreshLayout = mBinding.refreshLayout;
        if (mRefreshLayout != null) {
            mRefreshLayout.setRefreshHeader(new ClassicsHeader(this));
            mRefreshLayout.setRefreshFooter(new ClassicsFooter(this));
            mRefreshLayout.setOnLoadMoreListener(this);
            mRefreshLayout.setOnRefreshListener(this);
        }
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        ++page;
        loadData();
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        page = 1;
        loadData();
    }

    @Override
    public void showBanksSuccess(BankDetailsBean bean) {
        RefreshLayoutUtil.finishRefreshLayout(mRefreshLayout);
        ArrayList<BankDetailsBean.TakeLogsBean> list = (ArrayList<BankDetailsBean.TakeLogsBean>) bean.getTakeLogs();
        if (null != list && list.size() > 0) {
            if (page == 1) {
                mAdapter.setDatas(list, true);
            } else {
                mAdapter.addDatas(list, true);
            }

            if (list.size() < size) {// 加载的数据不够页面数量 则认为没有下一页
                mRefreshLayout.setNoMoreData(true);
            } else {
                mRefreshLayout.setNoMoreData(false);
            }
        } else {
            if (page == 1) {
                mAdapter.clearData(true);
            }
        }
    }

    @Override
    public void showBanksFail() {
        RefreshLayoutUtil.finishRefreshLayout(mRefreshLayout);
    }

    @Override
    public void showBanksSumSuccess(BankDetailsBean bean) {
        mBinding.tvBits.setText(String.format(getResources().getString(R.string.withdraw_number),
                String.valueOf(bean.getTakeSize())));//累计提现笔数
        mBinding.tvTakeSum.setText("-" + bean.getTakeSum());//累计提现金额
    }

    @Override
    public void showBanksSumFail() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_date:
                String date = mBinding.tvDate.getText().toString();
                date = date.replace("年", "-");
                date = date.replace("月", "-");
                date += "01";
                mDatePicker.show(date);
                break;
        }
    }

    private void setLogo(String shortName) {
        mBinding.ivLogo.setImageResource(BankImgUtil.getBankImgTwo(shortName));
        mBinding.llBg.setBackgroundResource(BankImgUtil.getBankImgBag(shortName));
    }
}
