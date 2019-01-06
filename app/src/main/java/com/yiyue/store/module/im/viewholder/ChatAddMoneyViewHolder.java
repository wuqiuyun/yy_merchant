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
import com.yiyue.store.R;
import com.yiyue.store.model.vo.bean.ChatAdapterHolderbean;

/**
 * Created by zhangzz on 2018/11/5
 */
public class ChatAddMoneyViewHolder extends BaseAdapter {
    private ImageView ic_user;
    private TextView tv;
    private Context mContext;
    private LinearLayout layout_choose;
    private TextView tv_status;

    public ChatAddMoneyViewHolder(View view, Context context) {
        super(view);
        ic_user = itemView.findViewById(R.id.ic_user);
        tv = itemView.findViewById(R.id.tv);
        layout_choose = itemView.findViewById(R.id.layout_choose);
        tv_status = itemView.findViewById(R.id.tv_status);
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
                //111.申请加入门店
                if (model.getChatNoFriendBean().getDefinedMsgType() == 111) {
                    tv.setText(mContext.getResources().getString(R.string.im_add_store));
                } else {
                    String spans = String.format(mContext.getResources().getString(R.string.im_add_money), getLabelType(model.getChatNoFriendBean().getDefinedMsgType()), model.getChatNoFriendBean().getMoney());
                    tv.setText(spans);
                }

                if (model.getChatNoFriendBean().getMsgStatus() == 1) {
                    layout_choose.setVisibility(View.VISIBLE);
                    tv_status.setVisibility(View.GONE);
                } else if (model.getChatNoFriendBean().getMsgStatus() == 2) {
                    layout_choose.setVisibility(View.GONE);
                    tv_status.setVisibility(View.VISIBLE);
                    tv_status.setText(mContext.getResources().getString(R.string.Refused));
                } else if (model.getChatNoFriendBean().getMsgStatus() == 3) {
                    layout_choose.setVisibility(View.GONE);
                    tv_status.setVisibility(View.VISIBLE);
                    tv_status.setText(mContext.getResources().getString(R.string.im_had_agree));
                } else if (model.getChatNoFriendBean().getMsgStatus() == 4) {
                    layout_choose.setVisibility(View.GONE);
                    tv_status.setVisibility(View.GONE);
                }
            }

        }
    }

    /**
     * 1.洗剪吹 2.洗吹 3.烫发 4.染发 5.护理 6.接发 7.洗色 8.拉直 9.单项套餐 10.多项套餐
     *
     * @param type
     * @return
     */
    private String getLabelType(int type) {
        switch (type) {
            case 1:
                return "洗剪吹";
            case 2:
                return "洗吹";
            case 3:
                return "烫发";
            case 4:
                return "染发";
            case 5:
                return "护理";
            case 6:
                return "接发";
            case 7:
                return "洗色";
            case 8:
                return "拉直";
            case 9:
                return "单项套餐";
            case 10:
                return "多项套餐";
        }
        return null;
    }
}
