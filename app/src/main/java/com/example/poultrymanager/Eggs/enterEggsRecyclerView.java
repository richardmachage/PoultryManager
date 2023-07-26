package com.example.poultrymanager.Eggs;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poultrymanager.MainActivity;
import com.example.poultrymanager.R;

import java.util.ArrayList;

public class enterEggsRecyclerView extends RecyclerView.Adapter<enterEggsRecyclerView.enterEggsVH> {

    ArrayList<EggRecord>cellsArrayList;
    Context context;

    public enterEggsRecyclerView(ArrayList cellsArrayList, Context context) {
        this.cellsArrayList = cellsArrayList;
        this.context = context;
    }


    @NonNull
    @Override
    public enterEggsRecyclerView.enterEggsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.entereggs_recyclerview,parent,false);

        return new enterEggsVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull enterEggsRecyclerView.enterEggsVH holder, int position) {

        holder.cellNumber.setText("Cell Number: "+ (1 + position ));
        holder.numOfeggs.setText(String.valueOf(cellsArrayList.get(holder.getAdapterPosition()).getNumOfEggs()));
        holder.numOfeggs.setSelection(holder.numOfeggs.getText().length());

        holder.numOfeggs.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    cellsArrayList.get(holder.getAdapterPosition()).setNumOfEggs(
                            Integer.parseInt(holder.numOfeggs.getText().toString())
                    );
                }catch (NumberFormatException numberFormatException){
                    cellsArrayList.get(holder.getAdapterPosition()).setNumOfEggs(0);
                }

                EggsFragment2.totalEggs.setText("Total Eggs: "+EggsFragment2.computeTotalEggs());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return cellsArrayList.size();
    }

    public class enterEggsVH extends RecyclerView.ViewHolder {

        TextView cellNumber;
        EditText numOfeggs;

        public enterEggsVH(@NonNull View itemView) {
            super(itemView);
            cellNumber = itemView.findViewById(R.id.cellNumber);
            numOfeggs = itemView.findViewById(R.id.numOfEggs);

        }
    }
}
