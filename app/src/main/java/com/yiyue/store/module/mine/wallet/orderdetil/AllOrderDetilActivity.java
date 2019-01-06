package com.yiyue.store.module.mine.wallet.orderdetil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.base.adapter.BaseRecycleViewAdapter;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivityOrderDetilBinding;
import com.yiyue.store.model.vo.bean.OrderDetailBean;
import com.yiyue.store.model.vo.bean.RegisterGapBetweenBean;
import com.yiyue.store.module.home.order.details.OrderDetailsActivity;
import com.yiyue.store.util.RefreshLayoutUtil;
import com.yiyue.store.util.ScreenUtils;
import com.yiyue.store.util.TypeConvertUtils;
import com.yiyue.store.util.Utils;
import com.yiyue.store.widget.popwindow.PopupUtil;
import com.yiyue.store.widget.popwindow.TriangleDrawable;
import com.yiyue.store.widget.popwindow.XGravity;
import com.yiyue.store.widget.popwindow.YGravity;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 全部订单明细
 */
@CreatePresenter(AllOrderDetilPresenter.class)
public class AllOrderDetilActivity extends BaseMvpAppCompatActivity<IAllOrderDetilView, AllOrderDetilPresenter>
        implements IAllOrderDetilView,  OnLoadMoreListener, OnRefreshListener{

    private ActivityOrderDetilBinding mBinding;
    private PopupUtil mPopWindow;
    private AllOrderDetilAdapter mAdapter;
    private int pageNo=1;
    private int PageSize=10;
    private int type=1;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_order_detil;
    }

    @Override
    protected void init() {
        initView();
        loadData();
    }

    private void loadData() {
        getMvpPresenter().getRegisterGapBetween();
        //1 全部 2 结算 3 完成
        getMvpPresenter().findOrderDetail(type,pageNo,PageSize);
    }

    @SuppressLint("StringFormatInvalid")
    private void initView() {
        mBinding = (ActivityOrderDetilBinding) viewDataBinding;
        // 返回
        mBinding.titleView.setLeftClickListener(v -> finish());
        mBinding.titleView.setRightTextClickListener(v -> {
            showPop(mBinding.titleView.getRightText());
        });
         // mBinding.refreshLayout
        mBinding.refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        mBinding.refreshLayout.setRefreshFooter(new ClassicsFooter(this));
        mBinding.refreshLayout.setOnLoadMoreListener(this);
        mBinding.refreshLayout.setOnRefreshListener(this);
        //
        initPop();
        // recycleview
        mBinding.rvOrder.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new AllOrderDetilAdapter(this);
        mBinding.rvOrder.setAdapter(mAdapter);
        mAdapter.setItemListener(new BaseRecycleViewAdapter.SimpleRecycleViewItemListener(){
            @Override
            public void onItemClick(View view, int position) {
                OrderDetailsActivity.startActivity(AllOrderDetilActivity.this, String.valueOf(mAdapter.getDatas().get(position).getOrderId()));
            }
        });
    }


    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0,null);
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(message);
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        pageNo++;
        getMvpPresenter().findOrderDetail(type,pageNo,PageSize);
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        pageNo=1;
        getMvpPresenter().findOrderDetail(type,pageNo,PageSize);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
    private void initPop() {
        mPopWindow = PopupUtil.create()
                .setContext(this)
                .setContentView(R.layout.popwin_order_layout)
                .setAnimationStyle(R.style.AnimImPopwindow)
                .setOnViewListener(new PopupUtil.OnViewListener() {
                    @Override
                    public void initViews(View view, PopupUtil basePopup) {
                        View arrowView = view.findViewById(R.id.v_arrow);
                        arrowView.setBackground(new TriangleDrawable(TriangleDrawable.TOP, Color.WHITE));
                        view.findViewById(R.id.tv_all).setOnClickListener(v -> {
                            //全部
                            type=1;
                            pageNo=1;
                            getMvpPresenter().findOrderDetail(type,pageNo,PageSize);
                            mPopWindow.dismiss();
                        });

                        view.findViewById(R.id.tv_complete).setOnClickListener(v -> {
                            //已完成
                            type=2;
                            pageNo=1;
                            getMvpPresenter().findOrderDetail(type,pageNo,PageSize);
                            mPopWindow.dismiss();
                        });

                        view.findViewById(R.id.tv_settlement).setOnClickListener(v -> {
                            //已结算
                            type=3;
                            pageNo=1;
                            getMvpPresenter().findOrderDetail(type,pageNo,PageSize);
                            mPopWindow.dismiss();
                        });
                    }
                })
                .setFocusAndOutsideEnable(true)
                .setBackgroundDimEnable(true)
                .setDimValue(0.4f)
                .apply();
    }

    private void showPop(View view) {
        int offsetX = Utils.dp2px(this, 20) - view.getWidth() / 2;
        int offsetY = (mBinding.titleView.getHeight() - view.getHeight()) / 2;
        mPopWindow.showAtAnchorView(view, YGravity.BELOW, XGravity.ALIGN_RIGHT, offsetX, offsetY - 10);
    }


    @Override
    public void findOrderDetail(List<OrderDetailBean> list) {
        RefreshLayoutUtil.finishRefreshLayout(mBinding.refreshLayout);
        if (pageNo == 1) {
            mAdapter.setDatas((ArrayList<OrderDetailBean>) list, true);
        } else {
            mAdapter.addDatas((ArrayList<OrderDetailBean>) list, true);
        }

        if (list.size() < PageSize ) {// 加载的数据不够页面数量 则认为没有下一页
            mBinding.refreshLayout.setNoMoreData(true);
        } else {
            mBinding.refreshLayout.setNoMoreData(false);
        }
    }

    @Override
    public void getRegisterGapBetween(String data) {
        double amountCount = TypeConvertUtils.convertToDouble(data, 0);
        //订单业绩
        mBinding.tvAmountCount.setText(String.valueOf(amountCount));
        mBinding.pbAmountCount.setProgress((int) amountCount);
        ViewTreeObserver vto2 = mBinding.tvAmountCount.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mBinding.tvAmountCount.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                setViewLocation(AllOrderDetilActivity.this,mBinding.tvAmountCount,amountCount/200000
                        ,mBinding.pbAmountCount,mBinding.llAmountCount,mBinding.tvAmountCount.getWidth());
            }
        });
    }

    @Override
    public void findOrderDetailFail() {

    }

    /**
     * 设置位置
     * @param accountedFor 占比
     * @param tvWidth textView宽度
     */
    private void setViewLocation(Context context, TextView textView, double accountedFor, ProgressBar progressBar, LinearLayout linearLayout,int tvWidth) {
        if (accountedFor>1)accountedFor=1;
        ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(textView.getLayoutParams());

        int llWidth =linearLayout.getWidth();//父布局宽
        int pbWidth =progressBar.getWidth();//ProgressBar宽
        int aWidth = llWidth - pbWidth;//父宽度减ProgressBar宽

        double dpLeft = (pbWidth*accountedFor)+aWidth-(tvWidth/2);//偏移值
        if (dpLeft<aWidth)dpLeft=aWidth-tvWidth/2;//限制最小偏移
        if (dpLeft>llWidth-tvWidth)dpLeft=llWidth-tvWidth;//限制最大偏移

        int dpTop = ScreenUtils.dip2px(context,0);
        int dpRight = ScreenUtils.dip2px(context,0);
        int dpButtom=ScreenUtils.dip2px(context,5);
        margin.setMargins(TypeConvertUtils.convertToInt(dpLeft,0), dpTop, dpRight, dpButtom);
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(margin);
        textView.setLayoutParams(layoutParams);
    }
}
