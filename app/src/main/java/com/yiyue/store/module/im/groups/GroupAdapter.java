package com.yiyue.store.module.im.groups;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yiyue.store.R;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.vo.bean.daobean.GroupBean;


/**
 */
public class GroupAdapter extends BaseQuickAdapter<GroupBean, BaseViewHolder> {

    public GroupAdapter(int layoutResId) {
        super(layoutResId, null);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, GroupBean group) {
        baseViewHolder.setText(R.id.tv_name, group.getName());
        if (AccountManager.getInstance().getUserId().equals(String.valueOf(group.getUserId()))) {
            baseViewHolder.setGone(R.id.iv_owner, true);
        } else {
            baseViewHolder.setVisible(R.id.iv_owner, false);
        }
        ImageView iv = baseViewHolder.getView(R.id.iv_avatar);
        iv.setTag(R.id.imageid, group.getPath() + group.getImgroup());

        Glide.with(mContext).load(group.getPath()).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                if (iv.getTag(R.id.imageid) != null && (group.getPath() + group.getImgroup()).equals(iv.getTag(R.id.imageid))) {
                    iv.setImageBitmap(resource);
                }
            }
        });
    }
}
