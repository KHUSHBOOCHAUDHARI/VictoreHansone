package com.business.agrocoin.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.business.agrocoin.Model.TransactionModel;
import com.business.agrocoin.R;
import com.business.agrocoin.Util.Method;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TransactionHistoryAdapter extends RecyclerView.Adapter<TransactionHistoryAdapter.ViewHolder> {
    private List<TransactionModel> listdata;
    private Activity activity;
    public static Dialog bottomSheetDialogg;
    Method method;
    TextView nodat;
    TextView send_btn;
    ProgressBar progressBardialog;
    public static String ID = "";
    public static String TYPE = "";

    public TransactionHistoryAdapter(Activity activity, List<TransactionModel> listdata) {
        this.activity = activity;
        this.listdata = listdata;
        method = new Method(activity);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.all_transection_list, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final TransactionModel myListData = listdata.get(position);
        holder.title.setText(listdata.get(position).getTitle());
        holder.totalamount_txt.setText("AGRO "+listdata.get(position).getTotal_amt());
        holder.date_txt.setText(date_formatnew(listdata.get(position).getDate()));
        holder.status.setText(listdata.get(position).getStatus());
        holder.plane_txt.setText(listdata.get(position).getType());

        holder.plane_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", holder.plane_txt.getText().toString());
                Toast.makeText(activity, "Txn Id Copy", Toast.LENGTH_SHORT).show();
                clipboard.setPrimaryClip(clip);
            }
        });

    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title, totalamount_lable, totalamount_txt, date_txt, date_lable, status, plane_txt;
        RelativeLayout UserPinCardView;
        LinearLayout planelyt;

        public ViewHolder(View itemView) {
            super(itemView);

            this.totalamount_txt = (TextView) itemView.findViewById(R.id.totalamount_txt);
            this.date_lable = (TextView) itemView.findViewById(R.id.date_lable);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.date_txt = (TextView) itemView.findViewById(R.id.date_txt);
            this.status = (TextView) itemView.findViewById(R.id.status);
            this.plane_txt = (TextView) itemView.findViewById(R.id.plane_txt);
            this.planelyt = (LinearLayout) itemView.findViewById(R.id.planelyt);
            this.UserPinCardView = (RelativeLayout) itemView.findViewById(R.id.UserPinCardView);
        }
    }

    public void AlertDialogDetail() {

    }

    public String date_format(String strDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        try {
            Date varDate = dateFormat.parse(strDate);
//            dateFormat=new SimpleDateFormat("dd MMM EEE yyyy");
            dateFormat = new SimpleDateFormat("MMMM-yyyy");
            System.out.println("Date :" + dateFormat.format(varDate));
            return dateFormat.format(varDate);
        } catch (Exception e) {
            e.printStackTrace();
            return strDate;
        }


    }

    public String date_formatnew(String strDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        try {
            Date varDate = dateFormat.parse(strDate);
//            dateFormat=new SimpleDateFormat("dd MMM EEE yyyy");
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println("Date :" + dateFormat.format(varDate));
            return dateFormat.format(varDate);
        } catch (Exception e) {
            e.printStackTrace();
            return strDate;
        }


    }

}