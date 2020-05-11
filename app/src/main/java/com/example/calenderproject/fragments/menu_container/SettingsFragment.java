package com.example.calenderproject.fragments.menu_container;

import android.os.Bundle;

import com.example.calenderproject.R;
import com.jaredrummler.cyanea.prefs.CyaneaSettingsFragment;

public class SettingsFragment extends CyaneaSettingsFragment {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource( R.xml.root_preferences, rootKey );
    }
}
