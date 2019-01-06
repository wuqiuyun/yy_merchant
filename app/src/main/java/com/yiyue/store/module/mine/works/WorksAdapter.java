package com.yiyue.store.module.mine.works;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yiyue.store.R;
import com.yiyue.store.base.adapter.BaseRecycleViewAdapter;
import com.yiyue.store.databinding.ItemWorksBinding;
import com.yiyue.store.model.vo.bean.OpusBean;
import com.yl.core.component.image.ImageLoader;

/**
 * Created by zm on 2018/10/10.
 */
public class WorksAdapter extends BaseRecycleViewAdapter<OpusBean>{
    private Context context;

    public WorksAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WorksViewHolder(LayoutInflater.from(context).inflate(R.layout.item_works, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        WorksViewHolder viewHolder = (WorksViewHolder) holder;

        OpusBean bean = getDatas().get(position);

        if (!TextUtils.isEmpty(bean.getOpusPath())){
            ImageLoader.loadImage(viewHolder.mBinding.ivWorks , bean.getOpusPath());
        }

    }

    public class WorksViewHolder extends BaseViewHolder {

        private ItemWorksBinding mBinding;

        public WorksViewHolder(View itemView) {
            super(itemView);

            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
