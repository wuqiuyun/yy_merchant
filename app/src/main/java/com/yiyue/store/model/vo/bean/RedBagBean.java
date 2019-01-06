package com.yiyue.store.model.vo.bean;

/**
 * Created by zhangzz on 2018/11/9
 */
public class RedBagBean {
    /**
     * "total": 2,
     "quantity": 2,
     "lsit": [
     {
     "id": 666,
     "sendUserId": 1879,
     "receiveUserId": 492,
     "sendamount": 1,
     "receiveamount": 1,
     "fee": 0,
     "status": 2,
     "isclear": 0,
     "remark": "大吉大利, 今晚吃鸡!",
     "createtime": 1542105611000,
     "updatetime": 1542105614000,
     "nickname": null,
     "path": null
     },
     */
    private long id;//红包id
    private long sendUserId;//用户id(user表id) 发送
    private long receiveUserId;// 用户id(user表id) 接收
    private double sendamount;//发送金额
    private double receiveamount;//接收金额
    private double fee;//手续费 暂时无用
    private int status;//状态 0 失效 1 发送中 2 已接收
    private String createtime;//创建时间
    private String updatetime;
    private String remark;
    private String nickname;//昵称
    private String path;//头像地址

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(long sendUserId) {
        this.sendUserId = sendUserId;
    }

    public long getReceiveUserId() {
        return receiveUserId;
    }

    public void setReceiveUserId(long receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    public double getSendamount() {
        return sendamount;
    }

    public void setSendamount(double sendamount) {
        this.sendamount = sendamount;
    }

    public double getReceiveamount() {
        return receiveamount;
    }

    public void setReceiveamount(double receiveamount) {
        this.receiveamount = receiveamount;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
