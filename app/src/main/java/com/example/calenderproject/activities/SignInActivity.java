package com.example.calenderproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.calenderproject.R;
import com.example.calenderproject.firebase.AuthService;
import com.google.android.gms.tasks.OnFailureListener;

public  class SignInActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_sign_in );

        //Buttons
        Button buttonSignIn = findViewById( R.id.buttonSignIn );
        ImageButton buttonToStart =findViewById( R.id.buttonToStartFromSign );

        final EditText editSignEmail = findViewById( R.id.editSignEmail );
        final EditText editSignPassword = findViewById( R.id.editSignPassword );

        buttonToStart.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToStartActivity();
            }
        } );
        buttonSignIn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editSignEmail.getText().toString();
                String password = editSignPassword.getText().toString();
                if(email.length()>0 || password.length() > 0) {
                    AuthService.signIn( email, password )
                            .addOnFailureListener( new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText( getBaseContext(), "Can't log in: " + e.getMessage(), Toast.LENGTH_SHORT ).show();
                                }
                            } );
                }
                else{
                    Toast.makeText( getBaseContext(), "Can't log in: You must write your  password and email!" , Toast.LENGTH_SHORT ).show();
                }
            }
        } );

    }

    private void goToStartActivity() {
        Intent Intent = new Intent( com.example.calenderproject.activities.SignInActivity.this, StartActivity.class );
        startActivity( Intent );
        finish();
    }

    private void goToMainActivity() {
        Intent Intent = new Intent( com.example.calenderproject.activities.SignInActivity.this, MainActivity.class );
        startActivity( Intent );
        finish();
    }
}
