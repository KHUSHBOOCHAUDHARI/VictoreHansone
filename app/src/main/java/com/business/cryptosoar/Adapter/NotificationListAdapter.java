package com.business.cryptosoar.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.business.cryptosoar.Activity.AddressListActivity;
import com.business.cryptosoar.Model.NotificationModel;
import com.business.cryptosoar.Model.PaymentSetingListModel;
import com.business.cryptosoar.R;
import com.business.cryptosoar.Util.Apis;
import com.business.cryptosoar.Util.Method;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.ViewHolder>{
    private List<NotificationModel> listdata;
    private Activity activity;
    Method method;

    public NotificationListAdapter(List<NotificationModel> listdata, Activity activity) {
        this.listdata = listdata;
        this.activity = activity;
        method=new Method(activity);
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

        public TextView title,date,messageTxt;
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