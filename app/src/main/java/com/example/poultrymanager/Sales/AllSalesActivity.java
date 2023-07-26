package com.example.poultrymanager.Sales;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import com.example.poultrymanager.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AllSalesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Sale> listOfSales;
    private AllSalesRecyclerAdapter allSalesRecyclerAdapter;
    private FirebaseFirestore database;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_sales);
        setTitle("Sales");

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Sales");
        progressDialog.setMessage("Fetching Records...");
        //progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        recyclerView = findViewById(R.id.allSalesRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        database = FirebaseFirestore.getInstance();
        listOfSales = new ArrayList<Sale>();
        allSalesRecyclerAdapter = new AllSalesRecyclerAdapter(AllSalesActivity.this, listOfSales);
        recyclerView.setAdapter(allSalesRecyclerAdapter);

        getSalesFromDatabase();


    }

    private void getSalesFromDatabase() {
        database.collection("Sales").orderBy("date", Query.Direction.DESCENDING).
                addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if( error != null){
                            progressDialog.dismiss();
                            error.printStackTrace();
                            return;
                        }

                        for (DocumentChange documentChange : value.getDocumentChanges()){
                            if(documentChange.getType() == DocumentChange.Type.ADDED){
                                listOfSales.add(documentChange.getDocument().toObject(Sale.class));
                            }

                            allSalesRecyclerAdapter.notifyDataSetChanged();

                            progressDialog.dismiss();

                        }
                    }
                });
    }
}