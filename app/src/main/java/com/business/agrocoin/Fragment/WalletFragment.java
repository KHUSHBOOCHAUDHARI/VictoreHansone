package com.business.agrocoin.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.business.agrocoin.Activity.MainActivity;
import com.business.agrocoin.Adapter.WalletAdaptor;
import com.business.agrocoin.Model.WalletModel;
import com.business.agrocoin.R;
import com.business.agrocoin.Util.Apis;
import com.business.agrocoin.Util.Method;
import com.business.agrocoin.databinding.WalletFragmentBinding;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class WalletFragment extends Fragment {
    public static String user_id;
    public static RecyclerView filterRecyclerview, tansectionRecyclerview;
    public static ProgressBar progressBar;
    public static TextView nodata, accrued_txt, toalinterest_txt, account_balance_txt;
    public static String TYTPE = "";
    WalletFragmentBinding binding;
    Method method;
    private List<WalletModel> walletModels;
    private WalletAdaptor walletAdaptor;

    public WalletFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.wallet_fragment, container, false);
        method = new Method(getActivity());
        MainActivity.txt_toolbartitle.setText(getResources().getString(R.string.welcome_dashboard_txt));
        MainActivity.selctedrecy.setVisibility(View.VISIBLE);
        MainActivity.filter.setVisibility(View.VISIBLE);
        filterRecyclerview = binding.filterRecyclerview;
        tansectionRecyclerview = binding.tansectionRecyclerview;
        progressBar = binding.progressBar;
        accrued_txt = binding.accruedTxt;
        toalinterest_txt = binding.toalinterestTxt;
        account_balance_txt = binding.accountBalanceTxt;
        nodata = binding.textViewCategory;
        walletModels = new ArrayList<>();
        WalletFragment.TYTPE.equals("2");
        binding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (method.isNetworkAvailable(getActivity())) {
                    data(method.pref.getString(method.Id, null));
                } else {
                    method.alertBox(getResources().getString(R.string.internet_connection));
                }
            }
        });
        try {
            JSONArray itemArray = new JSONArray(method.pref.getString(method.Address_Id, null));
            if (itemArray.length() != 0) {
                binding.progressBar.setVisibility(View.GONE);
                for (int i = 0; i < itemArray.length(); i++) {
                    WalletModel contact = new WalletModel();
                    contact.setAddress(String.valueOf(itemArray.getString(i)));
                    contact.setTitle("Agrocoin");
                    walletModels.add(contact);
                }
                binding.textViewCategory.setVisibility(View.GONE);

            } else {
                binding.textViewCategory.setVisibility(View.VISIBLE);

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        binding.tansectionRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        walletAdaptor = new WalletAdaptor(getActivity(), walletModels);
        binding.tansectionRecyclerview.setAdapter(walletAdaptor);
        binding.tansectionRecyclerview.setNestedScrollingEnabled(false);
        binding.tansectionRecyclerview.setHasFixedSize(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        binding.tansectionRecyclerview.setLayoutManager(layoutManager);
        binding.tansectionRecyclerview.setFocusable(false);
        return binding.getRoot();
    }

    private void data(String UserId) {
        binding.progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        String url = Apis.ROOT_URLL + getString(R.string.create_allet) + "userId=" + UserId;
        Log.e("Url------->", url);
        client.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);
                Log.e("Url------->", res.toString());
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    String status = jsonObject.getString("statusCode");
                    if (status.equalsIgnoreCase("200")) {
                        String result = jsonObject.getString("result");
                        if (method.isNetworkAvailable(getActivity())) {
                            binding.progressBar.setVisibility(View.VISIBLE);
                            login(method.pref.getString(method.UserName, null), method.pref.getString(method.Password, null));
                        } else {
                            method.alertBox(getResources().getString(R.string.internet_connection));
                        }
                    } else {
                        binding.progressBar.setVisibility(View.GONE);
                        String message = jsonObject.getString("statusMsg");
                        method.alertBox(message.toString());
                    }
                } catch (JSONException e) {
                    binding.progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }


    //Login Api
    public void login(String email, String sendPassword) {
        binding.progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        String url = Apis.ROOT_URLL + getString(R.string.login_user) + "username=" + email + "&password=" + sendPassword;
        Log.e("Url------->", url);
        client.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);
                Log.e("Url------->", res.toString());
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    String status = jsonObject.getString("statusCode");
                    if (status.equalsIgnoreCase("200")) {
                        Toast.makeText(getActivity(), "Address created successfully", Toast.LENGTH_SHORT).show();
                        JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                        JSONArray itemArray = jsonObject1.getJSONArray("walletAddress");
                        method.editor.putString(method.Address_Id, itemArray.toString());
                        method.editor.commit();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finishAffinity();
                    } else {
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


}
