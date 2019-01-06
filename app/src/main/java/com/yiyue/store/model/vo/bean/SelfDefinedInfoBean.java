package com.yiyue.store.model.vo.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangzz on 2018/10/29
 * 订单详情页等页面跳到聊天时 需要传递的jsonbean
 */
public class SelfDefinedInfoBean implements Parcelable {

    /**
     * 以下为自定义消息必传字段
     */
    //自定义消息  发送方信息
    private long userId;//用户id
    private String nickname;//昵称
    private String imusername;
    private String path;//头像路径
    private String label; //标签
    private int gender;//性别 1男  2女 3人妖

    // 自定义消息 内容区域传值 : 第三方账户信息 或者分享的内容
    private long toUserId;//用户id
    private String toNickname;//昵称
    private String toImusername;
    private String toPath;//头像路径
    private String toLabel; //标签
    private int toGender;//性别 1男  2女 3人妖


    //  自定义消息  接收方的信息
    private long recvUserId;//用户id
    private String recvNickname;//昵称
    private String recvImusername;
    private String recvPath;//头像路径
    private String recvLabel; //标签
    private int recvGender;//性别 1男  2女 3人妖
    /**
     * 以上为自定义消息必传字段
     */

    private long friendId;//好友关系id，非必须!用于好友详情页请求详情
    /**
     * 以下为 服务类型加价消息 必传字段
     */

    /**
     * definedMsgType字段： 自定义消息 不同自定义消息的类型 对应不同业务
     * <p>
     *
     * 10.加价项目为10   之前定义的不再使用（1.洗剪吹 2.洗吹 3.烫发 4.染发 5.护理 6.接发 7.洗色 8.拉直 9.单项套餐 10.多项套餐）
     * 101.分享门店 102.分享作品 103.分享美发师 110.推荐好友 111.邀请平台美发师加入 112.邀请门店美发师加入  113平台美发师加入申请 114门店美发师加入申请
     * 201.红包
     * 202.已领取红包  根据是否是本人来区分展示ui
     * 301.转账
     * 302.已接收转账 根据是否是本人来区分展示ui
     * 401.群邀请加入好友的消息item
     */
    private int definedMsgType;

    private String content;//发送的内容 比如红包描述："大红包来了"

    //加价
    private double money;//加价金额、红包大小金额、转账金额
    private int msgStatus;//1为发送人发起加价状态 2为已同意  3.为已拒绝
    private String serviceName;// 加价时 服务项目名称 /内容

    /**
     * 以上为 服务类型加价消息 必传字段
     */

    //以下为 红包 必传字段
    private int redPacketStatus;//红包状态 0 失效 1 发送中 2 已接收
    private long tradeId;//红包 转账 id


    //邀请入驻 平台  申请加入门店之类
    private String address;//门店地址
    private String detailId;//门店Id 美发师ID  分享作品、店铺、美发师的ID 聊天页面跳转门店详情/美发师详情/作品详情
    private String enterRecordID;//入驻申请记录ID(对应后台给的MsgId)

    private List<GroupJoinMemberBean> groupJoinMembers;//群邀请加入好友的列表
    /**
     * 以上为 红包 必传字段
     */


    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getFriendId() {
        return friendId;
    }

    public void setFriendId(long friendId) {
        this.friendId = friendId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getImusername() {
        return imusername;
    }

    public void setImusername(String imusername) {
        this.imusername = imusername;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getDefinedMsgType() {
        return definedMsgType;
    }

    public void setDefinedMsgType(int definedMsgType) {
        this.definedMsgType = definedMsgType;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public int getMsgStatus() {
        return msgStatus;
    }

    public void setMsgStatus(int msgStatus) {
        this.msgStatus = msgStatus;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getRedPacketStatus() {
        return redPacketStatus;
    }

    public void setRedPacketStatus(int redPacketStatus) {
        this.redPacketStatus = redPacketStatus;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public long getTradeId() {
        return tradeId;
    }

    public void setTradeId(long tradeId) {
        this.tradeId = tradeId;
    }

    public long getToUserId() {
        return toUserId;
    }

    public void setToUserId(long toUserId) {
        this.toUserId = toUserId;
    }

    public String getToNickname() {
        return toNickname;
    }

    public void setToNickname(String toNickname) {
        this.toNickname = toNickname;
    }

    public String getToImusername() {
        return toImusername;
    }

    public void setToImusername(String toImusername) {
        this.toImusername = toImusername;
    }

    public String getToPath() {
        return toPath;
    }

    public void setToPath(String toPath) {
        this.toPath = toPath;
    }

    public String getToLabel() {
        return toLabel;
    }

    public void setToLabel(String toLabel) {
        this.toLabel = toLabel;
    }

    public int getToGender() {
        return toGender;
    }

    public void setToGender(int toGender) {
        this.toGender = toGender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public long getRecvUserId() {
        return recvUserId;
    }

    public void setRecvUserId(long recvUserId) {
        this.recvUserId = recvUserId;
    }

    public String getRecvNickname() {
        return recvNickname;
    }

    public void setRecvNickname(String recvNickname) {
        this.recvNickname = recvNickname;
    }

    public String getRecvImusername() {
        return recvImusername;
    }

    public void setRecvImusername(String recvImusername) {
        this.recvImusername = recvImusername;
    }

    public String getRecvPath() {
        return recvPath;
    }

    public void setRecvPath(String recvPath) {
        this.recvPath = recvPath;
    }

    public String getRecvLabel() {
        return recvLabel;
    }

    public void setRecvLabel(String recvLabel) {
        this.recvLabel = recvLabel;
    }

    public int getRecvGender() {
        return recvGender;
    }

    public void setRecvGender(int recvGender) {
        this.recvGender = recvGender;
    }

    public String getEnterRecordID() {
        return enterRecordID;
    }

    public void setEnterRecordID(String enterRecordID) {
        this.enterRecordID = enterRecordID;
    }

    public List<GroupJoinMemberBean> getGroupJoinMembers() {
        return groupJoinMembers;
    }

    public void setGroupJoinMembers(List<GroupJoinMemberBean> groupJoinMembers) {
        this.groupJoinMembers = groupJoinMembers;
    }

    public SelfDefinedInfoBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.userId);
        dest.writeString(this.nickname);
        dest.writeString(this.imusername);
        dest.writeString(this.path);
        dest.writeString(this.label);
        dest.writeInt(this.gender);
        dest.writeLong(this.toUserId);
        dest.writeString(this.toNickname);
        dest.writeString(this.toImusername);
        dest.writeString(this.toPath);
        dest.writeString(this.toLabel);
        dest.writeInt(this.toGender);
        dest.writeLong(this.recvUserId);
        dest.writeString(this.recvNickname);
        dest.writeString(this.recvImusername);
        dest.writeString(this.recvPath);
        dest.writeString(this.recvLabel);
        dest.writeInt(this.recvGender);
        dest.writeLong(this.friendId);
        dest.writeInt(this.definedMsgType);
        dest.writeString(this.content);
        dest.writeDouble(this.money);
        dest.writeInt(this.msgStatus);
        dest.writeString(this.serviceName);
        dest.writeInt(this.redPacketStatus);
        dest.writeLong(this.tradeId);
        dest.writeString(this.address);
        dest.writeString(this.detailId);
        dest.writeString(this.enterRecordID);
        dest.writeList(this.groupJoinMembers);
    }

    protected SelfDefinedInfoBean(Parcel in) {
        this.userId = in.readLong();
        this.nickname = in.readString();
        this.imusername = in.readString();
        this.path = in.readString();
        this.label = in.readString();
        this.gender = in.readInt();
        this.toUserId = in.readLong();
        this.toNickname = in.readString();
        this.toImusername = in.readString();
        this.toPath = in.readString();
        this.toLabel = in.readString();
        this.toGender = in.readInt();
        this.recvUserId = in.readLong();
        this.recvNickname = in.readString();
        this.recvImusername = in.readString();
        this.recvPath = in.readString();
        this.recvLabel = in.readString();
        this.recvGender = in.readInt();
        this.friendId = in.readLong();
        this.definedMsgType = in.readInt();
        this.content = in.readString();
        this.money = in.readDouble();
        this.msgStatus = in.readInt();
        this.serviceName = in.readString();
        this.redPacketStatus = in.readInt();
        this.tradeId = in.readLong();
        this.address = in.readString();
        this.detailId = in.readString();
        this.enterRecordID = in.readString();
        this.groupJoinMembers = new ArrayList<GroupJoinMemberBean>();
        in.readList(this.groupJoinMembers, GroupJoinMemberBean.class.getClassLoader());
    }

    public static final Creator<SelfDefinedInfoBean> CREATOR = new Creator<SelfDefinedInfoBean>() {
        @Override
        public SelfDefinedInfoBean createFromParcel(Parcel source) {
            return new SelfDefinedInfoBean(source);
        }

        @Override
        public SelfDefinedInfoBean[] newArray(int size) {
            return new SelfDefinedInfoBean[size];
        }
    };
}
