package com.business.cryptosoar.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.business.cryptosoar.Activity.AddAddressActivity;
import com.business.cryptosoar.Activity.AddressListActivity;
import com.business.cryptosoar.Model.CoinListModel;
import com.business.cryptosoar.Model.CryproMethod;
import com.business.cryptosoar.R;

import java.util.ArrayList;
import java.util.List;

public class CoinListAdapter extends RecyclerView.Adapter<CoinListAdapter.ViewHolder>{
    private List<CoinListModel> listdata;
    private Activity activity;
    private List<CryproMethod> cryproMethod;

    public CoinListAdapter(Activity activity, List<CoinListModel> listdata) {
        this.activity = activity;
        this.listdata = listdata;
        cryproMethod = new ArrayList<CryproMethod>();


    }

    @Override
    public CoinListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.coin_list_item, parent, false);
        return new CoinListAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(CoinListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final CoinListModel myListData = listdata.get(position);
        holder.name_txt.setText(listdata.get(position).getName());



        AddAddressActivity.name.setText(listdata.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddAddressActivity.name.setText(listdata.get(position).getName());
                AddAddressActivity.bottomSheetDialog.dismiss();
           //                int previousItem = selectedItem;
//                selectedItem = position;
//
//                notifyItemChanged(previousItem);
//                notifyItemChanged(position);

            }
        });




    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name_txt;
        RelativeLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.name_txt = (TextView) itemView.findViewById(R.id.name_txt);
            this.layout=(RelativeLayout) itemView.findViewById(R.id.layout);

        }
    }
}

