package com.example.calenderproject.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.calenderproject.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class StartActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and go to MainActivity then.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (currentUser != null && account != null) {
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
        SignInButton buttonSignIn =findViewById( R.id.buttonGoogleSign );


        GoogleSignInOptions gso = new GoogleSignInOptions . Builder ( GoogleSignInOptions . DEFAULT_SIGN_IN )
                .requestEmail()
                .build ();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        buttonSignIn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        } );
        buttonToReg.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegActivity();
            }
        } );
        buttonToSign.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, SignInActivity.class);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StartActivity.this, v, "SignIn");
                Bundle bundle = options.toBundle();
                startActivity(intent, bundle);
            }
        }
        );

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult( ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }
    // [END auth_with_google]

    // [START signin]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    //Functions
    private void goToRegActivity() {
        Intent intent = new Intent( StartActivity.this, RegisterActivity.class );
        startActivity( intent ,ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        finish();
    }


    private void goToMainActivity() {
        Intent intent = new Intent( StartActivity.this, MainActivity.class );
        startActivity( intent );
        finish();
    }
}

