package com.business.victorehansone.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;


import com.bumptech.glide.Glide;
import com.business.victorehansone.Activity.BlogDetailActivity;
import com.business.victorehansone.Activity.CategoryPostActivity;
import com.business.victorehansone.Activity.LoginActivity;
import com.business.victorehansone.Activity.SubscribeActivity;
import com.business.victorehansone.Model.HomeModel;
import com.business.victorehansone.Model.SliderItems;
import com.business.victorehansone.R;
import com.business.victorehansone.Util.Method;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {
    private List<SliderItems> sliderItems;
    private Activity activity;
    Method method;
    AlertDialog.Builder builder;

    public SliderAdapter(List<SliderItems> sliderItems, Activity activity) {
        this.sliderItems = sliderItems;
        this.activity = activity;

    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.slide_item_container, parent, false) );
    }
    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final SliderItems myListData = sliderItems.get(position);
        method=new Method(activity);
        builder = new AlertDialog.Builder(activity);
//        holder.imageView.setImageResource(sliderItems.get(position).getImage());
        Glide.with(activity).load(sliderItems.get(position).getImage()).into(holder.imageView);
        holder.ic_userPinCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sliderItems.get(position).getVDH().equals("sorry"))
                {

                    Intent intent = new Intent(activity, BlogDetailActivity.class);
                    intent.putExtra("Id", sliderItems.get(position).getId());
                    intent.putExtra("titlename", sliderItems.get(position).getHeadline());
                    intent.putExtra("Type","Search");
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.right_enter, R.anim.left_out);

                }
                else
                {




                    builder.setTitle("VDH Ultra");
                    builder.setMessage("This Content is only available for our VDH Ultra members. If you are an existing member, Please click on login button below to access this content.\n" +
                            "\n" +
                            "To become a new member, Click on Subscribe button below.")
                            .setCancelable(false)
                            .setPositiveButton(Html.fromHtml("<font color='#262626'>Yes</font>"), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {


//                                    method.editor.putBoolean(method.pref_login, false);
//                                    method.editor.clear();
//                                    method.editor.commit();
                                    Intent intent = new Intent(activity, LoginActivity.class);
                                    activity.overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                                    activity.startActivity(intent);
                                    activity.finish();

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
                    alert.setTitle(Html.fromHtml("<font color='#262626'>VDH Ultra</font>"));
                    alert.show();


//                    Intent intent = new Intent(activity, LoginActivity.class);
//                    activity.startActivity(intent);
//                    activity.overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                }






            }
        });


    }
    @Override
    public int getItemCount() {
        return sliderItems.size();
    }
    class SliderViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private RelativeLayout ic_userPinCardView;
        SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            ic_userPinCardView=itemView.findViewById(R.id.ic_userPinCardView);
        }

    }

}
