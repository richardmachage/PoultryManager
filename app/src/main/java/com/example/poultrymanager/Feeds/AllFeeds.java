package com.example.poultrymanager.Feeds;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.poultrymanager.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AllFeeds extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AllFeedsRecyclerAdapter allFeedsRecyclerAdapter;
    private ArrayList<FeedsRecord> listOfFeeds;

    FirebaseFirestore database = FirebaseFirestore.getInstance();
    CollectionReference FeedsCollection = database.collection("Feeds");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_feeds);
        setTitle("Feeds");

        initializeViews();
        getFeedsRecordsFromDatabase();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(allFeedsRecyclerAdapter);
    }

    private void initializeViews(){
        listOfFeeds = new ArrayList<FeedsRecord>();
        recyclerView = findViewById(R.id.all_feeds_recyclerView);
        allFeedsRecyclerAdapter = new AllFeedsRecyclerAdapter(this,listOfFeeds);

    }

    private void getFeedsRecordsFromDatabase() {
        FeedsCollection.orderBy("date", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {
                            return;
                        }

                        for (DocumentChange documentChange : value.getDocumentChanges()) {
                            if (documentChange.getType() == DocumentChange.Type.ADDED) {
                                listOfFeeds.add(documentChange.getDocument().toObject(FeedsRecord.class));
                            }

                            allFeedsRecyclerAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}