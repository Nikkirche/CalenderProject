package com.example.calenderproject.fragments.menu_container;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.calenderproject.R;

public class ChannelFragment extends Fragment {
    TextView NameView;
    @Override
    public void onStart() {
        super.onStart();
        Bundle bundle = getArguments();
        if (bundle != null) {
            String ChannelName = bundle.getString("ChannelName");
            NameView.setText( ChannelName );
        }
        else {
            NameView.setText( "Error" );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View  view = inflater.inflate( R.layout.fragment_channel, container, false );
         NameView = view.findViewById( R.id.ChannelFragmentNameView );
        ImageButton UnsubscribeButton = view.findViewById( R.id.buttonUnsubscribe );
        UnsubscribeButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        } );


        return view;
    }
}
