package com.business.agrocoin.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.business.agrocoin.Activity.MainActivity;
import com.business.agrocoin.Adapter.TransactionHistoryAdapter;
import com.business.agrocoin.Model.TransactionModel;
import com.business.agrocoin.R;
import com.business.agrocoin.Util.Apis;
import com.business.agrocoin.Util.Method;
import com.business.agrocoin.databinding.HistoryFragmentBinding;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class HistoryFragment extends Fragment {
    HistoryFragmentBinding binding;
    private List<TransactionModel> transactionModels;
    TransactionHistoryAdapter TransactionHistoryAdapter;
    Method method;
    private Boolean isOver = false;
    public static String catid, keyword, discount_by, user_id;
    private int oldPosition = 0;
    private int pagination_index = 1;
    String str = "1";
    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.history_fragment, container, false);
        MainActivity.txt_toolbartitle.setText(getResources().getString(R.string.transaction_history_txt));
        MainActivity.selctedrecy.setVisibility(View.GONE);


        method = new Method(getActivity());

        binding.deposit.isChecked();
        transactionModels = new ArrayList<>();
        MainActivity.filter.setVisibility(View.VISIBLE);
        binding.tansectionRecyclerview.setNestedScrollingEnabled(false);
        binding.tansectionRecyclerview.setHasFixedSize(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        binding.tansectionRecyclerview.setLayoutManager(layoutManager);
        binding.tansectionRecyclerview.setFocusable(false);

        if (Method.isNetworkAvailable(getActivity())) {
            TrasectionHistoryPage(method.pref.getString(method.Id, null),"DEPOSIT");
          //  Collections.reverse(transactionModels);
        } else {
            method.alertBox(getResources().getString(R.string.internet_connection));
            binding.progressBar.setVisibility(View.GONE);
        }

        binding.deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRadioButtonClickedd(v);
            }
        });
        binding.withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRadioButtonClickedd(v);
            }
        });






        return binding.getRoot();
    }

    public void onRadioButtonClickedd(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.deposit:
              //  Toast.makeText(getActivity(), "Depo", Toast.LENGTH_SHORT).show();
                binding.tansectionRecyclerview.setNestedScrollingEnabled(false);
                binding.tansectionRecyclerview.setHasFixedSize(false);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

                binding.tansectionRecyclerview.setLayoutManager(layoutManager);
                binding.tansectionRecyclerview.setFocusable(false);

                if (Method.isNetworkAvailable(getActivity())) {
                    TrasectionHistoryPage(method.pref.getString(method.Id, null),"DEPOSIT");
                   // Collections.reverse(transactionModels);
                } else {
                    method.alertBox(getResources().getString(R.string.internet_connection));
                    binding.progressBar.setVisibility(View.GONE);
                }

                str = "1";
                break;
            case R.id.withdraw:
                if (checked)
                  //  Toast.makeText(getActivity(), "With", Toast.LENGTH_SHORT).show();
                    binding.tansectionRecyclerview.setNestedScrollingEnabled(false);
                binding.tansectionRecyclerview.setHasFixedSize(false);
                LinearLayoutManager layoutManagerr = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

                binding.tansectionRecyclerview.setLayoutManager(layoutManagerr);
                binding.tansectionRecyclerview.setFocusable(false);

                if (Method.isNetworkAvailable(getActivity())) {
                    TrasectionHistoryPage(method.pref.getString(method.Id, null),"WITHDRAW");

                } else {
                    method.alertBox(getResources().getString(R.string.internet_connection));
                    binding.progressBar.setVisibility(View.GONE);
                }

                str = "2";
                break;

        }
    }

    private void TrasectionHistoryPage(String UserId, String TransactionType) {
        transactionModels.clear();
             binding.textViewCategory.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.VISIBLE);
            AsyncHttpClient client = new AsyncHttpClient();
        String url = Apis.ROOT_URLL + getString(R.string.transection_history)+"userId="+UserId+"&transactionType="+TransactionType;
        Log.e("Url------->", url);

        client.post(url,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);
                Log.e("Url------->", res.toString());
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    String status = jsonObject.getString("statusCode");
                    if (status.equalsIgnoreCase("200")) {

                        binding.textViewCategory.setVisibility(View.GONE);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String date = jsonObject1.getString("date");
                            String amount = jsonObject1.getString("amount");
                            String txHash = jsonObject1.getString("txHash");
                            String type = jsonObject1.getString("type");
                            transactionModels.add(new TransactionModel("",type,amount,type,date,"Success",txHash));
                        }



                                if (transactionModels.size() != 0) {
                                    binding.progressBar.setVisibility(View.GONE);
                                    Collections.reverse(transactionModels);
                                    TransactionHistoryAdapter = new TransactionHistoryAdapter(getActivity(), transactionModels);
                                    binding.tansectionRecyclerview.setAdapter(TransactionHistoryAdapter);
                                    binding.textViewCategory.setVisibility(View.GONE);

                                } else {
                                    binding.textViewCategory.setVisibility(View.VISIBLE);

                                }



                        } else {
                            binding.progressBar.setVisibility(View.GONE);
                            binding.progressbarLoadmore.setVisibility(View.GONE);
                            String message = jsonObject.getString("statusMsg");
                           // binding.textViewCategory.setText(message);
                            //binding.textViewCategory.setVisibility(View.VISIBLE);
                        }

                        binding.progressBar.setVisibility(View.GONE);
                        binding.progressbarLoadmore.setVisibility(View.GONE);
                        binding.linearLayoutHomeAdapter.setVisibility(View.VISIBLE);

                    } catch (JSONException e) {
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



            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                binding.progressbarLoadmore.setVisibility(View.GONE);
            }
        });
    }






}
