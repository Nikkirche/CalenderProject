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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_register );

        //Buttons
        Button buttonReg = findViewById( R.id.buttonRegister );
        ImageButton buttonToStart = findViewById( R.id.buttonToStartFromReg );
        //Password & Email & Name
        final EditText regName = findViewById( R.id.editNameReg );
        final EditText regEmail = findViewById( R.id.editEmailReg );
        final EditText regPassword = findViewById( R.id.editPasswordReg );


        FirebaseAuth.getInstance()
                .addAuthStateListener(new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        if (firebaseAuth.getCurrentUser() != null && firebaseAuth.getCurrentUser().getDisplayName() == null) {
                            final String name = regName.getText().toString();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build();

                            firebaseAuth.getCurrentUser()
                                    .updateProfile(profileUpdates)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            goToMainActivity();
                                        }
                                    });
                        }

                    }
                });
        buttonToStart.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToStartActivity();
            }
        } );
        buttonReg.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = regEmail.getText().toString();
                String password = regPassword.getText().toString();
                final String name = regName.getText().toString();

                if (!isEmailCorrect( email ) || !isPasswordCorrect( password )) {
                    Toast.makeText( v.getContext(), "Wrong password or email", Toast.LENGTH_SHORT ).show();
                    return;
                }

                AuthService.signInEmailAndPasword( email, password, name )
                        .addOnFailureListener( new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText( getBaseContext(), "Can't auth: " + e.getMessage(), Toast.LENGTH_SHORT ).show();
                            }
                        } );
            }
        });
    }

    private void goToStartActivity() {
        Intent intent = new Intent( RegisterActivity.this, StartActivity.class );
        startActivity( intent );
        finish();
    }


    private boolean isEmailCorrect(String email) {
        return email.contains( "@" ) && email.contains( "." ) && email.length() > 5;
    }

    private boolean isPasswordCorrect(String password) {
        return password.length() > 5;
    }
    private void goToMainActivity() {
        Intent intent = new Intent( RegisterActivity.this, MainActivity.class );
        startActivity( intent );
        finish();
    }
}