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
import com.hyphenate.chat.EMLocationMessageBody;
import com.yiyue.store.R;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.model.vo.bean.ChatAdapterHolderbean;

/**
 * Created by zhangzz on 2018/10/10
 */
public class ChatSendLocationViewHolder extends BaseAdapter {
    private ImageView ic_user;
    private LinearLayout layout_send_pic;
    private TextView chat_tv_location;
    private Context mContext;

    public ChatSendLocationViewHolder(View view, Context context) {
        super(view);
        ic_user = (ImageView) itemView.findViewById(R.id.ic_user);
        layout_send_pic = (LinearLayout) itemView.findViewById(R.id.layout_send_pic);
        chat_tv_location = itemView.findViewById(R.id.chat_tv_location);
        mContext = context;
    }

    @Override
    public void setData(Object object) {
        super.setData(object);
        if (object != null) {
            ChatAdapterHolderbean model = (ChatAdapterHolderbean) object;

            EMLocationMessageBody body = (EMLocationMessageBody) model.getMessage().getBody();
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
            chat_tv_location.setText(body.getAddress());
//        File file = new File(body.getLocalUrl());
//        if (file.exists()) {
//            Uri uri = Uri.fromFile(file);
//            GlideImageLoaderStrategy.getInstance().loadImage(chat_tv_location, body.getLocalUrl());
//        }

            layout_send_pic.setOnClickListener(v -> {
                ToastUtils.shortToast("点击进入地图页面");
            });
        }
    }
}