package com.yiyue.store.module.mine.store;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yiyue.store.R;
import com.yiyue.store.base.adapter.BaseRecycleViewAdapter;
import com.yiyue.store.base.fragment.BaseMvpFragment;
import com.yiyue.store.component.amap.LocationPresenter;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.FragmentStoreBinding;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.vo.bean.StoreBean;
import com.yiyue.store.module.home.store.StoreManagerActivity;
import com.yiyue.store.util.RefreshLayoutUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 门店
 * <p>
 * Created by zm on 2018/10/10.
 */
@CreatePresenter(StorePresenter.class)
public class StoreFragment extends BaseMvpFragment<IStoreView, StorePresenter> 
        implements IStoreView,OnRefreshListener,OnLoadMoreListener , AMapLocationListener {

    FragmentStoreBinding binding;
    private StoreAdapter adapter;
    private ArrayList<StoreBean> data = new ArrayList<>();

    private SmartRefreshLayout refreshLayout;
    
    private LocationPresenter locationPresenter;

    private String mUserId;
    private double lat;
    private double lng;

    private int page = 1;//页数
    private int size = 10;//每页数量

    private int fromActivity;//从哪个页面来的

    public static Fragment newInstance(int from) {
        StoreFragment storeFragment = new StoreFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("from",from);
        storeFragment.setArguments(bundle);
        return storeFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_store;
    }

    @Override
    protected void initView() {
        Bundle bundle = getArguments();
        fromActivity = bundle.getInt("from", 0);
        
        binding = (FragmentStoreBinding) viewDataBinding;
        // init recycleview
        binding.recycleView.setLayoutManager(new LinearLayoutManager(getContext()));

        refreshLayout = binding.refreshLayout;
        refreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
        refreshLayout.setRefreshFooter(new ClassicsFooter(getContext()));
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
        
        adapter = new StoreAdapter(getContext());
        adapter.setItemListener(new BaseRecycleViewAdapter.SimpleRecycleViewItemListener(){
            @Override
            public void onItemClick(View view, int position) {
                // TODO 门店详情
                Bundle b = new Bundle();
                StoreBean store = adapter.getDatas().get(position);
                b.putInt(Constants.STORE_TYPE, Constants.STORE_COLLECT_FOOTPRINT);
                b.putString(Constants.STORE_ID, String.valueOf(store.getStoreId()));
                StoreManagerActivity.startActivity(getActivity(),StoreManagerActivity.class, b);
            }
        });
        binding.recycleView.setAdapter(adapter);

    }

    @Override
    protected void loadData() {
        mUserId = AccountManager.getInstance().getUserId();
        locationPresenter = new LocationPresenter(getContext());
        locationPresenter.setMapLocationListener(this);
        locationPresenter.startLocation();

        getStoreList(lat, lng, page, size, mUserId);
    }
    
    private void getStoreList(double lat, double lng, int page, int size, String mUserId) {
        switch (fromActivity) {
            case Constants.ACTIVITY_COLLECT :
                getMvpPresenter().getStoreCollection(lat, lng, page, size, mUserId);
                break;
            case Constants.ACTIVITY_FOOTPRINT :
                getMvpPresenter().getStoreFoot(lat, lng, page, size, mUserId);
                break;
            case 0:
                showToast("来源页获取错误");
                break;
        }
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(message);
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        page ++;
        lat = locationPresenter.getLastLatLng().latitude;
        lng = locationPresenter.getLastLatLng().longitude;
        getStoreList(lat, lng, page, size, mUserId);
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        page = 1;
        lat = locationPresenter.getLastLatLng().latitude;
        lng = locationPresenter.getLastLatLng().longitude;
        getStoreList(lat, lng, page, size, mUserId);
    }

    @Override
    public void getStoreListSuccess(List<StoreBean> list) {
        RefreshLayoutUtil.finishRefreshLayout(refreshLayout);
        ArrayList<StoreBean> newData = (ArrayList<StoreBean>) list;
        if (page == 1) {
            adapter.setDatas(newData, true);
        } else {
            adapter.addDatas(newData, true);
        }

        if (list.size() < size ) {// 加载的数据不够页面数量 则认为没有下一页
            refreshLayout.setNoMoreData(true);
        } else {
            refreshLayout.setNoMoreData(false);
        }
    }

    @Override
    public void getStoreListFail() {
        RefreshLayoutUtil.finishRefreshLayout(refreshLayout);
        refreshLayout.setNoMoreData(true);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (null != locationPresenter) {
            locationPresenter.stopLocation();
            locationPresenter = null;
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        lat = aMapLocation.getLatitude();
        lng = aMapLocation.getLongitude();
    }
}
