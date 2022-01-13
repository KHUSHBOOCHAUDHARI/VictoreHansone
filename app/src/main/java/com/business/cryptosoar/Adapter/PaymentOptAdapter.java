package com.business.cryptosoar.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.business.cryptosoar.Model.CryproMethod;
import com.business.cryptosoar.Model.CryproProtocolMethod;
import com.business.cryptosoar.Model.PaymentOptModel;
import com.business.cryptosoar.R;
import com.hbb20.CountryCodePicker;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class PaymentOptAdapter extends RecyclerView.Adapter<PaymentOptAdapter.ViewHolder>{
    private List<PaymentOptModel> listdata;
    public static Activity activity;
    private List<CryproMethod> cryproMethod;
    private RecyclerView mRecyclerView;
    public static  String  method="0";
    private int amountOfItemsSelected=0;
    private int row_of_index = -1;

    public PaymentOptAdapter(Activity activity, List<PaymentOptModel> listdata) {
        this.activity = activity;
        this.listdata = listdata;
        cryproMethod = new ArrayList<CryproMethod>();

    }
    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        mRecyclerView = recyclerView;

    }

    @Override
    public PaymentOptAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.peyment_opt_list, parent, false);
        return new PaymentOptAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(PaymentOptAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final PaymentOptModel myListData = listdata.get(position);
        holder.name_txt.setText(listdata.get(position).getName());

        cryproMethod = myListData.getArrayList();

        Log.e("Datat------",cryproMethod.toString());
        Log.e("Datatkk------",cryproMethod.toString());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false);
        holder.layout_recy.setLayoutManager(layoutManager);
        CryproTypeMethodAdaptet adapter = new CryproTypeMethodAdaptet(activity,cryproMethod);
        holder.layout_recy.setAdapter(adapter);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                              method=listdata.get(position).getId();
                holder.relativeLayout.setBackgroundTintList(ContextCompat.getColorStateList(activity, R.color.toolbar));




                holder.contact_number_txt.setText(listdata.get(position).getContactno());
                holder.contact_person_txt.setText(listdata.get(position).getContactname());
                holder.state_city_txt.setText(listdata.get(position).getState());
           //     Toast.makeText(activity, holder.contact_number_txt.getText().toString(), Toast.LENGTH_SHORT).show();
                holder.banknumber_txt.setText(listdata.get(position).getBankaccooutno());
                holder.branchname_txt.setText(listdata.get(position).getBankaccountname());
                holder.ifsc_txt.setText(listdata.get(position).getBankaccountifsc());


                if (listdata.get(position).getId().equals("3"))
                {

                    holder.relativeLayout.setBackgroundTintList(ContextCompat.getColorStateList(activity, R.color.toolbar));


                    if (holder.kkk.getVisibility()==View.VISIBLE)
                    {

                        holder.kkk.setVisibility(View.GONE);
                        holder.layout_recy.setVisibility(View.GONE);
                        holder.cryptocurrancyme_logop.setVisibility(View.GONE);
                        holder.cryptocurrancypo_logo.setVisibility(View.VISIBLE);
                    }
                    else if (holder.kkk.getVisibility()==View.GONE)
                    {

                        holder.kkk.setVisibility(View.VISIBLE);
                        holder.layout_recy.setVisibility(View.GONE);
                        holder.cryptocurrancyme_logop.setVisibility(View.VISIBLE);
                        holder.cryptocurrancypo_logo.setVisibility(View.GONE);
                    }

                }
                else if (listdata.get(position).getId().equals("2"))
                {
                    holder.relativeLayout.setBackgroundTintList(ContextCompat.getColorStateList(activity, R.color.toolbar));

                    if (holder.coin_lyt.getVisibility()==View.VISIBLE)
                    {
                        holder.coin_lyt.setVisibility(View.GONE);
                        holder.layout_recy.setVisibility(View.GONE);
                        holder.cryptocurrancyme_logop.setVisibility(View.GONE);
                        holder.cryptocurrancypo_logo.setVisibility(View.VISIBLE);

                    }
                    else if (holder.coin_lyt.getVisibility()==View.GONE)
                    {

                        holder.coin_lyt.setVisibility(View.VISIBLE);
                        holder.layout_recy.setVisibility(View.GONE);
                        holder.cryptocurrancyme_logop.setVisibility(View.VISIBLE);
                        holder.cryptocurrancypo_logo.setVisibility(View.GONE);
                    }
                }

                else {

                    holder.relativeLayout.setBackgroundTintList(ContextCompat.getColorStateList(activity, R.color.toolbar));

                    if (holder.layout_recy.getVisibility() == View.GONE) {
                        holder.layout_recy.setVisibility(View.VISIBLE);
                        holder.kkk.setVisibility(View.GONE);
                        holder.coin_lyt.setVisibility(View.GONE);
                        holder.cryptocurrancyme_logop.setVisibility(View.VISIBLE);
                        holder.cryptocurrancypo_logo.setVisibility(View.GONE);
                    } else if (holder.layout_recy.getVisibility() == View.VISIBLE) {
                        holder.layout_recy.setVisibility(View.GONE);
                        holder.kkk.setVisibility(View.GONE);
                        holder.coin_lyt.setVisibility(View.GONE);
                        holder.cryptocurrancyme_logop.setVisibility(View.GONE);
                        holder.cryptocurrancypo_logo.setVisibility(View.VISIBLE);

                    }
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
        RelativeLayout relativeLayout,kkk,coin_lyt;
        CountryCodePicker ccp;
        ImageView cryptocurrancypo_logo,cryptocurrancyme_logop;
        TextView contact_person_txt,contact_number_txt,state_city_txt,banknumber_txt,branchname_txt;
        public static TextView ifsc_txt;
        public ViewHolder(View itemView) {
            super(itemView);
            this.name_txt = (TextView) itemView.findViewById(R.id.name_txt);
            this.layout_recy=(RecyclerView) itemView.findViewById(R.id.layout_recy);
            this.relativeLayout=(RelativeLayout) itemView.findViewById(R.id.cryptocurrancy_lyt);
            this.cryptocurrancyme_logop=(ImageView) itemView.findViewById(R.id.cryptocurrancyme_logo);
            this.cryptocurrancypo_logo=(ImageView) itemView.findViewById(R.id.cryptocurrancypo_logo);
            this.kkk=(RelativeLayout) itemView.findViewById(R.id.kkk);
            this.coin_lyt=(RelativeLayout) itemView.findViewById(R.id.coin_lyt);
            this.contact_person_txt=(TextView) itemView.findViewById(R.id.contact_person_txt);
            this.contact_number_txt=(TextView) itemView.findViewById(R.id.contact_number_txt);
            this.state_city_txt=(TextView) itemView.findViewById(R.id.state_city_txt);
            banknumber_txt=(TextView) itemView.findViewById(R.id.banknumber_txt);
            ifsc_txt=(TextView) itemView.findViewById(R.id.ifsc_txt);
            branchname_txt=(TextView) itemView.findViewById(R.id.branchname_txt);
            ccp=itemView.findViewById(R.id.ccp);



        }

    }
}
