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
import com.business.cryptosoar.Model.PaymentSetingListModel;
import com.business.cryptosoar.Model.ReferralModel;
import com.business.cryptosoar.R;
import com.business.cryptosoar.Util.Apis;
import com.business.cryptosoar.Util.Method;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class PaymentSettingListAdapter extends RecyclerView.Adapter<PaymentSettingListAdapter.ViewHolder>{
    private List<PaymentSetingListModel> listdata;
    private Activity activity;
    Method method;

    public PaymentSettingListAdapter(Activity activity, List<PaymentSetingListModel> listdata) {
        this.activity = activity;
        this.listdata = listdata;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.payment_setting_list, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final PaymentSetingListModel myListData = listdata.get(position);
        method=new Method(activity);
        // holder.name.setText(listdata.get(position).getName());
        holder.address_txt.setText(listdata.get(position).getAddress());
        holder.wallet_txt.setText(listdata.get(position).getLable());
        holder.imageView_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteCartItem(method.pref.getString(method.Id,null),listdata.get(position).getId(), position);

            }
        });
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name,address_txt,wallet_txt;
        public ImageView imageView_pro;

        public ViewHolder(View itemView) {
            super(itemView);


            this.name = (TextView) itemView.findViewById(R.id.title);
            this.address_txt = (TextView) itemView.findViewById(R.id.address_txt);
            this.wallet_txt = (TextView) itemView.findViewById(R.id.wallet_txt);
            this.imageView_pro=(ImageView) itemView.findViewById(R.id.imageView_pro);

        }
    }


    private void DeleteCartItem(String user_id,String address_id,int position) {
        AddressListActivity.progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("user_id", user_id);
        params.add("address_id",address_id);
        String url = Apis.ROOT_URL + activity.getString(R.string.delete_address);

        client.post(url, params,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (activity != null) {
                    Log.d("Response", new String(responseBody));
                    String res = new String(responseBody);
                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("true")) {

                            String Message=jsonObject.getString("message");

                            Toast.makeText(activity, Message.toString(), Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(activity, AddressListActivity.class);

                            activity.startActivity(i);
                            activity.overridePendingTransition(R.anim.right_enter,R.anim.left_out);
                            activity.finish();
                            Toast.makeText(activity, Message.toString(), Toast.LENGTH_SHORT).show();
                        } else {
                            String Message=jsonObject.getString("message");
                            Toast.makeText(activity,Message.toString(), Toast.LENGTH_SHORT).show();
                        }
                        AddressListActivity.progressBar.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        AddressListActivity.progressBar.setVisibility(View.GONE);
                        method.alertBox(activity.getResources().getString(R.string.failed_try_again));
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                AddressListActivity.progressBar.setVisibility(View.GONE);
                method.alertBox(activity.getResources().getString(R.string.something_went_wrong));
            }
        });
    }
}