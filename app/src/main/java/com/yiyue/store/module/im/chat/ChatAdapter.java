package com.yiyue.store.module.im.chat;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.yiyue.store.R;

import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.model.vo.bean.ChatAdapterHolderbean;
import com.yiyue.store.model.vo.bean.ChatAdapterItemTypeBean;
import com.yiyue.store.module.im.chat.chatview.EmotionUtils;
import com.yiyue.store.module.im.chat.chatview.SpanStringUtils;
import com.yiyue.store.module.im.viewholder.BaseAdapter;
import com.yiyue.store.module.im.viewholder.ChatAddMoneyViewHolder;
import com.yiyue.store.module.im.viewholder.ChatRecvVoiceViewHolder;
import com.yiyue.store.module.im.viewholder.ChatRedPacketViewHolder;
import com.yiyue.store.module.im.viewholder.ChatSendLocationViewHolder;
import com.yiyue.store.module.im.viewholder.ChatSendPicViewHolder;
import com.yiyue.store.module.im.viewholder.ChatSendVoiceViewHolder;
import com.yiyue.store.module.im.viewholder.ChatVoiceCallViewHolder;

import java.util.ArrayList;

/**
 * Created by zhangzz on 2018/9/18
 */
public class ChatAdapter extends RecyclerView.Adapter<BaseAdapter> {
    private ArrayList<ChatAdapterItemTypeBean> dataList = new ArrayList<>();
    private Context context;

    public void replaceAll(ArrayList<ChatAdapterItemTypeBean> list) {
        dataList.clear();
        if (list != null && list.size() > 0) {
            dataList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<ChatAdapterItemTypeBean> list) {
        if (dataList != null && list != null) {
            dataList.addAll(list);
            notifyItemRangeChanged(dataList.size(), list.size());
        }

    }

    @Override
    public BaseAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        switch (viewType) {
            case ChatAdapterItemTypeBean.CHAT_RECV:
                return new ChatRecvViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_recv, parent, false), context);
            case ChatAdapterItemTypeBean.CHAT_SEND:
                return new ChatSendViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_send, parent, false), context);
            case ChatAdapterItemTypeBean.CHAT_RECV_VOICE:
                return new ChatRecvVoiceViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_recv_voice, parent, false), context);
            case ChatAdapterItemTypeBean.CHAT_SEND_VOICE:
                return new ChatSendVoiceViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_send_voice, parent, false), context);
            case ChatAdapterItemTypeBean.CHAT_SEND_PIC:
                return new ChatSendPicViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_send_pic, parent, false), context);
            case ChatAdapterItemTypeBean.CHAT_RECV_PIC:
                return new ChatSendPicViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_recv_pic, parent, false), context);
            case ChatAdapterItemTypeBean.CHAT_SEND_LOCATION:
                return new ChatSendLocationViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_send_location, parent, false), context);
            case ChatAdapterItemTypeBean.CHAT_RECV_LOCATION:
                return new ChatSendLocationViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_recv_location, parent, false), context);
            case ChatAdapterItemTypeBean.CHAT_SEND_VOICE_CALL:
                return new ChatVoiceCallViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_send_voice_call, parent, false), context);
            case ChatAdapterItemTypeBean.CHAT_RECV_VOICE_CALL:
                return new ChatVoiceCallViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_recv_voice_call, parent, false), context);
            case ChatAdapterItemTypeBean.CHAT_RECV_ADD_MONEY:
                return new ChatAddMoneyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_recv_add_money, parent, false), context);
            case ChatAdapterItemTypeBean.CHAT_SEND_ADD_MONEY:
                return new ChatAddMoneyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_send_add_money, parent, false), context);
            case ChatAdapterItemTypeBean.CHAT_SEND_RED_PACKET:
                return new ChatRedPacketViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_send_red_packet, parent, false), context);
            case ChatAdapterItemTypeBean.CHAT_RECV_RED_PACKET:
                return new ChatRedPacketViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_recv_red_packet, parent, false), context);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(BaseAdapter holder, int position) {
        holder.setData(dataList.get(position).object);
    }

    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).type;
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    private class ChatRecvViewHolder extends BaseAdapter {
        private ImageView ic_user;
        private TextView tv;
        private Context mContext;

        public ChatRecvViewHolder(View view, Context context) {
            super(view);
            ic_user = (ImageView) itemView.findViewById(R.id.ic_user);
            tv = (TextView) itemView.findViewById(R.id.tv);
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

                SpannableString spans = SpanStringUtils.getEmotionContent(EmotionUtils.EMOTION_CLASSIC_TYPE,
                        mContext, tv, model.getContent());
                tv.setText(spans);
            }
        }
    }

    private class ChatSendViewHolder extends BaseAdapter {
        private ImageView ic_user;
        private TextView tv;
        private Context mContext;

        public ChatSendViewHolder(View view, Context context) {
            super(view);
            ic_user = (ImageView) itemView.findViewById(R.id.ic_user);
            tv = (TextView) itemView.findViewById(R.id.tv);
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

                SpannableString spans = SpanStringUtils.getEmotionContent(EmotionUtils.EMOTION_CLASSIC_TYPE,
                        mContext, tv, model.getContent());
                tv.setText(spans);

                tv.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        ToastUtils.shortToast("长点击");
                        return true;
                    }
                });
            }
        }
    }
}
