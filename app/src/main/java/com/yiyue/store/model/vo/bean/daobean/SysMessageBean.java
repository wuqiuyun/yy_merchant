package com.yiyue.store.model.vo.bean.daobean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.util.Date;

/**
 * Created by Lizhuo on 2018/11/15.
 */
@Entity
public class SysMessageBean {
    private static final long serialVersionUID = 1L;

    /**
     * code=3, data={
     * "createtime":long,
     * "orderNo":"",
     * "orderId":0,
     * "serverName":"",
     * "title":"提现通知",
     * "msgtype":0,
     * "content":"您的提现已成功，请注意查收！
     * 
     * {"createtime":1542262105960,
     * "orderNo":"",
     * "orderId":0,
     * "serverName":"",
     * "ordertime":0,
     * "title":"提现通知",
     * "msgtype":0,
     * "content":"您的提现已成功，请注意查收！"}
     */
    @Id(autoincrement = true)
    private Long _id;
    private long createtime;
    private String orderNo;
    private long orderId;
    private String serverName;
    private long ordertime;
    private String title;
    private int msgtype;
    private String content;
    @Generated(hash = 744354383)
    public SysMessageBean(Long _id, long createtime, String orderNo, long orderId,
            String serverName, long ordertime, String title, int msgtype,
            String content) {
        this._id = _id;
        this.createtime = createtime;
        this.orderNo = orderNo;
        this.orderId = orderId;
        this.serverName = serverName;
        this.ordertime = ordertime;
        this.title = title;
        this.msgtype = msgtype;
        this.content = content;
    }
    @Generated(hash = 178059297)
    public SysMessageBean() {
    }
    public Long get_id() {
        return this._id;
    }
    public void set_id(Long _id) {
        this._id = _id;
    }
    public long getCreatetime() {
        return this.createtime;
    }
    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }
    public String getOrderNo() {
        return this.orderNo;
    }
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    public long getOrderId() {
        return this.orderId;
    }
    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }
    public String getServerName() {
        return this.serverName;
    }
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
    public long getOrdertime() {
        return this.ordertime;
    }
    public void setOrdertime(long ordertime) {
        this.ordertime = ordertime;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public int getMsgtype() {
        return this.msgtype;
    }
    public void setMsgtype(int msgtype) {
        this.msgtype = msgtype;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }

}
