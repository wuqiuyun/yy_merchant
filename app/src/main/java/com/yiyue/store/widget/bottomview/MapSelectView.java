package com.yiyue.store.widget.bottomview;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.view.View;

import com.amap.api.maps.model.LatLng;
import com.yiyue.store.R;
import com.yiyue.store.component.databind.ClickHandler;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ViewMapSelectBinding;
import com.yiyue.store.model.vo.bean.LocationBean;
import com.yiyue.store.util.ColorUtil;
import com.yiyue.store.widget.bottomview.base.BaseBottomView;

/**
 * Created by zm on 2018/11/22.
 */
public class MapSelectView extends BaseBottomView implements ClickHandler{
    public static double pi = 3.141592653589793 * 3000.0 / 180.0;

    //百度地图URL跳转到步行路线的参数

    //头部 添加相应地区
    private final static String BAIDU_HEAD = "baidumap://map/direction?region=0";
    //起点的经纬度
    private final static String BAIDU_ORIGIN = "&origin=";
    //终点的经纬度
    private final static String BAIDU_DESTINATION = "&destination=";
    //路线规划方式
    private final static String BAIDU_MODE = "&mode=driving";
    // 坐标类型
    private final static String BAIDU_COORD_TYPE = "&coord_type=gcj02";
    //百度地图的包名
    private final static String BAIDU_PKG = "com.baidu.BaiduMap";


    //高德地图URL跳转到步行路线的参数

    //头部 后面的sourceApplicaiton填自己APP的名字
    private final static String GAODE_HEAD = "androidamap://route?sourceApplication=BaiduNavi";
    //起点经度
    private final static String GAODE_SLON = "&slon=";
    //起点纬度
    private final static String GAODE_SLAT = "&slat=";
    //起点名字
    private final static String GAODE_SNAME = "&sname=";
    //终点经度
    private final static String GAODE_DLON = "&dlon=";
    //终点纬度
    private final static String GAODE_DLAT = "&dlat=";
    //终点名字
    private final static String GAODE_DNAME = "&dname=";
    // dev 起终点是否偏移(0:lat 和 lon 是已经加密后的,不需要国测加密; 1:需要国测加密)
    // t = 1(公交) =2（驾车） =4(步行)
    private final static String GAODE_MODE ="&dev=0&t=0";
    //高德地图包名
    private final static String GAODE_PKG = "com.autonavi.minimap";


    //腾讯地图URL跳转到路线的参数

    //头部 type出行方式
    private final static String TX_HEAD = "qqmap://map/routeplan?type=walk";
    //起点名称
    private final static String TX_FROM = "&from=";
    //起点的经纬度
    private final static String TX_FROMCOORD = "&fromcoord=";
    //终点名称
    private final static String TX_TO = "&to=";
    //终点的经纬度
    private final static String TX_TOCOORD = "&tocoord=";
    /**
     本参数取决于type参数的取值
     公交：type=bus，policy有以下取值
     0：较快捷
     1：少换乘
     2：少步行
     3：不坐地铁
     驾车：type=drive，policy有以下取值
     0：较快捷
     1：无高速
     2：距离
     policy的取值缺省为0
     */
    private final static String TX_END = "&policy=1&referer=myapp";
    //腾讯地图包名
    private final static String TX_PKG = "com.tencent.map";


    private LocationBean mStart,mEnd;
    private boolean isInstanllMap = false;

    private ViewMapSelectBinding mBinding;

    public MapSelectView(Context context) {
        super(context, R.style.BottomViewTheme_Default, R.layout.view_map_select);
        mBinding = DataBindingUtil.bind(rootView);
        mBinding.setClick(this);

        initView();
    }

    private void initView() {
        if (checkMapAppsIsExist(mContext, GAODE_PKG)) {
            mBinding.tvGaode.setVisibility(View.VISIBLE);
            isInstanllMap = true;
        }
        if (checkMapAppsIsExist(mContext, BAIDU_PKG)) {
            mBinding.tvBaidu.setVisibility(View.VISIBLE);
            isInstanllMap = true;
        }
        if (checkMapAppsIsExist(mContext, TX_PKG)) {
            mBinding.tvTengxun.setVisibility(View.VISIBLE);
            isInstanllMap = true;
        }

        if (!isInstanllMap) {
            mBinding.llMapList.setVisibility(View.GONE);
            mBinding.btnCancel.setText("未找到地图应用.");
            mBinding.btnCancel.setTextColor(ColorUtil.getColor(R.color.dark_gray));
        }else {
            mBinding.btnCancel.setText("取消");
            mBinding.btnCancel.setTextColor(ColorUtil.getColor(R.color.text_color5));
        }
    }

    public void setStartLocation(double lat, double lon, String address) {
        this.mStart = new LocationBean();
        this.mStart.setLatitude(lat);
        this.mStart.setLongitude(lon);
        this.mStart.setName(address);
    }

    public void setEndLocation(double lat, double lon, String address) {
        this.mEnd = new LocationBean();
        this.mEnd.setLatitude(lat);
        this.mEnd.setLongitude(lon);
        this.mEnd.setName(address);
    }

    /**
     * 检测应用是否安装
     * @param context
     * @param packagename
     * @return
     */
    public boolean checkMapAppsIsExist(Context context,String packagename){
        PackageInfo packageInfo;
        try{
            packageInfo = context.getPackageManager().getPackageInfo(packagename,0);
        }catch (Exception e){
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null){
            return false;
        }else{
            return true;
        }
    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.tv_gaode:
                dismissBottomView();
                if(mEnd == null || mEnd.getLatitude() <= 0 && mEnd.getLongitude() <= 0) {
                    ToastUtils.shortToast("门店位置异常.");
                    return;
                }

                if (checkMapAppsIsExist(mContext,GAODE_PKG)){
                    StringBuilder uriPath = new StringBuilder();
                    uriPath.append(GAODE_HEAD);
                    if (mStart != null) {
                        uriPath.append(GAODE_SLAT+mStart.getLatitude()+GAODE_SLON+mStart.getLongitude()+mStart.getName());
                    }
                    if (mEnd != null) {
                        uriPath.append(GAODE_DLAT+mEnd.getLatitude()+GAODE_DLON+mEnd.getLongitude()+
                                GAODE_DNAME+mEnd.getName());
                    }
                    uriPath.append(GAODE_MODE);

                    intent.setAction("android.intent.action.VIEW");
                    intent.setPackage("com.autonavi.minimap");
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse(uriPath.toString()));
                    mContext.startActivity(intent);
                }else {
                    ToastUtils.shortToast("高德地图未安装");
                }
                break;
            case R.id.tv_baidu:
                dismissBottomView();
                if(mEnd == null || mEnd.getLatitude() <= 0 && mEnd.getLongitude() <= 0) {
                    ToastUtils.shortToast("门店位置异常.");
                    return;
                }

                if (checkMapAppsIsExist(mContext,BAIDU_PKG)){
                    intent.setData(Uri.parse(BAIDU_HEAD+BAIDU_ORIGIN+mStart.getLatitude()
                            +","+mStart.getLongitude()+BAIDU_DESTINATION+mEnd.getLatitude()+","+mEnd.getLongitude()
                            +BAIDU_COORD_TYPE + BAIDU_MODE));
                    mContext.startActivity(intent);
                }else {
                    ToastUtils.shortToast("百度地图未安装");
                }
                break;
            case R.id.tv_tengxun:
                dismissBottomView();
                if(mEnd == null || mEnd.getLatitude() <= 0 && mEnd.getLongitude() <= 0) {
                    ToastUtils.shortToast("门店位置异常.");
                    return;
                }

                if (checkMapAppsIsExist(mContext, TX_PKG)){
                    //Toast.makeText(MainActivity.this,"百度地图已经安装",Toast.LENGTH_SHORT).show();
                    intent.setData(Uri.parse(TX_HEAD + TX_FROM+mStart.getName()+TX_FROMCOORD+mStart.getLatitude()+
                            ","+mStart.getLongitude()+TX_TO+mEnd.getName()+TX_TOCOORD+mEnd.getLatitude()+
                            ","+mEnd.getLongitude()+TX_END));
                    mContext.startActivity(intent);
                }else {
                    ToastUtils.shortToast("腾讯地图未安装");
                }
                break;
            case R.id.btn_cancel:
                dismissBottomView();
                break;
        }
    }

    /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 将 GCJ-02 坐标转换成 BD-09 坐标
     *
     * @param latLng
     * @return
     */
    public static LatLng GCJ02ToBD09(LatLng latLng) {
        double x = latLng.longitude, y = latLng.latitude;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * pi);
        double bd_lon = z * Math.cos(theta) + 0.0065;
        double bd_lat = z * Math.sin(theta) + 0.006;
        return new LatLng(bd_lon, bd_lat);
    }
}
