package com.yiyue.store.module.im.conversation;

import android.graphics.Bitmap;
import android.text.SpannableString;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yiyue.store.module.im.chat.chatview.EmotionUtils;
import com.yiyue.store.module.im.chat.chatview.SpanStringUtils;
import com.yl.core.util.DateUtil;
import com.yiyue.store.R;
import com.yiyue.store.model.vo.bean.ConversationBean;

import java.util.List;


/**
 */
public class ConverAdapter extends BaseQuickAdapter<ConversationBean, BaseViewHolder> {

    public ConverAdapter(int layoutResId, List<ConversationBean> conBeans) {
        super(layoutResId, conBeans);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ConversationBean conversationBean) {
//        baseViewHolder.setText(R.id.conv_tv_content, conversationBean.getContent());
        TextView tvContent = baseViewHolder.getView(R.id.conv_tv_content);
        SpannableString spans = SpanStringUtils.getEmotionContent(EmotionUtils.EMOTION_CLASSIC_TYPE, mContext, tvContent, conversationBean.getContent());
        tvContent.setText(spans);

        baseViewHolder.setText(R.id.conv_tv_name, TextUtils.isEmpty(conversationBean.getRemarks()) ? conversationBean.getNickname() : conversationBean.getRemarks());
        baseViewHolder.setText(R.id.conv_tv_time, DateUtil.getTime(conversationBean.getCreatetime(), DateUtil.FORMAT_HM));
        if (conversationBean.getStatus() == 0) {
            if (conversationBean.getUnreadNum() == 0) {
                baseViewHolder.setVisible(R.id.conv_layout_unread_icon, false);
            } else {
                baseViewHolder.setVisible(R.id.conv_layout_unread_icon, true);
                baseViewHolder.setText(R.id.conv_tv_unread_icon, conversationBean.getUnreadNum() + "");
            }
        } else {
            baseViewHolder.setVisible(R.id.conv_layout_unread_icon, false);
        }

        ImageView iv = baseViewHolder.getView(R.id.conv_iv_head);
        iv.setImageResource(R.drawable.im_avatar);
        iv.setTag(R.id.imageid, conversationBean.getPath() + conversationBean.getImusername());

        Glide.with(mContext).load(conversationBean.getPath()).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                if (iv.getTag(R.id.imageid) != null && (conversationBean.getPath() + conversationBean.getImusername()).equals(iv.getTag(R.id.imageid))) {
                    iv.setImageBitmap(resource);
                }
            }
        });
    }
}
