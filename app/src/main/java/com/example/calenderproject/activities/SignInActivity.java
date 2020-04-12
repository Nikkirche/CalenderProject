package com.example.calenderproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
        Button buttonToRegFromSign = findViewById( R.id.buttonToRegFromSign );

        final EditText editSignEmail = findViewById( R.id.editSignEmail );
        final EditText editSignPassword = findViewById( R.id.editSignPassword );
        buttonToRegFromSign.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegActivity();
            }
        } );
        buttonSignIn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editSignEmail.getText().toString();
                String password = editSignPassword.getText().toString();

                AuthService.signIn( email, password )
                        .addOnFailureListener( new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText( getBaseContext(), "Can't log in: " + e.getMessage(), Toast.LENGTH_SHORT ).show();
                            }
                        } );
            }
        } );

    }

    private void goToMainActivity() {
        Intent Intent = new Intent( com.example.calenderproject.activities.SignInActivity.this, MainActivity.class );
        startActivity( Intent );
        finish();
    }

    private void goToRegActivity() {
        Intent Intent = new Intent( com.example.calenderproject.activities.SignInActivity.this, RegisterActivity.class );
        startActivity( Intent );
        finish();
    }
}
