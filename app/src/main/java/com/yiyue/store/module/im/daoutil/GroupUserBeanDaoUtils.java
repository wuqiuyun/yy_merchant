package com.yiyue.store.module.im.daoutil;

import android.content.Context;

import com.yiyue.store.component.greendao.DaoUtils;
import com.yiyue.store.model.local.greendao.GroupUserBeanDao;
import com.yiyue.store.model.vo.bean.daobean.GroupUserBean;

import org.greenrobot.greendao.async.AsyncOperation;
import org.greenrobot.greendao.async.AsyncOperationListener;
import org.greenrobot.greendao.async.AsyncSession;
import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangzz on 2018/10/26
 * 自定DaoUtil操作类
 */

public class GroupUserBeanDaoUtils extends DaoUtils<GroupUserBean> {

    public GroupUserBeanDaoUtils(Context context, String dbName) {
        super(context, dbName);
    }

    /**
     * 单个插入
     *
     * @param user
     */
    public void insertUser(GroupUserBean user) {
        insertSingle(user);
    }

    /**
     * 批量插入
     */
    public void insertBatchUser(List<GroupUserBean> list) {
        insertBatch(GroupUserBean.class, list);
    }

    /**
     * 条件查询
     */
    public void queryWhereUser(String imusername) {
        query(GroupUserBean.class, GroupUserBeanDao.Properties.Imusername.eq(imusername), imusername);
    }

    /**
     * 同步查询方法，异步自己在页面处理
     *
     * @param imusername
     * @return
     */
    public GroupUserBean querySynch(String imusername) {
        return querySynch(GroupUserBean.class, GroupUserBeanDao.Properties.Imusername.eq(imusername));
    }

    /**
     * 查询全部
     */
    public void queryAllUser() {
        queryAll(GroupUserBean.class, null);
//        return users;
    }

    /**
     * 条件删除
     */
    public void deleteSingleUser(int memberId) {
        GroupUserBean user = new GroupUserBean();
        user.setId(memberId);
        deleteSingle(user);
    }

    /**
     * 批量删除
     */
    public void deleteBatchUser(List<Integer> integerList) {
        List<GroupUserBean> userList = new ArrayList<>();
        for (int i = 0; i < integerList.size(); i++) {
            GroupUserBean user = new GroupUserBean();
            user.setId(integerList.get(i));
            userList.add(user);
        }
        deleteBatch(GroupUserBean.class, userList);
    }

    /**
     * 单个更新
     */
    public void updateSingleUser(GroupUserBean user) {
        updateSingle(GroupUserBean.class, user);
    }

    /**
     * 批量更新
     */
    public void updateBatchUser(List<GroupUserBean> list) {
        updateBatch(GroupUserBean.class, list);
    }

    /**
     * 根据Id 单个删除
     */
    public void deleteByIdSingleUser(long memberId) {
        deleteByIdSingle(GroupUserBean.class, memberId);
    }

    /**
     * 根据Id批量删除
     */
    public void deleteByIdBatchUser(List<Long> longList) {
        deleteByIdBatch(GroupUserBean.class, longList);
    }

    /**
     * 删除所有
     */
    public void deleteAll() {
        deleteAll(GroupUserBean.class);
    }

    /**
     * 条件批量删除
     */
    public void deleteWhereBatch(String ageString) {
        Query<GroupUserBean> build = daoSession.queryBuilder(GroupUserBean.class).where(GroupUserBeanDao.Properties._id.eq(ageString)).build();
        AsyncSession asyncSession = daoSession.startAsyncSession();
        asyncSession.setListenerMainThread(new AsyncOperationListener() {
            @Override
            public void onAsyncOperationCompleted(AsyncOperation operation) {
                if (operation.isCompletedSucessfully()) {
                    deleteBatch(GroupUserBean.class, (List<GroupUserBean>) operation.getResult());
                }
            }
        });
        asyncSession.queryList(build);
    }
}
