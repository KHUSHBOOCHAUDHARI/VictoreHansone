package com.business.agrocoin.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.business.agrocoin.Activity.MainActivity;
import com.business.agrocoin.Fragment.ReceivedFragment;
import com.business.agrocoin.Fragment.WalletFragment;
import com.business.agrocoin.Model.WalletModel;
import com.business.agrocoin.R;

import java.util.List;

public class WalletAdaptor extends RecyclerView.Adapter<WalletAdaptor.ViewHolder> {
    private List<WalletModel> listdata;
    private Activity activity;
    String RECEIVED_VALUE="";

    public WalletAdaptor(Activity activity, List<WalletModel> listdata) {
        this.activity = activity;
        this.listdata = listdata;



    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.wallet_list_item, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final WalletModel myListData = listdata.get(position);
        holder.name_txt.setText(listdata.get(position).getTitle());
        holder.addres_txt.setText(listdata.get(position).getAddress());


        holder.received_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WalletFragment.TYTPE="1";

                RECEIVED_VALUE=holder.addres_txt.getText().toString();
                //Toast.makeText(activity, holder.addres_txt.getText().toString(), Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putString("type",holder.addres_txt.getText().toString());
                ReceivedFragment searchFragment = new ReceivedFragment();
                searchFragment.setArguments(bundle);
                ((AppCompatActivity) activity).getSupportFragmentManager().beginTransaction().replace(R.id.container, searchFragment).commit();
                activity.overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                MainActivity.profilelogo.setImageTintList(ContextCompat.getColorStateList(activity, R.color.white));
                MainActivity.profiletxt.setTextColor(ContextCompat.getColor(activity, R.color.white));
                MainActivity.walletlogo.setImageTintList(ContextCompat.getColorStateList(activity, R.color.lightwhite));
                MainActivity.historylogo.setImageTintList(ContextCompat.getColorStateList(activity, R.color.lightwhite));
                MainActivity.withdrologo.setImageTintList(ContextCompat.getColorStateList(activity, R.color.lightwhite));

                MainActivity.wallettxt.setTextColor(ContextCompat.getColor(activity, R.color.lightwhite));
                MainActivity.historytxt.setTextColor(ContextCompat.getColor(activity, R.color.lightwhite));
                MainActivity.withdrotxt.setTextColor(ContextCompat.getColor(activity, R.color.lightwhite));

              //  RECEIVED_VALUE="Click";
            }
        });

        holder.copy_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", holder.addres_txt.getText().toString());
                Toast.makeText(activity, "Text Copy", Toast.LENGTH_SHORT).show();
                clipboard.setPrimaryClip(clip);
            }
        });


//        AddAddressActivity.name.setText(listdata.get(position).getName());
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AddAddressActivity.name.setText(listdata.get(position).getName());
//                AddAddressActivity.bottomSheetDialog.dismiss();
//                //                int previousItem = selectedItem;
////                selectedItem = position;
////
////                notifyItemChanged(previousItem);
////                notifyItemChanged(position);
//
//            }
//        });


    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name_txt,addres_txt,copy_txt;
        RelativeLayout layout;
        TextView received_txt;

        public ViewHolder(View itemView) {
            super(itemView);
            this.name_txt = (TextView) itemView.findViewById(R.id.name_txt);
            this.addres_txt = (TextView) itemView.findViewById(R.id.address_txt);
            this.copy_txt=(TextView) itemView.findViewById(R.id.copy_txt);
            this.layout = (RelativeLayout) itemView.findViewById(R.id.layout);
            this.received_txt=(TextView) itemView.findViewById(R.id.received_txt);

        }
    }
}