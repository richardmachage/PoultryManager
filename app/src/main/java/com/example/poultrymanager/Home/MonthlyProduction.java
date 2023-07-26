package com.example.poultrymanager.Home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.poultrymanager.Eggs.EggRecord;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
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
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.poultrymanager.databinding.ActivityMonthlyProductionBinding;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MonthlyProduction extends AppCompatActivity {

    ActivityMonthlyProductionBinding binding;
    private DateFormat dateFormat = new SimpleDateFormat("dd/MMMM/yyyy");
    private FirebaseFirestore database = FirebaseFirestore.getInstance();

    private ArrayList<EggRecord> eggRecordsArrayList = new ArrayList<>();
    private String yearOverall = "2021";
    private String monthOverall = "January";
    private int cageNumber = 1;
    private int cellNumber = 1;

    private LineData lineData;
    private LineDataSet lineDataSet;

    private BarData barData;
    private BarDataSet barDataSet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMonthlyProductionBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        setTitle("Monthly Production");

        clickListeners();

        getProductionDataFromDatabase(cageNumber,cellNumber);
    }

    private void clickListeners() {
        binding.plotChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                plotBarChart(FilterCellRecordsByMonth(monthOverall, yearOverall, getProductionDataFromDatabase(1, 1)));

            }
        });

        binding.inputYearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                yearOverall = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.inputMonthsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                monthOverall = adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.inputCageNumberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cageNumber = Integer.parseInt(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.inputCellNumberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cellNumber = Integer.parseInt(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private ArrayList<EggRecord> FilterCellRecordsByMonth(String month, String year, ArrayList<EggRecord> recordsOfaCell) {
        ArrayList<EggRecord> eggRecords = new ArrayList<>();


        for (int recordNumber = 0; recordNumber < recordsOfaCell.size(); recordNumber++) {

            EggRecord eggRecord = recordsOfaCell.get(recordNumber);
            String splitDate[] = dateFormat.format(eggRecord.getDate()).split("/");

            System.out.println("Month from db: " + splitDate[1] + " not equal to " + month);
            System.out.println("Year from db: " + splitDate[2] + " not equal to " + year);


            if (splitDate[1].equals(month) && splitDate[2].equals(year)) {
                eggRecords.add(eggRecord);
            }

        }

        return eggRecords;
    }


    private ArrayList<EggRecord> getProductionDataFromDatabase(int cageNumber, int cellNumber) {

        //this will get all records of a single cell
        database.collection("Cages/ Cage " + cageNumber + "/Cells/Cell " + cellNumber + "/EggCollection")
                .orderBy("date", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            eggRecordsArrayList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                eggRecordsArrayList.add(document.toObject(EggRecord.class));
                            }
                        } else {
                            task.getException().printStackTrace();
                            Toast.makeText(getApplicationContext(),"Failed to load data, check Your internet Connection",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        return eggRecordsArrayList;
    }

    private void plotBarChart(ArrayList<EggRecord> eggRecordsToPlot) {
        ArrayList<Entry> lineEntryArrayList = new ArrayList<Entry>();
        ArrayList<BarEntry> barEntryArrayList = new ArrayList<BarEntry>();
        ArrayList labelNames = new ArrayList();

        for (int i = 0; i < eggRecordsToPlot.size(); i++) {
            lineEntryArrayList.add(new Entry(i, eggRecordsToPlot.get(i).getNumOfEggs()));
            barEntryArrayList.add(new BarEntry(i, eggRecordsToPlot.get(i).getNumOfEggs()));
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
