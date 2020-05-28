package com.example.calenderproject.UI.menu_container;

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
import com.jaredrummler.cyanea.app.CyaneaFragment;

public class InterfaceFragment extends CyaneaFragment {
    private CalenderFragment CalenderFragment;
    private AccountFragment AccountFragment;
    private MyChannelsFragment MyChannelsFragment;
    private  ShareFragment ShareFragment;
    private BottomNavigationView menu;
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
        AccountFragment = new AccountFragment();
        MyChannelsFragment = new MyChannelsFragment();
        ShareFragment = new ShareFragment();
        menu = view.findViewById( R.id.menu );
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
                    case R.id.Account:
                        selectedFragment = AccountFragment;
                        break;
                    case R.id.Share:
                        selectedFragment = ShareFragment;
                }
                if (selectedFragment != null) {
                    getChildFragmentManager().beginTransaction().replace( R.id.menu_container, selectedFragment ).commit();
                }
                return  true;
            }
        } );
        return view;
    }
    void hideMenu(){
        menu.setVisibility( View.GONE );
    }
    void showMenu(){
        menu.setVisibility( View.VISIBLE );
    }



}
