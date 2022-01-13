package com.business.cryptosoar.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.business.cryptosoar.Model.ContractListModel;
import com.business.cryptosoar.Model.ReferralModel;
import com.business.cryptosoar.R;

import java.util.List;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder>{
    private List<ContractListModel> listdata;
    private Activity activity;

    public ContactListAdapter(Activity activity, List<ContractListModel> listdata) {
        this.activity = activity;
        this.listdata = listdata;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.contact_list, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ContractListModel myListData = listdata.get(position);
         holder.status.setText(listdata.get(position).getMonth());
        holder.des_txt.setText(listdata.get(position).getDiscriptio());
        holder.con_value_txt.setText(listdata.get(position).getValue());

    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView status,des_txt,con_value_txt;

        public ViewHolder(View itemView) {
            super(itemView);


            this.status = (TextView) itemView.findViewById(R.id.status);
            this.des_txt = (TextView) itemView.findViewById(R.id.des_txt);
            this.con_value_txt = (TextView) itemView.findViewById(R.id.con_value_txt);


        }
    }
}