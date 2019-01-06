package com.yiyue.store.module.im.daoutil;

import android.content.Context;

import com.yiyue.store.component.greendao.DaoUtils;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.local.greendao.ImMessageBeanDao;
import com.yiyue.store.model.vo.bean.EventBean;
import com.yiyue.store.model.vo.bean.daobean.ImMessageBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.greendao.async.AsyncSession;
import org.greenrobot.greendao.query.Query;

import java.util.List;

/**
 * Created by Lizhuo on 2018/11/13.
 * 系统消息——互动消息操作类
 */
public class ImMessageDaoUtils extends DaoUtils<ImMessageBean> {
    public ImMessageDaoUtils(Context context) {
        super(context, Constants.DB_NAME_IM + AccountManager.getInstance().getUserId());
    }

    /**
     * 单个插入
     *
     * @param message
     */
    public void insert(ImMessageBean message) {
        insertSingle(message);
    }

    /**
     * 根据id查询消息决定是否插入或更新
     */
    public void insertOrUpdate(ImMessageBean message, String id){
        Query<ImMessageBean> build = daoSession.queryBuilder(ImMessageBean.class).where(ImMessageBeanDao.Properties.Id.eq(id)).build();
        AsyncSession asyncSession = daoSession.startAsyncSession();
        asyncSession.setListenerMainThread(operation -> {
            if (operation.isCompletedSucessfully()) {
                if (operation.getResult() != null) {
                    //代表有数据 则删除原来的旧数据 插入最新的数据
//                    message.set_id(((ImMessageBean) operation.getResult()).get_id());
//                    setOnUpdateInterface(type -> {
//                        if (type) {
//                            EventBus.getDefault().post(new EventBean.NewImMessage());
//                        }
//                    });
//                    updateSingle(ImMessageBean.class, message);

                    setOnDeleteInterface(type -> {
                        if (type){
                            insertSingle(message);
                        }
                    });
                    setOnIsertInterface(type -> {
                        if (type) {
                            EventBus.getDefault().post(new EventBean.NewImMessage());
                        }
                    });

                    deleteSingle(((ImMessageBean) operation.getResult()).get_id());

                } else {
                    setOnIsertInterface(type -> {
                        if (type) {
                            EventBus.getDefault().post(new EventBean.NewImMessage());
                        }
                    });
                    insertSingle(message);
                }
            } else {
                //查询失败
            }
        });
        asyncSession.queryUnique(build);
    }
    
    /**
     * 批量插入
     */
    public void insertBatch(List<ImMessageBean> list) {
        insertBatch(ImMessageBean.class, list);
    }


    /**
     * 查询全部
     */
    public void queryAll() {
        queryAll(ImMessageBean.class, null);
//        return users;
    }

    /**
     * 单个更新
     */
    public void updateSingle(ImMessageBean user) {
        updateSingle(ImMessageBean.class, user);
    }

    /**
     * 批量更新
     */
    public void updateBatchUser(List<ImMessageBean> list) {
        updateBatch(ImMessageBean.class, list);
    }

    /**
     * 条件删除
     */
    public void deleteSingle(long _id) {
        ImMessageBean message = new ImMessageBean();
        message.set_id(_id);
        deleteSingle(message);
    }

    /**
     * 删除所有
     */
    public void deleteAll() {
        deleteAll(ImMessageBean.class);
    }

//    /**
//     * 条件批量删除
//     */
//    public void deleteWhereBatch(String ageString) {
//        Query<ImMessageBean> build = daoSession.queryBuilder(ImMessageBean.class).where(ImMessageBeanDao.Properties._id.eq(ageString)).build();
//        AsyncSession asyncSession = daoSession.startAsyncSession();
//        asyncSession.setListenerMainThread(new AsyncOperationListener() {
//            @Override
//            public void onAsyncOperationCompleted(AsyncOperation operation) {
//                if (operation.isCompletedSucessfully()) {
//                    deleteBatch(ImMessageBean.class, (List<ImMessageBean>) operation.getResult());
//                }
//            }
//        });
//        asyncSession.queryList(build);
//    }
}
