package com.business.victorehansone.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.business.victorehansone.Model.CommentModel;
import com.business.victorehansone.R;
import com.business.victorehansone.Util.Method;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private List<CommentModel> listdata;
    private Activity activity;
    Method method;
    public static Dialog bottomSheetDialogg;
    public CommentAdapter(Activity activity, List<CommentModel> listdata) {
        this.activity = activity;
        this.listdata = listdata;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.comment_list, parent, false);
        return new ViewHolder(view);

    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final CommentModel myListData = listdata.get(position);
        method=new Method(activity);
        holder.name.setText(myListData.getName());
        holder.date.setText(date_formatnew(myListData.getDate()));
        holder.comment.setText(Html.fromHtml(myListData.getComment()));
        Glide.with(activity).load(listdata.get(position).getImage()).into(holder.image);

        holder.reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialogg = new Dialog(activity) {
                    @Override
                    public boolean onTouchEvent(MotionEvent event) {
                        // Tap anywhere to close dialog.
                        bottomSheetDialogg.dismiss();
                        return true;
                    }
                };
                bottomSheetDialogg.requestWindowFeature(Window.FEATURE_NO_TITLE);
                bottomSheetDialogg.setContentView(R.layout.dialog_filter);
                TextView filterRecy = bottomSheetDialogg.findViewById(R.id.name_txt);
                EditText message = bottomSheetDialogg.findViewById(R.id.message);
                ProgressBar progressBardialog = bottomSheetDialogg.findViewById(R.id.progressBardialog);
                TextView send_btn = bottomSheetDialogg.findViewById(R.id.send_btn);
                filterRecy.setText(listdata.get(position).getName());
                ImageView cancle=bottomSheetDialogg.findViewById(R.id.cancle);
                send_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String Message= message.getText().toString();
                        if (Message.isEmpty() || Message.equalsIgnoreCase("")) {
                            message.requestFocus();
                            message.setError("Please enter Message");

                        }

                        else
                        {
                            bottomSheetDialogg.dismiss();
                        }

                    }
                });
                cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialogg.dismiss();
                    }
                });




                bottomSheetDialogg.show();
                bottomSheetDialogg.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                bottomSheetDialogg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                bottomSheetDialogg.getWindow().getAttributes().windowAnimations = R.style.Dialoganimation;
                bottomSheetDialogg.getWindow().setGravity(Gravity.CENTER);

            }
        });



    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,date,comment,reply;
        ImageView image;
        CardView ic_userPinCardView;
        LinearLayout planelyt;

        public ViewHolder(View itemView) {
            super(itemView);


            this.image = (ImageView) itemView.findViewById(R.id.imageView_pro);
            this.name = (TextView) itemView.findViewById(R.id.name);
            this.date=(TextView) itemView.findViewById(R.id.date);
            this.comment=(TextView) itemView.findViewById(R.id.comment);
            this.reply=(TextView) itemView.findViewById(R.id.reply);


        }
    }
    public String date_formatnew(String strDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Date varDate = dateFormat.parse(strDate);
//            dateFormat=new SimpleDateFormat("dd MMM EEE yyyy");
            dateFormat = new SimpleDateFormat("MMMM,dd,yyyy HH:mm:ss.SS");
            System.out.println("Date :" + dateFormat.format(varDate));
            return dateFormat.format(varDate);
        } catch (Exception e) {
            e.printStackTrace();
            return strDate;
        }


    }


    public void AlertDialogFilter() {


    }

}