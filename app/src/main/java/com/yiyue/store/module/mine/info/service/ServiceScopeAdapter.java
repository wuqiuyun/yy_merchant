package com.yiyue.store.module.mine.info.service;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yiyue.store.R;
import com.yiyue.store.base.adapter.BaseRecycleViewAdapter;
import com.yiyue.store.databinding.ItemServiceSettingBinding;
import com.yiyue.store.model.vo.bean.CategoryBean;
import com.yiyue.store.util.FormatUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zm on 2018/10/27.
 */
public class ServiceScopeAdapter extends BaseRecycleViewAdapter<CategoryBean> {

    private Context mContext;

    public ServiceScopeAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_service_setting, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ServiceViewHolder viewHolder = (ServiceViewHolder) holder;
        CategoryBean bean = mDatas.get(position);
        viewHolder.mBinding.cbLabel.setText(FormatUtil.Formatstring(bean.getName()));
        viewHolder.mBinding.cbLabel.setChecked(bean.isChecked());
    }

    public class ServiceViewHolder extends BaseViewHolder {
        private ItemServiceSettingBinding mBinding;

        public ServiceViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }

    /**
     * 获取选中的服务类目
     * @return
     */
    public ArrayList<CategoryBean> getSetleCategoryBean() {
        ArrayList<CategoryBean> selectategoryBeans = new ArrayList<>();
        for (CategoryBean categoryBean : mDatas) {
            if (categoryBean.isChecked()) {
                selectategoryBeans.add(categoryBean);
            }
        }
        return selectategoryBeans;
    }

    /**
     * 获取选中的服务类目id集合
     * @return
     */
    public List<Integer> getSetleCategoryIds() {
        List<Integer> selectategoryIds = new ArrayList<>();
        for (CategoryBean categoryBean : mDatas) {
            if (categoryBean.isChecked()) {
                selectategoryIds.add(categoryBean.getId());
            }
        }
        return selectategoryIds;
    }
}
