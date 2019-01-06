package com.yiyue.store.model.vo.result;

import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.model.vo.bean.RoleIncomeBean;

import java.util.List;

/**
 * Created by zm on 2019/1/2.
 */
public class RecommendResult extends BaseResponse<RecommendResult.Data> {
    public static final class Data {
        List<RoleIncomeBean> incomeList;
        double incometotal;

        public List<RoleIncomeBean> getIncomeList() {
            return incomeList;
        }

        public void setIncomeList(List<RoleIncomeBean> incomeList) {
            this.incomeList = incomeList;
        }

        public double getIncometotal() {
            return incometotal;
        }

        public void setIncometotal(double incometotal) {
            this.incometotal = incometotal;
        }
    }
}
