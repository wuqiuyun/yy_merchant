package com.yiyue.store.model.vo.bean;

/**
 * Created by lyj on 2018/11/8.
 */
public class BankCardBean {

    /**
     * id : 1
     * bankname : 工商银行
     * createtime : 2018-11-06 20:55:17
     * bankshort : ICBC
     * status : 1
     * remark : null
     * updatetime : 2018-11-06 20:55:12
     */

    private int id;
    private String bankname;
    private String createtime;
    private String bankshort;
    private int status;
    private Object remark;
    private String updatetime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getBankshort() {
        return bankshort;
    }

    public void setBankshort(String bankshort) {
        this.bankshort = bankshort;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getRemark() {
        return remark;
    }

    public void setRemark(Object remark) {
        this.remark = remark;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }
}
