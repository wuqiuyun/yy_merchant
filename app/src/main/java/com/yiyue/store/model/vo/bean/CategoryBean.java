package com.yiyue.store.model.vo.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 服务类目
 * Created by zm on 2018/10/27.
 */
public class CategoryBean implements Parcelable{
    private int id;
    private String name;
    private String describe;
    private boolean checked;

    protected CategoryBean(Parcel in) {
        id = in.readInt();
        name = in.readString();
        describe = in.readString();
        checked = in.readByte() != 0;
    }

    public CategoryBean(int id,String name, boolean checked) {
        this.id = id;
        this.name = name;
        this.checked = checked;
    }

    public static final Creator<CategoryBean> CREATOR = new Creator<CategoryBean>() {
        @Override
        public CategoryBean createFromParcel(Parcel in) {
            return new CategoryBean(in);
        }

        @Override
        public CategoryBean[] newArray(int size) {
            return new CategoryBean[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(describe);
        dest.writeByte((byte) (checked ? 1 : 0));
    }
}
