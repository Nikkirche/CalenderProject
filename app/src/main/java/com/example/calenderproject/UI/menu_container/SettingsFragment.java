package com.example.calenderproject.UI.menu_container;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.preference.Preference;

import com.example.calenderproject.MainActivity;
import com.example.calenderproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.jaredrummler.cyanea.prefs.CyaneaSettingsActivity;
import com.jaredrummler.cyanea.prefs.CyaneaSettingsFragment;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
                try {
                    FileOutputStream BigDude = getContext().openFileOutput( "events.txt", Context.MODE_PRIVATE );
                    try {
                        BigDude.flush();
                        try {
                            BigDude.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
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



