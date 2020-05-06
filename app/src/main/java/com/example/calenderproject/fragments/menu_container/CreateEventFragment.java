package com.example.calenderproject.fragments.menu_container;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.calenderproject.R;
import com.example.calenderproject.firebase.EventService;

public class CreateEventFragment extends Fragment {
    String ChannelName;
    TextView NameView;

    @Override
    public void onStart() {
        super.onStart();
        Bundle bundle = getArguments();
        if (bundle != null) {
            ChannelName = bundle.getString( "ChannelName" );

            NameView.setText( ChannelName );
        } else {
            NameView.setText( "Error" );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ChannelFragment ChannelFragment = ((ChannelFragment) CreateEventFragment.this.getParentFragment());
        View view = inflater.inflate( R.layout.fragment_create_event, container, false );
        NameView = view.findViewById( R.id.CreateEventChannelNameView );
        final Button buttonCreateEvent = view.findViewById( R.id.buttonCreateEvent );
        ImageButton backButton = view.findViewById( R.id.buttonToChannelFromEvent );
        final EditText EditData = view.findViewById( R.id.CreateEventEditData );
        final EditText EditText = view.findViewById( R.id.CreateEventEditText );
        backButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChannelFragment.getChildFragmentManager().popBackStackImmediate();
            }
        } );
        buttonCreateEvent.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonCreateEvent.setClickable( false);
                String text = EditText.getText().toString();
                Integer data = Integer.parseInt( EditData.getText().toString() );
                if (DataIsTrue( data ) && TextIsTrue( text )) {
                    EventService.createNewEvent( ChannelName, data, text );
                    buttonCreateEvent.setClickable( false);
                    ChannelFragment.getChildFragmentManager().popBackStackImmediate();
                }else{
                    buttonCreateEvent.setClickable( true);
                }
            }
        } );
        return view;
    }

    private boolean TextIsTrue(String text) {
        return text.length() > 5;
    }

    private boolean DataIsTrue(Integer data) {
        return true;
    }
}
