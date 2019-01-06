package com.yiyue.store.model.vo.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by lyj on  2018/10/29
 */

public class StoreCommentListBean {

    /**
     * id : 1
     * level : 11
     * serviceName : 洗剪吹
     * comment : 好丑啊，好喜欢
     * nickname : 我是美发师
     * reply : 很适合你
     * userId : 3
     * headImg : 嘻嘻嘻嘻嘻嘻嘻嘻寻寻寻寻寻寻寻。jpg
     * createtime : 2018-10-27 11:54:21
     * imgPaths : ["1112","2222"]
     */

    private int id;
    private float level;
    private String serviceName;
    private String comment;
    private String nickname;
    private String reply;
    private int userId;
    private String headImg;
    private String createtime;
    private ArrayList<String> imgPaths;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getLevel() {
        return level;
    }

    public void setLevel(float level) {
        this.level = level;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public ArrayList<String> getImgPaths() {
        return imgPaths;
    }

    public void setImgPaths(ArrayList<String> imgPaths) {
        this.imgPaths = imgPaths;
    }
}
