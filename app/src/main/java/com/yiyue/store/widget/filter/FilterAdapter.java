package com.yiyue.store.widget.filter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yiyue.store.R;
import com.yiyue.store.base.adapter.BaseRecycleViewAdapter;
import com.yiyue.store.databinding.ItemFilterFilterBinding;
import com.yiyue.store.model.vo.bean.ServiceSettingBean;
import com.yl.core.component.log.DLog;

import java.util.ArrayList;
import java.util.Map;


/*
 *  @创建者:   27407
 *  @创建时间:  2018/10/18 16:14
 *  @描述：    筛选的适配器
 */

public class FilterAdapter extends BaseRecycleViewAdapter<Object> {
    private Context mContext;
    private FilterContextAdapter mFilterContextAdapter;
    private IOkButtonListener mIOkButtonListener;

    public interface IOkButtonListener {
        void onOkButtonClick( Map<Integer, Integer> screenings );
    }

    public void setIOkButtonListener(IOkButtonListener IOkButtonListener) {
        mIOkButtonListener = IOkButtonListener;
    }

    public FilterAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public FilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_filter_filter, parent, false);
        return new FilterAdapter.FilterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        FilterViewHolder viewHolder = (FilterViewHolder) holder;
        RecyclerView recyclerView = viewHolder.mBinding.contextRecycle;
        if (mFilterContextAdapter==null){
            mFilterContextAdapter = new FilterContextAdapter(mContext);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(mFilterContextAdapter);
        mFilterContextAdapter.setDatas(mDatas,true);


        //确定按钮事件监听
        if (null != mIOkButtonListener) {
            viewHolder.mBinding.tvOk.setOnClickListener(v -> {
                mIOkButtonListener.onOkButtonClick( mFilterContextAdapter.getSelects());
            });
        }
        //重置
        viewHolder.mBinding.tvReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFilterContextAdapter.reset();
            }
        });

    }

    @Override
    public int getItemCount() {
        return 1;
    }



    public class FilterViewHolder extends BaseViewHolder {
        private ItemFilterFilterBinding mBinding;

        public FilterViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }

}
