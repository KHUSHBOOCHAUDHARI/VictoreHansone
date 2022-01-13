package com.business.cryptosoar.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.business.cryptosoar.Activity.AddAddressActivity;
import com.business.cryptosoar.Activity.AddressListActivity;
import com.business.cryptosoar.Activity.ChangePasswordActivity;
import com.business.cryptosoar.Activity.ContactUsActivity;
import com.business.cryptosoar.Activity.EmailVerificationActivity;
import com.business.cryptosoar.Activity.FaqActivity;
import com.business.cryptosoar.Activity.LoginActivity;
import com.business.cryptosoar.Activity.MainActivity;
import com.business.cryptosoar.Activity.PaymentSettingActivity;
import com.business.cryptosoar.Activity.PrivacyPolicy;
import com.business.cryptosoar.R;
import com.business.cryptosoar.Util.Apis;
import com.business.cryptosoar.Util.Method;
import com.business.cryptosoar.databinding.ProfileFragmentBinding;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ProfileFragment extends Fragment {
    ProfileFragmentBinding binding;
    AlertDialog.Builder builder;
    Method method;
    public ProfileFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.profile_fragment, container, false);
        builder = new AlertDialog.Builder(getActivity());
        MainActivity.txt_toolbartitle.setText(getResources().getString(R.string.profile_setting_txt));
        MainActivity.RelativeLayout.setVisibility(View.GONE);
        MainActivity.filter.setVisibility(View.GONE);
       method=new Method(getActivity());

        binding.paymentSettingLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), PaymentSettingActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_enter,R.anim.left_out);
            }
        });
        binding.faqlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), FaqActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_enter,R.anim.left_out);

            }
        });
        binding.privacyLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), PrivacyPolicy.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_enter,R.anim.left_out);
            }
        });
        binding.changepasswordLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), ChangePasswordActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_enter,R.anim.left_out);
            }
        });
        binding.contactLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), ContactUsActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_enter,R.anim.left_out);
            }
        });
        binding.addresslistLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), AddressListActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_enter,R.anim.left_out);
            }
        });
        binding.logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setTitle("Logout");
                builder.setMessage("Are you sure you want to logout ?")
                        .setCancelable(false)
                        .setPositiveButton(Html.fromHtml("<font color='#BF953F'>Yes</font>"), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                                method.editor.putBoolean(method.pref_login, false);
                                method.editor.clear();
                                method.editor.commit();
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                getActivity().overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                                startActivity(intent);
                                getActivity().finish();
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
            }
        });





        return binding.getRoot();







    }






}
