package com.example.calenderproject.UI;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.example.calenderproject.R;
import com.example.calenderproject.UI.menu_container.InterfaceFragment;
import com.example.calenderproject.presenter.StartPresenter;
import com.example.calenderproject.util.MorphAnimation;
import com.google.firebase.auth.FirebaseAuth;
import com.jaredrummler.cyanea.app.CyaneaFragment;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;


public class StartFragment extends CyaneaFragment {
    private StartPresenter startPresenter;
    private CircularProgressButton buttonSignIn;
    private CircularProgressButton buttonReg;

    @Override
    public void onStart() {
        super.onStart();
        startPresenter = new StartPresenter( this );
        startPresenter.CheckStatusOfUser();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_start, container, false );
        //logo//Containers
        View loginContainer = view.findViewById( R.id.form_login );
        View registerContainer = view.findViewById( R.id.form_register );
        //ViewGroups
        final ViewGroup InfoTextViews = view.findViewById( R.id.info_form );
        final ViewGroup loginViews = view.findViewById( R.id.login_views );
        final ViewGroup registerViews = view.findViewById( R.id.register_views );
        //Buttons to form
        final Button buttonToReg = view.findViewById( R.id.buttonToRegFromStart );
        final Button buttonToSign = view.findViewById( R.id.buttonToSignInFromStart );
        //Button Go in
        buttonReg = view.findViewById( R.id.buttonRegister );
        buttonSignIn = view.findViewById( R.id.buttonSignIn );
        //EditTexts
        final EditText regEmail = view.findViewById( R.id.editEmailReg );
        final EditText regName = view.findViewById( R.id.editNameReg );
        final EditText regPassword = view.findViewById( R.id.editPasswordReg );
        final EditText editSignEmail = view.findViewById( R.id.editSignEmail );
        final EditText editSignPassword = view.findViewById( R.id.editSignPassword );
        //AnimationLayout
        final FrameLayout layout = view.findViewById( R.id.frame );
        //Animation
        MorphAnimation morphAnimationLogin = new MorphAnimation( loginContainer, layout, loginViews );
        MorphAnimation morphAnimationRegister = new MorphAnimation( registerContainer, layout, registerViews );
        FirebaseAuth.getInstance()
                .addAuthStateListener( firebaseAuth -> {
                    final String name = regName.getText().toString();
                    startPresenter.ChangedAuthStatus( name, firebaseAuth );

                } );
        regPassword.setOnEditorActionListener( (textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_DONE) {
                final String email = regEmail.getText().toString();
                final String password = regPassword.getText().toString();
                final String name = regName.getText().toString();
                startPresenter.register( email, password, name );
            }
            return true;
        } );
        editSignPassword.setOnEditorActionListener( (textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_DONE) {
                final String email = editSignEmail.getText().toString();
                final String password = editSignPassword.getText().toString();
                startPresenter.signIn( email, password );
            }
            return true;
        } );
        //ButtonFormAnimation
        buttonToReg.setOnClickListener( v -> {
            if (!morphAnimationRegister.isPressed()) {
                loginViews.setVisibility( View.GONE );
                InfoTextViews.setVisibility( View.GONE );
                if ((getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)) {
                    morphAnimationRegister.morphIntoForm( "MATCH_PARENT" );
                } else {
                    morphAnimationRegister.morphIntoForm( "WRAP_CONTENT" );
                }
                buttonToReg.setText( R.string.back );
            } else {
                morphAnimationRegister.morphIntoButton();
                InfoTextViews.setVisibility( View.VISIBLE );
                loginViews.setVisibility( View.VISIBLE );
                buttonToReg.setText( R.string.register );
            }
        } );
        buttonToSign.setOnClickListener( v -> {
                    if (!morphAnimationLogin.isPressed()) {
                        registerViews.setVisibility( View.GONE );
                        InfoTextViews.setVisibility( View.GONE );
                        if ((getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)) {
                            morphAnimationLogin.morphIntoForm( "MATCH_PARENT" );
                        } else {
                            morphAnimationLogin.morphIntoForm( "WRAP_CONTENT" );
                        }
                        buttonToSign.setText( R.string.back );
                    } else {
                        morphAnimationLogin.morphIntoButton();
                        InfoTextViews.setVisibility( View.VISIBLE );
                        buttonToSign.setText( R.string.sign_in );
                        registerViews.setVisibility( View.VISIBLE );
                    }
                    ;
                }
        );
        buttonReg.setOnClickListener( v -> {
            buttonReg.setClickable( false );
            //Get Text
            final String email = regEmail.getText().toString();
            final String password = regPassword.getText().toString();
            final String name = regName.getText().toString();
            startPresenter.register( email, password, name );
            buttonReg.setClickable( true );
        } );
        buttonSignIn.setOnClickListener( v -> {
            buttonSignIn.setClickable( false );
            String email = editSignEmail.getText().toString();
            String password = editSignPassword.getText().toString();
            startPresenter.signIn( email, password );
            buttonSignIn.setClickable( true );
        } );
        return view;
    }

    public void GoToLoadingFragment() {
        AuthLoadingFragment authLoadingFragment = new AuthLoadingFragment();
        getParentFragmentManager().beginTransaction().replace( R.id.container,authLoadingFragment);
    }

    public void GoToApp() {
        InterfaceFragment interfaceFragment = new InterfaceFragment();
        getParentFragmentManager().beginTransaction().replace( R.id.container,interfaceFragment);
    }

    public void SignInAnimationStop() {
        buttonSignIn.revertAnimation();
    }
    public void RegisterInAnimationStop() {
        buttonReg.revertAnimation();
    }
    public void SignInAnimationStart() {
        buttonSignIn.startAnimation();
    }
    public void RegisterInAnimationStart() {
        buttonReg.startAnimation();
    }


}

