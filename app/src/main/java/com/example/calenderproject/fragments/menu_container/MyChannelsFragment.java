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
        InterfaceFragment interfaceFragment = new InterfaceFragment();
        CreateChannelFragment createChannelFragment = new CreateChannelFragment();
        MyChannelsFragment myChannelsFragment = new MyChannelsFragment();
          FragmentTransaction fragmentTransaction =  getChildFragmentManager().beginTransaction();
          switch (Fragment){
              case "CreateChannel":
                  fragmentTransaction.hide( interfaceFragment);
                  fragmentTransaction.hide(myChannelsFragment);
                  fragmentTransaction.add(R.id.my_channel_container,createChannelFragment);
                  break;
              case "MyChannels":
                  fragmentTransaction.show( interfaceFragment );
                  fragmentTransaction.show( myChannelsFragment );
                  fragmentTransaction.remove(createChannelFragment);
                  break;
          }
          fragmentTransaction.commit();
    }
}
