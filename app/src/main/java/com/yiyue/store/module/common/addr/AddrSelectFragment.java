package com.yiyue.store.module.common.addr;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.model.CameraPosition;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yiyue.store.R;
import com.yiyue.store.base.adapter.BaseRecycleViewAdapter;
import com.yiyue.store.base.fragment.BaseMapFragment;
import com.yiyue.store.component.databind.ClickHandler;
import com.yiyue.store.component.recycleview.SpaceItemDecoration;
import com.yiyue.store.databinding.FragmentAddressSelectBinding;
import com.yiyue.store.model.vo.bean.AddrInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zm on 2018/10/19.
 */
@CreatePresenter(AddrSelectPresenter.class)
public class AddrSelectFragment extends BaseMapFragment<IAddrSelectView, AddrSelectPresenter>
        implements IAddrSelectView, ClickHandler {

    private FragmentAddressSelectBinding mBinding;
    private AddrSelectAdapter mSelectAdapter;
    private AddrSelectAdapter mSearchAdapter;

    private String cityId; // 城市id

    public static Fragment newInstance() {
        return new AddrSelectFragment();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mBinding.mapRoot.onSaveInstanceState(outState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_address_select;
    }

    @Override
    protected void initView() {
        mBinding = (FragmentAddressSelectBinding) viewDataBinding;
        mBinding.setClick(this);

        // initMap
        mBinding.mapRoot.onCreate(savedInstanceState);
        initMap(mBinding.mapRoot);

        // select address recycleView
        mBinding.selectAddrList.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.selectAddrList.addItemDecoration(new SpaceItemDecoration(0));
        mSelectAdapter = new AddrSelectAdapter(getContext());
        mSelectAdapter.setItemListener(new BaseRecycleViewAdapter.SimpleRecycleViewItemListener(){
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("data", mSelectAdapter.getDatas().get(position));
                Intent intent = new Intent();
                intent.putExtras(bundle);
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }
        });
        mBinding.selectAddrList.setAdapter(mSelectAdapter);

        // search address recycleView
        mBinding.searchAddrList.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.searchAddrList.addItemDecoration(new SpaceItemDecoration(0));
        mSearchAdapter = new AddrSelectAdapter(getContext());
        mSearchAdapter.setItemListener(new BaseRecycleViewAdapter.SimpleRecycleViewItemListener(){
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("data", mSearchAdapter.getDatas().get(position));
                Intent intent = new Intent();
                intent.putExtras(bundle);
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }
        });
        mBinding.searchAddrList.setAdapter(mSearchAdapter);

        mBinding.etKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    mSearchAdapter.clearData(true);
                    mBinding.searchAddrList.setVisibility(View.GONE);
                    return;
                }
                mBinding.searchAddrList.setVisibility(View.VISIBLE);
                String str = s.toString().trim();
                if (!TextUtils.isEmpty(str)) {
                    getMvpPresenter().queryKeyword(getContext(), str, cityId);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onCameraChangeFinish(CameraPosition cameraPosition) {
        startLocationAnimation();
        getMvpPresenter().queryBound(getContext(), cameraPosition.target.latitude, cameraPosition.target.longitude);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        super.onLocationChanged(aMapLocation);
        cityId = aMapLocation.getCityCode();
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void showToast(String message) {

    }

    /**
     * 开启位置选择动画
     */
    private void startLocationAnimation() {
        mBinding.mvLocation.startAnim(null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel: // 取消
                getActivity().finish();
                break;
            case R.id.iv_location: // 定位
                new RxPermissions(getActivity()).request(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        .subscribe(granted -> {
                            if (granted) {
                                restartLocation();
                            } else {
                                // TODO 打开设置界面
                            }
                        });
                moveToLastLocation();
                break;
        }
    }

    @Override
    public void onSearchedList(List<AddrInfoBean> addrInfoBeans) {
        mSearchAdapter.setDatas((ArrayList<AddrInfoBean>) addrInfoBeans, true);
    }

    @Override
    public void onSelectedList(List<AddrInfoBean> addrInfoBeans) {
        mSelectAdapter.setDatas((ArrayList<AddrInfoBean>) addrInfoBeans, true);
    }
}
