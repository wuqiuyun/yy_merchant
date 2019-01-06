package com.yiyue.store.module.im.immap;

import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.model.LatLng;
import com.yiyue.store.R;
import com.yiyue.store.base.fragment.BaseMapFragment;
import com.yiyue.store.databinding.FragmentImmapLayoutBinding;
import com.yiyue.store.widget.bottomview.BottomViewFactory;
import com.yiyue.store.widget.bottomview.MapSelectView;

/**
 * Created by zhangzz on 2018/11/19
 */
public class ImMapFragment extends BaseMapFragment<ImMapView, ImMapPresenter> implements ImMapView {
    FragmentImmapLayoutBinding mBinding;
    private double latitude;
    private double longitude;
    private String address;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_immap_layout;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mBinding.mapRoot.onSaveInstanceState(outState);
    }

    @Override
    protected void initView() {
        //116.34008,39.884916

        mBinding = (FragmentImmapLayoutBinding) viewDataBinding;
        mBinding.mapRoot.onCreate(savedInstanceState);
        isAutoManageLocation = false;
        initMap(mBinding.mapRoot);

        restartLocation();

        double lat = getArguments().getDouble("lat", 39.884916);
        double lon = getArguments().getDouble("lon", 116.34008);
        String address = getArguments().getString("address");

        LatLng lastLatLng = new LatLng(lat, lon);
        locationMarkerChanged(lastLatLng);

        mBinding.titleView.setLeftClickListener(v -> {
            getActivity().finish();
        });
        mBinding.titleView.setRightTextClickListener(v -> {
            // 选择地图
            MapSelectView mapView = (MapSelectView) BottomViewFactory.create(getActivity(), BottomViewFactory.Type.Map);
            if (latitude > 0 && longitude > 0) {
                mapView.setStartLocation(latitude, longitude, address);
            }
            mapView.setEndLocation(lat, lon, address);
            mapView.showBottomView(true);
        });
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void showToast(String message) {

    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        super.onLocationChanged(aMapLocation);
        latitude = aMapLocation.getLatitude();
        longitude = aMapLocation.getLongitude();
        address = aMapLocation.getAddress();
    }
}
