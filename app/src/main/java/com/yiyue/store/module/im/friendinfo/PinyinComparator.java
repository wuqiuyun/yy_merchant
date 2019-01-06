package com.yiyue.store.module.im.friendinfo;

import com.yiyue.store.model.vo.bean.daobean.UserFriendsBean;

import java.util.Comparator;

/**
 * Created by zhangzz on 2018/10/17
 */
public class PinyinComparator implements Comparator<UserFriendsBean> {
    public int compare(UserFriendsBean o1, UserFriendsBean o2) {
        //这里主要是用来对List里面的数据根据ABCDEFG...来排序
        if (o2.getIndex().equals("#")) {
            return -1;
        } else if (o1.getIndex().equals("#")) {
            return 1;
        } else {
            return o1.getIndex().compareTo(o2.getIndex());
        }
    }
}
