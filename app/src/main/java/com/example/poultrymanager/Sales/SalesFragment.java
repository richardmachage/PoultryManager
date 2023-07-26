package com.example.poultrymanager.Sales;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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


public class SalesFragment extends Fragment {

    private ImageButton calenderButton;
    private EditText inputDate, inputNumOfTrays, inputPricePerTray, inputBuyerName, inputBuyerPhone, inputAmountPaid;
    private TextView totalPrice;
    private View v;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private Button viewAllButton, confirmSaleButton;
    private RecyclerView recyclerView;
    private AllSalesRecyclerAdapter allSalesRecyclerAdapter;
    private ArrayList<Sale> listOfSales;
    private DateFormat format;

    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference salesCollection = database.collection("Sales");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_sales, container, false);

        format = new SimpleDateFormat("dd/MM/yyyy");
        initializeViews(); //this Method initializes all views From the layout file to the java class
        getSalesFromDatabase(); //this Method gets data from Firebase Database and populates the recyclerView

        inputDate.setShowSoftInputOnFocus(false);
        inputPricePerTray.setText("9000");

        //recentSales Recyclerview functionality
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(allSalesRecyclerAdapter);


        //START of button Listeners

        confirmSaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInputs()) {

                    ProgressDialog progressDialog = new ProgressDialog(getContext());
                    progressDialog.setTitle("Sales");
                    progressDialog.setMessage("saving...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    Date date = null;
                    try {
                        date = format.parse(inputDate.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    int numOfTrays = Integer.parseInt(inputNumOfTrays.getText().toString());
                    int pricePerTray = Integer.parseInt(inputPricePerTray.getText().toString());
                    String buyerName = inputBuyerName.getText().toString();

                    Sale sale = new Sale(date, numOfTrays, pricePerTray, buyerName);

                    salesCollection.add(sale)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    progressDialog.dismiss();
                                    clearFields();
                                    Toast.makeText(getContext(), "Sale Complete", Toast.LENGTH_SHORT).show();

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

        inputNumOfTrays.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    totalPrice.setText("Total Price: Tsh " + Integer.parseInt(inputPricePerTray.getText().toString()) * Integer.parseInt(inputNumOfTrays.getText().toString()));
                } catch (Exception s) {
                    totalPrice.setText("Total Price: Tsh 00");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

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

        calenderButton.setOnClickListener(new View.OnClickListener() {
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

        viewAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToAllSales = new Intent(getContext(), AllSalesActivity.class);
                startActivity(goToAllSales);
            }
        });
        //END of button listeners

        return v;
    }

    private void initializeViews() {
        //textViews
        totalPrice = v.findViewById(R.id.totalPriceTextview);

        //EditText
        inputAmountPaid = v.findViewById(R.id.inputAmountPaid);
        inputBuyerName = v.findViewById(R.id.inputBuyerName);
        inputBuyerPhone = v.findViewById(R.id.inputBuyerContact);
        inputNumOfTrays = v.findViewById(R.id.inputNumOfTrays);
        inputPricePerTray = v.findViewById(R.id.inputPricePerTray);
        inputDate = v.findViewById(R.id.inputDate);

        //Buttons
        viewAllButton = v.findViewById(R.id.viewAllSalesButton);
        calenderButton = v.findViewById(R.id.calenderImageButton);
        confirmSaleButton = v.findViewById(R.id.confirmSellButton);

        //Recyclerview
        recyclerView = v.findViewById(R.id.recentSalesRecyclerView);
        listOfSales = new ArrayList<Sale>();
        allSalesRecyclerAdapter = new AllSalesRecyclerAdapter(getContext(), listOfSales);
    }

    private void clearFields() {
       // inputPricePerTray.setText(null);
        inputDate.setText(null);
        inputNumOfTrays.setText(null);
        inputBuyerPhone.setText(null);
        inputBuyerName.setText(null);
        inputAmountPaid.setText(null);
    }

    private void getSalesFromDatabase() {
        database.collection("Sales").orderBy("date", Query.Direction.DESCENDING).limit(3).
                addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            error.printStackTrace();
                            return;
                        }

                        for (DocumentChange documentChange : value.getDocumentChanges()) {
                            if (documentChange.getType() == DocumentChange.Type.ADDED) {
                                listOfSales.add(documentChange.getDocument().toObject(Sale.class));
                            }

                            allSalesRecyclerAdapter.notifyDataSetChanged();

                        }
                    }
                });
    }

    private boolean validateInputs() {

        if (inputDate.getText().toString().isEmpty() ||
                inputNumOfTrays.getText().toString().isEmpty() ||
                inputPricePerTray.getText().toString().isEmpty() ||
                inputBuyerName.getText().toString().isEmpty()) {

            Toast.makeText(getContext(), " error! Fill all required fields!!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}