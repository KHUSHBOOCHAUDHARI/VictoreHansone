package com.business.cryptosoar.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.InetAddresses;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.business.cryptosoar.Adapter.CryproProtocoleMethodAdaptet;
import com.business.cryptosoar.Adapter.CryproTypeMethodAdaptet;
import com.business.cryptosoar.Adapter.PaymentOptAdapter;
import com.business.cryptosoar.Adapter.PaymentSettingListAdapter;
import com.business.cryptosoar.Model.CryproMethod;
import com.business.cryptosoar.Model.CryproProtocolMethod;
import com.business.cryptosoar.Model.PaymentOptModel;
import com.business.cryptosoar.Model.PaymentSetingListModel;
import com.business.cryptosoar.R;
import com.business.cryptosoar.Util.Apis;
import com.business.cryptosoar.Util.FilePath;
import com.business.cryptosoar.Util.Method;
import com.business.cryptosoar.Util.SingleUploadBroadcsatReceiver;
import com.business.cryptosoar.databinding.ActivityConformationBinding;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import net.gotev.uploadservice.MultipartUploadRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cz.msebera.android.httpclient.Header;

public class ConformationActivity extends AppCompatActivity  implements SingleUploadBroadcsatReceiver.Delegate{


    private ActivityConformationBinding binding;
    private PaymentOptAdapter paymentOptAdapter;
    private List<PaymentOptModel> paymentOptModels;
    Method method;
    String plan_type,fund_amount;
    private boolean is_profile;
    private int PICK_PDF_REQUEST = 1;
    private Uri filePath;
    String method_id;
    private PaymentSettingListAdapter paymentSettingListAdapter;
    private List<PaymentSetingListModel> paymentSetingListModels;
    private String image_profile;
    private static final int STORAGE_PERMISSION_CODE = 123;
    public static TextView to_txt,notess_txt;
    private final SingleUploadBroadcsatReceiver uploadReceiver = new SingleUploadBroadcsatReceiver();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(ConformationActivity.this,R.layout.activity_conformation);

        method=new Method(ConformationActivity.this);
        paymentOptModels=new ArrayList<>();
        paymentSetingListModels= new ArrayList<>();
        AddressList(method.pref.getString(method.Id,null));
        GetPaymentOptList(method.pref.getString(method.Id,null));
        plan_type=getIntent().getStringExtra("type");
        fund_amount=getIntent().getStringExtra("amount");
        binding.billamountTxt.setText("$"+fund_amount);
        binding.requiredTxt.setText("$"+method.pref.getString(method.addfund_minimum_diposit,null));
        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.right_enter,R.anim.left_out);
                finish();
            }
        });
        binding.notificationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ConformationActivity.this,NotificationActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_enter,R.anim.left_out);
            }
        });

        if (plan_type.equals("reinvestment"))
        {
            binding.radiotxt.setText("Re-investment Plan");
        }
        else
        {
            binding.radiotxt.setText("Monthly Payment");
        }
        binding.accountBalanceTxt.setText("$"+method.pref.getString(method.account_balance,null));
        is_profile=false;
        requestStoragePermission();
      //  Toast.makeText(ConformationActivity.this, "PlaneType= "+plan_type + "FundAmount= "+fund_amount, Toast.LENGTH_SHORT).show();
        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              onBackPressed();
              overridePendingTransition(R.anim.right_enter,R.anim.left_out);
            }
        });
        to_txt=(TextView) findViewById(R.id.to_txt);
        notess_txt=(TextView) findViewById(R.id.notess_txt);
        binding.paymentOptRecy.setNestedScrollingEnabled(true);
        binding.paymentOptRecy.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ConformationActivity.this,LinearLayoutManager.VERTICAL,false);
        binding.paymentOptRecy.setLayoutManager(layoutManager);
        binding.paymentOptRecy.setFocusable(false);


        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onBackPressed();
               overridePendingTransition(R.anim.right_enter,R.anim.left_out);
            }
        });
        fullscreen();


        binding.requerstNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String UserId = method.pref.getString(method.Id,null);
                String PlaneType = plan_type.toString();
                String FundAmount = fund_amount.toString();
                String payment_method_id = PaymentOptAdapter.method;
                String crypto_type_id = CryproTypeMethodAdaptet.ID;
                String crypto_protocol_id = CryproProtocoleMethodAdaptet.IDD;
                String BankAccountNumber = "";
                String BankIfscCode = "";
                String BankBranchName = "";


                if (payment_method_id.equals("0") || payment_method_id.isEmpty()) {

                    method.alertBox("Please Select Payment method");
                } else {

                    if (PaymentOptAdapter.method.equals("1")) {
                        if (crypto_protocol_id.equals("")) {
                            method.alertBox("Please Select CryptoTypeId");
                        } else {

                            if (crypto_protocol_id.equals("")) {
                                method.alertBox("Please Select CryptoProtocolId & AddressId");
                            }
                            else {

                                Log.e("UserId", UserId);
                                Log.e("PlaneType", plan_type);
                                Log.e("FundAmount", fund_amount);
                                Log.e("PaymentId", payment_method_id);
                                Log.e("CryptoTypeId", crypto_type_id);
                                Log.e("CryptoProtocolId", crypto_protocol_id);

                                Intent intent = new Intent(ConformationActivity.this, PDFUploade.class);
                                intent.putExtra("planeType", PlaneType.toString());
                                intent.putExtra("FundAmount", FundAmount.toString());
                                intent.putExtra("PaymentId", payment_method_id);
                                intent.putExtra("CryptoTypeId", crypto_type_id);
                                intent.putExtra("CryptoProtocolId", crypto_protocol_id);
                                intent.putExtra("BankAccountNumber", BankAccountNumber);
                                intent.putExtra("BankIfscCode", BankIfscCode);
                                intent.putExtra("BankBranchName", BankBranchName);
                                startActivity(intent);
                                overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                            }

                        }

                    } else if (PaymentOptAdapter.method.equals("2")) {

                        Intent intent = new Intent(ConformationActivity.this, PDFUploade.class);
                        intent.putExtra("planeType", PlaneType.toString());
                        intent.putExtra("FundAmount", FundAmount.toString());
                        intent.putExtra("PaymentId", payment_method_id);
                        intent.putExtra("CryptoTypeId", "0");
                        intent.putExtra("CryptoProtocolId", "0");
                        intent.putExtra("BankAccountNumber", BankAccountNumber);
                        intent.putExtra("BankIfscCode", BankIfscCode);
                        intent.putExtra("BankBranchName", BankBranchName);

                        startActivity(intent);
                        overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                    } else if (PaymentOptAdapter.method.equals("3")) {

                        Intent intent = new Intent(ConformationActivity.this, PDFUploade.class);
                        intent.putExtra("planeType", PlaneType.toString());
                        intent.putExtra("FundAmount", FundAmount.toString());
                        intent.putExtra("PaymentId", payment_method_id);
                        intent.putExtra("CryptoTypeId", "0");
                        intent.putExtra("CryptoProtocolId", "0");
                        intent.putExtra("BankAccountNumber", BankAccountNumber);
                        intent.putExtra("BankIfscCode", PaymentOptAdapter.ViewHolder.ifsc_txt.getText().toString());
                        intent.putExtra("BankBranchName", BankBranchName);

                        startActivity(intent);
                        overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                    }
                }
            }














        });


    }

    @Override
    protected void onRestart() {
        GetPaymentOptList(method.pref.getString(method.Id,null));
        super.onRestart();
    }
    //Payment Method
    private void GetPaymentOptList(String user_id) {
        paymentOptModels.clear();
        binding.progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("user_id", user_id);

        String url = Apis.ROOT_URL+getString(R.string.payment_opt_list);
        Log.e("Url------>", url);
        //   Toast.makeText(getApplicationContext(),url, Toast.LENGTH_SHORT).show();

        client.post(url,params,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (getApplicationContext() != null) {
                    Log.d("Response", new String(responseBody));
//                    Toast.makeText(getActivity(),new String(responseBody), Toast.LENGTH_SHORT).show();
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

                                JSONArray jsonArray1 = object.optJSONArray("crypto_types");
                                ArrayList data = new ArrayList();

                                if (jsonArray1!=null) {

                                    for (int i1 = 0; i1 < jsonArray1.length(); i1++) {
                                        JSONObject image = jsonArray1.optJSONObject(i1);
                                        CryproMethod variationPojo = new CryproMethod();
                                        variationPojo.setId(image.optString("id"));
                                        variationPojo.setName(image.optString("name"));

                                        JSONArray jsonArray2 = image.optJSONArray("user_addressbooks");
                                        ArrayList dataa = new ArrayList();
                                        if (jsonArray2!=null)
                                        {
                                            for (int i2=0;i2<jsonArray2.length();i2++)
                                            {
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

                                        Log.e("dklklkll-->",dataa.toString());

                                        variationPojo.setArrayList(dataa);


                                        data.add(variationPojo);




                                    }
                                }
                                paymentOptModels.add(new PaymentOptModel(method_id, method_title,bank_account_number,bank_name,bank_ifsc,contact_person_name,contact_number,state_city,data));
                            }


//                            JSONArray jsonArray = jsonObject.getJSONArray("data");
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject object = jsonArray.getJSONObject(i);
//                                String id = object.getString("id");
//                                String name = object.getString("name");
//                                JSONArray jsonArray1=object.getJSONArray("crypto_types");
//                                ArrayList data = new ArrayList();
//                                for (int ii = 0; ii < jsonArray1.length(); ii++) {
//                                    JSONObject jsonObject1 = jsonArray1.getJSONObject(ii);
//                                    CryproMethod cryproMethod=new CryproMethod();
//                                    cryproMethod.setId(jsonObject1.getString("id"));
//                                    cryproMethod.setName(jsonObject1.getString("name"));
//                                    data.add(cryproMethod);
//
//                                }
//
//                                paymentOptModels.add(new PaymentOptModel(id,name,data));
//
//
//                            }
//
                            binding.progressBar.setVisibility(View.GONE);
                            paymentOptAdapter = new PaymentOptAdapter(ConformationActivity.this,paymentOptModels);

                            binding.paymentOptRecy.setAdapter(paymentOptAdapter);


                        }
                        else
                        {
                            String Message=jsonObject.getString("message");
                            Toast.makeText(ConformationActivity.this, Message.toString(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(ConformationActivity.this,getResources().getString(R.string.failed_try_again), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(ConformationActivity.this,getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                is_profile = true;
                filePath = data.getData();

                Log.e("PDF---", "***************" + filePath);
            }
        }
    }

    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void uploadMultipart(String UserId,String plan_type, String fund_amount,
                                String payment_method_id,String crypto_type_id,
                                String crypto_protocol_id,String bank_account_number,
                                String bank_ifsc_code,String bank_branch_name
                               ) {
        String path = FilePath.getRealPath(this, filePath);
        Log.e("olohkgof",path.toString());
        if (path == null) {
            Toast.makeText(this, "Please move your .pdf file to internal storage and retry", Toast.LENGTH_LONG).show();
        } else {
//            Log.e("","***************"+path.toString());
            //Uploading code
            try {

                String url = Apis.ROOT_URL + getString(R.string.add_userfund_api);

                String uploadId = UUID.randomUUID().toString();
                uploadReceiver.setDelegate((SingleUploadBroadcsatReceiver.Delegate) this);
                uploadReceiver.setUploadID(uploadId);
                Toast.makeText(this, "Uploading file. Please wait...", Toast.LENGTH_SHORT).show();
                //Creating a multi part request
                new MultipartUploadRequest(this,url)
                        .addFileToUpload(path, "document_file") //Adding file
                        .addParameter("id", UserId) //Adding text parameter to the request
                        .addParameter("plan_type", plan_type)
                        .addParameter("fund_amount", fund_amount)
                        .addParameter("payment_method_id", payment_method_id)
                        .addParameter("crypto_type_id", crypto_type_id)
                        .addParameter("crypto_protocol_id", crypto_protocol_id)
                        .addParameter("bank_account_number", bank_account_number)
                        .addParameter("bank_ifsc_code", bank_ifsc_code)
                        .addParameter("bank_branch_name", bank_branch_name)

//                        .addHeader("API-TOKEN",getString(R.string.API_TOKEN))
                        .setMaxRetries(2)
                        .startUpload(); //Starting the upload


                Log.e("","***************"+path);

            } catch (Exception exc) {
                Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        uploadReceiver.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        uploadReceiver.unregister(this);
    }


    @Override
    public void onProgress(int progress) {

    }

    @Override
    public void onProgress(long uploadedBytes, long totalBytes) {
        Toast.makeText(this, "pdf uploaded is on progess  till wait ..", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onError(Exception exception) {
        Toast.makeText(this, "Error Occured Try again", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onCompleted(int serverResponseCode, byte[] serverResponseBody) {
        Toast.makeText(this, "pdf uploaded successfully", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onCancelled() {
        Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
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
    public void AddressList(String UserId) {
        paymentSetingListModels.clear();
        binding.progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("user_id", UserId);
        String url = Apis.ROOT_URL + getString(R.string.payment_setting_list_api);
        Log.e("Url------->",url);
        client.post(url,params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);
                Log.e("Url------->",res.toString());
                // Toast.makeText(getApplicationContext(),  new String(responseBody), Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                        for (int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String id = jsonObject1.getString("id");
                            String user_id = jsonObject1.getString("user_id");
                            String crypto_type_id = jsonObject1.getString("crypto_type_id");
                            String crypto_protocol_id = jsonObject1.getString("crypto_protocol_id");
                            String crypto_address = jsonObject1.getString("crypto_address");
                            String crypto_label = jsonObject1.getString("crypto_label");
                            String created_date = jsonObject1.getString("created_date");
                            paymentSetingListModels.add(new PaymentSetingListModel(id, crypto_label,crypto_address,crypto_label));
                        }

                        if (paymentSetingListModels.size() == 0) {
                            binding.progressBar.setVisibility(View.GONE);
                            String message = jsonObject.getString("message");


                        }
                        else
                        {

                            binding.progressBar.setVisibility(View.GONE);
                            paymentSettingListAdapter = new PaymentSettingListAdapter(ConformationActivity.this, paymentSetingListModels);

                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);


                        }
                        Log.e("kdnfkdsfjsdk", String.valueOf(paymentSetingListModels.size()));


                    }

                    else {
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


}