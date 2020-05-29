package com.example.calenderproject;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.calenderproject.UI.AuthLoadingFragment;
import com.example.calenderproject.UI.StartFragment;
import com.example.calenderproject.UI.menu_container.InterfaceFragment;
import com.jaredrummler.cyanea.app.CyaneaAppCompatActivity;

public class MainActivity extends CyaneaAppCompatActivity {

    private PowerManager.WakeLock wl;
    private StartFragment StartFragment;
    @Override
    public void onStart() {
        super.onStart();
        StartFragment = new StartFragment();
        FragmentTransaction fstart = getSupportFragmentManager().beginTransaction();
        fstart.add( R.id.container, StartFragment ).commit();
        createEventNotificationChannel();
    }

    @Override
    protected void onStop() {
        super.onStop();
        wl.release();
    }
    @Override
    protected void onResume() {
        super.onResume();
        wl.acquire(10*60*1000L /*10 minutes*/);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PowerManager pm = (PowerManager) getSystemService( Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "CalenderProject:MainActivity");
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

    }


    public void GoToFragment(@NonNull String fragment) {
        InterfaceFragment interfaceFragment = new InterfaceFragment();
        AuthLoadingFragment authLoadingFragment = new AuthLoadingFragment();
        StartFragment startFragment = new StartFragment();
        FragmentTransaction ftrans = getSupportFragmentManager().beginTransaction();
        switch (fragment) {
            case "InterfaceFragment":
                ftrans.replace( R.id.container, interfaceFragment );
                break;
            case "AuthLoadingFragment":
                ftrans.replace( R.id.container, authLoadingFragment );
                break;
            case "StartFragment":
                ftrans.replace( R.id.container, startFragment);
                break;
        }
        ftrans.commit();
    }

    private void createEventNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString( R.string.channel_name );
            String description = getString( R.string.channel_description );
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel( "Events", name, importance );
            channel.setDescription( description );
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService( NotificationManager.class );
            notificationManager.createNotificationChannel( channel );
        }
    }
    public void createEventNotification(){
        Notification newEventNotification = new NotificationCompat.Builder( this.getBaseContext(), "Events" )
                //.setSmallIcon(R.drawable.new_mail)
                .setContentTitle( "test" )
                //.setContentText(emailObject.getSubject())
                //.setLargeIcon(emailObject.getSenderAvatar())
                .build();
    }

}



