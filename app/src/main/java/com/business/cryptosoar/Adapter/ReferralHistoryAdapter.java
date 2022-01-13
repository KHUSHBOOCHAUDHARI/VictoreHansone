package com.business.cryptosoar.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.business.cryptosoar.Model.ReferralModel;
import com.business.cryptosoar.Model.TransactionModel;
import com.business.cryptosoar.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReferralHistoryAdapter extends RecyclerView.Adapter<ReferralHistoryAdapter.ViewHolder>{
    private List<ReferralModel> listdata;
    private Activity activity;

    public ReferralHistoryAdapter(Activity activity, List<ReferralModel> listdata) {
        this.activity = activity;
        this.listdata = listdata;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.referal_list, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ReferralModel myListData = listdata.get(position);
         holder.name.setText(listdata.get(position).getName());
        holder.referalcode.setText(listdata.get(position).getReferalcode());
        holder.date_txt.setText(listdata.get(position).getDate());
        holder.balance.setText("$"+listdata.get(position).getAmount());
        holder.date_txt.setText(date_format(listdata.get(position).getDate()));
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name,referalcode,date_txt,balance;

        public ViewHolder(View itemView) {
            super(itemView);


            this.name = (TextView) itemView.findViewById(R.id.name);
            this.referalcode = (TextView) itemView.findViewById(R.id.referral_txt);
            this.date_txt = (TextView) itemView.findViewById(R.id.date_txt);
            this.balance = (TextView) itemView.findViewById(R.id.balance);

        }
    }

    public String date_format(String strDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date varDate = dateFormat.parse(strDate);
//            dateFormat=new SimpleDateFormat("dd MMM EEE yyyy");
            dateFormat = new SimpleDateFormat("dd MMM yyyy");
            System.out.println("Date :" + dateFormat.format(varDate));
            return dateFormat.format(varDate);
        } catch (Exception e) {
            e.printStackTrace();
            return strDate;
        }


    }
}