package com.example.calenderproject;

import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;

import com.example.calenderproject.firebase.AuthService;
import com.example.calenderproject.fragments.AuthLoadingFragment;
import com.example.calenderproject.fragments.StartFragment;
import com.example.calenderproject.fragments.menu_container.InterfaceFragment;
import com.jaredrummler.cyanea.app.CyaneaAppCompatActivity;

public class MainActivity extends CyaneaAppCompatActivity {


    private StartFragment StartFragment;
    private InterfaceFragment InterfaceFragment;

    @Override
    public void onStart() {
        // Check if user is signed in (non-null) and go to MainActivity then.

        super.onStart();
        if (AuthService.CheckStatusOfUser()) {
            GoToFragment( "InterfaceFragment" );
        }
        else {
            StartFragment = new StartFragment();
            FragmentTransaction fstart = getSupportFragmentManager().beginTransaction();
            fstart.add( R.id.container, StartFragment ).commit();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
    }

    public void GoToFragment(String fragment) {
        StartFragment = new StartFragment();
        InterfaceFragment = new InterfaceFragment();
        AuthLoadingFragment authLoadingFragment = new AuthLoadingFragment();
        FragmentTransaction ftrans = getSupportFragmentManager().beginTransaction();
        switch (fragment) {
            case "StartFragment":
                ftrans.replace( R.id.container, StartFragment );
                break;
            case "InterfaceFragment":
                ftrans.replace( R.id.container, InterfaceFragment);
                break;
            case "AuthLoadingFragment":
                ftrans.replace( R.id.container,authLoadingFragment );
        }
        ftrans.commit();
    }
}

