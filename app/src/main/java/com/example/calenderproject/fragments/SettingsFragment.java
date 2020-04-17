package com.example.calenderproject.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;

import androidx.preference.DialogPreference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.calenderproject.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource( R.xml.root_preferences, rootKey );

    }

}

