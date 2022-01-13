package com.business.cryptosoar.Fragment;

import static com.business.cryptosoar.Activity.WithdrowFudThreeActivity.CONTRACT_CHARGE;
import static com.business.cryptosoar.Activity.WithdrowFudThreeActivity.CONTRACT_INTEREST;
import static com.business.cryptosoar.Activity.WithdrowFudThreeActivity.FUND_ID;
import static com.business.cryptosoar.Activity.WithdrowFudThreeActivity.RadioButton;
import static com.business.cryptosoar.Activity.WithdrowFudThreeActivity.Responce;
import static com.business.cryptosoar.Adapter.WithdrowPaymentOptAdapter.AccountNumber;
import static com.business.cryptosoar.Adapter.WithdrowPaymentOptAdapter.BranchName;
import static com.business.cryptosoar.Adapter.WithdrowPaymentOptAdapter.City;
import static com.business.cryptosoar.Adapter.WithdrowPaymentOptAdapter.ContactNumber;
import static com.business.cryptosoar.Adapter.WithdrowPaymentOptAdapter.ContactPersonName;
import static com.business.cryptosoar.Adapter.WithdrowPaymentOptAdapter.IFSC;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.business.cryptosoar.Activity.ConformationActivity;
import com.business.cryptosoar.Activity.MainActivity;
import com.business.cryptosoar.Activity.PDFUploade;
import com.business.cryptosoar.Activity.WithdrawalFundListActivity;
import com.business.cryptosoar.Activity.WithdrowFudThreeActivity;
import com.business.cryptosoar.Adapter.FilterMainListAdapter;
import com.business.cryptosoar.Adapter.PaymentOptAdapter;
import com.business.cryptosoar.Adapter.PaymentSettingListAdapter;
import com.business.cryptosoar.Adapter.WithDrowCryproTypeMethodAdaptet;
import com.business.cryptosoar.Adapter.WithdrowCryproProtocoleMethodAdaptet;
import com.business.cryptosoar.Adapter.WithdrowPaymentOptAdapter;
import com.business.cryptosoar.Model.CryproMethod;
import com.business.cryptosoar.Model.CryproProtocolMethod;
import com.business.cryptosoar.Model.PaymentOptModel;
import com.business.cryptosoar.Model.PaymentSetingListModel;
import com.business.cryptosoar.R;
import com.business.cryptosoar.Util.Apis;
import com.business.cryptosoar.Util.InputFilterMinMax;
import com.business.cryptosoar.Util.Method;
import com.business.cryptosoar.databinding.WithdrowFragmentBinding;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class WithdrowFragment extends Fragment {
    WithdrowFragmentBinding binding;
    private WithdrowPaymentOptAdapter paymentOptAdapter;
    private List<PaymentOptModel> paymentOptModels;
    Method method;
    public static TextView btn;
    String withdraw_type;
    String withdraw_amount;
    String method_id;
    String currentString;
    public static Dialog bottomSheetDialogg;
    private PaymentSettingListAdapter paymentSettingListAdapter;
    private List<PaymentSetingListModel> paymentSetingListModels;
    public static String user_contact_person_name, user_contact_number, user_state_city, user_bank_account_number, user_bank_name, user_bank_ifsc;
    String interest_account;

    public WithdrowFragment() {
        // Required empty public constructor
    }

    String tyy = "ty";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.withdrow_fragment, container, false);
        method = new Method(getActivity());
        paymentOptModels = new ArrayList<>();

        btn = binding.requerstNowBtn;
        paymentSetingListModels = new ArrayList<>();
        MainActivity.txt_toolbartitle.setText(getResources().getString(R.string.withdraw_funds_txt));
        MainActivity.RelativeLayout.setVisibility(View.VISIBLE);
        MainActivity.intLayout.setVisibility(View.VISIBLE);
        MainActivity.filter.setVisibility(View.GONE);
        MainActivity.withdrologo.setImageTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorPrimaryDark));
        MainActivity.withdrotxt.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        WalletHistory(method.pref.getString(method.Id, null));
        RemainingBalance(method.pref.getString(method.Id, null));
        binding.accountBalanceTxt.setText("$" + method.pref.getString(method.account_balance, null));
        binding.paymentOptRecy.setNestedScrollingEnabled(true);
        binding.paymentOptRecy.setHasFixedSize(true);
        currentString = WithdrowFudThreeActivity.Responcett.toString();
        currentString = currentString.replace("$", "");
        binding.baseaccountTxt.setText(currentString.toString());
        AddressList(method.pref.getString(method.Id, null));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        binding.paymentOptRecy.setLayoutManager(layoutManager);
        binding.paymentOptRecy.setFocusable(false);
        Bundle bundle = getActivity().getIntent().getExtras();
        GetPaymentOptList(method.pref.getString(method.Id, null));

        binding.cancleTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WithdrawalFundListActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_enter, R.anim.left_out);
            }
        });

        if (!binding.baseaccountTxt.getText().toString().equals("")) {
            binding.interestpaidTxt.setEnabled(false);
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
                    binding.cancleTxt.setEnabled(false);
                    binding.cancleTxt.setClickable(false);
                } else if (binding.interestpaidTxt.getText().toString().equals("")) {
                    binding.cancleTxt.setEnabled(true);
                    binding.cancleTxt.setClickable(true);
                }
            }
        });

        binding.interestpaidTxt.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {

                    String min = method.pref.getString(method.InterestAccount, null);
                    String Baase = binding.interestpaidTxt.getText().toString();
                    String minWith = method.pref.getString(method.withdrawal_minimum_amount, null);

                    if (!Baase.isEmpty()) {
                        double min_val = Double.parseDouble(min);
                        double min_with = Double.parseDouble(minWith);
                        double Baase_val = Double.parseDouble(Baase);
                        if (Baase_val > min_val) {
                            method.alertBox("The maximum withdraw Wallet amount is " + "$" + method.pref.getString(method.InterestAccount, null));
                        }
                        if (Baase_val < min_with) {

                            method.alertBox("The minimum withdraw amount is " + "$" + method.pref.getString(method.withdrawal_minimum_amount, null));
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


                String Baasee = binding.baseaccountTxt.getText().toString();
                String Baase = binding.interestpaidTxt.getText().toString();
                String min = method.pref.getString(method.InterestAccount, null);
                String minWith = method.pref.getString(method.withdrawal_minimum_amount, null);
                String user_id = method.pref.getString(method.Id, null);
                String payment_method_id = WithdrowPaymentOptAdapter.method;
                String crypto_type_id = WithDrowCryproTypeMethodAdaptet.ID;
                String crypto_protocol_id = WithdrowCryproProtocoleMethodAdaptet.IDD;
                String user_address_id = WithDrowCryproTypeMethodAdaptet.ID;
                String bank_account_number = AccountNumber.toString();
                String bank_ifsc_code = IFSC.toString();
                String bank_branch_name = BranchName.toString();
                String contact_person_name = ContactPersonName.toString();
                String contact_number = ContactNumber.toString();
                String state_cit = City.toString();


                if (!binding.baseaccountTxt.getText().toString().equals("")) {

                    withdraw_type = "baseac";
                    withdraw_amount = binding.baseaccountTxt.getText().toString();
                } else if (!binding.interestpaidTxt.getText().toString().equals("")) {
                    withdraw_type = "interest";
                    withdraw_amount = binding.interestpaidTxt.getText().toString();
                } else if (!binding.baseaccountTxt.getText().toString().equals("") || binding.interestpaidTxt.getText().toString().equals("")) {
                    withdraw_type = "baseac";
                    withdraw_amount = binding.baseaccountTxt.getText().toString();
                }

                if (Baase.equals("") && Baasee.equals("")) {
                    method.alertBox("Please Enter Amount");
                }
                else if (!binding.interestpaidTxt.getText().toString().equals("")) {
                    double min_val = Double.parseDouble(min);
                    double min_with = Double.parseDouble(minWith);
                    double Baase_val = Double.parseDouble(Baase);
                    if (Baase_val > min_val) {
                        method.alertBox("Your interest amount is " + "$" + method.pref.getString(method.InterestAccount, null));
                    } else if (Baase_val < min_with) {
                        method.alertBox("The maximum withdraw amount s " + "$" + method.pref.getString(method.withdrawal_minimum_amount, null));
                    } else {
                        if (WithdrowPaymentOptAdapter.method.equals("0") || WithdrowPaymentOptAdapter.method.isEmpty()) {
                            method.alertBox("Please Select Payment method");
                        } else {
                            if (WithdrowPaymentOptAdapter.method.equals("1")) {
                                if (crypto_protocol_id.equals("")) {
                                    method.alertBox("Please Select CryptoTypeId");
                                } else {

                                    if (crypto_protocol_id.equals("")) {
                                        method.alertBox("Please Select CryptoProtocolId & AddressId");
                                    } else {
                                        Log.e("user_id", user_id);
                                        Log.e("withdraw_type", withdraw_type);
                                        Log.e("withdraw_amount", withdraw_amount);
                                        Log.e("payment_method_id", payment_method_id);
                                        Log.e("crypto_type_id", crypto_type_id);
                                        Log.e("crypto_protocol_id", crypto_protocol_id);
                                        Log.e("user_address_id", user_address_id);
                                        Log.e("bank_account_number", bank_account_number);
                                        Log.e("bank_ifsc_code", bank_ifsc_code);
                                        Log.e("bank_branch_name", bank_branch_name);
                                        Log.e("contact_person_name", contact_person_name);
                                        Log.e("contact_number", contact_number);
                                        Log.e("state_cit", state_cit);


                                        AddPaymentSetting(user_id, withdraw_type, withdraw_amount, payment_method_id, crypto_type_id, crypto_protocol_id,
                                                user_address_id, bank_account_number, bank_ifsc_code, bank_branch_name, contact_person_name,
                                                contact_number, state_cit, FUND_ID, Responce, CONTRACT_CHARGE, CONTRACT_INTEREST, RadioButton, WithdrowFudThreeActivity.CustomAmount,WithdrowFudThreeActivity.MONTHS);

                                    }

                                }

                            } else if (WithdrowPaymentOptAdapter.method.equals("3")) {
                                String BankName = WithdrowPaymentOptAdapter.BranchName;
                                String BankNUmber = WithdrowPaymentOptAdapter.AccountNumber;
                                String BankIFSC = WithdrowPaymentOptAdapter.IFSC;

                                if (BankNUmber.isEmpty() || BankNUmber.equalsIgnoreCase("")) {
                                    method.alertBox(getResources().getString(R.string.enter_account_no_txt));
                                } else if (BankIFSC.isEmpty() || BankIFSC.equalsIgnoreCase("")) {
                                    method.alertBox(getResources().getString(R.string.enter_bank_ifsc_txt));
                                } else if (BankName.isEmpty() || BankName.equalsIgnoreCase("")) {
                                    method.alertBox(getResources().getString(R.string.enter_branch_name_txt));
                                } else {
                                    Log.e("user_id", user_id);
                                    Log.e("withdraw_type", withdraw_type);
                                    Log.e("withdraw_amount", withdraw_amount);
                                    Log.e("payment_method_id", payment_method_id);
                                    Log.e("crypto_type_id", crypto_type_id);
                                    Log.e("crypto_protocol_id", crypto_protocol_id);
                                    Log.e("user_address_id", user_address_id);
                                    Log.e("bank_account_number", bank_account_number);
                                    Log.e("bank_ifsc_code", bank_ifsc_code);
                                    Log.e("bank_branch_name", bank_branch_name);
                                    Log.e("contact_person_name", contact_person_name);
                                    Log.e("contact_number", contact_number);

                                    Log.e("state_cit", state_cit);

                                    AddPaymentSetting(user_id, withdraw_type, withdraw_amount, payment_method_id, "0", "0", "0", bank_account_number, bank_ifsc_code, bank_branch_name, contact_person_name, contact_number, state_cit, FUND_ID, Responce, CONTRACT_CHARGE, CONTRACT_INTEREST, RadioButton, WithdrowFudThreeActivity.CustomAmount,WithdrowFudThreeActivity.MONTHS);
                                }
                            } else if (WithdrowPaymentOptAdapter.method.equals("2")) {
                                String Name = WithdrowPaymentOptAdapter.ContactPersonName;
                                String NUmber = WithdrowPaymentOptAdapter.ContactNumber;
                                String State = WithdrowPaymentOptAdapter.City;
                                if (Name.isEmpty() || Name.equalsIgnoreCase("")) {
                                    method.alertBox(getResources().getString(R.string.enter_contact_person_name_txt));
                                } else if (NUmber.isEmpty() || NUmber.equalsIgnoreCase("")) {
                                    method.alertBox(getResources().getString(R.string.enter_contact_number_txt));
                                } else if (State.isEmpty() || State.equalsIgnoreCase("")) {
                                    method.alertBox(getResources().getString(R.string.enter_state_txt));
                                } else {

                                    Log.e("user_id", user_id);
                                    Log.e("withdraw_type", withdraw_type);
                                    Log.e("withdraw_amount", withdraw_amount);
                                    Log.e("payment_method_id", payment_method_id);
                                    Log.e("crypto_type_id", crypto_type_id);
                                    Log.e("crypto_protocol_id", crypto_protocol_id);
                                    Log.e("user_address_id", user_address_id);
                                    Log.e("bank_account_number", bank_account_number);
                                    Log.e("bank_ifsc_code", bank_ifsc_code);
                                    Log.e("bank_branch_name", bank_branch_name);
                                    Log.e("contact_person_name", contact_person_name);
                                    Log.e("contact_number", contact_number);
                                    Log.e("state_cit", state_cit);


                                    AddPaymentSetting(user_id, withdraw_type, withdraw_amount, payment_method_id, "0", "0", "0", bank_account_number, bank_ifsc_code, bank_branch_name, Name, NUmber, State, FUND_ID, Responce, CONTRACT_CHARGE, CONTRACT_INTEREST, RadioButton, WithdrowFudThreeActivity.CustomAmount,WithdrowFudThreeActivity.MONTHS);
                                }
                            }

                        }


                    }
                }
                else {
                    if (WithdrowPaymentOptAdapter.method.equals("0") || WithdrowPaymentOptAdapter.method.isEmpty()) {
                        method.alertBox("Please Select Payment method");
                    } else {
                        if (WithdrowPaymentOptAdapter.method.equals("1")) {
                            if (crypto_type_id.equals("")) {
                                method.alertBox("Please Select CryptoTypeId");
                            } else {

                                if (crypto_protocol_id.equals("")) {
                                    method.alertBox("Please Select CryptoProtocolId & AddressId");
                                } else {
                                    Log.e("user_id", user_id);
                                    Log.e("withdraw_type", withdraw_type);
                                    Log.e("withdraw_amount", withdraw_amount);
                                    Log.e("payment_method_id", payment_method_id);
                                    Log.e("crypto_type_id", crypto_type_id);
                                    Log.e("crypto_protocol_id", crypto_protocol_id);
                                    Log.e("user_address_id", user_address_id);
                                    Log.e("bank_account_number", bank_account_number);
                                    Log.e("bank_ifsc_code", bank_ifsc_code);
                                    Log.e("bank_branch_name", bank_branch_name);
                                    Log.e("contact_person_name", contact_person_name);
                                    Log.e("contact_number", contact_number);
                                    Log.e("state_cit", state_cit);


                                    AddPaymentSetting(user_id, withdraw_type, withdraw_amount, payment_method_id, crypto_type_id, crypto_protocol_id,
                                            user_address_id, bank_account_number, bank_ifsc_code, bank_branch_name, contact_person_name,
                                            contact_number, state_cit, FUND_ID, Responce, CONTRACT_CHARGE, CONTRACT_INTEREST, RadioButton, WithdrowFudThreeActivity.CustomAmount,WithdrowFudThreeActivity.MONTHS);

                                }

                            }

                        }
                        else if (WithdrowPaymentOptAdapter.method.equals("3")) {
                            String BankName = WithdrowPaymentOptAdapter.BranchName;
                            String BankNUmber = WithdrowPaymentOptAdapter.AccountNumber;
                            String BankIFSC = WithdrowPaymentOptAdapter.IFSC;

                            if (BankNUmber.isEmpty() || BankNUmber.equalsIgnoreCase("")) {
                                method.alertBox(getResources().getString(R.string.enter_account_no_txt));
                            } else if (BankIFSC.isEmpty() || BankIFSC.equalsIgnoreCase("")) {
                                method.alertBox(getResources().getString(R.string.enter_bank_ifsc_txt));
                            } else if (BankName.isEmpty() || BankName.equalsIgnoreCase("")) {
                                method.alertBox(getResources().getString(R.string.enter_branch_name_txt));
                            } else {
                                Log.e("user_id", user_id);
                                Log.e("withdraw_type", withdraw_type);
                                Log.e("withdraw_amount", withdraw_amount);
                                Log.e("payment_method_id", payment_method_id);
                                Log.e("crypto_type_id", crypto_type_id);
                                Log.e("crypto_protocol_id", crypto_protocol_id);
                                Log.e("user_address_id", user_address_id);
                                Log.e("bank_account_number", bank_account_number);
                                Log.e("bank_ifsc_code", bank_ifsc_code);
                                Log.e("bank_branch_name", bank_branch_name);
                                Log.e("contact_person_name", contact_person_name);
                                Log.e("contact_number", contact_number);
                                Log.e("state_cit", state_cit);
                                Log.e("CustomAmount", WithdrowFudThreeActivity.CustomAmount);

                                AddPaymentSetting(user_id, withdraw_type,withdraw_amount, payment_method_id, "0",
                                        "0", "0", bank_account_number, bank_ifsc_code,
                                        bank_branch_name, contact_person_name, contact_number, state_cit,
                                        FUND_ID, Responce, CONTRACT_CHARGE, CONTRACT_INTEREST, RadioButton, WithdrowFudThreeActivity.CustomAmount,WithdrowFudThreeActivity.MONTHS);
                            }
                        }
                        else if (WithdrowPaymentOptAdapter.method.equals("2")) {
                            String Name = WithdrowPaymentOptAdapter.ContactPersonName;
                            String NUmber = WithdrowPaymentOptAdapter.ContactNumber;
                            String State = WithdrowPaymentOptAdapter.City;


                            if (Name.isEmpty() || Name.equalsIgnoreCase("")) {
                                method.alertBox(getResources().getString(R.string.enter_contact_person_name_txt));
                            } else if (NUmber.isEmpty() || NUmber.equalsIgnoreCase("")) {
                                method.alertBox(getResources().getString(R.string.enter_contact_number_txt));
                            } else if (State.isEmpty() || State.equalsIgnoreCase("")) {
                                method.alertBox(getResources().getString(R.string.enter_state_txt));
                            } else {

                                Log.e("user_id", user_id);
                                Log.e("withdraw_type", withdraw_type);
                                Log.e("withdraw_amount", withdraw_amount);
                                Log.e("payment_method_id", payment_method_id);
                                Log.e("crypto_type_id", crypto_type_id);
                                Log.e("crypto_protocol_id", crypto_protocol_id);
                                Log.e("user_address_id", user_address_id);
                                Log.e("bank_account_number", bank_account_number);
                                Log.e("bank_ifsc_code", bank_ifsc_code);
                                Log.e("bank_branch_name", bank_branch_name);
                                Log.e("contact_person_name", contact_person_name);
                                Log.e("contact_number", contact_number);
                                Log.e("state_cit", state_cit);

                                AddPaymentSetting(user_id, withdraw_type, currentString, payment_method_id, "0", "0", "0", bank_account_number, bank_ifsc_code, bank_branch_name, Name, NUmber, State, FUND_ID, Responce, CONTRACT_CHARGE, CONTRACT_INTEREST, RadioButton, WithdrowFudThreeActivity.CustomAmount,WithdrowFudThreeActivity.MONTHS);
                            }


                        }

                    }


                }
            }


        });

        return binding.getRoot();
    }


    //Payment Method
    private void GetPaymentOptList(String user_id) {
        paymentOptModels.clear();
        binding.progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("user_id", user_id);

        String url = Apis.ROOT_URL + getString(R.string.payment_opt_list);
        Log.e("Url------>", url);
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (getActivity() != null) {
                    Log.d("Response", new String(responseBody));
                    String res = new String(responseBody);
                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("true")) {
                            binding.progressBar.setVisibility(View.GONE);

                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject object = jsonArray.getJSONObject(i);
                                method_id = object.getString("id");
                                String method_title = object.getString("name");
                                String bank_account_number = object.getString("bank_account_number");
                                String bank_name = object.getString("bank_name");
                                String bank_ifsc = object.getString("bank_ifsc");
                                String contact_person_name = object.getString("contact_person_name");
                                String contact_number = object.getString("contact_number");
                                String state_city = object.getString("state_city");
                                String is_active = object.getString("is_active");
                                String created_date = object.getString("created_date");
                                if (method_id.equals("3")) {
                                    user_bank_account_number = object.getString("user_bank_account_number");
                                    user_bank_name = object.getString("user_bank_name");
                                    user_bank_ifsc = object.getString("user_bank_ifsc");
                                }
                                if (method_id.equals("2")) {
                                    user_contact_person_name = object.getString("user_contact_person_name");
                                    user_contact_number = object.getString("user_contact_number");
                                    user_state_city = object.getString("user_state_city");
                                }


                                JSONArray jsonArray1 = object.optJSONArray("crypto_types");
                                ArrayList data = new ArrayList();

                                if (jsonArray1 != null) {

                                    for (int i1 = 0; i1 < jsonArray1.length(); i1++) {
                                        JSONObject image = jsonArray1.optJSONObject(i1);
                                        CryproMethod variationPojo = new CryproMethod();
                                        variationPojo.setId(image.optString("id"));
                                        variationPojo.setName(image.optString("name"));

                                        JSONArray jsonArray2 = image.optJSONArray("user_addressbooks");
                                        ArrayList dataa = new ArrayList();
                                        if (jsonArray2 != null) {
                                            for (int i2 = 0; i2 < jsonArray2.length(); i2++) {
                                                JSONObject jsonObject1 = jsonArray2.optJSONObject(i2);
                                                CryproProtocolMethod cryproProtocolMethod = new CryproProtocolMethod();
                                                cryproProtocolMethod.setId(jsonObject1.optString("id"));
                                                cryproProtocolMethod.setTypeId(jsonObject1.optString("crypto_type_id"));
                                                cryproProtocolMethod.setProId(jsonObject1.optString("crypto_protocol_id"));
                                                cryproProtocolMethod.setName(jsonObject1.optString("name"));
                                                cryproProtocolMethod.setAddress(jsonObject1.optString("crypto_address"));
                                                cryproProtocolMethod.setLabel(jsonObject1.optString("crypto_label"));
                                                cryproProtocolMethod.setToaddress(jsonObject1.optString("admin_crypto_address"));
                                                cryproProtocolMethod.setAdmin_crypto_note(jsonObject1.optString("admin_crypto_note"));
                                                dataa.add(cryproProtocolMethod);

                                            }

                                        }

                                        Log.e("dklklkll-->", dataa.toString());

                                        variationPojo.setArrayList(dataa);


                                        data.add(variationPojo);


                                    }
                                }

                                paymentOptModels.add(new PaymentOptModel(method_id, method_title, bank_account_number, bank_name, bank_ifsc, contact_person_name, contact_number, state_city, data));
                            }


//
                            binding.progressBar.setVisibility(View.GONE);
                            paymentOptAdapter = new WithdrowPaymentOptAdapter(getActivity(), paymentOptModels);

                            binding.paymentOptRecy.setAdapter(paymentOptAdapter);


                        } else {
                            String Message = jsonObject.getString("message");
                            Toast.makeText(getActivity(), Message.toString(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), getResources().getString(R.string.failed_try_again), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Add Fund Api
    public void AddPaymentSetting(String user_id, String withdraw_type, String withdraw_amount,
                                  String payment_method_id, String crypto_type_id,
                                  String crypto_protocol_id, String user_address_id,
                                  String bank_account_number, String bank_ifsc_code,
                                  String bank_branch_name, String contact_person_name,
                                  String contact_number, String state_city, String fund_id, String json,
                                  String end_contract_charge, String end_contract_interest,
                                  String end_contract_type, String custom_amount,String month_value

    ) {
        binding.progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("user_id", user_id);
        params.add("withdraw_type", withdraw_type);
        params.add("withdraw_amount", withdraw_amount);
        params.add("payment_method_id", payment_method_id);
        params.add("crypto_type_id", crypto_type_id);
        params.add("crypto_protocol_id", crypto_protocol_id);
        params.add("user_address_id", user_address_id);
        params.add("bank_account_number", bank_account_number);
        params.add("bank_ifsc_code", bank_ifsc_code);
        params.add("bank_branch_name", bank_branch_name);
        params.add("contact_person_name", contact_person_name);
        params.add("contact_number", contact_number);
        params.add("state_city", state_city);
        params.add("fund_id", fund_id);
        params.add("row_data_contract", json);
        params.add("end_contract_charge", end_contract_charge);
        params.add("end_contract_interest", end_contract_interest);
        params.add("end_contract_type", end_contract_type);
        params.add("custom_amount", custom_amount);
        params.add("month_value", month_value);


        String url = Apis.ROOT_URL + getString(R.string.request_withdrow_api);
        Log.e("Url------->", url);
        Log.e("withdraw_amount------->", withdraw_amount.toString());
        Log.e("nekdjwe------->", binding.accountBalanceTxt.getText().toString());
        Log.e("nekdjwefdfsd------->", currentString.toString());
        Log.e("paramsss-------", params.toString());
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
                        WithdrowFudThreeActivity.Responcett = "";
                        WithdrowFudThreeActivity.CustomAmount="";
                        WithdrowFudThreeActivity.Responcett="";
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String id = jsonObject1.getString("id");
                            String withdraw_amount = jsonObject1.getString("withdraw_amount");
                            String withdraw_type = jsonObject1.getString("withdraw_type");
                            String payment_method_id = jsonObject1.getString("payment_method_id");
                            String crypto_type_id = jsonObject1.getString("crypto_type_id");
                            String crypto_protocol_id = jsonObject1.getString("crypto_protocol_id");
                            String crypto_user_address_id = jsonObject1.getString("crypto_user_address_id");
                            String bank_account_number = jsonObject1.getString("bank_account_number");
                            String bank_ifsc_code = jsonObject1.getString("bank_ifsc_code");
                            String bank_branch_name = jsonObject1.getString("bank_branch_name");
                            String contact_person_name = jsonObject1.getString("contact_person_name");
                            String contact_number = jsonObject1.getString("contact_number");
                            String state_city = jsonObject1.getString("state_city");
                            String created_date = jsonObject1.getString("created_date");
                            String updated_date = jsonObject1.getString("updated_date");


                            bottomSheetDialogg = new Dialog(getActivity()) {
                                @Override
                                public boolean onTouchEvent(MotionEvent event) {
                                    // Tap anywhere to close dialog.
                                    bottomSheetDialogg.dismiss();
                                    return true;
                                }
                            };
                            bottomSheetDialogg.setCancelable(false);
                            bottomSheetDialogg.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            bottomSheetDialogg.setContentView(R.layout.dialog_withdraw_success);

                            TextView send_btn = bottomSheetDialogg.findViewById(R.id.send_btn);
                            send_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    WithdrowFudThreeActivity.CustomAmount="";
                                    WithdrowFudThreeActivity.Responcett="";
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    startActivity(intent);
                                    getActivity().overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                                    getActivity().finish();
                                }
                            });


                            bottomSheetDialogg.setCancelable(false);
                            bottomSheetDialogg.show();
                            bottomSheetDialogg.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            bottomSheetDialogg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            bottomSheetDialogg.getWindow().getAttributes().windowAnimations = R.style.Dialoganimation;
                            bottomSheetDialogg.getWindow().setGravity(Gravity.CENTER);


                        }

                    } else {
                        binding.progressBar.setVisibility(View.GONE);
                        String message = jsonObject.getString("message");
                        WithdrowFudThreeActivity.CustomAmount="";
                        WithdrowFudThreeActivity.Responcett="";
                        Toast.makeText(getActivity(), message.toString(), Toast.LENGTH_SHORT).show();
                    }
                    binding.progressBar.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    binding.progressBar.setVisibility(View.GONE);
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                    WithdrowFudThreeActivity.CustomAmount="";
                    WithdrowFudThreeActivity.Responcett="";
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                binding.progressBar.setVisibility(View.GONE);
                method.alertBox(getResources().getString(R.string.something_went_wrong));
                WithdrowFudThreeActivity.CustomAmount="";
                WithdrowFudThreeActivity.Responcett="";
            }
        });
    }

    // remaining Balance

    private void RemainingBalance(String UserId) {
        binding.progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("user_id", UserId);

        String url = Apis.ROOT_URL + getString(R.string.remaining_api);
        Log.e("Parms", params.toString());
        Log.e("Url---->", url.toString());
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (getActivity() != null) {
                    Log.d("Response", new String(responseBody));
                    String res = new String(responseBody);
                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("true")) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            String base_account = jsonObject1.getString("base_account");
                            interest_account = jsonObject1.getString("interest_account");
                            method.editor.putString(method.InterestAccount, interest_account);
                            method.editor.commit();
                            binding.baseaccountExa.setText("Remaining Balance: " + "$" + base_account);
                            binding.interestpaidExa.setText("Remaining Balance: " + "$" + interest_account);
                        } else {
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

    public void WalletHistory(String UserId) {


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
                        MainActivity.account_balance_txt.setText("$" + account_balance.toString());
                        MainActivity.toalreferal_txt.setText("$" + total_interest_paid.toString());
                        MainActivity.balance.setText("Account Balance:" + " $" + account_balance, null);
                        method.editor.putString(method.account_balance, account_balance);
                        method.editor.putString(method.total_interest_paid, total_interest_paid);
                        method.editor.putString(method.accrued_interest, accrued_interest);
                        method.editor.putBoolean(method.pref_login, true);
                        method.editor.commit();


                    } else {

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

    public void AddressList(String UserId) {
        paymentSetingListModels.clear();
        binding.progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("user_id", UserId);
        String url = Apis.ROOT_URL + getString(R.string.payment_setting_list_api);
        Log.e("Url------->", url);
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);
                Log.e("Url------->", res.toString());
                // Toast.makeText(getApplicationContext(),  new String(responseBody), Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String id = jsonObject1.getString("id");
                            String user_id = jsonObject1.getString("user_id");
                            String crypto_type_id = jsonObject1.getString("crypto_type_id");
                            String crypto_protocol_id = jsonObject1.getString("crypto_protocol_id");
                            String crypto_address = jsonObject1.getString("crypto_address");
                            String crypto_label = jsonObject1.getString("crypto_label");
                            String created_date = jsonObject1.getString("created_date");
                            paymentSetingListModels.add(new PaymentSetingListModel(id, crypto_label, crypto_address, crypto_label));
                        }

                        if (paymentSetingListModels.size() == 0) {
                            binding.progressBar.setVisibility(View.GONE);
                            String message = jsonObject.getString("message");


                        } else {

                            binding.progressBar.setVisibility(View.GONE);
                            paymentSettingListAdapter = new PaymentSettingListAdapter(getActivity(), paymentSetingListModels);

                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);


                        }
                        Log.e("kdnfkdsfjsdk", String.valueOf(paymentSetingListModels.size()));


                    } else {
                        binding.progressBar.setVisibility(View.GONE);
                        String message = jsonObject.getString("message");
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

    public void AlertDialogFilter() {


    }

}
