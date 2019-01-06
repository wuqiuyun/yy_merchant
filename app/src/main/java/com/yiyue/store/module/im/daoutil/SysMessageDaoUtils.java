package com.yiyue.store.module.im.daoutil;

import android.content.Context;

import com.yiyue.store.component.greendao.DaoUtils;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.vo.bean.daobean.SysMessageBean;

import java.util.List;

/**
 * Created by Lizhuo on 2018/11/13.
 * 系统消息——系统通知操作类
 */
public class SysMessageDaoUtils extends DaoUtils<SysMessageBean> {
    public SysMessageDaoUtils(Context context) {
        super(context, Constants.DB_NAME_SYS + AccountManager.getInstance().getUserId());
    }

    /**
     * 单个插入
     *
     * @param message
     */
    public void insert(SysMessageBean message) {
        insertSingle(message);
    }

    /**
     * 批量插入
     */
    public void insertBatch(List<SysMessageBean> list) {
        insertBatch(SysMessageBean.class, list);
    }


    /**
     * 查询全部
     */
    public void queryAll() {
        queryAll(SysMessageBean.class, null);
//        return users;
    }

    /**
     * 单个更新
     */
    public void updateSingle(SysMessageBean user) {
        updateSingle(SysMessageBean.class, user);
    }

    /**
     * 批量更新
     */
    public void updateBatchUser(List<SysMessageBean> list) {
        updateBatch(SysMessageBean.class, list);
    }

    /**
     * 删除所有
     */
    public void deleteAll() {
        deleteAll(SysMessageBean.class);
    }

//    /**
//     * 条件批量删除
//     */
//    public void deleteWhereBatch(String ageString) {
//        Query<SysMessageBean> build = daoSession.queryBuilder(SysMessageBean.class).where(SysMessageBeanDao.Properties._id.eq(ageString)).build();
//        AsyncSession asyncSession = daoSession.startAsyncSession();
//        asyncSession.setListenerMainThread(new AsyncOperationListener() {
//            @Override
//            public void onAsyncOperationCompleted(AsyncOperation operation) {
//                if (operation.isCompletedSucessfully()) {
//                    deleteBatch(SysMessageBean.class, (List<SysMessageBean>) operation.getResult());
//                }
//            }
//        });
//        asyncSession.queryList(build);
//    }
}
