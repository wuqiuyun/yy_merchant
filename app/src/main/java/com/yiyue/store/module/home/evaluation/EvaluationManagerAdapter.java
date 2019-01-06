package com.yiyue.store.module.home.evaluation;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yiyue.store.R;
import com.yiyue.store.base.adapter.BaseRecycleViewAdapter;
import com.yiyue.store.component.recycleview.GridSpacingItemDecoration;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ItemEvaluationManagerBinding;
import com.yiyue.store.model.vo.bean.StoreCommentListBean;
import com.yiyue.store.widget.PhotoView.PhotoViewActivity;
import com.yl.core.component.image.ImageLoader;
import com.yl.core.component.image.ImageLoaderConfig;

/**
 * 评价管理适配器
 * Created by lvlong on 2018/10/11.
 */
public class EvaluationManagerAdapter extends BaseRecycleViewAdapter<StoreCommentListBean> {

    private Context mContext;
    private int mType;

    public EvaluationManagerAdapter(Context context, int type) {
        mContext = context;
        mType = type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EvaluationManagerAdapter.EvaluationManagerViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_evaluation_manager, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        EvaluationManagerAdapter.EvaluationManagerViewHolder viewHolder = (EvaluationManagerAdapter.EvaluationManagerViewHolder) holder;
        StoreCommentListBean bean = mDatas.get(position);
        viewHolder.mBinding.materialRatingBar.setRating(bean.getLevel());
        viewHolder.mBinding.materialRatingBar.setEnabled(false);
        String times = bean.getCreatetime();
        viewHolder.mBinding.tvTime.setText(times.substring(5, times.length()));
        viewHolder.mBinding.tvName.setText(bean.getNickname());
        viewHolder.mBinding.tvHairdressingType.setText("[" + bean.getServiceName() + "]");
        if (null != bean.getComment() && !TextUtils.isEmpty(bean.getComment().trim())) {
            viewHolder.mBinding.tvUserReply.setText(bean.getComment());
        } else {
            viewHolder.mBinding.tvUserReply.setText("这个家伙很懒，什么也没有留下~");
        }

        if (mType == 1) {
            String tvReply = bean.getReply();
            if (tvReply != null && !TextUtils.isEmpty(tvReply)) {
                viewHolder.mBinding.etReply.setText(tvReply);
                viewHolder.mBinding.etReply.setEnabled(false);
                viewHolder.mBinding.tvSend.setVisibility(View.GONE);

            } else {
                viewHolder.mBinding.tvSend.setVisibility(View.VISIBLE);
                viewHolder.mBinding.etReply.setText("");
                viewHolder.mBinding.etReply.setEnabled(true);
            }
            viewHolder.mBinding.tvSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String etStr = viewHolder.mBinding.etReply.getText().toString().trim();
                    if (TextUtils.isEmpty(etStr) || etStr == null) {
                        ToastUtils.shortToast("请输入评论!");
                    } else {
                        onSendViewClick.sendMessage(etStr, position);
                    }
                }
            });
        } else {
            viewHolder.mBinding.etReply.setEnabled(false);
            viewHolder.mBinding.llReply.setVisibility(View.GONE);
        }

        //设置头像
        ImageLoaderConfig config = new ImageLoaderConfig.Builder()
                .setAsBitmap(true)
                .setCropType(ImageLoaderConfig.CENTER_INSIDE)
                .setPlaceHolderResId(R.drawable.icon_head_pic_def)
                .setErrorResId(R.drawable.icon_head_pic_def)
                .setDiskCacheStrategy(ImageLoaderConfig.DiskCache.SOURCE)
                .setPrioriy(ImageLoaderConfig.LoadPriority.NORMAL)
                .build();
        ImageLoader.loadImage(viewHolder.mBinding.civHeadPhoto, bean.getHeadImg(), config, null);

        //评论recycleview
        RecyclerView replyRecycle = viewHolder.mBinding.rvReply;

        //图片recycleview
        RecyclerView photoRecycle = viewHolder.mBinding.photoRecycle;
        photoRecycle.setHasFixedSize(true);
        PhotoAdapter photoAdapter = new PhotoAdapter(mContext);
        photoRecycle.setLayoutManager(new GridLayoutManager(mContext, 3));
        if (photoRecycle.getItemDecorationCount() <= 0) {
            photoRecycle.addItemDecoration(new GridSpacingItemDecoration(3, 15, false));
        }

        if (bean.getImgPaths() != null) {
            photoRecycle.setAdapter(photoAdapter);
            photoAdapter.setDatas(bean.getImgPaths(), true);
            photoAdapter.notifyDataSetChanged();
        }
        photoAdapter.setItemListener(new SimpleRecycleViewItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putStringArrayList(PhotoViewActivity.IMAGE_URL, photoAdapter.getDatas());
                bundle.putInt(PhotoViewActivity.SHOW_POSITION, position);
                PhotoViewActivity.startActivity(mContext, PhotoViewActivity.class, bundle);
            }
        });
    }

    public class EvaluationManagerViewHolder extends BaseViewHolder {
        private ItemEvaluationManagerBinding mBinding;

        public EvaluationManagerViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }


    private OnsendViewClick onSendViewClick;

    public void setOnSendViewClick(OnsendViewClick onSendViewClick) {
        this.onSendViewClick = onSendViewClick;
    }

    interface OnsendViewClick {

        void sendMessage(String mssage, int position);
    }


}
