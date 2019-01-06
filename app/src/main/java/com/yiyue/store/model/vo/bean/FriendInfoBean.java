package com.yiyue.store.model.vo.bean;

/**
 * Created by zhangzz on 2018/10/17
 */
public class FriendInfoBean {
    /**
     * id : 8
     * createtime : 2018-10-11T07:41:09.000+0000
     * friendId : 100003
     * nickname : null
     * status : 0
     * updatetime : 2018-10-11T08:07:47.000+0000
     * userId : 100001
     * imusername : pktest002
     * path : https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1539422758542&di=bf27be6d5644423f50c97bc338ce0dcd&imgtype=0&src=http%3A%2F%2Fwww.goumin.com%2Fattachments%2Fphoto%2F0%2F0%2F69%2F17866%2F4573921.jpg
     * remarks : 例子
     * gender : null
     * labels : [{"userId":100003,"title":"可爱"},{"userId":100003,"title":"大方"}]
     */
    private int id;
    private String createtime;
    private int friendId;
    private String nickname;//昵称
    private int status;//0正常 1禁言 屏蔽好友与否用
    private String updatetime;
    private int userId;
    private String imusername;
    private String path;//头像路径
    private String remarks;//备注
    private String gender;//性别

    //新添加字段
    private Integer birthday;   //生日 20181017
    private String job;         //职业
    private String facetype;    //脸型
    private String hairlength;  //f发长
    private String hobby;       //爱好
    //标签
    private String label;       //标签
    private String index;//首字母索引

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public int getFriendId() {
        return friendId;
    }

    public void setFriendId(int friendId) {
        this.friendId = friendId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public Integer getBirthday() {
        return birthday;
    }

    public void setBirthday(Integer birthday) {
        this.birthday = birthday;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getFacetype() {
        return facetype;
    }

    public void setFacetype(String facetype) {
        this.facetype = facetype;
    }

    public String getHairlength() {
        return hairlength;
    }

    public void setHairlength(String hairlength) {
        this.hairlength = hairlength;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
