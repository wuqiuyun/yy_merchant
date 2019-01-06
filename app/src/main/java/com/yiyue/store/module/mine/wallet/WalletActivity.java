package com.yiyue.store.module.mine.wallet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.yiyue.store.R;
import com.yiyue.store.YLApplication;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.component.databind.ClickHandler;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivityMineWalletBinding;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.local.preferences.CommonSharedPreferences;
import com.yiyue.store.model.vo.bean.CashInfoBean;
import com.yiyue.store.model.vo.bean.CoinInfoBean;
import com.yiyue.store.module.common.h5.WebActivity;
import com.yiyue.store.module.mine.wallet.address.AddressActivity;
import com.yiyue.store.module.mine.wallet.cochain.CochainActivity;
import com.yiyue.store.module.mine.wallet.incomedetail.IncomeDetailsActivity;
import com.yiyue.store.module.mine.wallet.orderdetil.AllOrderDetilActivity;
import com.yiyue.store.module.mine.wallet.recharge.RechargeActivity;
import com.yiyue.store.module.mine.wallet.statistics.OrderStatisticsFragment;
import com.yiyue.store.module.mine.wallet.statistics.StylistStatistics.StylistStatisticsActivity;
import com.yiyue.store.module.mine.wallet.transfer.TransferActivity;
import com.yiyue.store.module.mine.wallet.withdraw.WithdrawActivity;
import com.yiyue.store.util.FormatUtil;
import com.yiyue.store.util.MathematicsUtils;
import com.yiyue.store.util.Utils;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 我的钱包
 * <p>
 * Create by zm on 2018/9/29
 */
@CreatePresenter(WalletPresenter.class)
public class WalletActivity extends BaseMvpAppCompatActivity<IWalletView, WalletPresenter>
        implements IWalletView, ClickHandler, ViewPager.OnPageChangeListener {

    public static final int WALLET_TIXIAN = 0x008;
    public static final int WALLET_ZHUANZENG = 0x009;

    protected int pageSize = 10; // 每页数量
    private ActivityMineWalletBinding mBinding;

    private String coinBalance = "0.00", cashBalance = "0.00";//余额
    private double cost = 0.00;//代币参考价
    private double coinRate;//代币总价值
    private double coinBalances;//代币数量
    private int type = 0; // 0余额，1代币
    private String mMoneyFree;//冻结金额
    private double mMoneyNow;//可提现金额

    private List<OrderStatisticsFragment> mFragments;
    private final String[] labels = new String[]{
            YLApplication.getContext().getString(R.string.balance),
            YLApplication.getContext().getString(R.string.score)
    };

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_mine_wallet;
    }

    @Override
    protected void init() {

        initView();
        setFragment();
        setListener();
    }

    @SuppressLint("StringFormatInvalid")
    private void initView() {
        mBinding = (ActivityMineWalletBinding) viewDataBinding;
        mBinding.setClick(this);

        // 返回
        mBinding.titleView.setLeftClickListener(v -> finish());
        mBinding.titleView.setRightTextClickListener(v -> {
            jumpRightClick();
        });

        // tabLayout
        setTable(mBinding.tableLayout, getLayoutInflater());
        Utils.changeTextColor(mBinding.tableLayout);
        // type
        setType(0);
    }

    private void setType(int type) {
        this.type = type;
        mBinding.titleView.setRightText(type == 0 ? getString(R.string.description) : getString(R.string.address));
        //获取余额或代币余额
        if (type == 0) {
            mBinding.llWalletOne.setVisibility(View.VISIBLE);
            mBinding.view.setVisibility(View.VISIBLE);
            mBinding.tvWithdraw.setVisibility(View.VISIBLE);
            mBinding.titleView.setRightTextVisib(true);
            //获取余额
            getMvpPresenter().getCashInfo();
        } else {
            mBinding.llWalletOne.setVisibility(View.GONE);
            mBinding.view.setVisibility(View.GONE);
            mBinding.tvWithdraw.setVisibility(View.GONE);// 隐藏提现按钮
            mBinding.titleView.setRightTextVisib(false);
            //获取代币余额
            getMvpPresenter().getCoinInfo();
        }
    }

    private void setFragment() {
        mFragments = new ArrayList<>();
        OrderStatisticsFragment orderStatisticsFragment1 = (OrderStatisticsFragment) OrderStatisticsFragment.newInstance(mBinding.viewPage, Constants.ORDER_TODAY);
        OrderStatisticsFragment orderStatisticsFragment2 = (OrderStatisticsFragment) OrderStatisticsFragment.newInstance(mBinding.viewPage, Constants.ORDER_YESTERDAY);
        OrderStatisticsFragment orderStatisticsFragment3 = (OrderStatisticsFragment) OrderStatisticsFragment.newInstance(mBinding.viewPage, Constants.ORDER_RECENT7);
        OrderStatisticsFragment orderStatisticsFragment4 = (OrderStatisticsFragment) OrderStatisticsFragment.newInstance(mBinding.viewPage, Constants.ORDER_RECENT30);
        mFragments.add(orderStatisticsFragment1);
        mFragments.add(orderStatisticsFragment2);
        mFragments.add(orderStatisticsFragment3);
        mFragments.add(orderStatisticsFragment4);
        mBinding.viewPage.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                super.destroyItem(container, position, object);
            }
        });
        mBinding.viewPage.addOnPageChangeListener(this);
        // 是否允许viewPage滑动切换
        mBinding.viewPage.setCanScroll(false);
        // viewPage预加载1个页面
        mBinding.viewPage.setOffscreenPageLimit(1);
        // 加载第几个页面
        mBinding.viewPage.setCurrentItem(0, false);
    }

    private void setListener() {
        mBinding.rgDay.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.tab1:
                        mBinding.viewPage.setCurrentItem(0, false);
                        break;
                    case R.id.tab2:
                        mBinding.viewPage.setCurrentItem(1, false);
                        break;
                    case R.id.tab3:
                        mBinding.viewPage.setCurrentItem(2, false);
                        break;
                    case R.id.tab4:
                        mBinding.viewPage.setCurrentItem(3, false);
                        break;
                }
            }
        });

        mBinding.tableLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == 0) {//余额
                    setType(0);
                } else {//积分
                    setType(1);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void jumpRechargeClick() {
        if (type == 0) {
            //充值
            startActivity(WalletActivity.this, RechargeActivity.class);
        } else {
            //上链
            Intent intent = new Intent().putExtra("currency", mBinding.tvTotalNum.getText().toString());
            intent.setClass(WalletActivity.this, CochainActivity.class);
            startActivity(intent);
        }
    }

    private void jumpWithdrawClick() {

        if (mMoneyNow > 0) {//余额减去冻结余额要大于100才能提现
            //提现
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putDouble(Constants.WITHDRAW_MONEY, mMoneyNow);
            intent.setClass(this, WithdrawActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, WALLET_TIXIAN);
        } else {
            ToastUtils.shortToast("可提现余额不足");
        }
    }

    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    private void jumpRightClick() {
        if (type == 0) {
            String url = Constants.WEB_MONEYEXOLAIN;
            WebActivity.startActivity(WalletActivity.this, url, "钱包说明");
        } else {
            startActivity(WalletActivity.this, AddressActivity.class);
        }
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(message);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.tv_recharge: // 充值
//                jumpRechargeClick();
//                break;
            case R.id.tv_withdraw: // 提现
                jumpWithdrawClick();
                break;

            case R.id.mv_income_details: // 收入明细
                IncomeDetailsActivity.startActivity(this, IncomeDetailsActivity.class);
                break;
            case R.id.mv_order_details: // 全部订单明细
                AllOrderDetilActivity.startActivity(this, AllOrderDetilActivity.class);
                break;

            case R.id.mv_stylist_statistic: // 美发师统计
                StylistStatisticsActivity.startActivity(WalletActivity.this, StylistStatisticsActivity.class);
                break;

            case R.id.tv_give: // 转赠
                if (coinBalances > 0) {
                    Intent intent = new Intent().putExtra("currency", mBinding.tvTotalNum.getText().toString()).putExtra("coincost", String.valueOf(cost));
                    intent.setClass(WalletActivity.this, TransferActivity.class);
                    startActivityForResult(intent, WALLET_ZHUANZENG);
                } else {
                    ToastUtils.shortToast("可转赠" + FormatUtil.Formatstring(CommonSharedPreferences.getInstance().getBasicDataBean().getSysCoinDefault()) + "不足");
                }
                break;
            case R.id.tv_cochain://上链
                Intent intent = new Intent().putExtra("currency", mBinding.tvTotalNum.getText().toString());
                intent.setClass(WalletActivity.this, CochainActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onGetCashInfoSuccess(CashInfoBean bean) {
        cashBalance = FormatUtil.Formatstring(bean.getBalance());//余额
        mMoneyFree = FormatUtil.Formatstring(bean.getFreezeBalance());//冻结余额
        try {
            if (!TextUtils.isEmpty(cashBalance) && !TextUtils.isEmpty(mMoneyFree)) {
                double mMoney = Double.parseDouble(cashBalance);
                double mMoney1 = Double.parseDouble(mMoneyFree);
                mMoneyNow = sub(mMoney, mMoney1);//可用余额
                mBinding.tvMoenyBalance.setText(FormatUtil.FormatDouble(mMoneyNow));
                mBinding.tvMoenyEnbalance.setText(FormatUtil.FormatDouble(mMoney1));
                mBinding.tvTotalNum.setText(FormatUtil.FormatDouble(mMoney));
                mBinding.tvTotalText.setText("总额");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @SuppressLint("StringFormatMatches")
    @Override
    public void onGetCoinInfoSuccess(CoinInfoBean bean) {
        coinRate = bean.getRate();//代币总价值
        coinBalances = bean.getBalance();
        if (coinRate > 0) {
            cost = MathematicsUtils.div(coinRate, coinBalances);
        }
        coinBalance = FormatUtil.Formatstring(String.valueOf(coinBalances));

        mBinding.tvTotalNum.setText(FormatUtil.FormatDouble(coinBalances));
        mBinding.tvTotalText.setText("总积分");
    }

//    @Override
//    public void onGetCashInfoListSuccess(ArrayList<CashInfoAccountBean> bean) {
////        RefreshLayoutUtil.finishRefreshLayout(refreshLayout);
//        if (bean != null && bean.size() > 0) {
//            if (pageIndx == 1) {
//                cashInfoAccountBeans.clear();
//            }
//            cashInfoAccountBeans.addAll(bean);
//            adapter.setDatas(cashInfoAccountBeans, true);
//        }
//    }
//
//    @Override
//    public void onGetCoinInfoListSuccess(ArrayList<CoinInfoAccountBean> bean) {
////        RefreshLayoutUtil.finishRefreshLayout(refreshLayout);
//        if (bean != null && bean.size() > 0) {
//            if (pageIndx == 1) {
//                coinInfoAccountBeans.clear();
//            }
//            coinInfoAccountBeans.addAll(bean);
//            coinadapter.setDatas(coinInfoAccountBeans, true);
//
//        }
//    }
//
//    @Override
//    public void onGetCashInfoListFail() {
////        RefreshLayoutUtil.finishRefreshLayout(refreshLayout);
//    }
//
//    @Override
//    public void onGetCoinInfoListFail() {
////        RefreshLayoutUtil.finishRefreshLayout(refreshLayout);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case WALLET_TIXIAN: // 提现
                    // type
                    setType(0);
                    break;
                case WALLET_ZHUANZENG: // 转赠
                    // type
                    setType(1);
                    break;
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mBinding.viewPage.resetHeight(position);
        switch (position) {
            case 0:
                mBinding.rgDay.check(R.id.tab1);
                break;
            case 1:
                mBinding.rgDay.check(R.id.tab2);
                break;
            case 2:
                mBinding.rgDay.check(R.id.tab3);
                break;
            case 3:
                mBinding.rgDay.check(R.id.tab4);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 设置Tab
     *
     * @param tabLayout
     * @param inflater
     */
    private void setTable(TabLayout tabLayout, LayoutInflater inflater) {
        for (String label : labels) {
            TabLayout.Tab newTab = tabLayout.newTab();
            View tabView = inflater.inflate(R.layout.tab_layout_order, null);
            newTab.setCustomView(tabView);

            TextView tvLabel = tabView.findViewById(R.id.tv_label);
            tvLabel.setText(label);
            tabLayout.addTab(newTab);
        }
    }
}
