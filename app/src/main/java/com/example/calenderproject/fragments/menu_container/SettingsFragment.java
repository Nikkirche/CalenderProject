package com.example.calenderproject.fragments.menu_container;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.calenderproject.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource( R.xml.root_preferences, rootKey );

    }

}

