package com.example.calenderproject.UI.menu_container;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.calenderproject.R;
import com.example.calenderproject.firebase.EventService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.cyanea.app.CyaneaFragment;

import java.util.Calendar;
import java.util.HashMap;

public class CreateEventFragment extends CyaneaFragment {
    private String ChannelName;
    private TextView NameView;
    private static Button  ButtonSetData;
    private  static Button ButtonSetTime;

     private DatabaseReference refUser;
     private DatabaseReference refChannel;
     private DatabaseReference refToUser;
     HashMap<String, HashMap<String, String>> UserChannelEvent;
     HashMap<String, String> SubscriberChannelEvent;
     HashMap<String, HashMap<String, HashMap<String, String>>> mapp1;
     HashMap<String,HashMap<String, HashMap<String, HashMap<String, String>>>> mapp;
     HashMap<String,HashMap<String, HashMap<String, HashMap<String, String>>>> mapp2;
     private FirebaseUser CurrentUser = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    public void onStart() {
        super.onStart();
        getChannelName();


        refChannel = FirebaseDatabase.getInstance().getReference( "Channels" ).child( ChannelName );
        refChannel.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mapp1 = (HashMap) dataSnapshot.getValue();

                    if (mapp1.get( "events" ) == null) {
                        UserChannelEvent = mapp1.get("events");
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        } );
        refUser = FirebaseDatabase.getInstance().getReference( "Channels" ).child( ChannelName );
        refUser.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, HashMap<String, String>> map = (HashMap) dataSnapshot.getValue();
                if (map != null) {
                    SubscriberChannelEvent = map.get( "subscribers" );
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        } );
        try {
            Thread.sleep(1000);
            refToUser = FirebaseDatabase.getInstance().getReference( "users" );
            refToUser.addValueEventListener( new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mapp = (HashMap) dataSnapshot.getValue();
                    mapp2=(HashMap) dataSnapshot.getValue();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            } );
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void getChannelName() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            ChannelName = bundle.getString( "ChannelName" );

            NameView.setText( ChannelName );
        } else {
            NameView.setText( "Error" );
        }

    }

    @Override
    public  View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_create_event, container, false );
        NameView = view.findViewById( R.id.CreateEventChannelNameView );
        final Button buttonCreateEvent = view.findViewById( R.id.buttonCreateEvent );
        ImageButton backButton = view.findViewById( R.id.buttonToChannelFromEvent );
         ButtonSetTime = view.findViewById( R.id.ButtonCreateEventSetTime );
        ButtonSetData = view.findViewById( R.id.ButtonCreateEventSetData);
        final EditText EditText = view.findViewById( R.id.CreateEventEditText );
        backButton.setOnClickListener( view12 -> getParentFragmentManager().popBackStackImmediate() );
        ButtonSetTime.setOnClickListener( view1 -> {
            TimePickerFragment mTimePicker = new TimePickerFragment();
            mTimePicker.show(getChildFragmentManager(), "Select time");
        } );
        ButtonSetData.setOnClickListener( view13 -> {
            DatePickerFragment mDatePicker = new DatePickerFragment();
            mDatePicker.show(getChildFragmentManager(), "Select date");
        } );
        buttonCreateEvent.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonCreateEvent.setClickable( false);
                EventService.SetEvent (  UserChannelEvent,
                         SubscriberChannelEvent,
                         mapp1,
                         mapp,
                         mapp2
                );
                String text = EditText.getText().toString();
                String  data = ButtonSetData.getText().toString() + " " +  ButtonSetTime.getText().toString();
                if (DataIsTrue( data ) && TextIsTrue( text )) {
                    EventService.createNewEvent( ChannelName, data, text );
                    buttonCreateEvent.setClickable( false);
                    getParentFragmentManager().popBackStackImmediate();
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

    private boolean DataIsTrue(String  data) {
        return true;
    }
    public static  class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
        }
        @Override
        public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
            String timeToShow;
            if (hourOfDay>9) {
                if (minute>9) {
                    timeToShow = String.valueOf( hourOfDay ) + ":" + String.valueOf( minute );
                }
                else{
                    timeToShow = String.valueOf( hourOfDay ) + ":0" + String.valueOf( minute );
                }
            }
            else{
                if (minute>9) {
                    timeToShow = "0"+String.valueOf( hourOfDay ) + ":" + String.valueOf( minute );
                }
                else{
                    timeToShow = "0"+String.valueOf( hourOfDay ) + ":0" + String.valueOf( minute );
                }
            }
            ButtonSetTime.setText(timeToShow);
        }
    }
    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get( Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }
        public void onDateSet(DatePicker view, int year, int month, int day) {
            String dataToShow;
            if (month>9) {
                if (day>9) {
                    dataToShow = String.valueOf( year ) + "-" + String.valueOf( month ) + "-" + String.valueOf( day );
                }
                else {
                    dataToShow = String.valueOf( year ) + "-" + String.valueOf( month ) + "-0" + String.valueOf( day );
                }
            }
            else{
                if (day>9) {
                    dataToShow = String.valueOf( year ) + "-0" + String.valueOf( month ) + "-" + String.valueOf( day );
                }
                else{
                    dataToShow = String.valueOf( year ) + "-0" + String.valueOf( month ) + "-0" + String.valueOf( day );
                }
            }
            ButtonSetData.setText( dataToShow );
        }
    }
}
