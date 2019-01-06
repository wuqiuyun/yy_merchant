package com.yiyue.store.api;

import android.text.TextUtils;

import com.yiyue.store.base.data.BaseRequestBody;
import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.component.net.YLRequestManager;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.component.rx.RxSchedulers;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.vo.requestbody.DoStoreDataRequestBody;
import com.yiyue.store.model.vo.result.LoginResult;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 用户相关api
 * <p>
 * Created by zm on 2018/9/9.
 */
public class StoreUserApi {

    public interface Api {
        // 登陆
        @POST("/store-api/storeUser/login")
        Observable<LoginResult> login(@Body RequestBody requestBody);

        // 微信登陆
        @GET("/store-api/storeUser/wxlogin")
        Observable<LoginResult> wxlogin(@Query("code") String code,@Query("client") String client);

        // 绑定手机号
        @POST("/store-api/storeUser/loginWXAdd")
        Observable<LoginResult> loginWXAdd(@Body RequestBody requestBody);

        // 登出
        @GET("/store-api/storeUser/logout")
        Observable<BaseResponse> logout();

        // 填写邀请码
        @GET("/store-api/storeUser/inviteFriends")
        Observable<BaseResponse> inviteFriends(@Query("userId") String userId, @Query("invitecode") String invitecode);

        // 更新密码
        @POST("/store-api/storeUser/updatePass")
        Observable<BaseResponse> updatePwd(@Body RequestBody requestBody);

        // 完善资料
        @POST("/store-api/storeUser/doUserData")
        Observable<BaseResponse> doUserData(@Body RequestBody requestBody);

        // 门店基本信息
        @POST("/store-api/storeUser/doStoreData")
        Observable<BaseResponse> doStoreData(@Body RequestBody requestBody);
    }

    private Api mApi;

    public StoreUserApi() {
        this.mApi = YLRequestManager.getRequest(Api.class);
    }

    /**
     *
     * @param requestBody
     * @param subscriberHelper
     */
    public void doStoreData(DoStoreDataRequestBody requestBody, YLRxSubscriberHelper<BaseResponse> subscriberHelper) {
        mApi.doStoreData(new BaseRequestBody(requestBody).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     *微信登录确认后绑定手机号
     *
     * @param client 性别 -> 1男 2女 3人妖
     * @param mobile 头像地址
     * @param openId 密码
     * @param phoneCode 密码
     * @param type 密码
     */
    public void loginWXAdd(String client, String mobile, String openId,  String phoneCode, int type,
                           YLRxSubscriberHelper<LoginResult> subscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("client", client);
        params.put("mobile", mobile);
        params.put("openId", openId);
        params.put("phoneCode", phoneCode);
        params.put("type", String.valueOf(type));

        mApi.loginWXAdd(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     *完善用户信息
     *
     * @param gender 性别 -> 1男 2女 3人妖
     * @param headPortrait 头像地址
     * @param password 密码
     */
    public void doUserData(int gender, String headPortrait, String password, YLRxSubscriberHelper<BaseResponse> subscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("id", AccountManager.getInstance().getUserId());
        params.put("gender", String.valueOf(gender));
        params.put("headPortrait", headPortrait);
        params.put("password", password);

        mApi.doUserData(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     * 填写邀请码
     * @param userId 用户id
     * @param invitecode 邀请码
     */
    public void inviteFriends(String userId, String invitecode, YLRxSubscriberHelper<BaseResponse> subscriberHelper) {
        mApi.inviteFriends(userId, invitecode)
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     * 微信登录
     * @param code 微信code
     */
    public void wxlogin(String code, YLRxSubscriberHelper<LoginResult> subscriberHelper) {
        mApi.wxlogin(code,"android")
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     * @param mobile 手机号码
     * @param phoneCode 验证码
     * @param password 密码
     */
    public void login(String mobile, String phoneCode, String password, YLRxSubscriberHelper<LoginResult> subscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("client", "android");
        params.put("mobile", mobile);
        if (!TextUtils.isEmpty(phoneCode))
            params.put("phoneCode", phoneCode);
            params.put("type", "2");
        if (!TextUtils.isEmpty(password)) {
            params.put("password", password);
        }
        mApi.login(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     * 登出
     */
    public void logout(YLRxSubscriberHelper<BaseResponse> rxSubscriberHelper) {
        mApi.logout()
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(rxSubscriberHelper);
    }

    /**
     * 更新密码
     * @param mobile
     * @param phoneCode
     * @param newPassword
     */
    public void updatePwd(String mobile, String phoneCode, String newPassword, YLRxSubscriberHelper<BaseResponse> subscriberHelper) {
            Map<String, String> params = new HashMap<>();
            params.put("mobile", mobile);
            params.put("type", "3");
            params.put("phoneCode", phoneCode);
            params.put("newPassword", newPassword);
            mApi.updatePwd(new BaseRequestBody(params).toRequestBody())
                    .compose(RxSchedulers.rxSchedulerHelper())
                    .compose(RxSchedulers.handleResult())
                    .subscribe(subscriberHelper);
    }
}
