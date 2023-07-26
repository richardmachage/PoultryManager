package com.example.poultrymanager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.poultrymanager.Eggs.EggRecord;
import com.example.poultrymanager.Eggs.EggsFragment;
import com.example.poultrymanager.Eggs.EggsFragment2;
import com.example.poultrymanager.Feeds.FeedsFragment;
import com.example.poultrymanager.Home.HomeFragment;
import com.example.poultrymanager.Sales.Notification;
import com.example.poultrymanager.Sales.SalesFragment;
import com.example.poultrymanager.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    public static ArrayList<EggRecord> listOfEggRecords;
    public static ArrayList<EggRecord> cells;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //getEggRecords();
        populateCells();
        onNewIntent(getIntent());

        binding.bottomNavigationBar.setOnItemSelectedListener(item ->

        {

            switch (item.getItemId()) {
                case R.id.homeBottomNav:
                    replaceFrameLayoutWithFragment(new HomeFragment());
                    setTitle("Home");
                    break;
                case R.id.salesBottomNav:
                    replaceFrameLayoutWithFragment(new SalesFragment());
                    setTitle("Sales");
                    break;
                case R.id.feedsBottomNav:
                    replaceFrameLayoutWithFragment(new FeedsFragment());
                    setTitle("Chicken Feeds");
                    break;
                case R.id.eggBottomNav:
                    replaceFrameLayoutWithFragment(new EggsFragment2());
                    setTitle("Egg Collection");

                    break;
            }
            return true;
        });
    }

    private void populateCells(){
        cells = new ArrayList();

        for(int i = 0; i<24; i++){
            cells.add(new EggRecord());
        }
    }



    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Bundle extras = intent.getExtras();
        if (extras != null) {
            if(extras.containsKey("FRAGMENT_DESTINATION")){
                String destination = extras.getString("FRAGMENT_DESTINATION");
                replaceFrameLayoutWithFragment(new EggsFragment2());
                binding.bottomNavigationBar.setSelectedItemId(R.id.eggBottomNav);
            }
        }else{
            replaceFrameLayoutWithFragment(new HomeFragment());
        }
    }

    private void replaceFrameLayoutWithFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutMainActivity, fragment);
        fragmentTransaction.commit();
    }

    private void getEggRecords() {
        listOfEggRecords = new ArrayList<>();
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
                            //recentEggsRecyclerAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

}




