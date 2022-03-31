package com.business.victorehansone.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.business.victorehansone.Activity.ChangePasswordActivity;
import com.business.victorehansone.Activity.ContactUsActivity;
import com.business.victorehansone.Activity.EditProfile;
import com.business.victorehansone.Activity.FaqActivity;
import com.business.victorehansone.Activity.LoginActivity;
import com.business.victorehansone.Activity.MainActivity;
import com.business.victorehansone.Activity.NotificationActivity;
import com.business.victorehansone.Activity.PrivacyPolicy;
import com.business.victorehansone.Activity.SavePostListActivity;
import com.business.victorehansone.Activity.SettingActivity;
import com.business.victorehansone.Activity.SubscriptionHistoryActivity;
import com.business.victorehansone.R;
import com.business.victorehansone.Util.Method;
import com.business.victorehansone.databinding.ProfileFragmentBinding;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileFragment extends Fragment {
    ProfileFragmentBinding binding;
    AlertDialog.Builder builder;
    Method method;
    private String phone_code;
    String Name="";
    String FirstName, LastName, Emailid, Password, MobileNo, ReferralCode;
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.profile_fragment, container, false);
        method = new Method(getActivity());

        builder = new AlertDialog.Builder(getActivity());
        MainActivity.txt_toolbartitle.setText(getResources().getString(R.string.profile_setting_txt));
        if(method.pref.getBoolean(method.pref_login, true))
        {
            binding.logoutEdt.setText("Logout");
        }
        else
        {
            binding.logoutEdt.setText("Login");
        }

        if(method.pref.getBoolean(method.pref_login, true)) {
          //  Log.e("nameee",method.pref.getString(method.display_name, null));
            String Name = method.pref.getString(method.display_name, null);
           // Log.e("dksahdksahdksa",method.pref.getString(method.profile_image,null));

            Glide.with(getActivity()).load(method.pref.getString(method.profile_image,null)).placeholder(R.drawable.progress_animation).into(binding.imgprofile);
          if (!TextUtils.isEmpty(method.pref.getString(method.First_Name,null))) {
              binding.firstnameEdt.setText(method.pref.getString(method.First_Name, null));
          }
            if (!TextUtils.isEmpty(method.pref.getString(method.Last_Name,null))) {
                binding.lastnameEdt.setText(method.pref.getString(method.Last_Name, null));
            }

            binding.emailEdt.setText(method.pref.getString(method.user_email, null));
            binding.planeEdt.setText(method.pref.getString(method.transaction_subject, null));
            if (method.pref.getBoolean(method.pref_login, false)) {
                if ((method.pref.getString(method.transaction_subject, null).equals(""))) {
                    binding.planeEdt.setText("Free Subscription");
                }
            }

            binding.lastnameLayout.setVisibility(View.VISIBLE);
            binding.emailLayout.setVisibility(View.VISIBLE);
            binding.passwordLayout.setVisibility(View.VISIBLE);
            binding.conPasswordLayout.setVisibility(View.VISIBLE);

            binding.profilecamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(), EditProfile.class);
                    intent.putExtra("first_name",binding.firstnameEdt.getText().toString());
                    intent.putExtra("last_name",binding.lastnameEdt.getText().toString());
                    intent.putExtra("email_id",binding.emailEdt.getText().toString());

                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.right_enter,R.anim.left_out);
                }
            });
            binding.conPasswordLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(), SubscriptionHistoryActivity.class);

                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.right_enter,R.anim.left_out);
                }
            }); binding.conPasswordEdt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(), SubscriptionHistoryActivity.class);

                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.right_enter,R.anim.left_out);
                }
            });

            binding.savePostLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(), SavePostListActivity.class);

                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.right_enter,R.anim.left_out);
                }
            });
            binding.savepostEdt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(), SavePostListActivity.class);

                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.right_enter,R.anim.left_out);
                }
            });
            binding.notificationLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(), NotificationActivity.class);

                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.right_enter,R.anim.left_out);
                }
            });
            binding.notificationEdt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(), NotificationActivity.class);

                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.right_enter,R.anim.left_out);
                }
            });
            binding.settingLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(), SettingActivity.class);

                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.right_enter,R.anim.left_out);
                }
            });
            binding.settingEdt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(), SettingActivity.class);

                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.right_enter,R.anim.left_out);
                }
            });
            binding.logoutLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                   LOGOUT();
                }
            });
            binding.logoutEdt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LOGOUT();
                }
            });


        }
        else
        {
            binding.firstnameEdt.setText("Guest User");
            binding.lastnameLayout.setVisibility(View.GONE);
            binding.emailLayout.setVisibility(View.GONE);
            binding.passwordLayout.setVisibility(View.GONE);
            binding.conPasswordLayout.setVisibility(View.GONE);
            binding.profilecamera.setClickable(false);
            binding.notificationLayout.setVisibility(View.GONE);
            binding.savePostLayout.setVisibility(View.GONE);
            binding.notificationSwitchedLayout.setVisibility(View.GONE);
            binding.logoutLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    LOGOUT();
                }
            });
            binding.logoutEdt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LOGOUT();
                }
            });
        }
        if (method.pref.getBoolean(method.notification, true)) {
            binding.switchSetting.setChecked(true);
        } else {
            binding.switchSetting.setChecked(false);
        }

        binding.switchSetting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    FirebaseInstanceId.getInstance().getToken();
                } else {
                    try {
                        FirebaseInstanceId.getInstance().deleteInstanceId();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                method.editor.putBoolean(method.notification, isChecked);
                method.editor.commit();
            }
        });


        return binding.getRoot();


    }


    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
    public void LOGOUT()
    {

        if(method.pref.getBoolean(method.pref_login, true))
        {
            builder.setTitle("Logout");
            builder.setMessage("Are you sure you want to logout ?")
                    .setCancelable(false)
                    .setPositiveButton(Html.fromHtml("<font color='#262626'>Yes</font>"), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {


                            method.editor.putBoolean(method.pref_login, false);
                            method.editor.clear();
                            method.editor.commit();
                            Intent intent = new Intent(getActivity(), LoginActivity.class);

                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.right_enter, R.anim.left_out);

                            getActivity().finish();

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
            Intent i = new Intent(getActivity(), LoginActivity.class);
            startActivity(i);
            getActivity().overridePendingTransition(R.anim.right_enter, R.anim.left_out);
           getActivity().finish();
        }

    }
}
