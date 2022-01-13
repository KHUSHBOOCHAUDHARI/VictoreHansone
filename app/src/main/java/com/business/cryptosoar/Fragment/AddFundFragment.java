package com.business.cryptosoar.Fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.business.cryptosoar.Activity.ConformationActivity;
import com.business.cryptosoar.Activity.MainActivity;
import com.business.cryptosoar.Activity.TermandCondition;
import com.business.cryptosoar.R;
import com.business.cryptosoar.Util.Apis;
import com.business.cryptosoar.Util.Method;
import com.business.cryptosoar.databinding.AddfundFragmentBinding;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class AddFundFragment extends Fragment {

    AddfundFragmentBinding binding;
    RadioButton radioButton;
    Method method;
    float P=00;
    float U;
    float T;
    float SI=00;
    String Method="Monthly Payment";
    public AddFundFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.addfund_fragment, container, false);
        method=new Method(getActivity());
        binding.accountBalanceTxt.setText("$"+method.pref.getString(method.account_balance,null));
        MainActivity.txt_toolbartitle.setText(getResources().getString(R.string.add_fund_txt));
        MainActivity.RelativeLayout.setVisibility(View.VISIBLE);
       MainActivity.intLayout.setVisibility(View.GONE);
        MainActivity.filter.setVisibility(View.GONE);
        WalletHistory(method.pref.getString(method.Id,null));
    //    DisableWithDrawal(method.pref.getString(method.Id,null));
//        Toast.makeText(getActivity(), method.pref.getString(method.monthly_interest_percentage,null), Toast.LENGTH_SHORT).show();
        int selectedID = binding.groop.getCheckedRadioButtonId();
        String terms = "<font color='#FFFFFF'>By proceeding you agree to the </font><font color='#BF953F'>Terms & Conditions</font>";
        binding.termandconditionTxt.setText(Html.fromHtml(terms), TextView.BufferType.SPANNABLE);


        String interest = "<font color='#A7A7A7'>Interest to be paid on the every month </font>"+"$"+00.00;
        binding.noteTxt.setText(Html.fromHtml(interest), TextView.BufferType.SPANNABLE);


      //  binding.noteTag.setText("Get monthly " + method.pref.getString(method.monthly_interest_percentage,null) + "% Interest Cancelling contract before time period would be chargeable.");
        binding.noteTag.setText("Get monthly " + method.pref.getString(method.monthly_interest_percentage,null) + "% Interest Cancelling contract before time period would be chargeable.");

        binding.monthlyRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked())
                {
                    binding.noteTag.setText("Get monthly " + method.pref.getString(method.monthly_interest_percentage,null) + "% Interest Cancelling contract before time period would be chargeable.");

                }
                else
                {
                                     binding.noteTag.setText("Get monthly 5% Interest in your capital amount.");

                }



                                         }
        });


        binding.termandconditionTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), TermandCondition.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_enter,R.anim.left_out);
            }
        });

        binding.interestpaidTxt.setHint("Minimum Deposit Amount is " +"$"+ method.pref.getString(method.addfund_minimum_diposit,null));

        binding.interestpaidTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (binding.interestpaidTxt.getText().toString().equals(""))
                {
                    P= 00;
                }
                else {
                    P=Float.parseFloat(binding.interestpaidTxt.getText().toString());
                }


                U=Float.parseFloat(method.pref.getString(method.monthly_interest_percentage,null));
                SI=(P*U)/100;
                String inttt= String.valueOf(SI);
                String interest = "<font color='#A7A7A7'>Interest to be paid at the end of the month </font>"+"$"+inttt;
                binding.noteTxt.setText(Html.fromHtml(interest), TextView.BufferType.SPANNABLE);

            }
        });


        binding.interestpaidTxt.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    String Baase= binding.interestpaidTxt.getText().toString();
                    String min= method.pref.getString(method.addfund_minimum_diposit,null);
                    String max=method.pref.getString(method.addfund_maximum_diposit,null);

                    if (!Baase.isEmpty())
                    {

                        double min_val = Double.parseDouble(min);
                        double Baase_val = Double.parseDouble(Baase);
                        double max_val = Double.parseDouble(max);
                        if (min_val  > Baase_val) {

                            method.alertBox("The Minimum Deposit Amount is "+"$"+method.pref.getString(method.addfund_minimum_diposit,null));
                        }


                    }

                    return true;
                }
                return false;
            }
        });

        binding.requerstNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String Baase= binding.interestpaidTxt.getText().toString();
                String min= method.pref.getString(method.addfund_minimum_diposit,null);
                String max=method.pref.getString(method.addfund_maximum_diposit,null);


                if (Baase.equals("") || Baase.isEmpty())
                {
                    binding.interestpaidTxt.requestFocus();
                    binding.interestpaidTxt.setError(getResources().getString(R.string.enter_amount_txt));
                }
                else {
                    double min_val = Double.parseDouble(min);
                    double Baase_val = Double.parseDouble(Baase);
                    double max_val = Double.parseDouble(max);
                     if (min_val  > Baase_val) {

                       method.alertBox("The Minimum Deposit Amount is "+"$"+method.pref.getString(method.addfund_minimum_diposit,null));
                    }



                    else
                     {
                         if (binding.termandconditionBox.isChecked()) {
                             int selectedId = binding.groop.getCheckedRadioButtonId();
                             radioButton = (RadioButton) getActivity().findViewById(selectedId);
                             String Method = radioButton.getText().toString();
                             String FundAmount = binding.interestpaidTxt.getText().toString();

                             if (FundAmount.equals("") || FundAmount.isEmpty()) {

                                 binding.interestpaidTxt.requestFocus();
                                 binding.interestpaidTxt.setError("Please Enter Amount");


                             }
                             else {


                                 if (Method.equals("Monthly Payment")) {
                                     Intent intent = new Intent(getActivity(), ConformationActivity.class);
                                     intent.putExtra("amount", binding.interestpaidTxt.getText().toString());
                                     intent.putExtra("type", "monthly");
                                     startActivity(intent);
                                     getActivity().overridePendingTransition(R.anim.right_enter, R.anim.left_out);

                                 } else {
                                     Intent intent = new Intent(getActivity(), ConformationActivity.class);
                                     intent.putExtra("amount", binding.interestpaidTxt.getText().toString());
                                     intent.putExtra("type", "reinvestment");
                                     startActivity(intent);
                                     getActivity().overridePendingTransition(R.anim.right_enter, R.anim.left_out);

                                 }
                             }
                         }
                         else
                         {
                             method.alertBox("Please Select Terms & Condition");
                         }
                     }

                }






            }
        });


        return binding.getRoot();
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

                        String message = jsonObject.getString("message");
                        Toast.makeText(getActivity(), message.toString(), Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();

                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                method.alertBox(getResources().getString(R.string.something_went_wrong));
            }
        });
    }

}
