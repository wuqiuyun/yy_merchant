package com.yiyue.store.model.vo.bean;

import com.contrarywind.interfaces.IPickerViewData;

import java.util.List;

/**
 * Created by zm on 2018/10/17.
 */
public class ProvinceBean implements IPickerViewData{
    private String name;
    private long areaId;
    private List<CityBean> areaList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAreaId() {
        return String.valueOf(areaId);
    }

    public void setAreaId(long areaId) {
        this.areaId = areaId;
    }

    public List<CityBean> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<CityBean> areaList) {
        this.areaList = areaList;
    }

    @Override
    public String getPickerViewText() {
        return this.name;
    }

    public static class CityBean implements IPickerViewData{
        private String name;
        private long areaId;
        private List<AreaBean> areaList;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAreaId() {
            return String.valueOf(areaId);
        }

        public void setAreaId(long areaId) {
            this.areaId = areaId;
        }

        public List<AreaBean> getAreaList() {
            return areaList;
        }

        public void setAreaList(List<AreaBean> areaList) {
            this.areaList = areaList;
        }

        @Override
        public String getPickerViewText() {
            return this.name == null ? "" : this.name;
        }
    }

    public static class AreaBean implements IPickerViewData{
        private String name;
        private long areaId;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAreaId() {
            return String.valueOf(areaId);
        }

        public void setAreaId(long areaId) {
            this.areaId = areaId;
        }

        @Override
        public String getPickerViewText() {
            return this.name == null ? "" : this.name;
        }
    }

}
