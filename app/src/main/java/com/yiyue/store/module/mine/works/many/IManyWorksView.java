package com.yiyue.store.module.mine.works.many;

import com.yiyue.store.base.mvp.IBaseView;
import com.yiyue.store.model.vo.bean.OpusListBean;

/**
 * Created by zm on 2018/10/13.
 */
public interface IManyWorksView extends IBaseView {

    void onOpusList(OpusListBean bean);

    void getOpusListScreenSuc(OpusListBean opusList);
}
