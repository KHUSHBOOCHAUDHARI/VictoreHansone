package com.business.cryptosoar.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

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

import com.business.cryptosoar.R;
import com.business.cryptosoar.Util.Apis;
import com.business.cryptosoar.Util.Method;
import com.business.cryptosoar.databinding.ActivityDetailBinding;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class DetailActivity extends AppCompatActivity {
    private ActivityDetailBinding binding;
    Method method;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_detail);
        method=new Method(DetailActivity.this);
        binding.titleTxt.setText(getIntent().getStringExtra("name")+" Investment Deatail");
        Calculate(method.pref.getString(method.Id,null),getIntent().getStringExtra("fundid"));
        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.right_enter,R.anim.left_out);
                finish();
            }
        });

        binding.requerstNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        fullscreen();


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

    public void Calculate(String UserId,String fund_id) {

        binding.progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("user_id", UserId);
        params.add("fund_id", fund_id);
        String url = Apis.ROOT_URL + getString(R.string.end_contract_calculation);
        Log.e("Url------->",url);

        client.post(url,params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);
                Log.e("ressss------->",res.toString());
                try {

                    JSONObject jsonObject = new JSONObject(res);

                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        String contract_fund_amount = jsonObject1.getString("contract_fund_amount");
                        String contract_end_per = jsonObject1.getString("contract_end_per");
                        String contract_end_charge = jsonObject1.getString("contract_end_charge");
                        String contract_end_interest_per = jsonObject1.getString("contract_end_interest_per");
                        String contract_end_interest = jsonObject1.getString("contract_end_interest");
                        String contract_total = jsonObject1.getString("contract_total");
                        String months = jsonObject1.getString("months");
                        String days = jsonObject1.getString("days");
                        String fund_id = jsonObject1.getString("fund_id");
                        String contract_date = jsonObject1.getString("contract_date");

                        binding.title.setText(getIntent().getStringExtra("title"));
                        binding.createddateText.setText(getIntent().getStringExtra("date"));
                        binding.cancelationdateText.setText(getIntent().getStringExtra("date"));
                        binding.fundamountText.setText("$"+contract_fund_amount);
                        binding.cancellationchargeTag.setText("Cancellation Charge"+"("+contract_end_per+"%"+")");
                        binding.cancellationchargeText.setText("$"+contract_end_charge);
                        binding.interesTag.setText("Interest Amount"+"("+contract_end_interest_per+"%"+")");
                        binding.interestText.setText("$"+contract_end_interest);
                        binding.totallText.setText("$"+contract_total);
                        method.editor.putString(method.contract_fund_amount, contract_fund_amount);
                        method.editor.putString(method.contract_end_per, contract_end_per);
                        method.editor.putString(method.contract_end_charge, contract_end_charge);
                        method.editor.putString(method.contract_end_interest_per, contract_end_interest_per);
                        method.editor.putString(method.contract_end_interest, contract_end_interest);
                        method.editor.putString(method.contract_total, contract_total);



                        method.editor.putBoolean(method.pref_login, true);
                        method.editor.commit();


                    }

                    else {
                        binding.progressBar.setVisibility(View.GONE);
                        String message = jsonObject.getString("message");
                        Toast.makeText(DetailActivity.this, message.toString(), Toast.LENGTH_SHORT).show();
                    }
                    binding.progressBar.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    binding.progressBar.setVisibility(View.GONE);
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                binding.progressBar.setVisibility(View.GONE);
                method.alertBox(getResources().getString(R.string.something_went_wrong));
            }
        });
    }

}