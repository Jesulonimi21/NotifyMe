package com.jesulonimi.user.notifyme;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.icu.util.ULocale;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.List;

import static com.jesulonimi.user.notifyme.App.CHANNEL_1_ID;
import static com.jesulonimi.user.notifyme.App.GROUP_1_ID;

public class MainActivity extends AppCompatActivity {
    private NotificationManagerCompat notificationManagerCompat;
    private EditText editTextTitle;
    private EditText editTextMessage;

    static List<Message> messageList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notificationManagerCompat = NotificationManagerCompat.from(this);
        editTextTitle = findViewById(R.id.text_title);
        editTextMessage = findViewById(R.id.text_message);


        messageList.add(new Message("Good Morning","Jim"));
        messageList.add(new Message("hello",null));
        messageList.add(new Message("hi","Jim"));
    }

    public void sendOnChannel1(View v) {
        if(!notificationManagerCompat.areNotificationsEnabled()){
            openNotificationSettings();
            return;
        }

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O&&isChannelBlocked(CHANNEL_1_ID)){
            openChannelSettings(CHANNEL_1_ID);
            return;
        }

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);

        String title = editTextTitle.getText().toString();
        String message = editTextMessage.getText().toString();

        Bitmap picture = BitmapFactory.decodeResource(getResources(), R.drawable.profile);

        Intent broadcastIntent = new Intent(this, NotificationReceiver.class);
         broadcastIntent.putExtra("toastMessage", message);
        PendingIntent actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(MainActivity.this, App.CHANNEL_1_ID).
                setSmallIcon(R.drawable.ic_looks_one_black_24dp)
                .setContentText(message)
                .setContentText(title)
                .setLargeIcon(picture)
//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .bigText("long dummy text")
//                        .setBigContentTitle("Bid content titel")
//                        .setSummaryText("Summary text"))
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(picture)
                        .bigLargeIcon(null)
                        .setBigContentTitle("This is a very importqnt title")
                            .setSummaryText("this is not to be taken for granted"))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
//                .setColor(Color.BLUE)
//                .addAction(R.mipmap.ic_launcher, "Toast", actionIntent)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE).build();
        notificationManagerCompat.notify(1, notification);
    }


    public void sendOnChannel2(View v) {
        String title = editTextTitle.getText().toString();
        String message = editTextMessage.getText().toString();
        Notification notification = new NotificationCompat.Builder(MainActivity.this, App.CHANNEL_2_ID).
                setSmallIcon(R.drawable.ic_mms_black_24dp)
                .setContentText(message)
                .setContentText(title)
                .setStyle(new NotificationCompat.InboxStyle()
                        .addLine(" line 1").addLine("Line 2").addLine("Line 3").addLine("Line 4")
                        .setBigContentTitle("Bid content titel")
                        .setSummaryText("Summary text"))

                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();
        notificationManagerCompat.notify(2, notification);
    }

    public void sendUsingMessagingStyle(View v){
        sendMessagingStyle(this);
    }
    public static void sendMessagingStyle(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, 0);



        Intent broadcastIntent = new Intent(context, NotificationReceiver.class);
        broadcastIntent.putExtra("toastMessage", "no message");
        PendingIntent actionIntent = PendingIntent.getBroadcast(context, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            android.support.v4.app.RemoteInput remoteInput=new    android.support.v4.app.RemoteInput.Builder("key_text")
                .setLabel("your reply").build();

            Intent replyIntent=new Intent(context,NotificationReceiver.class);
            PendingIntent replyPendingIntent=PendingIntent.getBroadcast(context,0,replyIntent,0);

            NotificationCompat.Action   action=new NotificationCompat.Action.Builder(
                    R.drawable.ic_send_black_24dp,
                    "send",
                    replyPendingIntent
            ).addRemoteInput(remoteInput).build();

            NotificationCompat.MessagingStyle messagingStyle=new NotificationCompat.MessagingStyle("usernsame");
            messagingStyle.setConversationTitle("Group name");

                    for(Message chatMessage:messageList){
                        NotificationCompat.MessagingStyle.Message message1=new NotificationCompat.MessagingStyle.Message(
                                chatMessage.getText(),chatMessage.getTimestamp(),chatMessage.getSender()
                        );
                        messagingStyle.addMessage(message1);
                    }
        Notification notification = new NotificationCompat.Builder(context, App.CHANNEL_1_ID).
                setSmallIcon(R.drawable.ic_looks_one_black_24dp)
                .addAction(action)
                .setColor(Color.GREEN)
                .setStyle(messagingStyle)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
//                .setColor(Color.BLUE)
//                .addAction(R.mipmap.ic_launcher, "Toast", actionIntent)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE).build();
                    NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(1, notification);
    }

    public void sendNotificationProgress(View v){
                final int progressMaax=100;
        final NotificationCompat.Builder notification = new NotificationCompat.Builder(MainActivity.this, App.CHANNEL_2_ID).
                setSmallIcon(R.drawable.ic_mms_black_24dp)
                .setContentTitle("Download")
                .setContentText("Download in progress")
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setStyle(new NotificationCompat.InboxStyle()
                        .addLine(" line 1").addLine("Line 2").addLine("Line 3").addLine("Line 4")
                        .setBigContentTitle("Bid content titel")
                        .setSummaryText("Summary text"))

                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setProgress(progressMaax,0,false);

        notificationManagerCompat.notify(2, notification.build());
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(2000);
                for(int progress=0;progress<=progressMaax;progress+=10){
                    notification.setProgress(progressMaax,progress,false);
                    notificationManagerCompat.notify(2,notification.build());
                    SystemClock.sleep(1000);
                }
                notification.setContentText("Download finished").setProgress(0,0,false).setOngoing(false);
                notificationManagerCompat.notify(2,notification.build());

            }
        }).start();

    }
    public void sendNotificationGroups(View v){
String title1="title1";
String title2="title2";
String message1="message1";
String message2="message2";

         Notification notification1 = new NotificationCompat.Builder(MainActivity.this, App.CHANNEL_2_ID).
                setSmallIcon(R.drawable.ic_mms_black_24dp)
                .setContentTitle(title1)
                .setContentText(message1)
                 .setGroup("Example_group")
                .setOnlyAlertOnce(true).build();


        Notification notification2 = new NotificationCompat.Builder(MainActivity.this, App.CHANNEL_2_ID).
                setSmallIcon(R.drawable.ic_mms_black_24dp)
                .setContentTitle(title2)
                .setContentText(message2)
                .setGroup("Example_group")
                .setOnlyAlertOnce(true).build();

        Notification summaryNotification = new NotificationCompat.Builder(MainActivity.this, App.CHANNEL_2_ID).
                setSmallIcon(R.drawable.ic_mms_black_24dp)
                .setStyle(new NotificationCompat.InboxStyle()
                        .addLine(title2+ " "+message2)
                .addLine(title1+" "+message2)
                .setBigContentTitle("two new messages"))
                .setGroupSummary(true)
                .setGroup("Example_group")
                .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
                .setOnlyAlertOnce(true).build();

            SystemClock.sleep(2000);
            notificationManagerCompat.notify(2,notification1);
        SystemClock.sleep(2000);
            notificationManagerCompat.notify(3,notification2);
        SystemClock.sleep(2000);
            notificationManagerCompat.notify(4,summaryNotification);
    }

    private void openNotificationSettings() {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            Intent i=new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            i.putExtra(Settings.EXTRA_APP_PACKAGE,getPackageName());
            startActivity(i);
        }else{
            Intent i=new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            i.setData(Uri.parse("package:"+getPackageName()));
            startActivity(i);
        }
    }
    @RequiresApi(26)
    private boolean isChannelBlocked(String channelId){
        NotificationManager notificationManager=getSystemService(NotificationManager.class);
        NotificationChannel notificationChannel=notificationManager.getNotificationChannel(channelId);

        return notificationChannel!=null&&notificationChannel.getImportance()==NotificationManager.IMPORTANCE_NONE;
    }
    @RequiresApi(26)
    public void openChannelSettings(String channelId){
        Intent i=new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
        i.putExtra(Settings.EXTRA_APP_PACKAGE,getPackageName());
        i.putExtra(Settings.EXTRA_CHANNEL_ID,channelId);
        startActivity(i);
    }

    public void deleteNotification(View v){
       if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
           NotificationManager manager=getSystemService(NotificationManager.class);
           manager.deleteNotificationChannel(CHANNEL_1_ID);
         //  manager.deleteNotificationChannelGroup(GROUP_1_ID);
       }



    }

    public void customNotification(View v){


        Intent broadcastIntent = new Intent(this, NotificationReceiver.class);
        broadcastIntent.putExtra("toastMessage", "no message");
        PendingIntent actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        RemoteViews colapsesView=new RemoteViews(getPackageName(),R.layout.collapsed_view);
        RemoteViews expandedView=new RemoteViews(getPackageName(),R.layout.expanded_view);
        colapsesView.setTextViewText(R.id.textViewCollapsed1,"Hello world");
        expandedView.setImageViewResource(R.id.imageViewExpanded,R.drawable.account);
        expandedView.setOnClickPendingIntent(R.id.imageViewExpanded,actionIntent);

        Notification customNotification=new NotificationCompat.Builder(this,CHANNEL_1_ID)
                .setSmallIcon(R.drawable.account)
                .setCustomContentView(colapsesView)
                .setCustomBigContentView(expandedView)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .build();

        notificationManagerCompat.notify(3,customNotification);
    }
}
// you can also learn about media style if you are interested