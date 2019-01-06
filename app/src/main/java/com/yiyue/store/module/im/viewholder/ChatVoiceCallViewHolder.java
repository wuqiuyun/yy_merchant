package com.yiyue.store.module.im.viewholder;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.yiyue.store.R;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.model.vo.bean.ChatAdapterHolderbean;

/**
 * Created by zhangzz on 2018/10/10
 */
public class ChatVoiceCallViewHolder extends BaseVoiceAdapter {
    private ImageView ic_user;
    private LinearLayout layout_voice_call;
    private TextView chat_tv_voice_callmsg;
    private Context mContext;

    public ChatVoiceCallViewHolder(View view, Context context) {
        super(view);
        ic_user = (ImageView) itemView.findViewById(R.id.ic_user);
        layout_voice_call = (LinearLayout) itemView.findViewById(R.id.layout_voice_call);
        chat_tv_voice_callmsg = itemView.findViewById(R.id.chat_tv_voice_callmsg);
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
            if (TextUtils.isEmpty(model.getContent())) {
                chat_tv_voice_callmsg.setVisibility(View.GONE);
            } else {
                chat_tv_voice_callmsg.setVisibility(View.VISIBLE);
                chat_tv_voice_callmsg.setText(model.getContent());
            }
            layout_voice_call.setOnClickListener(v -> {
                ToastUtils.shortToast("点击语音通话");
            });
        }
    }
}