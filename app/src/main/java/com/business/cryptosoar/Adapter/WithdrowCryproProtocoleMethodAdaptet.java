package com.business.cryptosoar.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.business.cryptosoar.Model.CryproProtocolMethod;
import com.business.cryptosoar.R;

import java.util.List;

public class WithdrowCryproProtocoleMethodAdaptet  extends RecyclerView.Adapter<WithdrowCryproProtocoleMethodAdaptet.ViewHolder>{
    private List<CryproProtocolMethod> listdata;
    private Activity activity;
    public static String IDD="";
    private int lastPosition = -1;
    int row_index = -1;
    int index;
    public WithdrowCryproProtocoleMethodAdaptet(Activity activity, List<CryproProtocolMethod> listdata) {
        this.activity = activity;
        this.listdata = listdata;


    }

    @Override
    public WithdrowCryproProtocoleMethodAdaptet.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.crypto_pro_list, parent, false);
        return new WithdrowCryproProtocoleMethodAdaptet.ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(WithdrowCryproProtocoleMethodAdaptet.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final CryproProtocolMethod myListData = listdata.get(position);
        holder.name_txt.setText(listdata.get(position).getAddress());
        holder.lable.setText(listdata.get(position).getLabel());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                WithDrowCryproTypeMethodAdaptet.ViewHolder.address_layout.setVisibility(View.VISIBLE);



                IDD=listdata.get(position).getProId();
                WithDrowCryproTypeMethodAdaptet.ViewHolder.to_txt.setText(listdata.get(position).getToaddress());
                WithDrowCryproTypeMethodAdaptet.ViewHolder.notess_txt.setText("NOTES:-"+listdata.get(position).getAdmin_crypto_note());
                row_index=position;
                notifyDataSetChanged();
            }
        });
        if (row_index==position) {
            holder.layout.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
            holder.name_txt.setTextColor(Color.parseColor("#000000"));
            holder.lable.setTextColor(Color.parseColor("#000000"));
        } else {
            holder.layout.setBackgroundColor(activity.getResources().getColor(R.color.toolbar));
            holder.name_txt.setTextColor(Color.parseColor("#ffffff"));
            holder.lable.setTextColor(Color.parseColor("#ffffff"));
        }
        setAnimation(holder.itemView, position);
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView lable;
        public TextView name_txt;
        public RelativeLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.lable=(TextView) itemView.findViewById(R.id.lable);
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
