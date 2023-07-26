package com.example.poultrymanager.Eggs;

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

public class RecentEggsRecyclerAdapter extends RecyclerView.Adapter<RecentEggsRecyclerAdapter.RecentEggsVH>{

    private Context context;
    private ArrayList<EggRecord> listOfEggRecords;
    DateFormat format = new SimpleDateFormat("dd/MM/yyyy");

    public RecentEggsRecyclerAdapter(Context context, ArrayList<EggRecord> listOfEggRecords) {
        this.context = context;
        this.listOfEggRecords = listOfEggRecords;
    }

    @NonNull
    @Override
    public RecentEggsRecyclerAdapter.RecentEggsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.recenteggs_recyclerview_layout, parent,false);

        return new RecentEggsVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentEggsRecyclerAdapter.RecentEggsVH holder, int position) {
        holder.dateHolder.setText("Date : "+format.format(listOfEggRecords.get(position).getDate()));
        holder.traysAndEggsHolder.setText(
                String.valueOf(listOfEggRecords.get(position).getNumOfEggs() / 30)+" Trays" +" and " +
                        String.valueOf(listOfEggRecords.get(position).getNumOfEggs() % 30)+" Eggs"
        );

        holder.totalEggsHolder.setText("Total Eggs: " + listOfEggRecords.get(position).getNumOfEggs());

    }

    @Override
    public int getItemCount() {
        return listOfEggRecords.size();
    }

    public class RecentEggsVH extends RecyclerView.ViewHolder {

        TextView dateHolder, traysAndEggsHolder, totalEggsHolder;

        public RecentEggsVH(@NonNull View itemView) {
            super(itemView);

            dateHolder = itemView.findViewById(R.id.date_recentEggsRecycler);
            traysAndEggsHolder = itemView.findViewById(R.id.collection_recentEggsRecycler);
            totalEggsHolder = itemView.findViewById(R.id.totalEggs_recentEggsRecycler);

        }
    }
}
