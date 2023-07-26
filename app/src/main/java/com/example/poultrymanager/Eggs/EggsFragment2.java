package com.example.poultrymanager.Eggs;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.poultrymanager.EggDataReminder;
import com.example.poultrymanager.MainActivity;
import com.example.poultrymanager.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EggsFragment2 extends Fragment {
    View v;

    Calendar calendar;

    public static final String SHARED_PREFERENCES = "Shared Preferences";

    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private EditText inputDate;
    public static TextView totalEggs;
    private EditText inputCageNumber;
    private RecyclerView recyclerView;
    private enterEggsRecyclerView enterEggsRecyclerViewAdapter;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private Button saveButton;
    private DateFormat dateFormat;

    private FirebaseFirestore database = FirebaseFirestore.getInstance();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_eggs2, container, false);

        initializeViews();
        inputDate.setShowSoftInputOnFocus(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(enterEggsRecyclerViewAdapter);


        inputDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getContext(), dateSetListener, year, month, day);
                dialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;

                String dateSelected = day + "/" + month + "/" + year;

                inputDate.setText(dateSelected);
            }
        };

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveRecord();
            }
        });
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.egg_fragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.eggReminderSet) {
            if (!loadAlarmSate()) {
                showTimePicker();
                item.setIcon(R.drawable.reminder_ic);
            } else {
                cancelAlarm();
                item.setIcon(R.drawable.reminder_off_ic);
            }
        }

        return super.onOptionsItemSelected(item);
    }


    private void initializeViews() {
        inputDate = v.findViewById(R.id.inputDateEggs2);
        inputCageNumber = v.findViewById(R.id.inputCageNum);
        recyclerView = v.findViewById(R.id.cellsRecyclerView);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        enterEggsRecyclerViewAdapter = new enterEggsRecyclerView(MainActivity.cells, getContext());
        saveButton = v.findViewById(R.id.saveEggRecords);
        totalEggs = v.findViewById(R.id.totalEggesCollected);
    }

    private void saveRecord() {
        if (validateInputs()) {

            int cageNumber = Integer.parseInt(inputCageNumber.getText().toString());
            Date date = null;
            try {
                date = dateFormat.parse(inputDate.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //set the date for the EggRecord and Save to Database
            for (int i = 1; i <= 24; i++) {
                MainActivity.cells.get(i - 1).setDate(date);
                System.out.println("Date :"+MainActivity.cells.get(i-1).getDate());
            }


            
                for (int i = 1; i <= 24; i++) {
                    database.collection("Cages/ Cage " + cageNumber + "/Cells/Cell " + i + "/EggCollection")
                            .add(MainActivity.cells.get(i - 1));
                }


            clearFields();

            MainActivity.cells.clear();
            for (int i = 0; i < 24; i++) {
                MainActivity.cells.add(new EggRecord());
            }

            enterEggsRecyclerViewAdapter.notifyDataSetChanged();

            Toast.makeText(getContext(), "Record saved Successfully", Toast.LENGTH_SHORT).show();

        }
    }

    private void clearFields(){
        inputCageNumber.setText(null);
        inputDate.setText(null);
    }
    private boolean validateInputs() {
        if (inputDate.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "fill in date field", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (inputCageNumber.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "fill in cage Number", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void showTimePicker() {
        TimePickerDialog timePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                createNotificationChannel();
                setAlarm();
            }

        },
                calendar.HOUR_OF_DAY, calendar.MINUTE, false
        );

        timePicker.setTitle("Set Reminder");

/*
        timePicker.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                cancelAlarm();
                AppCompatActivity act = (AppCompatActivity) getActivity();

                Toolbar toolbar = (Toolbar) act.getSupportActionBar().getCustomView();
                MenuItem reminderIcon = (MenuItem) toolbar.findViewById(R.id.eggReminderSet);
                reminderIcon.setIcon(R.drawable.reminder_off_ic);
            }
        });
*/

        timePicker.show();

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Egg collection Reminder";
            String description = "Channel for egg reminder";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel("eggReminder", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void setAlarm() {

        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), EggDataReminder.class);
        pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        Toast.makeText(getContext(), "Reminder Set successfully", Toast.LENGTH_SHORT).show();

        saveAlarmState(true);

    }

    private void cancelAlarm() {
        Intent intent = new Intent(getContext(), EggDataReminder.class);
        pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);

        if (alarmManager == null) {
            alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        }

        alarmManager.cancel(pendingIntent);

        saveAlarmState(false);
        Toast.makeText(getContext(), "Reminder has been  Canceled", Toast.LENGTH_SHORT).show();
    }

    private void saveAlarmState(boolean state) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("Alarm ON", state);
        editor.apply();
    }

    private boolean loadAlarmSate() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        boolean state = sharedPreferences.getBoolean("Alarm ON", false);

        return state;
    }


    public static int computeTotalEggs(){
        int totalEggs=0;

        for(int i=0; i<MainActivity.cells.size(); i++){
            totalEggs += MainActivity.cells.get(i).getNumOfEggs();
        }

        return totalEggs;
    }
}
