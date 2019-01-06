package com.yiyue.store.model.vo.bean;

/**
 * Created by zhangzz on 2018/10/16
 */
public class SearchUserBean {
    /**
     * id : 100004
     * createtime : 2018-10-12 15:26:28
     * gender : 2
     * idNo : 2131312
     * mobile : 18300000000
     * nickname : adsa
     * password : null
     * role : 1
     * salf : wedq
     * status : 0
     * updatetime : 2018-10-12 15:26:31
     * username : null
     * imusername : null
     * path : 宝塔镇河妖
     * labels : [{"userId":100004,"title":"温柔"}]
     */

    private int id;//搜索出的用户的useId
    private String createtime;
    private int gender;
    private String idNo;
    private String mobile;
    private String nickname;
    private String password;
    private int role;
    private String salf;
    private int status;
    private long updatetime;
    private String username;
    private String imusername;
    private String path;
    //新添加字段
    private Integer birthday;   //生日 20181017
    private String job;         //职业
    private String facetype;    //脸型
    private String hairlength;  //f发长
    private String hobby;       //爱好
    //标签
    private String label;       //标签

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

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getSalf() {
        return salf;
    }

    public void setSalf(String salf) {
        this.salf = salf;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(long updatetime) {
        this.updatetime = updatetime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
