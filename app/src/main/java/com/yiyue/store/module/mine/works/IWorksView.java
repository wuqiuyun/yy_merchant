package com.yiyue.store.module.mine.works;

import com.yiyue.store.base.mvp.IBaseView;
import com.yiyue.store.model.vo.bean.OpusBean;

import java.util.List;

/**
 * Created by zm on 2018/10/10.
 */
public interface IWorksView extends IBaseView {
    
    void getOpusListSuccess(List<OpusBean> list);
    
    void getOpusListFail();
}
