package com.yiyue.store.module.im.viewholder;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.yiyue.store.R;
import com.yiyue.store.module.im.chat.chatview.ImChatRowVoicePlayer;

import java.io.File;

/**
 * Created by zhangzz on 2018/10/10
 */
public abstract class BaseVoiceAdapter extends BaseAdapter {
    private Context mContext;

    private ImChatRowVoicePlayer voicePlayer;
    protected ImageView chat_iv_voice;
    protected ImageView chat_iv_unread_voice;
    protected AnimationDrawable voiceAnimation;


    public BaseVoiceAdapter(View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        voicePlayer = ImChatRowVoicePlayer.getInstance(mContext);
        chat_iv_voice = itemView.findViewById(R.id.chat_iv_voice);
        chat_iv_unread_voice = itemView.findViewById(R.id.chat_iv_unread_voice);
    }


    public void startVoicePlayAnimation(EMMessage message) {
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


    public void stopVoicePlayAnimation(EMMessage message) {
        if (voiceAnimation != null) {
            voiceAnimation.stop();
        }

        if (message.direct() == EMMessage.Direct.RECEIVE) {
            chat_iv_voice.setImageResource(R.drawable.im_chatfrom_voice_playing);
        } else {
            chat_iv_voice.setImageResource(R.drawable.im_chatto_voice_playing);
        }
    }

    public void onBubbleClick(final EMMessage message) {
        String msgId = message.getMsgId();

        if (voicePlayer.isPlaying()) {
            // Stop the voice play first, no matter the playing voice item is this or others.
            voicePlayer.stop();
            // Stop the voice play animation.
            stopVoicePlayAnimation(message);

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
                playVoice(message);
                // Start the voice play animation.
                startVoicePlayAnimation(message);
            } else {
                asyncDownloadVoice(message);
            }
        } else {
            final String st = mContext.getResources().getString(R.string.Is_download_voice_click_later);
            if (message.status() == EMMessage.Status.SUCCESS) {
                if (EMClient.getInstance().getOptions().getAutodownloadThumbnail()) {
                    play(message);
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
                            play(message);
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

    private void play(EMMessage message) {
        String localPath = ((EMVoiceMessageBody) message.getBody()).getLocalUrl();
        File file = new File(localPath);
        if (file.exists() && file.isFile()) {
            ackMessage(message);
            playVoice(message);
            // Start the voice play animation.
            startVoicePlayAnimation(message);
        } else {
//            EMLog.e(TAG, "file not exist");
        }
    }

    private void ackMessage(EMMessage message) {
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


    private void playVoice(EMMessage msg) {
        voicePlayer.play(msg, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // Stop the voice play animation.
                stopVoicePlayAnimation(msg);
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
