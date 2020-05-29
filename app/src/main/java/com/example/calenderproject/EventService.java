package com.example.calenderproject;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TreeMap;

public class EventService extends Service {
    private static final int NOTIF_ID = 1;
    private static final String NOTIF_CHANNEL_ID = "Channel_Id";
    HashMap<String, HashMap<String, String>> map;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel( CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT );

            ((NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE )).createNotificationChannel( channel );

            Notification notification = new NotificationCompat.Builder( this, CHANNEL_ID )
                    .setContentTitle( "" )
                    .setContentText( "" ).build();

            startForeground( 1, notification );
        }

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Thread WriteThread = new Thread( new Runnable() {
            @Override
            public void run() {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference( "users" ).child( firebaseUser.getUid() );
                    ref.addValueEventListener( new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild( "events" )) {
                                map = (HashMap<String, HashMap<String, String>>) dataSnapshot.child( "events" ).getValue();
                                Log.e( "testService", map.toString() );
                                String infadata = "";
                                for (String yep : map.keySet()) {
                                    HashMap<String, String> minimap = map.get( yep );
                                    for (String yep1 : minimap.keySet()) {
                                        infadata = infadata + minimap.get( yep1 ) + "      " + yep1 + "@@@@@@";

                                    }

                                }
                                Log.e( "testServiceWrite", infadata );
                                try {
                                    FileOutputStream BigDude = getApplicationContext().openFileOutput( "events.txt", Context.MODE_PRIVATE );
                                    BigDude.flush();
                                    BigDude.write( infadata.getBytes() );
                                    BigDude.close();
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }

                        @SuppressLint("CheckResult")
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            //Toasty.error( getContext(),R.string.no_internet  );
                        }
                    } );

                }
            }


        });
        Thread NotificationThread = new Thread( new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep( 1200 );
                    while (true) {
                        TreeMap<String, String> MapOfEvents = new TreeMap<>();
                        TreeMap<String, String> test = new TreeMap<>();
                        FileInputStream SmallDude = getApplicationContext().openFileInput( "events.txt" );
                        InputStreamReader reader = new InputStreamReader( SmallDude );
                        BufferedReader redr = new BufferedReader( reader );
                        StringBuffer lover = new StringBuffer();
                        String leaver = new String();
                        String[] harassment = redr.readLine().split( "@@@@@@" );

                        //redr.reset();
                        int lena = harassment.length;
                        for (int j = 0; j < lena; j++) {
                            test.put(harassment[j], "." );

                        }
                        Log.e( "testNotifications", "alive?" );

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
                        }
                        DateFormat df = new SimpleDateFormat( "yyyy-MM-dd HH:mm" );
                        String date = df.format( Calendar.getInstance().getTime() );
                        Log.e( "testLocalData",date);
                        for (String key1 : MapOfEvents.keySet()) {
                            Log.e( "test",key1 );
                            if (key1.contains( date )) {
                                showNotification( date, date.substring( 15 ) );
                            }
                        }
                        Thread.sleep( 60000 );
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                    Log.e( "error","broke" );
                }
            }
        } );
        WriteThread.start();
        NotificationThread.start();
        return super.onStartCommand( intent, flags, startId );


    }

    private void showNotification(String time, String text) {
        Intent intent = new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
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
                .setContentIntent(pendingIntent)
                .setSmallIcon( R.mipmap.ic_launcher );
        manager.notify( 1, builder.build() );
    }
}
