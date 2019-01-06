package com.yiyue.store.widget.filter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yiyue.store.R;
import com.yiyue.store.base.adapter.BaseRecycleViewAdapter;
import com.yiyue.store.databinding.ItemFilterSubBinding;
import com.yiyue.store.model.vo.bean.ServiceSettingBean;
import com.yiyue.store.util.FormatUtil;

/**
 * Created by lvlong on 2018/10/12.
 */
public class FilterSubAdapter extends BaseRecycleViewAdapter<ServiceSettingBean> {

    private Context mContext;

    public FilterSubAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public FilterSubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_filter_sub, parent, false);
        return new FilterSubViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        FilterSubViewHolder viewHolder = (FilterSubViewHolder) holder;
        ServiceSettingBean bean = mDatas.get(position);
        viewHolder.mBinding.cbLabel.setText(FormatUtil.Formatstring(bean.getLabel()));
        viewHolder.mBinding.cbLabel.setChecked(bean.isChecked());
    }

    public class FilterSubViewHolder extends BaseViewHolder {
        private ItemFilterSubBinding mBinding;

        public FilterSubViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
