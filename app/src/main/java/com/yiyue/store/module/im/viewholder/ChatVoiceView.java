package com.yiyue.store.module.im.viewholder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.DateUtils;
import com.yiyue.store.R;
import com.yiyue.store.model.vo.bean.ChatAdapterHolderbean;
import com.yiyue.store.model.vo.bean.ChatAdapterItemTypeBean;
import com.yiyue.store.module.im.chat.chatview.ImChatRowVoicePlayer;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangzz on 2018/11/23
 */
public class ChatVoiceView {
    private Context mContext;
    private ImChatRowVoicePlayer voicePlayer;
    protected AnimationDrawable voiceAnimation;
    private BaseViewHolder helper;
    private ChatAdapterItemTypeBean item;
    private ImageView chat_iv_voice;
    private ImageView chat_iv_unread_voice;
    private List<ChatAdapterItemTypeBean> mData;

    public ChatVoiceView(Context context, BaseViewHolder helper, ChatAdapterItemTypeBean item, List<ChatAdapterItemTypeBean> datas) {
        this.mContext = context;
        this.helper = helper;
        this.item = item;
        voicePlayer = ImChatRowVoicePlayer.getInstance(mContext);
        createVoiceChatView();
    }

    private void createVoiceChatView() {
        if (item != null && item.object != null) {
            chat_iv_voice = helper.getView(R.id.chat_iv_voice);
            chat_iv_unread_voice = helper.getView(R.id.chat_iv_unread_voice);
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

    /**
     * 时间显示 消息间隔超过30秒展示时间
     * @param helper
     * @param model
     */
    private void timeShow(BaseViewHolder helper, ChatAdapterHolderbean model) {
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
}
