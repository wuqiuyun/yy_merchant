package com.yiyue.store.module.im.daoutil;

import android.content.Context;

import com.yiyue.store.component.greendao.DaoManager;
import com.yiyue.store.component.greendao.DaoUtils;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.local.greendao.ChatNoFriendBeanDao;
import com.yiyue.store.model.vo.bean.daobean.ChatNoFriendBean;
import com.yiyue.store.model.vo.bean.daobean.UserFriendsBean;

import org.greenrobot.greendao.async.AsyncOperation;
import org.greenrobot.greendao.async.AsyncOperationListener;
import org.greenrobot.greendao.async.AsyncSession;
import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangzz on 2018/10/17
 * 自定DaoUtil操作类
 */

public class ChatNoFriendDaoUtils extends DaoUtils<ChatNoFriendBean> {

    public ChatNoFriendDaoUtils(Context context) {
        super(context, Constants.DB_NAME_NOFRIEND + AccountManager.getInstance().getUserId());
    }

    /**
     * 单个插入
     *
     * @param user
     */
    public void insertUser(ChatNoFriendBean user) {
        insertSingle(user);
    }

    /**
     * 批量插入
     */
    public void insertBatchUser(List<ChatNoFriendBean> list) {
        insertBatch(ChatNoFriendBean.class, list);
    }

    /**
     * 条件查询
     */
    public void queryWhereUser(String imusername) {
        query(ChatNoFriendBean.class, ChatNoFriendBeanDao.Properties.Imusername.eq(imusername), imusername);
    }

    /**
     * 同步查询方法，异步自己在页面处理
     *
     * @param imusername
     * @return
     */
    public ChatNoFriendBean querySynch(String imusername) {
        return querySynch(ChatNoFriendBean.class, ChatNoFriendBeanDao.Properties.Imusername.eq(imusername));
    }

    /**
     * 查询全部
     */
    public void queryAllUser() {
        queryAll(ChatNoFriendBean.class, null);
//        return users;
    }

    /**
     * 条件删除
     */
    public void deleteSingleUser(String imusername) {
        ChatNoFriendBean user = new ChatNoFriendBean();
        user.setImusername(imusername);
        deleteSingle(user);
    }

    /**
     * 批量删除
     */
    public void deleteBatchUser(List<String> stringList) {
        List<ChatNoFriendBean> userList = new ArrayList<>();
        for (int i = 0; i < stringList.size(); i++) {
            ChatNoFriendBean user = new ChatNoFriendBean();
            user.setImusername(stringList.get(i));
            userList.add(user);
        }
        deleteBatch(ChatNoFriendBean.class, userList);
    }

    /**
     * 单个更新
     */
    public void updateSingleUser(ChatNoFriendBean user) {
        updateSingle(ChatNoFriendBean.class, user);
    }

    /**
     * 批量更新
     */
    public void updateBatchUser(List<ChatNoFriendBean> list) {
        updateBatch(ChatNoFriendBean.class, list);
    }

    /**
     * 根据Id 单个删除
     */
    public void deleteByIdSingleUser(long memberId) {
        deleteByIdSingle(ChatNoFriendBean.class, memberId);
    }

    /**
     * 根据Id批量删除
     */
    public void deleteByIdBatchUser(List<Long> longList) {
        deleteByIdBatch(ChatNoFriendBean.class, longList);
    }

    /**
     * 删除所有
     */
    public void deleteAll() {
        deleteAll(ChatNoFriendBean.class);
    }

    /**
     * 条件批量删除
     */
    public void deleteWhereBatch(String ageString) {
        Query<ChatNoFriendBean> build = daoSession.queryBuilder(ChatNoFriendBean.class).where(ChatNoFriendBeanDao.Properties._id.eq(ageString)).build();
        AsyncSession asyncSession = daoSession.startAsyncSession();
        asyncSession.setListenerMainThread(new AsyncOperationListener() {
            @Override
            public void onAsyncOperationCompleted(AsyncOperation operation) {
                if (operation.isCompletedSucessfully()) {
                    deleteBatch(UserFriendsBean.class, (List<ChatNoFriendBean>) operation.getResult());
                }
            }
        });
        asyncSession.queryList(build);
    }
}
