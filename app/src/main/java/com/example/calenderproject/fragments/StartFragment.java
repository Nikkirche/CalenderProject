package com.example.calenderproject.fragments;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.calenderproject.MainActivity;
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

import java.util.Objects;
import java.util.concurrent.Executor;

public class StartFragment extends Fragment {


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_start, container, false );
        // Initialize Firebase Auth
        //Buttons
        final Button buttonToReg = view.findViewById( R.id.buttonToRegFromStart );
        final Button buttonToSign = view.findViewById( R.id.buttonToSignInFromStart );
        final MainActivity act = (MainActivity) getActivity();
        buttonToReg.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) { Objects.requireNonNull( act ).GoToFragment( "RegisterFragment" );
            }
        } );
        buttonToSign.setOnClickListener( new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) { act.GoToFragment( "SignInFragment" );
                                             }
                                         }
        );
        return view;

    }

}

