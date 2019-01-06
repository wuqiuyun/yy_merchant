package com.yiyue.store.module.mine.stylist.details;

import com.yiyue.store.base.mvp.IBaseView;
import com.yiyue.store.model.vo.bean.ReCodeBean;
import com.yiyue.store.model.vo.bean.StoreCollectionBean;
import com.yiyue.store.model.vo.bean.StylistCardBean;
import com.yiyue.store.model.vo.result.CheckMsgResult;

/**
 * Created by zm on 2018/10/11.
 */
public interface IStylistDetailsView extends IBaseView {
    /**
     * 获取美发师card成功
     */
    void getStylistCardListSucceed(StylistCardBean stylistCardBean);

    /**
     * 获取美发师名片gg
     */
    void getStylistCardListFail();

    /**
     * 收藏/取消收藏美发师成功
     */
    void setStoreCollectionSucceed(StoreCollectionBean storeCollectionSucceed);

    /**
     * 解约成功
     * */
    void breakStoreNexusSucceed();

    /**
     * 解约失败
     * */
    void breakStoreNexusFail();

    /**
     * 签约或者入驻成功
     */
    void nexusSuccess(boolean isAgree);

    void checkStoreAuthSuccess();

    void checkStoreAuthFail();

    void findReCodeSuc(ReCodeBean reCode);

    void checkMsg(CheckMsgResult checkMsgResult);
}
