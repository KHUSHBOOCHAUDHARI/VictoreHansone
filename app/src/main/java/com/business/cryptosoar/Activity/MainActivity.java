package com.business.cryptosoar.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.business.cryptosoar.Adapter.TransactionHistoryAdapter;
import com.business.cryptosoar.Fragment.AddFundFragment;
import com.business.cryptosoar.Fragment.HistoryFragment;
import com.business.cryptosoar.Fragment.ProfileFragment;
import com.business.cryptosoar.Fragment.WalletFragment;
import com.business.cryptosoar.Fragment.WithdrowFragment;
import com.business.cryptosoar.Model.TransactionModel;
import com.business.cryptosoar.R;
import com.business.cryptosoar.Util.Apis;
import com.business.cryptosoar.Util.Method;
import com.business.cryptosoar.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity   implements NavigationView.OnNavigationItemSelectedListener{
    private ActivityMainBinding binding;
    Method method;
    public static FrameLayout container;
    public static DrawerLayout drawer;
    public static NavigationView navigationView;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    public static Toolbar toolbar;
    public static TextView name, balance, textView_Phone_nav, textView_Walletbalance_nav;
    private CircleImageView user_image;
    public static TextView txt_toolbartitle;
    public static RelativeLayout RelativeLayout;
    public static ImageView menu;
    public static TextView account_balance_txt,toalreferal_txt;
    public static LinearLayout intLayout;
    public static ImageView addFubdLogo,historylogo,walletlogo,withdrologo,profilelogo;
    public static TextView addFundTxt,historytxt,wallettxt,withdrotxt,profiletxt;
    private ImageView imageView_edit_profile;
    AlertDialog.Builder builder;
    String Type="";
    String account_balance;
    public static ImageView filter;
    boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        method=new Method(MainActivity.this);
        builder = new AlertDialog.Builder(MainActivity.this);
        account_balance_txt=findViewById(R.id.account_balance_txt);
        toalreferal_txt=findViewById(R.id.toalreferal_txt);
        container=binding.container;




        toolbar=findViewById(R.id.toolbar_main);
        txt_toolbartitle = findViewById(R.id.appname);
        filter=findViewById(R.id.filter);
        menu = findViewById(R.id.menu);
        txt_toolbartitle.setText(getResources().getString(R.string.app_name));
        txt_toolbartitle.setVisibility(View.VISIBLE);
        binding.notificationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,NotificationActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_enter,R.anim.left_out);
            }
        });
        WalletHistory(method.pref.getString(method.Id,null));
        int[] color = {getResources().getColor(R.color.colorPrimary),getResources().getColor(R.color.colorPrimaryDark)};
        float[] position = {0, 1};
        Shader.TileMode tile_mode = Shader.TileMode.MIRROR; // or TileMode.REPEAT;
        LinearGradient lin_grad = new LinearGradient(0, 0, 0, 50,color,position, tile_mode);
        Shader shader_gradient = lin_grad;
        //binding.walletTxt.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimaryDark));
        account_balance_txt.getPaint().setShader(shader_gradient);
        account_balance_txt.setText("$"+method.pref.getString(method.account_balance,null));
        RelativeLayout=findViewById(R.id.first_lyt);
        intLayout=findViewById(R.id.intLayout);
        setSupportActionBar(toolbar);

        FirebaseApp.initializeApp(MainActivity.this);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("FaireBaseToken",refreshedToken.toString());

        binding.toalreferalTxt.setText("$"+method.pref.getString(method.total_interest_paid,null));


        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        addFubdLogo = findViewById(R.id.add_fubd_logo);
        addFundTxt = findViewById(R.id.add_fund_txt);
        historylogo=findViewById(R.id.history_logo);
        historytxt=findViewById(R.id.history_txt);
        walletlogo=findViewById(R.id.wallet_logo);
        wallettxt=findViewById(R.id.wallet_txt);
        withdrologo=findViewById(R.id.withdraw_logo);
        withdrotxt=findViewById(R.id.withdraw_txt);


        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(actionBarDrawerToggle);
        //toolbar.setNavigationIcon(R.drawable.ic_sidemenu);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);


        actionBarDrawerToggle.syncState();

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(Gravity.LEFT);



            }

        });


        final View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);
        name = headerLayout.findViewById(R.id.name);
        imageView_edit_profile = headerLayout.findViewById(R.id.imageView_edit_profile);
        balance = headerLayout.findViewById(R.id.balance);
        name.setText(method.pref.getString(method.First_Name.toString(),null)+" "+method.pref.getString(method.Last_Name,null));




        user_image = headerLayout.findViewById(R.id.imageView_Userimage);
        imageView_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,EditProfile.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_enter,R.anim.left_out);
            }
        });
        if (method.pref.getBoolean(method.pref_login, false)) {
            navigationView.getMenu().getItem(1).setVisible(true);
            navigationView.getMenu().getItem(2).setVisible(true);
            navigationView.getMenu().getItem(3).setVisible(true);
            navigationView.getMenu().getItem(4).setVisible(true);


        } else {
            navigationView.getMenu().getItem(1).setVisible(false);
            navigationView.getMenu().getItem(3).setVisible(false);


        }
        navigationView.getMenu().getItem(0).setChecked(true);
        DisableWithDrawal(method.pref.getString(method.Id,null));






        fullscreen();
        initMain();

    }

    public void openCloseNavigationDrawer() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
            drawer.openDrawer(GravityCompat.START);

        }
    }

    private boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) !=
                PackageManager.PERMISSION_GRANTED) {
            //  ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, requestCode);
            return false;
        }

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initMain() {
        if (getIntent().getStringExtra("type")!= null &&
                !getIntent().getStringExtra("type").isEmpty() &&
                !getIntent().getStringExtra("type").equals("null"))
        {

            String Res=getIntent().getStringExtra("type");
            String Id=getIntent().getStringExtra("fund_id");
            String amount=getIntent().getStringExtra("amount");
            String contract_end_charge=getIntent().getStringExtra("contract_end_charge");
            String contract_end_interest=getIntent().getStringExtra("contract_end_interest");
            WithdrowFragment searchFragment = new WithdrowFragment();
            Bundle bundle = new Bundle();

            bundle.putString("type",Res);

            searchFragment.setArguments(bundle);

            setDefultBottomBar();
            binding.walletLogo.setImageTintList(ContextCompat.getColorStateList(MainActivity.this, R.color.lightwhite));
            binding.walletTxt.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.lightwhite));
            binding.withdrawLogo.setImageTintList(ContextCompat.getColorStateList(MainActivity.this, R.color.colorPrimaryDark));
            binding.withdrawTxt.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimaryDark));
            getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.container, searchFragment).commit();

            overridePendingTransition(R.anim.right_enter, R.anim.left_out);


        }
        else
        {
            setDefultBottomBar();
            binding.walletLogo.setImageTintList(ContextCompat.getColorStateList(MainActivity.this, R.color.colorPrimaryDark));
            wallettxt.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimaryDark));

            getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.container, new WalletFragment()).commit();
            overridePendingTransition(R.anim.right_enter, R.anim.left_out);


        }




        binding.historyLyt.setOnClickListener(v -> {
            setDefultBottomBar();
            binding.historyLogo.setImageTintList(ContextCompat.getColorStateList(MainActivity.this, R.color.colorPrimaryDark));
            binding.historyTxt.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimaryDark));
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new HistoryFragment()).commit();
            overridePendingTransition(R.anim.right_enter, R.anim.left_out);
            WithdrowFudThreeActivity.Responcett="";
            DisableWithDrawal(method.pref.getString(method.Id,null));
        });
        binding.addFundLyt.setOnClickListener(v -> {
            setDefultBottomBar();
            binding.addFubdLogo.setImageTintList(ContextCompat.getColorStateList(MainActivity.this, R.color.colorPrimaryDark));
            binding.addFundTxt.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimaryDark));
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new AddFundFragment()).commit();
            overridePendingTransition(R.anim.right_enter, R.anim.left_out);
            WithdrowFudThreeActivity.Responcett="";
            DisableWithDrawal(method.pref.getString(method.Id,null));

        });




        binding.profileLyt.setOnClickListener(v -> {
            setDefultBottomBar();
            binding.profileLogo.setImageTintList(ContextCompat.getColorStateList(MainActivity.this, R.color.colorPrimaryDark));
            binding.profileTxt.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimaryDark));
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new ProfileFragment()).commit();
            overridePendingTransition(R.anim.right_enter, R.anim.left_out);
            WithdrowFudThreeActivity.Responcett="";
            DisableWithDrawal(method.pref.getString(method.Id,null));
        });
        binding.walletLyout.setOnClickListener(v -> {
            setDefultBottomBar();
            binding.walletLogo.setImageTintList(ContextCompat.getColorStateList(MainActivity.this, R.color.colorPrimaryDark));
            binding.walletTxt.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimaryDark));
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new WalletFragment()).commit();
            overridePendingTransition(R.anim.right_enter, R.anim.left_out);
            WithdrowFudThreeActivity.Responcett="";
            DisableWithDrawal(method.pref.getString(method.Id,null));
        });

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setDefultBottomBar() {
        binding.walletLogo.setImageTintList(ContextCompat.getColorStateList(this, R.color.lightwhite));
        binding.walletTxt.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.lightwhite));

        binding.historyLogo.setImageTintList(ContextCompat.getColorStateList(this, R.color.lightwhite));
        binding.addFubdLogo.setImageTintList(ContextCompat.getColorStateList(this, R.color.lightwhite));
        binding.withdrawLogo.setImageTintList(ContextCompat.getColorStateList(MainActivity.this, R.color.lightwhite));

        binding.withdrawTxt.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.lightwhite));

        binding.profileLogo.setImageTintList(ContextCompat.getColorStateList(this, R.color.lightwhite));

              binding.historyTxt.setTextColor(ContextCompat.getColor(this, R.color.lightwhite));
        binding.addFundTxt.setTextColor(ContextCompat.getColor(this, R.color.lightwhite));
            binding.profileTxt.setTextColor(ContextCompat.getColor(this, R.color.lightwhite));



    }
    @Override public boolean onNavigationItemSelected(MenuItem item) {
        // Handle bottom_navigation view item clicks here.
        //Checking if the item is in checked state or not, if not make it in checked state
        if (item.isChecked())
            item.setChecked(false);
        else
            item.setChecked(true);

        //Closing drawer on item click
        drawer.closeDrawers();

        // Handle bottom_navigation view item clicks here.
        int id = item.getItemId();

        //bottom_navigation.getMenu().setGroupCheckable(0, false, true);

        switch (id) {

            case R.id.addfund:
                setDefultBottomBar();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new AddFundFragment(), getResources().getString(R.string.add_fund_txt)).commitAllowingStateLoss();
                binding.addFubdLogo.setImageTintList(ContextCompat.getColorStateList(this, R.color.colorPrimaryDark));
                binding.addFundTxt.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

                return true;


            case R.id.wallet:
                setDefultBottomBar();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new WalletFragment(), getResources().getString(R.string.withdraw_txt)).commitAllowingStateLoss();
                binding.walletTxt.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
                binding.walletLogo.setImageTintList(ContextCompat.getColorStateList(this, R.color.colorPrimaryDark));

                return true;


            case R.id.transection:
                setDefultBottomBar();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new HistoryFragment(), getResources().getString(R.string.transaction_history_txt)).commitAllowingStateLoss();
                historytxt.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
                binding.historyLogo.setImageTintList(ContextCompat.getColorStateList(this, R.color.colorPrimaryDark));

                return true;
            case R.id.faq:
                Intent i =new Intent(MainActivity.this,FaqActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.right_enter,R.anim.left_out);
                return true;

            case R.id.privacypolicy:
                Intent ii =new Intent(MainActivity.this,PrivacyPolicy.class);
                startActivity(ii);
                overridePendingTransition(R.anim.right_enter,R.anim.left_out);
                return true;
            case R.id.contactus:
                Intent intent =new Intent(MainActivity.this,ContactUsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_enter,R.anim.left_out);
                return true;
            case R.id.logout:
                builder.setTitle("Logout");
                builder.setMessage("Do you want to close this application ?")
                        .setCancelable(false)
                        .setPositiveButton(Html.fromHtml("<font color='#BF953F'>Yes</font>"), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                                method.editor.putBoolean(method.pref_login, false);
                                method.editor.clear();
                                method.editor.commit();
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                MainActivity.this.overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                                startActivity(intent);
                                MainActivity.this.finish();
                            }
                        })
                        .setNegativeButton(Html.fromHtml("<font color='#BF953F'>NO</font>"), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();

                                // Toast.makeText(getActivity(),"you choose no action for alertbox", Toast.LENGTH_SHORT).show();
                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();

                //Setting the title manually
                alert.setTitle(Html.fromHtml("<font color='#BF953F'>Logout</font>"));
                alert.show();
                return true;



            default:
                return true;
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

    public void DisableWithDrawal(String UserId) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("user_id", UserId);
        String url = Apis.ROOT_URL + getString(R.string.check_user_withdrawal);
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

                        binding.withdrawLyt.setOnClickListener(v -> {
                            binding.progressBar.setVisibility(View.GONE);
                            setDefultBottomBar();


                            binding.withdrawLogo.setImageTintList(ContextCompat.getColorStateList(MainActivity.this, R.color.colorPrimaryDark));
                            binding.withdrawTxt.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimaryDark));
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, new WithdrowFragment()).commit();
                            overridePendingTransition(R.anim.right_enter, R.anim.left_out);

                        });

                    }

                    else
                    {









                        binding.withdrawLogo.setImageTintList(ContextCompat.getColorStateList(MainActivity.this, R.color.toolbar));
                        binding.withdrawTxt.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.toolbar));
                        binding.withdrawLyt.setClickable(false);



                        //method.alertBox(jsonObject.getString("message"));

                    }


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
    @Override
    protected void onResume() {
        WalletHistory(method.pref.getString(method.Id,null));
        super.onResume();
    }

    @Override
    protected void onRestart() {
        WalletHistory(method.pref.getString(method.Id,null));
        super.onRestart();
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
                        account_balance = jsonObject1.getString("account_balance");
                        String total_interest_paid = jsonObject1.getString("total_interest_paid");
                        String accrued_interest = jsonObject1.getString("accrued_interest");
                        binding.accountBalanceTxt.setText("$"+account_balance.toString());
                        binding.toalreferalTxt.setText("$"+total_interest_paid.toString());

                        balance.setText("Account Balance:" + " $" + account_balance, null);
                        method.editor.putString(method.account_balance, account_balance);
                        method.editor.putString(method.total_interest_paid, total_interest_paid);
                        method.editor.putString(method.accrued_interest, accrued_interest);
                        method.editor.putBoolean(method.pref_login, true);
                        method.editor.commit();


                    }

                    else {
                        binding.progressBar.setVisibility(View.GONE);
                        String message = jsonObject.getString("message");
                        Toast.makeText(MainActivity.this, message.toString(), Toast.LENGTH_SHORT).show();
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



    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
            }
            if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
                String title = getSupportFragmentManager().getFragments().get(getSupportFragmentManager().getBackStackEntryCount() - 1).getTag();
                if (title != null) {
                    txt_toolbartitle.setText(title);
                }
                super.onBackPressed();
            } else {
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, getResources().getString(R.string.Please_click_BACK_again_to_exit), Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            }

        }
    }
}

