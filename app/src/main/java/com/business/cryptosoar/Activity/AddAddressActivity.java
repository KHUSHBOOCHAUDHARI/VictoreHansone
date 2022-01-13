package com.business.cryptosoar.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.business.cryptosoar.Adapter.CoinListAdapter;
import com.business.cryptosoar.Adapter.NetworkListAdapter;
import com.business.cryptosoar.Model.CoinListModel;
import com.business.cryptosoar.Model.NetworkListModel;
import com.business.cryptosoar.R;
import com.business.cryptosoar.Util.Apis;
import com.business.cryptosoar.Util.Method;

import com.business.cryptosoar.databinding.ActivityAddAddressBinding;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class AddAddressActivity extends AppCompatActivity {
    private ActivityAddAddressBinding binding;
    Method method;
    public static Dialog bottomSheetDialog;
    private CoinListAdapter coinListAdapter;
    private List<CoinListModel> coinListModels;
    public static RelativeLayout coin_lytt;
    private NetworkListAdapter networkListAdapter;
    private List<NetworkListModel> networkListModels;
    public static TextView coin_txt;
    public  static ImageView interes_logo,down;
    public static TextView name;
    String payment_id,crypto_types_id;
    public static String Network="";

    ImageView menu;
   TextView save_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      binding=DataBindingUtil.setContentView(AddAddressActivity.this,R.layout.activity_add_address);
        method=new Method(AddAddressActivity.this);
         coinListModels=new ArrayList<>();
        networkListModels=new ArrayList<>();
        name=findViewById(R.id.coin_txt);
        coin_lytt=findViewById(R.id.coin_lytt);
        coin_txt=findViewById(R.id.coin_txt);
        interes_logo=findViewById(R.id.interes_logo);
        down=findViewById(R.id.down);
        save_btn=findViewById(R.id.save_btn);
        NetworkList(method.pref.getString(method.Id,null));

        menu=findViewById(R.id.menu);
       menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.right_enter,R.anim.left_out);
            }
        });
        fullscreen();
        coin();

        Log.e("","************************"+coinListModels.size());
        if(coinListModels.size()==0)
        {
           coin_lytt.setEnabled(false);
            coin_txt.setEnabled(false);
            down.setVisibility(View.GONE);
        }

        else
        {
           coin_lytt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    down.setVisibility(View.VISIBLE);

                    bottomSheetDialog = new Dialog(AddAddressActivity.this) {
                        @Override
                        public boolean onTouchEvent(MotionEvent event) {
                            // Tap anywhere to close dialog.
                            this.dismiss();
                            return true;
                        }
                    };

                    bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    bottomSheetDialog.setContentView(R.layout.fragment_bottomsheet_coin);
                    RecyclerView recyclerView=(RecyclerView) bottomSheetDialog.findViewById(R.id.coin_recy);
                    coinListModels.clear();
                    binding.progressBar.setVisibility(View.VISIBLE);
                    AsyncHttpClient client = new AsyncHttpClient();
                    RequestParams params = new RequestParams();
                    params.add("user_id", method.pref.getString(method.Id,null));
                    String url = Apis.ROOT_URL + getString(R.string.coin_list);
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
                                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                                    for (int i=0;i<jsonArray.length();i++)
                                    {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        String id = jsonObject1.getString("id");
                                        payment_id = jsonObject1.getString("payment_id");
                                        String payment_name = jsonObject1.getString("name");
                                        String short_name = jsonObject1.getString("short_name");
                                        String is_active = jsonObject1.getString("is_active");
                                        String created_date = jsonObject1.getString("created_date");


                                        coinListModels.add(new CoinListModel(payment_id, payment_name));

                                    }

                                    binding.progressBar.setVisibility(View.GONE);
                                    coinListAdapter = new CoinListAdapter(AddAddressActivity.this,coinListModels);
                                    recyclerView.setAdapter(coinListAdapter);
                                    recyclerView.setNestedScrollingEnabled(false);
                                    recyclerView.setHasFixedSize(true);

                                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
                                    recyclerView.setLayoutManager(layoutManager);

                                    recyclerView.setFocusable(false);

                                }

                                else {
                                    binding.progressBar.setVisibility(View.GONE);
                                    String message = jsonObject.getString("message");
                                    Toast.makeText(AddAddressActivity.this, message.toString(), Toast.LENGTH_SHORT).show();
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

                    bottomSheetDialog.show();
                    bottomSheetDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    bottomSheetDialog.getWindow().getAttributes().windowAnimations=R.style.Dialoganimation;
                    bottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);



                }
            });
        }

        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Address=binding.addressTxt.getText().toString();
                String lable=binding.wallateTxt.getText().toString();
                if (Address.equals("") || Address.isEmpty())
                {
                    method.alertBox("Enter your address");

                }

                else if (Network.equals(""))
                {
                    method.alertBox("Please select network");
                }


                else if (lable.equals("") || lable.isEmpty())
                {


                    method.alertBox("Enter your wallet label");
                }

                else
                {
                   // Toast.makeText(getApplicationContext(), Network.toString(), Toast.LENGTH_SHORT).show();

                    AddPaymentSetting(method.pref.getString(method.Id,null),payment_id,crypto_types_id,binding.addressTxt.getText().toString(),binding.wallateTxt.getText().toString());

                }


            }
        });





    }
    //NetworkList Api
    public void NetworkList(String UserId) {
        coinListModels.clear();
        binding.progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("user_id", UserId);
        String url = Apis.ROOT_URL + getString(R.string.network_list);
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
                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                        for (int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String id = jsonObject1.getString("id");
                             crypto_types_id = jsonObject1.getString("crypto_types_id");
                            String payment_name = jsonObject1.getString("name");
                            String short_name = jsonObject1.getString("crypto_address");
                            String is_active = jsonObject1.getString("is_active");
                            String created_date = jsonObject1.getString("created_date");

                            networkListModels.add(new NetworkListModel(payment_id, payment_name));

                        }

                        binding.progressBar.setVisibility(View.GONE);
                        networkListAdapter = new NetworkListAdapter(AddAddressActivity.this,networkListModels);
                        binding.networkRecy.setAdapter(networkListAdapter);
                        binding.networkRecy.setNestedScrollingEnabled(false);
                        binding.networkRecy.setHasFixedSize(true);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
                        binding.networkRecy.setLayoutManager(layoutManager);
                        binding.networkRecy.setFocusable(false);

                    }

                    else {
                        binding.progressBar.setVisibility(View.GONE);
                        String message = jsonObject.getString("message");
                        Toast.makeText(AddAddressActivity.this, message.toString(), Toast.LENGTH_SHORT).show();
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





    //Login Api
    public void AddPaymentSetting(String UserId,String crypto_types_id, String crypto_protocol_id,
                                  String crypto_address,String crypto_label) {

        binding.progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("user_id", UserId);
        params.add("crypto_types_id", crypto_types_id);
        params.add("crypto_protocol_id", crypto_protocol_id);
        params.add("crypto_address", crypto_address);
        params.add("crypto_label", crypto_label);
        String url = Apis.ROOT_URL + getString(R.string.payment_setting_api);
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
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);

                            String id = jsonObject1.getString("id");
                            String user_id = jsonObject1.getString("user_id");
                            String crypto_type_id = jsonObject1.getString("crypto_type_id");
                            String crypto_protocol_id = jsonObject1.getString("crypto_protocol_id");
                            String crypto_address = jsonObject1.getString("crypto_address");
                            String crypto_label = jsonObject1.getString("crypto_label");
                            method.editor.putString(method.Address_Id, id);
                            method.editor.putString(method.Id, user_id);
                            method.editor.putBoolean(method.pref_login, true);
                            method.editor.commit();
                            String message = jsonObject.getString("message");
                            Toast.makeText(AddAddressActivity.this, message.toString(), Toast.LENGTH_SHORT).show();
                            onBackPressed();
//
//                            Intent intent=new Intent(AddAddressActivity.this, AddressListActivity.class);
//                            startActivity(intent);
                            overridePendingTransition(R.anim.right_enter,R.anim.left_out);
                            finish();

                        }


                    }

                    else {
                        binding.progressBar.setVisibility(View.GONE);
                        String message = jsonObject.getString("message");
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
    public void coin() {
        coinListModels.clear();
        binding.progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("user_id", method.pref.getString(method.Id,null));
        String url = Apis.ROOT_URL + getString(R.string.coin_list);
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
                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                        for (int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String id = jsonObject1.getString("id");
                            payment_id = jsonObject1.getString("payment_id");
                            String payment_name = jsonObject1.getString("name");
                            String short_name = jsonObject1.getString("short_name");
                            String is_active = jsonObject1.getString("is_active");
                            String created_date = jsonObject1.getString("created_date");
                            name.setText(payment_name);

                            coinListModels.add(new CoinListModel(payment_id, payment_name));

                        }

                        binding.progressBar.setVisibility(View.GONE);
                        coinListAdapter = new CoinListAdapter(AddAddressActivity.this,coinListModels);


                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);

                    }

                    else {
                        binding.progressBar.setVisibility(View.GONE);
                        String message = jsonObject.getString("message");
                        Toast.makeText(AddAddressActivity.this, message.toString(), Toast.LENGTH_SHORT).show();
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