package com.example.calenderproject.fragments.menu_container;

import android.content.Intent;
import android.os.Bundle;

import androidx.preference.Preference;

import com.example.calenderproject.MainActivity;
import com.example.calenderproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.jaredrummler.cyanea.prefs.CyaneaSettingsActivity;
import com.jaredrummler.cyanea.prefs.CyaneaSettingsFragment;

public class SettingsFragment extends CyaneaSettingsFragment {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource( R.xml.root_preferences, rootKey );
        Preference LogOut = findPreference( "LogOut" );
        Preference Customisation = findPreference( "Customisation" );
        LogOut.setOnPreferenceClickListener( new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                FirebaseAuth.getInstance().signOut();
                MainActivity activity = (MainActivity)getActivity();
                activity.GoToFragment( "StartFragment" );
                return true;
            }
        } );
        Customisation.setOnPreferenceClickListener( new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(getActivity(), CyaneaSettingsActivity.class));
                return true;
            }
        } );

    }
    }



