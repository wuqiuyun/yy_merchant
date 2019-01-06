package com.yiyue.store.model.vo.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zm on 2018/10/18.
 */
public class ImageBean implements Parcelable{
    private String fileUrl;
    private String filePath;

    public ImageBean() {}

    protected ImageBean(Parcel in) {
        fileUrl = in.readString();
        filePath = in.readString();
    }

    public static final Creator<ImageBean> CREATOR = new Creator<ImageBean>() {
        @Override
        public ImageBean createFromParcel(Parcel in) {
            return new ImageBean(in);
        }

        @Override
        public ImageBean[] newArray(int size) {
            return new ImageBean[size];
        }
    };

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fileUrl);
        dest.writeString(filePath);
    }
}
