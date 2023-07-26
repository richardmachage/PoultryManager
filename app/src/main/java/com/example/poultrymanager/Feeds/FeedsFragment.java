package com.example.poultrymanager.Feeds;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.poultrymanager.R;
import com.google.android.gms.tasks.OnCanceledListener;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class FeedsFragment extends Fragment {
    private View v;
    private ImageButton calenderImageButton;
    private EditText inputDate, inputNumOfSacks;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private Button monthlyTrend, quarterlyTrend, yearlyTrend, viewAllFeeds, saveFeedsRecord;
    private ArrayList<FeedsRecord> listOfFeedsRecords;

    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference FeedsCollection = database.collection("Feeds");

    private RecyclerView recyclerView;
    private AllFeedsRecyclerAdapter allFeedsRecyclerAdapter;

    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_feeds, container, false);

        initializeViews(); //this Method initializes all views From the layout file to the java class
        getFeedsRecordsFromDatabase();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(allFeedsRecyclerAdapter);


        inputDate.setShowSoftInputOnFocus(false);

        // START of Button Listeners

        saveFeedsRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInputs()) {
                    ProgressDialog progressDialog = new ProgressDialog(getContext());
                    progressDialog.setTitle("Feeds");
                    progressDialog.setMessage("Saving...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    Date date = null;
                    try {
                        date = dateFormat.parse(inputDate.getText().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Float numOfSacks = Float.parseFloat(inputNumOfSacks.getText().toString());

                    FeedsRecord feedsRecord = new FeedsRecord(date, numOfSacks);

                    FeedsCollection.add(feedsRecord).
                            addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Saved Successfully", Toast.LENGTH_SHORT).show();
                                    clearFields();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Error! Failed to save", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            }).addOnCanceledListener(new OnCanceledListener() {
                                @Override
                                public void onCanceled() {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Error! Failed to save", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        calenderImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), dateSetListener, year, month, day);
                datePickerDialog.show();
            }
        });

        inputDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), dateSetListener, year, month, day);
                datePickerDialog.show();
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

        monthlyTrend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToMonthlyTrend = new Intent(getContext(), MonthlyFeedingTrends.class);
                startActivity(goToMonthlyTrend);
            }
        });

        quarterlyTrend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToQuarterlyTrend = new Intent(getContext(), QuarterlyFeedingTrend.class);
                startActivity(goToQuarterlyTrend);
            }
        });

        yearlyTrend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToYearlyTrend = new Intent(getContext(), YearlyFeedingTrend.class);
                startActivity(goToYearlyTrend);
            }
        });

        viewAllFeeds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToAllFeeds = new Intent(getContext(), AllFeeds.class);
                startActivity(goToAllFeeds);
            }
        });
        //END of Button Listeners


        return v;
    }

    private void initializeViews() {
        saveFeedsRecord = v.findViewById(R.id.saveFeedsButton);
        monthlyTrend = v.findViewById(R.id.feedsMonthlyTrendButton);
        viewAllFeeds = v.findViewById(R.id.recentFeedsViewAllButton);
        yearlyTrend = v.findViewById(R.id.feedsYearlyTrendButton);
        quarterlyTrend = v.findViewById(R.id.feedsQuarterlyTrendButton);
        calenderImageButton = v.findViewById(R.id.calenderImageButtonFeeds);
        inputDate = v.findViewById(R.id.inputdateFeeds);
        inputNumOfSacks = v.findViewById(R.id.inputNumOfSacks);
        listOfFeedsRecords = new ArrayList<FeedsRecord>();

        recyclerView = v.findViewById(R.id.recentFeedsRecyclerView);
        allFeedsRecyclerAdapter = new AllFeedsRecyclerAdapter(getContext(), listOfFeedsRecords);
    }

    private void getFeedsRecordsFromDatabase() {
        FeedsCollection.orderBy("date", Query.Direction.DESCENDING)
                .limit(10)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {
                            return;
                        }

                        for (DocumentChange documentChange : value.getDocumentChanges()) {
                            if (documentChange.getType() == DocumentChange.Type.ADDED) {
                                listOfFeedsRecords.add(documentChange.getDocument().toObject(FeedsRecord.class));
                            }

                            allFeedsRecyclerAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private boolean validateInputs() {
        if (inputDate.getText().toString().isEmpty() || inputNumOfSacks.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Error! Cant save empty Field", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void clearFields() {
        inputDate.setText(null);
        inputNumOfSacks.setText(null);
    }
}