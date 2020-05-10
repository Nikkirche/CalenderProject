package com.example.calenderproject.fragments.menu_container;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.calenderproject.R;

public class SearchChannelFragment extends Fragment {
    private TextView NameView;

    @Override
    public void onStart() {
        super.onStart();
        Bundle bundle = getArguments();
        if (bundle != null) {
            String channelName = bundle.getString( "ChannelName" );
            NameView.setText( channelName );
        } else {
            NameView.setText( "Error" );
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate( R.layout.fragment_search_channel, container, false );
        ImageButton SubscribeButton = view.findViewById( R.id.SubscribeButton );
        NameView = view.findViewById( R.id.NameView );
        return view ;
    }
}
