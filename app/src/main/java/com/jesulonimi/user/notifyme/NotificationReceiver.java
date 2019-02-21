package com.jesulonimi.user.notifyme;

import android.app.RemoteInput;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import static com.jesulonimi.user.notifyme.MainActivity.messageList;
import static com.jesulonimi.user.notifyme.MainActivity.sendMessagingStyle;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT_WATCH) {
            Bundle remoteInput=RemoteInput.getResultsFromIntent(intent);


            if(remoteInput!=null){
                CharSequence reply=remoteInput.getCharSequence("key_text");
                Message message=new Message(reply,null);
                messageList.add(message);
                sendMessagingStyle(context);
            }
        }


    }
}
