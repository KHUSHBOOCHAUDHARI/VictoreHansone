package com.business.victorehansone.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.business.victorehansone.Model.NotificationModel;
import com.business.victorehansone.Model.SubscriptionModel;
import com.business.victorehansone.R;
import com.business.victorehansone.Util.Method;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SubscriptiobAdapter extends RecyclerView.Adapter<SubscriptiobAdapter.ViewHolder> {
    private List<SubscriptionModel> listdata;
    private Activity activity;
    Method method;

    public SubscriptiobAdapter(List<SubscriptionModel> listdata, Activity activity) {
        this.listdata = listdata;
        this.activity = activity;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.subscription_layout, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final SubscriptionModel myListData = listdata.get(position);
        method = new Method(activity);
        holder.title.setText(listdata.get(position).getTitle());
        holder.price_txt.setText(date_format(listdata.get(position).getPrice()));
        holder.messgae_txt.setText(listdata.get(position).getMessage());

        holder.signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link="https://victorhanson.com/subscription-plan/";
                Uri uri = Uri.parse(link); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                activity.startActivity(intent);
               activity.overridePendingTransition(R.anim.right_enter, R.anim.left_out);
            }
        });


    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title, price_txt, messgae_txt,signin_btn,title_new;
        public ImageView imageView_pro;

        public ViewHolder(View itemView) {
            super(itemView);


            this.title = (TextView) itemView.findViewById(R.id.title);
            this.price_txt = (TextView) itemView.findViewById(R.id.price_txt);
            this.messgae_txt = (TextView) itemView.findViewById(R.id.messgae_txt);
            this.signin_btn = (TextView) itemView.findViewById(R.id.login_btn);
            this.title_new=(TextView) itemView.findViewById(R.id.title_new);



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