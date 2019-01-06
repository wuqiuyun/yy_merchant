package com.yiyue.store.module.im.imsearch;

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
 */
public class SearchLocalGroupAdapter extends BaseQuickAdapter<GroupBean, BaseViewHolder> {

    public SearchLocalGroupAdapter(int layoutResId) {
        super(layoutResId, null);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, GroupBean groupBean) {
        if (groupBean != null) {
            baseViewHolder.setText(R.id.af_name, groupBean.getName());

            baseViewHolder.setText(R.id.af_tv_message, TextUtils.isEmpty(groupBean.getDescribe()) ? "暂无描述" : groupBean.getDescribe());
            baseViewHolder.addOnClickListener(R.id.af_tv_addfriend);
            baseViewHolder.setGone(R.id.af_tv_addfriend, false);

            ImageView iv = baseViewHolder.getView(R.id.af_avatar);
            iv.setTag(R.id.imageid, groupBean.getPath() + groupBean.getImgroup());

            Glide.with(mContext).load(groupBean.getPath()).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    if (iv.getTag(R.id.imageid) != null && (groupBean.getPath() + groupBean.getImgroup()).equals(iv.getTag(R.id.imageid))) {
                        iv.setImageBitmap(resource);
                    }
                }
            });
        }
    }
}
