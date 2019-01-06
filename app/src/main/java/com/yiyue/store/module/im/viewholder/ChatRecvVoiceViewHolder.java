package com.yiyue.store.module.im.viewholder;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.hyphenate.chat.EMMessage;
import com.yiyue.store.R;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.model.vo.bean.ChatAdapterHolderbean;

/**
 * Created by zhangzz on 2018/10/10
 */
public class ChatRecvVoiceViewHolder extends BaseVoiceAdapter {
    private ImageView ic_user;
    private LinearLayout layout_recv_voice;
    private TextView chat_tv_voicelen;
    private Context mContext;

    public ChatRecvVoiceViewHolder(View view, Context context) {
        super(view);
        ic_user = (ImageView) itemView.findViewById(R.id.ic_user);
        layout_recv_voice = (LinearLayout) itemView.findViewById(R.id.layout_recv_voice);
        chat_tv_voicelen = itemView.findViewById(R.id.chat_tv_voicelen);
        mContext = context;
    }

    @Override
    public void setData(Object object) {
        super.setData(object);
        if (object != null) {
            ChatAdapterHolderbean model = (ChatAdapterHolderbean) object;
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
            chat_tv_voicelen.setText(model.getContent());

            layout_recv_voice.setOnClickListener(v -> {
                ToastUtils.shortToast("点击播放接收录音");
            });
            if (model.getMessage().direct() == EMMessage.Direct.RECEIVE) {
                if (model.getMessage().isListened()) {
                    // hide the unread icon
                    chat_iv_unread_voice.setVisibility(View.GONE);
                } else {
                    chat_iv_unread_voice.setVisibility(View.VISIBLE);
                }
            }
        }
    }

}