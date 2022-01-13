package com.business.cryptosoar.Fragment;

import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.business.cryptosoar.Activity.AddressListActivity;
import com.business.cryptosoar.Activity.FilterActivity;
import com.business.cryptosoar.Activity.MainActivity;
import com.business.cryptosoar.Adapter.PaymentSettingListAdapter;
import com.business.cryptosoar.Adapter.TransactionHistoryAdapter;
import com.business.cryptosoar.Model.PaymentSetingListModel;
import com.business.cryptosoar.Model.TransactionModel;
import com.business.cryptosoar.R;
import com.business.cryptosoar.Util.Apis;
import com.business.cryptosoar.Util.Method;
import com.business.cryptosoar.databinding.HistoryFragmentBinding;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class HistoryFragment extends Fragment {
    HistoryFragmentBinding binding;
    private List<TransactionModel> transactionModels;
    TransactionHistoryAdapter TransactionHistoryAdapter;
    Method method;
    private Boolean isOver = false;
    public static String catid, keyword,discount_by, user_id;
    private int oldPosition = 0;
    private int pagination_index = 1;
    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.history_fragment, container, false);
        MainActivity.txt_toolbartitle.setText(getResources().getString(R.string.transaction_history_txt));
        MainActivity.RelativeLayout.setVisibility(View.VISIBLE);
        MainActivity.intLayout.setVisibility(View.GONE);

        method=new Method(getActivity());

        transactionModels=new ArrayList<>();
        MainActivity.filter.setVisibility(View.GONE);
        binding.tansectionRecyclerview.setNestedScrollingEnabled(false);
        binding.tansectionRecyclerview.setHasFixedSize(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        binding.tansectionRecyclerview.setLayoutManager(layoutManager);
        binding.tansectionRecyclerview.setFocusable(false);
        WalletHistory(method.pref.getString(method.Id,null));
        if (Method.isNetworkAvailable(getActivity())) {
            TrasectionHistory(method.pref.getString(method.Id,null), String.valueOf(pagination_index),"10");
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

                                        if (Method.isNetworkAvailable(getActivity())) {
                                            TrasectionHistoryPage(method.pref.getString(method.Id,null), String.valueOf(pagination_index),"10");
                                        } else {
                                            method.alertBox(getResources().getString(R.string.internet_connection));
                                            binding.progressBar.setVisibility(View.GONE);
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

        return binding.getRoot();
    }
    private void TrasectionHistory(String UserId,String pagination_index, String page_limit) {
        if (TransactionHistoryAdapter == null) {
            transactionModels.clear();
            binding.progressBar.setVisibility(View.VISIBLE);
        }

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("user_id",UserId);
        params.add("page_index",pagination_index);
        params.add("page_limit",page_limit);
        String url = Apis.ROOT_URL + getString(R.string.transection_history);
        client.cancelAllRequests(true);

        client.post(url,params,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                if (getActivity() != null) {
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
                                    TransactionHistoryAdapter = new TransactionHistoryAdapter(getActivity(), transactionModels);
                                    binding.tansectionRecyclerview.setAdapter(TransactionHistoryAdapter);
                                    binding.textViewCategory.setVisibility(View.GONE);

                                } else {
                                    binding.textViewCategory.setVisibility(View.VISIBLE);

                                }
                            }
                            else {

                                TransactionHistoryAdapter.notifyItemMoved(oldPosition, transactionModels.size());
                                TransactionHistoryAdapter.notifyDataSetChanged();
                                binding.progressbarLoadmore.setVisibility(View.VISIBLE);
                            }
                        }
                        else
                        {
                            binding.progressBar.setVisibility(View.GONE);
                            binding.progressbarLoadmore.setVisibility(View.GONE);
                            String message = jsonObject.getString("message");
                            binding.textViewCategory.setVisibility(View.VISIBLE);

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

    private void TrasectionHistoryPage(String UserId,String pagination_index, String page_limit) {
        if (TransactionHistoryAdapter == null) {
            transactionModels.clear();
            binding.progressBar.setVisibility(View.VISIBLE);
        }

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("user_id",UserId);
        params.add("page_index",pagination_index);
        params.add("page_limit",page_limit);
        String url = Apis.ROOT_URL + getString(R.string.transection_history);
        client.cancelAllRequests(true);

        client.post(url,params,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                if (getActivity() != null) {
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
                                transactionModels.add(new TransactionModel(id, text, amount,transaction_type,date, transaction_status,type));
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
                                    TransactionHistoryAdapter = new TransactionHistoryAdapter(getActivity(), transactionModels);
                                    binding.tansectionRecyclerview.setAdapter(TransactionHistoryAdapter);
                                    binding.textViewCategory.setVisibility(View.GONE);

                                } else {
                                    binding.textViewCategory.setVisibility(View.VISIBLE);

                                }
                            }
                            else {

                                TransactionHistoryAdapter.notifyItemMoved(oldPosition, transactionModels.size());
                                TransactionHistoryAdapter.notifyDataSetChanged();
                                binding.progressbarLoadmore.setVisibility(View.VISIBLE);
                            }
                        }
                        else
                        {
                            binding.progressBar.setVisibility(View.GONE);
                            binding.progressbarLoadmore.setVisibility(View.GONE);
                            String message = jsonObject.getString("message");
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
                        MainActivity.account_balance_txt.setText("$"+account_balance.toString());
                        MainActivity.toalreferal_txt.setText("$"+total_interest_paid.toString());
                        MainActivity.balance.setText("Account Balance:" + " $" + account_balance, null);
                        method.editor.putString(method.account_balance, account_balance);
                        method.editor.putString(method.total_interest_paid, total_interest_paid);
                        method.editor.putString(method.accrued_interest, accrued_interest);
                        method.editor.putBoolean(method.pref_login, true);
                        method.editor.commit();


                    }

                    else {
                        binding.progressBar.setVisibility(View.GONE);
                        String message = jsonObject.getString("message");
                        Toast.makeText(getActivity(), message.toString(), Toast.LENGTH_SHORT).show();
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
