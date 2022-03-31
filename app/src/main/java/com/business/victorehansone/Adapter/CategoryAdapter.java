package com.business.victorehansone.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.business.victorehansone.Activity.CategoryPostActivity;
import com.business.victorehansone.Model.CategoryModel;
import com.business.victorehansone.Model.HomeModel;
import com.business.victorehansone.R;
import com.business.victorehansone.Util.Apis;
import com.business.victorehansone.Util.Method;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import cz.msebera.android.httpclient.Header;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private List<CategoryModel> listdata;
    private Activity activity;
    public static Dialog bottomSheetDialogg;
    Method method;
    TextView nodat;
    TextView send_btn;
    ProgressBar progressBardialog;
    public static String ID = "";
    public static String TYPE = "";

    public CategoryAdapter(Activity activity, List<CategoryModel> listdata) {
        this.activity = activity;
        this.listdata = listdata;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.home_list, parent, false);
        return new ViewHolder(view);

    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final CategoryModel myListData = listdata.get(position);
        Random rnd = new Random();
        int currentColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        method = new Method(activity);

        //holder.image.setBackgroundTintList(ColorStateList.valueOf(currentColor));
        //holder.image.setColorFilter(activity.getResources().getColor(currentColor));
       // holder.image.setBackgroundTintList(ContextCompat.getColorStateList(activity, currentColor));
        holder.name.setText(myListData.getName());


        holder.ic_userPinCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity, CategoryPostActivity.class);
                intent.putExtra("iddd",myListData.getId());
                intent.putExtra("name",myListData.getName());
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.right_enter,R.anim.left_out);
            }
        });

        if (position == 0)
        {
            holder.image.setBackground(ContextCompat.getDrawable(activity, R.drawable.fifthbanner));
        }
        else if (position ==1 )
        {
            holder.image.setBackground(ContextCompat.getDrawable(activity, R.drawable.secondbanner));

        }
        else if (position == 2)
        {
            holder.image.setBackground(ContextCompat.getDrawable(activity, R.drawable.secondbanner));

        }
        else if (position == 3)
        {
            holder.image.setBackground(ContextCompat.getDrawable(activity, R.drawable.fifthbanner));

        }
        else if (position == 4)
        {
            holder.image.setBackground(ContextCompat.getDrawable(activity, R.drawable.fifthbanner));

        }
        else if (position == 5)
        {
            holder.image.setBackground(ContextCompat.getDrawable(activity, R.drawable.secondbanner));

        }
        else if (position == 6)
        {
            holder.image.setBackground(ContextCompat.getDrawable(activity, R.drawable.secondbanner));

        }
        else if (position == 7)
        {
            holder.image.setBackground(ContextCompat.getDrawable(activity, R.drawable.fifthbanner));

        }
        else
        {
            holder.image.setBackground(ContextCompat.getDrawable(activity, R.drawable.fourthbanner));

        }



        if (method.pref.getInt(String.valueOf(method.dec),0) == 0)
        {
            holder.name.setTextSize(14);
        }
        else
        {
            holder.name.setTextSize(method.pref.getInt(String.valueOf(method.dec),0));
        }


    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView image;
        CardView ic_userPinCardView;
        LinearLayout planelyt;

        public ViewHolder(View itemView) {
            super(itemView);


            this.image = (ImageView) itemView.findViewById(R.id.image);
            this.name = (TextView) itemView.findViewById(R.id.name);
            this.ic_userPinCardView=(CardView) itemView.findViewById(R.id.ic_userPinCardView);


        }
    }


}