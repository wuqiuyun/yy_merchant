package com.yiyue.store.model.vo.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zm on 2019/1/4.
 */
public class UserIncomeInfoBean implements Parcelable {

    private String headportrait;
    private double income;
    private long inviteTime;
    private String inviteUserId;
    private String mobile;
    private String name;
    private int nextSize;

    protected UserIncomeInfoBean(Parcel in) {
        headportrait = in.readString();
        income = in.readDouble();
        inviteTime = in.readLong();
        inviteUserId = in.readString();
        mobile = in.readString();
        name = in.readString();
        nextSize = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(headportrait);
        dest.writeDouble(income);
        dest.writeLong(inviteTime);
        dest.writeString(inviteUserId);
        dest.writeString(mobile);
        dest.writeString(name);
        dest.writeInt(nextSize);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserIncomeInfoBean> CREATOR = new Creator<UserIncomeInfoBean>() {
        @Override
        public UserIncomeInfoBean createFromParcel(Parcel in) {
            return new UserIncomeInfoBean(in);
        }

        @Override
        public UserIncomeInfoBean[] newArray(int size) {
            return new UserIncomeInfoBean[size];
        }
    };

    public String getHeadportrait() {
        return headportrait;
    }

    public void setHeadportrait(String headportrait) {
        this.headportrait = headportrait;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public long getInviteTime() {
        return inviteTime;
    }

    public void setInviteTime(long inviteTime) {
        this.inviteTime = inviteTime;
    }

    public String getInviteUserId() {
        return inviteUserId;
    }

    public void setInviteUserId(String inviteUserId) {
        this.inviteUserId = inviteUserId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNextSize() {
        return nextSize;
    }

    public void setNextSize(int nextSize) {
        this.nextSize = nextSize;
    }
}
