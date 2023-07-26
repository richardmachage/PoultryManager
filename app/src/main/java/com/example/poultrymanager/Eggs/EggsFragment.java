package com.example.poultrymanager.Eggs;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.poultrymanager.EggDataReminder;
import com.example.poultrymanager.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class EggsFragment extends Fragment {
    public static final String SHARED_PREFERENCES = "Shared Preferences";

    View v;
    Calendar calendar;
    private ImageButton calenderImageButton;
    private Button saveRecordButton;
    private EditText inputDate, inputNumOfEggs;
    private TextView showCollection;
    private RecyclerView recyclerView;
    private RecentEggsRecyclerAdapter recentEggsRecyclerAdapter;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private  ArrayList<EggRecord> listOfEggRecords;

    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference EggsCollection = database.collection("Eggs");

    private DateFormat format = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_eggs, container, false);

        initializeViews(); //this Method initializes all views From the layout file to the java class
        getEggRecords(); //this Method gets data from Firebase Database and populates the recyclerView

        inputDate.setShowSoftInputOnFocus(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recentEggsRecyclerAdapter);


        //START of button Listeners
        calenderImageButton.setOnClickListener(new View.OnClickListener() {
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

        saveRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInputs()) {

                    ProgressDialog progressDialog = new ProgressDialog(getContext());
                    progressDialog.setTitle("Eggs");
                    progressDialog.setMessage("saving...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();



                    Date date = null;
                    try {
                        date = format.parse(inputDate.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    int numOfEggs = Integer.parseInt(inputNumOfEggs.getText().toString());

                    EggRecord eggRecord = new EggRecord(date, numOfEggs);

                    EggsCollection.add(eggRecord).
                            addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    progressDialog.dismiss();
                                    clearFields();
                                    Toast.makeText(getContext(), "Record saved Successfully", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Error! Failed to save", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            });

                }
            }
        });

        inputNumOfEggs.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!inputNumOfEggs.getText().toString().isEmpty()) {
                    showCollection.setText("TRAYS : " + String.valueOf(
                            Integer.parseInt(inputNumOfEggs.getText().toString()) / 30) +
                            "\n\nEGGS : " + String.valueOf(Integer.parseInt(inputNumOfEggs.getText().toString()) % 30)
                    );
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //END of Button Listeners

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
        inputDate = v.findViewById(R.id.inputDateEggs);
        inputNumOfEggs = v.findViewById(R.id.inputEggsCollected);

        calenderImageButton = v.findViewById(R.id.calenderImageButtonEggs);
        saveRecordButton = v.findViewById(R.id.saveEggCollection);

        showCollection = v.findViewById(R.id.showEggsCollected);
        listOfEggRecords = new ArrayList<EggRecord>();

        recyclerView = v.findViewById(R.id.recentEggRecyclerView);
        recentEggsRecyclerAdapter = new RecentEggsRecyclerAdapter(getContext(), listOfEggRecords);

    }

    private boolean validateInputs() {

        if (inputDate.getText().toString().isEmpty() || inputNumOfEggs.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Error! Empty Field", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void clearFields() {
        inputNumOfEggs.setText(null);
        inputDate.setText(null);
        showCollection.setText("TRAYS : 0\n\n EGGS : 0");
    }

    private void getEggRecords() {
        database.collection("Eggs").orderBy("date", Query.Direction.DESCENDING).
                addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            error.printStackTrace();
                            return;
                        }

                        for (DocumentChange documentChange : value.getDocumentChanges()) {
                            if (documentChange.getType() == DocumentChange.Type.ADDED) {
                                listOfEggRecords.add(documentChange.getDocument().toObject(EggRecord.class));
                            }
                            recentEggsRecyclerAdapter.notifyDataSetChanged();
                        }
                    }
                });
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
}