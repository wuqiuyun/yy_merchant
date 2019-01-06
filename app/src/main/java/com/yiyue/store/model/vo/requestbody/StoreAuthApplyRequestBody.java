package com.yiyue.store.model.vo.requestbody;

import android.text.TextUtils;

import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.util.AccountValidatorUtil;

import java.util.List;

/**
 * Created by zm on 2018/10/22.
 */
public class StoreAuthApplyRequestBody {

    private String businesslicensePath; // 营业执照
    private String cardno; // 身份证号
    private String handIDcardPath; // 手持身份证路径
    private String licenseno; // 营业执照编号
    private String realname; // 真实姓名
    private String storeId; // 用户ID
    private String bannerPath; // 门店封面
    private List<String> storephotosPaths; // 门店环境照片

    private StoreAuthApplyRequestBody(Builder builder) {
        businesslicensePath = builder.businesslicensePath;
        cardno = builder.cardno;
        handIDcardPath = builder.handIDcardPath;
        licenseno = builder.licenseno;
        realname = builder.realname;
        storeId = builder.storeId;
        bannerPath = builder.bannerPath;
        storephotosPaths = builder.storephotosPaths;
    }

    public boolean checkParams() {
        if (TextUtils.isEmpty(realname)) {
            ToastUtils.shortToast("请输入真实姓名.");
            return false;
        }
        if (TextUtils.isEmpty(cardno)) {
            ToastUtils.shortToast("请输入身份证号码.");
            return false;
        }
        if (!AccountValidatorUtil.isIDCard(cardno)) {
            ToastUtils.shortToast("请输人正确的身份证号码.");
            return false;
        }
        if (TextUtils.isEmpty(licenseno)) {
            ToastUtils.shortToast("请输入营业执照编号.");
            return false;
        }
        if (TextUtils.isEmpty(businesslicensePath)) {
            ToastUtils.shortToast("请上传营业执照.");
            return false;
        }
        if (TextUtils.isEmpty(bannerPath)) {
            ToastUtils.shortToast("请上传门店封面.");
            return false;
        }
        if (storephotosPaths == null || storephotosPaths.size() == 0) {
            ToastUtils.shortToast("请上传门店照片.");
            return false;
        }
        if (TextUtils.isEmpty(handIDcardPath)) {
            ToastUtils.shortToast("请上传手持身份证照.");
            return false;
        }
        return true;
    }


    public static final class Builder {
        private String businesslicensePath;
        private String cardno;
        private String handIDcardPath;
        private String licenseno;
        private String realname;
        private String storeId;
        private String bannerPath;
        private List<String> storephotosPaths;

        public Builder() {
        }

        public Builder businesslicensePath(String val) {
            businesslicensePath = val;
            return this;
        }

        public Builder cardno(String val) {
            cardno = val;
            return this;
        }

        public Builder handIDcardPath(String val) {
            handIDcardPath = val;
            return this;
        }

        public Builder licenseno(String val) {
            licenseno = val;
            return this;
        }

        public Builder realname(String val) {
            realname = val;
            return this;
        }

        public Builder storeId(String val) {
            storeId = val;
            return this;
        }

        public Builder bannerPath(String val) {
            bannerPath = val;
            return this;
        }

        public Builder storephotosPaths(List<String> val) {
            storephotosPaths = val;
            return this;
        }

        public StoreAuthApplyRequestBody build() {
            return new StoreAuthApplyRequestBody(this);
        }
    }
}