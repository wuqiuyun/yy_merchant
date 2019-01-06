package com.yiyue.store.module.mine.pplarz.details;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yiyue.store.R;
import com.yiyue.store.base.adapter.BaseRecycleViewAdapter;
import com.yiyue.store.databinding.AdapterIncomeRoleBinding;
import com.yiyue.store.model.vo.bean.UserIncomeInfoBean;
import com.yiyue.store.util.FormatUtil;
import com.yl.core.util.DateUtil;

/**
 * Created by zm on 2019/1/4.
 */
public class IncomeRoleAdapter extends BaseRecycleViewAdapter<UserIncomeInfoBean> {
    private Context context;

    public IncomeRoleAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new IncomeRoleViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_income_role, null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        IncomeRoleViewHolder holder = (IncomeRoleViewHolder) viewHolder;
        UserIncomeInfoBean bean = mDatas.get(i);
        holder.binding.tvName.setText(FormatUtil.Formatstring(bean.getName()));
        holder.binding.tvIncome.setText(String.valueOf(bean.getIncome()));
        holder.binding.tvContact.setText("联系方式:" + bean.getMobile());
        holder.binding.tvTime.setText("邀请时间:" + DateUtil.date2Str(bean.getInviteTime(),DateUtil.FORMAT_MDHM ));
    }

    private class IncomeRoleViewHolder extends BaseViewHolder {
        AdapterIncomeRoleBinding binding;

        public IncomeRoleViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
