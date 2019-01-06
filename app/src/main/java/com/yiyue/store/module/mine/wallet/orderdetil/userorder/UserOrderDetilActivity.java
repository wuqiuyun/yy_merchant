package com.yiyue.store.module.mine.wallet.orderdetil.userorder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.yiyue.store.databinding.ActivityUserOrderDetilBinding;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.vo.bean.OrderDetailBean;
import com.yiyue.store.model.vo.bean.RegisterGapBetweenBean;
import com.yiyue.store.model.vo.bean.StoreStylistBean;
import com.yiyue.store.model.vo.bean.UserOrderBean;
import com.yiyue.store.module.home.order.details.OrderDetailsActivity;
import com.yiyue.store.module.mine.wallet.orderdetil.AllOrderDetilActivity;
import com.yiyue.store.module.mine.wallet.orderdetil.AllOrderDetilAdapter;
import com.yiyue.store.module.mine.wallet.orderdetil.AllOrderDetilPresenter;
import com.yiyue.store.module.mine.wallet.orderdetil.IAllOrderDetilView;
import com.yiyue.store.util.RefreshLayoutUtil;
import com.yiyue.store.util.ScreenUtils;
import com.yiyue.store.util.TypeConvertUtils;
import com.yiyue.store.util.Utils;
import com.yiyue.store.widget.mytimepickview.CustomDatePicker;
import com.yiyue.store.widget.popwindow.PopupUtil;
import com.yiyue.store.widget.popwindow.TriangleDrawable;
import com.yiyue.store.widget.popwindow.XGravity;
import com.yiyue.store.widget.popwindow.YGravity;
import com.yl.core.component.image.ImageLoader;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.DateUtil;
import com.yl.core.util.StatusBarUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.annotations.NonNull;


/**
 * 全部订单明细
 */
@CreatePresenter(UserOrderDetilPresenter.class)
public class UserOrderDetilActivity extends BaseMvpAppCompatActivity<IUserOrderDetilView, UserOrderDetilPresenter>
        implements IUserOrderDetilView,  OnLoadMoreListener, OnRefreshListener{

    private ActivityUserOrderDetilBinding mBinding;
    private UserOrderDetilAdapter mAdapter;
    private int pageNo=1;
    private int PageSize=10;
    private String stylistId;
    private String requestData;
    public  static String STYLIST_NEXUS="nexus";
    public  static String STYLIST_PHOTO="photo";
    private CustomDatePicker mDatePicker;
    private String data =DateUtil.date2Str(new Date(),DateUtil.FORMAT_YMD);

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_user_order_detil;
    }

    @Override
    protected void init() {
        initView();
        loadData();
    }

    private void loadData() {
        Intent intent = getIntent();
        if (intent==null)return;
        stylistId = intent.getStringExtra(Constants.STYLIST_ID);
       String nexus = intent.getStringExtra(STYLIST_NEXUS);
        String[] split = data.split("-");
        requestData = split[0]+split[1];

        if (nexus != null&&!nexus.equals("null")) {
            if (nexus.equals("0")){
                mBinding.tvStoreType.setText(getString(R.string.stylist_join));
            }else{
                mBinding.tvStoreType.setText(getString(R.string.stylist_signing));
            }
        }
        mBinding.tvDate.setText(TypeConvertUtils.convertToInt(data.split("-")[1],0)+"月");
        ImageLoader.loadImage(mBinding.ivPhoto,intent.getStringExtra(STYLIST_PHOTO));

        getMvpPresenter().findOrderDetailByStoreAndStylist(stylistId,requestData,pageNo,PageSize);
        getMvpPresenter().findOrderIncomeSumByStoreAndStylist(stylistId,requestData);
    }

    @SuppressLint("StringFormatInvalid")
    private void initView() {
        mBinding = (ActivityUserOrderDetilBinding) viewDataBinding;
        // 返回
        mBinding.titleView.setLeftClickListener(v -> finish());
        mBinding.titleView.setRightImgClickListener(v -> {
            mDatePicker.show(data);
        });
        //初始化日期选择
        initDatePicker();
//        // mBinding.refreshLayout
        mBinding.refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        mBinding.refreshLayout.setRefreshFooter(new ClassicsFooter(this));
        mBinding.refreshLayout.setOnLoadMoreListener(this);
        mBinding.refreshLayout.setOnRefreshListener(this);
        //
//        // recycleview
        mBinding.rvOrder.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new UserOrderDetilAdapter(this);
        mBinding.rvOrder.setAdapter(mAdapter);
        mAdapter.setItemListener(new BaseRecycleViewAdapter.SimpleRecycleViewItemListener(){
            @Override
            public void onItemClick(View view, int position) {
                OrderDetailsActivity.startActivity(UserOrderDetilActivity.this, mAdapter.getDatas().get(position).getOrderId());
            }
        });
    }

    private void initDatePicker() {
        String mNowDate = DateUtil.date2Str(new Date(), DateUtil.FORMAT_YMD);
        //刷新数据
        mDatePicker = new CustomDatePicker(this, "2018-01-01", mNowDate, time -> {
            data=time;
            pageNo=1;
            //刷新数据
            String[] split = data.split("-");
            requestData=split[0]+split[1];
            mBinding.tvDate.setText(TypeConvertUtils.convertToInt(data.split("-")[1],0)+"月");
            getMvpPresenter().findOrderDetailByStoreAndStylist(stylistId,requestData,pageNo,PageSize);
            getMvpPresenter().findOrderIncomeSumByStoreAndStylist(stylistId,requestData);
        });
        //隐藏Day
        mDatePicker.setHideDay(true);
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
        getMvpPresenter().findOrderDetailByStoreAndStylist(stylistId,requestData,pageNo,PageSize);

    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        pageNo=1;
        getMvpPresenter().findOrderDetailByStoreAndStylist(stylistId,requestData,pageNo,PageSize);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
    /**
     * @param context
     * @param stylistId 美发师ID
     */
    public static void startActivity (Context context,@NonNull String stylistId,@NonNull String nexus,@NonNull String photo) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.STYLIST_ID, stylistId);
        bundle.putString(STYLIST_NEXUS, nexus);
        bundle.putString(STYLIST_PHOTO, photo);
        startActivity(context, UserOrderDetilActivity.class, bundle);
    }

    @Override
    public void getUserOrder(ArrayList<UserOrderBean> list) {
        RefreshLayoutUtil.finishRefreshLayout(mBinding.refreshLayout);
        if (list==null)list=new ArrayList<>();
        if (pageNo == 1) {
            mAdapter.setDatas(list, true);
        } else {
            mAdapter.addDatas(list, true);
        }
        if (list.size() < PageSize ) {// 加载的数据不够页面数量 则认为没有下一页
            mBinding.refreshLayout.setNoMoreData(true);
        } else {
            mBinding.refreshLayout.setNoMoreData(false);
        }


    }

    @Override
    public void getOrderIncomeSum(StoreStylistBean storeStylistBean) {
            mBinding.tvName.setText(storeStylistBean.getStylistName());
            if (storeStylistBean.getNexustime()!=null)mBinding.tvTime.setText(String.format(storeStylistBean.getNexustime().split("\\s")[0]));
            mBinding.tvProfessor.setText(storeStylistBean.getPosition());
            mBinding.tvIncome.setText(String.valueOf(storeStylistBean.getAmount()));
            mBinding.tvOrders.setText(String.valueOf(storeStylistBean.getOrderCount()));
    }

    @Override
    public void getFail() {
        RefreshLayoutUtil.finishRefreshLayout(mBinding.refreshLayout);
    }


}
