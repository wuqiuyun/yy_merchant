package com.yiyue.store.component.greendao;

import java.util.List;

/**
 * Created by zhangzz on 2018/10/17
 * 数据库操作完成回调接口
 */

public interface DaoCallBackInterface {

    /**
     * type true 成功。 false 失败
     * @param <T>
     */

    interface OnIsertInterface<T>{//单个插入成功
        void onIsertInterface(boolean type);
    }

    interface OnQuerySingleInterface<T>{//单个查询成功
        void onQuerySingleInterface(T entry);
    }

    interface OnQuerySingleBackInterface<T>{//单个查询成功 带传参返回
        void onQuerySingleBackInterface(T entry, String id);
    }

    interface OnDeleteInterface<T>{//单个删除成功
        void onDeleteInterface(boolean type);
    }

    interface OnUpdateInterface<T>{//单个修改成功
        void onUpdateInterface(boolean type);
    }

    interface OnQueryAllInterface<T>{//批量查询数据回调
        void onQueryAllBatchInterface(List<T> list);
        void onQueryAllBatchFailInterface();
    }

}
