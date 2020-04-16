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
import com.google.firebase.auth.AuthResult;

public class SignInFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_sign_in, container, false );
        Button buttonSignIn = view.findViewById( R.id.buttonSignIn );
        ImageButton buttonToStart = view.findViewById( R.id.buttonToStartFromSign );

        final EditText editSignEmail = view.findViewById( R.id.editSignEmail );
        final EditText editSignPassword = view.findViewById( R.id.editSignPassword );

        final MainActivity act = (MainActivity) getActivity();

        buttonToStart.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                act.GoToFragment( "StartFragment" );
            }
        } );
        buttonSignIn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editSignEmail.getText().toString();
                String password = editSignPassword.getText().toString();
                if (email.length() > 0 || password.length() > 0) {
                    AuthService.signIn( email, password )
                            .addOnFailureListener( new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText( getContext(), "Can't log in: " + e.getMessage(), Toast.LENGTH_SHORT ).show();
                                }
                            } ).addOnSuccessListener( new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                    act.GoToFragment( "CalenderFragment" );
                        }
                    } );
                } else {
                    Toast.makeText( getContext(), "Can't log in: You must write your  password and email!", Toast.LENGTH_SHORT ).show();
                }
            }
        } );

        return view;
    }

}
