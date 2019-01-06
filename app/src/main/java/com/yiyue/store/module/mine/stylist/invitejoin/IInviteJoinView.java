package com.yiyue.store.module.mine.stylist.invitejoin;

import com.yiyue.store.base.mvp.IBaseView;
import com.yiyue.store.model.vo.bean.StoreCollectionBean;
import com.yiyue.store.model.vo.bean.StylistCardBean;
import com.yiyue.store.model.vo.result.CheckMsgResult;
import com.yiyue.store.model.vo.result.SendMsgResult;
import com.yiyue.store.model.vo.result.StoreInfoResult;

/**
 * Created by zm on 2018/10/11.
 */
public interface IInviteJoinView extends IBaseView {

    void getStoreInforamtion(StoreInfoResult storeInfoResult);
    void sendMsg(SendMsgResult sendMsgResult, int type);
}
