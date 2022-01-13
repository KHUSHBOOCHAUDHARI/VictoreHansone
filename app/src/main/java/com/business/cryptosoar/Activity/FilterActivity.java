package com.business.cryptosoar.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.business.cryptosoar.Adapter.FilterListAdapter;
import com.business.cryptosoar.Adapter.FilterMainListAdapter;
import com.business.cryptosoar.Adapter.PaymentSettingListAdapter;
import com.business.cryptosoar.Adapter.TransactionHistoryAdapter;
import com.business.cryptosoar.Model.FilterListModel;

import com.business.cryptosoar.R;
import com.business.cryptosoar.Util.Apis;
import com.business.cryptosoar.Util.Method;
import com.business.cryptosoar.databinding.ActivityFilterBinding;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class FilterActivity extends AppCompatActivity {
    private ActivityFilterBinding binding;
    private FilterMainListAdapter paymentSettingListAdapter;
    private List<FilterListModel> paymentSetingListModels;
    public static String statuss;
    private Boolean isOver = false;
    Method method;
    private int oldPosition = 0;
    private int pagination_index = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(FilterActivity.this,R.layout.activity_filter);
        paymentSetingListModels=new ArrayList<>();
        method=new Method(FilterActivity.this);
        binding.list.setNestedScrollingEnabled(false);
        binding.list.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        binding.list.setLayoutManager(layoutManager);
        binding.list.setFocusable(false);

        if (Method.isNetworkAvailable(FilterActivity.this)) {


            FilterList(method.pref.getString(method.Id,null), String.valueOf(pagination_index),"10");

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
                                oldPosition = paymentSetingListModels.size();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        pagination_index++;
                                        binding.progressbarLoadmore.setVisibility(View.VISIBLE);
                                        if (Method.isNetworkAvailable(FilterActivity.this)) {
                                          //  FilterListpage(method.pref.getString(method.Id,null), String.valueOf(pagination_index),"10");
                                        } else {
                                            method.alertBox(getResources().getString(R.string.internet_connection));
                                            binding.progressbarLoadmore.setVisibility(View.GONE);
                                        }                                    }
                                }, 1000);
                            } else {
                            }
                        }
                    }
                }
            }
        });

        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.right_enter,R.anim.left_out);
            }
        });



        fullscreen();
    }

    //NetworkList Api
    public void FilterList(String UserId,String pagination_index, String page_limit) {

        if (paymentSettingListAdapter == null) {
            paymentSetingListModels.clear();
            binding.progressBar.setVisibility(View.VISIBLE);
        }

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("user_id",UserId);
        params.add("page_index",pagination_index);
        params.add("page_limit",page_limit);
        String url = Apis.ROOT_URL + getString(R.string.add_fund);
        Log.e("Url------->",url);
        client.post(url,params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                if (getApplicationContext() != null) {
                    Log.d("Response", new String(responseBody));
                    String res = new String(responseBody);
                    Log.e("Url------->", res.toString());
                    Log.e("dasdsa","33333");
                                 try {
                        Log.e("dasdsa","11111");
                        JSONObject jsonObject = new JSONObject(res);
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("true")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String id = jsonObject1.getString("id");
                                String user_id = jsonObject1.getString("user_id");
                                String fund_plan_type = jsonObject1.getString("fund_plan_type");
                                String fund_amount = jsonObject1.getString("fund_amount");
                                String payment_method_id = jsonObject1.getString("payment_method_id");
                                String crypto_type_id = jsonObject1.getString("crypto_type_id");
                                String crypto_protocol_id = jsonObject1.getString("crypto_protocol_id");
                                String crypto_user_address_id = jsonObject1.getString("crypto_user_address_id");
                                String bank_account_number = jsonObject1.getString("bank_account_number");
                                String bank_ifsc_code = jsonObject1.getString("bank_ifsc_code");
                                String bank_branch_name = jsonObject1.getString("bank_branch_name");
                                statuss = jsonObject1.getString("status");
                                String created_date = jsonObject1.getString("created_date");
                                //String is_withdrawal_request_accept = jsonObject1.getString("is_withdrawal_request_accept");


                                paymentSetingListModels.add(new FilterListModel(id, fund_plan_type, fund_amount, created_date, statuss,""));

                            }

                            if (jsonArray.length() == 0) {
                                if (paymentSettingListAdapter != null) {
                                    isOver = true;
                                    binding.progressBar.setVisibility(View.GONE);

                                }
                            }
                            if (paymentSettingListAdapter == null) {

                                if (paymentSetingListModels.size() != 0) {
                                    binding.progressBar.setVisibility(View.GONE);
                                    paymentSettingListAdapter = new FilterMainListAdapter(FilterActivity.this, paymentSetingListModels);
                                    binding.list.setAdapter(paymentSettingListAdapter);
                                    binding.textViewCategory.setVisibility(View.GONE);


                                } else {
                                    binding.textViewCategory.setVisibility(View.VISIBLE);
                                }
                            } else {
                                paymentSettingListAdapter.notifyItemMoved(oldPosition, paymentSetingListModels.size());
                                paymentSettingListAdapter.notifyDataSetChanged();
                                binding.progressbarLoadmore.setVisibility(View.VISIBLE);
                            }
                            binding.progressBar.setVisibility(View.GONE);
                            binding.progressbarLoadmore.setVisibility(View.GONE);
                            binding.linearLayoutHomeAdapter.setVisibility(View.VISIBLE);

                        }
                        else {
                            binding.progressBar.setVisibility(View.GONE);
                            binding.progressbarLoadmore.setVisibility(View.GONE);
                            String message = jsonObject.getString("message");
                            binding.textViewCategory.setVisibility(View.VISIBLE);
                         }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("dasdsa","00000");
                        isOver = true;
                        binding.progressBar.setVisibility(View.GONE);
                        binding.progressbarLoadmore.setVisibility(View.GONE);
                        binding.textViewCategory.setVisibility(View.VISIBLE);
                        if (paymentSettingListAdapter != null) {
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

                method.alertBox(getResources().getString(R.string.something_went_wrong));
            }
        });
    }

    public void FilterListpage(String UserId,String pagination_index, String page_limit) {

        if (paymentSettingListAdapter == null) {
            paymentSetingListModels.clear();
            binding.progressBar.setVisibility(View.VISIBLE);
        }
        ;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("user_id",UserId);
        params.add("page_index",pagination_index);
        params.add("page_limit",page_limit);
        String url = Apis.ROOT_URL + getString(R.string.add_fund);
        Log.e("Url------->",url);
        client.post(url,params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                if (getApplicationContext() != null) {
                    Log.d("Response", new String(responseBody));
                    String res = new String(responseBody);
                    Log.e("Url------->", res.toString());
                                     try {
                        JSONObject jsonObject = new JSONObject(res);
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("true")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String id = jsonObject1.getString("id");
                                String user_id = jsonObject1.getString("user_id");
                                String fund_plan_type = jsonObject1.getString("fund_plan_type");
                                String fund_amount = jsonObject1.getString("fund_amount");
                                String payment_method_id = jsonObject1.getString("payment_method_id");
                                String crypto_type_id = jsonObject1.getString("crypto_type_id");
                                String crypto_protocol_id = jsonObject1.getString("crypto_protocol_id");
                                String crypto_user_address_id = jsonObject1.getString("crypto_user_address_id");
                                String bank_account_number = jsonObject1.getString("bank_account_number");
                                String bank_ifsc_code = jsonObject1.getString("bank_ifsc_code");
                                String bank_branch_name = jsonObject1.getString("bank_branch_name");
                                statuss = jsonObject1.getString("status");
                                String created_date = jsonObject1.getString("created_date");
                                String is_withdrawal_request_accept = jsonObject1.getString("is_withdrawal_request_accept");


                                paymentSetingListModels.add(new FilterListModel(id, fund_plan_type, fund_amount, created_date, statuss,is_withdrawal_request_accept));

                            }

                            if (jsonArray.length() == 0) {
                                if (paymentSettingListAdapter != null) {
                                    isOver = true;
                                    binding.progressBar.setVisibility(View.GONE);

                                }
                            }
                            if (paymentSettingListAdapter == null) {

                                if (paymentSetingListModels.size() != 0) {
                                    binding.progressBar.setVisibility(View.GONE);
                                    paymentSettingListAdapter = new FilterMainListAdapter(FilterActivity.this, paymentSetingListModels);
                                    binding.list.setAdapter(paymentSettingListAdapter);
                                    binding.textViewCategory.setVisibility(View.GONE);


                                } else {
                                    binding.textViewCategory.setVisibility(View.VISIBLE);
                                }
                            } else {
                                paymentSettingListAdapter.notifyItemMoved(oldPosition, paymentSetingListModels.size());
                                paymentSettingListAdapter.notifyDataSetChanged();
                                binding.progressbarLoadmore.setVisibility(View.VISIBLE);
                            }
                            binding.progressBar.setVisibility(View.GONE);
                            binding.progressbarLoadmore.setVisibility(View.GONE);
                            binding.linearLayoutHomeAdapter.setVisibility(View.VISIBLE);

                        }
                        else {
                            binding.progressBar.setVisibility(View.GONE);
                            binding.progressbarLoadmore.setVisibility(View.GONE);
                            String message = jsonObject.getString("message");

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        isOver = true;
                        binding.progressBar.setVisibility(View.GONE);
                        binding.progressbarLoadmore.setVisibility(View.GONE);
                        binding.textViewCategory.setVisibility(View.VISIBLE);
                        if (paymentSettingListAdapter != null) {
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

                method.alertBox(getResources().getString(R.string.something_went_wrong));
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