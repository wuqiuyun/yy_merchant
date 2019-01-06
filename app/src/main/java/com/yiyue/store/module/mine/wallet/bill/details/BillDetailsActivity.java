package com.yiyue.store.module.mine.wallet.bill.details;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivityBillDetailsBinding;
import com.yiyue.store.model.vo.bean.BillDetailsBean;
import com.yiyue.store.model.vo.bean.IncomeBean;
import com.yl.core.component.image.ImageLoader;
import com.yl.core.component.image.ImageLoaderConfig;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;

import java.text.DecimalFormat;

/**
 * 账单详情
 * Created by wqy on 2018/12/18.
 */
@CreatePresenter(BillDetailsPresenter.class)
public class BillDetailsActivity extends BaseMvpAppCompatActivity<IBillDetailsView, BillDetailsPresenter>
        implements IBillDetailsView {
    private ActivityBillDetailsBinding mBinding;
    private String billId;
    private int inType;
    private int type;//1:总收入 2:总支出
    private ImageLoaderConfig config;
    private IncomeBean incomeBean;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_bill_details;
    }

    @Override
    protected void init() {
        initView();
        hasExtras();
    }

    private void initView() {
        StatusBarUtil.setLightMode(this);
        mBinding = (ActivityBillDetailsBinding) viewDataBinding;
        mBinding.titleView.setLeftClickListener(view -> finish());

        if (config == null) {
            config = new ImageLoaderConfig.Builder().
                    setAsBitmap(true).
                    setPlaceHolderResId(R.drawable.icon_servicerevenue).
                    setErrorResId(R.drawable.icon_servicerevenue).
                    setDiskCacheStrategy(ImageLoaderConfig.DiskCache.SOURCE).
                    setPrioriy(ImageLoaderConfig.LoadPriority.HIGH).build();
        }
    }

    private void hasExtras() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            billId = bundle.getString("billId");
            inType = bundle.getInt("inType", 1);
            type = bundle.getInt("type", 1);
            incomeBean = (IncomeBean) bundle.getSerializable("IncomeBean");
        }

        if (incomeBean != null) {//来自：收入明细-余额
            mBinding.tvRemark.setText(incomeBean.getRemark());

            //金额保留两位小数
            DecimalFormat df = new DecimalFormat("0.00");
            String changebalance = df.format(incomeBean.getChangebalance());
            if (incomeBean.getStatus() == 1) {// status 0:减少 1：增加
                mBinding.tvAmount.setText("+" + changebalance);
            } else {
                mBinding.tvAmount.setText("-" + changebalance);
            }
            mBinding.tvCreateTime.setText(incomeBean.getCreatetime());

        } else { //来自：总收入/支出
            loadData();
        }
    }

    private void loadData() {
        getMvpPresenter().getDetail(billId, inType, type);
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(message);
    }

    @Override
    public void getDetailSuccess(BillDetailsBean bean) {
        mBinding.tvBillName.setText(bean.getBillName());
        mBinding.tvStatus.setText(bean.getStatus());
        mBinding.tvTypeName.setText(bean.getTypeName());
        mBinding.tvClassify.setText(bean.getClassify());
        mBinding.tvCreateTime.setText(bean.getCreatetTime());

        //备注
        if (null != bean.getRemark() && !TextUtils.isEmpty(bean.getRemark())) {
            mBinding.trRemark.setVisibility(View.VISIBLE);
            mBinding.tvRemark.setText(bean.getRemark());
        } else {
            mBinding.trRemark.setVisibility(View.GONE);
        }

        //账户
        if (null != bean.getUserAccount() && !TextUtils.isEmpty(bean.getUserAccount())) {
            mBinding.trUserAccount.setVisibility(View.VISIBLE);
            mBinding.tvUserAccount.setText(bean.getUserAccount());
        } else {
            mBinding.trUserAccount.setVisibility(View.GONE);
        }

        //对方账户
        if (null != bean.getOtherAccount() && !TextUtils.isEmpty(bean.getOtherAccount())) {
            mBinding.trOtherAccount.setVisibility(View.VISIBLE);
            mBinding.tvOtherAccount.setText(bean.getOtherAccount());
        } else {
            mBinding.trOtherAccount.setVisibility(View.GONE);
        }

        //金额保留两位小数
        DecimalFormat df = new DecimalFormat("0.00");
        String ammount = df.format(bean.getAmmount());
        if (type == 1) {
            mBinding.tvAmount.setText("+" + ammount);//收入金额
            if (inType == 1) { //注册奖金
                mBinding.ivPic.setImageResource(R.drawable.icon_bonus);
            } else if (inType == 2) {//推广佣金
                mBinding.ivPic.setImageResource(R.drawable.icon_commission);
            } else if (inType == 3) {//红包
                ImageLoader.loadImage(mBinding.ivPic, bean.getUserImg(), config, null);
            } else if (inType == 4) {//服务收入
                mBinding.ivPic.setImageResource(R.drawable.icon_servicerevenue);
            } else if (inType == 5) {//转账
                ImageLoader.loadImage(mBinding.ivPic, bean.getUserImg(), config, null);
            } else if (inType == 6) {//订单退款
                mBinding.ivPic.setImageResource(R.drawable.icon_servicerevenue);
            } else if (inType == 7) {//提现驳回
                mBinding.ivPic.setImageResource(R.drawable.icon_propose_nor);
            } else if (inType == 8) {//系统清算
                mBinding.ivPic.setImageResource(R.drawable.icon_system_nor);
            } else if (inType == 9) {//转可提现
                mBinding.ivPic.setImageResource(R.drawable.icon_propose_nor);
            } else {//其他
                mBinding.ivPic.setImageResource(R.drawable.icon_other_nor);
            }
        } else {
            mBinding.tvAmount.setText("-" + ammount);//支出金额
            if (inType == 1) {//提现
                mBinding.ivPic.setImageResource(R.drawable.icon_propose_nor);
            } else if (inType == 2) {//转账
                ImageLoader.loadImage(mBinding.ivPic, bean.getUserImg());
            } else if (inType == 3) {//红包
                ImageLoader.loadImage(mBinding.ivPic, bean.getUserImg());
            } else if (inType == 4) {//违约扣款
                mBinding.ivPic.setImageResource(R.drawable.icon_deduction_nor);
            } else if (inType == 5) {//购买
                mBinding.ivPic.setImageResource(R.drawable.icon_servicerevenue);
            } else if (inType == 6) {//系统清算
                mBinding.ivPic.setImageResource(R.drawable.icon_system_nor);
            } else {//其他
                mBinding.ivPic.setImageResource(R.drawable.icon_other_nor);
            }
        }

    }

    @Override
    public void getDetailFail() {
        showToast("获取账单详情失败");
    }
}
