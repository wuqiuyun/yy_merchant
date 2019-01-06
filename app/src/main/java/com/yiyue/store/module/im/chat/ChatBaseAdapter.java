package com.yiyue.store.module.im.chat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMLocationMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.DateUtils;
import com.yiyue.store.R;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.vo.bean.ChatAdapterHolderbean;
import com.yiyue.store.model.vo.bean.ChatAdapterItemTypeBean;
import com.yiyue.store.model.vo.bean.GroupJoinMemberBean;
import com.yiyue.store.model.vo.bean.SelfDefinedInfoBean;
import com.yiyue.store.module.im.PicBigActivity;
import com.yiyue.store.module.im.chat.chatview.EmotionUtils;
import com.yiyue.store.module.im.chat.chatview.ImChatRowVoicePlayer;
import com.yiyue.store.module.im.chat.chatview.SpanStringUtils;
import com.yiyue.store.module.im.immap.ImMapActivity;
import com.yl.core.component.image.ImageLoader;
import com.yl.core.component.image.ImageLoaderConfig;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangzz on 2018/11/26
 */
public abstract class ChatBaseAdapter extends BaseMultiItemQuickAdapter<ChatAdapterItemTypeBean, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ChatBaseAdapter(List<ChatAdapterItemTypeBean> data) {
        super(data);
    }

    protected void createTransAccountView(BaseViewHolder helper, ChatAdapterItemTypeBean item) {
        if (item != null && item.object != null) {
            ChatAdapterHolderbean model = (ChatAdapterHolderbean) item.object;

            timeShow(helper, model);

            ImageView ic_user = helper.getView(R.id.ic_user);
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
                SelfDefinedInfoBean chatNoFriendBean = model.getChatNoFriendBean();
                double money = chatNoFriendBean.getMoney();
                if (!TextUtils.isEmpty(money + "")) {
                    helper.setText(R.id.tv_trans_money_num, String.format(chatNoFriendBean.getContent(), money + ""));
                }
            }

            helper.addOnClickListener(R.id.layout_transfer);
            helper.addOnClickListener(R.id.ic_user);
        }
    }

    protected void createRedOpenView(BaseViewHolder helper, ChatAdapterItemTypeBean item) {
        if (item != null && item.object != null) {
            ChatAdapterHolderbean model = (ChatAdapterHolderbean) item.object;
            timeShow(helper, model);

            if (model != null && model.getChatNoFriendBean() != null) {
                boolean isMySelf = false;
                SelfDefinedInfoBean chatNoFriendBean = model.getChatNoFriendBean();
                String myIm = AccountManager.getInstance().getAccount().getImusername();
                if (!TextUtils.isEmpty(chatNoFriendBean.getToImusername()) && !TextUtils.isEmpty(myIm)) {
                    if (chatNoFriendBean.getToImusername().equals(myIm)) {//如果该红包发送对象是 我自己
                        isMySelf = true;
                    } else {
                        isMySelf = false;
                    }
                }

                if (model.getChatNoFriendBean().getDefinedMsgType() == 202) {
                    if (isMySelf) {
                        helper.setText(R.id.tv, String.format(mContext.getString(R.string.rp_self_open_red), model.getChatNoFriendBean().getRecvNickname()));
                    } else {
                        helper.setText(R.id.tv, String.format(mContext.getString(R.string.rp_other_open_red), model.getChatNoFriendBean().getToNickname()));
                    }
                } else if (model.getChatNoFriendBean().getDefinedMsgType() == 302) {
                    if (isMySelf) {
                        helper.setText(R.id.tv, mContext.getResources().getString(R.string.transfer_money_inme));
                    } else {
                        helper.setText(R.id.tv, mContext.getResources().getString(R.string.transfer_money_inother));
                    }
                }
            }

            helper.addOnClickListener(R.id.ic_user);
        }
    }

    protected void createGroupJoinView(BaseViewHolder helper, ChatAdapterItemTypeBean item) {
        if (item != null && item.object != null) {
            ChatAdapterHolderbean model = (ChatAdapterHolderbean) item.object;
            timeShow(helper, model);

            if (model != null && model.getChatNoFriendBean() != null) {
                boolean isMySelf = false;
                SelfDefinedInfoBean chatNoFriendBean = model.getChatNoFriendBean();
                String myIm = AccountManager.getInstance().getAccount().getImusername();
                if (!TextUtils.isEmpty(chatNoFriendBean.getImusername()) && !TextUtils.isEmpty(myIm)) {
                    if (chatNoFriendBean.getImusername().equals(myIm)) {//如果该红包发送对象是 我自己
                        isMySelf = true;
                    } else {
                        isMySelf = false;
                    }
                }
                StringBuffer groupJoinContent = new StringBuffer();
                if (chatNoFriendBean.getGroupJoinMembers() != null && chatNoFriendBean.getGroupJoinMembers().size() > 0) {
                    GroupJoinMemberBean groupJoinMemberBean = new GroupJoinMemberBean(AccountManager.getInstance().getNickName(), AccountManager.getInstance().getAccount().getImusername());
                    if (chatNoFriendBean.getGroupJoinMembers().contains(groupJoinMemberBean)) {
                        groupJoinContent.append(chatNoFriendBean.getNickname() + " 邀请你加入群聊");
                    } else {
                        String memberStr = getMemberNames(chatNoFriendBean.getGroupJoinMembers());
                        if (isMySelf){
                            groupJoinContent.append(memberStr + " 加入群聊");
                        } else {
                            groupJoinContent.append(chatNoFriendBean.getNickname() + " 邀请 " + memberStr + " 加入群聊");
                        }
                    }

                }

                helper.setText(R.id.tv, groupJoinContent.toString());
            }

            helper.addOnClickListener(R.id.ic_user);
        }
    }

    private String getMemberNames(List<GroupJoinMemberBean> groupJoinMembers) {
        StringBuffer groupJoinContent = new StringBuffer();
        if (groupJoinMembers!=null && groupJoinMembers.size()>0){
            int len = groupJoinMembers.size();
            for (int i = 0; i < groupJoinMembers.size(); ++i){
                GroupJoinMemberBean bean = groupJoinMembers.get(i);
                if (i == len - 1){
                    groupJoinContent.append(bean.getNickName());
                } else {
                    groupJoinContent.append(bean.getNickName() + "、");
                }
            }
        }

        return groupJoinContent.toString();
    }

    protected void createShareMsgView(BaseViewHolder helper, ChatAdapterItemTypeBean item) {
        if (item != null && item.object != null) {
            ChatAdapterHolderbean model = (ChatAdapterHolderbean) item.object;
            timeShow(helper, model);

            ImageView ic_user = helper.getView(R.id.ic_user);
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

            // * 101.分享门店 102.分享作品 103.分享美发师 110.推荐好友  111.邀请平台美发师加入 112.邀请门店美发师加入  113平台美发师加入申请 114门店美发师加入申请

            if (model.getChatNoFriendBean() != null) {
                SelfDefinedInfoBean chatNoFriendBean = model.getChatNoFriendBean();
                helper.setText(R.id.tv, getLabelType(chatNoFriendBean.getDefinedMsgType()));

                showNomHeadPathUI(helper, chatNoFriendBean);

                if (chatNoFriendBean.getDefinedMsgType() == 111 || chatNoFriendBean.getDefinedMsgType() == 112 || chatNoFriendBean.getDefinedMsgType() == 113 || chatNoFriendBean.getDefinedMsgType() == 114) {
                    String myIm = AccountManager.getInstance().getAccount().getImusername();
                    if (!TextUtils.isEmpty(model.getChatNoFriendBean().getToImusername()) && !TextUtils.isEmpty(myIm)) {
                        if (chatNoFriendBean.getMsgStatus() == 1) {
                            if (model.getChatNoFriendBean().getToImusername().equals(myIm)) {//如果是本人发出的 发起状态的邀请美发师状态  则 不显示底部选择框
                                helper.setGone(R.id.layout_choose_label, false);
                            } else {
                                helper.setGone(R.id.layout_choose_label, true);
                                showOrHideChooseUi(chatNoFriendBean, helper);
                            }
                        } else {
                            helper.setGone(R.id.layout_choose_label, true);
                            showOrHideChooseUi(chatNoFriendBean, helper);
                        }
                    } else {
                        helper.setGone(R.id.layout_choose_label, true);
                        showOrHideChooseUi(chatNoFriendBean, helper);
                    }
                    helper.setText(R.id.tv_content, TextUtils.isEmpty(chatNoFriendBean.getAddress()) ? "暂无" : chatNoFriendBean.getAddress());
                } else {
                    helper.setGone(R.id.layout_choose_label, false);
                    helper.setText(R.id.tv_content, TextUtils.isEmpty(chatNoFriendBean.getToLabel()) ? "暂无" : chatNoFriendBean.getToLabel());
                }

            }

            helper.addOnClickListener(R.id.layout_share_content);
            helper.addOnClickListener(R.id.ic_user);
        }

    }

    protected void showNomHeadPathUI(BaseViewHolder helper, SelfDefinedInfoBean chatNoFriendBean) {
        ImageView iv_head = helper.getView(R.id.iv_head);
        iv_head.setImageResource(R.drawable.im_avatar);
        iv_head.setTag(R.id.imageid, chatNoFriendBean.getToImusername() + chatNoFriendBean.getToPath());

        Glide.with(mContext).load(chatNoFriendBean.getToPath()).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                if (iv_head.getTag(R.id.imageid) != null && (chatNoFriendBean.getToImusername() + chatNoFriendBean.getToPath()).equals(iv_head.getTag(R.id.imageid))) {
                    iv_head.setImageBitmap(resource);
                }
            }
        });

        helper.setText(R.id.tv_content_name, chatNoFriendBean.getToNickname());

        if (chatNoFriendBean.getGender() == 1) {
            helper.setImageDrawable(R.id.iv_sex, mContext.getResources().getDrawable(R.drawable.man_selected));
        } else {
            helper.setImageDrawable(R.id.iv_sex, mContext.getResources().getDrawable(R.drawable.girl_selected));
        }
    }

    /**
     * 在邀请加入平台和申请加入平台时  才展示底部选择框，此时才需要判断是展示选择同意 还是已拒绝 已同意状态的ui
     *
     * @param chatNoFriendBean
     * @param helper
     */
    protected void showOrHideChooseUi(SelfDefinedInfoBean chatNoFriendBean, BaseViewHolder helper) {
        //1为发送人发起加价状态 2为已回复 同意  3.为已回复 拒绝的状态
        if (chatNoFriendBean.getMsgStatus() == 1) {
            String myIm = AccountManager.getInstance().getAccount().getImusername();
            if (!TextUtils.isEmpty(chatNoFriendBean.getImusername()) && !TextUtils.isEmpty(myIm)) {
                if (chatNoFriendBean.getImusername().equals(myIm)) {//如果是本人发出的 发起状态的邀请美发师状态  则 不显示底部选择框
                    helper.setGone(R.id.layout_choose, false);
                    helper.setGone(R.id.tv_status, false);
                } else {
                    helper.setGone(R.id.layout_choose, true);
                    helper.setGone(R.id.tv_status, false);
                }
            } else {
                helper.setGone(R.id.layout_choose, true);
                helper.setGone(R.id.tv_status, false);
            }

        } else if (chatNoFriendBean.getMsgStatus() == 2) {
            helper.setGone(R.id.layout_choose, false);
            helper.setGone(R.id.tv_status, true);
            helper.setText(R.id.tv_status, mContext.getResources().getString(R.string.im_had_agree));
        } else if (chatNoFriendBean.getMsgStatus() == 3) {
            helper.setGone(R.id.layout_choose, false);
            helper.setGone(R.id.tv_status, true);
            helper.setText(R.id.tv_status, mContext.getResources().getString(R.string.Refused));
        }
    }

    protected void createRedPacketView(BaseViewHolder helper, ChatAdapterItemTypeBean item) {
        if (item != null && item.object != null) {
            ChatAdapterHolderbean model = (ChatAdapterHolderbean) item.object;
            timeShow(helper, model);

            ImageView ic_user = helper.getView(R.id.ic_user);
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
                SelfDefinedInfoBean chatNoFriendBean = model.getChatNoFriendBean();
                helper.setText(R.id.tv_red_name, chatNoFriendBean.getContent());
            }

            TextView tv_red_content = helper.getView(R.id.tv_red_content);//领取红包几个字的透明度
            tv_red_content.setTextColor(Color.argb(155, 0, 255, 0));  //文字透明度

            helper.addOnClickListener(R.id.layout_red_packet);
            helper.addOnClickListener(R.id.ic_user);
        }
    }


    protected void createAddMoneyView(BaseViewHolder helper, ChatAdapterItemTypeBean item) {
        if (item != null && item.object != null) {
            ChatAdapterHolderbean model = (ChatAdapterHolderbean) item.object;
            timeShow(helper, model);

            ImageView ic_user = helper.getView(R.id.ic_user);
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
                String spans = String.format(mContext.getResources().getString(R.string.im_add_money), model.getChatNoFriendBean().getServiceName(), model.getChatNoFriendBean().getMoney());
                helper.setText(R.id.tv, spans);

                if (model.getChatNoFriendBean().getMsgStatus() == 1) {
                    String myIm = AccountManager.getInstance().getAccount().getImusername();
                    if (!TextUtils.isEmpty(model.getChatNoFriendBean().getToImusername()) && !TextUtils.isEmpty(myIm)) {
                        if (!model.getChatNoFriendBean().getToImusername().equals(myIm)) {//如果是本人发出的 发起状态的邀请美发师状态  则 不显示底部选择框
                            helper.setGone(R.id.layout_choose, false);
                            helper.setGone(R.id.tv_status, false);
                        } else {
                            helper.setGone(R.id.layout_choose, true);
                            helper.setGone(R.id.tv_status, false);
                        }
                    } else {
                        helper.setGone(R.id.layout_choose, true);
                        helper.setGone(R.id.tv_status, false);
                    }
                } else if (model.getChatNoFriendBean().getMsgStatus() == 2) {
                    helper.setGone(R.id.layout_choose, false);
                    helper.setGone(R.id.tv_status, true);
                    helper.setText(R.id.tv_status, mContext.getResources().getString(R.string.im_had_agree));
                } else if (model.getChatNoFriendBean().getMsgStatus() == 3) {
                    helper.setGone(R.id.layout_choose, false);
                    helper.setGone(R.id.tv_status, true);
                    helper.setText(R.id.tv_status, mContext.getResources().getString(R.string.Refused));
                }
            }
            helper.addOnClickListener(R.id.layout_addmoney_content);
            helper.addOnClickListener(R.id.ic_user);
        }
    }

    protected void createVoiceCallView(BaseViewHolder helper, ChatAdapterItemTypeBean item, boolean isVoice) {
        if (item != null && item.object != null) {
            ChatAdapterHolderbean model = (ChatAdapterHolderbean) item.object;

            timeShow(helper, model);
            ImageView ic_user = helper.getView(R.id.ic_user);
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
                helper.setGone(R.id.chat_tv_voice_callmsg, false);
            } else {
                helper.setGone(R.id.chat_tv_voice_callmsg, true);
                helper.setText(R.id.chat_tv_voice_callmsg, model.getContent());
                if (isVoice) {
                    helper.setImageResource(R.id.chat_iv_voice, R.drawable.im_chat_voice_call_receive);
                } else {
                    helper.setImageResource(R.id.chat_iv_voice, R.drawable.im_chat_video_call_self);
                }
            }
//            helper.getView(R.id.layout_voice_call).setOnClickListener(v -> {
//                //再次吊起语音通话
//            });
            helper.addOnClickListener(R.id.layout_voice_call);  //再次吊起语音通话
            helper.addOnClickListener(R.id.ic_user);
        }
    }


    protected void createLocationChatView(BaseViewHolder helper, ChatAdapterItemTypeBean item) {
        if (item != null && item.object != null) {
            ChatAdapterHolderbean model = (ChatAdapterHolderbean) item.object;

            timeShow(helper, model);

            EMLocationMessageBody body = (EMLocationMessageBody) model.getMessage().getBody();
            ImageView ic_user = helper.getView(R.id.ic_user);
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
            helper.setText(R.id.chat_tv_location, body.getAddress());
//        File file = new File(body.getLocalUrl());
//        if (file.exists()) {
//            Uri uri = Uri.fromFile(file);
//            GlideImageLoaderStrategy.getInstance().loadImage(chat_tv_location, body.getLocalUrl());
//        }

            helper.getView(R.id.layout_send_location).setOnClickListener(v -> {
                if (body != null) {
                    Intent intent = new Intent(mContext, ImMapActivity.class);
                    intent.putExtra("lat", body.getLatitude());
                    intent.putExtra("lon", body.getLongitude());
                    intent.putExtra("address", body.getAddress());
                    mContext.startActivity(intent);
                }
            });

            helper.addOnClickListener(R.id.ic_user);
        }
    }


    protected void createNormChatView(BaseViewHolder helper, ChatAdapterItemTypeBean item) {
        if (item != null && item.object != null) {
            ChatAdapterHolderbean model = (ChatAdapterHolderbean) item.object;
            timeShow(helper, model);

            ImageView ic_user = helper.getView(R.id.ic_user);
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

            TextView tv = helper.getView(R.id.tv);
            SpannableString spans = SpanStringUtils.getEmotionContent(EmotionUtils.EMOTION_CLASSIC_TYPE,
                    mContext, tv, model.getContent());
            tv.setText(spans);

            helper.addOnClickListener(R.id.ic_user);
        }
    }

    protected ImChatRowVoicePlayer voicePlayer;
    protected AnimationDrawable voiceAnimation;

    protected void createVoiceChatView(BaseViewHolder helper, ChatAdapterItemTypeBean item) {
        if (item != null && item.object != null) {
            voicePlayer = ImChatRowVoicePlayer.getInstance(mContext);
            ImageView chat_iv_voice = helper.getView(R.id.chat_iv_voice);
            ImageView chat_iv_unread_voice = helper.getView(R.id.chat_iv_unread_voice);

            ChatAdapterHolderbean model = (ChatAdapterHolderbean) item.object;
            timeShow(helper, model);

            ImageView ic_user = helper.getView(R.id.ic_user);
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
            helper.setText(R.id.chat_tv_voicelen, model.getContent());

            helper.setOnClickListener(R.id.layout_recv_voice, v -> {
                onBubbleClick(model.getMessage(), chat_iv_voice, chat_iv_unread_voice);
            });
            if (model.getMessage().direct() == EMMessage.Direct.RECEIVE) {
                if (model.getMessage().isListened()) {
                    // hide the unread icon
                    helper.setGone(R.id.chat_iv_unread_voice, false);
                } else {
                    helper.setGone(R.id.chat_iv_unread_voice, true);
                }
            }
            helper.addOnClickListener(R.id.ic_user);
        }
    }

    protected void createPicChatView(BaseViewHolder helper, ChatAdapterItemTypeBean item) {
        if (item != null && item.object != null) {
            ChatAdapterHolderbean model = (ChatAdapterHolderbean) item.object;
            timeShow(helper, model);

            EMImageMessageBody body = (EMImageMessageBody) model.getMessage().getBody();

            ImageView ic_user = helper.getView(R.id.ic_user);
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

            ImageLoaderConfig config = null;
            if (config == null) {
                config = new ImageLoaderConfig.Builder().
                        setAsBitmap(true).
                        setPlaceHolderResId(R.drawable.im_avatar).
                        setErrorResId(R.drawable.im_avatar).
                        setDiskCacheStrategy(ImageLoaderConfig.DiskCache.SOURCE).
                        setPrioriy(ImageLoaderConfig.LoadPriority.HIGH).build();
            }

            if (file.exists()) {
                ImageLoader.loadImage(helper.getView(R.id.chat_iv_pic), body.getLocalUrl(), config, null);
            } else {
                ImageLoader.loadImage(helper.getView(R.id.chat_iv_pic), body.getRemoteUrl(), config, null);
            }


            helper.getView(R.id.layout_send_pic).setOnClickListener(v -> {
                Intent intent = new Intent(mContext, PicBigActivity.class);
                intent.putExtra("localUrl", body.getLocalUrl());
                intent.putExtra("remoteUrl", body.getRemoteUrl());

                mContext.startActivity(intent);

            });
            helper.addOnClickListener(R.id.ic_user);
        }
    }

    /**
     * 1.洗剪吹 2.洗吹 3.烫发 4.染发 5.护理 6.接发 7.洗色 8.拉直 9.单项套餐 10.多项套餐
     * 101.分享门店 102.分享作品 103.分享美发师 110.推荐好友  111.邀请平台美发师加入 112.邀请门店美发师加入  113平台美发师加入申请 114门店美发师加入申请
     *
     * @param type
     * @return
     */
    protected String getLabelType(int type) {
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
            case 101:
                return "分享门店";
            case 102:
                return "分享作品";
            case 103:
                return "分享美发师";
            case 110:
                return "推荐好友";
            case 111:
                return "邀请平台美发师加入";
            case 112:
                return "邀请店内美发师加入";
            case 113:
                return "平台美发师加入申请";
            case 114:
                return "门店美发师加入申请";
        }
        return null;
    }

    /**
     * 时间显示 消息间隔超过30秒展示时间
     *
     * @param helper
     * @param model
     */
    protected void timeShow(BaseViewHolder helper, ChatAdapterHolderbean model) {
        if (helper != null && model != null) {
            TextView timestamp = helper.getView(R.id.timestamp);
            if (timestamp != null) {
                if (helper.getLayoutPosition() == 0) {
                    timestamp.setText(DateUtils.getTimestampString(new Date(model.getMessage().getMsgTime())));
                    timestamp.setVisibility(View.VISIBLE);
                } else {
                    // show time stamp if interval with last message is > 30 seconds
                    ChatAdapterItemTypeBean tempPre = this.mData.get(helper.getLayoutPosition() - 1);
                    if (tempPre != null) {
                        ChatAdapterHolderbean modelPre = (ChatAdapterHolderbean) tempPre.object;
                        if (modelPre != null) {
                            EMMessage prevMessage = modelPre.getMessage();
                            if (prevMessage != null && DateUtils.isCloseEnough(model.getMessage().getMsgTime(), prevMessage.getMsgTime())) {
                                timestamp.setVisibility(View.GONE);
                            } else {
                                timestamp.setText(DateUtils.getTimestampString(new Date(model.getMessage().getMsgTime())));
                                timestamp.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 语音消息点击播放逻辑
     *
     * @param message
     * @param chat_iv_voice
     * @param chat_iv_unread_voice
     */
    public void onBubbleClick(final EMMessage message, ImageView chat_iv_voice, ImageView chat_iv_unread_voice) {
        String msgId = message.getMsgId();

        if (voicePlayer.isPlaying()) {
            // Stop the voice play first, no matter the playing voice item is this or others.
            voicePlayer.stop();
            // Stop the voice play animation.
            stopVoicePlayAnimation(message, chat_iv_voice, chat_iv_unread_voice);

            // If the playing voice item is this item, only need stop play.
            String playingId = voicePlayer.getCurrentPlayingId();
            if (msgId.equals(playingId)) {
                return;
            }
        }

        if (message.direct() == EMMessage.Direct.SEND) {
            // Play the voice
            String localPath = ((EMVoiceMessageBody) message.getBody()).getLocalUrl();
            File file = new File(localPath);
            if (file.exists() && file.isFile()) {
                playVoice(message, chat_iv_voice, chat_iv_unread_voice);
                // Start the voice play animation.
                startVoicePlayAnimation(message, chat_iv_voice, chat_iv_unread_voice);
            } else {
                asyncDownloadVoice(message);
            }
        } else {
            final String st = mContext.getResources().getString(R.string.Is_download_voice_click_later);
            if (message.status() == EMMessage.Status.SUCCESS) {
                if (EMClient.getInstance().getOptions().getAutodownloadThumbnail()) {
                    play(message, chat_iv_voice, chat_iv_unread_voice);
                } else {
                    EMVoiceMessageBody voiceBody = (EMVoiceMessageBody) message.getBody();
//                    EMLog.i(TAG, "Voice body download status: " + voiceBody.downloadStatus());
                    switch (voiceBody.downloadStatus()) {
                        case PENDING:// Download not begin
                        case FAILED:// Download failed
//                            getChatRow().updateView(getMessage());
                            asyncDownloadVoice(message);
                            break;
                        case DOWNLOADING:// During downloading
                            Toast.makeText(mContext, st, Toast.LENGTH_SHORT).show();
                            break;
                        case SUCCESSED:// Download success
                            play(message, chat_iv_voice, chat_iv_unread_voice);
                            break;
                    }
                }
            } else if (message.status() == EMMessage.Status.INPROGRESS) {
                Toast.makeText(mContext, st, Toast.LENGTH_SHORT).show();
            } else if (message.status() == EMMessage.Status.FAIL) {
                Toast.makeText(mContext, st, Toast.LENGTH_SHORT).show();
                asyncDownloadVoice(message);
            }
        }
    }

    public void startVoicePlayAnimation(EMMessage message, ImageView chat_iv_voice, ImageView chat_iv_unread_voice) {
        if (message.direct() == EMMessage.Direct.RECEIVE) {
            chat_iv_voice.setImageResource(R.drawable.voice_from_icon);
        } else {
            chat_iv_voice.setImageResource(R.drawable.voice_to_icon);
        }
        voiceAnimation = (AnimationDrawable) chat_iv_voice.getDrawable();
        voiceAnimation.start();

        // Hide the voice item not listened status view.
        if (message.direct() == EMMessage.Direct.RECEIVE) {
            chat_iv_unread_voice.setVisibility(View.INVISIBLE);//未读或者已读红点
        }
    }


    public void stopVoicePlayAnimation(EMMessage message, ImageView chat_iv_voice, ImageView chat_iv_unread_voice) {
        if (voiceAnimation != null) {
            voiceAnimation.stop();
        }

        if (message.direct() == EMMessage.Direct.RECEIVE) {
            chat_iv_voice.setImageResource(R.drawable.im_chatfrom_voice_playing);
        } else {
            chat_iv_voice.setImageResource(R.drawable.im_chatto_voice_playing);
        }
    }

    private void play(EMMessage message, ImageView chat_iv_voice, ImageView chat_iv_unread_voice) {
        String localPath = ((EMVoiceMessageBody) message.getBody()).getLocalUrl();
        File file = new File(localPath);
        if (file.exists() && file.isFile()) {
            ackMessage(message, chat_iv_voice, chat_iv_unread_voice);
            playVoice(message, chat_iv_voice, chat_iv_unread_voice);
            // Start the voice play animation.
            startVoicePlayAnimation(message, chat_iv_voice, chat_iv_unread_voice);
        } else {
//            EMLog.e(TAG, "file not exist");
        }
    }

    private void ackMessage(EMMessage message, ImageView chat_iv_voice, ImageView chat_iv_unread_voice) {
        EMMessage.ChatType chatType = message.getChatType();
        if (!message.isAcked() && chatType == EMMessage.ChatType.Chat) {
            try {
                EMClient.getInstance().chatManager().ackMessageRead(message.getFrom(), message.getMsgId());
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
        }
        if (!message.isListened()) {
            EMClient.getInstance().chatManager().setVoiceMessageListened(message);
        }
    }


    private void playVoice(EMMessage msg, ImageView chat_iv_voice, ImageView chat_iv_unread_voice) {
        voicePlayer.play(msg, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // Stop the voice play animation.
                stopVoicePlayAnimation(msg, chat_iv_voice, chat_iv_unread_voice);
            }
        });
    }

    private void asyncDownloadVoice(final EMMessage message) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                EMClient.getInstance().chatManager().downloadAttachment(message);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
//                getChatRow().updateView(getMessage());
            }
        }.execute();
    }
}
