package com.yiyue.store.module.mine.store;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yiyue.store.util.FormatKmUtil;
import com.yl.core.component.image.ImageLoader;
import com.yiyue.store.R;
import com.yiyue.store.base.adapter.BaseRecycleViewAdapter;
import com.yiyue.store.model.vo.bean.StoreBean;
import com.yiyue.store.databinding.ItemStoreBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zm on 2018/10/10.
 */
public class StoreAdapter extends BaseRecycleViewAdapter<StoreBean>{
    private Context context;

    public StoreAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StoreViewHolder(LayoutInflater.from(context).inflate(R.layout.item_store, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        StoreViewHolder holder = (StoreViewHolder) viewHolder;
        StoreBean item = getDatas().get(position);
        ArrayList<String> list = item.getServes();
        String serves = getServes(list);
        ImageLoader.loadImage(holder.storeBinding.ivStore, item.getStoreCover());
        holder.storeBinding.tvName.setText(item.getStoreName());
        holder.storeBinding.tvLocationDistance.setText(FormatKmUtil.FormatKmStr(item.getDistance()));
        holder.storeBinding.tvAddress.setText(item.getLocation());
        holder.storeBinding.tvServiceType.setText(serves);
    }

    public class StoreViewHolder extends BaseViewHolder {
        private ItemStoreBinding storeBinding;
        
        public StoreViewHolder(View itemView) {
            super(itemView);
            storeBinding = DataBindingUtil.bind(itemView);
        }
    }
    
    private String getServes(List<String> list) {
        StringBuffer sb = new StringBuffer();
        if (null == list || list.size() == 0) {
            return "暂无";
        } else {
            for (String serve: list ) {
                sb.append(serve).append("、");
            }
            return sb.toString().substring(0,sb.length()-1);
        }
    }
}
