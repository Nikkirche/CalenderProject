package com.example.calenderproject.fragments.menu_container;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.calenderproject.R;
public class MyChannelsFragment extends Fragment {
    @Override
    public void onStart() {
        ListChannelsFragment listChannelsFragment = new ListChannelsFragment();
        super.onStart();
        getChildFragmentManager().beginTransaction().add( R.id.my_channel_container,listChannelsFragment).commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate( R.layout.fragment_my_channels, container, false );
        final ImageButton buttonGoCreate = view.findViewById( R.id.buttonToCreateChannel );
        buttonGoCreate.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoToFragment( "CreateChannel" );
                buttonGoCreate.setClickable( false );
            }
        } );
        return view ;
    }
    void GoToFragment(String Fragment){
        ListChannelsFragment listChannelsFragment = new ListChannelsFragment();
        InterfaceFragment interfaceFragment = new InterfaceFragment();
        CreateChannelFragment createChannelFragment = new CreateChannelFragment();
        MyChannelsFragment myChannelsFragment = new MyChannelsFragment();
          FragmentTransaction fragmentTransaction =  getChildFragmentManager().beginTransaction();
          switch (Fragment){
              case "CreateChannel":
                  //fragmentTransaction.remove( interfaceFragment);
                  //fragmentTransaction.hide(myChannelsFragment);
                  fragmentTransaction.replace(R.id.my_channel_container,createChannelFragment);
                  break;
              case "MyChannels":
                  //fragmentTransaction.show( interfaceFragment );
                  //fragmentTransaction.show( myChannelsFragment );
                  fragmentTransaction.replace( R.id.my_channel_container,listChannelsFragment);
                  break;
          }
          fragmentTransaction.commit();
    }
}
