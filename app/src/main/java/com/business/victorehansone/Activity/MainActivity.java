package com.business.victorehansone.Activity;

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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.business.victorehansone.Adapter.HomeAdapter;
import com.business.victorehansone.Fragment.CategoryFragment;
import com.business.victorehansone.Fragment.ProfileFragment;
import com.business.victorehansone.Fragment.SendFragment;
import com.business.victorehansone.Fragment.HomeFragment;

import com.business.victorehansone.Util.Apis;
import com.business.victorehansone.Util.Method;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;

import com.business.victorehansone.R;
import com.business.victorehansone.databinding.ActivityMainBinding;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.net.URL;
import java.text.DecimalFormat;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ActivityMainBinding binding;
    Method method;
    public static SeekBar seekBar;
    public static FrameLayout container;
    public static RelativeLayout selctedrecy;
    public static DrawerLayout drawer;
    public static NavigationView navigationView;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    public static Toolbar toolbar;
    public static TextView name, balance;
    private CircleImageView ic_user_image;
    public static TextView txt_toolbartitle;
    public static RelativeLayout bottomlayout,toolbarlyt;
    public static ImageView menu;
    public static TextView account_balance_txt;
    public static LinearLayout intLayout;
    public static ImageView addFubdLogo,historylogo,walletlogo,withdrologo,profilelogo;
    public static TextView addFundTxt,historytxt,wallettxt,withdrotxt,profiletxt;
    private ImageView imageView_edit_profile;
    AlertDialog.Builder builder;
    String Type = "";
    String account_balance;
    public static ImageView filter;
    boolean doubleBackToExitPressedOnce = false;
    public static int textSize = 3;
    int saveProgress;
    Handler mHandler = new Handler();
    LinearLayout linear_facbook,linear_print,linear_twit,linear_insta;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        method = new Method(MainActivity.this);
        builder = new AlertDialog.Builder(MainActivity.this);
        setSupportActionBar(toolbar);
        bottomlayout=binding.bottomlayout;
        toolbarlyt=binding.toolbarlyt;
        container = binding.container;
        toolbar = findViewById(R.id.toolbar_main);
        txt_toolbartitle = findViewById(R.id.appname);
        linear_facbook = findViewById(R.id.linear_facbook);
        linear_print = findViewById(R.id.linear_print);
        linear_twit = findViewById(R.id.linear_twit);
        linear_insta = findViewById(R.id.linear_insta);
        linear_facbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RedirectLink("https://www.facebook.com/VDHansonsCup");
            }
        });
        linear_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RedirectLink("https://parler.com/#/user/VictorDavisHanson");
            }
        });
        linear_twit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RedirectLink("https://twitter.com/VDHanson");
            }
        });
        linear_insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RedirectLink("https://www.facebook.com/VDHansonsCup");
            }
        });

        if(method.pref.getBoolean(method.pref_login, false))
        {
            if(method.pref.getString(method.current_plane,null).equals("free_subscription") || method.pref.getString(method.current_plane,null).equals(""))
            {
                binding.adView.setVisibility(View.VISIBLE);
                MobileAds.initialize(MainActivity.this, new OnInitializationCompleteListener() {
                    @Override
                    public void onInitializationComplete(InitializationStatus initializationStatus) {
                       // Toast.makeText(MainActivity.this, " sucesfull ", Toast.LENGTH_SHORT).show();
                    }
                });


                AdRequest adRequest = new AdRequest.Builder().build();
                binding.adView.loadAd(adRequest);
            }
            else {
                binding.adView.setVisibility(View.GONE);
            }
        }

        else
        {
            binding.adView.setVisibility(View.VISIBLE);
            MobileAds.initialize(MainActivity.this, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                  //  Toast.makeText(MainActivity.this, "sucesfull ", Toast.LENGTH_SHORT).show();
                }
            });


            AdRequest adRequest = new AdRequest.Builder().build();
            binding.adView.loadAd(adRequest);
        }

//        MobileAds.initialize(MainActivity.this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//                Toast.makeText(MainActivity.this, "sucesfull ", Toast.LENGTH_SHORT).show();
//            }
//        });
//        AdRequest adRequest = new AdRequest.Builder().build();
//        binding.adView.loadAd(adRequest);

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

        int[] color = {getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.white)};
        float[] position = {0, 1};
        Shader.TileMode tile_mode = Shader.TileMode.MIRROR; // or TileMode.REPEAT;
        LinearGradient lin_grad = new LinearGradient(0, 0, 0, 50, color, position, tile_mode);
        Shader shader_gradient = lin_grad;

        String mainbalnace=method.pref.getString(method.account_balance,null);
        DecimalFormat dtime = new DecimalFormat("0.00");
        dtime.setRoundingMode(RoundingMode.DOWN);
//        account_balance_txt.setText("AGRO " + dtime.format(Double.parseDouble(mainbalnace.toString())));

        setSupportActionBar(toolbar);
        FirebaseApp.initializeApp(MainActivity.this);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("FaireBaseToken", refreshedToken.toString());
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);


        if(method.pref.getBoolean(method.pref_login, false))
        {
            navigationView.getMenu().getItem(6).setTitle("Logout");
        }
        else
        {
            navigationView.getMenu().getItem(6).setTitle("Login");
        }





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
                                  //UpdateBalance(method.pref.getString(method.Id,null));
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
//        name.setText(method.pref.getString(method.ic_userName.toString(), null));

//        balance.setText("Account Balance: " + account_balance_txt.getText().toString());
        binding.bottomlayout.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.black));
        binding.homeLayout.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.green));
        ic_user_image = headerLayout.findViewById(R.id.imageView_ic_userimage);
        imageView_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });


        binding.homeLogo.setImageTintList(ContextCompat.getColorStateList(MainActivity.this, R.color.white));
        binding.homeTxt.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
        binding.homeLayout.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.black));
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
        overridePendingTransition(R.anim.right_enter, R.anim.left_out);
        HomeFragment.TYTPE.equals("2");
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
            binding.homeLogo.setImageTintList(ContextCompat.getColorStateList(MainActivity.this, R.color.white));
            binding.homeTxt.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
        binding.homeLayout.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.black));
     getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.container, new HomeFragment()).commit();
     overridePendingTransition(R.anim.right_enter, R.anim.left_out);
     binding.categoryLyt.setOnClickListener(v -> {
            setDefultBottomBar();
            binding.categoryLogo.setImageTintList(ContextCompat.getColorStateList(MainActivity.this, R.color.white));
            binding.categoryTxt.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
         binding.categoryLyt.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.black));
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new CategoryFragment()).commit();
            overridePendingTransition(R.anim.right_enter, R.anim.left_out);
            HomeFragment.TYTPE.equals("2");
        });

     binding.homeLayout.setOnClickListener(v -> {
            setDefultBottomBar();
            binding.homeLogo.setImageTintList(ContextCompat.getColorStateList(MainActivity.this, R.color.white));
            binding.homeTxt.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
            binding.homeLayout.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.black));
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
            overridePendingTransition(R.anim.right_enter, R.anim.left_out);
            HomeFragment.TYTPE.equals("2");
        });
     binding.profileLyt.setOnClickListener(v -> {

            setDefultBottomBar();


            binding.profileLogo.setImageTintList(ContextCompat.getColorStateList(MainActivity.this, R.color.white));
            binding.profileTxt.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
             binding.profileLyt.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.black));
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new ProfileFragment()).commit();
            overridePendingTransition(R.anim.right_enter, R.anim.left_out);
            HomeFragment.TYTPE.equals("2");
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setDefultBottomBar() {
        binding.homeLogo.setImageTintList(ContextCompat.getColorStateList(this, R.color.lightwhite));
        binding.homeTxt.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.lightwhite));
        binding.homeLayout.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.headline));
        binding.categoryLogo.setImageTintList(ContextCompat.getColorStateList(this, R.color.lightwhite));
        binding.profileLogo.setImageTintList(ContextCompat.getColorStateList(MainActivity.this, R.color.lightwhite));
        binding.categoryLyt.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.headline));
        binding.profileTxt.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.lightwhite));
        binding.categoryTxt.setTextColor(ContextCompat.getColor(this, R.color.lightwhite));
        binding.profileLyt.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.headline));

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

            case R.id.home:
                setDefultBottomBar();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment(), getResources().getString(R.string.profile_txt)).commitAllowingStateLoss();
                binding.homeTxt.setTextColor(ContextCompat.getColor(this, R.color.white));
                binding.homeLogo.setImageTintList(ContextCompat.getColorStateList(this, R.color.white));
                HomeFragment.TYTPE.equals("2");
                return true;


            case R.id.newsletter:
                Intent ii=new Intent(getApplicationContext(),PastNewLettersActivity.class);
                startActivity(ii);
                overridePendingTransition(R.anim.right_enter,R.anim.left_out);
                return true;

            case R.id.offers:

                Intent intent=new Intent(getApplicationContext(),OfferActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_enter,R.anim.left_out);
                return true;
            case R.id.podcasts:
                Intent podintent=new Intent(getApplicationContext(),PoadCastActivity.class);
                startActivity(podintent);
                overridePendingTransition(R.anim.right_enter,R.anim.left_out);
                return true;
            case R.id.aboutus:


                Intent intent1=new Intent(getApplicationContext(),TermandCondition.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.right_enter,R.anim.left_out);
                return true;

            case R.id.contactus:

             Intent intentt=new Intent(getApplicationContext(),ContactUsActivity.class);
             startActivity(intentt);
             overridePendingTransition(R.anim.right_enter,R.anim.left_out);
                return true;

            case R.id.logout:



                if(method.pref.getBoolean(method.pref_login, false))
                {
                    builder.setTitle("Logout");
                    builder.setMessage("Are you sure you want to logout ?")
                            .setCancelable(false)
                            .setPositiveButton(Html.fromHtml("<font color='#262626'>Yes</font>"), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {


                                    method.editor.putBoolean(method.pref_login, false);
                                    method.editor.clear();
                                    method.editor.commit();
                                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);

                                    startActivity(intent);
                                    MainActivity.this.overridePendingTransition(R.anim.right_enter, R.anim.left_out);

                                    MainActivity.this.finish();

                                }
                            })
                            .setNegativeButton(Html.fromHtml("<font color='#262626'>NO</font>"), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();
                                }
                            });
                    //Creating dialog box
                    AlertDialog alert = builder.create();

                    //Setting the title manually
                    alert.setTitle(Html.fromHtml("<font color='#262626'>Logout</font>"));
                    alert.show();
                }
                else
                {
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);

                    startActivity(i);
                    MainActivity.this.overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                    MainActivity.this.finish();
                }

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
        }
        else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                finish();
            }

            if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
                String title = getSupportFragmentManager().getFragments().get(getSupportFragmentManager().getBackStackEntryCount() - 1).getTag();
                if (title != null) {
                    txt_toolbartitle.setText(title);
                }
                super.onBackPressed();

            }
            else {

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
    private void UpdateBalance(String ic_userId) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = Apis.ROOT_URLL + getString(R.string.update_balnce)+"ic_userId="+ic_userId;
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
    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item_logout = menu.findItem(R.id.log_out);

        if(method.pref.getBoolean(method.pref_login, false))
        {
            item_logout.setTitle("Logout");
        }
        else
        {
            item_logout.setTitle("Login");
        }

        return true;
    }
    @Override public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.log_out){

            if(method.pref.getBoolean(method.pref_login, false))
            {
                builder.setTitle("Logout");
                builder.setMessage("Are you sure you want to logout ?")
                        .setCancelable(false)
                        .setPositiveButton(Html.fromHtml("<font color='#262626'>Yes</font>"), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                                method.editor.putBoolean(method.pref_login, false);
                                method.editor.clear();
                                method.editor.commit();
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
                                MainActivity.this.overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                                MainActivity.this.finish();

                            }
                        })
                        .setNegativeButton(Html.fromHtml("<font color='#262626'>NO</font>"), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();
                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();

                //Setting the title manually
                alert.setTitle(Html.fromHtml("<font color='#262626'>Logout</font>"));
                alert.show();
            }
            else
            {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);

                startActivity(intent);
                MainActivity.this.overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                MainActivity.this.finish();
            }

        }
        return super.onOptionsItemSelected(item);
    }
    public void RedirectLink(String uri) {
        String url = uri;

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }


}

