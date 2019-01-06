package com.yiyue.store.module.im.daoutil;

import android.content.Context;

import com.yiyue.store.component.greendao.DaoUtils;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.local.greendao.UserFriendsBeanDao;
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

public class UserFriendDaoUtils extends DaoUtils<UserFriendsBean> {

    public UserFriendDaoUtils(Context context) {
        super(context, Constants.DB_NAME_FRIEND + AccountManager.getInstance().getUserId());
    }

    /**
     * 单个插入
     *
     * @param user
     */
    public void insertUser(UserFriendsBean user) {
        insertSingle(user);
    }

    /**
     * 批量插入
     */
    public void insertBatchUser(List<UserFriendsBean> list) {
        insertBatch(UserFriendsBean.class, list);
    }

    /**
     * 模糊查询
     * 以nikename和remark分别查一次，因为后台给了两个字段
     *
     * @param nickName
     * @return
     */
    public List<UserFriendsBean> query(String nickName) {
        List<UserFriendsBean> listNick = new ArrayList<>();
        List<UserFriendsBean> listRemark = new ArrayList<>();

        listNick = daoSession
                .getUserFriendsBeanDao()
                .queryBuilder()
                .where(UserFriendsBeanDao.Properties.Nickname.like("%" + nickName + "%"))
                .orderDesc(UserFriendsBeanDao.Properties._id)
                .list();
        listRemark = daoSession
                .getUserFriendsBeanDao()
                .queryBuilder()
                .where(UserFriendsBeanDao.Properties.Remarks.like("%" + nickName + "%"))
                .orderDesc(UserFriendsBeanDao.Properties._id)
                .list();

        //去重
        Set s = new HashSet(listNick);
        for (UserFriendsBean i : listRemark) {
            if (!s.add(i)) {
                s.remove(i);
            }
        }

        List listResult = new ArrayList(s);
        return listResult;
    }

    /**
     * 条件查询
     */
    public void queryWhereUser(String imusername) {
        query(UserFriendsBean.class, UserFriendsBeanDao.Properties.Imusername.eq(imusername), imusername);
    }

    /**
     * 同步查询方法，异步自己在页面处理
     *
     * @param imusername
     * @return
     */
    public UserFriendsBean querySynch(String imusername) {
        return querySynch(UserFriendsBean.class, UserFriendsBeanDao.Properties.Imusername.eq(imusername));
    }

    /**
     * 查询全部
     */
    public void queryAllUser() {
        queryAll(UserFriendsBean.class, null);
//        return users;
    }

    /**
     * 条件删除
     */
    public void deleteSingleUser(int memberId) {
        UserFriendsBean user = new UserFriendsBean();
        user.setId(memberId);
        deleteSingle(user);
    }

    /**
     * 批量删除
     */
    public void deleteBatchUser(List<Integer> integerList) {
        List<UserFriendsBean> userList = new ArrayList<>();
        for (int i = 0; i < integerList.size(); i++) {
            UserFriendsBean user = new UserFriendsBean();
            user.setId(integerList.get(i));
            userList.add(user);
        }
        deleteBatch(UserFriendsBean.class, userList);
    }

    /**
     * 单个更新
     */
    public void updateSingleUser(UserFriendsBean user) {
        updateSingle(UserFriendsBean.class, user);
    }

    /**
     * 批量更新
     */
    public void updateBatchUser(List<UserFriendsBean> list) {
        updateBatch(UserFriendsBean.class, list);
    }

    /**
     * 根据Id 单个删除
     */
    public void deleteByIdSingleUser(long memberId) {
        deleteByIdSingle(UserFriendsBean.class, memberId);
    }

    /**
     * 根据Id批量删除
     */
    public void deleteByIdBatchUser(List<Long> longList) {
        deleteByIdBatch(UserFriendsBean.class, longList);
    }

    /**
     * 删除所有
     */
    public void deleteAll() {
        deleteAll(UserFriendsBean.class);
    }

    /**
     * 条件批量删除
     */
    public void deleteWhereBatch(String ageString) {
        Query<UserFriendsBean> build = daoSession.queryBuilder(UserFriendsBean.class).where(UserFriendsBeanDao.Properties._id.eq(ageString)).build();
        AsyncSession asyncSession = daoSession.startAsyncSession();
        asyncSession.setListenerMainThread(new AsyncOperationListener() {
            @Override
            public void onAsyncOperationCompleted(AsyncOperation operation) {
                if (operation.isCompletedSucessfully()) {
                    deleteBatch(UserFriendsBean.class, (List<UserFriendsBean>) operation.getResult());
                }
            }
        });
        asyncSession.queryList(build);
    }

}
