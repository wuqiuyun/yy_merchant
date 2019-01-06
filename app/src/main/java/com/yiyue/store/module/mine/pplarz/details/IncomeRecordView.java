package com.yiyue.store.module.mine.pplarz.details;


import com.yiyue.store.base.mvp.IBaseView;
import com.yiyue.store.model.vo.bean.IncomeRecordBean;

import java.util.ArrayList;

/**
 * Created by zm on 2019/1/4.
 */
public interface IncomeRecordView extends IBaseView {
    void setData(ArrayList<IncomeRecordBean> datas);
}
