package com.yiyue.store.component.greendao;

import android.content.Context;

import com.yiyue.store.YLApplication;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.local.greendao.DaoMaster;
import com.yiyue.store.model.local.greendao.DaoSession;
import com.yiyue.store.model.vo.bean.daobean.GroupBean;
import com.yiyue.store.module.im.daoutil.ChatNoFriendDaoUtils;
import com.yiyue.store.module.im.daoutil.GroupDaoUtils;
import com.yiyue.store.module.im.daoutil.GroupUserBeanDaoUtils;
import com.yiyue.store.module.im.daoutil.ImMessageDaoUtils;
import com.yiyue.store.module.im.daoutil.OrderMessageDaoUtils;
import com.yiyue.store.module.im.daoutil.SysMessageDaoUtils;
import com.yiyue.store.module.im.daoutil.UserFriendDaoUtils;

import java.util.List;
import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by zhangzz on 2018/10/17
 * 数据库管理类
 */

public class DaoManager {
    private DaoMaster daoMaster;
    private DaoSession daoSession = null;
    private DaoMaster.DevOpenHelper devOpenHelper;

    public DaoManager() {
    }

    public void init(Context context) {
        new MySQLiteOpenHelper(context, Constants.DB_NAME_FRIEND + AccountManager.getInstance().getUserId(), null);
        new MySQLiteOpenHelper(context, Constants.DB_NAME_GROUP + AccountManager.getInstance().getUserId(), null);
        new MySQLiteOpenHelper(context, Constants.DB_NAME_NOFRIEND + AccountManager.getInstance().getUserId(), null);
        new MySQLiteOpenHelper(context, Constants.DB_NAME_ORDER + AccountManager.getInstance().getUserId(), null);
        new MySQLiteOpenHelper(context, Constants.DB_NAME_IM + AccountManager.getInstance().getUserId(), null);
        new MySQLiteOpenHelper(context, Constants.DB_NAME_SYS + AccountManager.getInstance().getUserId(), null);
    }

    public DaoMaster getDaoManager(Context context, String DB_NAME) {
        if (daoMaster == null) {
            devOpenHelper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
            daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        }
        return daoMaster;
    }

    public DaoSession getDaoSession() {
        if (daoSession == null) {
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

    /**
     * 关闭DaoSession
     */
    private void closeDaoSession() {
        if (daoSession != null) {
            daoSession.clear();
            daoSession = null;
        }
    }

    /**
     * 关闭Helper
     */
    private void closeHelper() {
        if (devOpenHelper != null) {
            devOpenHelper.close();
            devOpenHelper = null;
        }
    }

    /**
     * 关闭所有的操作
     */
    public void closeConnection() {
        closeDaoSession();
        closeHelper();
        daoMaster = null;
    }

    public void dropTable(Context context, String DB_NAME) {
        if (devOpenHelper == null) {
            devOpenHelper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
        }
        daoMaster.dropAllTables(devOpenHelper.getWritableDb(), true);
    }

    /**
     * 删除所有群成员表(依赖群列表数据库进行 表名查找删除 所有优先级是先删除群成员库 再删除群列表库)
     * 然后调用环信退出方法 清空好友和群组表 退出环信
     *
     * @param context
     * @param userId
     * @param isDelete  是否是清空数据库表中数据
     */
    public void dropTables(Context context, String userId, boolean isDelete) {
        GroupDaoUtils groupDaoUtils = new GroupDaoUtils(context);
        groupDaoUtils.setOnQueryAllInterface(new DaoCallBackInterface.OnQueryAllInterface<GroupBean>() {
            @Override
            public void onQueryAllBatchInterface(List<GroupBean> list) {
                Task.callInBackground(new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        if (list != null && list.size() > 0) {
                            for (GroupBean groupBean : list) {
                                GroupUserBeanDaoUtils groupUserBeanDaoUtils = new GroupUserBeanDaoUtils(context, groupBean.getId() + AccountManager.getInstance().getUserId() + "_db");

                                if (isDelete) {
                                    groupUserBeanDaoUtils.deleteAll();
                                } else {
                                    groupUserBeanDaoUtils.dropTable(context, groupBean.getId() + "_db");
                                }
                            }
                        }
                        return null;
                    }
                }).continueWith(new Continuation<Void, Object>() {
                    @Override
                    public Object then(Task<Void> task) throws Exception {
                        clearDao(userId, isDelete);

                        return null;
                    }
                }, Task.UI_THREAD_EXECUTOR);

            }

            @Override
            public void onQueryAllBatchFailInterface() {
                clearDao(userId, isDelete);
            }
        });

        groupDaoUtils.queryAllUser();
    }

    public void clearDao(String userId, boolean isDelete) {
        UserFriendDaoUtils userFriendDaoUtils = new UserFriendDaoUtils(YLApplication.getContext());

        GroupDaoUtils groupDaoUtils = new GroupDaoUtils(YLApplication.getContext());

        ChatNoFriendDaoUtils chatNoFriendDaoUtils = new ChatNoFriendDaoUtils(YLApplication.getContext());

        OrderMessageDaoUtils orderMessageDaoUtils = new OrderMessageDaoUtils(YLApplication.getContext());

        ImMessageDaoUtils imMessageDaoUtils = new ImMessageDaoUtils(YLApplication.getContext());

        SysMessageDaoUtils sysMessageDaoUtils = new SysMessageDaoUtils(YLApplication.getContext());

        if (isDelete) {
            userFriendDaoUtils.deleteAll();
            groupDaoUtils.deleteAll();
            chatNoFriendDaoUtils.deleteAll();
            orderMessageDaoUtils.deleteAll();
            imMessageDaoUtils.deleteAll();
            sysMessageDaoUtils.deleteAll();
        } else {
            userFriendDaoUtils.dropTable(YLApplication.getContext(), Constants.DB_NAME_FRIEND + userId);
            groupDaoUtils.dropTable(YLApplication.getContext(), Constants.DB_NAME_GROUP + userId);
            chatNoFriendDaoUtils.dropTable(YLApplication.getContext(), Constants.DB_NAME_NOFRIEND + userId);
            orderMessageDaoUtils.dropTable(YLApplication.getContext(), Constants.DB_NAME_ORDER + userId);
            imMessageDaoUtils.dropTable(YLApplication.getContext(), Constants.DB_NAME_IM + userId);
            sysMessageDaoUtils.dropTable(YLApplication.getContext(), Constants.DB_NAME_SYS + userId);
        }


        userFriendDaoUtils.closeConnection();//关闭数据库连接
        groupDaoUtils.closeConnection();
        chatNoFriendDaoUtils.closeConnection();
        orderMessageDaoUtils.closeConnection();
        imMessageDaoUtils.closeConnection();
        sysMessageDaoUtils.closeConnection();
    }
}
