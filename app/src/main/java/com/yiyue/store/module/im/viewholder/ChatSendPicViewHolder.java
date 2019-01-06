package com.yiyue.store.module.im.viewholder;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.hyphenate.chat.EMImageMessageBody;
import com.yl.core.component.image.glide.GlideImageLoaderStrategy;
import com.yiyue.store.R;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.model.vo.bean.ChatAdapterHolderbean;

import java.io.File;

/**
 * Created by zhangzz on 2018/10/10
 */
public class ChatSendPicViewHolder extends BaseAdapter {
    private ImageView ic_user;
    private LinearLayout layout_send_pic;
    private ImageView chat_iv_pic;
    private Context mContext;

    public ChatSendPicViewHolder(View view, Context context) {
        super(view);
        ic_user = (ImageView) itemView.findViewById(R.id.ic_user);
        layout_send_pic = (LinearLayout) itemView.findViewById(R.id.layout_send_pic);
        chat_iv_pic = itemView.findViewById(R.id.chat_iv_pic);
        mContext = context;
    }

    @Override
    public void setData(Object object) {
        super.setData(object);
        if (object != null) {
            ChatAdapterHolderbean model = (ChatAdapterHolderbean) object;

            EMImageMessageBody body = (EMImageMessageBody) model.getMessage().getBody();
            ic_user.setImageResource(R.drawable.im_avatar);
            ic_user.setTag(R.id.imageid, model.getMessage().getFrom() + model.getIcon());

            Glide.with(mContext).load(model.getIcon()).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    if (ic_user.getTag(R.id.imageid) != null && (model.getMessage().getFrom() + model.getIcon()).equals(ic_user.getTag(R.id.imageid))) {
                        ic_user.setImageBitmap(resource);
                    }
                }
            });

            File file = new File(body.getLocalUrl());
            if (file.exists()) {
                GlideImageLoaderStrategy.getInstance().loadImage(chat_iv_pic, body.getLocalUrl());
            } else {
                GlideImageLoaderStrategy.getInstance().loadImage(chat_iv_pic, body.getRemoteUrl());
            }

            layout_send_pic.setOnClickListener(v -> {
                ToastUtils.shortToast("点击放大图片");
            });
        }
    }
}