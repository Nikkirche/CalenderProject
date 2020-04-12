package com.example.calenderproject.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.calenderproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and go to MainActivity then.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            goToMainActivity();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_start );
        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        //Buttons
        final Button buttonToReg = findViewById( R.id.buttonToRegFromStart );
        final Button buttonToSign = findViewById( R.id.buttonToSignInFromStart );
        //ButtonsActivities
        buttonToReg.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegActivity();
            }
        } );
        buttonToSign.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSignActivity();
            }
        } );

    }

    private void goToRegActivity() {
        Intent intent = new Intent( StartActivity.this, RegisterActivity.class );
        startActivity( intent ,ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        finish();
    }


    private void goToSignActivity() {
        Intent intent = new Intent( StartActivity.this, SignInActivity.class );
        startActivity( intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        finish();
    }

    private void goToMainActivity() {
        Intent intent = new Intent( StartActivity.this, MainActivity.class );
        startActivity( intent );
        finish();
    }
}

