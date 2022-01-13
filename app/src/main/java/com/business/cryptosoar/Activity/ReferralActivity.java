package com.business.cryptosoar.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.business.cryptosoar.Adapter.ReferralHistoryAdapter;
import com.business.cryptosoar.Adapter.TransactionHistoryAdapter;
import com.business.cryptosoar.Model.ReferralModel;

import com.business.cryptosoar.Model.TransactionModel;
import com.business.cryptosoar.R;
import com.business.cryptosoar.Util.Apis;
import com.business.cryptosoar.Util.Method;
import com.business.cryptosoar.databinding.ActivityReferralBinding;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ReferralActivity extends AppCompatActivity {
    private ActivityReferralBinding binding;
    private List<ReferralModel> referralModels;
    ReferralHistoryAdapter referralHistoryAdapter;
    Method method;
    String referral_amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(ReferralActivity.this, R.layout.activity_referral);
        referralModels=new ArrayList<>();
        method=new Method(ReferralActivity.this);
        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.right_enter,R.anim.left_out);
                finish();
            }
        });
        WalletHistory(method.pref.getString(method.Id,null));


        binding.referalId.setText(method.pref.getString(method.unique_id,null));
        ReferalHistory(method.pref.getString(method.Id,null));

        binding.shareImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                String shareBody = binding.referalId.getText().toString();
                intent.setType("text/plain");
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.referalcode_txt));
                intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(intent, getString(R.string.referal_history)));
            }
        });

        binding.copyTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", binding.referalId.getText().toString());
                Toast.makeText(ReferralActivity.this, "Text Copy", Toast.LENGTH_SHORT).show();
                clipboard.setPrimaryClip(clip);
            }
        });

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(ReferralActivity.this,LinearLayoutManager.VERTICAL,false);
        binding.referallRecyclerview.setLayoutManager(linearLayoutManager);
        referralHistoryAdapter = new ReferralHistoryAdapter(ReferralActivity.this,referralModels);
        binding.referallRecyclerview.setAdapter(referralHistoryAdapter);
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

    public void ReferalHistory(String UserId) {
        referralModels.clear();
        binding.progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("user_id", UserId);
        String url = Apis.ROOT_URL + getString(R.string.referal_history);
        Log.e("Url------->",url);
        client.post(url,params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);
                Log.e("Url------->",res.toString());
                // Toast.makeText(getApplicationContext(),  new String(responseBody), Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("true")) {
                        JSONArray jsonArray=jsonObject.getJSONArray("data");

                        for (int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String id = jsonObject1.getString("id");
                            String user_name = jsonObject1.getString("user_name");
                            String referral_code = jsonObject1.getString("referral_code");
                            String amount = jsonObject1.getString("referral_amount");
                            String created_date = jsonObject1.getString("created_date");

                            referralModels.add(new ReferralModel(id,user_name,referral_code,created_date,amount));

                        }
                        Log.e("sgdhagdahs", String.valueOf(referralModels.size()));

                        if (referralModels.size() == 0)
                        {
                            binding.progressBar.setVisibility(View.GONE);
                            binding.textViewCategory.setVisibility(View.VISIBLE);
                        }
                        else
                        {

                            String refer_amount=jsonObject.getString("total_referral_balance");
                            binding.toalreferalTxt.setText("$"+refer_amount.toString());
                            binding.progressBar.setVisibility(View.GONE);
                            referralHistoryAdapter = new ReferralHistoryAdapter(ReferralActivity.this, referralModels);
                            binding.referallRecyclerview.setAdapter(referralHistoryAdapter);
                            binding.referallRecyclerview.setNestedScrollingEnabled(false);
                            binding.referallRecyclerview.setHasFixedSize(true);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ReferralActivity.this, LinearLayoutManager.VERTICAL, false);
                            binding.referallRecyclerview.setLayoutManager(layoutManager);
                            binding.referallRecyclerview.setFocusable(false);
                            binding.textViewCategory.setVisibility(View.GONE);

                        }




                    }

                    else {
                        binding.progressBar.setVisibility(View.GONE);

                        String message = jsonObject.getString("message");
                        binding.textViewCategory.setVisibility(View.VISIBLE);
                        binding.textViewCategory.setText(message.toString());
                        Toast.makeText(ReferralActivity.this, message.toString(), Toast.LENGTH_SHORT).show();


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

    public void WalletHistory(String UserId) {


        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("user_id", UserId);
        String url = Apis.ROOT_URL + getString(R.string.dash_board);
        Log.e("Url------->",url);

        client.post(url,params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);
                Log.e("Url------->",res.toString());
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("true")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        String account_balance = jsonObject1.getString("account_balance");
                        String total_interest_paid = jsonObject1.getString("total_interest_paid");
                        String accrued_interest = jsonObject1.getString("accrued_interest");
                        binding.accountBalanceTxt.setText("$"+account_balance.toString());

                        method.editor.putString(method.account_balance, account_balance);
                        method.editor.putString(method.total_interest_paid, total_interest_paid);
                        method.editor.putString(method.accrued_interest, accrued_interest);
                        method.editor.commit();


                    }

                    else {
                        binding.progressBar.setVisibility(View.GONE);
                        String message = jsonObject.getString("message");
                        Toast.makeText(ReferralActivity.this, message.toString(), Toast.LENGTH_SHORT).show();
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