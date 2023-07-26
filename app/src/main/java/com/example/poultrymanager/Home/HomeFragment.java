package com.example.poultrymanager.Home;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.poultrymanager.R;


public class HomeFragment extends Fragment {

    View v ;
    Button monthlyTrendButton, quarterlyTrendButton, yearlyTrendButton, monthlySalesButton, quarterlySalesButton, yearlySalesButton, topCustomersButton, customerPaymentsButton;
    TextView numOfChicken, numOfCages, numOfSacks;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home, container, false);
        
        initializeViews(); //this Method initializes all views From the layout file to the java class


        //START of Button Listeners

        monthlyTrendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToMonthlyTrend = new Intent(getContext(), MonthlyProduction.class);
                startActivity(goToMonthlyTrend);
            }
        });

        monthlySalesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Intent goToMonthlySales = new Intent(getContext(), MonthlySales.class);
                startActivity(goToMonthlySales);*/
            }
        });

       /* quarterlyTrendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToQuarterlyTrend = new Intent(getContext(), QuarterProduction.class);
                startActivity(goToQuarterlyTrend);
            }
        });

        quarterlySalesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goTOQuarterlySales = new Intent(getContext(), QuarterSales.class);
                startActivity(goTOQuarterlySales);
            }
        });*/

        yearlyTrendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToYearlyTrend = new Intent(getContext(), YearlyProduction.class);
                startActivity(goToYearlyTrend);
            }
        });

        yearlySalesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Intent goToYearlySales = new Intent(getContext(), YearlySales.class);
                startActivity(goToYearlySales);
                */
            }
        });

        topCustomersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*
                Toast.makeText(getContext(),"you clicked top customers", Toast.LENGTH_SHORT).show();
*/
            }
        });

        customerPaymentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*
            Toast.makeText(getContext(),"you clicked customer payments", Toast.LENGTH_SHORT).show();
*/
            }
        });

        //End of Button Listeners

        return v ;
    }

    public void initializeViews(){
        monthlyTrendButton = v.findViewById(R.id.monthlytrendButton);
        monthlySalesButton = v.findViewById(R.id.monthlySales);

       /* quarterlyTrendButton = v.findViewById(R.id.quarterlyTrend);
        quarterlySalesButton = v.findViewById(R.id.quarterlySales);
*/
        yearlyTrendButton = v.findViewById(R.id.yearlyTrend);
        yearlySalesButton = v.findViewById(R.id.yearlySales);

        topCustomersButton = v.findViewById(R.id.topCustomer);
        customerPaymentsButton = v.findViewById(R.id.customerPayments);
    }

}