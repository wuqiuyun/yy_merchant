package com.yiyue.store.model.vo.requestbody;

import android.text.TextUtils;

import com.yiyue.store.component.toast.ToastUtils;

/**
 * Created by lvlong on 2018/10/29.
 */
public class CommitSetServiceBody {
    private String storeId;
    private String categoryIds;
    private String starttime;
    private String endtime;
    private String station;
    private String workday;
    private String id;

    @Override
    public String toString() {
        return "CommitSetServiceBody{" +
                "storeId='" + storeId + '\'' +
                ", categoryIds='" + categoryIds + '\'' +
                ", starttime='" + starttime + '\'' +
                ", endtime='" + endtime + '\'' +
                ", station='" + station + '\'' +
                ", workday='" + workday + '\'' +
                ", workday='" + id + '\'' +
                '}';
    }

    private CommitSetServiceBody(Builder builder) {
        storeId = builder.storeId;
        categoryIds = builder.categoryIds;
        starttime = builder.starttime;
        endtime = builder.endtime;
        station = builder.station;
        workday = builder.workday;
        id = builder.id;
    }

    /**
     * 检查门店参数
     * @return
     */
    public boolean checkStoreParams() {
        if (TextUtils.isEmpty(workday)) {
            ToastUtils.shortToast("请选择营业周期");
            return false;
        }
        if (TextUtils.isEmpty(station)) {
            ToastUtils.shortToast("请输入工位数量");
            return false;
        }

        if (TextUtils.isEmpty(categoryIds)) {
            ToastUtils.shortToast("请选择美发设施");
            return false;
        }
        return true;
    }

    public static final class Builder {
        private String storeId;
        private String categoryIds;
        private String starttime;
        private String endtime;
        private String station;
        private String workday;
        private String id;

        public Builder() {
        }

        public Builder storeId(String val) {
            storeId = val;
            return this;
        }

        public Builder categoryIds(String val) {
            categoryIds = val;
            return this;
        }

        public Builder starttime(String val) {
            starttime = val;
            return this;
        }

        public Builder endtime(String val) {
            endtime = val;
            return this;
        }

        public Builder station(String val) {
            station = val;
            return this;
        }

        public Builder workday(String val) {
            workday = val;
            return this;
        }

        public Builder id(String val) {
            id = val;
            return this;
        }

        public CommitSetServiceBody build() {
            return new CommitSetServiceBody(this);
        }
    }
}
