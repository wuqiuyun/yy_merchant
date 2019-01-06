package com.yiyue.store.model.vo.bean;

/**
 * Created by Lizhuo on 2018/11/19.
 * 公告详情
 */
public class BulletinDetailBean {
    /**
     "content": "<!DOCTYPE html>\r\n<!-- saved from url=(0022)https://www.baidu.com/ -->\r\n<bodu>nihao\r\n</body></html>",
     */
    
    private long id;
    private long createtime;
    private long updatetime;
    private long endtime;
    private long starttime;
    private String author;
    private int status;
    private String title;
    private String desc;
    private String path;
    private String content; //"content": "<!DOCTYPE html>\r\n<!-- saved from url=(0022)https://www.baidu.com/ -->\r\n<bodu>nihao\r\n</body></html>",
    private int isRead; //0  未读   1 已读

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public long getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(long updatetime) {
        this.updatetime = updatetime;
    }

    public long getEndtime() {
        return endtime;
    }

    public void setEndtime(long endtime) {
        this.endtime = endtime;
    }

    public long getStarttime() {
        return starttime;
    }

    public void setStarttime(long starttime) {
        this.starttime = starttime;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }
}
