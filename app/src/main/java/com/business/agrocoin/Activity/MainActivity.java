package com.business.agrocoin.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.business.agrocoin.Adapter.TransactionHistoryAdapter;
import com.business.agrocoin.Fragment.HistoryFragment;
import com.business.agrocoin.Fragment.ReceivedFragment;
import com.business.agrocoin.Fragment.SendFragment;
import com.business.agrocoin.Fragment.WalletFragment;

import com.business.agrocoin.Model.TransactionModel;
import com.business.agrocoin.Util.Apis;
import com.business.agrocoin.Util.Method;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;

import com.business.agrocoin.R;
import com.business.agrocoin.databinding.ActivityMainBinding;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Collections;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ActivityMainBinding binding;
    Method method;
    public static FrameLayout container;
    public static RelativeLayout selctedrecy;
    public static DrawerLayout drawer;
    public static NavigationView navigationView;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    public static Toolbar toolbar;
    public static TextView name, balance, textView_Phone_nav, textView_Walletbalance_nav;
    private CircleImageView user_image;
    public static TextView txt_toolbartitle;
    public static RelativeLayout RelativeLayout;
    public static ImageView menu;
    public static TextView account_balance_txt, toalreferal_txt;
    public static LinearLayout intLayout;
    public static ImageView addFubdLogo, historylogo, walletlogo, withdrologo, profilelogo;
    public static TextView addFundTxt, historytxt, wallettxt, withdrotxt, profiletxt;
    private ImageView imageView_edit_profile;
    AlertDialog.Builder builder;
    String Type = "";
    String account_balance;
    public static ImageView filter;
    boolean doubleBackToExitPressedOnce = false;
    Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        method = new Method(MainActivity.this);
        builder = new AlertDialog.Builder(MainActivity.this);
        account_balance_txt = findViewById(R.id.account_balance_txt);

        container = binding.container;
        selctedrecy=findViewById(R.id.selctedrecy);

        toolbar = findViewById(R.id.toolbar_main);
        txt_toolbartitle = findViewById(R.id.appname);
        filter = findViewById(R.id.filter);
        menu = findViewById(R.id.menu);
        txt_toolbartitle.setText(getResources().getString(R.string.app_name));
        txt_toolbartitle.setVisibility(View.VISIBLE);
        binding.notificationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_enter, R.anim.left_out);
            }
        });
        binding.receivedLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefultBottomBar();


                binding.profileLogo.setImageTintList(ContextCompat.getColorStateList(MainActivity.this, R.color.white));
                binding.profileTxt.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new ReceivedFragment()).commit();

                WalletFragment.TYTPE.equals("2");
            }
        });
        binding.sendLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefultBottomBar();


                binding.withdrawLogo.setImageTintList(ContextCompat.getColorStateList(MainActivity.this, R.color.white));
                binding.withdrawTxt.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new SendFragment()).commit();

                WalletFragment.TYTPE.equals("2");
            }
        });
        int[] color = {getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.white)};
        float[] position = {0, 1};
        Shader.TileMode tile_mode = Shader.TileMode.MIRROR; // or TileMode.REPEAT;
        LinearGradient lin_grad = new LinearGradient(0, 0, 0, 50, color, position, tile_mode);
        Shader shader_gradient = lin_grad;

        String mainbalnace=method.pref.getString(method.account_balance,null);
        DecimalFormat dtime = new DecimalFormat("0.00");
        dtime.setRoundingMode(RoundingMode.DOWN);
        account_balance_txt.setText("AGRO " + dtime.format(Double.parseDouble(mainbalnace.toString())));

        setSupportActionBar(toolbar);
        FirebaseApp.initializeApp(MainActivity.this);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("FaireBaseToken", refreshedToken.toString());
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        addFubdLogo = findViewById(R.id.add_fubd_logo);
        addFundTxt = findViewById(R.id.add_fund_txt);
        historylogo = findViewById(R.id.history_logo);
        historytxt = findViewById(R.id.history_txt);
        walletlogo = findViewById(R.id.wallet_logo);
        wallettxt = findViewById(R.id.wallet_txt);
        withdrologo = findViewById(R.id.withdraw_logo);
        profilelogo = findViewById(R.id.profile_logo);
        withdrotxt = findViewById(R.id.withdraw_txt);
        profiletxt = findViewById(R.id.profile_txt);



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
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (true) {
                    try {
                        Thread.sleep(10000);
                        mHandler.post(new Runnable() {

                            @Override
                            public void run() {
                                  UpdateBalance(method.pref.getString(method.Id,null));
                                // TODO Auto-generated method stub
                                // Write your code here to update the UI.
                            }
                        });
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
            }
        }).start();

        final View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);
        name = headerLayout.findViewById(R.id.name);
        imageView_edit_profile = headerLayout.findViewById(R.id.imageView_edit_profile);
        balance = headerLayout.findViewById(R.id.balance);
        name.setText(method.pref.getString(method.UserName.toString(), null));

        balance.setText("Account Balance: " + account_balance_txt.getText().toString());

        user_image = headerLayout.findViewById(R.id.imageView_Userimage);
        imageView_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditProfile.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_enter, R.anim.left_out);
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
        //DisableWithDrawal(method.pref.getString(method.Id, null));


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


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initMain() {

            setDefultBottomBar();
            binding.walletLogo.setImageTintList(ContextCompat.getColorStateList(MainActivity.this, R.color.white));
            wallettxt.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));

            getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.container, new WalletFragment()).commit();
            overridePendingTransition(R.anim.right_enter, R.anim.left_out);





        binding.historyLyt.setOnClickListener(v -> {
            setDefultBottomBar();
            binding.historyLogo.setImageTintList(ContextCompat.getColorStateList(MainActivity.this, R.color.white));
            binding.historyTxt.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new HistoryFragment()).commit();
            overridePendingTransition(R.anim.right_enter, R.anim.left_out);
            WalletFragment.TYTPE.equals("2");
        });


        binding.profileLyt.setOnClickListener(v -> {
            setDefultBottomBar();
            binding.profileLogo.setImageTintList(ContextCompat.getColorStateList(MainActivity.this, R.color.white));
            binding.profileTxt.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new ReceivedFragment()).commit();
            overridePendingTransition(R.anim.right_enter, R.anim.left_out);
            WalletFragment.TYTPE.equals("2");
        });
        binding.walletLyout.setOnClickListener(v -> {
            setDefultBottomBar();
            binding.walletLogo.setImageTintList(ContextCompat.getColorStateList(MainActivity.this, R.color.white));
            binding.walletTxt.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new WalletFragment()).commit();
            overridePendingTransition(R.anim.right_enter, R.anim.left_out);
            WalletFragment.TYTPE.equals("2");
        });
        binding.withdrawLyt.setOnClickListener(v -> {

            setDefultBottomBar();


            binding.withdrawLogo.setImageTintList(ContextCompat.getColorStateList(MainActivity.this, R.color.white));
            binding.withdrawTxt.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new SendFragment()).commit();
            overridePendingTransition(R.anim.right_enter, R.anim.left_out);
            WalletFragment.TYTPE.equals("2");
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

        WalletFragment.TYTPE.equals("2");
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

            case R.id.wallet:
                setDefultBottomBar();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new WalletFragment(), getResources().getString(R.string.withdraw_txt)).commitAllowingStateLoss();
                binding.walletTxt.setTextColor(ContextCompat.getColor(this, R.color.white));
                binding.walletLogo.setImageTintList(ContextCompat.getColorStateList(this, R.color.white));
                WalletFragment.TYTPE.equals("2");
                return true;


            case R.id.transection:
                setDefultBottomBar();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new HistoryFragment(), getResources().getString(R.string.list_wallet_txt)).commitAllowingStateLoss();
                historytxt.setTextColor(ContextCompat.getColor(this, R.color.white));
                binding.historyLogo.setImageTintList(ContextCompat.getColorStateList(this, R.color.white));
                WalletFragment.TYTPE.equals("2");
                return true;

            case R.id.send:

                setDefultBottomBar();
                binding.withdrawLogo.setImageTintList(ContextCompat.getColorStateList(MainActivity.this, R.color.white));
                binding.withdrawTxt.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new SendFragment()).commit();
                WalletFragment.TYTPE.equals("2");
                return true;
            case R.id.recieved:
                setDefultBottomBar();
                binding.profileLogo.setImageTintList(ContextCompat.getColorStateList(MainActivity.this, R.color.white));
                binding.profileTxt.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new ReceivedFragment()).commit();
                overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                WalletFragment.TYTPE.equals("2");
                return true;
            case R.id.logout:
                builder.setTitle("Logout");
                builder.setMessage("Are you sure you want to logout ?")
                        .setCancelable(false)
                        .setPositiveButton(Html.fromHtml("<font color='#029E9A'>Yes</font>"), new DialogInterface.OnClickListener() {
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
                        .setNegativeButton(Html.fromHtml("<font color='#029E9A'>NO</font>"), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();
                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();

                //Setting the title manually
                alert.setTitle(Html.fromHtml("<font color='#029E9A'>Logout</font>"));
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
    @Override public void onBackPressed() {
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


    private void UpdateBalance(String UserId) {


        AsyncHttpClient client = new AsyncHttpClient();
        String url = Apis.ROOT_URLL + getString(R.string.update_balnce)+"userId="+UserId;
        Log.e("Url------->", url);

        client.get(url,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);
                Log.e("Urlgfg------->", res.toString());
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    String status = jsonObject.getString("statusCode");
                    if (status.equalsIgnoreCase("200")) {
                        String result = jsonObject.getString("result");

                        Log.e("MainAccountBalance",result.toString());
                        method.editor.putString(method.account_balance, result);
                        method.editor.commit();
                        String mainbalnace=method.pref.getString(method.account_balance,null);
                        DecimalFormat dtime = new DecimalFormat("0.00");
                        dtime.setRoundingMode(RoundingMode.DOWN);
                        account_balance_txt.setText("AGRO " + dtime.format(Double.parseDouble(mainbalnace.toString())));

                    }






                     else {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "kkkkkkkkkk", Toast.LENGTH_SHORT).show();

                        String message = jsonObject.getString("statusMsg");
                        // binding.textViewCategory.setText(message);
                        //binding.textViewCategory.setVisibility(View.VISIBLE);
                    }




                } catch (JSONException e) {
                    e.printStackTrace();


                }
            }



            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {


            }
        });
    }
}

