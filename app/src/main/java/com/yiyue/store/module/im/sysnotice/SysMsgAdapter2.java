package com.yiyue.store.module.im.sysnotice;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.vo.bean.daobean.ImMessageBean;
import com.yl.core.component.image.ImageLoader;
import com.yl.core.component.image.ImageLoaderConfig;
import com.yl.core.util.DateUtil;
import com.yiyue.store.R;


/**
 * 互动消息显示适配器
 */
public class SysMsgAdapter2 extends BaseQuickAdapter<ImMessageBean, BaseViewHolder> {

    public SysMsgAdapter2(int layoutResId) {
        super(layoutResId, null);
    }

    @Override
    protected void convert(final BaseViewHolder holder, final ImMessageBean msg) {
        if (msg != null) {
            //发起申请者是否为自己这一方
            boolean isSelf = String.valueOf(msg.getRequestUserId()).equals(AccountManager.getInstance().getUserId());
            // 加载的头像始终为对方
            ImageLoaderConfig config = new ImageLoaderConfig.Builder().
                    setCropType(ImageLoaderConfig.CENTER_INSIDE).
                    setAsBitmap(true).
                    setCropCircle(true).
                    setPlaceHolderResId(R.drawable.im_avatar).
                    setErrorResId(R.drawable.im_avatar).
                    setDiskCacheStrategy(ImageLoaderConfig.DiskCache.SOURCE).
                    setPrioriy(ImageLoaderConfig.LoadPriority.HIGH).build();
            if (isSelf){
                ImageLoader.loadImage(holder.getView(R.id.avatar), msg.getFriendPtah(), config, null);
            } else {
                ImageLoader.loadImage(holder.getView(R.id.avatar), msg.getRequestPtah(), config, null);
            }

            //状态  0 申请添加  1 同意申请 2 拒绝申请 3 4 5 6 7
            //为申请状态时，才显示同意和拒绝按钮
            if (msg.getStatus() == 0) {
                holder.setGone(R.id.rl_refuse_agree, true);
                holder.setText(R.id.tv_status, "");
            } else {
                holder.setGone(R.id.rl_refuse_agree, false);
                if (msg.getStatus() == 1) {
                    holder.setText(R.id.tv_status, "已同意");
                } else if (msg.getStatus() == 2) {
                    holder.setText(R.id.tv_status, "已拒绝");
                }
            }

            switch (msg.getType()) {
                case 1://好友申请
                    holder.setText(R.id.tv_msg_type, mContext.getString(R.string.smsg_friend_notice));
                    //如果申请人id是自己 则是自己向他人加好友
                    if(isSelf){
                        holder.setText(R.id.name, msg.getFriendNickname());
                        holder.setText(R.id.message, "您申请加TA为好友 " + msg.getRemarks());
                        //
                        if (msg.getStatus() == 0) holder.setText(R.id.tv_status, "等待验证");
                        //不显示同意/拒绝按钮
                        holder.setGone(R.id.rl_refuse_agree, false);

                    }
                    //他人的好友申请
                    else {
                        holder.setText(R.id.name, msg.getRequestNickname());
                        holder.setText(R.id.message, "申请加您为好友 " + msg.getRemarks());
                    }
                    break;

                case 2://加群申请
                    holder.setText(R.id.tv_msg_type, mContext.getString(R.string.smsg_group_notice));
                    //如果申请人id是自己 则是自己申请入群
                    if(isSelf){
                        holder.setText(R.id.name, msg.getFriendNickname());
                        holder.setText(R.id.message, "您申请加入群 " + msg.getFriendNickname());
                        //
                        if (msg.getStatus() == 0) holder.setText(R.id.tv_status, "等待验证");
                        //不显示同意/拒绝按钮
                        holder.setGone(R.id.rl_refuse_agree, false);
                    }
                    //他人的加群申请
                    else {
                        holder.setText(R.id.name, msg.getRequestNickname());
                        String message1 = "申请加入 " + msg.getFriendNickname() + " " + msg.getRemarks();
                        holder.setText(R.id.message, message1);
                    }
                    break;
                case 3://邀请加群
                    // TODO: 2018/11/19 具体样式可能需修改
                    holder.setText(R.id.tv_msg_type, mContext.getString(R.string.smsg_group_notice));
                    holder.setText(R.id.name, msg.getRequestNickname());
                    String message2 = "群主邀请您加入 " + msg.getFriendNickname();
                    holder.setText(R.id.message, message2);
                    break;
            }
            holder.setText(R.id.tv_time, DateUtil.getTime(msg.getCreatetime(), DateUtil.FORMAT_HM));

            holder.addOnClickListener(R.id.tv_agree);
            holder.addOnClickListener(R.id.tv_refuse);
        }
    }
}
