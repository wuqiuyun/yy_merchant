package com.yiyue.store.api;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.yiyue.store.base.data.BaseRequestBody;
import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.component.net.YLRequestManager;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.component.rx.RxSchedulers;
import com.yiyue.store.model.vo.bean.GroupAddReqBean;
import com.yiyue.store.model.vo.result.GroupListResult;
import com.yiyue.store.model.vo.result.GroupResult;
import com.yiyue.store.model.vo.result.findGroupAllUserResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * 群组相关Api
 * 
 * Created by Lizhuo on 2018/10/16.
 */
public class GroupApi {
    
    public interface Api {
        // TODO: 2018/10/17 remove api-b 
        @POST("msg-api/group/create")
        Observable<GroupResult> createGroup(@Body RequestBody requestBody);

        @POST("msg-api/group/deleteGroup")
        Observable<BaseResponse> deleteGroup(@Body RequestBody requestBody);

        @POST("msg-api/group/addSingleUserToChatGroup")
        Observable<BaseResponse> addSingleToGroup(@Body RequestBody requestBody);

        @POST("msg-api/group/removeSingleUserFromChatGroup")
        Observable<BaseResponse> removeSingleFromGroup(@Body RequestBody requestBody);

        @POST("msg-api/group/addBatchUsersToChatGroup")
        Observable<BaseResponse> addBatchToGroup(@Body RequestBody requestBody);

        @POST("msg-api/group/removeBatchUsersFromChatGroup")
        Observable<BaseResponse> removeBatchFromGroup(@Body RequestBody requestBody);

        @POST("msg-api/group/transferChatGroupOwner")
        Observable<BaseResponse> transferGroupOwner(@Body RequestBody requestBody);

        @POST("msg-api/group/update")
        Observable<BaseResponse> updateGroup(@Body RequestBody requestBody);

        @POST("msg-api/group/userGroupService")
        Observable<BaseResponse> userGroupService(@Body RequestBody requestBody);

        @POST("msg-api/group/requestAddGroup")
        Observable<BaseResponse> requestAddGroup(@Body RequestBody requestBody);

        @GET("msg-api/group/findAllGroup")
        Observable<GroupListResult> findAllGroup(@QueryMap Map<String, String> map);

        @GET("msg-api/group/findGroupAllUser")
        Observable<findGroupAllUserResult> findGroupAllUser(@QueryMap Map<String, String> map);

        @GET("msg-api/group/searchGroup")
        Observable<BaseResponse> searchGroup(@QueryMap Map<String, String> map);

        @GET("msg-api/group/searchGroupPage")
        Observable<GroupListResult> searchGroupPage(@QueryMap Map<String, String> map);

        @GET("msg-api/group/findGroup")
        Observable<GroupResult> findGroup(@QueryMap Map<String, String> map);

        @GET("msg-api/group/receiveMessage")
        Observable<BaseResponse> receiveMessage(@QueryMap Map<String, String> map);

        @GET("msg-api/group/refuseMessage")
        Observable<BaseResponse> refuseMessage(@QueryMap Map<String, String> map);
    }

    private Api mApi;
    
    public GroupApi() {
        mApi = YLRequestManager.getRequest(GroupApi.Api.class);
    }

    /**
     * 创建群组
     * 
     * @param name 群名称
     * @param describe 描述
     * @param path 群头像路径
     * @param imusername 用户对应im帐号
     * @param userId 用户id
     */
    public void createGroup(String name, String describe, String path, String imusername, String userId, YLRxSubscriberHelper<GroupResult> subscriberelper) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("describe", describe);
        params.put("path", path);
        params.put("imusername", imusername);
        params.put("userId", userId);

        mApi.createGroup(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberelper);
    }

    /**
     * 解散群组
     * 
     * @param id 群组id
     * @param imgroup 群组编号
     */
    public void deleteGroup(String id, String imgroup, YLRxSubscriberHelper<BaseResponse> subscriberelper) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("imgroup", imgroup);

        mApi.deleteGroup(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberelper);
    }

    /**
     * 添加单一用户到群
     * 
     * @param groupId 群组id
     * @param imgroup 群组编号
     * @param imusername 要加入群的用户对应的imusername
     * @param userId 加入群的用户userId
     */
    public void addSingleToGroup(String groupId, String imgroup, String imusername, String userId, YLRxSubscriberHelper<BaseResponse> subscriberelper) {
        Map<String, String> params = new HashMap<>();
        params.put("groupId", groupId);
        params.put("imgroup", imgroup);
        params.put("imusername", imusername);
        params.put("userId", userId);

        mApi.addSingleToGroup(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberelper);
    }

    /**
     * 从群移除单一用户
     *
     * @param groupId 群组id
     * @param imusername 要从群移除的用户对应的imusername
     * @param id 要从群移除的用户id
     */
    public void removeSingleFromGroup(String groupId, String imgroup, String imusername, String id, YLRxSubscriberHelper<BaseResponse> subscriberelper) {
        Map<String, String> params = new HashMap<>();
        params.put("groupId", groupId);
        params.put("imgroup", imgroup);
        params.put("imusername", imusername);
        params.put("id", id);

        mApi.removeSingleFromGroup(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberelper);
    }
    
    /**
     * 添加多人到群
     * 
     * @param groupId 群组id
     * @param imgroup 群组编号
     * @param users 选择的人
     * "users": 
     * [{"imusername": "比克大魔王", "userId": 1} {"imusername": "比克小魔王", "userId": 2}]
     */
    public void addBatchToGroup(String groupId, String imgroup, List<GroupAddReqBean> users, YLRxSubscriberHelper<BaseResponse> subscriberelper) {
        Map<String, Object> params = new HashMap<>();
        params.put("groupId", groupId);
        params.put("imgroup", imgroup);
        params.put("users", users);

        mApi.addBatchToGroup(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberelper);
    }

    /**
     * 从群移除多人
     *
     * @param groupId 群组id
     * @param imgroup 群组编号
     * @param users 选择的人
     * "users": 
     * [{"imusername": "比克大魔王", "userId": 1} {"imusername": "比克小魔王", "userId": 2}]
     */
    public void removeBatchFromGroup(String groupId, String imgroup, List users, YLRxSubscriberHelper<BaseResponse> subscriberelper) {
        Map<String, String> params = new HashMap<>();
        params.put("groupId", groupId);
        params.put("imgroup", imgroup);
        params.put("users", getJson(users));

        mApi.removeBatchFromGroup(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberelper);
    }

    //users 转 json字符串
    private String getJson(List list) {
        Gson gson = new Gson();
        return  gson.toJson(list);
    }

    /**
     * 转让群给某人
     * 
     * @param id 群组id
     * @param imgroup 群组编号
     * @param imusername 被转让者的imusername
     * @param userId 被转让者的userId
     */
    public void transferGroupOwner(String id, String imgroup, String imusername, String userId, YLRxSubscriberHelper<BaseResponse> subscriberelper) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("imgroup", imgroup);
        params.put("imusername", imusername);
        params.put("userId", userId);

        mApi.transferGroupOwner(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberelper);
    }

    /**
     * 编辑群组信息
     * 
     * @param id 群组id 
     * 以下参数可选
     * @param name 群名称
     * @param describe 群介绍
     * @param membersonly 加群验证 0 需验证  1 不需验证
     */
    public void updateGroup(String id, String name, String describe, String membersonly, YLRxSubscriberHelper<BaseResponse> subscriberelper) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        if (!TextUtils.isEmpty(name)) params.put("name", name);
        if (!TextUtils.isEmpty(describe)) params.put("describe", describe);
        if (!TextUtils.isEmpty(membersonly)) params.put("membersonly", membersonly);

        mApi.updateGroup(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberelper);
    }

    /**
     * 群组举报
     * 
     * @param groupId 群组id
     * @param type 举报类型(1欺诈骗钱,2侮辱诋毁,3广告骚扰,4反动或色情)
     * @param userId 举报者userId
     */
    public void userGroupService(String groupId, String type, String userId, YLRxSubscriberHelper<BaseResponse> subscriberelper) {
        Map<String, String> params = new HashMap<>();
        params.put("groupId", groupId);
        params.put("type", type);
        params.put("userId", userId);

        mApi.userGroupService(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberelper);
    }

    /**
     * 申请加入群组
     * 
     * @param userId 申请人userid
     * @param nickname 申请人 昵称
     * @param path 申请人 头像路径
     * @param imusername 申请人 im帐号
     * @param groupId 群组id
     * @param friendId 群主userid
     * @param remarks 备注 
     * @param imgroup 群组im编号
     */
    public void requestAddGroup(String userId, String nickname, String path, String imusername, String groupId, String friendId, String remarks,String imgroup, 
                                YLRxSubscriberHelper<BaseResponse> subscriberelper) {
        Map<String, String> params = new HashMap<>();
        params.put("requestUserId", userId);
        params.put("requestNickname", nickname);
        params.put("requestPtah", path);
        params.put("imusername", imusername);
        params.put("groupId", groupId);
        params.put("friendId", friendId);
        params.put("remarks", remarks);
        params.put("imgroup", imgroup);

        mApi.requestAddGroup(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberelper);
    }

    /**
     * 获取用户所有的群组
     * 
     * @param userId 用户id
     */
    public void findAllGroup(String userId, YLRxSubscriberHelper<GroupListResult> subscriberelper) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", userId);

        mApi.findAllGroup(params)
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberelper);
    }

    /**
     * 获取群组所有用户
     * 
     * @param groupId 群组id
     */
    public void findGroupAllUser(String groupId, YLRxSubscriberHelper<findGroupAllUserResult> subscriberelper) {
        Map<String, String> params = new HashMap<>();
        params.put("groupId", groupId);

        mApi.findGroupAllUser(params)
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberelper);
    }

    /**
     * 搜索群组(全部)
     *
     * @param param 搜索关键字
     */
    public void searchGroup(String param, YLRxSubscriberHelper<BaseResponse> subscriberelper) {
        Map<String, String> params = new HashMap<>();
        params.put("param", param);

        mApi.searchGroup(params)
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberelper);
    }

    /**
     * 搜索群组(分页)
     *
     * @param param 搜索关键字
     */
    public void searchGroupPage(String param, String pageNo, String pageSize, YLRxSubscriberHelper<GroupListResult> subscriberelper) {
        Map<String, String> params = new HashMap<>();
        params.put("param", param);
        params.put("pageNo", pageNo);
        params.put("pageSize", pageSize);

        mApi.searchGroupPage(params)
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberelper);
    }

    /**
     * 查找单个群
     *
     * @param groupId 搜索关键字
     */
    public void findGroup(String groupId, String userId, YLRxSubscriberHelper<GroupResult> subscriberelper) {
        Map<String, String> params = new HashMap<>();
        params.put("groupId", groupId);
        params.put("userId", userId);

        mApi.findGroup(params)
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberelper);
    }

    /**
     * 接收群消息
     *
     * @param id 搜索关键字
     */
    public void receiveMessage(String id, YLRxSubscriberHelper<BaseResponse> subscriberelper) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);

        mApi.receiveMessage(params)
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberelper);
    }

    /**
     * 屏蔽群消息
     *
     * @param id 搜索关键字
     */
    public void refuseMessage(String id, YLRxSubscriberHelper<BaseResponse> subscriberelper) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);

        mApi.refuseMessage(params)
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberelper);
    }
}
