package com.example.calenderproject.fragments.menu_container;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.calenderproject.R;
import com.jaredrummler.cyanea.prefs.CyaneaThemePickerFragment;

public class CustomisationFragment extends CyaneaThemePickerFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate( R.layout.fragment_customisation, container, false );
    }
}
