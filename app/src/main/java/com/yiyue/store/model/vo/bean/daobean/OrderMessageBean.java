package com.yiyue.store.model.vo.bean.daobean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Lizhuo on 2018/11/13.
 */
@Entity
public class OrderMessageBean {
    private static final long serialVersionUID = 1L;

    /**
     * code=2, data={
     "orderNo":"djskaf;s",
     "orderTime":1542094292862,
     "orderId":1,
     "serverName":"洗剪套餐",
     "title":"",
     "msgtype":0,
     "content":""
     */
    @Id(autoincrement = true)
    private Long _id;
    private String orderNo;
    private long ordertime;
    private long createtime;
    private long orderId;
    private long oldordertime;
    private String serverName;
    private String title;
    private String content;
    private int msgtype;
    @Generated(hash = 1914798883)
    public OrderMessageBean(Long _id, String orderNo, long ordertime,
            long createtime, long orderId, long oldordertime, String serverName,
            String title, String content, int msgtype) {
        this._id = _id;
        this.orderNo = orderNo;
        this.ordertime = ordertime;
        this.createtime = createtime;
        this.orderId = orderId;
        this.oldordertime = oldordertime;
        this.serverName = serverName;
        this.title = title;
        this.content = content;
        this.msgtype = msgtype;
    }
    @Generated(hash = 1478032176)
    public OrderMessageBean() {
    }
    public Long get_id() {
        return this._id;
    }
    public void set_id(Long _id) {
        this._id = _id;
    }
    public String getOrderNo() {
        return this.orderNo;
    }
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    public long getOrdertime() {
        return this.ordertime;
    }
    public void setOrdertime(long ordertime) {
        this.ordertime = ordertime;
    }
    public long getCreatetime() {
        return this.createtime;
    }
    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }
    public long getOrderId() {
        return this.orderId;
    }
    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }
    public long getOldordertime() {
        return this.oldordertime;
    }
    public void setOldordertime(long oldordertime) {
        this.oldordertime = oldordertime;
    }
    public String getServerName() {
        return this.serverName;
    }
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public int getMsgtype() {
        return this.msgtype;
    }
    public void setMsgtype(int msgtype) {
        this.msgtype = msgtype;
    }
}
