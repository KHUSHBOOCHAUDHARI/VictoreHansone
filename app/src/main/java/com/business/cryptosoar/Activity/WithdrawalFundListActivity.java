package com.business.cryptosoar.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.business.cryptosoar.Adapter.ContactListAdapter;
import com.business.cryptosoar.Adapter.FilterListAdapter;
import com.business.cryptosoar.Adapter.FilterMainListAdapter;
import com.business.cryptosoar.Adapter.ReferralHistoryAdapter;
import com.business.cryptosoar.Model.ContractListModel;
import com.business.cryptosoar.Model.FilterListModel;
import com.business.cryptosoar.Model.ReferralModel;
import com.business.cryptosoar.R;
import com.business.cryptosoar.Util.Apis;
import com.business.cryptosoar.Util.Method;
import com.business.cryptosoar.databinding.ActivityWithdrawalFundListBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class WithdrawalFundListActivity extends AppCompatActivity {
    private ActivityWithdrawalFundListBinding binding;
    private FilterListAdapter paymentSettingListAdapter;
    private List<FilterListModel> paymentSetingListModels;
    private ContactListAdapter contactListAdapter;
    private List<ContractListModel> contractListModels;
    public static String statuss;
    public static Dialog bottomSheetDialogg;
    private Boolean isOver = false;
    Method method;
    private int oldPosition = 0;
    private int pagination_index = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(WithdrawalFundListActivity.this, R.layout.activity_withdrawal_fund_list);
        paymentSetingListModels = new ArrayList<>();
        contractListModels = new ArrayList<>();
        method = new Method(WithdrawalFundListActivity.this);
        binding.list.setNestedScrollingEnabled(false);
        WalletHistory(method.pref.getString(method.Id, null));
        binding.list.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        binding.list.setLayoutManager(layoutManager);
        binding.list.setFocusable(false);
        binding.toalreferalTxt.setText("$" + method.pref.getString(method.total_interest_paid, null));
        binding.viewTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialogg = new Dialog(WithdrawalFundListActivity.this) {
                    @Override
                    public boolean onTouchEvent(MotionEvent event) {
                        // Tap anywhere to close dialog.
                        bottomSheetDialogg.dismiss();
                        return true;
                    }
                };
                bottomSheetDialogg.requestWindowFeature(Window.FEATURE_NO_TITLE);
                bottomSheetDialogg.setContentView(R.layout.fragment_withdrawallist_bottomsheet);
                RecyclerView recyclerView = bottomSheetDialogg.findViewById(R.id.recyclerView);
                contractListModels.clear();
                binding.progressBar.setVisibility(View.VISIBLE);
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.add("user_id", method.pref.getString(method.Id, null));
                String url = Apis.ROOT_URL + getString(R.string.contact_list);
                Log.e("Url------->", url);
                client.post(url, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
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
                                    String contract_name = jsonObject1.getString("contract_name");
                                    String contract_description = jsonObject1.getString("contract_description");
                                    String contract_name_key = jsonObject1.getString("contract_name_key");
                                    String contract_persentage = jsonObject1.getString("contract_persentage");
                                    String created_date = jsonObject1.getString("created_date");
                                    contractListModels.add(new ContractListModel(contract_name, contract_description, contract_persentage + "%"));

                                }

                                binding.progressBar.setVisibility(View.GONE);
                                contactListAdapter = new ContactListAdapter(WithdrawalFundListActivity.this, contractListModels);
                                recyclerView.setAdapter(contactListAdapter);

                                recyclerView.setNestedScrollingEnabled(false);
                                recyclerView.setHasFixedSize(true);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(WithdrawalFundListActivity.this, LinearLayoutManager.VERTICAL, false);
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setFocusable(false);


                            } else {
                                binding.progressBar.setVisibility(View.GONE);
                                String message = jsonObject.getString("message");
                                Toast.makeText(WithdrawalFundListActivity.this, message.toString(), Toast.LENGTH_SHORT).show();
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

                bottomSheetDialogg.show();
                bottomSheetDialogg.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                bottomSheetDialogg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                bottomSheetDialogg.getWindow().getAttributes().windowAnimations = R.style.Dialoganimation;
                bottomSheetDialogg.getWindow().setGravity(Gravity.BOTTOM);

            }
        });
        binding.accountBalanceTxt.setText("$" + method.pref.getString(method.account_balance, null));

        if (Method.isNetworkAvailable(WithdrawalFundListActivity.this)) {


            UserFuldList(method.pref.getString(method.Id, null), String.valueOf(pagination_index), "100");

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
                                        if (Method.isNetworkAvailable(WithdrawalFundListActivity.this)) {
                                            UserFuldListPage(method.pref.getString(method.Id, null), String.valueOf(pagination_index), "10");
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

        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                finish();
            }
        });


        // NetworkList();
        fullscreen();
    }

    //NetworkList Api
    public void UserFuldList(String UserId, String pagination_index, String page_limit) {

        if (paymentSettingListAdapter == null) {
            paymentSetingListModels.clear();
            binding.progressBar.setVisibility(View.VISIBLE);
        }
        ;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("user_id", UserId);
        params.add("page_index", pagination_index);
        params.add("page_limit", page_limit);
        String url = Apis.ROOT_URL + getString(R.string.user_cancel_fund_list);
        Log.e("Url------->", url);
        client.post(url, params, new AsyncHttpResponseHandler() {
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
                                //  String is_withdrawal_request_accept = jsonObject1.getString("is_withdrawal_request_accept");


                                paymentSetingListModels.add(new FilterListModel(id, fund_plan_type, fund_amount, fund_plan_type, created_date, statuss));

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
                                    paymentSettingListAdapter = new FilterListAdapter(WithdrawalFundListActivity.this, paymentSetingListModels);
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

                        } else {
                            binding.progressBar.setVisibility(View.GONE);
                            binding.progressbarLoadmore.setVisibility(View.GONE);
                            binding.textViewCategory.setVisibility(View.VISIBLE);
                        }


                    } catch (JSONException e) {
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

    public void UserFuldListPage(String UserId, String pagination_index, String page_limit) {

        if (paymentSettingListAdapter == null) {
            paymentSetingListModels.clear();
            binding.progressBar.setVisibility(View.VISIBLE);
        }
        ;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("user_id", UserId);
        params.add("page_index", pagination_index);
        params.add("page_limit", page_limit);
        String url = Apis.ROOT_URL + getString(R.string.user_cancel_fund_list);
        Log.e("Url------->", url);
        client.post(url, params, new AsyncHttpResponseHandler() {
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
                                //  String is_withdrawal_request_accept = jsonObject1.getString("is_withdrawal_request_accept");


                                paymentSetingListModels.add(new FilterListModel(id, fund_plan_type, fund_amount, created_date, statuss, ""));

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
                                    paymentSettingListAdapter = new FilterListAdapter(WithdrawalFundListActivity.this, paymentSetingListModels);
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

                        } else {
                            binding.progressBar.setVisibility(View.GONE);
                            binding.progressbarLoadmore.setVisibility(View.GONE);

                        }


                    } catch (JSONException e) {
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

    //Login Api
    public void WalletHistory(String UserId) {

        binding.progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("user_id", UserId);
        String url = Apis.ROOT_URL + getString(R.string.dash_board);
        Log.e("Url------->", url);

        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);
                Log.e("Url------->", res.toString());
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        String account_balance = jsonObject1.getString("account_balance");
                        String total_interest_paid = jsonObject1.getString("total_interest_paid");
                        String accrued_interest = jsonObject1.getString("accrued_interest");
                        binding.accountBalanceTxt.setText("$" + account_balance.toString());
                        binding.toalreferalTxt.setText("$" + total_interest_paid.toString());

                        method.editor.putString(method.account_balance, account_balance);
                        method.editor.putString(method.total_interest_paid, total_interest_paid);
                        method.editor.putString(method.accrued_interest, accrued_interest);
                        method.editor.commit();


                    } else {
                        binding.progressBar.setVisibility(View.GONE);
                        String message = jsonObject.getString("message");
                        Toast.makeText(WithdrawalFundListActivity.this, message.toString(), Toast.LENGTH_SHORT).show();
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