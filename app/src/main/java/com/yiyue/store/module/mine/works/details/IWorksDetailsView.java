package com.yiyue.store.module.mine.works.details;

import com.yiyue.store.base.mvp.IBaseView;
import com.yiyue.store.model.vo.bean.OpusDetailBean;
import com.yiyue.store.model.vo.bean.ReCodeBean;

/**
 * Created by zm on 2018/10/12.
 */
public interface IWorksDetailsView extends IBaseView{
    void getDetailSuccess(OpusDetailBean detail);
    
    void getDetailFail();
    
    void collectSuccess();
    
    void collectFail();

    void findReCodeSuc(ReCodeBean reCode);
}
