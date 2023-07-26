package com.example.poultrymanager.Home;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.poultrymanager.MainActivity;
import com.example.poultrymanager.R;
import com.example.poultrymanager.databinding.ActivityQuarterProductionBinding;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class QuarterProduction extends AppCompatActivity {
    ActivityQuarterProductionBinding binding;
    DateFormat dateFormat = new SimpleDateFormat("MMMM, yyyy");
    private String quarterSelected;
    private String yearsSelected;

    private PieDataSet pieDataSet;
    private BarDataSet barDataSet;
    private LineDataSet lineDataSet;

    private PieData pieData;
    private BarData barData;
    private LineData lineData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityQuarterProductionBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        setTitle("Quarterly Production");

        clickListeners();

        yearsSelected = "2021";

    }

    private void clickListeners(){
        binding.plotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());
                progressDialog.setTitle("Graph");
                progressDialog.setMessage("plotting Graph");
                progressDialog.setCancelable(false);
                progressDialog.show();*/

                plot();

//                progressDialog.dismiss();
            }
        });

        binding.quarter1Radio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quarterSelected = "1";
            }
        });

        binding.quarter2Radio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quarterSelected = "2";
            }
        });

        binding.quarter3Radio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quarterSelected = "3";
            }
        });

        binding.selectYearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                yearsSelected = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.selectQuaterRadioGroup.check(binding.quarter2Radio.getId());
    }

    private void plot() {

        ArrayList counter = new ArrayList();
        int jan = 0, feb = 0, mar = 0, apr = 0, may = 0, jun = 0, jul = 0, aug = 0, sep = 0, oct = 0, nov = 0, dec = 0;


        for (int i = 0; i < MainActivity.listOfEggRecords.size(); i++) {

            String month = dateFormat.format(MainActivity.listOfEggRecords.get(i).getDate());
            int numOfEggs = (int) MainActivity.listOfEggRecords.get(i).getNumOfEggs();

            //algorithm to sort only date selected
            String monthSplitted[] = month.split(",");

            if (quarterSelected.equals("1")) {
                if (monthSplitted[0].equals("January") && yearsSelected.equals(monthSplitted[1])) {
                    jan += numOfEggs;
                    counter.set(0,"has something");
                }
                if (monthSplitted[0].equals("February") && yearsSelected.equals(monthSplitted[1])) {
                    feb += numOfEggs;
                    counter.set(0,"has something");
                }
                if (monthSplitted[0].equals("March") && yearsSelected.equals(monthSplitted[1])) {
                    mar += numOfEggs;
                    counter.set(0,"has something");
                }
                if (monthSplitted[0].equals("April") && yearsSelected.equals(monthSplitted[1])) {
                    apr += numOfEggs;
                    counter.set(0,"has something");
                }

            }
            else if (quarterSelected.equals("2")) {
                if (monthSplitted[0].equals("May") && yearsSelected.equals(monthSplitted[1])) {
                    may += numOfEggs;
                    counter.set(0,"has something");
                }
                if (monthSplitted[0].equals("June") && yearsSelected.equals(monthSplitted[1])) {
                    jun += numOfEggs;
                    counter.set(0,"has something");
                }
                if (monthSplitted[0].equals("July") && yearsSelected.equals(monthSplitted[1])) {
                    jul += numOfEggs;
                    counter.set(0,"has something");
                }
                if (monthSplitted[0].equals("August") && yearsSelected.equals(monthSplitted[1])) {
                    aug += numOfEggs;
                    counter.set(0,"has something");
                }

            }
            else if (quarterSelected.equals("3")) {
                if (monthSplitted[0].equals("September") && yearsSelected.equals(monthSplitted[1])) {
                    sep += numOfEggs;
                    counter.set(0,"has something");
                }
                if (monthSplitted[0].equals("October") && yearsSelected.equals(monthSplitted[1])) {
                    oct += numOfEggs;
                    counter.set(0,"has something");
                }
                if (monthSplitted[0].equals("November") && yearsSelected.equals(monthSplitted[1])) {
                    nov += numOfEggs;
                    counter.set(0,"has something");
                }
                if (monthSplitted[0].equals("December") && yearsSelected.equals(monthSplitted[1])) {
                    dec += numOfEggs;
                    counter.set(0,"has something");
                }
            }

        }

        ArrayList labelNames = new ArrayList();
        switch (quarterSelected) {
            case "1":
                labelNames.add("January");
                labelNames.add("February");
                labelNames.add("March");
                labelNames.add("April");
                break;
            case "2":
                labelNames.add("May");
                labelNames.add("June");
                labelNames.add("July");
                labelNames.add("August");
            case "3":
                labelNames.add("September");
                labelNames.add("October");
                labelNames.add("November");
                labelNames.add("December");
                break;
        }

        ArrayList barEntryArrayList = new ArrayList<>();
        switch (quarterSelected) {
            case "1":
                barEntryArrayList.add(new BarEntry(0, jan));
                barEntryArrayList.add(new BarEntry(1, feb));
                barEntryArrayList.add(new BarEntry(2, mar));
                barEntryArrayList.add(new BarEntry(3, apr));
                break;

            case "2":
                barEntryArrayList.add(new BarEntry(0, may));
                barEntryArrayList.add(new BarEntry(1, jun));
                barEntryArrayList.add(new BarEntry(2, jul));
                barEntryArrayList.add(new BarEntry(3, aug));


            case "3":
                barEntryArrayList.add(new BarEntry(0, sep));
                barEntryArrayList.add(new BarEntry(1, oct));
                barEntryArrayList.add(new BarEntry(2, nov));
                barEntryArrayList.add(new BarEntry(3, dec));
                break;

        }


        ArrayList lineEntryArrayList = new ArrayList();
        switch (quarterSelected) {
            case "1":
                lineEntryArrayList.add(new Entry(0, jan));
                lineEntryArrayList.add(new Entry(1, feb));
                lineEntryArrayList.add(new Entry(2, mar));
                lineEntryArrayList.add(new Entry(3, apr));
                break;

            case "2":
                lineEntryArrayList.add(new Entry(0, may));
                lineEntryArrayList.add(new Entry(1, jun));
                lineEntryArrayList.add(new Entry(2, jul));
                lineEntryArrayList.add(new Entry(3, aug));


            case "3":
                lineEntryArrayList.add(new Entry(0, sep));
                lineEntryArrayList.add(new Entry(1, oct));
                lineEntryArrayList.add(new Entry(2, nov));
                lineEntryArrayList.add(new Entry(3, dec));
                break;

        }

        ArrayList pieEntryArrayList = new ArrayList();
        switch (quarterSelected) {
            case "1":
                pieEntryArrayList.add(new PieEntry(jan, "January"));
                pieEntryArrayList.add(new PieEntry(feb, "February"));
                pieEntryArrayList.add(new PieEntry(mar, "March"));
                pieEntryArrayList.add(new PieEntry(apr, "April"));
                break;

            case "2":
                pieEntryArrayList.add(new PieEntry(may, "May"));
                pieEntryArrayList.add(new PieEntry(jun, "June"));
                pieEntryArrayList.add(new PieEntry(jul, "July"));
                pieEntryArrayList.add(new PieEntry(aug, "August"));


            case "3":
                pieEntryArrayList.add(new PieEntry(sep, "September"));
                pieEntryArrayList.add(new PieEntry(oct, "October"));
                pieEntryArrayList.add(new PieEntry(nov, "November"));
                pieEntryArrayList.add(new PieEntry(dec, "December"));
                break;
        }

        //START of PIE CHART
        pieDataSet = new PieDataSet(pieEntryArrayList, "Quarter " + quarterSelected + "-" + yearsSelected);
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);
        pieDataSet.setValueTextSize(10);
        pieDataSet.setValueTextColor(Color.BLACK);

        pieData = new PieData(pieDataSet);
        binding.quarterProductionPieChart.setEntryLabelColor(Color.BLACK);
        binding.quarterProductionPieChart.setData(pieData);
        binding.quarterProductionPieChart.getLegend().setEnabled(false);
        binding.quarterProductionPieChart.animateXY(2000, 2000);
        binding.quarterProductionPieChart.invalidate();

        //END of PIE CHART

        //START OF BARCHART
        barDataSet = new BarDataSet(barEntryArrayList, "Quarter " + quarterSelected + "-" + yearsSelected);
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barData = new BarData(barDataSet);
        binding.quarterProductionBarChart.setData(barData);

        XAxis xAxis = binding.quarterProductionBarChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labelNames));

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(labelNames.size());
        xAxis.setLabelRotationAngle(270);
        binding.quarterProductionBarChart.animateY(1500);
        binding.quarterProductionBarChart.invalidate();
        binding.quarterProductionBarChart.getDescription().setEnabled(false);
        //END OF BARCHART

        //START OF LINE CHART
        lineDataSet = new LineDataSet(lineEntryArrayList, "Quarter " + quarterSelected + "-" + yearsSelected);
        lineDataSet = new LineDataSet(lineEntryArrayList,"Monthly Production");
        lineDataSet.setDrawIcons(true);
        lineDataSet.enableDashedLine(10f, 5f, 0f);
        lineDataSet.enableDashedHighlightLine(10f, 5f, 0f);
        lineDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        lineDataSet.setLineWidth(1f);
        lineDataSet.setCircleRadius(3f);
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setValueTextSize(9f);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFormLineWidth(1f);
        lineDataSet.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        lineDataSet.setFormSize(15.f);

        lineData =new LineData(lineDataSet);
        binding.quarterProductionLineChart.setData(lineData);


        XAxis xAxislineChart = binding.quarterProductionLineChart.getXAxis();
        xAxislineChart.setValueFormatter((new IndexAxisValueFormatter(labelNames)));

        xAxislineChart.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xAxislineChart.setGranularity(1f);
        xAxislineChart.setLabelCount(labelNames.size());
        xAxislineChart.setLabelRotationAngle(270);


        binding.quarterProductionLineChart.invalidate();
        binding.quarterProductionLineChart.getDescription().setEnabled(false);
        //END OF LINE CHART


        Toast.makeText(getApplicationContext(),String.valueOf(pieEntryArrayList.size()),Toast.LENGTH_SHORT).show();
    }

}
