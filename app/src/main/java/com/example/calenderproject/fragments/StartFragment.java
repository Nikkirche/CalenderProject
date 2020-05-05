package com.example.calenderproject.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.calenderproject.MainActivity;
import com.example.calenderproject.MorphAnimation;
import com.example.calenderproject.R;
import com.example.calenderproject.firebase.AuthService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;


public class StartFragment extends Fragment {
    private MorphAnimation morphAnimationLogin;
    private MorphAnimation morphAnimationRegister;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_start, container, false );

        View loginContainer = view.findViewById( R.id.form_login );
        View registerContainer = view.findViewById( R.id.form_register );
        final ViewGroup InfoTextViews =view.findViewById( R.id.info_form );

        final ViewGroup loginViews = (ViewGroup) view.findViewById( R.id.login_views );
        final ViewGroup registerViews = (ViewGroup) view.findViewById( R.id.register_views );
        // Initialize Firebase Auth
        //Buttons
        final Button buttonToReg = view.findViewById( R.id.buttonToRegFromStart );
        final Button buttonToSign = view.findViewById( R.id.buttonToSignInFromStart );
        final Button buttonReg = view.findViewById( R.id.buttonRegister );
        final Button buttonSignIn = view.findViewById( R.id.buttonSignIn);

        final EditText regEmail = view.findViewById( R.id.editEmailReg );
        final EditText regName = view.findViewById( R.id.editNameReg );
        final EditText regPassword = view.findViewById( R.id.editPasswordReg );
        final EditText editSignEmail = view.findViewById( R.id.editSignEmail );
        final EditText editSignPassword = view.findViewById( R.id.editSignPassword );
        final FrameLayout layout = (FrameLayout) view.findViewById( R.id.frame );
        final MainActivity act = (MainActivity) getActivity();
        FirebaseAuth.getInstance()
                .addAuthStateListener( new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        if (firebaseAuth.getCurrentUser() != null && firebaseAuth.getCurrentUser().getDisplayName() == null) {
                            final String name = regName.getText().toString();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName( name )
                                    .build();

                            firebaseAuth.getCurrentUser()
                                    .updateProfile( profileUpdates )
                                    .addOnSuccessListener( new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            act.GoToFragment( "InterfaceFragment" );
                                        }
                                    } );
                        }

                    }
                } );
        morphAnimationLogin = new MorphAnimation( loginContainer, layout, loginViews );
        morphAnimationRegister = new MorphAnimation( registerContainer, layout, registerViews );
        buttonToReg.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!morphAnimationRegister.isPressed()) {
                    loginViews.setVisibility( View.GONE);
                    InfoTextViews.setVisibility( View.GONE );
                    morphAnimationRegister.morphIntoForm();
                    buttonToReg.setText( R.string.back );
                } else {
                    morphAnimationRegister.morphIntoButton();
                    InfoTextViews.setVisibility( View.VISIBLE );
                    buttonToReg.setText( R.string.register);
                    loginViews.setVisibility( View.VISIBLE);
                }
            }
        } );
        buttonToSign.setOnClickListener( new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 if (!morphAnimationLogin.isPressed()) {
                                                     registerViews.setVisibility( View.GONE);
                                                     InfoTextViews.setVisibility( View.GONE );
                                                     morphAnimationLogin.morphIntoForm();
                                                     buttonToSign.setText( R.string.back );
                                                 } else {
                                                     morphAnimationLogin.morphIntoButton();
                                                     InfoTextViews.setVisibility( View.VISIBLE );
                                                     buttonToSign.setText( R.string.sign_in);
                                                    registerViews.setVisibility( View.VISIBLE);
                                                 }
                                                 ;
                                             }
                                         }
        );
        buttonReg.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonReg.setClickable( false );
                String email = regEmail.getText().toString();
                String password = regPassword.getText().toString();
                final String name = regName.getText().toString();

                if (!isEmailCorrect( email ) || !isPasswordCorrect( password )) {
                    Toast.makeText( v.getContext(), "Wrong password or email", Toast.LENGTH_SHORT ).show();
                    buttonReg.setClickable( true );
                }

                AuthService.signInEmailAndPasword( email, password, name )
                        .addOnFailureListener( new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText( getActivity(), "Can't auth: " + e.getMessage(), Toast.LENGTH_SHORT ).show();
                                buttonReg.setClickable( true );
                            }
                        } );
            }
        } );
        buttonSignIn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSignIn.setClickable( false );
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
                            act.GoToFragment( "InterfaceFragment" );
                        }
                    } );
                } else {
                    buttonSignIn.setClickable( true );
                    Toast.makeText( getContext(), "Can't log in: You must write your  password and email!", Toast.LENGTH_SHORT ).show();
                }
            }
        } );
        return view;

    }

    private boolean isEmailCorrect(String email) {
        return email.contains( "@" ) && email.contains( "." ) && email.length() > 5;
    }

    private boolean isPasswordCorrect(String password) {
        return password.length() > 5;
    }

}

