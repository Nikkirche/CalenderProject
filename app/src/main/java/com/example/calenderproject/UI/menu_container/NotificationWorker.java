package com.example.calenderproject.UI.menu_container;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.calenderproject.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TreeMap;

public class NotificationWorker extends Worker {
    private final String WORK_RESULT = "work_result";

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super( context, workerParams );
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            TreeMap<String, String> MapOfEvents = new TreeMap<>();
            TreeMap<String, String> test = new TreeMap<>();
            FileInputStream SmallDude = getApplicationContext().openFileInput( "events.txt" );
            InputStreamReader reader = new InputStreamReader( SmallDude );
            BufferedReader redr = new BufferedReader( reader );
            StringBuffer lover = new StringBuffer();
            String leaver = new String();
            String[] harassment = redr.readLine().split( "@@@@@@" );

            redr.reset();
            int lena = harassment.length;
            for (int j = 0; j < lena; j++) {
                String[] time = harassment[j].split( " " );
                int lena1 = time.length;
                String HourAndMin = new String();
                for (int k = 1; k < lena1; k++) {
                    HourAndMin = HourAndMin + " " + time[k];
                }


                test.put( HourAndMin, "." );

            }
            for (String key : test.keySet()) {
                String ReadableTime;
                String RedactTime[] = key.split( ":" );
                if (RedactTime[0].length() == 1) {
                    int lena3 = RedactTime.length;
                    ReadableTime = "0";
                    for (int j = 0; j < lena3; j++) {
                        ReadableTime = ReadableTime + RedactTime[j];
                    }

                } else {
                    ReadableTime = key;
                }
                String value = test.get( key );
                MapOfEvents.put( ReadableTime, value );
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String date = df.format( Calendar.getInstance().getTime());
                for (String key1:MapOfEvents.keySet()){
                    if (key1.contains( date )){
                        showNotification(date,date.substring( 15 ) );
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Data outputData = new Data.Builder().putString( WORK_RESULT, "Jobs Finished" ).build();
        return Result.success( outputData );
    }

    private void showNotification(String time, String text) {
        NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService( Context.NOTIFICATION_SERVICE );
        String channelId = "task_channel";
        String channelName = "task_name";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new
                    NotificationChannel( channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT );
            manager.createNotificationChannel( channel );
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder( getApplicationContext(), channelId )
                .setContentTitle( time )
                .setContentText( text )
                .setSmallIcon( R.mipmap.ic_launcher );
        manager.notify( 1, builder.build() );
    }
}