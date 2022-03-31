package com.business.victorehansone.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.business.victorehansone.Adapter.OfferAdapter;
import com.business.victorehansone.Adapter.TransactionHistoryAdapter;
import com.business.victorehansone.Model.OfferModel;
import com.business.victorehansone.Model.TransactionModel;
import com.business.victorehansone.R;
import com.business.victorehansone.Util.Apis;
import com.business.victorehansone.Util.Method;
import com.business.victorehansone.databinding.ActivityOfferBinding;
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

public class OfferActivity extends AppCompatActivity {
    private ActivityOfferBinding binding;
    Method method;
    List<OfferModel> list = new ArrayList<>();
    OfferAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(OfferActivity.this,R.layout.activity_offer);
        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(R.anim.right_enter,R.anim.left_out);
            }
        });
        method= new Method(OfferActivity.this);
        binding.offersRecyclerview.setHasFixedSize(true);
        binding.offersRecyclerview.setLayoutManager(new LinearLayoutManager(OfferActivity.this));
        binding.offersRecyclerview.setAdapter(adapter);
        if (Method.isNetworkAvailable(OfferActivity.this)) {
            binding.textKeywordHide.setVisibility(View.GONE);
            OfferList("","offers");
        }
        else
        {
            binding.textKeywordHide.setVisibility(View.VISIBLE);
            method.alertBox("Internet connection not available");
        }



        fullscreen();
    }
    //Login Api
    public void OfferList(String id,String F) {
        binding.progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("user_id", id);
        params.add("f", F);
        String url = Apis.ROOT_URLLL + getString(R.string.login_ic_user);
        String someData = "{\"user_id\":\""+id+"\",\"f\":\""+F+"\"}";
        StringEntity entity = null;
        try {
            entity = new StringEntity(someData);
            entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch(Exception e) {

        }

        client.addHeader("api-token","4db248bc10877bef6f3008ef64e5c76a");
        Log.e("Url------->", url);
        client.post(OfferActivity.this,url, entity,"application/json", new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);
                Log.e("Url------->", res.toString());
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("200")) {
                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObjectt = jsonArray.getJSONObject(i);
                            String the_title = jsonObjectt.getString("the_title");
                            String the_content = jsonObjectt.getString("the_content");
                            String image=jsonObjectt.getString("image");
                            list.add(new OfferModel("",the_title,the_content,image));
                        }

                        if (list.size() == 0)
                        {
                            binding.offersRecyclerview.setVisibility(View.GONE);
                            binding.textNoOffers.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            binding.progressBar.setVisibility(View.GONE);
                            adapter = new OfferAdapter(OfferActivity.this,list );
                            binding.offersRecyclerview.setAdapter(adapter);
                            binding.textNoOffers.setVisibility(View.GONE);
                        }




                    }
                    else {
                        binding.progressBar.setVisibility(View.GONE);
                        String message = jsonObject.getString("message");
                        binding.textNoOffers.setVisibility(View.VISIBLE);
                        binding.textNoOffers.setText(message.toString());
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