package com.yiyue.store.model.vo.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * Created by zm on 2018/10/19.
 */
public class AddrInfoBean implements Parcelable{
    // 地址 通常为短地址
    private String addr;
    // 地址 长地址
    private String addrDetail;
    // 地址 lat
    private double lat;
    // 地址 lot
    private double lon;
    // 城市 id
    private String cityId;
    // 城市 name
    private String city;
    // 省份 id
    private String provinceId;
    // 省份 name
    private String provinceName;
    // 行政区 id
    private String adId;
    // 行政区 name
    private String adName;

    public AddrInfoBean() {

    }

    protected AddrInfoBean(Parcel in) {
        addr = in.readString();
        addrDetail = in.readString();
        lat = in.readDouble();
        lon = in.readDouble();
        cityId = in.readString();
        city = in.readString();
        provinceId = in.readString();
        provinceName = in.readString();
        adId = in.readString();
        adName = in.readString();
    }

    public static final Creator<AddrInfoBean> CREATOR = new Creator<AddrInfoBean>() {
        @Override
        public AddrInfoBean createFromParcel(Parcel in) {
            return new AddrInfoBean(in);
        }

        @Override
        public AddrInfoBean[] newArray(int size) {
            return new AddrInfoBean[size];
        }
    };

    public String getAdId() {
        return TextUtils.isEmpty(adId) ? "0" : adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getAdName() {
        return adName;
    }

    public void setAdName(String adName) {
        this.adName = adName;
    }

    public String getProvinceId() {
        return TextUtils.isEmpty(provinceId) ? "0" : provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getAddrDetail() {
        return addrDetail;
    }

    public void setAddrDetail(String addrDetail) {
        this.addrDetail = addrDetail;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getCityId() {
        return TextUtils.isEmpty(cityId) ? "0" : cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(addr);
        dest.writeString(addrDetail);
        dest.writeDouble(lat);
        dest.writeDouble(lon);
        dest.writeString(cityId);
        dest.writeString(city);
        dest.writeString(provinceId);
        dest.writeString(provinceName);
        dest.writeString(adId);
        dest.writeString(adName);
    }
}
