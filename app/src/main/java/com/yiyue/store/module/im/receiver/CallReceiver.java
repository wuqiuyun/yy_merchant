package com.yiyue.store.module.im.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hyphenate.util.EMLog;
import com.yiyue.store.module.im.voicecall.VideoCallActivity;
import com.yiyue.store.module.im.voicecall.VoiceCallActivity;
import com.yiyue.store.util.easyutils.EasyHelper;

public class CallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!EasyHelper.getInstance().isLoggedIn())
            return;
        //username
        String from = intent.getStringExtra("from");
        //call type
        String type = intent.getStringExtra("type");

        if ("video".equals(type)) { //video call
//		    context.startActivity(new Intent(context, VideoCallActivity.class).
//                    putExtra("username", from).putExtra("isComingCall", true).
//                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } else { //voice call
            context.startActivity(new Intent(context, VoiceCallActivity.class).
                    putExtra("username", from).putExtra("isComingCall", true).
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
        EMLog.d("CallReceiver", "app received a incoming call");
    }

}
