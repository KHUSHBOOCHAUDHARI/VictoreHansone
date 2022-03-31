package com.business.victorehansone.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.business.victorehansone.Activity.BlogDetailActivity;
import com.business.victorehansone.Activity.CategoryPostActivity;
import com.business.victorehansone.Activity.OfferDetailActivity;
import com.business.victorehansone.Model.CategoryModel;
import com.business.victorehansone.Model.OfferModel;
import com.business.victorehansone.R;
import com.business.victorehansone.Util.Method;

import java.util.List;
import java.util.Random;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.ViewHolder> {
    private List<OfferModel> listdata;
    private Activity activity;
    public static Dialog bottomSheetDialogg;
    Method method;
    TextView nodat;
    TextView send_btn;
    ProgressBar progressBardialog;
    public static String ID = "";
    public static String TYPE = "";

    public OfferAdapter(Activity activity, List<OfferModel> listdata) {
        this.activity = activity;
        this.listdata = listdata;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.offerlist_list, parent, false);
        return new ViewHolder(view);

    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final OfferModel myListData = listdata.get(position);
        Random rnd = new Random();
        int currentColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        method = new Method(activity);
        holder.title.setText(myListData.getName());
        holder.date.setText(myListData.getDetail());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed()) {
            return;
        } else {
            Glide.with(activity).load(myListData.getImage()).into(holder.imageView_pro);

        }
        holder.ic_userPinCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity, OfferDetailActivity.class);
                intent.putExtra("image",myListData.getImage());
                intent.putExtra("detail",myListData.getDetail());
                intent.putExtra("title",myListData.getName());
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.right_enter,R.anim.left_out);
            }
        });



    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title,date;
        ImageView imageView_pro;
        CardView ic_userPinCardView;
        LinearLayout planelyt;

        public ViewHolder(View itemView) {
            super(itemView);


            this.title = (TextView) itemView.findViewById(R.id.title);
            this.date = (TextView) itemView.findViewById(R.id.date);
            this.ic_userPinCardView=(CardView) itemView.findViewById(R.id.ic_userPinCardView);
            this.imageView_pro=(ImageView) itemView.findViewById(R.id.imageView_pro);


        }
    }


}