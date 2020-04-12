package com.example.calenderproject.firebase;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
public class AuthService {
    public static void signOut() {
        FirebaseAuth.getInstance()
                .signOut();
    }

    public static Task<AuthResult> signIn(final String email, String password, final String name) {
        return FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword( email, password )
                .addOnSuccessListener( new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        UserService.createNewUser( email, name );

                    }
                } );
    }

    public static Task<AuthResult> signIn(final String email, String password) {

        return FirebaseAuth.getInstance()
                .signInWithEmailAndPassword( email, password );
    }
}