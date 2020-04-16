package com.example.calenderproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.calenderproject.fragments.CalenderFragment;
import com.example.calenderproject.fragments.RegisterFragment;
import com.example.calenderproject.fragments.SignInFragment;
import com.example.calenderproject.fragments.StartFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {


    SignInFragment SignInFragment;
    RegisterFragment RegisterFragment;
    StartFragment StartFragment;
    CalenderFragment CalenderFragment;
    private FirebaseAuth  firebaseAuth;
    @Override
    public void onStart() {
        // Check if user is signed in (non-null) and go to MainActivity then.
        super.onStart();
        firebaseAuth =FirebaseAuth.getInstance();
        if ( firebaseAuth.getCurrentUser()!=null) {
            GoToFragment( "CalenderFragment" );
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        StartFragment = new StartFragment();
        FragmentTransaction fstart = getSupportFragmentManager().beginTransaction();
        fstart.add( R.id.container, StartFragment ).commit();

    }

    public void GoToFragment(String fragment) {
        SignInFragment = new SignInFragment();
        RegisterFragment = new RegisterFragment();
        CalenderFragment = new CalenderFragment();
        StartFragment = new StartFragment();
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
            case "CalenderFragment":
                ftrans.replace( R.id.container, CalenderFragment);
                break;
        }
        ftrans.commit();
    }
}

