package com.example.calenderproject.fragments.menu_container;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.calenderproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class InterfaceFragment extends Fragment {
    CalenderFragment CalenderFragment;
    com.example.calenderproject.fragments.menu_container.SettingsFragment SettingsFragment;
    MyChannelsFragment MyChannelsFragment;

    @Override
    public void onStart() {
        super.onStart();
        FragmentTransaction ftrans = getChildFragmentManager().beginTransaction();
        ftrans.add( R.id.menu_container,CalenderFragment ).commit();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_interface, container, false );
        setHasOptionsMenu( true );
        CalenderFragment = new CalenderFragment();
        SettingsFragment = new SettingsFragment();
        MyChannelsFragment = new MyChannelsFragment();

        BottomNavigationView menu = view.findViewById( R.id.menu );
        menu.setOnNavigationItemSelectedListener( new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.Calender:
                        selectedFragment = CalenderFragment;
                        break;
                    case R.id.MyChannels:
                        selectedFragment = MyChannelsFragment;
                        break;
                    case R.id.Settings:
                        selectedFragment = SettingsFragment;
                        break;
                }
                if (selectedFragment != null) {
                    getChildFragmentManager().beginTransaction().replace( R.id.menu_container, selectedFragment ).commit();
                }
                return  true;
            }
        } );
        return view;
    }



}