package com.yiyue.store.api;

import com.yiyue.store.base.data.BaseRequestBody;
import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.component.net.YLRequestManager;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.component.rx.RxSchedulers;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.vo.result.AddFriendResult;
import com.yiyue.store.model.vo.result.FriendInfoResult;
import com.yiyue.store.model.vo.result.SysMsgResult;
import com.yiyue.store.model.vo.result.UserFriendsResult;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * Created by zm on 2018/10/15.
 */
public class ContactsApi {

    public interface Api {
        @POST("/msg-api/user/contacts/requestAddFriend")
        Observable<AddFriendResult> addFriend(@Body RequestBody requestBody);

        @POST("/msg-api/user/contacts/receiveAddFriend")
        Observable<BaseResponse> receiveAddFriend(@Body RequestBody requestBody);

        @POST("/msg-api/group/receiveAddGroup")
        Observable<BaseResponse> receiveAddGroup(@Body RequestBody requestBody);

        @POST("/msg-api/user/contacts/updateNickName")
        Observable<BaseResponse> updateNickName(@Body RequestBody requestBody);

        @GET("/msg-api/user/contacts/searchUser")
        Observable<UserFriendsResult> searchUser(@QueryMap Map<String, String> params);

        @GET("/msg-api/user/contacts/findAddFriend")
        Observable<SysMsgResult> findAddFriend(@QueryMap Map<String, String> params);

        @GET("/msg-api/user/contacts/findAllUserContacts")
        Observable<UserFriendsResult> findAllContacts(@QueryMap Map<String, String> params);

        @GET("/msg-api/user/contacts/deleteFriendSingle")
        Observable<BaseResponse> deleteFriendSingle(@QueryMap Map<String, String> params);

        @GET("/msg-api/user/contacts/addToBlackList")
        Observable<BaseResponse> addToBlackList(@QueryMap Map<String, String> params);

        @GET("/msg-api/user/contacts/removeFromBlackList")
        Observable<BaseResponse> removeFromBlackList(@QueryMap Map<String, String> params);

        @GET("/msg-api/user/contacts/getFriends")
        Observable<FriendInfoResult> getFriends(@QueryMap Map<String, String> params);
    }

    private Api mApi;

    public ContactsApi() {
        mApi = YLRequestManager.getRequest(ContactsApi.Api.class);
    }

    /**
     * 申请添加好友
     *
     * @param userId
     * @param friendId
     * @param subscriberelper
     */
    public void requestAddFriend(String userId, String requestNickname, String friendId, String remarks, YLRxSubscriberHelper<AddFriendResult> subscriberelper) {
        Map<String, String> params = new HashMap<>();
        params.put("requestUserId", userId);
        params.put("requestNickname", requestNickname);
        params.put("friendId", friendId);
        params.put("remarks", remarks);
        params.put("requestPtah", AccountManager.getInstance().getAccount().getHeadImg());
        mApi.addFriend(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberelper);
    }

    /**
     * 查询所有添加好友请求，//TODO 以后这个接口会取消，变成推送发过来
     *
     * @param userId
     * @param subscriberelper
     */
    public void requestFindAddFriend(String userId, YLRxSubscriberHelper<SysMsgResult> subscriberelper) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", userId);
        mApi.findAddFriend(params)
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberelper);
    }

    /**
     * 搜索好友
     *
     * @param param           关键字(昵称,或好友ID编号)
     * @param subscriberelper
     */
    public void requestSearchUser(String param, YLRxSubscriberHelper<UserFriendsResult> subscriberelper) {
        Map<String, String> params = new HashMap<>();
        params.put("param", param);

        mApi.searchUser(params)
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberelper);
    }

    /**
     * 接收好友添加申请
     */
    public void requestReceiveAddFriend(String id, String status, YLRxSubscriberHelper<BaseResponse> rxSubscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("status", status);

        mApi.receiveAddFriend(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(rxSubscriberHelper);
    }

    /**
     * 接收群组添加申请
     */
    public void requestReceiveAddGroup(String id, String status, YLRxSubscriberHelper<BaseResponse> rxSubscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("status", status);

        mApi.receiveAddGroup(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(rxSubscriberHelper);
    }


    /**
     * 设置好友备注
     */
    public void requestUpdateNickName(String id, String nickname, YLRxSubscriberHelper<BaseResponse> rxSubscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("nickname", nickname);

        mApi.updateNickName(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(rxSubscriberHelper);
    }

    /**
     * 查询用户的所有好友好友
     *
     * @param userId
     * @param subscriberelper
     */
    public void requestFindContacts(String userId, YLRxSubscriberHelper<UserFriendsResult> subscriberelper) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", userId);

        mApi.findAllContacts(params)
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberelper);
    }

    /**
     * 删除好友 接触IM用户的好友关系
     */
    public void requestDeleteFriendSingle(String id, YLRxSubscriberHelper<BaseResponse> rxSubscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);

        mApi.deleteFriendSingle(params)
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(rxSubscriberHelper);
    }

    /**
     * 屏蔽好友 不接收该好友信息
     */
    public void requestAddToBlackList(String id, YLRxSubscriberHelper<BaseResponse> rxSubscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);

        mApi.addToBlackList(params)
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(rxSubscriberHelper);
    }

    /**
     * 解除屏蔽好友 再次接收该好友信息
     */
    public void requestRemoveFromBlackList(String id, YLRxSubscriberHelper<BaseResponse> rxSubscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);

        mApi.removeFromBlackList(params)
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(rxSubscriberHelper);
    }

    /**
     * 查看好友信息
     */
    public void requestGetFriend(String userId, String imusername, YLRxSubscriberHelper<FriendInfoResult> rxSubscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", userId);
        params.put("imusername", imusername);

        mApi.getFriends(params)
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(rxSubscriberHelper);
    }
}
