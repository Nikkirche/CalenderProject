package com.example.calenderproject.presenter;


import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.calenderproject.UI.StartFragment;
import com.example.calenderproject.models.StartModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class StartPresenter {
    StartFragment view;
    StartModel model;

    public StartPresenter(StartFragment view) {
        this.view = view;
        this.model = new StartModel();
    }

    public void CheckStatusOfUser() {
        if (model.StatusOfUser()) {
            view.GoToApp();
        }
    }

    public boolean isEmailCorrect(String email) {
        return email.contains( "@" ) && email.contains( "." ) && email.length() > 5;
    }

    public boolean isPasswordCorrect(String password) {
        return password.length() > 5;
    }

    public void signIn(String email, String password) {
        if (email.length() > 0 || password.length() > 0) {
            view.SignInAnimationStart();
            model.signIn( email, password )
                    .addOnFailureListener( e -> {
                        Toast.makeText( view.getContext(), "Can't log in: " + e.getMessage(), Toast.LENGTH_SHORT ).show();
                        view.SignInAnimationStop();
                    } ).addOnSuccessListener( new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    view.GoToApp();
                    FileOutputStream Stream = null;
                    try {
                        Stream = view.getContext().openFileOutput( "user.txt", Context.MODE_PRIVATE );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    model.GetSignData( Stream);
                }
            } );
        } else {
            Toast.makeText( view.getContext(), "Can't log in: You must write your  password and email!", Toast.LENGTH_SHORT ).show();
        }
    }

    public void register(String email, String password, String name) {
        if (!isEmailCorrect( email ) || !isPasswordCorrect( password )) {
            Toast.makeText( view.getContext(), "Wrong password or email", Toast.LENGTH_SHORT ).show();
        } else {
            view.RegisterInAnimationStart();
            model.signInEmailAndPassword( email, password, name )
                    .addOnFailureListener( new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText( view.getContext(), "Can't auth: " + e.getMessage(), Toast.LENGTH_SHORT ).show();
                            view.RegisterInAnimationStop();
                        }
                    } ).addOnSuccessListener( new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    FileOutputStream Stream = null;
                    try {
                        Stream = view.getContext().openFileOutput( "user.txt", Context.MODE_PRIVATE );
                        try {
                            model.writeUserData( Stream,name );
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            } );
        }
    }

    public void ChangedAuthStatus(String name, FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() != null && firebaseAuth.getCurrentUser().getDisplayName() == null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName( name )
                    .build();

            firebaseAuth.getCurrentUser()
                    .updateProfile( profileUpdates )
                    .addOnSuccessListener( new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            view.GoToLoadingFragment();
                        }
                    } );
        }
    }
}
