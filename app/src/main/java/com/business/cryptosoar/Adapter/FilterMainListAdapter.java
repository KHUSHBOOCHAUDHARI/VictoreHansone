package com.business.cryptosoar.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.business.cryptosoar.Activity.FilterDetailActivity;
import com.business.cryptosoar.Activity.WithdrowFudThreeActivity;
import com.business.cryptosoar.Fragment.WalletFragment;
import com.business.cryptosoar.Model.FilterListModel;
import com.business.cryptosoar.Model.TransactionModel;
import com.business.cryptosoar.R;
import com.business.cryptosoar.Util.Apis;
import com.business.cryptosoar.Util.Method;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class FilterMainListAdapter extends RecyclerView.Adapter<FilterMainListAdapter.ViewHolder>{
    private List<FilterListModel> listdata;
    private Activity activity;
    Method method;

    int pos=0;
    private int lastPosition = -1;
    int row_index = -1;
    public static String FUND_ID="";
    private List<TransactionModel> transactionModels;
    TransactionHistoryByFundAdapter transactionHistoryByFundAdapter;

    public FilterMainListAdapter(Activity activity, List<FilterListModel> listdata) {
        this.activity = activity;
        this.listdata = listdata;
        method=new Method(activity);
        transactionModels=new ArrayList<>();


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.filter_list, parent, false);
        return new ViewHolder(view);

    }

    @Override public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final FilterListModel myListData = listdata.get(position);
        pos = position+1;
        holder.title.setText("$"+listdata.get(position).getTotal_amt()+ " (" + listdata.get(position).getTitle()+ ")");

          // holder.title.setChecked(listdata.get(position).isSelected());
        if (listdata.get(position).getId().equalsIgnoreCase(WalletFragment.Fund)) {
            holder.title.setChecked(true);


        }




        holder.title.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (holder.title.isChecked())
                {

                                   WalletFragment.bottomSheetDialogg.dismiss();
                    FUND_ID=listdata.get(position).getId();

                    WalletFragment.filterRecyclerview.setVisibility(View.VISIBLE);
                    WalletFragment.tansectionRecyclerview.setVisibility(View.GONE);
                    ViewAllProducts(method.pref.getString(method.Id,null),FilterMainListAdapter.FUND_ID,String.valueOf("1"),"100");
                    WalletFragment.filterRecyclerview.setNestedScrollingEnabled(false);
                    WalletFragment.filterRecyclerview.setHasFixedSize(false);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
                    WalletFragment.filterRecyclerview.setLayoutManager(layoutManager);
                    WalletFragment.filterRecyclerview.setFocusable(false);

                }
                else
                {
                    FUND_ID="";
                }
                row_index=position;
                notifyDataSetChanged();

            }
        });





        if (row_index==position) {
            holder.title.setChecked(true);


        }





    }
    @Override public int getItemCount() {
        return listdata.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView totalamount_txt,date_txt,status;
        RelativeLayout UserPinCardView;
        RadioButton title;
        RadioGroup radiogrup;

        public ViewHolder(View itemView) {
            super(itemView);


            this.title = (RadioButton) itemView.findViewById(R.id.offer_select);
            this.totalamount_txt = (TextView) itemView.findViewById(R.id.totalamount_txt);
            this.date_txt = (TextView) itemView.findViewById(R.id.date_txt);
            this.status = (TextView) itemView.findViewById(R.id.status);
            UserPinCardView=itemView.findViewById(R.id.UserPinCardView);
            radiogrup=itemView.findViewById(R.id.radiogrup);
        }
    }
    public String date_format(String strDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date varDate = dateFormat.parse(strDate);
//            dateFormat=new SimpleDateFormat("dd MMM EEE yyyy");
            dateFormat = new SimpleDateFormat("dd MMM yyyy");
            System.out.println("Date :" + dateFormat.format(varDate));
            return dateFormat.format(varDate);
        } catch (Exception e) {
            e.printStackTrace();
            return strDate;
        }


    }
    private void ViewAllProducts(String UserId, String FundId, String pagination_index, String page_limit) {

        transactionModels.clear();
        WalletFragment.progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("user_id",UserId);
        params.add("fund_id",FundId);
    params.add("page_index",pagination_index);
        params.add("page_limit",page_limit);
        String url = Apis.ROOT_URL + activity.getString(R.string.transection_history_byfund);
        client.cancelAllRequests(true);

        client.post(url,params,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                if (activity != null) {
                    Log.d("Response", new String(responseBody));
                    String res = new String(responseBody);

                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("true")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String id = jsonObject1.getString("id");
                                String order_date = jsonObject1.getString("order_date");
                                String type = jsonObject1.getString("type");
                                String text = jsonObject1.getString("text");
                                String amount = jsonObject1.getString("amount");
                                String transaction_status = jsonObject1.getString("transaction_status");
                                String transaction_type = jsonObject1.getString("transaction_type");
                                String date_month = jsonObject1.getString("date_month");
                                String date = jsonObject1.getString("date");
                                String time = jsonObject1.getString("time");
                                transactionModels.add(new TransactionModel(id, text, amount,transaction_type ,date, transaction_status,type));

                            }


                                if (transactionModels.size() != 0) {
                                    WalletFragment.progressBar.setVisibility(View.GONE);
                                    transactionHistoryByFundAdapter = new TransactionHistoryByFundAdapter(activity, transactionModels);
                                    WalletFragment.filterRecyclerview.setAdapter(transactionHistoryByFundAdapter);

                                    WalletFragment.nodata.setVisibility(View.GONE);
                                } else {
                                    WalletFragment.progressBar.setVisibility(View.GONE);
                                    WalletFragment.nodata.setVisibility(View.VISIBLE);
                                }
                        }
                        String account_balance=jsonObject.getString("account_balance");
                        String total_interest_paid=jsonObject.getString("total_interest_paid");
                        String accrued_interest=jsonObject.getString("accrued_interest");

                        WalletFragment.account_balance_txt.setText("$"+account_balance);
                        WalletFragment.toalinterest_txt.setText("$"+total_interest_paid);
                        WalletFragment.accrued_txt.setText("$"+accrued_interest);



                    } catch(JSONException e){
                        e.printStackTrace();
                        WalletFragment.progressBar.setVisibility(View.GONE);
                        WalletFragment.nodata.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                WalletFragment.progressBar.setVisibility(View.GONE);
            }
        });
    }

}