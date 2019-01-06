package com.yiyue.store.module.common.addr;

import android.content.Context;
import android.text.TextUtils;

import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.amap.PoiPresenter;
import com.yiyue.store.model.vo.bean.AddrInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zm on 2018/10/19.
 */
public class AddrSelectPresenter extends BasePresenter<IAddrSelectView> {

    private PoiPresenter mPoiPresenter;

    /**
     * 周边查询
     * @param lat
     * @param lon
     */
    public void queryBound(Context context, double lat, double lon) {
        if (mPoiPresenter == null) {
            mPoiPresenter = new PoiPresenter(context);
        }
        mPoiPresenter.setOnPoiPresenterListener(new PoiPresenter.OnPoiPresenterListener() {
            @Override
            public void onPoiSearched(AddrInfoBean addrInfoBean) {

            }

            @Override
            public void onSearchedList(List<AddrInfoBean> addrInfoBeans) {
                if (addrInfoBeans == null || addrInfoBeans.size()==0){
                    getMvpView().onSelectedList(new ArrayList<>());
                    return;
                }
                getMvpView().onSelectedList(addrInfoBeans);
            }

            @Override
            public void onPoiError(Throwable throwable) {

            }
        });
        mPoiPresenter.queryBound(lat, lon);
    }

    /**
     * 关键字查寻
     * @param keyword
     * @param cityCode
     */
    public void queryKeyword(Context context, String keyword, String cityCode) {
        if (TextUtils.isEmpty(keyword )) return;
        if (TextUtils.isEmpty(cityCode)) return;

        if (mPoiPresenter == null) {
            mPoiPresenter = new PoiPresenter(context);
        }
        mPoiPresenter.setOnPoiPresenterListener(new PoiPresenter.OnPoiPresenterListener() {
            @Override
            public void onPoiSearched(AddrInfoBean addrInfoBean) {

            }

            @Override
            public void onSearchedList(List<AddrInfoBean> addrInfoBeans) {
                if (addrInfoBeans == null || addrInfoBeans.size()==0){
                    getMvpView().onSearchedList(new ArrayList<>());
                    return;
                }
                getMvpView().onSearchedList(addrInfoBeans);
            }

            @Override
            public void onPoiError(Throwable throwable) {

            }
        });
        mPoiPresenter.queryPoi(keyword, cityCode);
    }
}
