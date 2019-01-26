package com.example.asus.weathercast;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
 import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    public int mNum;

    static TimePickerFragment newInstance(int num) {
        TimePickerFragment f = new TimePickerFragment();
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        mNum = getArguments().getInt("num");
        Log.v("numpassed",String.valueOf(mNum));

        //Create and return a new instance of TimePickerDialog
        return new TimePickerDialog(getActivity(),this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView alarmclock1 =(TextView) getActivity().findViewById(R.id.textClock);
        TextView alarmclock2 =(TextView) getActivity().findViewById(R.id.textClock2);
        TextView alarmclock3 =(TextView) getActivity().findViewById(R.id.textClock3);


        int hour=hourOfDay;
        int minutes =minute;
        String timeSet = "";
        if (hour > 12) {
            hour -= 12;
            timeSet = "PM";
        } else if (hour == 0) {
            hour += 12;
            timeSet = "AM";
        } else if (hour == 12){
            timeSet = "PM";
        }else{
            timeSet = "AM";
        }
        String min = "";
        if (minutes < 10)
            min = "0" + minutes ;
        else
            min = String.valueOf(minutes);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hour).append(':')
                .append(min ).append(" ").append(timeSet).toString();
        Log.v("timetoshow",aTime);
        switch (mNum){
            case 1:
                alarmclock1.setText(aTime);
                return;
            case 2:
                alarmclock2.setText(aTime);
                return;
            case 3:
                alarmclock3.setText(aTime);
                return;
            }
        }

     }

