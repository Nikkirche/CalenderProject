package com.example.calenderproject.fragments.menu_container;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.calenderproject.R;
import com.example.calenderproject.firebase.ChannelService;

public class CreateChannelFragment extends Fragment {
    MyChannelsFragment myChannelsFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_create_channel, container, false );
        ImageButton buttonGoBack = view.findViewById( R.id.buttonToMyChannelsFromCreate);
        final EditText editNameChannel = view.findViewById( R.id.editCreateChannelName );
        final Button buttonCreateChannel = view.findViewById( R.id.buttonCreateChannel );
        buttonCreateChannel.setOnClickListener( new View.OnClickListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onClick(View v) {
                String name = editNameChannel.getText().toString();
                if(name.length() >3) {
                    ChannelService.createNewChannel( name, null );
                    buttonCreateChannel.setClickable( false );
                }
                else{
                    Toast.makeText( getContext(),"You must write a name longer as 3 symbols",Toast.LENGTH_SHORT );
                }
            }
        } );
        buttonGoBack.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    myChannelsFragment.GoToFragment( "MyChannels" );
            }
        } );

        return view;
    }
}
