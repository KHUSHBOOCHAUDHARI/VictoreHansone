package com.business.cryptosoar.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.business.cryptosoar.Activity.FilterActivity;
import com.business.cryptosoar.Activity.FilterDetailActivity;
import com.business.cryptosoar.Activity.MainActivity;
import com.business.cryptosoar.Activity.WithdrowFudThreeActivity;
import com.business.cryptosoar.Model.FilterListModel;
import com.business.cryptosoar.Model.PaymentSetingListModel;
import com.business.cryptosoar.R;
import com.business.cryptosoar.Util.Method;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FilterListAdapter extends RecyclerView.Adapter<FilterListAdapter.ViewHolder>{
    private List<FilterListModel> listdata;
    private Activity activity;
    Method method;

    public FilterListAdapter(Activity activity, List<FilterListModel> listdata) {
        this.activity = activity;
        this.listdata = listdata;
        method=new Method(activity);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.monthly_list, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final FilterListModel myListData = listdata.get(position);
        holder.title.setText(listdata.get(position).getTitle());
        holder.totalamount_txt.setText("$"+listdata.get(position).getTotal_amt());

        holder.status.setText(listdata.get(position).getStatus());
        holder.date_txt.setText(date_format(listdata.get(position).getDate()));
        if (listdata.get(position).getStatus().equalsIgnoreCase("accept"))
        {
            holder.status.setText("Completed");
            holder.UserPinCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                if (listdata.get(position).getTitle().equals("Reinvestment")) {
                    String Baase = listdata.get(position).getTotal_amt();
                    String min = method.pref.getString(method.withdrawal_minimum_limit, null);
                    // String max=method.pref.getString(method.addfund_maximum_diposit,null);
                    double min_val = Double.parseDouble(min);
                    double Baase_val = Double.parseDouble(Baase);
                    if (min_val > Baase_val) {
                        method.alertBox("The Maximum withdraw amount is " + "$" + method.pref.getString(method.withdrawal_minimum_limit, null));
                    } else {
                        Intent intent = new Intent(activity, WithdrowFudThreeActivity.class);
                        intent.putExtra("amount", listdata.get(position).getTotal_amt());
                        intent.putExtra("id", listdata.get(position).getId());
                        intent.putExtra("title",listdata.get(position).getTitle());
                        activity.startActivity(intent);
                        activity.overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                    }

                }
                else
                {
                    Intent intent = new Intent(activity, WithdrowFudThreeActivity.class);
                    intent.putExtra("amount", listdata.get(position).getTotal_amt());
                    intent.putExtra("id", listdata.get(position).getId());
                    intent.putExtra("title",listdata.get(position).getTitle());
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.right_enter, R.anim.left_out);

                }


                }
            });


        }
        else if (listdata.get(position).getStatus().equalsIgnoreCase("request"))
        {
            holder.status.setText("Pending");
            holder.status.setTextColor(Color.parseColor("#FF7373"));

            holder.UserPinCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    method.alertBox("Your request is pending.");
                }
            });


        }

    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title,totalamount_txt,date_txt,status;
        RelativeLayout UserPinCardView;


        public ViewHolder(View itemView) {
            super(itemView);


            this.title = (TextView) itemView.findViewById(R.id.title);
            this.totalamount_txt = (TextView) itemView.findViewById(R.id.totalamount_txt);
            this.date_txt = (TextView) itemView.findViewById(R.id.date_txt);
            this.status = (TextView) itemView.findViewById(R.id.status);
            UserPinCardView=itemView.findViewById(R.id.UserPinCardView);
        }
    }
    public String date_format(String strDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date varDate = dateFormat.parse(strDate);
//            dateFormat=new SimpleDateFormat("dd MMM EEE yyyy");
            dateFormat = new SimpleDateFormat("MMM yyyy");
            System.out.println("Date :" + dateFormat.format(varDate));
            return dateFormat.format(varDate);
        } catch (Exception e) {
            e.printStackTrace();
            return strDate;
        }


    }
}