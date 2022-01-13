package com.business.cryptosoar.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Point;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.business.cryptosoar.Activity.EmailVerificationActivity;
import com.business.cryptosoar.Activity.FilterActivity;
import com.business.cryptosoar.Activity.FilterDetailActivity;
import com.business.cryptosoar.Activity.LoginActivity;
import com.business.cryptosoar.Activity.MainActivity;
import com.business.cryptosoar.Activity.WithdrawalFundListActivity;
import com.business.cryptosoar.Adapter.ContactListAdapter;
import com.business.cryptosoar.Adapter.FilterMainListAdapter;
import com.business.cryptosoar.Adapter.TransactionHistoryAdapter;
import com.business.cryptosoar.Adapter.TransactionHistoryByFundAdapter;
import com.business.cryptosoar.Model.ContractListModel;
import com.business.cryptosoar.Model.FilterListModel;
import com.business.cryptosoar.Model.TransactionModel;
import com.business.cryptosoar.R;
import com.business.cryptosoar.Activity.ReferralActivity;
import com.business.cryptosoar.Util.Apis;
import com.business.cryptosoar.Util.Method;
import com.business.cryptosoar.databinding.WalletFragmentBinding;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class WalletFragment extends Fragment {
    WalletFragmentBinding binding;
    public static String fund_amount="";
    public static  Dialog bottomSheetDialogg;
    LinearLayoutManager layoutManager;
    private int mWidth;
    private int mHeight;
    private GradientManager mGradientManager;
    Method method;
    private List<TransactionModel> transactionModels;
    TransactionHistoryAdapter TransactionHistoryAdapter;
    TransactionHistoryByFundAdapter transactionHistoryByFundAdapter;
    private Boolean isOver = false;
    public static String catid, keyword,discount_by, user_id;
    private int oldPosition = 0;

    private int pagination_index = 1;
    public static String transaction_status="";
    RecyclerView filterRecy;
    TextView nodat;
    TextView send_btn;
    NestedScrollView nestedScrollView_home_fragment;
    public static RecyclerView filterRecyclerview,tansectionRecyclerview;
    ProgressBar progressBardialog;
    private FilterMainListAdapter paymentSettingListAdapter;
    private List<FilterListModel> paymentSetingListModels;
    public static  ProgressBar progressBar;
    public static TextView nodata,accrued_txt,toalinterest_txt,account_balance_txt;
    public static String Fund="";
    public static String TYTPE="";
    public WalletFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.wallet_fragment, container, false);
        method=new Method(getActivity());

        transactionModels=new ArrayList<>();
              String Id=method.pref.getString(method.Id,null);
        MainActivity.filter.setVisibility(View.VISIBLE);
        transactionModels=new ArrayList<>();
        filterRecyclerview=binding.filterRecyclerview;
        tansectionRecyclerview=binding.tansectionRecyclerview;
        progressBar=binding.progressBar;
        accrued_txt=binding.accruedTxt;
        toalinterest_txt=binding.toalinterestTxt;
        account_balance_txt=binding.accountBalanceTxt;
        nodata=binding.textViewCategory;

        Log.e("iddddd",method.pref.getString(method.Id,null));

            if (Method.isNetworkAvailable(getActivity())) {
                TrasectionHistory(method.pref.getString(method.Id,null), String.valueOf(pagination_index),"5");
            } else {
                method.alertBox(getResources().getString(R.string.internet_connection));
                binding.progressBar.setVisibility(View.GONE);
            }



            binding.tansectionRecyclerview.setNestedScrollingEnabled(false);
            binding.tansectionRecyclerview.setHasFixedSize(false);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            binding.tansectionRecyclerview.setLayoutManager(layoutManager);
            binding.tansectionRecyclerview.setFocusable(false);




        MainActivity.filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               AlertDialogFilter();
            }
        });

        WalletHistory(Id);
        SettingApi(Id);
        MainActivity.txt_toolbartitle.setText(getResources().getString(R.string.welcome_dashboard_txt));
        MainActivity.RelativeLayout.setVisibility(View.GONE);
              binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MainActivity.navigationView.setVisibility(View.VISIBLE);

            }
        });


        binding.tranHistorylisTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.addFubdLogo.setImageTintList(ContextCompat.getColorStateList(getActivity(), R.color.lightwhite));
                MainActivity.addFundTxt.setTextColor(ContextCompat.getColor(getActivity(), R.color.lightwhite));
                MainActivity.walletlogo.setImageTintList(ContextCompat.getColorStateList(getActivity(), R.color.lightwhite));
                MainActivity.historylogo.setImageTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorPrimaryDark));
                MainActivity.withdrologo.setImageTintList(ContextCompat.getColorStateList(getActivity(), R.color.lightwhite));

                MainActivity.wallettxt.setTextColor(ContextCompat.getColor(getActivity(), R.color.lightwhite));
                MainActivity.historytxt.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
                MainActivity.withdrotxt.setTextColor(ContextCompat.getColor(getActivity(), R.color.lightwhite));
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new HistoryFragment()).commit();
                getActivity().overridePendingTransition(R.anim.right_enter,R.anim.left_out);
            }
        });

        binding.addfundLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.addFubdLogo.setImageTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorPrimaryDark));
                MainActivity.addFundTxt.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
                MainActivity.walletlogo.setImageTintList(ContextCompat.getColorStateList(getActivity(), R.color.lightwhite));
                MainActivity.historylogo.setImageTintList(ContextCompat.getColorStateList(getActivity(), R.color.lightwhite));
                MainActivity.withdrologo.setImageTintList(ContextCompat.getColorStateList(getActivity(), R.color.lightwhite));

                MainActivity.wallettxt.setTextColor(ContextCompat.getColor(getActivity(), R.color.lightwhite));
                MainActivity.historytxt.setTextColor(ContextCompat.getColor(getActivity(), R.color.lightwhite));
                 MainActivity.withdrotxt.setTextColor(ContextCompat.getColor(getActivity(), R.color.lightwhite));
               getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new AddFundFragment()).commit();
                getActivity().overridePendingTransition(R.anim.right_enter,R.anim.left_out);

            }
        });
        binding.withdrowLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.addFubdLogo.setImageTintList(ContextCompat.getColorStateList(getActivity(), R.color.lightwhite));
                MainActivity.addFundTxt.setTextColor(ContextCompat.getColor(getActivity(), R.color.lightwhite));
                MainActivity.walletlogo.setImageTintList(ContextCompat.getColorStateList(getActivity(), R.color.lightwhite));
                MainActivity.historylogo.setImageTintList(ContextCompat.getColorStateList(getActivity(), R.color.lightwhite));
                MainActivity.withdrologo.setImageTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorPrimaryDark));

                MainActivity.wallettxt.setTextColor(ContextCompat.getColor(getActivity(), R.color.lightwhite));
                MainActivity.historytxt.setTextColor(ContextCompat.getColor(getActivity(), R.color.lightwhite));
                MainActivity.withdrotxt.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new WithdrowFragment()).commit();
                getActivity().overridePendingTransition(R.anim.right_enter,R.anim.left_out);

            }
        });



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
//                                        pagination_index++;
//                                        binding.progressbarLoadmore.setVisibility(View.VISIBLE);
//                                        if (Method.isNetworkAvailable(getActivity())) {
//                                            TrasectionHistorypage(method.pref.getString(method.Id, null), String.valueOf(pagination_index), "10");
//                                        } else {
//                                            method.alertBox(getResources().getString(R.string.internet_connection));
//                                            binding.progressbarLoadmore.setVisibility(View.GONE);
//                                        }
                                    }
                                }, 1000);
                            } else {
                            }
                        }
                    }
                }
            }
        });

        int[] color = {getResources().getColor(R.color.colorPrimary),getResources().getColor(R.color.colorPrimaryDark)};
        float[] position = {0, 1};
        Shader.TileMode tile_mode = Shader.TileMode.MIRROR; // or TileMode.REPEAT;
        LinearGradient lin_grad = new LinearGradient(0, 0, 0, 50,color,position, tile_mode);
        Shader shader_gradient = lin_grad;
        binding.accountBalanceTxt.getPaint().setShader(shader_gradient);



    binding.thirdlayout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent=new Intent(getActivity(), ReferralActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.right_enter,R.anim.left_out);
        }
    });
     return binding.getRoot();








    }


    //Wallet Api
    public void WalletHistory(String UserId) {

        binding.progressBar.setVisibility(View.VISIBLE);
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
                        String register_bonus_percentage = jsonObject1.getString("register_bonus_percentage");
                        binding.accountBalanceTxt.setText("$"+account_balance.toString());
                        binding.toalinterestTxt.setText("$"+total_interest_paid.toString());
                        binding.accruedTxt.setText("$"+accrued_interest.toString());
                        binding.inverstTxt.setText("Invite your friends to join get up to "+register_bonus_percentage+"% of the total invest done by referral");

                        MainActivity.balance.setText("Account Balance:" + " $" + account_balance, null);
                        method.editor.putString(method.account_balance, account_balance);
                        method.editor.putString(method.total_interest_paid, total_interest_paid);
                        method.editor.putString(method.accrued_interest, accrued_interest);
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

                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                binding.progressBar.setVisibility(View.GONE);
                method.alertBox(getResources().getString(R.string.something_went_wrong));
            }
        });
    }

    private void SettingApi(String UserId) {
        binding.progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("user_id", UserId);

        String url = Apis.ROOT_URL+ getString(R.string.setting_api);
        Log.e("Parms",params.toString());
        Log.e("Url---->",url.toString());
        // client.addHeader("API-TOKEN", getString(R.string.API_TOKEN));
        client.post(url,params,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (getActivity()!= null) {
                    Log.d("Response", new String(responseBody));
                    String res = new String(responseBody);
                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("true")) {
                            JSONObject jsonObject1=jsonObject.getJSONObject("data");
                            String addfund_minimum_diposit=jsonObject1.getString("addfund_minimum_diposit");
                            String addfund_maximum_diposit=jsonObject1.getString("addfund_maximum_diposit");
                            String monthly_interest_percentage=jsonObject1.getString("monthly_interest_percentage");
                            String register_bonus_percentage=jsonObject1.getString("register_bonus_percentage");
                            String withdrawal_minimum_amount=jsonObject1.getString("withdrawal_minimum_amount");
                            String withdrawal_minimum_limit=jsonObject1.getString("withdrawal_minimum_limit");
                            method.editor.putString(method.addfund_minimum_diposit, addfund_minimum_diposit);
                            method.editor.putString(method.monthly_interest_percentage, monthly_interest_percentage);
                            method.editor.putString(method.addfund_maximum_diposit, addfund_maximum_diposit);
                            method.editor.putString(method.register_bonus_percentage, register_bonus_percentage);
                            method.editor.putString(method.withdrawal_minimum_amount, withdrawal_minimum_amount);
                            method.editor.putString(method.withdrawal_minimum_limit,withdrawal_minimum_limit);
                            method.editor.commit();

                        }

                        else
                        {
                            String Message = jsonObject.getString("message");
                            method.alertBox(Message);

                        }

                        binding.progressBar.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        binding.progressBar.setVisibility(View.GONE);
                    }
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                method.alertBox(getResources().getString(R.string.something_went_wrong));
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void TrasectionHistory(String UserId,String pagination_index, String page_limit) {

        if (TransactionHistoryAdapter == null) {
            transactionModels.clear();
            binding.progressBar.setVisibility(View.VISIBLE);
        }
        binding.tansectionRecyclerview.setVisibility(View.VISIBLE);
        binding.filterRecyclerview.setVisibility(View.GONE);

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
                                transaction_status = jsonObject1.getString("transaction_status");
                                String transaction_type = jsonObject1.getString("transaction_type");
                                String date_month = jsonObject1.getString("date_month");
                                String date = jsonObject1.getString("date");
                                String time = jsonObject1.getString("time");
                                transactionModels.add(new TransactionModel(id, text, amount,transaction_type ,date, transaction_status,type));

                            }
                            if (jsonArray.length() == 0) {
                                if (TransactionHistoryAdapter != null) {
                                    isOver = true;
                                    binding.progressBar.setVisibility(View.GONE);

                                }
                            }
                            if (TransactionHistoryAdapter == null) {

                                if (transactionModels.size() != 0) {
                                    binding.progressBar.setVisibility(View.GONE);
                                    binding.filterRecyclerview.setVisibility(View.GONE);
                                    binding.tansectionRecyclerview.setVisibility(View.VISIBLE);
                                    TransactionHistoryAdapter = new TransactionHistoryAdapter(getActivity(), transactionModels);
                                    binding.tansectionRecyclerview.setAdapter(TransactionHistoryAdapter);
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

    private void TrasectionHistorypage(String UserId,String pagination_index, String page_limit) {
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
                        Log.e("sdsd","dsdsds");
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
                                transaction_status = jsonObject1.getString("transaction_status");
                                String transaction_type = jsonObject1.getString("transaction_type");
                                String date_month = jsonObject1.getString("date_month");
                                String date = jsonObject1.getString("date");
                                String time = jsonObject1.getString("time");
                                transactionModels.add(new TransactionModel(id,text, amount, transaction_type,date, transaction_status,type));

                            }
                            if (jsonArray.length() == 0) {
                                if (TransactionHistoryAdapter != null) {
                                    isOver = true;
                                    binding.progressBar.setVisibility(View.GONE);

                                }
                            }
                            if (TransactionHistoryAdapter == null) {

                                if (transactionModels.size() != 0) {

                                    binding.progressBar.setVisibility(View.GONE);
                                    binding.filterRecyclerview.setVisibility(View.GONE);
                                    binding.tansectionRecyclerview.setVisibility(View.VISIBLE);
                                    TransactionHistoryAdapter = new TransactionHistoryAdapter(getActivity(), transactionModels);
                                    binding.tansectionRecyclerview.setAdapter(TransactionHistoryAdapter);
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
                        Log.e("sdsd","dsdsds");
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

    public void AlertDialogFilter() {
     bottomSheetDialogg = new Dialog(getActivity()) {
            @Override
            public boolean onTouchEvent(MotionEvent event) {
                // Tap anywhere to close dialog.
                bottomSheetDialogg.dismiss();
                return true;
            }
        };
        bottomSheetDialogg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomSheetDialogg.setContentView(R.layout.dialog_filter);
        filterRecy = bottomSheetDialogg.findViewById(R.id.listtt);
        nodat=bottomSheetDialogg.findViewById(R.id.nodata);
        progressBardialog=bottomSheetDialogg.findViewById(R.id.progressBardialog);
        send_btn = bottomSheetDialogg.findViewById(R.id.send_btn);
        nestedScrollView_home_fragment=bottomSheetDialogg.findViewById(R.id.nestedScrollView_home_fragment);
        Fund=FilterMainListAdapter.FUND_ID;

        if (Fund.equals(""))
        {
            send_btn.setVisibility(View.GONE);
        }

        paymentSetingListModels=new ArrayList<>();
        String UserId = "";
        String page_limit="";



        if (Method.isNetworkAvailable(getActivity())) {

            paymentSetingListModels.clear();
            FilterList(method.pref.getString(method.Id,null), String.valueOf(pagination_index),"1000");

        } else {
            method.alertBox(getResources().getString(R.string.internet_connection));
            binding.progressBar.setVisibility(View.GONE);
        }

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Method.isNetworkAvailable(getActivity())) {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    transactionModels.clear();
                    TrasectionHistory(method.pref.getString(method.Id,null), String.valueOf(pagination_index),"10");
//                    binding.tansectionRecyclerview.setVisibility(View.VISIBLE);
//                    binding.tansectionRecyclerview.setVisibility(View.GONE);
                    binding.textViewCategory.setVisibility(View.GONE);
                    WalletHistory(method.pref.getString(method.Id,null));
                    Fund="";
                   FilterMainListAdapter.FUND_ID="";

                } else {
                    method.alertBox(getResources().getString(R.string.internet_connection));
                    binding.progressBar.setVisibility(View.GONE);

                }

                bottomSheetDialogg.dismiss();
            }
        });

        filterRecy.setNestedScrollingEnabled(false);
        filterRecy.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        filterRecy.setLayoutManager(layoutManager);
        filterRecy.setFocusable(false);


        bottomSheetDialogg.show();
        bottomSheetDialogg.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        bottomSheetDialogg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bottomSheetDialogg.getWindow().getAttributes().windowAnimations=R.style.Dialoganimation;
        bottomSheetDialogg.getWindow().setGravity(Gravity.CENTER);




    }



 public void FilterList(String UserId,String pagination_index, String page_limit) {




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

                if (getActivity() != null) {
                    Log.d("Response", new String(responseBody));
                    String res = new String(responseBody);
                    Log.e("Url------->", res.toString());

                    try {
                        Log.e("dsdsdsa","000000000");
                        JSONObject jsonObject = new JSONObject(res);
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("true")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Log.e("dsdsdsa","000000000");
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String id = jsonObject1.getString("id");
                                String user_id = jsonObject1.getString("user_id");
                                String fund_plan_type = jsonObject1.getString("fund_plan_type");
                                fund_amount = jsonObject1.getString("fund_amount");
                                String payment_method_id = jsonObject1.getString("payment_method_id");
                                String crypto_type_id = jsonObject1.getString("crypto_type_id");
                                String crypto_protocol_id = jsonObject1.getString("crypto_protocol_id");
                                String crypto_user_address_id = jsonObject1.getString("crypto_user_address_id");
                                String bank_account_number = jsonObject1.getString("bank_account_number");
                                String bank_ifsc_code = jsonObject1.getString("bank_ifsc_code");
                                String bank_branch_name = jsonObject1.getString("bank_branch_name");
                                String statuss = jsonObject1.getString("status");
                                String created_date = jsonObject1.getString("created_date");


                                paymentSetingListModels.add(new FilterListModel(id, fund_plan_type, fund_amount, created_date, statuss, ""));

                            }






                                if (paymentSetingListModels.size() != 0) {
                                    progressBardialog.setVisibility(View.GONE);
                                    paymentSettingListAdapter = new FilterMainListAdapter(getActivity(), paymentSetingListModels);
                                    filterRecy.setAdapter(paymentSettingListAdapter);
                                    nodat.setVisibility(View.GONE);
                                } else {
                                    nodat.setVisibility(View.VISIBLE);
                                }





                            progressBardialog.setVisibility(View.GONE);
                            filterRecy.setVisibility(View.VISIBLE);

                    }
                        else {

                            progressBardialog.setVisibility(View.GONE);
                            String message = jsonObject.getString("message");
                            nodat.setVisibility(View.VISIBLE);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();



                            progressBardialog.setVisibility(View.GONE);
                            nodat.setVisibility(View.GONE);

                    }
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressBardialog.setVisibility(View.GONE);

                method.alertBox(getResources().getString(R.string.something_went_wrong));
            }
        });
    }

    public void FilterListPage(String UserId,String pagination_index, String page_limit) {


        if (paymentSettingListAdapter == null) {
            paymentSetingListModels.clear();
            progressBardialog.setVisibility(View.VISIBLE);
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

                if (getActivity() != null) {
                    Log.d("Response", new String(responseBody));
                    String res = new String(responseBody);
                    Log.e("Url------->", res.toString());

                    try {
                        Log.e("dsdsdsa","000000000");
                        JSONObject jsonObject = new JSONObject(res);
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("true")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Log.e("dsdsdsa","000000000");
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String id = jsonObject1.getString("id");
                                String user_id = jsonObject1.getString("user_id");
                                String fund_plan_type = jsonObject1.getString("fund_plan_type");
                                fund_amount = jsonObject1.getString("fund_amount");
                                String payment_method_id = jsonObject1.getString("payment_method_id");
                                String crypto_type_id = jsonObject1.getString("crypto_type_id");
                                String crypto_protocol_id = jsonObject1.getString("crypto_protocol_id");
                                String crypto_user_address_id = jsonObject1.getString("crypto_user_address_id");
                                String bank_account_number = jsonObject1.getString("bank_account_number");
                                String bank_ifsc_code = jsonObject1.getString("bank_ifsc_code");
                                String bank_branch_name = jsonObject1.getString("bank_branch_name");
                                String statuss = jsonObject1.getString("status");
                                String created_date = jsonObject1.getString("created_date");


                                paymentSetingListModels.add(new FilterListModel(id, fund_plan_type, fund_amount, created_date, statuss, ""));

                            }

                            if (jsonArray.length() == 0) {
                                if (paymentSettingListAdapter != null) {
                                    isOver = true;
                                    progressBardialog.setVisibility(View.GONE);
                                }
                            }




                            if (paymentSettingListAdapter == null) {
                                nodat.setVisibility(View.VISIBLE);

                                if (paymentSetingListModels.size() != 0) {
                                    progressBardialog.setVisibility(View.GONE);
                                    paymentSettingListAdapter = new FilterMainListAdapter(getActivity(), paymentSetingListModels);
                                    filterRecy.setAdapter(paymentSettingListAdapter);

                                } else {
                                    progressBardialog.setVisibility(View.GONE);
                                }
                            }
                            else {

                                paymentSettingListAdapter.notifyItemMoved(oldPosition, transactionModels.size());
                                paymentSettingListAdapter.notifyDataSetChanged();
                                progressBardialog.setVisibility(View.VISIBLE);
                            }






                            progressBardialog.setVisibility(View.GONE);
                            filterRecy.setVisibility(View.VISIBLE);


                        }
                        else {

                            progressBardialog.setVisibility(View.GONE);
                            String message = jsonObject.getString("message");

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                         isOver = true;
                        progressBardialog.setVisibility(View.GONE);
                          nodat.setVisibility(View.VISIBLE);
                        if (paymentSettingListAdapter != null) {
                            isOver = true;
                            progressBardialog.setVisibility(View.GONE);
                            nodat.setVisibility(View.GONE);
                        }





                    }
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressBardialog.setVisibility(View.GONE);

                method.alertBox(getResources().getString(R.string.something_went_wrong));
            }
        });
    }




}
