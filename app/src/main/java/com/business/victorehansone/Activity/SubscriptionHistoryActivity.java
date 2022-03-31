package com.business.victorehansone.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.business.victorehansone.Adapter.CategoryAdapter;
import com.business.victorehansone.Adapter.CommentAdapter;
import com.business.victorehansone.Adapter.TransactionHistoryAdapter;
import com.business.victorehansone.Model.CategoryModel;
import com.business.victorehansone.Model.SearechModel;
import com.business.victorehansone.Model.TransactionModel;
import com.business.victorehansone.R;
import com.business.victorehansone.Util.Apis;
import com.business.victorehansone.Util.Method;
import com.business.victorehansone.databinding.ActivitySubscribeBinding;
import com.business.victorehansone.databinding.ActivitySubscriptionHistoryBinding;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class SubscriptionHistoryActivity extends AppCompatActivity {
    private ActivitySubscriptionHistoryBinding binding;
    Method method;
    List<TransactionModel> list = new ArrayList<>();
    TransactionHistoryAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(SubscriptionHistoryActivity.this,R.layout.activity_subscription_history);
        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(R.anim.right_enter,R.anim.left_out);
            }
        });


        method= new Method(SubscriptionHistoryActivity.this);
        binding.subscribeRecyclerview.setHasFixedSize(true);
        binding.subscribeRecyclerview.setLayoutManager(new LinearLayoutManager(SubscriptionHistoryActivity.this));
        binding.subscribeRecyclerview.setAdapter(adapter);

        if (Method.isNetworkAvailable(SubscriptionHistoryActivity.this)) {
            binding.textViewCategory.setVisibility(View.GONE);
            SubscriptionHistory(method.pref.getString(method.Id,null),"subscription_history");
        }
        else
        {
            binding.progressBar.setVisibility(View.GONE);
            binding.layout.setVisibility(View.GONE);
            binding.textViewCategory.setVisibility(View.VISIBLE);
            binding.textViewCategory.setText(getResources().getString(R.string.internet_connection));
        }
        fullscreen();
    }

    //Subscription History Api
    public void SubscriptionHistory(String user_id,String F) {
        binding.progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("user_id", user_id);
        params.add("f", F);
        String url = Apis.ROOT_URLLL + getString(R.string.login_ic_user);
        String someData = "{\"user_id\":\""+user_id+"\",\"f\":\""+F+"\"}";
        StringEntity entity = null;
        try {
            entity = new StringEntity(someData);
            entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch(Exception e) {

        }

        client.addHeader("api-token","4db248bc10877bef6f3008ef64e5c76a");
        Log.e("Url------->", url);
        client.post(SubscriptionHistoryActivity.this,url, entity,"application/json", new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);
                Log.e("Url------->", res.toString());
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("200")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        JSONArray jsonArray=jsonObject1.getJSONArray("subscription_history");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObjectt = jsonArray.getJSONObject(i);
                            String id = jsonObjectt.getString("id");
                            String paydate = jsonObjectt.getString("paydate");
                            String txn_id = jsonObjectt.getString("txn_id");
                            JSONObject jsonObject2=jsonObjectt.getJSONObject("payment_data");
                            String transaction_id=jsonObject2.getString("transaction_id");
                            String transaction_subject=jsonObject2.getString("transaction_subject");
                            String payment_date=jsonObject2.getString("payment_date");
                            String payment_type=jsonObject2.getString("payment_type");
                            String payment_gross =jsonObject2.getString("payment_gross");
                            String payment_status =jsonObject2.getString("payment_status");
                            String mc_currency=jsonObject2.getString("mc_currency");
                            String order_id=jsonObject2.getString("order_id");
                            String amount=payment_gross+mc_currency;
                            String Code="IUMP00"+order_id;
                            list.add(new TransactionModel(id,transaction_subject,amount,payment_type,transaction_id,payment_date,payment_status,Code));
                        }

                        if (list.size()==0)
                        {
                            binding.progressBar.setVisibility(View.GONE);
                            binding.textViewCategory.setVisibility(View.VISIBLE);
                        }
                        else
                        {

                            binding.progressBar.setVisibility(View.GONE);
                            adapter = new TransactionHistoryAdapter(SubscriptionHistoryActivity.this,list );
                            binding.subscribeRecyclerview.setAdapter(adapter);
                            binding.textViewCategory.setVisibility(View.GONE);

                        }






                    }
                    else {
                        binding.progressBar.setVisibility(View.GONE);
                        String message = jsonObject.getString("message");

                        binding.textViewCategory.setVisibility(View.VISIBLE);
                        binding.textViewCategory.setText(message.toString());
                    }
                    binding.progressBar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                    binding.progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                binding.progressBar.setVisibility(View.GONE);
                method.alertBox(getResources().getString(R.string.something_went_wrong));
            }
        });
    }

    //Full Screen
    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
    public void fullscreen() {

        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

    }

}