package com.yiyue.store.module.im.groups.groupaddfriends;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.CompoundButton;
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
public class GroupAddFriendsAdapter extends BaseQuickAdapter<UserFriendsBean, BaseViewHolder> {
    private boolean isSelect;//是否需要勾选

    public GroupAddFriendsAdapter(int layoutResId, boolean isSelect) {
        super(layoutResId, null);
        this.isSelect = isSelect;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final UserFriendsBean friendBean) {
        if (friendBean != null) {
            baseViewHolder.setGone(R.id.tv_index, false);
            if (isSelect) baseViewHolder.setVisible(R.id.cb_select, true);
            else baseViewHolder.setVisible(R.id.cb_select, false);

            baseViewHolder.setText(R.id.tv_name, TextUtils.isEmpty(friendBean.getRemarks()) ? friendBean.getNickname() : friendBean.getRemarks());
            baseViewHolder.setChecked(R.id.cb_select, friendBean.isSelect());

            if (friendBean.isEnable()){
                baseViewHolder.setVisible(R.id.cb_select, true);
            } else {
                baseViewHolder.setVisible(R.id.cb_select, false);
            }

            baseViewHolder.setOnCheckedChangeListener(R.id.cb_select, new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    friendBean.setSelect(isChecked);
                }
            });

            baseViewHolder.setText(R.id.mentioned, friendBean.getLabel());

            ImageView iv = baseViewHolder.getView(R.id.avatar);
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
}
