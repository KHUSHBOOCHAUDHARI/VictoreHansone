package com.business.cryptosoar.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.business.cryptosoar.Adapter.TransactionHistoryAdapter;
import com.business.cryptosoar.Adapter.TransactionHistoryByFundAdapter;
import com.business.cryptosoar.Model.TransactionModel;
import com.business.cryptosoar.R;
import com.business.cryptosoar.Util.Apis;
import com.business.cryptosoar.Util.Method;
import com.business.cryptosoar.databinding.ActivityFilterBinding;
import com.business.cryptosoar.databinding.ActivityFilterDetailBinding;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class FilterDetailActivity extends AppCompatActivity {
    private ActivityFilterDetailBinding binding;
    private List<TransactionModel> transactionModels;
    TransactionHistoryByFundAdapter TransactionHistoryAdapter;
    Method method;
    private Boolean isOver = false;
    public static String catid, keyword,discount_by, user_id;
    private int oldPosition = 0;
    private int pagination_index = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(FilterDetailActivity.this,R.layout.activity_filter_detail);
        binding.titleTxt.setText(getIntent().getStringExtra("fundname")+" Investment Details");
        transactionModels=new ArrayList<>();
        method=new Method(FilterDetailActivity.this);
        binding.list.setNestedScrollingEnabled(false);
        binding.list.setHasFixedSize(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(FilterDetailActivity.this, LinearLayoutManager.VERTICAL, false);
        binding.list.setLayoutManager(layoutManager);
        binding.list.setFocusable(false);
        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.right_enter,R.anim.left_out);
            }
        });






        if (Method.isNetworkAvailable(FilterDetailActivity.this)) {


            ViewAllProducts(method.pref.getString(method.Id,null),getIntent().getStringExtra("fundid"),String.valueOf(pagination_index),"100");

        } else {
            method.alertBox(getResources().getString(R.string.internet_connection));
            binding.progressBar.setVisibility(View.GONE);
        }

        binding.nestedScrollViewHomeFragment.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (v.getChildAt(v.getChildCount() - 1) != null) {
                    if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                            scrollY > oldScrollY) {

                        int visibleItemCount = layoutManager.getChildCount();
                        int totalItemCount = layoutManager.getItemCount();
                        int pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            if (!isOver) {
                                oldPosition = transactionModels.size();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        pagination_index++;
                                        binding.progressbarLoadmore.setVisibility(View.VISIBLE);
                                        if (Method.isNetworkAvailable(FilterDetailActivity.this)) {
                                            ViewAllProducts(method.pref.getString(method.Id,null),getIntent().getStringExtra("fundid"),String.valueOf(pagination_index),"100");
                                        } else {
                                            method.alertBox(getResources().getString(R.string.internet_connection));
                                            binding.progressbarLoadmore.setVisibility(View.GONE);
                                        }
                                    }
                                    }, 1000);
                            } else {
                            }
                        }
                    }
                }
            }
        });
        fullscreen();

    }



    private void ViewAllProducts(String UserId,String FundId,String pagination_index, String page_limit) {
        if (TransactionHistoryAdapter == null) {
            transactionModels.clear();
            binding.progressBar.setVisibility(View.VISIBLE);

        }
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("user_id",UserId);
        params.add("fund_id",FundId);

        params.add("page_index",pagination_index);
        params.add("page_limit",page_limit);
        String url = Apis.ROOT_URL + getString(R.string.transection_history_byfund);
        client.cancelAllRequests(true);

        client.post(url,params,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                if (getApplicationContext() != null) {
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
                                transactionModels.add(new TransactionModel(id, text, amount, transaction_type,date, transaction_status,type));

                            }
                            if (jsonArray.length() == 0) {
                                if (TransactionHistoryAdapter != null) {
                                    isOver = true;
                                    binding.progressBar.setVisibility(View.GONE);

                                }
                            }
                            if (TransactionHistoryAdapter == null) {
                                binding.textViewCategory.setVisibility(View.VISIBLE);
                                if (transactionModels.size() != 0) {
                                    binding.progressBar.setVisibility(View.GONE);
                                    TransactionHistoryAdapter = new TransactionHistoryByFundAdapter(FilterDetailActivity.this, transactionModels);
                                    binding.list.setAdapter(TransactionHistoryAdapter);
                                    binding.textViewCategory.setVisibility(View.GONE);

                                } else {
                                    binding.textViewCategory.setVisibility(View.VISIBLE);
                                }
                            } else {
                                TransactionHistoryAdapter.notifyItemMoved(oldPosition, transactionModels.size());
                                TransactionHistoryAdapter.notifyDataSetChanged();
                                binding.progressbarLoadmore.setVisibility(View.VISIBLE);
                            }

                        }




                        binding.progressBar.setVisibility(View.GONE);
                        binding.progressbarLoadmore.setVisibility(View.GONE);
                        binding.linearLayoutHomeAdapter.setVisibility(View.VISIBLE);

                    } catch(JSONException e){
                        e.printStackTrace();
                        isOver = true;
                        binding.progressBar.setVisibility(View.GONE);
                        binding.progressbarLoadmore.setVisibility(View.GONE);
                        binding.textViewCategory.setVisibility(View.VISIBLE);
                        if (TransactionHistoryAdapter != null) {
                            isOver = true;
                            binding.progressbarLoadmore.setVisibility(View.GONE);
                            binding.textViewCategory.setVisibility(View.GONE);
                        }
                    }
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                binding.progressbarLoadmore.setVisibility(View.GONE);
            }
        });
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

}