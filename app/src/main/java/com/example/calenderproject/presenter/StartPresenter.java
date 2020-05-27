package com.example.calenderproject.presenter;


import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.calenderproject.fragments.StartFragment;
import com.example.calenderproject.fragments.StartModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

public class StartPresenter {
    StartFragment view;
    StartModel model;
    public StartPresenter(StartFragment view) {
        this.view = view;
        this.model = new StartModel();
    }

    public  void CheckStatusOfUser() {
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
    public  void   signIn(String email,String password){
        if (email.length() > 0 || password.length() > 0) {
            model.signIn( email, password )
                    .addOnFailureListener( e -> {
                        Toast.makeText( view.getContext(), "Can't log in: " + e.getMessage(), Toast.LENGTH_SHORT ).show();
                        view.SignInAnimationStop();
                    } ).addOnSuccessListener( authResult -> view.GoToApp() );
        } else {
            Toast.makeText( view.getContext(), "Can't log in: You must write your  password and email!", Toast.LENGTH_SHORT ).show();
            view.SignInAnimationStop();
        }
    }

    public void register(String email, String password, String name) {
        if (!isEmailCorrect( email ) || !isPasswordCorrect( password )) {
            Toast.makeText( view.getContext(), "Wrong password or email", Toast.LENGTH_SHORT ).show();
            view.RegisterInAnimationStop();
        }
        else {
            model.signInEmailAndPassword( email, password, name )
                    .addOnFailureListener( new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText( view.getContext(), "Can't auth: " + e.getMessage(), Toast.LENGTH_SHORT ).show();
                            view.RegisterInAnimationStop();
                        }
                    } );
        }
    }
    public void ChangedAuthStatus(String name, FirebaseAuth firebaseAuth){
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
