package com.example.poultrymanager.Eggs;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.poultrymanager.EggDataReminder;
import com.example.poultrymanager.databinding.ActivitySetEggReminderBinding;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class setEggReminderActivity extends AppCompatActivity {
    private ActivitySetEggReminderBinding binding;
    Calendar calendar;
    MaterialTimePicker picker;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetEggReminderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //set button listeners
        binding.selectTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker();
            }
        });

        binding.setAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        binding.cancelAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //cancelAlarm();
            }
        });
    }

    private void showTimePicker() {
        TimePickerDialog timePicker = new TimePickerDialog(setEggReminderActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                SimpleDateFormat format = new SimpleDateFormat("k:mm: a");
                String time = format.format(calendar.getTime());

                binding.showTimeReminder.setText(time);

               // createNotificationChannel();
               // setAlarm();
            }
        },
                calendar.HOUR_OF_DAY, calendar.MINUTE, false
        );

        timePicker.setTitle("Set time for your Reminder");
        timePicker.show();

    }



}