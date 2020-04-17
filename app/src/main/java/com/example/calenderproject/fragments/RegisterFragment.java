package com.example.calenderproject.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.calenderproject.MainActivity;
import com.example.calenderproject.R;
import com.example.calenderproject.firebase.AuthService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterFragment extends Fragment {

    private FirebaseAuth firebaseAuth;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final MainActivity act = (MainActivity) getActivity();
        View view = inflater.inflate( R.layout.fragment_register, container, false );
        //Buttons
        Button buttonReg = view.findViewById( R.id.buttonRegister );
        ImageButton buttonToStart = view.findViewById( R.id.buttonToStartFromReg );
        //Password & Email & Name
        final EditText regName = view.findViewById( R.id.editNameReg );
        final EditText regEmail = view.findViewById( R.id.editEmailReg );
        final EditText regPassword = view.findViewById( R.id.editPasswordReg );


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
                                            act.GoToFragment( "CalenderFragment" );
                                            act.GoToFragment( "InterfaceFragment" );
                                        }
                    });
                        }

                    }
                });
        buttonToStart.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                act.GoToFragment( "StartFragment" );
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
                                Toast.makeText( getActivity(), "Can't auth: " + e.getMessage(), Toast.LENGTH_SHORT ).show();
                            }
                        } );
            }
        });
        return view;
    }


    private boolean isEmailCorrect(String email) {
        return email.contains( "@" ) && email.contains( "." ) && email.length() > 5;
    }

    private boolean isPasswordCorrect(String password) {
        return password.length() > 5;
    }
}