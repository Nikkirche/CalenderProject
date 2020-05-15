package com.example.calenderproject.fragments.menu_container;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.calenderproject.R;

public class CameraFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_camera, container, false );
        Button buttonToCamera = view.findViewById( R.id.buttonToCameraFragment);
        buttonToCamera.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraFragment cameraFragment = new CameraFragment();
                getChildFragmentManager().beginTransaction().add( R.id.ShareContainer ,cameraFragment).commit();
            }
        } );
        return view;
    }
}
