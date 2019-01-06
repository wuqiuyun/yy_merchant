package com.yiyue.store.component.greendao;

import android.content.Context;

import com.yiyue.store.model.local.greendao.DaoSession;

import org.greenrobot.greendao.async.AsyncOperation;
import org.greenrobot.greendao.async.AsyncOperationListener;
import org.greenrobot.greendao.async.AsyncSession;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;

/**
 * Created by zhangzz on 2018/10/17
 * 数据库操作方法基类
 */

public class DaoUtils<T> extends DaoInterface {
    public DaoManager daoManager;
    public DaoSession daoSession;

    public DaoUtils(Context context, String dao_name) {
        daoManager = new DaoManager();
        daoManager.getDaoManager(context, dao_name);
        daoSession = daoManager.getDaoSession();
    }

    /**
     * 根据环信id进行条件查询数据，返回时携带环信id
     *
     * @param cls
     * @return
     */
    public <T> void query(Class cls, WhereCondition whereCondition, String imusername) {
        AsyncSession asyncSession = daoSession.startAsyncSession();
        asyncSession.setListenerMainThread(new AsyncOperationListener() {
            @Override
            public void onAsyncOperationCompleted(AsyncOperation operation) {
                if (operation.isCompletedSucessfully() && onQuerySingleBackInterface != null) {
                    onQuerySingleBackInterface.onQuerySingleBackInterface(operation.getResult(), imusername);
                } else if (onQuerySingleBackInterface != null) {
                    onQuerySingleBackInterface.onQuerySingleBackInterface(null, imusername);
                }
            }
        });
        Query query = daoSession.queryBuilder(cls).where(whereCondition).build();
        asyncSession.queryUnique(query);
    }

    /**
     * 同步查询返回, 线程在主页面自己处理
     *
     * @param cls
     * @param whereCondition
     */
    public T querySynch(Class cls, WhereCondition whereCondition) {
        T query = (T) daoSession.queryBuilder(cls).where(whereCondition).build().unique();
        return query;
    }

    /**
     * 根据环信id进行条件查询数据，返回时携带环信id
     *
     * @param cls
     * @return
     */
    public <T> void query(Class cls, WhereCondition whereCondition) {
        AsyncSession asyncSession = daoSession.startAsyncSession();
        asyncSession.setListenerMainThread(new AsyncOperationListener() {
            @Override
            public void onAsyncOperationCompleted(AsyncOperation operation) {
                if (operation.isCompletedSucessfully() && onQuerySingleInterface != null) {
                    onQuerySingleInterface.onQuerySingleInterface(operation.getResult());
                } else if (onQuerySingleInterface != null) {
                    onQuerySingleInterface.onQuerySingleInterface(null);
                }
            }
        });
        Query query = daoSession.queryBuilder(cls).where(whereCondition).build();
        asyncSession.queryUnique(query);
    }


    public T query(Class cls, String whereString, String[] params) {
        return (T) daoSession.getDao(cls).queryRaw(whereString, params);
    }

    /**
     * 批量查询
     *
     * @param object
     * @return
     */
    public void queryAll(Class object, Query<T> query) {
        AsyncSession asyncSession = daoSession.startAsyncSession();
        asyncSession.setListenerMainThread(new AsyncOperationListener() {
            @Override
            public void onAsyncOperationCompleted(AsyncOperation operation) {
                if (operation != null) {
                    if (operation.isCompletedSucessfully() && onQueryAllInterface != null) {
                        List<T> result = (List<T>) operation.getResult();
                        onQueryAllInterface.onQueryAllBatchInterface(result);
                    } else if (onQueryAllInterface != null) {
                        onQueryAllInterface.onQueryAllBatchFailInterface();
                    }
                }
            }
        });
        if (query == null) {
            asyncSession.loadAll(object);
        } else {
            asyncSession.queryList(query);
        }

//        asyncSession.queryList(null);
    }

    /**
     * 删除
     */
    public void deleteSingle(T entry) {
        AsyncSession asyncSession = daoSession.startAsyncSession();
        asyncSession.setListenerMainThread(new AsyncOperationListener() {
            @Override
            public void onAsyncOperationCompleted(AsyncOperation operation) {
                if (operation.isCompletedSucessfully() && onDeleteInterface != null) {
                    onDeleteInterface.onDeleteInterface(true);
                } else if (onDeleteInterface != null) {
                    onDeleteInterface.onDeleteInterface(false);
                }
            }
        });
        asyncSession.delete(entry);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(Class cls, final List<T> list) {
        AsyncSession asyncSession = daoSession.startAsyncSession();
        asyncSession.setListenerMainThread(new AsyncOperationListener() {
            @Override
            public void onAsyncOperationCompleted(AsyncOperation operation) {
                if (operation.isCompletedSucessfully() && onDeleteInterface != null) {
                    onDeleteInterface.onDeleteInterface(true);
                } else if (onDeleteInterface != null) {
                    onDeleteInterface.onDeleteInterface(false);
                }
            }
        });
        asyncSession.deleteInTx(cls, list);
    }

    /**
     * 根据Id单个删除
     */
    public void deleteByIdSingle(Class cls, long longParams) {//此处longParams数值类型必须为主键id的类型
        daoSession.getDao(cls).deleteByKey(longParams);
    }

    /**
     * 根据Id批量删除
     */
    public void deleteByIdBatch(Class cls, List<Long> longList) {//同上
        daoSession.getDao(cls).deleteByKeyInTx(longList);
    }

    /**
     * 删除所有数据
     */
    public void deleteAll(Class cls) {
        final AsyncSession asyncSession = daoSession.startAsyncSession();
        asyncSession.setListenerMainThread(new AsyncOperationListener() {
            @Override
            public void onAsyncOperationCompleted(AsyncOperation operation) {
                if (operation.isCompletedSucessfully() && onDeleteInterface != null) {
                    onDeleteInterface.onDeleteInterface(true);
                } else if (onDeleteInterface != null) {
                    onDeleteInterface.onDeleteInterface(false);
                }
            }
        });
        asyncSession.deleteAll(cls);
    }

    /**
     * 插入一条数据
     */
    public void insertSingle(final T entity) {
        AsyncSession asyncSession = daoSession.startAsyncSession();
        asyncSession.setListenerMainThread(new AsyncOperationListener() {
            @Override
            public void onAsyncOperationCompleted(AsyncOperation operation) {
                if (operation.isCompletedSucessfully() && onIsertInterface != null) {
                    onIsertInterface.onIsertInterface(true);
                } else if (onIsertInterface != null) {
                    onIsertInterface.onIsertInterface(false);
                }
            }
        });
        asyncSession.runInTx(new Runnable() {
            @Override
            public void run() {
                daoSession.insertOrReplace(entity);
            }
        });
    }

    /**
     * 批量插入
     */

    public <T> void insertBatch(final Class cls, final List<T> userList) {
        AsyncSession asyncSession = daoSession.startAsyncSession();
        asyncSession.setListenerMainThread(new AsyncOperationListener() {
            @Override
            public void onAsyncOperationCompleted(AsyncOperation operation) {
                if (operation.isCompletedSucessfully() && onIsertInterface != null) {
                    onIsertInterface.onIsertInterface(true);
                } else if (onIsertInterface != null) {
                    onIsertInterface.onIsertInterface(false);
                }
            }
        });
        asyncSession.runInTx(new Runnable() {
            @Override
            public void run() {
                for (T object : userList) {
                    daoSession.insertOrReplace(object);
                }
            }
        });
    }

    /**
     * 更新一个数据
     */
    public <T> void updateSingle(Class cls, T entry) {
        AsyncSession asyncSession = daoSession.startAsyncSession();
        asyncSession.setListenerMainThread(new AsyncOperationListener() {
            @Override
            public void onAsyncOperationCompleted(AsyncOperation operation) {
                if (operation.isCompletedSucessfully() && onUpdateInterface != null) {
                    onUpdateInterface.onUpdateInterface(true);
                } else if (onUpdateInterface != null) {
                    onUpdateInterface.onUpdateInterface(false);
                }
            }
        });
        AsyncOperation asyncOperation = asyncSession.update(entry);
    }

    /**
     * 批量更新数据
     */
    public <T> void updateBatch(final Class cls, final List<T> tList) {
        AsyncSession asyncSession = daoSession.startAsyncSession();
        asyncSession.setListenerMainThread(new AsyncOperationListener() {
            @Override
            public void onAsyncOperationCompleted(AsyncOperation operation) {
                if (operation.isCompletedSucessfully() && onUpdateInterface != null) {
                    onUpdateInterface.onUpdateInterface(true);
                } else if (onUpdateInterface != null) {
                    onUpdateInterface.onUpdateInterface(false);
                }
            }
        });
        asyncSession.updateInTx(cls, tList);
    }

    /**
     * 删除对应的数据库
     */
    public void dropTable(Context context, String DB_NAME) {
        daoManager.dropTable(context, DB_NAME);
    }


    /**
     * 关闭当前dao所有的操作
     */
    public void closeConnection() {
        daoManager.closeConnection();
    }

    /**
     * 清除当前daoSession的缓存
     */
    public void clearDaoSession(){
        daoSession.clear();
    }
}
