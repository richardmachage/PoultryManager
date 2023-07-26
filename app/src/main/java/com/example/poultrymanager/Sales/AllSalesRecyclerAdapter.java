package com.example.poultrymanager.Sales;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poultrymanager.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AllSalesRecyclerAdapter extends RecyclerView.Adapter<AllSalesRecyclerAdapter.AllSalesViewHolder>{

    private Context context;
    private ArrayList<Sale> listOfSales;
    DateFormat format = new SimpleDateFormat("dd/MM/yyyy");

    public AllSalesRecyclerAdapter(Context context, ArrayList<Sale> listOfSales) {
        this.context = context;
        this.listOfSales = listOfSales;
    }

    @NonNull
    @Override
    public AllSalesRecyclerAdapter.AllSalesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.all_sales_recyclerview,parent, false);


        return new AllSalesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AllSalesRecyclerAdapter.AllSalesViewHolder holder, int position) {

        Sale sale = listOfSales.get(position);
        holder.buyerNameTextview.setText("Buyer Name: "+sale.getBuyerName());
        holder.traysSoldTextview.setText("Trays Sold: "+String.valueOf(sale.getNumOfTrays()));
        holder.pricePerTrayTextView.setText("Price Per Tray: "+String.valueOf(sale.getPricePerTray()));
        holder.priceTextView.setText("Price Sold: "+String.valueOf(sale.getNumOfTrays() * sale.getPricePerTray()));
        holder.saleIdTextView.setText("Date: "+format.format(sale.getDate()));
    }

    @Override
    public int getItemCount() {
        return listOfSales.size();
    }

    public class AllSalesViewHolder extends RecyclerView.ViewHolder {

        TextView saleIdTextView, buyerNameTextview, traysSoldTextview, pricePerTrayTextView, priceTextView;


        public AllSalesViewHolder(@NonNull View itemView) {
            super(itemView);

            saleIdTextView = itemView.findViewById(R.id.saleId_allSalesRecyclclerTextview);
            buyerNameTextview = itemView.findViewById(R.id.buyerName_allSalesRecyclclerTextview);
            traysSoldTextview = itemView.findViewById(R.id.traySold_allSalesRecyclclerTextview);
            pricePerTrayTextView = itemView.findViewById(R.id.pricePerTray_allSalesRecyclclerTextview);
            priceTextView = itemView.findViewById(R.id.price_allSalesRecyclclerTextview);
        }
    }
}
