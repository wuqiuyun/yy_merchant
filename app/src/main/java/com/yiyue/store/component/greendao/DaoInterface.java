package com.yiyue.store.component.greendao;

/**
 * Created by zhangzz on 2018/10/17
 * 设置回调基类
 */

public class DaoInterface<T>  {

    protected DaoCallBackInterface.OnQueryAllInterface<T> onQueryAllInterface;
    protected DaoCallBackInterface.OnUpdateInterface<T> onUpdateInterface;
    protected DaoCallBackInterface.OnIsertInterface<T> onIsertInterface;
    protected DaoCallBackInterface.OnQuerySingleInterface<T> onQuerySingleInterface;
    protected DaoCallBackInterface.OnQuerySingleBackInterface<T> onQuerySingleBackInterface;
    protected DaoCallBackInterface.OnDeleteInterface<T> onDeleteInterface;

    public DaoCallBackInterface.OnQueryAllInterface<T> getOnQueryAllInterface() {
        return onQueryAllInterface;
    }

    public void setOnQueryAllInterface(DaoCallBackInterface.OnQueryAllInterface<T> onQueryAllInterface) {
        this.onQueryAllInterface = onQueryAllInterface;
    }

    public DaoCallBackInterface.OnUpdateInterface<T> getOnUpdateInterface() {
        return onUpdateInterface;
    }

    public void setOnUpdateInterface(DaoCallBackInterface.OnUpdateInterface<T> onUpdateInterface) {
        this.onUpdateInterface = onUpdateInterface;
    }

    public DaoCallBackInterface.OnIsertInterface<T> getOnIsertInterface() {
        return onIsertInterface;
    }

    public void setOnIsertInterface(DaoCallBackInterface.OnIsertInterface<T> onIsertInterface) {
        this.onIsertInterface = onIsertInterface;
    }

    public DaoCallBackInterface.OnQuerySingleInterface<T> getOnQuerySingleInterface() {
        return onQuerySingleInterface;
    }

    public void setOnQuerySingleInterface(DaoCallBackInterface.OnQuerySingleInterface<T> onQuerySingleInterface) {
        this.onQuerySingleInterface = onQuerySingleInterface;
    }

    public DaoCallBackInterface.OnQuerySingleBackInterface<T> getOnQuerySingleBackInterface() {
        return onQuerySingleBackInterface;
    }

    public void setOnQuerySingleBackInterface(DaoCallBackInterface.OnQuerySingleBackInterface<T> onQuerySingleBackInterface) {
        this.onQuerySingleBackInterface = onQuerySingleBackInterface;
    }

    public DaoCallBackInterface.OnDeleteInterface<T> getOnDeleteInterface() {
        return onDeleteInterface;
    }

    public void setOnDeleteInterface(DaoCallBackInterface.OnDeleteInterface<T> onDeleteInterface) {
        this.onDeleteInterface = onDeleteInterface;
    }
}
