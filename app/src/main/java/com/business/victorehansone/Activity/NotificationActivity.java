package com.business.victorehansone.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.business.victorehansone.Adapter.NotificationListAdapter;
import com.business.victorehansone.Model.NotificationModel;
import com.business.victorehansone.R;
import com.business.victorehansone.Util.Apis;
import com.business.victorehansone.Util.Method;
import com.business.victorehansone.databinding.ActivityNotificationBinding;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class NotificationActivity extends AppCompatActivity {
    public static ProgressBar progressBar;
    Method method;
    private ActivityNotificationBinding binding;
    private NotificationListAdapter paymentSettingListAdapter;
    private List<NotificationModel> paymentSetingListModels;
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
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(NotificationActivity.this, R.layout.activity_notification);
        paymentSetingListModels = new ArrayList<>();
        method = new Method(NotificationActivity.this);
        AddressList(method.pref.getString(method.Id, null));
        progressBar = binding.progressBar;

        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.right_enter, R.anim.left_out);
            }
        });
        // NetworkList();
        fullscreen();

    }
    //NetworkList Api
    public void AddressList(String ic_userId) {
        paymentSetingListModels.clear();
        binding.progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("ic_user_id", ic_userId);
        String url = Apis.ROOT_URL + getString(R.string.notification_list_api);
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
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String id = jsonObject1.getString("id");
                            String ic_user_id = jsonObject1.getString("ic_user_id");
                            String notification_text = jsonObject1.getString("notification_text");
                            String title = jsonObject1.getString("title");
                            String notification_type = jsonObject1.getString("notification_type");
                            String notification_img = jsonObject1.getString("notification_img");
                            String notification_date = jsonObject1.getString("notification_date");
                            paymentSetingListModels.add(new NotificationModel(id, title, notification_date, notification_text));
                        }

                        if (paymentSetingListModels.size() == 0) {
                            binding.progressBar.setVisibility(View.GONE);
                            String message = jsonObject.getString("message");
                            binding.nodata.setVisibility(View.VISIBLE);

                        } else {

                            binding.progressBar.setVisibility(View.GONE);
                            paymentSettingListAdapter = new NotificationListAdapter(paymentSetingListModels, NotificationActivity.this);
                            binding.list.setAdapter(paymentSettingListAdapter);
                            binding.list.setNestedScrollingEnabled(false);
                            binding.list.setHasFixedSize(true);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                            binding.list.setLayoutManager(layoutManager);
                            binding.list.setFocusable(false);
                            binding.nodata.setVisibility(View.GONE);

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

}