package com.example.poultrymanager.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.example.poultrymanager.Eggs.EggRecord;
import com.example.poultrymanager.Eggs.EggRecordMonthly;
import com.example.poultrymanager.R;
import com.example.poultrymanager.databinding.ActivityYearlyProductionBinding;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.color.MaterialColors;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class YearlyProduction extends AppCompatActivity {
    ActivityYearlyProductionBinding binding;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private ArrayList<EggRecord> eggRecordsArrayList = new ArrayList<>();
    private DateFormat dateFormat = new SimpleDateFormat("dd/MMMM/yyyy");

    private BarDataSet barDataSet;
    private LineDataSet lineDataSet;
    private PieDataSet pieDataSet;

    private  BarData barData;
    private LineData lineData;
    private PieData pieData;

    private String yearOverall ;
    private int cageNumber = 1;
    private int cellNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityYearlyProductionBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        setTitle("Yearly Production");

        clickListeners();
        getProductionDataFromDatabase(cageNumber,cellNumber);
    }

    private void clickListeners(){
        binding.plotChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               plotBarChart(filterCellRecordsByYear(yearOverall,getProductionDataFromDatabase(cageNumber,cellNumber)));
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

    private ArrayList<EggRecordMonthly> filterCellRecordsByYear(String year, ArrayList<EggRecord> recordsOfaCell) {
        ArrayList<EggRecordMonthly> eggRecords = new ArrayList<>();
        int jan = 0, feb = 0, mar = 0, apr = 0, may = 0, jun = 0, jul = 0, aug = 0, sep = 0, oct = 0, nov = 0, dec = 0;

        for (int recordNumber = 0; recordNumber < recordsOfaCell.size(); recordNumber++) {

            EggRecord eggRecord = recordsOfaCell.get(recordNumber);
            String splitDate[] = dateFormat.format(eggRecord.getDate()).split("/");

            System.out.println("Year from db: " + splitDate[2] + " not equal to " + year);


            if (splitDate[2].equals(year) && splitDate[1].equals("January"))
            {
                jan += eggRecord.getNumOfEggs();
            }
            if (splitDate[2].equals(year) && splitDate[1].equals("February"))
            {
                feb += eggRecord.getNumOfEggs();
            }
            if (splitDate[2].equals(year) && splitDate[1].equals("March"))
            {
                mar += eggRecord.getNumOfEggs();
            }
            if (splitDate[2].equals(year) && splitDate[1].equals("April"))
            {
                apr += eggRecord.getNumOfEggs();
            }
            if (splitDate[2].equals(year) && splitDate[1].equals("May"))
            {
                may += eggRecord.getNumOfEggs();
            }
            if (splitDate[2].equals(year) && splitDate[1].equals("June"))
            {
                jun += eggRecord.getNumOfEggs();
            }
            if (splitDate[2].equals(year) && splitDate[1].equals("July"))
            {
                jul += eggRecord.getNumOfEggs();
            }
            if (splitDate[2].equals(year) && splitDate[1].equals("August"))
            {
                aug += eggRecord.getNumOfEggs();
            }
            if (splitDate[2].equals(year) && splitDate[1].equals("September"))
            {
                sep += eggRecord.getNumOfEggs();
            }
            if (splitDate[2].equals(year) && splitDate[1].equals("October"))
            {
                oct += eggRecord.getNumOfEggs();
            }
            if (splitDate[2].equals(year) && splitDate[1].equals("November"))
            {
                nov += eggRecord.getNumOfEggs();
            }
            if (splitDate[2].equals(year) && splitDate[1].equals("December"))
            {
                dec += eggRecord.getNumOfEggs();
            }

        }



        eggRecords.add(new EggRecordMonthly("January", year, jan));
        eggRecords.add(new EggRecordMonthly("February", year, feb));
        eggRecords.add(new EggRecordMonthly("March", year, mar));
        eggRecords.add(new EggRecordMonthly("April", year, apr));
        eggRecords.add(new EggRecordMonthly("May", year, may));
        eggRecords.add(new EggRecordMonthly("June", year, jun));
        eggRecords.add(new EggRecordMonthly("July", year, jul));
        eggRecords.add(new EggRecordMonthly("August", year, aug));
        eggRecords.add(new EggRecordMonthly("September", year, sep));
        eggRecords.add(new EggRecordMonthly("October", year, oct));
        eggRecords.add(new EggRecordMonthly("November", year, nov));
        eggRecords.add(new EggRecordMonthly("Dec", year, dec));

        for(int i=0; i<eggRecords.size(); i++){
            System.out.println(eggRecords.get(i).getMonth()+" : "+eggRecords.get(i).getNumOfEggs());
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
                        }
                    }
                });

        return eggRecordsArrayList;
    }

    private void plotBarChart(ArrayList<EggRecordMonthly> eggRecordsToPlot) {
        ArrayList<Entry> lineEntryArrayList = new ArrayList<Entry>();
        ArrayList<BarEntry> barEntryArrayList = new ArrayList<BarEntry>();
        ArrayList<PieEntry> pieEntryArrayList = new ArrayList<PieEntry>();
        ArrayList labelNames = new ArrayList();

        for (int i = 0; i < eggRecordsToPlot.size(); i++) {
            lineEntryArrayList.add(new Entry(i, eggRecordsToPlot.get(i).getNumOfEggs()));
            barEntryArrayList.add(new BarEntry(i, eggRecordsToPlot.get(i).getNumOfEggs()));
            pieEntryArrayList.add(new PieEntry(eggRecordsToPlot.get(i).getNumOfEggs(),eggRecordsToPlot.get(i).getMonth()));
            labelNames.add(eggRecordsToPlot.get(i).getMonth());
        }

        lineDataSet = new LineDataSet(lineEntryArrayList, yearOverall+" Records");
        lineDataSet.setColor(Color.BLUE);
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


        barDataSet = new BarDataSet(barEntryArrayList, "Year Records");
        barDataSet.setColor(Color.BLUE);
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

        pieDataSet = new PieDataSet(pieEntryArrayList,"Year records");
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        binding.monthlyProducePiechart.getDescription().setEnabled(false);
        pieData = new PieData(pieDataSet);

        binding.monthlyProducePiechart.animateX(2000);
        binding.monthlyProducePiechart.setData(pieData);
        binding.monthlyProducePiechart.invalidate();
    }
}