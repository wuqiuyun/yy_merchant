package com.yiyue.store.module.im.groupmembers;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yiyue.store.R;
import com.yiyue.store.model.vo.bean.daobean.GroupUserBean;

import java.util.List;

/**
 * Created by Lizhuo on 2018/10/18.
 */
public class GroupMemberAdapter extends BaseQuickAdapter<GroupUserBean, BaseViewHolder> {
    private List<GroupUserBean> mList;
    private boolean isShow;//是否需要显示移除按钮

    public GroupMemberAdapter(int layoutResId, boolean isSelect) {
        super(layoutResId, null);
        this.isShow = isSelect;
    }

    @Override
    public void setNewData(@Nullable List<GroupUserBean> data) {
        super.setNewData(data);
        this.mList = data;
    }

    @Override
    protected void convert(BaseViewHolder holder, final GroupUserBean bean) {
        if (bean != null) {
            int position = holder.getLayoutPosition();
            if (mList != null) {
                if (isShow) {
                    if (position == 0 || (mList.get(position - 1) != null && !TextUtils.isEmpty(mList.get(position - 1).getIndex()) && !mList.get(position - 1).getIndex().equals(bean.getIndex()))) {
                        holder.setGone(R.id.tv_index, true);
                        holder.setText(R.id.tv_index, bean.getIndex());
                    } else {
                        holder.setGone(R.id.tv_index, false);
                    }
                } else {
                    holder.setGone(R.id.tv_index, false);
                }
            }

            if (isShow) {
                holder.setGone(R.id.tv_remove, true);
            } else {
                holder.setGone(R.id.tv_remove, false);
            }

            //狗群主特权
            if (bean.getRole() == 3) {
                holder.setGone(R.id.iv_master, true);
                holder.setGone(R.id.tv_remove, false);
            } else {
                holder.setGone(R.id.iv_master, false);
            }

            holder.setText(R.id.tv_name, TextUtils.isEmpty(bean.getRemarks()) ? bean.getNickname() : bean.getRemarks());
            holder.setText(R.id.mentioned, TextUtils.isEmpty(bean.getLabel()) ? "暂无标签" : bean.getLabel());
            holder.addOnClickListener(R.id.tv_remove);

            ImageView iv = holder.getView(R.id.avatar);
            iv.setImageResource(R.drawable.im_avatar);
            iv.setTag(R.id.imageid, bean.getPath() + bean.getImusername());

            Glide.with(mContext).load(bean.getPath()).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    if (iv.getTag(R.id.imageid) != null && (bean.getPath() + bean.getImusername()).equals(iv.getTag(R.id.imageid))) {
                        iv.setImageBitmap(resource);
                    }
                }
            });
        }
    }
}