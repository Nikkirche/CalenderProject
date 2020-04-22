package com.example.calenderproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.calenderproject.firebase.AuthService;
import com.example.calenderproject.fragments.menu_container.InterfaceFragment;
import com.example.calenderproject.fragments.RegisterFragment;
import com.example.calenderproject.fragments.SignInFragment;
import com.example.calenderproject.fragments.StartFragment;

public class MainActivity extends AppCompatActivity {


    SignInFragment SignInFragment;
    RegisterFragment RegisterFragment;
    StartFragment StartFragment;
    InterfaceFragment InterfaceFragment;

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
        SignInFragment = new SignInFragment();
        RegisterFragment = new RegisterFragment();
        StartFragment = new StartFragment();
        InterfaceFragment = new InterfaceFragment();
        FragmentTransaction ftrans = getSupportFragmentManager().beginTransaction();
        switch (fragment) {
            case "SignInFragment":
                ftrans.replace( R.id.container, SignInFragment );
                break;
            case "StartFragment":
                ftrans.replace( R.id.container, StartFragment );
                break;
            case "RegisterFragment":
                ftrans.replace( R.id.container, RegisterFragment );
                break;
            case "InterfaceFragment":
                ftrans.replace( R.id.container, InterfaceFragment);
                break;
        }
        ftrans.commit();
    }
}

