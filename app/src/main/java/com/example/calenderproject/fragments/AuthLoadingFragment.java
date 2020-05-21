package com.example.calenderproject.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.calenderproject.MainActivity;
import com.example.calenderproject.R;
public class AuthLoadingFragment extends Fragment {
    Button buttonStart;

    @Override
    public void onStart() {
        super.onStart();
        //код загрузки
        buttonStart.setVisibility( View.VISIBLE );
        final MainActivity act = (MainActivity) getActivity();
        act.GoToFragment( "InterfaceFragment" );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_auth_loading, container, false );
         buttonStart = view.findViewById( R.id.buttonStart );

        return view;
    }
}
