package com.yiyue.store.module.im.viewholder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.yiyue.store.R;
import com.yiyue.store.model.vo.bean.ChatAdapterHolderbean;
import com.yiyue.store.module.im.redpacket.reddetail.RedPacketDetailActivity;

/**
 * Created by zhangzz on 2018/11/5
 */
public class ChatRedPacketViewHolder extends BaseAdapter {
    private ImageView ic_user;
    private Context mContext;
    private TextView tv_red_name;
    private RelativeLayout layout_red_packet;

    public ChatRedPacketViewHolder(View view, Context context) {
        super(view);
        ic_user = itemView.findViewById(R.id.ic_user);
        tv_red_name = itemView.findViewById(R.id.tv_red_name);
        layout_red_packet = itemView.findViewById(R.id.layout_red_packet);
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

            if (model.getChatNoFriendBean() != null) {
                tv_red_name.setText(model.getChatNoFriendBean().getContent());
            }

            layout_red_packet.setOnClickListener(v -> {
                mContext.startActivity(new Intent(mContext, RedPacketDetailActivity.class));
            });
        }
    }
}
