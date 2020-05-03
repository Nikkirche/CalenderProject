package com.example.calenderproject.fragments.menu_container;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.CalenderProject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DataPickerFragment extends Fragment {

    public DataPickerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_data_picker, container, false );
    }
}
