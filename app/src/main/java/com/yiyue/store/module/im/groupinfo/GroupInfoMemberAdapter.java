package com.yiyue.store.module.im.groupinfo;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yiyue.store.R;
import com.yiyue.store.model.vo.bean.daobean.GroupUserBean;


/**
 */
public class GroupInfoMemberAdapter extends BaseQuickAdapter<GroupUserBean, BaseViewHolder> {
    private Context mContext;

    public GroupInfoMemberAdapter(int layoutResId, Context context) {
        super(layoutResId, null);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, GroupUserBean bean) {
        if (bean != null) {
            if (bean.isAdd()) {
                baseViewHolder.setImageResource(R.id.image_head, R.drawable.group_icon_add);
            } else {
                ImageView iv = baseViewHolder.getView(R.id.image_head);
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
//                GlideImageLoaderStrategy.getInstance()
//                        .loadImage(baseViewHolder.getView(R.id.image_head), bean.getPath(), mContext.getString(R.string.debug_head_url));
            }
        }
    }
}
