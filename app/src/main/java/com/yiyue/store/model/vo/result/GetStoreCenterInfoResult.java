package com.yiyue.store.model.vo.result;

import android.text.TextUtils;

import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.model.vo.bean.CategoryBean;
import com.yiyue.store.model.vo.bean.StoreLocationBean;

import java.util.List;

/**
 * Created by zm on 2018/10/26.
 */
public class GetStoreCenterInfoResult extends BaseResponse<GetStoreCenterInfoResult.Data>{

    public static class Data {
        private String storeId;
        private String imgPath;
        private String storeName;
        private String station;
        private String showLocation;
        private StoreLocationBean storeLocation;
        List<String> environmentImg;
        List<String> coverImg;
        List<CategoryBean> services;

        public String getStoreId() {
            return storeId;
        }

        public void setStoreId(String storeId) {
            this.storeId = storeId;
        }

        public String getImgPath() {
            return imgPath;
        }

        public void setImgPath(String imgPath) {
            this.imgPath = imgPath;
        }

        public String getStoreName() {
            return storeName;
        }

        public void setStoreName(String storeName) {
            this.storeName = storeName;
        }

        public String getStation() {
            return TextUtils.isEmpty(station) ? "0" : station;
        }

        public void setStation(String station) {
            this.station = station;
        }

        public String getShowLocation() {
            return showLocation;
        }

        public void setShowLocation(String showLocation) {
            this.showLocation = showLocation;
        }

        public StoreLocationBean getStoreLocation() {
            return storeLocation;
        }

        public void setStoreLocation(StoreLocationBean storeLocation) {
            this.storeLocation = storeLocation;
        }

        public List<String> getEnvironmentImg() {
            return environmentImg;
        }

        public void setEnvironmentImg(List<String> environmentImg) {
            this.environmentImg = environmentImg;
        }

        public List<String> getCoverImg() {
            return coverImg;
        }

        public void setCoverImg(List<String> coverImg) {
            this.coverImg = coverImg;
        }

        public List<CategoryBean> getServices() {
            return services;
        }

        public void setServices(List<CategoryBean> services) {
            this.services = services;
        }
    }
}
