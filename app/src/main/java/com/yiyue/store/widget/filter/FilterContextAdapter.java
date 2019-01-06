package com.yiyue.store.widget.filter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yiyue.store.R;
import com.yiyue.store.base.adapter.BaseRecycleViewAdapter;
import com.yiyue.store.databinding.ItemFilterContextBinding;
import com.yiyue.store.databinding.ItemFilterHeadBinding;
import com.yiyue.store.model.vo.bean.ServiceSettingBean;
import com.yl.core.component.log.DLog;

import java.util.ArrayList;
import java.util.HashMap;


/*
 *  @创建者:   27407
 *  @创建时间:  2018/10/18 20:03
 *  @描述：    筛选内容的适配器
 */

public class FilterContextAdapter extends BaseRecycleViewAdapter<Object> {

    public static final int HEAD = 0;     //标题
    public static final int CHILD = 1;    //内容
    private Context mContext;
    private ArrayList<ArrayList<ServiceSettingBean>> items=new ArrayList<>();
    private HashMap<Integer, Integer>  selects = new HashMap<>();

    public FilterContextAdapter(Context context) {
            mContext = context;
    }

    public HashMap<Integer, Integer> getSelects() {
        return selects;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(mContext);   //获取mInflater对象
        switch (viewType){  //根据viewtyupe来区分，是标题还是数据项
            case HEAD:  //标题
                HeadViewHolder headViewHolder = new HeadViewHolder(mInflater.inflate(R.layout.item_filter_head, parent, false));
                return headViewHolder;
            case CHILD:
                ChildViewHolder childViewHolder = new ChildViewHolder(mInflater.inflate(R.layout.item_filter_context, parent, false));
                return childViewHolder;
        }
        return null;
    }

    /**
     * 重置
     */
    public void reset() {
        items.clear();
        selects.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        items.add(new ArrayList<>());
        switch (getItemViewType(position)){  //根据viewtyupe来区分，是标题还是数据项
            case HEAD:  //标题
                HeadViewHolder headViewHolder= (FilterContextAdapter.HeadViewHolder) holder;
                headViewHolder.mBinding.cbLabel.setText((String)mDatas.get(position));
                break;
            case CHILD:
                ChildViewHolder childViewHolder= (FilterContextAdapter.ChildViewHolder) holder;
                FilterSubAdapter filterSubAdapter = new FilterSubAdapter(mContext);
                filterSubAdapter.setItemListener(new BaseRecycleViewAdapter.SimpleRecycleViewItemListener() {
                    @Override
                    public void onItemClick(View view, int position2) {
                        ArrayList<ServiceSettingBean> serviceSettingBeans = items.get(position);
                        for (int i = 0; i < serviceSettingBeans.size(); i++) {
                            serviceSettingBeans.get(i).setChecked(false);
                            if (i==position2){
                                serviceSettingBeans.get(i).setChecked(true);
                            }
                        }
                        filterSubAdapter.setDatas(serviceSettingBeans,true);
                        selects.put(position,position2);
                    }
                });
                childViewHolder.mBinding.rvType.setLayoutManager(new GridLayoutManager(mContext, 3));
                childViewHolder.mBinding.rvType.setAdapter(filterSubAdapter);
                if (items.get(position).size()==0){
                 ArrayList<ServiceSettingBean> tempList = new ArrayList<>();
                    ArrayList<String> subItem = (ArrayList<String>) mDatas.get(position);
                    for (int i = 0; i < subItem.size(); i++) {
                        tempList.add(new ServiceSettingBean( subItem.get(i),false));
                    }
                    items.set(position,tempList);
                }
                filterSubAdapter.setDatas(items.get(position),true);
                break;
        }
    }
    @Override
    public int getItemViewType(int position) {
        return position%2==0?0:1;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class HeadViewHolder extends BaseViewHolder {

        private ItemFilterHeadBinding mBinding;

        public HeadViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }

    public class ChildViewHolder extends BaseViewHolder {

        private ItemFilterContextBinding mBinding;

        public ChildViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }

}
