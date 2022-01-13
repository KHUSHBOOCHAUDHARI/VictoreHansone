package com.business.cryptosoar.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Network;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.business.cryptosoar.Activity.AddAddressActivity;
import com.business.cryptosoar.Model.NetworkListModel;
import com.business.cryptosoar.R;

import java.util.List;

public class NetworkListAdapter extends RecyclerView.Adapter<NetworkListAdapter.ViewHolder>{
    private List<NetworkListModel> listdata;
    private Activity activity;
    private int lastPosition = -1;
    int row_index = -1;
    int index;
    public NetworkListAdapter(Activity activity, List<NetworkListModel> listdata) {
        this.activity = activity;
        this.listdata = listdata;
    index=0;

    }

    @Override
    public NetworkListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.network_list_item, parent, false);
        return new NetworkListAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(NetworkListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        final NetworkListModel myListData = listdata.get(position);
        holder.name_txt.setText(listdata.get(position).getName());



        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index=position;
                notifyDataSetChanged();
                AddAddressActivity.Network=listdata.get(position).getName().toString();
            }
        });

        if (row_index==position) {
            holder.layout.setBackgroundTintList(ColorStateList.valueOf(activity.getResources().getColor(R.color.colorPrimaryDark)));
            holder.name_txt.setTextColor(Color.parseColor("#000000"));
        } else {
            holder.layout.setBackgroundTintList(ColorStateList.valueOf(activity.getResources().getColor(R.color.toolbar)));
            holder.name_txt.setTextColor(Color.parseColor("#ffffff"));
        }

         setAnimation(holder.itemView, position);



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
    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            //TranslateAnimation anim = new TranslateAnimation(0,-1000,0,-1000);
            ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            //anim.setDuration(new Random().nextInt(501));//to make duration random number between [0,501)
            anim.setDuration(550);//to make duration random number between [0,501)
            viewToAnimate.startAnimation(anim);
            lastPosition = position;

        }
    }
}

