package com.yiyue.store.model.vo.bean;

import java.util.List;

/**
 * Created by Lizhuo on 2018/10/24.
 * 美发师名片详情
 */
public class StylistCardBean {

    /**
     * position : 总监
     * nexus : 2
     * nickname : 我是美发师
     * star : 5.5
     * lable : 洗剪吹168元
     * cardOpusDTOs : [{"stylistOpusCovers":"xxxxxxxxxxx.jpg","stylistOpusId":1},{"stylistOpusCovers":"https://source.yunyl.com/1.jpg","stylistOpusId":2},{"stylistOpusCovers":"xxxxxxx.jpg","stylistOpusId":3}]
     * point : 5.5
     * stylistId : 1
     * cardStoreDTOs : [{"picture":"http://source.yunyl.com/photo/3.jpg","storename":"你说什么我","distance":1.18,"location":"月球","storeId":1}]
     * serverTypes : 烫发、染发、洗剪吹
     * isCollection : false
     * userId : 3
     * gender : 0
     * headPortrait : 嘻嘻嘻嘻嘻嘻嘻嘻寻寻寻寻寻寻寻。jpg
     * cardServerItems : [{"picture":"xxx.jpg","serviceId":2,"price":24,"name":"烫发"},{"picture":"xxx.jpg","serviceId":3,"price":33,"name":"染发"},{"serviceId":4,"price":33,"name":"洗剪吹"}]
     * mobile : 13729026555
     */
    private String backGroundImg;   //美发师背景图
    private String position;
    private int nexus;//入驻关系 0 入驻 1 签约，2没关系 ,
    private String nickname;
    private float star;
    private String lable;
    private double point;
    private int stylistId;//美发师ID
    private String serverTypes;
    private boolean isCollection;
    private int userId;//用户ID
    private int gender;
    private String headPortrait;
    private String mobile;
    private String yearbirth;//00后/白羊座/IT经营

    private long evaluatenumer; //全部评价

    private String imusername;//环信id

    private List<CardOpusDTOsBean> cardOpusDTOs;
    private List<CardStoreDTOsBean> cardStoreDTOs;
    private List<CardServerItemsBean> cardServerItems;
    private List<CardGradeDTOBean> cardGradeDTOs;
    private List<CardPackages> cardPackages;

    public String getBackGroundImg() {
        return backGroundImg;
    }

    public void setBackGroundImg(String backGroundImg) {
        this.backGroundImg = backGroundImg;
    }

    public long getEvaluatenumer() {
        return evaluatenumer;
    }

    public void setEvaluatenumer(long evaluatenumer) {
        this.evaluatenumer = evaluatenumer;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getNexus() {
        return nexus;
    }

    public void setNexus(int nexus) {
        this.nexus = nexus;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public float getStar() {
        return star;
    }

    public void setStar(float star) {
        this.star = star;
    }

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public double getPoint() {
        return point;
    }

    public void setPoint(double point) {
        this.point = point;
    }

    public int getStylistId() {
        return stylistId;
    }

    public void setStylistId(int stylistId) {
        this.stylistId = stylistId;
    }

    public String getServerTypes() {
        return serverTypes;
    }

    public void setServerTypes(String serverTypes) {
        this.serverTypes = serverTypes;
    }

    public boolean isIsCollection() {
        return isCollection;
    }

    public void setIsCollection(boolean isCollection) {
        this.isCollection = isCollection;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getYearbirth() {
        return yearbirth;
    }

    public void setYearbirth(String yearbirth) {
        this.yearbirth = yearbirth;
    }

    public String getImusername() {
        return imusername;
    }

    public void setImusername(String imusername) {
        this.imusername = imusername;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public List<com.yiyue.store.model.vo.bean.StylistCardBean.CardOpusDTOsBean> getCardOpusDTOs() {
        return cardOpusDTOs;
    }

    public void setCardOpusDTOs(List<com.yiyue.store.model.vo.bean.StylistCardBean.CardOpusDTOsBean> cardOpusDTOs) {
        this.cardOpusDTOs = cardOpusDTOs;
    }

    public List<com.yiyue.store.model.vo.bean.StylistCardBean.CardStoreDTOsBean> getCardStoreDTOs() {
        return cardStoreDTOs;
    }

    public void setCardStoreDTOs(List<com.yiyue.store.model.vo.bean.StylistCardBean.CardStoreDTOsBean> cardStoreDTOs) {
        this.cardStoreDTOs = cardStoreDTOs;
    }

    public List<com.yiyue.store.model.vo.bean.StylistCardBean.CardServerItemsBean> getCardServerItems() {
        return cardServerItems;
    }

    public void setCardServerItems(List<com.yiyue.store.model.vo.bean.StylistCardBean.CardServerItemsBean> cardServerItems) {
        this.cardServerItems = cardServerItems;
    }

    public List<CardGradeDTOBean> getCardGradeDTOs() {
        return cardGradeDTOs;
    }

    public void setCardGradeDTOs(List<CardGradeDTOBean> cardGradeDTOs) {
        this.cardGradeDTOs = cardGradeDTOs;
    }

    public List<CardPackages> getCardPackages() {
        return cardPackages;
    }

    public void setCardPackages(List<CardPackages> cardPackages) {
        this.cardPackages = cardPackages;
    }

    public static class CardGradeDTOBean {
        private String gradeDescrip;
        private String gradeType;
        private double point;

        public String getGradeDescrip() {
            return gradeDescrip;
        }

        public void setGradeDescrip(String gradeDescrip) {
            this.gradeDescrip = gradeDescrip;
        }

        public String getGradeType() {
            return gradeType;
        }

        public void setGradeType(String gradeType) {
            this.gradeType = gradeType;
        }

        public double getPoint() {
            return point;
        }

        public void setPoint(double point) {
            this.point = point;
        }
    }

    public static class CardOpusDTOsBean {
        /**
         * stylistOpusCovers : xxxxxxxxxxx.jpg
         * stylistOpusId : 1
         */

        private String stylistOpusCovers;
        private int stylistOpusId;

        public String getStylistOpusCovers() {
            return stylistOpusCovers;
        }

        public void setStylistOpusCovers(String stylistOpusCovers) {
            this.stylistOpusCovers = stylistOpusCovers;
        }

        public int getStylistOpusId() {
            return stylistOpusId;
        }

        public void setStylistOpusId(int stylistOpusId) {
            this.stylistOpusId = stylistOpusId;
        }
    }

    public static class CardStoreDTOsBean {
        /**
         * picture : http://source.yunyl.com/photo/3.jpg
         * storename : 你说什么我
         * distance : 1.18
         * location : 月球
         * storeId : 1
         */

        private String picture;
        private String storename;
        private double distance;
        private String location;
        private int storeId;

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getStorename() {
            return storename;
        }

        public void setStorename(String storename) {
            this.storename = storename;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public int getStoreId() {
            return storeId;
        }

        public void setStoreId(int storeId) {
            this.storeId = storeId;
        }
    }

    public static class CardServerItemsBean {
        /**
         * picture : xxx.jpg
         * serviceId : 2
         * price : 24
         * name : 烫发
         */

        private String picture;
        private int serviceId;
        private double price;
        private String name;

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public int getServiceId() {
            return serviceId;
        }

        public void setServiceId(int serviceId) {
            this.serviceId = serviceId;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class CardPackages {
        /**
         * name (string, optional): 套餐名称 ,
         * packageId (integer, optional): 套餐ID ,
         * price (number, optional): 套餐价格
         */

        private String name;
        private int packageId;
        private String price;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPackageId() {
            return packageId;
        }

        public void setPackageId(int packageId) {
            this.packageId = packageId;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

    }

}
