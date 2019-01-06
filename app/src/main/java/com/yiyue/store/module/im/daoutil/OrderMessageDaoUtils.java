package com.yiyue.store.module.im.daoutil;

import android.content.Context;

import com.yiyue.store.component.greendao.DaoUtils;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.local.greendao.OrderMessageBeanDao;
import com.yiyue.store.model.vo.bean.daobean.OrderMessageBean;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lizhuo on 2018/11/13.
 * 系统消息——订单交易操作类
 */
public class OrderMessageDaoUtils extends DaoUtils<OrderMessageBean> {
    public OrderMessageDaoUtils(Context context) {
        super(context, Constants.DB_NAME_ORDER + AccountManager.getInstance().getUserId());
    }

    /**
     * 单个插入
     *
     * @param message
     */
    public void insert(OrderMessageBean message) {
        insertSingle(message);
    }

    /**
     * 批量插入
     */
    public void insertBatch(List<OrderMessageBean> list) {
        insertBatch(OrderMessageBean.class, list);
    }

    /**
     * 条件查询 orderId
     */
    public void queryWhereUser(String orderId) {
        query(OrderMessageBean.class, OrderMessageBeanDao.Properties.OrderId.eq(orderId), orderId);
    }

    /**
     * 同步查询方法，异步自己在页面处理
     *
     * @param orderId
     * @return
     */
    public OrderMessageBean querySynch(String orderId) {
        return querySynch(OrderMessageBean.class, OrderMessageBeanDao.Properties.OrderId.eq(orderId));
    }


    /**
     * 查询全部
     */
    public void queryAll() {
        queryAll(OrderMessageBean.class, null);
//        return users;
    }

    /**
     * 条件删除
     */
    public void deleteSingle(long orderId) {
        OrderMessageBean user = new OrderMessageBean();
        user.setOrderId(orderId);
        deleteSingle(user);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Long> longList) {
        List<OrderMessageBean> userList = new ArrayList<>();
        for (int i = 0; i < longList.size(); i++) {
            OrderMessageBean user = new OrderMessageBean();
            user.setOrderId(longList.get(i));
            userList.add(user);
        }
        deleteBatch(OrderMessageBean.class, userList);
    }

    /**
     * 单个更新
     */
    public void updateSingle(OrderMessageBean user) {
        updateSingle(OrderMessageBean.class, user);
    }

    /**
     * 批量更新
     */
    public void updateBatchUser(List<OrderMessageBean> list) {
        updateBatch(OrderMessageBean.class, list);
    }

    /**
     * 删除所有
     */
    public void deleteAll() {
        deleteAll(OrderMessageBean.class);
    }

//    /**
//     * 条件批量删除
//     */
//    public void deleteWhereBatch(String ageString) {
//        Query<OrderMessageBean> build = daoSession.queryBuilder(OrderMessageBean.class).where(OrderMessageBeanDao.Properties._id.eq(ageString)).build();
//        AsyncSession asyncSession = daoSession.startAsyncSession();
//        asyncSession.setListenerMainThread(new AsyncOperationListener() {
//            @Override
//            public void onAsyncOperationCompleted(AsyncOperation operation) {
//                if (operation.isCompletedSucessfully()) {
//                    deleteBatch(OrderMessageBean.class, (List<OrderMessageBean>) operation.getResult());
//                }
//            }
//        });
//        asyncSession.queryList(build);
//    }
}
