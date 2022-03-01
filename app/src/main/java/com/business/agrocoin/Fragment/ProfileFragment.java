package com.business.agrocoin.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.business.agrocoin.Activity.ChangePasswordActivity;
import com.business.agrocoin.Activity.ContactUsActivity;
import com.business.agrocoin.Activity.FaqActivity;
import com.business.agrocoin.Activity.LoginActivity;
import com.business.agrocoin.Activity.MainActivity;
import com.business.agrocoin.Activity.PrivacyPolicy;
import com.business.agrocoin.R;
import com.business.agrocoin.Util.Method;
import com.business.agrocoin.databinding.ProfileFragmentBinding;

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
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(getActivity());
        MainActivity.txt_toolbartitle.setText(getResources().getString(R.string.profile_setting_txt));
        MainActivity.RelativeLayout.setVisibility(View.GONE);
        MainActivity.filter.setVisibility(View.VISIBLE);
        method = new Method(getActivity());


        binding.faqlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FaqActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_enter, R.anim.left_out);

            }
        });
        binding.privacyLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PrivacyPolicy.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_enter, R.anim.left_out);
            }
        });
        binding.changepasswordLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_enter, R.anim.left_out);
            }
        });
        binding.contactLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ContactUsActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_enter, R.anim.left_out);
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

                                dialog.cancel();
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
