package com.yiyue.store.model.vo.requestbody;

import android.text.TextUtils;

import com.yiyue.store.component.toast.ToastUtils;

/**
 * Created by zm on 2018/10/18.
 */
public class DoStoreDataRequestBody {

          private String cityGDId = "0"; // 城市id
          private String districtGDId = "0"; // 区id
          private double latitude; // 维度
          private String location; // 位置信息
          private double longitude; // 经度
          private String mobile; // 手机号码
          private String provinceGDId = "0"; // 省份id
          private String storename; // 门店名称
          private String userId; // 用户id

    private DoStoreDataRequestBody(Builder builder) {
        cityGDId = builder.cityGDId;
        districtGDId = builder.districtGDId;
        latitude = builder.latitude;
        location = builder.location;
        longitude = builder.longitude;
        mobile = builder.mobile;
        provinceGDId = builder.provinceGDId;
        storename = builder.storename;
        userId = builder.userId;
    }


    /**
     * 检查门店参数
     * @return
     */
    public boolean checkStoreParams() {
        if (TextUtils.isEmpty(storename)) {
            ToastUtils.shortToast("门店名称不能为空");
            return false;
        }
        if (TextUtils.isEmpty(location)) {
            ToastUtils.shortToast("详细地址不能为空");
            return false;
        }
        return true;
    }

    /**
     * 检查地址参数
     * @return
     */
    public boolean checkAddressParams() {

        if (TextUtils.isEmpty(location)) {
            ToastUtils.shortToast("信息地址不能为空");
            return false;
        }
        return true;
    }


    public static final class Builder {
        private String cityGDId;
        private String districtGDId;
        private double latitude;
        private String location;
        private double longitude;
        private String mobile;
        private String provinceGDId;
        private String storename;
        private String userId;

        public Builder() {
        }

        public Builder cityGDId(String val) {
            cityGDId = val;
            return this;
        }

        public Builder districtGDId(String val) {
            districtGDId = val;
            return this;
        }

        public Builder latitude(double val) {
            latitude = val;
            return this;
        }

        public Builder location(String val) {
            location = val;
            return this;
        }

        public Builder longitude(double val) {
            longitude = val;
            return this;
        }

        public Builder mobile(String val) {
            mobile = val;
            return this;
        }

        public Builder provinceGDId(String val) {
            provinceGDId = val;
            return this;
        }

        public Builder storename(String val) {
            storename = val;
            return this;
        }

        public Builder userId(String val) {
            userId = val;
            return this;
        }

        public DoStoreDataRequestBody build() {
            return new DoStoreDataRequestBody(this);
        }
    }
}
