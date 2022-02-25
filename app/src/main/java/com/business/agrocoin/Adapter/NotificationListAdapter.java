package com.business.agrocoin.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.business.agrocoin.Model.NotificationModel;
import com.business.agrocoin.R;
import com.business.agrocoin.Util.Method;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.ViewHolder> {
    private List<NotificationModel> listdata;
    private Activity activity;
    Method method;

    public NotificationListAdapter(List<NotificationModel> listdata, Activity activity) {
        this.listdata = listdata;
        this.activity = activity;
        method = new Method(activity);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.notificatio_list, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final NotificationModel myListData = listdata.get(position);
        // holder.name.setText(listdata.get(position).getName());
        holder.title.setText(listdata.get(position).getTitle());
        holder.date.setText(date_format(listdata.get(position).getDate()));
        holder.messageTxt.setText(listdata.get(position).getMessage());

    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title, date, messageTxt;
        public ImageView imageView_pro;

        public ViewHolder(View itemView) {
            super(itemView);


            this.title = (TextView) itemView.findViewById(R.id.title);
            this.date = (TextView) itemView.findViewById(R.id.date);
            this.messageTxt = (TextView) itemView.findViewById(R.id.messageTxt);


        }
    }

    public String date_format(String strDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date varDate = dateFormat.parse(strDate);
//            dateFormat=new SimpleDateFormat("dd MMM EEE yyyy");
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            System.out.println("Date :" + dateFormat.format(varDate));
            return dateFormat.format(varDate);
        } catch (Exception e) {
            e.printStackTrace();
            return strDate;
        }


    }


}