package com.example.calenderproject.fragments.menu_container;

import android.os.Bundle;

import androidx.preference.Preference;

import com.example.calenderproject.MainActivity;
import com.example.calenderproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.jaredrummler.cyanea.prefs.CyaneaSettingsFragment;

public class SettingsFragment extends CyaneaSettingsFragment {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource( R.xml.root_preferences, rootKey );
        Preference LogOut = findPreference( "LogOut" );
        LogOut.setOnPreferenceClickListener( new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                FirebaseAuth.getInstance().signOut();
                MainActivity activity = (MainActivity)getActivity();
                activity.GoToFragment( "StartFragment" );
                return true;
            }
        } );

    }
    }



