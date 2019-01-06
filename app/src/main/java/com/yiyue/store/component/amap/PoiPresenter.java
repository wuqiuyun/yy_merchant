package com.yiyue.store.component.amap;

import android.content.Context;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.yiyue.store.model.vo.bean.AddrInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Poi 逆地理编码 反查
 * <p>
 * Created by zm on 2018/10/19.
 */
public class PoiPresenter implements PoiSearch.OnPoiSearchListener{

    private Context mContext;
    private OnPoiPresenterListener mOnPoiPresenterListener;

    public PoiPresenter(Context context) {
        mContext = context;
    }

    public PoiPresenter(Context context, OnPoiPresenterListener onPoiPresenterListener) {
        mOnPoiPresenterListener = onPoiPresenterListener;
        mContext = context;
    }

    public void setOnPoiPresenterListener(OnPoiPresenterListener onPoiPresenterListener) {
        mOnPoiPresenterListener = onPoiPresenterListener;
    }

    /**
     * 周边检索
     * @param latitude
     * @param longitude
     */
    public void queryBound(double latitude, double longitude) {
        PoiSearch.Query query = new PoiSearch.Query("", "", "");
        query.setPageSize(20); // 设置每页最多返回多少条poiitem
        query.setPageNum(0); // 查询第一页
        PoiSearch mPoiSearch = new PoiSearch(mContext, query);
        mPoiSearch.setOnPoiSearchListener(this);
        mPoiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(latitude, longitude), 1000));
        mPoiSearch.searchPOIAsyn();
    }

    /**
     * 关键字检索a
     * @param keyWord 关键字
     * @param city 城市id
     */
    public void queryPoi(String keyWord, String city) {
        PoiSearch.Query query = new PoiSearch.Query(keyWord, "", city);
        query.setPageSize(20); // 设置每页最多返回多少条poiitem
        query.setPageNum(0); // 查询第一页
        PoiSearch mPoiSearch = new PoiSearch(mContext, query);
        mPoiSearch.setOnPoiSearchListener(this);
        mPoiSearch.searchPOIAsyn();
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        if (poiResult == null || poiResult.getPois() == null) {
            if (mOnPoiPresenterListener != null)
                mOnPoiPresenterListener.onSearchedList(null);
            return;
        }
        List<AddrInfoBean> addrInfoBeans = new ArrayList<>();
        for (PoiItem poiItems : poiResult.getPois()) {
            AddrInfoBean infoBean = new AddrInfoBean();
            infoBean.setCity(poiItems.getCityName());
            infoBean.setCityId(poiItems.getCityCode());
            infoBean.setProvinceId(poiItems.getProvinceCode());
            infoBean.setProvinceName(poiItems.getProvinceName());
            infoBean.setLat(poiItems.getLatLonPoint().getLatitude());
            infoBean.setLon(poiItems.getLatLonPoint().getLongitude());
            infoBean.setAdId(poiItems.getAdCode());
            infoBean.setAdName(poiItems.getAdName());
            infoBean.setAddr(poiItems.getTitle());
            infoBean.setAddrDetail(poiItems.getSnippet());
            addrInfoBeans.add(infoBean);
        }
        if (mOnPoiPresenterListener != null)
            mOnPoiPresenterListener.onSearchedList(addrInfoBeans);
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    public interface OnPoiPresenterListener {
        void onPoiSearched(AddrInfoBean addrInfoBean);

        void onSearchedList(List<AddrInfoBean> addrInfoBeans);

        void onPoiError(Throwable throwable);
    }
}
