package com.yiyue.store.module.mine.wallet.statistics.StylistStatistics;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yiyue.store.R;
import com.yiyue.store.base.adapter.BaseRecycleViewAdapter;
import com.yiyue.store.base.fragment.BaseMvpFragment;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.FragmentStylistStatisticsBinding;
import com.yiyue.store.model.vo.bean.EventOrderBean;
import com.yiyue.store.model.vo.bean.OrderChartBean;
import com.yiyue.store.model.vo.bean.StoreOrderBean;
import com.yiyue.store.model.vo.bean.StoreSuccessOrdersBean;
import com.yiyue.store.module.mine.wallet.statistics.OrderStatisticsPresenter;
import com.yiyue.store.module.mine.wallet.statistics.OrderStatisticsView;
import com.yiyue.store.module.mine.wallet.orderdetil.userorder.UserOrderDetilActivity;
import com.yiyue.store.util.FormatUtil;
import com.yiyue.store.util.TypeConvertUtils;
import com.yl.core.component.mvp.factory.CreatePresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单统计
 * Created by wqy on 2018/12/4.
 */
@CreatePresenter(OrderStatisticsPresenter.class)
public class StylistStatisticsFragment extends BaseMvpFragment<OrderStatisticsView, OrderStatisticsPresenter>
        implements OrderStatisticsView{
    private FragmentStylistStatisticsBinding mBinding;
    private String orderType;
    private String nexus = "2";
    private StylistStatisticsAdapter mAdapter;
    private DecimalFormat mDecimalFormat = new DecimalFormat("0.00");

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_stylist_statistics;
    }

    public static Fragment newInstance(int orderType) {
        StylistStatisticsFragment stylistStatisticsFragment = new StylistStatisticsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("orderType", orderType);
        stylistStatisticsFragment.setArguments(bundle);
        return stylistStatisticsFragment;
    }


    @Override
    protected void initView() {
        Bundle bundle = getArguments();
        orderType = String.valueOf(bundle.getInt("orderType", 0));
        mBinding = (FragmentStylistStatisticsBinding) viewDataBinding;
        //设置适配器
        RecyclerView orderRecycle = mBinding.orderRecycle;
        mAdapter = new StylistStatisticsAdapter(getContext());
        orderRecycle.setLayoutManager(new LinearLayoutManager(getContext()));
        orderRecycle.setAdapter(mAdapter);
        EventBus.getDefault().register(this);
        mAdapter.setItemListener(new BaseRecycleViewAdapter.SimpleRecycleViewItemListener(){
            @Override
            public void onItemClick(View view, int position) {
                StoreSuccessOrdersBean storeSuccessOrdersBean = mAdapter.getDatas().get(position);
                UserOrderDetilActivity.startActivity(getContext()
                        ,storeSuccessOrdersBean.getStylistId(),storeSuccessOrdersBean.getNexus(),storeSuccessOrdersBean.getHeadImg());
            }
        });
    }


    @Override
    protected void loadData() {
        getMvpPresenter().getStoreSuccessOrders(orderType, nexus);
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(message);
    }

    @Override
    public void onGetStoreOrderCountSuccess(StoreOrderBean bean) {

    }

    @Override
    public void getStoreTimeSliceOrder(List<List<OrderChartBean>> orderChartBeans) {

    }

    @Override
    public void getStoreSuccessOrders(ArrayList<StoreSuccessOrdersBean> storeSuccessOrdersBeans) {
        if (storeSuccessOrdersBeans == null&&storeSuccessOrdersBeans.size()==0) return;
        StoreSuccessOrdersBean bean = storeSuccessOrdersBeans.get(0);
        mBinding.tvCompleteOrder.setText(String.format(getString(R.string.order_number), FormatUtil.Formatstring(String.valueOf(bean.getNum()))));
        if (bean.getMoney() != null) {
            mBinding.tvIncome.setText(String.format(getString(R.string.RMB), FormatUtil.Formatstring(mDecimalFormat.format(TypeConvertUtils.convertToDouble(bean.getMoney(), 0)))));
        }
        storeSuccessOrdersBeans.remove(0);
        mAdapter.setDatas(storeSuccessOrdersBeans, true);
    }


    /**
     *刷新数据
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventOrderBean.OrderStatisticsBean event) {
        if (!event.getType().equals(orderType))return;
        if (event != null) {
            nexus = event.getNexus();
            getMvpPresenter().getStoreSuccessOrders(orderType, nexus);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
