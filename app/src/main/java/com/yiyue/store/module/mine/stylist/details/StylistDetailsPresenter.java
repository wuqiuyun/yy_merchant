package com.yiyue.store.module.mine.stylist.details;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.yiyue.store.api.RecomUserApi;
import com.yiyue.store.api.StoreApi;
import com.yiyue.store.api.StoreStylistApi;
import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.model.vo.bean.StoreCollectionBean;
import com.yiyue.store.model.vo.bean.StylistCardBean;
import com.yiyue.store.model.vo.result.CheckMsgResult;
import com.yiyue.store.model.vo.result.CheckStoreResult;
import com.yiyue.store.model.vo.result.FindReCodeResult;
import com.yiyue.store.model.vo.result.StoreCollectionResult;
import com.yiyue.store.model.vo.result.StylistCardResult;
import com.yiyue.store.module.mine.stylist.details.definedmsgdetail.StylistDetailsFromMsgActivity;
import com.yl.core.component.net.exception.ApiException;

/**
 * Created by zm on 2018/10/11.
 */
public class StylistDetailsPresenter extends BasePresenter<IStylistDetailsView> {

    /**
     * 获取我的推荐码
     */
    public void findReCode(){
        new RecomUserApi().findReCode(new YLRxSubscriberHelper<FindReCodeResult>() {
            @Override
            public void _onNext(FindReCodeResult findReCodeResult) {
                if (null != findReCodeResult.getData()) getMvpView().findReCodeSuc(findReCodeResult.getData());
                else getMvpView().showToast("获取我的推荐码失败");
            }

            @Override
            public void _onError(ApiException error) {
                super._onError(error);
            }
        });
    }
    
    /**
     * 获取美发师名片 stylistId必传，其他不要传
     *
     * @param nexus
     * @param storeId   门店id
     * @param stylistId 美发师id
     */
    public void getStylistCardList(String nexus, String storeId, String stylistId) {

        new StoreStylistApi().getStylistCardList(nexus, storeId, stylistId, new YLRxSubscriberHelper<StylistCardResult>() {
            @Override
            public void _onNext(StylistCardResult baseResponse) {
                StylistCardBean stylistCardBean = baseResponse.getData();
                // callback
                getMvpView().getStylistCardListSucceed(stylistCardBean);
            }

            @Override
            public void _onError(ApiException error) {
                getMvpView().getStylistCardListFail();
            }
        });
    }


    /**
     * 收藏及取消美发师收藏 stylistId必传，其他不要传
     *
     * @param stylistId 美发师I
     * @param type      收藏类型1收藏0取消 ,
     */
    public void getStoreCollection(int type, String stylistId, Context context) {

        new StoreApi().getStoreCollection(stylistId, type, new YLRxSubscriberHelper<StoreCollectionResult>(context,true) {
            @Override
            public void _onNext(StoreCollectionResult baseResponse) {
                StoreCollectionBean storeCollectionBean = baseResponse.getData();
                // callback
                getMvpView().setStoreCollectionSucceed(storeCollectionBean);
            }
        });
    }

    /**
     * 门店与美发师解约
     *
     * @param nexus     签约0,入驻1
     * @param storeId   门店ID
     * @param stylistId (integer, optional): 美发师ID
     */
    public void breakStoreNexus(int nexus, String storeId, String stylistId) {

        new StoreApi().breakStoreNexus(nexus, storeId, stylistId, new YLRxSubscriberHelper<BaseResponse>() {
            @Override
            public void _onNext(BaseResponse baseResponse) {
                // callback
                getMvpView().breakStoreNexusSucceed();
            }

            @Override
            public void _onError(ApiException error) {
                super._onError(error);
                getMvpView().breakStoreNexusFail();
            }
        });
    }

    @Override
    public void onDetachMvpView() {
        super.onDetachMvpView();
    }


    /**
     * 门店与美发师解约
     *  @param nexus     签约0,入驻1
     * @param storeId   门店ID
     * @param stylistId (integer, optional): 美发师ID
     */
    public void nexus(String nexus, String storeId, String stylistId, boolean isAgree, String msgId, Activity activity) {
        if (TextUtils.isEmpty(nexus)) {
            nexus = "0";
        }
        if (TextUtils.isEmpty(storeId)) {
            ToastUtils.shortToast("门店ID不能为空");
            return;
        }
        if (TextUtils.isEmpty(stylistId)) {
            ToastUtils.shortToast("美发师ID不能为空");
            return;
        }

        if (isAgree) {
            new StoreStylistApi().nexus(nexus, storeId, stylistId, msgId, new YLRxSubscriberHelper<BaseResponse>(activity, true) {
                @Override
                public void _onNext(BaseResponse baseResponse) {
                    // callback
                    getMvpView().nexusSuccess(true);
                }

                @Override
                public void _onError(ApiException error) {
                    super._onError(error);
                }
            });
        } else {
            new StoreStylistApi().refuse(nexus, storeId, stylistId, msgId, new YLRxSubscriberHelper<BaseResponse>(activity, true) {
                @Override
                public void _onNext(BaseResponse baseResponse) {
                    // callback
                    getMvpView().nexusSuccess(false);
                }

                @Override
                public void _onError(ApiException error) {
                    super._onError(error);
                }
            });
        }
    }

    public void checkStoreAuth(String storeId) {
        if (TextUtils.isEmpty(storeId)) {
            ToastUtils.shortToast("门店ID不能为空");
            return;
        }

        new StoreStylistApi().checkStoreAuth(storeId, new YLRxSubscriberHelper<CheckStoreResult>() {
            @Override
            public void _onNext(CheckStoreResult baseResponse) {
                // callback
                if (baseResponse != null && baseResponse.getData() != null) {
                    if ("success".equals(baseResponse.getData().getResult())) {
                        getMvpView().checkStoreAuthSuccess();
                    } else {
                        ToastUtils.shortToast("门店未通过认证,不能邀请美发师");
                    }
                } else {
                    ToastUtils.shortToast(baseResponse.getMessage());
                }
            }

            @Override
            public void _onError(ApiException error) {
                super._onError(error);
                ToastUtils.shortToast(error.message);
                getMvpView().checkStoreAuthFail();
            }
        });
    }

    /**
     * 消息处理状态接口
     * @param msgId
     * @param context
     */
    public void checkMsg(String msgId, Context context) {
        new StoreApi().checkMsg(msgId, new YLRxSubscriberHelper<CheckMsgResult>(context, true) {
            @Override
            public void _onNext(CheckMsgResult checkMsgResult) {
                // callback
                getMvpView().checkMsg(checkMsgResult);
            }

            @Override
            public void _onError(ApiException error) {
                super._onError(error);
            }
        });
    }
}
