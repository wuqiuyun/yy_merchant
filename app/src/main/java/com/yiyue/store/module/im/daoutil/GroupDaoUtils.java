package com.yiyue.store.module.im.daoutil;

import android.content.Context;

import com.yiyue.store.component.greendao.DaoManager;
import com.yiyue.store.component.greendao.DaoUtils;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.local.greendao.GroupBeanDao;
import com.yiyue.store.model.local.greendao.UserFriendsBeanDao;
import com.yiyue.store.model.vo.bean.daobean.GroupBean;
import com.yiyue.store.model.vo.bean.daobean.UserFriendsBean;

import org.greenrobot.greendao.async.AsyncOperation;
import org.greenrobot.greendao.async.AsyncOperationListener;
import org.greenrobot.greendao.async.AsyncSession;
import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by zhangzz on 2018/10/17
 * 自定DaoUtil操作类
 */

public class GroupDaoUtils extends DaoUtils<GroupBean> {

    public GroupDaoUtils(Context context) {
        super(context, Constants.DB_NAME_GROUP + AccountManager.getInstance().getUserId());
    }

    /**
     * 单个插入
     *
     * @param user
     */
    public void insertUser(GroupBean user) {
        insertSingle(user);
    }

    /**
     * 批量插入
     */
    public void insertBatchUser(List<GroupBean> list) {
        insertBatch(GroupBean.class, list);
    }

    /**
     * 条件查询
     */
    public void queryWhereUser(String imusername) {
        query(GroupBean.class, GroupBeanDao.Properties.Imgroup.eq(imusername), imusername);
    }

    /**
     * 同步查询方法，异步自己在页面处理
     *
     * @param imusername
     * @return
     */
    public GroupBean querySynch(String imusername) {
        return querySynch(GroupBean.class, GroupBeanDao.Properties.Imgroup.eq(imusername));
    }


    /**
     * 查询全部
     */
    public void queryAllUser() {
        queryAll(GroupBean.class, null);
//        return users;
    }

    /**
     * 条件删除
     */
    public void deleteSingleUser(int memberId) {
        GroupBean user = new GroupBean();
        user.setId(memberId);
        deleteSingle(user);
    }

    /**
     * 批量删除
     */
    public void deleteBatchUser(List<Integer> integerList) {
        List<GroupBean> userList = new ArrayList<>();
        for (int i = 0; i < integerList.size(); i++) {
            GroupBean user = new GroupBean();
            user.setId(integerList.get(i));
            userList.add(user);
        }
        deleteBatch(GroupBean.class, userList);
    }

    /**
     * 单个更新
     */
    public void updateSingleUser(GroupBean user) {
        updateSingle(GroupBean.class, user);
    }

    /**
     * 批量更新
     */
    public void updateBatchUser(List<GroupBean> list) {
        updateBatch(GroupBean.class, list);
    }

    /**
     * 根据Id 单个删除
     */
    public void deleteByIdSingleUser(long memberId) {
        deleteByIdSingle(GroupBean.class, memberId);
    }

    /**
     * 根据Id批量删除
     */
    public void deleteByIdBatchUser(List<Long> longList) {
        deleteByIdBatch(GroupBean.class, longList);
    }

    /**
     * 删除所有
     */
    public void deleteAll() {
        deleteAll(GroupBean.class);
    }

    /**
     * 条件批量删除
     */
    public void deleteWhereBatch(String ageString) {
        Query<GroupBean> build = daoSession.queryBuilder(GroupBean.class).where(GroupBeanDao.Properties._id.eq(ageString)).build();
        AsyncSession asyncSession = daoSession.startAsyncSession();
        asyncSession.setListenerMainThread(new AsyncOperationListener() {
            @Override
            public void onAsyncOperationCompleted(AsyncOperation operation) {
                if (operation.isCompletedSucessfully()) {
                    deleteBatch(GroupBean.class, (List<GroupBean>) operation.getResult());
                }
            }
        });
        asyncSession.queryList(build);
    }

    /**
     * 模糊查询
     * 以name
     *
     * @param name 以群Id和群名称模糊搜索
     * @return
     */
    public List<GroupBean> query(String name) {
        List<GroupBean> listName = new ArrayList<>();
        List<GroupBean> listId = new ArrayList<>();

        listName = daoSession
                .getGroupBeanDao()
                .queryBuilder()
                .where(GroupBeanDao.Properties.Name.like("%" + name + "%"))
                .orderDesc(GroupBeanDao.Properties._id)
                .list();
        listId = daoSession
                .getGroupBeanDao()
                .queryBuilder()
                .where(GroupBeanDao.Properties.Id.like("%" + name + "%"))
                .orderDesc(GroupBeanDao.Properties._id)
                .list();

        //去重
        Set s = new HashSet(listName);
        for (GroupBean i : listId) {
            if (!s.add(i)) {
                s.remove(i);
            }
        }

        List listResult = new ArrayList(s);
        return listResult;
    }

}
