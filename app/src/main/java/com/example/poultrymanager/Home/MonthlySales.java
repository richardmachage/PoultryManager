package com.example.poultrymanager.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.poultrymanager.Eggs.EggRecord;
import com.example.poultrymanager.R;
import com.example.poultrymanager.Sales.Sale;
import com.example.poultrymanager.databinding.ActivityMonthlySalesBinding;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MonthlySales extends AppCompatActivity {
    ActivityMonthlySalesBinding binding;

    private LineData lineData;
    private LineDataSet lineDataSet;
    private String buyerName;
    private ArrayList<String> listOfBuyerNames;
    private BarData barData;
    private BarDataSet barDataSet;
    private ArrayList<Sale> saleRecords;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMonthlySalesBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        setTitle("Monthly Sales");

        getSalesDataFromDatabase();

        listOfBuyerNames = new ArrayList<>();
        listOfBuyerNames.add("marco");
        listOfBuyerNames.add("silk");
        listOfBuyerNames.add("comfort");

        spinnerAdapter(listOfBuyerNames);

    }

    private void spinnerAdapter(List<String> listOfBuyers){

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, listOfBuyers);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        binding.inputBuyerNameSpinner.setAdapter(spinnerArrayAdapter);
        spinnerArrayAdapter.notifyDataSetChanged();
    }

    private ArrayList<Sale> getSalesDataFromDatabase() {
         saleRecords = new ArrayList<>();

        database.collection("Sales")
                .orderBy("date", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                saleRecords.add(document.toObject(Sale.class));
                            }

                        } else {
                            task.getException().printStackTrace();
                            Toast.makeText(getApplicationContext(), "Failed to load data, check Your internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        return saleRecords;
    }

    private void plotBarChart(ArrayList<Sale> eggRecordsToPlot) {
        ArrayList<Entry> lineEntryArrayList = new ArrayList<Entry>();
        ArrayList<BarEntry> barEntryArrayList = new ArrayList<BarEntry>();
        ArrayList labelNames = new ArrayList();

        for (int i = 0; i < eggRecordsToPlot.size(); i++) {
            lineEntryArrayList.add(new Entry(i, eggRecordsToPlot.get(i).getNumOfTrays()));
            barEntryArrayList.add(new BarEntry(i, eggRecordsToPlot.get(i).getNumOfTrays()));
            labelNames.add(new SimpleDateFormat("dd, MMMM").format(eggRecordsToPlot.get(i).getDate()));
        }

        lineDataSet = new LineDataSet(lineEntryArrayList, "Monthly Records");
        binding.monthlyProduceLinechart.getDescription().setEnabled(false);
        lineData = new LineData(lineDataSet);

        XAxis xAxis = binding.monthlyProduceLinechart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labelNames));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xAxis.setGranularity(1f);
        xAxis.setLabelRotationAngle(270);
        xAxis.setLabelCount(labelNames.size());

        binding.monthlyProduceLinechart.setData(lineData);
        binding.monthlyProduceLinechart.invalidate();


        barDataSet = new BarDataSet(barEntryArrayList, "Monthly Records");
        binding.monthlyProduceBarchart.getDescription().setEnabled(false);
        barData = new BarData(barDataSet);

        XAxis xAxisBar = binding.monthlyProduceBarchart.getXAxis();
        xAxisBar.setValueFormatter(new IndexAxisValueFormatter(labelNames));
        xAxisBar.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xAxisBar.setGranularity(1f);
        xAxisBar.setLabelRotationAngle(270);
        xAxisBar.setLabelCount(labelNames.size());


        binding.monthlyProduceBarchart.setData(barData);
        binding.monthlyProduceBarchart.invalidate();
    }


}