package com.example.poultrymanager.Feeds;

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

public class AllFeedsRecyclerAdapter extends RecyclerView.Adapter<AllFeedsRecyclerAdapter.AllFeedsVH> {

    Context context;
    ArrayList<FeedsRecord> listOfFeedsRecords;
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public AllFeedsRecyclerAdapter(Context context, ArrayList<FeedsRecord> listOfFeedsRecords) {
        this.context = context;
        this.listOfFeedsRecords = listOfFeedsRecords;
    }

    @NonNull
    @Override
    public AllFeedsRecyclerAdapter.AllFeedsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.all_feeds_recyclerview_layout, parent, false);

        return new AllFeedsVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AllFeedsRecyclerAdapter.AllFeedsVH holder, int position) {

        holder.date.setText(
                "Date : "+ dateFormat.format(listOfFeedsRecords.get(position).getDate())
        );
        holder.numOfSacks.setText(
                "Sacks Used : " + listOfFeedsRecords.get(position).getNumOfSacks()
        );
    }

    @Override
    public int getItemCount() {
        return listOfFeedsRecords.size();
    }

    public class AllFeedsVH extends RecyclerView.ViewHolder {
        TextView date, numOfSacks;

        public AllFeedsVH(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date_AllFeedsRecycler);
            numOfSacks = itemView.findViewById(R.id.numOfSacks_AllFeedsRecycler);
        }
    }
}
