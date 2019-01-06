package com.yiyue.store.model.vo.bean;

/**
 * Created by lvlong on 2018/10/12.
 */
public class ServiceSettingBean {
    private String label;
    private boolean checked;
    private String id;

    @Override
    public String toString() {
        return "ServiceSettingBean{" +
                "label='" + label + '\'' +
                ", checked=" + checked +
                ", id='" + id + '\'' +
                '}';
    }

    public ServiceSettingBean(String label, boolean checked) {
        this.label = label;
        this.checked = checked;
    }

    public ServiceSettingBean(String label, boolean checked, String id) {
        this.label = label;
        this.checked = checked;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
