package com.yiyue.store.model.vo.bean;

import java.util.List;

/**
 * Created by zhangzz on 2018/11/9
 */
public class RedRecordBean {
     /**
      *   * "total": 2,
      "quantity": 2,
      */
     private double total;//总共多少钱
     private int quantity;//几个红包
     private List<RedBagBean> lsit;

     public double getTotal() {
          return total;
     }

     public void setTotal(double total) {
          this.total = total;
     }

     public int getQuantity() {
          return quantity;
     }

     public void setQuantity(int quantity) {
          this.quantity = quantity;
     }

     public List<RedBagBean> getLsit() {
          return lsit;
     }

     public void setLsit(List<RedBagBean> lsit) {
          this.lsit = lsit;
     }
}
