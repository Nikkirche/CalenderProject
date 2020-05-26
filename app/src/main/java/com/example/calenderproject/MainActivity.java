package com.example.calenderproject;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.calenderproject.fragments.AuthLoadingFragment;
import com.example.calenderproject.fragments.StartFragment;
import com.example.calenderproject.fragments.menu_container.InterfaceFragment;
import com.jaredrummler.cyanea.app.CyaneaAppCompatActivity;

public class MainActivity extends CyaneaAppCompatActivity {


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
    protected void onCreate(Bundle savedInstanceState) {
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



