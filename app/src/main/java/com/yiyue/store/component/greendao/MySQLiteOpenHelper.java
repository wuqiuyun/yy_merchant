package com.yiyue.store.component.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.yiyue.store.model.local.greendao.ChatNoFriendBeanDao;
import com.yiyue.store.model.local.greendao.DaoMaster;
import com.yiyue.store.model.local.greendao.GroupBeanDao;
import com.yiyue.store.model.local.greendao.ImMessageBeanDao;
import com.yiyue.store.model.local.greendao.OrderMessageBeanDao;
import com.yiyue.store.model.local.greendao.SysMessageBeanDao;
import com.yiyue.store.model.local.greendao.UserFriendsBeanDao;
import com.yiyue.store.model.vo.bean.daobean.OrderMessageBean;
import com.yl.core.component.log.DLog;

import org.greenrobot.greendao.database.Database;

/**
 * Created by zhangzz on 2018/10/18
 * 数据库更新类
 */

public class MySQLiteOpenHelper extends DaoMaster.OpenHelper {


    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        DLog.i("greenDAO",
                "Upgrading schema from version " + oldVersion + " to " + newVersion + " by migrating all tables data");
        MigrationHelper.getInstance().migrate(db, UserFriendsBeanDao.class);//好友列表类
        MigrationHelper.getInstance().migrate(db, GroupBeanDao.class);//群组列表类
        MigrationHelper.getInstance().migrate(db, ChatNoFriendBeanDao.class);//非好友列表类
//        MigrationHelper.getInstance().migrate(db, OrderMessageBeanDao.class);//系统消息——订单消息
        MigrationHelper.getInstance().migrate(db, ImMessageBeanDao.class);//系统消息——互动消息
        MigrationHelper.getInstance().migrate(db, SysMessageBeanDao.class);//系统消息——系统通知
    }

}