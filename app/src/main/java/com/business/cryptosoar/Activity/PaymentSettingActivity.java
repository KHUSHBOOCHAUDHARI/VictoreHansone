package com.business.cryptosoar.Activity;

import static com.business.cryptosoar.Activity.SignupActivity.getEditTextFilter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.Toast;

import com.business.cryptosoar.R;
import com.business.cryptosoar.Util.Apis;
import com.business.cryptosoar.Util.Method;
import com.business.cryptosoar.databinding.ActivityPaymentSettingBinding;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentSettingActivity extends AppCompatActivity {
    private ActivityPaymentSettingBinding binding;
    String str="1";
    Method method;
    private String phone_code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=DataBindingUtil.setContentView(PaymentSettingActivity.this,R.layout.activity_payment_setting);
        method=new Method(PaymentSettingActivity.this);
        binding.cash.isChecked();
        binding.cashLayout.setVisibility(View.VISIBLE);
        binding.bankdetailLayout.setVisibility(View.GONE);
        binding.nameTxt.setFilters(new InputFilter[]{getEditTextFilter()});
        binding.cityTxt.setFilters(new InputFilter[]{getEditTextFilter()});
        binding.branchnameTxt.setFilters(new InputFilter[]{getEditTextFilter()});

        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.right_enter,R.anim.left_out);
            }
        });
        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                form();
            }
        });
        phone_code = binding.ccp.getSelectedCountryCode();

        binding.ccp.registerCarrierNumberEditText(binding.contactTxt);
        binding.ccp.setNumberAutoFormattingEnabled(false);


        fullscreen();
    }


    public void form()
    {




        if (str.equals("1"))
        {
            String Name=binding.nameTxt.getText().toString();
            String ContactNo=binding.contactTxt.getText().toString();
            String city=binding.cityTxt.getText().toString();

            binding.banknumberTxt.setError(null);
            binding.ifscTxt.setError(null);
            binding.branchnameTxt.setError(null);



            if ( Name.isEmpty() || Name.equalsIgnoreCase("")) {
                binding.nameTxt.requestFocus();
                binding.nameTxt.setError("Please Enter Full Name");
            }

            else if ( ContactNo.isEmpty() || ContactNo.equalsIgnoreCase("")) {
                binding.contactTxt.requestFocus();
                binding.contactTxt.setError("Please Enter Contact Number");
            }

            else if(!binding.ccp.isValidFullNumber())
            {
                binding.contactTxt.requestFocus();
                binding.contactTxt.setError(getResources().getString(R.string.enter_validephone_no));

            }
            else if (city.equals("") || city.isEmpty()) {
                binding.cityTxt.requestFocus();
                binding.cityTxt.setError("Please Enter City");
            }

            else
            {
                paymentSetting(method.pref.getString(method.Id,null),"","","",Name,ContactNo,city);
            }
        }
        else if (str.equals("2"))
        {
            String AccountNo=binding.banknumberTxt.getText().toString();
            String IFSC=binding.ifscTxt.getText().toString();
            String branchname=binding.branchnameTxt.getText().toString();

            binding.banknumberTxt.setError(null);
            binding.ifscTxt.setError(null);
            binding.branchnameTxt.setError(null);

            if ( AccountNo.isEmpty() || AccountNo.equalsIgnoreCase("")) {
                binding.banknumberTxt.requestFocus();
                binding.banknumberTxt.setError("Please Enter Account Number");
            }

            else if ( IFSC.isEmpty() || IFSC.equalsIgnoreCase("")) {
                binding.ifscTxt.requestFocus();
                binding.ifscTxt.setError("Please Enter Ifsc Code");
            }


            else if (branchname.equals("") || branchname.isEmpty()) {
                binding.branchnameTxt.requestFocus();
                binding.branchnameTxt.setError("Please Enter Bank Name");
            }

            else
            {
                paymentSetting(method.pref.getString(method.Id,null),AccountNo,IFSC,branchname,"","","");
            }
        }



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

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.cash:
                if(checked)
                    binding.cashLayout.setVisibility(View.VISIBLE);
                binding.bankdetailLayout.setVisibility(View.GONE);
                str="1";
                break;
            case R.id.bank:
                if(checked)
                    binding.cashLayout.setVisibility(View.GONE);
                binding.bankdetailLayout.setVisibility(View.VISIBLE);
                str="2";
                break;

        }
           }



    private void paymentSetting(String user_id, String bank_account_number, String bank_ifsc_code, String bank_branch_name,
                              String contact_person_name, String contact_number,String state_city){

        binding.progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("user_id", user_id);
        params.add("bank_account_number", bank_account_number);
        params.add("bank_ifsc_code", bank_ifsc_code);
        params.add("bank_branch_name", bank_branch_name);
        params.add("contact_person_name", contact_person_name);
        params.add("contact_number",contact_number);
        params.add("state_city",state_city);

        Log.e("Parms",params.toString());

        String url = Apis.ROOT_URL+ getString(R.string.payment_setting);
        Log.e("Url------->",url);
//        client.addHeader("API-TOKEN", getString(R.string.API_TOKEN));
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                if (getApplicationContext() != null) {
                    String res = new String(responseBody);
                    Log.e("Url------->",res.toString());
                    // Toast.makeText(getApplicationContext(), res.toString(), Toast.LENGTH_SHORT).show();
                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("true")) {
                            JSONObject jsonObject1=jsonObject.getJSONObject("data");
                            String bank_account_number=jsonObject1.getString("bank_account_number");
                            String bank_ifsc_code=jsonObject1.getString("bank_ifsc_code");
                            String bank_branch_name=jsonObject1.getString("bank_branch_name");
                            String contact_person_name=jsonObject1.getString("contact_person_name");
                            String contact_number=jsonObject1.getString("contact_number");
                            String state_city=jsonObject1.getString("state_city");

                            method.editor.putString(method.BankAccountNumber, bank_account_number);
                            method.editor.putString(method.BankIfscCode, bank_ifsc_code);
                            method.editor.putString(method.BranchName, bank_branch_name);

                            method.editor.putString(method.Name, contact_person_name);
                            method.editor.putString(method.ContactNumber, contact_number);
                            method.editor.putString(method.City, state_city);

                            method.editor.putBoolean(method.pref_login, true);
                            method.editor.commit();
                            String message = jsonObject.getString("message");
                            Toast.makeText(PaymentSettingActivity .this, message.toString(), Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(PaymentSettingActivity.this, MainActivity.class);
                            startActivity(i);
                            overridePendingTransition(R.anim.right_enter,R.anim.left_out);
                        }  else {
                            binding.progressBar.setVisibility(View.GONE);
                            String Message=jsonObject.getString("message");
                            method.alertBox(Message.toString());
                        }
                        binding.progressBar.setVisibility(View.GONE);
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                        binding.progressBar.setVisibility(View.GONE);
                        method.alertBox(getResources().getString(R.string.failed_try_again));                                           }
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                binding.progressBar.setVisibility(View.GONE);
                method.alertBox(getResources().getString(R.string.something_went_wrong));
            }


        });
    }


}