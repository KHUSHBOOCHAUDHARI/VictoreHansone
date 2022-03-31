package com.business.victorehansone.Util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Display;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;

import com.business.victorehansone.R;

import org.json.JSONObject;


public class Method {
    private Activity activity;
    private Context context;
    public static boolean loginBack = false, allowPermitionExternalStorage = false;
    public static boolean isUpload = true, isDownload = true;
    public boolean personalization_ad = false;
    public static String search_title;
    public SharedPreferences pref;
    public SharedPreferences.Editor editor;
    private final String myPreference = "login";
    public String pref_login = "pref_login";
    public String notification = "notification";
    public String Id = "id";
    private FullScreen fullScreen;


    public String Address_Id = "address_id";
    public String First_Name = "firstname";
    public String Last_Name = "lastname";
    public String Email_id = "emilid";
    public String Phone_No = "phoneno";
    private String firstTime = "firstTime";
    public String Password = "password";
    public String account_balance="account_balance";
    public String ic_userName="ic_username";
    public String profile_image="profile_image";
    public String JsonString="jasonstring";
    public String detailJsonObject="detailjsonobject";
    public String aboutusobject="aboutusobject";
    public String categoryobject="categoryobject";
    public String categorypostobject_1125="categoryobjectpot1125";
    public String categorypostobject_1121="categoryobjectpot1121";
    public String categorypostobject_1122="categoryobjectpot1122";
    public String categorypostobject_1123="categoryobjectpot1123";
    public String categorypostobject_1124="categoryobjectpot1124";
    public String categorypostobject_1126="categoryobjectpot1126";
    public String categorypostobject_1131="categoryobjectpot1131";
    public String categorypostobject_1132="categoryobjectpot1132";
    public String categorypostobject="categorypostobject";
    public String categorypostobjectnew="categorypostobjectnew";
    public String featurepost="featurepost";
    public String layoutvalue="layoutvalue";





    public String id = "id";
    public String user_login = "user_login";
    public String user_pass = "user_pass";
    public String user_nicename = "user_nicename";
    public String user_email = "user_email";
    public String user_url = "user_url";
    public String user_registered = "user_registered";
    public String user_activation_key="user_activation_key";
    public String user_status="user_status";
    public String display_name="display_name";
    public String m_id="m_id";
    public String txn_id="txn_id";
    public String u_id="u_id";
    public String transaction_subject="transaction_subject";
    public String current_plane="currentplane";
    public String post_list_add="postlist";
    public int inc=12;
    public int dec=17;











    @SuppressLint("StaticFieldLeak")
    public static Activity activity_upload;

    @SuppressLint("CommitPrefEdits")
    public Method(Activity activity) {
        this.activity = activity;

        pref = activity.getSharedPreferences(myPreference, 0); // 0 - for private mode
        editor = pref.edit();
    }




    public void login() {
        if (!pref.getBoolean(firstTime, false)) {
            editor.putBoolean(pref_login, false);
            editor.putBoolean(firstTime, true);
            editor.clear();
            editor.commit();
        }
    }



    //Whatsapp application installation or not check
    public boolean isAppInstalled_Whatsapp(Activity activity) {
        String packageName = "com.whatsapp";
        Intent mIntent = activity.getPackageManager().getLaunchIntentForPackage(packageName);
        if (mIntent != null) {
            return true;
        } else {
            return false;
        }
    }

    //instagram application installation or not check
    public boolean isAppInstalled_Instagram(Activity activity) {
        String packageName = "com.instagram.android";
        Intent mIntent = activity.getPackageManager().getLaunchIntentForPackage(packageName);
        if (mIntent != null) {
            return true;
        } else {
            return false;
        }
    }
    public void ShowFullScreen(boolean isFullScreen) {
        fullScreen.fullscreen(isFullScreen);
    }

    //facebook application installation or not check
    public boolean isAppInstalled_facebook(Activity activity) {
        String packageName = "com.facebook.katana";
        Intent mIntent = activity.getPackageManager().getLaunchIntentForPackage(packageName);
        if (mIntent != null) {
            return true;
        } else {
            return false;
        }
    }

    //twitter application installation or not check
    public boolean isAppInstalled_twitter(Activity activity) {
        String packageName = "com.twitter.android";
        Intent mIntent = activity.getPackageManager().getLaunchIntentForPackage(packageName);
        if (mIntent != null) {
            return true;
        } else {
            return false;
        }
    }

    //network check
    public static boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //get screen width
    public int getScreenWidth() {
        int columnWidth;
        WindowManager wm = (WindowManager) activity
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();

        point.x = display.getWidth();
        point.y = display.getHeight();

        columnWidth = point.x;
        return columnWidth;
    }

    //get screen height
    public int getScreenHeight() {
        int columnHeight;
        WindowManager wm = (WindowManager) activity
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();

        point.x = display.getWidth();
        point.y = display.getHeight();

        columnHeight = point.y;
        return columnHeight;
    }

    //alert message box
    public void alertBox(String message) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);

        alertDialogBuilder.setMessage(message);

        alertDialogBuilder.setPositiveButton(activity.getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.white);




    }




    /**
     * escape()
     *
     * Escape a give String to make it safe to be printed or stored.
     *
     * @param s The input String.
     * @return The output String.
     **/
    public static String escape(String s){
        return s.replace("\\", "\\\\")
                .replace("\t", "\\t")
                .replace("\b", "\\b")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\f", "\\f")
                .replace("\'", "\\'")
                .replace("\"", "\\\"");
    }

}
