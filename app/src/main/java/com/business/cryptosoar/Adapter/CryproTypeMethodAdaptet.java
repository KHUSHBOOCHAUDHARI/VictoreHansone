package com.business.cryptosoar.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.business.cryptosoar.Activity.AddAddressActivity;
import com.business.cryptosoar.Model.CryproMethod;

import com.business.cryptosoar.Model.CryproProtocolMethod;
import com.business.cryptosoar.R;
import com.business.cryptosoar.Util.Method;

import java.util.ArrayList;
import java.util.List;

public class CryproTypeMethodAdaptet extends RecyclerView.Adapter<CryproTypeMethodAdaptet.ViewHolder>{
    private List<CryproMethod> listdata;
    private Activity activity;
    private List<CryproProtocolMethod> cryproProtocolMethods;
    public static String ID="";
    Method method;
    public CryproTypeMethodAdaptet(Activity activity, List<CryproMethod> listdata) {
        this.activity = activity;
        this.listdata = listdata;

        cryproProtocolMethods = new ArrayList<CryproProtocolMethod>();
        method=new Method(activity);
    }

    @Override
    public CryproTypeMethodAdaptet.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.crypto_opt_list, parent, false);
        return new CryproTypeMethodAdaptet.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(CryproTypeMethodAdaptet.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final CryproMethod myListData = listdata.get(position);
        holder.name_txt.setText(listdata.get(position).getName());
        cryproProtocolMethods=myListData.getArrayList();
        Log.e("kkkkkkkkkk",cryproProtocolMethods.toString());


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false);
        holder.layout_recy.setLayoutManager(layoutManager);
        CryproProtocoleMethodAdaptet adapter = new CryproProtocoleMethodAdaptet(activity,cryproProtocolMethods);
        holder.layout_recy.setAdapter(adapter);

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(activity, AddAddressActivity.class);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.right_enter,R.anim.left_out);
            }
        });
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ID=listdata.get(position).getId();
                if (holder.layout_recy.getVisibility() == View.GONE) {
                    if (cryproProtocolMethods.size() ==0)
                    {
                        method.alertBox("No Data Found");
                    }
                    else
                    {
                        holder.relativeLayout.setBackgroundColor(activity.getResources().getColor(R.color.toolbar));
                        holder.layout_recy.setVisibility(View.VISIBLE);
                        holder.to_txt.setVisibility(View.VISIBLE);
                        holder.notess_txt.setVisibility(View.VISIBLE);
                    }

                }
                else if (holder.layout_recy.getVisibility()==View.VISIBLE)
                {
                    holder.layout_recy.setVisibility(View.GONE);
                    holder.to_txt.setVisibility(View.VISIBLE);
                    holder.notess_txt.setVisibility(View.VISIBLE);
                }


            }
        });

    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name_txt;
        RecyclerView layout_recy;
        RelativeLayout relativeLayout,layout,add;
        ImageView cryptocurrancypo_logo,cryptocurrancyme_logop;
        public static RelativeLayout address_layout;
        public static TextView to_txt,notess_txt;
        public ViewHolder(View itemView) {
            super(itemView);
            this.name_txt = (TextView) itemView.findViewById(R.id.name_txt);
            this.layout_recy=(RecyclerView) itemView.findViewById(R.id.layoutt_recy);
            relativeLayout=(RelativeLayout) itemView.findViewById(R.id.cryptocurrancy_lyt);
            cryptocurrancypo_logo=(ImageView) itemView.findViewById(R.id.cryptocurrancypo_logo);
            layout=(RelativeLayout) itemView.findViewById(R.id.layout);
            add=(RelativeLayout) itemView.findViewById(R.id.add_lyt);
            address_layout=itemView.findViewById(R.id.address_layout);
            to_txt=itemView.findViewById(R.id.to_txt);
            notess_txt=itemView.findViewById(R.id.notess_txt);
        }
    }
}
