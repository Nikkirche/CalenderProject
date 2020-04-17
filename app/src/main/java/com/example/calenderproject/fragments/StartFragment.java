package com.example.calenderproject.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.calenderproject.MainActivity;
import com.example.calenderproject.R;

import java.util.Objects;


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

