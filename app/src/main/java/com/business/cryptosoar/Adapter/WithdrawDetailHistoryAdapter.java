package com.business.cryptosoar.Adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.business.cryptosoar.Model.TransactionModel;
import com.business.cryptosoar.R;

import java.util.List;

public class WithdrawDetailHistoryAdapter extends RecyclerView.Adapter<WithdrawDetailHistoryAdapter.ViewHolder>{
    private List<TransactionModel> listdata;
    private Activity activity;

    public WithdrawDetailHistoryAdapter(Activity activity, List<TransactionModel> listdata) {
        this.activity = activity;
        this.listdata = listdata;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.transection_list, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final TransactionModel myListData = listdata.get(position);
         holder.title.setText(listdata.get(position).getTitle());
        holder.totalamount_txt.setText(listdata.get(position).getTotal_amt());
        holder.date_txt.setText(listdata.get(position).getDate());
        holder.status.setText(listdata.get(position).getStatus());
        if (listdata.get(position).getStatus().equalsIgnoreCase("accept"))
        {
            holder.status.setText("Completed");

        }
        else if (listdata.get(position).getStatus().equalsIgnoreCase("request"))
        {
            holder.status.setText("Pending");
            holder.status.setTextColor(Color.parseColor("#FF7373"));

        }
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title,totalamount_txt,date_txt,status;

        public ViewHolder(View itemView) {
            super(itemView);


            this.title = (TextView) itemView.findViewById(R.id.title);
            this.totalamount_txt = (TextView) itemView.findViewById(R.id.totalamount_txt);
            this.date_txt = (TextView) itemView.findViewById(R.id.date_txt);
            this.status = (TextView) itemView.findViewById(R.id.status);

        }
    }
}