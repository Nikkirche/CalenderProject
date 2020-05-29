package com.example.calenderproject;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

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

        if (/*CheckInternet.isOnline( getBaseContext() ) &&*/ FirebaseAuth.getInstance().getCurrentUser()!=null) {
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
                            FileOutputStream BigDude = getApplicationContext().openFileOutput( "example.txt", Context.MODE_PRIVATE );
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

        return super.onStartCommand( intent, flags, startId );
    }


}
