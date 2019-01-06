package com.yiyue.store.model.vo.bean;

/**
 * Created by Lizhuo on 2018/10/12.
 */
public class EventOrderBean {
    public static class OrderStatisticsBean{
        private String nexus;
        private String type;


        public String getNexus() {
            return nexus;
        }

        public String getType() {
            return type;
        }

        public void setNexus(String nexus) {
            this.nexus = nexus;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
