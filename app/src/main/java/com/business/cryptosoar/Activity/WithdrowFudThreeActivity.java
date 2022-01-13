package com.business.cryptosoar.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.business.cryptosoar.Fragment.HistoryFragment;
import com.business.cryptosoar.Fragment.WalletFragment;
import com.business.cryptosoar.Fragment.WithdrowFragment;
import com.business.cryptosoar.R;
import com.business.cryptosoar.Util.Apis;
import com.business.cryptosoar.Util.Method;
import com.business.cryptosoar.databinding.ActivityWithdrawalFundListBinding;
import com.business.cryptosoar.databinding.ActivityWithdrowFudThreeBinding;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class WithdrowFudThreeActivity extends AppCompatActivity {
    private ActivityWithdrowFudThreeBinding binding;
    Method method;
    public static String TotalAmount="";
    public static
    String Responce="";
    public static String Responcett="";
    public static String FUND_ID="";
    public static String CONTRACT_CHARGE="";
    public static String CONTRACT_INTEREST="";
    String contract_end_charge;
    String currentString="";
    String contract_end_interest="";
    public static String RadioButton="";
    public static String CustomAmount="";
    String Baase="";
    String months="";
    public static String MONTHS="";
    int NewAmount=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(WithdrowFudThreeActivity.this,R.layout.activity_withdrow_fud_three);
         method=new Method(WithdrowFudThreeActivity.this);

        Calculate(method.pref.getString(method.Id,null),getIntent().getStringExtra("id"));
        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.right_enter,R.anim.left_out);
                finish();
            }
        });
     //   Toast.makeText(getApplicationContext(), months.toString(), Toast.LENGTH_SHORT).show();
        binding.notemsg.setText("$"+method.pref.getString(method.withdrawal_minimum_limit,null)+" Deposited to continue Re-Investment");
       String Title=getIntent().getStringExtra("title");
        String Amount=getIntent().getStringExtra("amount");
       if (!Title.equals("Reinvestment"))
       {
           binding.radiolayout.setVisibility(View.GONE);
           binding.interestpaidLytt.setVisibility(View.GONE);
           binding.calBtn.setVisibility(View.GONE);
           binding.requerstNowBtn.setVisibility(View.VISIBLE);
           binding.requerstNowBtn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   //Toast.makeText(getApplicationContext(), "yehh", Toast.LENGTH_SHORT).show();
                   if (Responce != null && !Responce.isEmpty() && !Responce.equals("null")) {
                    //   Toast.makeText(getApplicationContext(), "yes", Toast.LENGTH_SHORT).show();

                       CustomAmount=binding.totallText.getText().toString();
                       Responcett = binding.totallText.getText().toString();



                       if (RadioButton.equals("partially")) {
//                           Toast.makeText(getApplicationContext(), "nooo", Toast.LENGTH_SHORT).show();

                           if (binding.interestpaidTxt.getText().toString().equals("")) {
                               method.alertBox("Please Enter Amount");
                           } else {
                               MainActivity.withdrologo.setImageTintList(ContextCompat.getColorStateList(WithdrowFudThreeActivity.this, R.color.colorPrimaryDark));
                               MainActivity.withdrotxt.setTextColor(ContextCompat.getColor(WithdrowFudThreeActivity.this, R.color.colorPrimaryDark));
                               Intent intent = new Intent(WithdrowFudThreeActivity.this, MainActivity.class);
                               intent.putExtra("type", "1");
                               CustomAmount=binding.interestpaidTxt.getText().toString();
                               CONTRACT_CHARGE = contract_end_charge;
                               MONTHS=binding.montg.getText().toString();
                               CONTRACT_INTEREST = contract_end_interest;
                               FUND_ID = getIntent().getStringExtra("id");
                               Responcett = binding.tottalText.getText().toString();
                               startActivity(intent);
                               overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                           }
                       }
                       else if (RadioButton.equals("full")){

                        //   Toast.makeText(getApplicationContext(), "noooo", Toast.LENGTH_SHORT).show();

                           MainActivity.withdrologo.setImageTintList(ContextCompat.getColorStateList(WithdrowFudThreeActivity.this, R.color.colorPrimaryDark));
                           MainActivity.withdrotxt.setTextColor(ContextCompat.getColor(WithdrowFudThreeActivity.this, R.color.colorPrimaryDark));
                           Intent intent = new Intent(WithdrowFudThreeActivity.this, MainActivity.class);
                           intent.putExtra("type", "1");
                           String current = binding.totallText.getText().toString();
                           current = current.replace("$", "");
                           CustomAmount=current;
                           MONTHS=binding.montg.getText().toString();
                           CONTRACT_CHARGE = contract_end_charge;
                           CONTRACT_INTEREST = contract_end_interest;
                           FUND_ID = getIntent().getStringExtra("id");
                           Responcett = binding.totallText.getText().toString();
                           startActivity(intent);
                           overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                       }
                       else if (RadioButton.equals("")) {

                        //   Toast.makeText(getApplicationContext(), "hhhhh", Toast.LENGTH_SHORT).show();

                           MainActivity.withdrologo.setImageTintList(ContextCompat.getColorStateList(WithdrowFudThreeActivity.this, R.color.colorPrimaryDark));
                           MainActivity.withdrotxt.setTextColor(ContextCompat.getColor(WithdrowFudThreeActivity.this, R.color.colorPrimaryDark));
                           Intent intent = new Intent(WithdrowFudThreeActivity.this, MainActivity.class);
                           intent.putExtra("type", "1");
                           String current = binding.totallText.getText().toString();
                           current = current.replace("$", "");
                           CustomAmount=current;
                           MONTHS=binding.montg.getText().toString();
                           CONTRACT_CHARGE = contract_end_charge;
                           CONTRACT_INTEREST = contract_end_interest;
                           FUND_ID = getIntent().getStringExtra("id");
                           Responcett = binding.totallText.getText().toString();
                           startActivity(intent);
                           overridePendingTransition(R.anim.right_enter, R.anim.left_out);

                       }

                   }
               }
           });
       }
       else if(Title.equals("Reinvestment")) {
         //  Toast.makeText(getApplicationContext(), "yehh", Toast.LENGTH_SHORT).show();
           binding.radiolayout.setVisibility(View.GONE);
           binding.interestpaidLytt.setVisibility(View.GONE);
           binding.calBtn.setVisibility(View.GONE);
           binding.requerstNowBtn.setVisibility(View.VISIBLE);

           Baase = Amount.toString();
           String min = method.pref.getString(method.withdrawal_minimum_limit, null);
          if (!Baase.isEmpty()) {
            //  Toast.makeText(getApplicationContext(), "yes", Toast.LENGTH_SHORT).show();
              double min_val = Double.parseDouble(min);
              double Baase_val = Double.parseDouble(Baase);


              if (min_val >= Baase_val) {
             //     Toast.makeText(getApplicationContext(), "no", Toast.LENGTH_SHORT).show();
                  binding.radiolayout.setVisibility(View.GONE);
                  binding.interestpaidLytt.setVisibility(View.GONE);
                  binding.calBtn.setVisibility(View.GONE);
                  binding.requerstNowBtn.setVisibility(View.VISIBLE);
                  binding.requerstNowBtn.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                       //   Toast.makeText(getApplicationContext(), "yehh", Toast.LENGTH_SHORT).show();
                          if (Responce != null && !Responce.isEmpty() && !Responce.equals("null")) {
                          //    Toast.makeText(getApplicationContext(), "yes", Toast.LENGTH_SHORT).show();

                              CustomAmount=binding.totallText.getText().toString();
                              Responcett = binding.totallText.getText().toString();



                              if (RadioButton.equals("partially")) {
                                //  Toast.makeText(getApplicationContext(), "nooo", Toast.LENGTH_SHORT).show();

                                  if (binding.interestpaidTxt.getText().toString().equals("")) {
                                      method.alertBox("Please Enter Amount");
                                  } else {
                                      MainActivity.withdrologo.setImageTintList(ContextCompat.getColorStateList(WithdrowFudThreeActivity.this, R.color.colorPrimaryDark));
                                      MainActivity.withdrotxt.setTextColor(ContextCompat.getColor(WithdrowFudThreeActivity.this, R.color.colorPrimaryDark));
                                      Intent intent = new Intent(WithdrowFudThreeActivity.this, MainActivity.class);
                                      intent.putExtra("type", "1");
                                      CustomAmount=binding.interestpaidTxt.getText().toString();
                                      CONTRACT_CHARGE = contract_end_charge;
                                      MONTHS=binding.montg.getText().toString();
                                      CONTRACT_INTEREST = contract_end_interest;
                                      FUND_ID = getIntent().getStringExtra("id");
                                      Responcett = binding.tottalText.getText().toString();
                                      startActivity(intent);
                                      overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                                  }
                              }
                              else if (RadioButton.equals("full")){

                             //     Toast.makeText(getApplicationContext(), "noooo", Toast.LENGTH_SHORT).show();

                                  MainActivity.withdrologo.setImageTintList(ContextCompat.getColorStateList(WithdrowFudThreeActivity.this, R.color.colorPrimaryDark));
                                  MainActivity.withdrotxt.setTextColor(ContextCompat.getColor(WithdrowFudThreeActivity.this, R.color.colorPrimaryDark));
                                  Intent intent = new Intent(WithdrowFudThreeActivity.this, MainActivity.class);
                                  intent.putExtra("type", "1");
                                  String current = binding.totallText.getText().toString();
                                  current = current.replace("$", "");
                                  CustomAmount=current;
                                  MONTHS=binding.montg.getText().toString();
                                  CONTRACT_CHARGE = contract_end_charge;
                                  CONTRACT_INTEREST = contract_end_interest;
                                  FUND_ID = getIntent().getStringExtra("id");
                                  Responcett = binding.totallText.getText().toString();
                                  startActivity(intent);
                                  overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                              }
                              else if (RadioButton.equals("")) {

                               //   Toast.makeText(getApplicationContext(), "hhhhh", Toast.LENGTH_SHORT).show();

                                  MainActivity.withdrologo.setImageTintList(ContextCompat.getColorStateList(WithdrowFudThreeActivity.this, R.color.colorPrimaryDark));
                                  MainActivity.withdrotxt.setTextColor(ContextCompat.getColor(WithdrowFudThreeActivity.this, R.color.colorPrimaryDark));
                                  Intent intent = new Intent(WithdrowFudThreeActivity.this, MainActivity.class);
                                  intent.putExtra("type", "1");
                                  String current = binding.totallText.getText().toString();
                                  current = current.replace("$", "");
                                  CustomAmount=current;
                                  MONTHS=binding.montg.getText().toString();
                                  CONTRACT_CHARGE = contract_end_charge;
                                  CONTRACT_INTEREST = contract_end_interest;
                                  FUND_ID = getIntent().getStringExtra("id");
                                  Responcett = binding.totallText.getText().toString();
                                  startActivity(intent);
                                  overridePendingTransition(R.anim.right_enter, R.anim.left_out);

                              }

                          }
                      }
                  });
              }
              else {
                //  Toast.makeText(getApplicationContext(), "nooo", Toast.LENGTH_SHORT).show();
                  binding.radiolayout.setVisibility(View.VISIBLE);
                  binding.interestpaidLytt.setVisibility(View.GONE);
                  binding.calBtn.setVisibility(View.GONE);
                  binding.requerstNowBtn.setVisibility(View.VISIBLE);



                  if (!binding.interestpaidTxt.getText().toString().equals("")) {
                      binding.calBtn.setVisibility(View.GONE);
                      binding.requerstNowBtn.setVisibility(View.VISIBLE);
                  }

                  binding.interestpaidTxt.addTextChangedListener(new TextWatcher() {

                      public void afterTextChanged(Editable s) {

                      }

                      public void beforeTextChanged(CharSequence s, int start,
                                                    int count, int after) {

                      }

                      public void onTextChanged(CharSequence s, int start,
                                                int before, int count) {
                          if (!binding.interestpaidTxt.getText().toString().equals("")) {
                              binding.calBtn.setVisibility(View.GONE);
                              binding.requerstNowBtn.setVisibility(View.GONE);
                          } else if (binding.interestpaidTxt.getText().toString().equals("")) {
                              binding.calBtn.setVisibility(View.GONE);
                              binding.requerstNowBtn.setVisibility(View.GONE);

                          }
                      }
                  });


                  binding.interestpaidTxt.setOnKeyListener(new View.OnKeyListener() {
                      public boolean onKey(View v, int keyCode, KeyEvent event) {
                          if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                              if (!Baase.isEmpty()) {
                                  String Baase = binding.interestpaidTxt.getText().toString();
                                  String min = binding.tottalText.getText().toString();
                                  currentString = min.toString();
                                  currentString = currentString.replace("$", "");
                                  double min_val = Double.parseDouble(currentString);
                                  double Baase_val = Double.parseDouble(Baase);

                                  if (min_val < Baase_val) {

                                      method.alertBox("The Maximum Withdraw Amount is " + "$" + currentString);
                                  } else if (binding.interestpaidTxt.getText().toString().equals("")) {
                                      method.alertBox("Please Enter Amount");
                                  } else {
                                      NewCalculate(method.pref.getString(method.Id, null), getIntent().getStringExtra("id"), RadioButton.toString(), binding.interestpaidTxt.getText().toString());
                                  }
                              }
                              return true;
                          }
                          return false;
                      }
                  });
                  binding.calBtn.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {

                          String Baase = binding.interestpaidTxt.getText().toString();
                          String min = binding.tottalText.getText().toString();
                          currentString = min.toString();
                          currentString = currentString.replace("$", "");
                          double min_val = Double.parseDouble(currentString);
                          double Baase_val = Double.parseDouble(Baase);

                          if (min_val < Baase_val) {

                              method.alertBox("The Maximum Withdraw Amount is " + "$" + currentString);
                          } else if (binding.interestpaidTxt.getText().toString().equals("")) {
                              method.alertBox("Please Enter Amount");
                          } else {
                              NewCalculate(method.pref.getString(method.Id, null), getIntent().getStringExtra("id"), RadioButton.toString(), binding.interestpaidTxt.getText().toString());
                          }
                      }
                  });
                  binding.partiallyRadio.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                          if (binding.partiallyRadio.isChecked()) {

                              String Limit = method.pref.getString(method.withdrawal_minimum_limit, null);
                              String AmountT = binding.tottalText.getText().toString();
                              currentString = AmountT.toString();
                              currentString = currentString.replace("$", "");
                              int NewAmount = Integer.parseInt(currentString) - Integer.parseInt(Limit);
                              binding.interestpaidTxt.setText(String.valueOf(NewAmount));

                              binding.interestpaidLytt.setVisibility(View.VISIBLE);
                              binding.fullamountRadio.setChecked(false);
                              RadioButton = "partially";

                              binding.calBtn.setVisibility(View.GONE);
                              binding.requerstNowBtn.setVisibility(View.VISIBLE);
                              String Baase = binding.interestpaidTxt.getText().toString();
                              String min = binding.tottalText.getText().toString();
                              currentString = min.toString();
                              currentString = currentString.replace("$", "");
                              double min_val = Double.parseDouble(currentString);
                              double Baase_val = Double.parseDouble(Baase);

                              if (min_val < Baase_val) {

                                  method.alertBox("The Maximum Withdraw Amount is " + "$" + currentString);
                              } else if (binding.interestpaidTxt.getText().toString().equals("")) {
                                  method.alertBox("Please Enter Amount");
                              } else {
                                  NewCalculate(method.pref.getString(method.Id, null), getIntent().getStringExtra("id"), RadioButton.toString(), binding.interestpaidTxt.getText().toString());
                              }
                             // Toast.makeText(getApplicationContext(), RadioButton.toString(), Toast.LENGTH_SHORT).show();
                          }
                      }
                  });
                  binding.fullamountRadio.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                          if (binding.fullamountRadio.isChecked()) {
                              binding.interestpaidLytt.setVisibility(View.GONE);
                              binding.partiallyRadio.setChecked(false);
                              binding.calBtn.setVisibility(View.GONE);
                              binding.requerstNowBtn.setVisibility(View.VISIBLE);
                              RadioButton = "full";
                              binding.calBtn.setVisibility(View.GONE);

                              Calculate(method.pref.getString(method.Id, null), getIntent().getStringExtra("id"));


                            //  Toast.makeText(getApplicationContext(), RadioButton.toString(), Toast.LENGTH_SHORT).show();
                          }
                          binding.requerstNowBtn.setVisibility(View.VISIBLE);
                      }
                  });

                  if (binding.fullamountRadio.isChecked())
                  {
                      binding.requerstNowBtn.setVisibility(View.VISIBLE);
                  }
                  binding.requerstNowBtn.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                      //    Toast.makeText(getApplicationContext(), "yehh", Toast.LENGTH_SHORT).show();
                          if (Responce != null && !Responce.isEmpty() && !Responce.equals("null")) {
                            //  Toast.makeText(getApplicationContext(), "yes", Toast.LENGTH_SHORT).show();

                              CustomAmount=binding.totallText.getText().toString();
                              Responcett = binding.totallText.getText().toString();



                              if (RadioButton.equals("partially")) {
                              //    Toast.makeText(getApplicationContext(), "nooo", Toast.LENGTH_SHORT).show();

                                  if (binding.interestpaidTxt.getText().toString().equals("")) {
                                      method.alertBox("Please Enter Amount");
                                  } else {
                                      MainActivity.withdrologo.setImageTintList(ContextCompat.getColorStateList(WithdrowFudThreeActivity.this, R.color.colorPrimaryDark));
                                      MainActivity.withdrotxt.setTextColor(ContextCompat.getColor(WithdrowFudThreeActivity.this, R.color.colorPrimaryDark));
                                      Intent intent = new Intent(WithdrowFudThreeActivity.this, MainActivity.class);
                                      intent.putExtra("type", "1");
                                      CustomAmount=binding.interestpaidTxt.getText().toString();
                                      CONTRACT_CHARGE = contract_end_charge;
                                      MONTHS=binding.montg.getText().toString();
                                      CONTRACT_INTEREST = contract_end_interest;
                                      FUND_ID = getIntent().getStringExtra("id");
                                      Responcett = binding.tottalText.getText().toString();
                                      startActivity(intent);
                                      overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                                  }
                              }
                              else if (RadioButton.equals("full")){

                               //   Toast.makeText(getApplicationContext(), "noooo", Toast.LENGTH_SHORT).show();

                                  MainActivity.withdrologo.setImageTintList(ContextCompat.getColorStateList(WithdrowFudThreeActivity.this, R.color.colorPrimaryDark));
                                  MainActivity.withdrotxt.setTextColor(ContextCompat.getColor(WithdrowFudThreeActivity.this, R.color.colorPrimaryDark));
                                  Intent intent = new Intent(WithdrowFudThreeActivity.this, MainActivity.class);
                                  intent.putExtra("type", "1");
                                  String current = binding.totallText.getText().toString();
                                  current = current.replace("$", "");
                                  CustomAmount=current;
                                  MONTHS=binding.montg.getText().toString();
                                  CONTRACT_CHARGE = contract_end_charge;
                                  CONTRACT_INTEREST = contract_end_interest;
                                  FUND_ID = getIntent().getStringExtra("id");
                                  Responcett = binding.totallText.getText().toString();
                                  startActivity(intent);
                                  overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                              }
                              else if (RadioButton.equals("")) {

                               //   Toast.makeText(getApplicationContext(), "hhhhh", Toast.LENGTH_SHORT).show();

                                  MainActivity.withdrologo.setImageTintList(ContextCompat.getColorStateList(WithdrowFudThreeActivity.this, R.color.colorPrimaryDark));
                                  MainActivity.withdrotxt.setTextColor(ContextCompat.getColor(WithdrowFudThreeActivity.this, R.color.colorPrimaryDark));
                                  Intent intent = new Intent(WithdrowFudThreeActivity.this, MainActivity.class);
                                  intent.putExtra("type", "1");
                                  String current = binding.totallText.getText().toString();
                                  current = current.replace("$", "");
                                  CustomAmount=current;
                                  MONTHS=binding.montg.getText().toString();
                                  CONTRACT_CHARGE = contract_end_charge;
                                  CONTRACT_INTEREST = contract_end_interest;
                                  FUND_ID = getIntent().getStringExtra("id");
                                  Responcett = binding.totallText.getText().toString();
                                  startActivity(intent);
                                  overridePendingTransition(R.anim.right_enter, R.anim.left_out);

                              }

                          }
                      }
                  });
              }
          }



       }







        fullscreen();

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
    public void Calculate(String UserId,String fund_id) {
        binding.layout.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("user_id", UserId);
        params.add("fund_id", fund_id);
        String url = Apis.ROOT_URL + getString(R.string.end_contract_calculation);
        Log.e("Url------->",url);

        client.post(url,params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);
                Log.e("ressss------->",res.toString());
                try {
                    Responce=res.toString();
                    JSONObject jsonObject = new JSONObject(res);

                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("true")) {
                        binding.layout.setVisibility(View.VISIBLE);
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        String contract_fund_amount = jsonObject1.getString("contract_fund_amount");
                        String contract_end_per = jsonObject1.getString("contract_end_per");
                        contract_end_charge = jsonObject1.getString("contract_end_charge");
                        String contract_end_interest_per = jsonObject1.getString("contract_end_interest_per");
                        contract_end_interest = jsonObject1.getString("contract_end_interest");
                        String contract_total = jsonObject1.getString("contract_total");
                        String months = jsonObject1.getString("months");
                        String days = jsonObject1.getString("days");
                        String fund_id = jsonObject1.getString("fund_id");
                        binding.montg.setText(months);
                        String contract_date = jsonObject1.getString("contract_date");
                        binding.tottalText.setText("$"+contract_fund_amount);
                        binding.interestpaidTxt.setText(contract_fund_amount);
                        binding.cancelationText.setText("$"+contract_end_charge);
                        binding.interestText.setText("$"+contract_end_interest);
                        binding.totallText.setText("$"+contract_total);
                        binding.cancelationTag.setText("Cancellation Charge"+"("+contract_end_per+"%"+")");
                        binding.interesTag.setText("Interest Amount"+"("+contract_end_interest_per+"%"+")");
                        binding.requerstNowBtn.setVisibility(View.VISIBLE);



                    }

                    else {
                        binding.layout.setVisibility(View.VISIBLE);
                        binding.progressBar.setVisibility(View.GONE);
                        String message = jsonObject.getString("message");
                        Toast.makeText(WithdrowFudThreeActivity.this, message.toString(), Toast.LENGTH_SHORT).show();
                    }
                    binding.progressBar.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    binding.progressBar.setVisibility(View.GONE);
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                    binding.layout.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                binding.progressBar.setVisibility(View.GONE);
                method.alertBox(getResources().getString(R.string.something_went_wrong));
                binding.layout.setVisibility(View.VISIBLE);
            }
        });
    }
    public void NewCalculate(String UserId,String fund_id,String contract_type ,String custom_amount ) {

        binding.progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("user_id", UserId);
        params.add("fund_id", fund_id);
        params.add("contract_type", contract_type);
        params.add("custom_amount", custom_amount);

        String url = Apis.ROOT_URL + getString(R.string.end_contract_calculation);
        Log.e("Url------->",url);

        client.post(url,params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);
                Log.e("ressss------->",res.toString());
                try {
                    Responce=res.toString();
                    JSONObject jsonObject = new JSONObject(res);

                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        String contract_fund_amount = jsonObject1.getString("contract_fund_amount");
                        String contract_end_per = jsonObject1.getString("contract_end_per");
                        contract_end_charge = jsonObject1.getString("contract_end_charge");
                        String contract_end_interest_per = jsonObject1.getString("contract_end_interest_per");
                        contract_end_interest = jsonObject1.getString("contract_end_interest");
                        String contract_total = jsonObject1.getString("contract_total");
                       months = jsonObject1.getString("months");
                        String days = jsonObject1.getString("days");
                        String fund_id = jsonObject1.getString("fund_id");
                        String contract_date = jsonObject1.getString("contract_date");
                        binding.montg.setText(months);

                        binding.tottalText.setText("$"+contract_fund_amount);
                        binding.interestpaidTxt.setText(contract_fund_amount);
                        binding.cancelationText.setText("$"+contract_end_charge);
                        binding.interestText.setText("$"+contract_end_interest);
                        binding.totallText.setText("$"+contract_total);
                        binding.cancelationTag.setText("Cancellation Charge"+"("+contract_end_per+"%"+")");
                        binding.interesTag.setText("Interest Amount"+"("+contract_end_interest_per+"%"+")");


                        binding.calBtn.setVisibility(View.GONE);
                        binding.requerstNowBtn.setVisibility(View.VISIBLE);



                    }

                    else {
                        binding.progressBar.setVisibility(View.GONE);
                        String message = jsonObject.getString("message");
                        Toast.makeText(WithdrowFudThreeActivity.this, message.toString(), Toast.LENGTH_SHORT).show();
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