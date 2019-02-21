package com.jesulonimi.user.notifyme;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {
    public static final String  GROUP_1_ID="GROUP1";
    public static final String GROUP_2_ID="GROUP2";

    public static final String CHANNEL_1_ID="channel1";
    public static final String CHANNEL_2_ID="CHANNEL2";
    public static final String CHANNEL_3_ID="CHANNEL3";
    public static final String CHANNEL_4_ID="CHANNEL4";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
    }

    public void createNotificationChannels(){
      if(Build. VERSION.SDK_INT>=Build.VERSION_CODES.O){

          NotificationChannelGroup group1=new NotificationChannelGroup(
                  GROUP_1_ID,
                  "GROUP 1"
          );
          NotificationChannelGroup group2=new NotificationChannelGroup(
                  GROUP_2_ID,
                  "GROUP 2"
          );
          NotificationChannel channel1=new NotificationChannel(
                  CHANNEL_1_ID,
                  "channel 1",
                  NotificationManager.IMPORTANCE_HIGH
          );
          channel1.setDescription("This is Channel 1");
          channel1.setGroup(GROUP_1_ID);
          NotificationChannel channel2=new NotificationChannel(
                  CHANNEL_2_ID,
                  "channel 2",
                  NotificationManager.IMPORTANCE_LOW
          );
          channel2.setDescription("This is Channel 2 ");
          channel2.setGroup(GROUP_1_ID);


          NotificationChannel channel3=new NotificationChannel(
                  CHANNEL_3_ID,
                  "channel 3",
                  NotificationManager.IMPORTANCE_HIGH
          );
          channel3.setDescription("This is Channel 3");
          channel3.setGroup(GROUP_2_ID);
          NotificationChannel channel4=new NotificationChannel(
                  CHANNEL_4_ID,
                  "channel 4",
                  NotificationManager.IMPORTANCE_LOW
          );
          channel4.setDescription("This is Channel 4 ");




          NotificationManager manager=getSystemService(NotificationManager.class);
                    manager.createNotificationChannelGroup(group1);
                    manager.createNotificationChannelGroup(group2);
                manager.createNotificationChannel(channel1);
          manager.createNotificationChannel(channel2);
          manager.createNotificationChannel(channel3);
          manager.createNotificationChannel(channel4);
      }
    }
}



