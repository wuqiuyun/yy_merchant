package com.yiyue.store.module.im.groups.groupsearch;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yiyue.store.R;
import com.yiyue.store.model.vo.bean.daobean.GroupBean;

/**
 * Created by Lizhuo on 2018/10/15.
 */
public class GroupSearchAdapter extends BaseQuickAdapter<GroupBean, BaseViewHolder> {
    public GroupSearchAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder holder, GroupBean item) {
        if (item != null) {
            ImageView iv = holder.getView(R.id.iv_avatar);
            iv.setTag(R.id.imageid, item.getPath() + item.getImusername());

            Glide.with(mContext).load(item.getPath()).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    if (iv.getTag(R.id.imageid) != null && (item.getPath() + item.getImusername()).equals(iv.getTag(R.id.imageid))) {
                        iv.setImageBitmap(resource);
                    }
                }
            });

            holder.setText(R.id.tv_name, item.getName());
            holder.setText(R.id.tv_member_num, item.getAffiliationscount() + "人");
            holder.setText(R.id.tv_group_info, TextUtils.isEmpty(item.getDescribe()) ? "暂无介绍" : item.getDescribe());
        }
    }
}
