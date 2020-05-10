package com.example.calenderproject.fragments.menu_container;

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

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.calenderproject.R;
import com.example.calenderproject.firebase.EventService;

import java.util.Calendar;

public class CreateEventFragment extends Fragment {
    private String ChannelName;
    private TextView NameView;
    private static Button  ButtonSetData;
    private  static Button ButtonSetTime;

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
    public  View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ChannelFragment ChannelFragment = ((ChannelFragment) CreateEventFragment.this.getParentFragment());
        View view = inflater.inflate( R.layout.fragment_create_event, container, false );
        NameView = view.findViewById( R.id.CreateEventChannelNameView );
        final Button buttonCreateEvent = view.findViewById( R.id.buttonCreateEvent );
        ImageButton backButton = view.findViewById( R.id.buttonToChannelFromEvent );
         ButtonSetTime = view.findViewById( R.id.ButtonCreateEventSetTime );
        ButtonSetData = view.findViewById( R.id.ButtonCreateEventSetData);
        final EditText EditData = view.findViewById( R.id.CreateEventEditData );
        final EditText EditText = view.findViewById( R.id.CreateEventEditText );
        backButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChannelFragment.getChildFragmentManager().popBackStackImmediate();
            }
        } );
        ButtonSetTime.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerFragment mTimePicker = new TimePickerFragment();
                mTimePicker.show(getChildFragmentManager(), "Select time");
            }
        } );
        ButtonSetData.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment mDatePicker = new DatePickerFragment();
                mDatePicker.show(getChildFragmentManager(), "Select date");
            }
        } );
        buttonCreateEvent.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonCreateEvent.setClickable( false);
                String text = EditText.getText().toString();
                String  data = ButtonSetData.getText().toString() + " " +  ButtonSetTime.getText().toString();
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

    private boolean DataIsTrue(String  data) {
        return true;
    }
    public  static  class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
        }
        @Override
        public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
            String timeToShow = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
            ButtonSetTime.setText(timeToShow);
        }
    }
    public  static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get( Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }
        public void onDateSet(DatePicker view, int year, int month, int day) {
            String dataToShow = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day);
            ButtonSetData.setText( dataToShow );
        }
    }
}
