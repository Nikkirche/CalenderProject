package com.example.calenderproject;

import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;

import com.example.calenderproject.fragments.AuthLoadingFragment;
import com.example.calenderproject.fragments.StartFragment;
import com.example.calenderproject.fragments.menu_container.InterfaceFragment;
import com.jaredrummler.cyanea.app.CyaneaAppCompatActivity;

public class MainActivity extends CyaneaAppCompatActivity {


    private StartFragment StartFragment;

    @Override
    public void onStart() {
        // Check if user is signed in (non-null) and go to MainActivity then.

        super.onStart();

            StartFragment = new StartFragment();
            FragmentTransaction fstart = getSupportFragmentManager().beginTransaction();
            fstart.add( R.id.container, StartFragment ).commit();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
    }

    public void GoToFragment(String fragment) {
        InterfaceFragment interfaceFragment = new InterfaceFragment();
        AuthLoadingFragment authLoadingFragment = new AuthLoadingFragment();
        FragmentTransaction ftrans = getSupportFragmentManager().beginTransaction();
        switch (fragment) {
            case "InterfaceFragment":
                ftrans.replace( R.id.container, interfaceFragment );
                break;
            case "AuthLoadingFragment":
                ftrans.replace( R.id.container, authLoadingFragment );
        }
        ftrans.commit();
    }
}

