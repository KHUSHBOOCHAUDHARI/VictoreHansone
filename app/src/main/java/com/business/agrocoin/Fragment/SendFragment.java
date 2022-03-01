package com.business.agrocoin.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.business.agrocoin.R;
import com.business.agrocoin.Activity.MainActivity;
import com.business.agrocoin.Activity.ScanBarCodeActivity;
import com.business.agrocoin.Activity.TermandCondition;
import com.business.agrocoin.Util.Apis;
import com.business.agrocoin.Util.Method;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.business.agrocoin.databinding.SendFragmentBinding;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import cz.msebera.android.httpclient.Header;

public class SendFragment  extends Fragment {
    SendFragmentBinding binding;
    Method method;

    AlertDialog.Builder builder;
    public SendFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.send_fragment, container, false);
        MainActivity.txt_toolbartitle.setText("Send Currency");
        MainActivity.selctedrecy.setVisibility(View.GONE);
        builder = new AlertDialog.Builder(getActivity());
        method=new Method(getActivity());
        String terms = "<font color='#A7A7A7'>By proceeding you agree to the </font><font color='#004CEF'>Terms & Conditions</font>";
        binding.termandconditionTxt.setText(Html.fromHtml(terms), TextView.BufferType.SPANNABLE);
       ScanBarCodeActivity.ADDRESS_SCANED="";
        binding.termandconditionTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TermandCondition.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_enter, R.anim.left_out);
            }
        });





        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Address = binding.addressTxt.getText().toString();
                String Amount = binding.amountTxt.getText().toString();
                if (Address.equals("") || Address.isEmpty()) {
                    method.alertBox("Enter your address");

                }
                else if (Amount.equals("") || Amount.isEmpty()) {


                    method.alertBox("Enter Your Amount");
                } else {
                   // Toast.makeText(getActivity(), "Done", Toast.LENGTH_SHORT).show();
                    SendCurrency(method.pref.getString(method.Id, null),binding.addressTxt.getText().toString(), binding.amountTxt.getText().toString());

                }

            }
        });

        binding.scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ScanBarCodeActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_enter, R.anim.left_out);
            }
        });




        return binding.getRoot();
    }

    @Override
    public void onResume() {
        binding.addressTxt.setText(ScanBarCodeActivity.ADDRESS_SCANED.toString());
     //   Toast.makeText(getActivity(), ScanBarCodeActivity.ADDRESS_SCANED.toString(), Toast.LENGTH_SHORT).show();

        super.onResume();
    }



    public void SendCurrency(String UserId, String address, String amount) {
        binding.progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        String url = Apis.ROOT_URLL + getString(R.string.withdrow_api)+"userId="+UserId+"&to="+address+"&amount="+amount;
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

                        Float round_walletamt = Float.parseFloat(method.pref.getString(method.account_balance,null)) - Float.parseFloat(binding.amountTxt.getText().toString());


                        method.editor.putString(method.account_balance, String.valueOf(round_walletamt));
                        method.editor.commit();
                        Toast.makeText(getActivity(), jsonObject.getString("statusMsg").toString(), Toast.LENGTH_SHORT).show();

                       // builder.setTitle(jsonObject.getString("statusMsg"));

                        builder.setMessage(result.toString())
                                .setCancelable(false)
                                .setPositiveButton(Html.fromHtml("<font color='#029E9A'>Ok</font>"), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent=new Intent(getActivity(),MainActivity.class);
                                        startActivity(intent);
                                        getActivity().overridePendingTransition(R.anim.right_enter,R.anim.left_out);
                                        getActivity().finish();

                                    }

                                });

                        AlertDialog alert = builder.create();
                        alert.setTitle(Html.fromHtml("<font color='#029E9A'>Withdrawal Successfully</font>"));
                        alert.show();


                    } else {
                        binding.progressBar.setVisibility(View.GONE);
                        String message = jsonObject.getString("statusMsg");
                        method.alertBox(message);
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


