package com.yiyue.store.module.im.friends;

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
import com.yiyue.store.model.vo.bean.daobean.UserFriendsBean;

import java.util.List;


/**
 */
public class UserFriendsAdapter extends BaseQuickAdapter<UserFriendsBean, BaseViewHolder> {
    private List<UserFriendsBean> mList;


    public UserFriendsAdapter(int layoutResId, List<UserFriendsBean> mList) {
        super(layoutResId, mList);
    }

    @Override
    public void setNewData(@Nullable List<UserFriendsBean> data) {
        super.setNewData(data);
        this.mList = data;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final UserFriendsBean friendBean) {
        int position = baseViewHolder.getLayoutPosition();
        if (mList != null && mList.size() > 0) {
            if (position == 0 || (!TextUtils.isEmpty(mList.get(position - 1).getIndex()) && !mList.get(position - 1).getIndex().equals(friendBean.getIndex()))) {
                baseViewHolder.setGone(R.id.tv_index, true);
                baseViewHolder.setText(R.id.tv_index, friendBean.getIndex());
            } else {
                baseViewHolder.setGone(R.id.tv_index, false);
            }
        }

        baseViewHolder.setText(R.id.tv_name, TextUtils.isEmpty(friendBean.getRemarks()) ? friendBean.getNickname() : friendBean.getRemarks());

        if (TextUtils.isEmpty(friendBean.getLabel())) {
            baseViewHolder.setText(R.id.mentioned, "暂无标签");
        } else {
            baseViewHolder.setText(R.id.mentioned, friendBean.getLabel());
        }

        ImageView iv = baseViewHolder.getView(R.id.avatar);
        iv.setImageResource(R.drawable.im_avatar);
        iv.setTag(R.id.imageid, friendBean.getPath() + friendBean.getImusername());

        Glide.with(mContext).load(friendBean.getPath()).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                if (iv.getTag(R.id.imageid) != null && (friendBean.getPath() + friendBean.getImusername()).equals(iv.getTag(R.id.imageid))) {
                    iv.setImageBitmap(resource);
                }
            }
        });
    }
}
