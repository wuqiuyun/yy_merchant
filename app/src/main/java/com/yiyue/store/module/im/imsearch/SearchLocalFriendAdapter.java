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
import com.yiyue.store.model.vo.bean.daobean.UserFriendsBean;


/**
 */
public class SearchLocalFriendAdapter extends BaseQuickAdapter<UserFriendsBean, BaseViewHolder> {

    public SearchLocalFriendAdapter(int layoutResId) {
        super(layoutResId, null);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, UserFriendsBean searchUserBean) {
        if (searchUserBean != null) {
            baseViewHolder.setText(R.id.af_name, TextUtils.isEmpty(searchUserBean.getRemarks()) ? searchUserBean.getNickname() : searchUserBean.getRemarks());

            baseViewHolder.setText(R.id.af_tv_message, TextUtils.isEmpty(searchUserBean.getLabel()) ? "暂无标签" : searchUserBean.getLabel());
            baseViewHolder.addOnClickListener(R.id.af_tv_addfriend);
            baseViewHolder.setGone(R.id.af_tv_addfriend, false);

            ImageView iv = baseViewHolder.getView(R.id.af_avatar);
            iv.setTag(R.id.imageid, searchUserBean.getPath() + searchUserBean.getImusername());

            Glide.with(mContext).load(searchUserBean.getPath()).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    if (iv.getTag(R.id.imageid) != null && (searchUserBean.getPath() + searchUserBean.getImusername()).equals(iv.getTag(R.id.imageid))) {
                        iv.setImageBitmap(resource);
                    }
                }
            });
        }
    }
}
