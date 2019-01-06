package com.yiyue.store.module.im.redpacket.redrecords;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yiyue.store.R;
import com.yiyue.store.model.vo.bean.RedBagBean;
import com.yl.core.util.DateUtil;

import java.util.List;


/**
 * 红包转账记录使用的adapter
 */
public class RedRecordsAdapter extends BaseQuickAdapter<RedBagBean, BaseViewHolder> {

    public RedRecordsAdapter(int layoutResId, List<RedBagBean> mList) {
        super(layoutResId, mList);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final RedBagBean redBagBean) {
        baseViewHolder.setText(R.id.tv_name, TextUtils.isEmpty(redBagBean.getNickname()) ? "无" : redBagBean.getNickname());

        if (!TextUtils.isEmpty(redBagBean.getCreatetime())) {
            baseViewHolder.setText(R.id.red_time, DateUtil.getDay(Long.parseLong(redBagBean.getCreatetime())));
        }

        baseViewHolder.setText(R.id.tv_money, TextUtils.isEmpty(redBagBean.getReceiveamount() + "") ? "0" : redBagBean.getReceiveamount() + "");

        ImageView iv = baseViewHolder.getView(R.id.avatar);
        iv.setImageResource(R.drawable.im_avatar);
        iv.setTag(R.id.imageid, redBagBean.getPath() + redBagBean.getSendUserId());

        Glide.with(mContext).load(redBagBean.getPath()).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                if (iv.getTag(R.id.imageid) != null && (redBagBean.getPath() + redBagBean.getSendUserId()).equals(iv.getTag(R.id.imageid))) {
                    iv.setImageBitmap(resource);
                }
            }
        });
    }
}
