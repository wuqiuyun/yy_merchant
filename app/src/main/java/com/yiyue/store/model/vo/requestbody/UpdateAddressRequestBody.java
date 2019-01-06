package com.yiyue.store.model.vo.requestbody;

import android.text.TextUtils;

import com.yiyue.store.component.toast.ToastUtils;

/**
 * Created by zm on 2018/10/25.
 */
public class UpdateAddressRequestBody {

    private String cityGDId;
    private String districtGDId;
    private double latitude;
    private String location;
    private double longitude;
    private String provinceGDId;
    private String storeId;

    private UpdateAddressRequestBody(Builder builder) {
        cityGDId = builder.cityGDId;
        districtGDId = builder.districtGDId;
        latitude = builder.latitude;
        location = builder.location;
        longitude = builder.longitude;
        provinceGDId = builder.provinceGDId;
        storeId = builder.storeId;
    }

    /**
     * 检查门店参数
     * @return
     */
    public boolean checkStoreParams() {
        if (TextUtils.isEmpty(location)) {
            ToastUtils.shortToast("详细地址不能为空");
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
        private String provinceGDId;
        private String storeId;

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

        public Builder provinceGDId(String val) {
            provinceGDId = val;
            return this;
        }

        public Builder storeId(String val) {
            storeId = val;
            return this;
        }

        public UpdateAddressRequestBody build() {
            return new UpdateAddressRequestBody(this);
        }
    }
}
