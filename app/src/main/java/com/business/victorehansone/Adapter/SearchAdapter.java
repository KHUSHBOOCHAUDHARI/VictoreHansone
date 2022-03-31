package com.business.victorehansone.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.business.victorehansone.Activity.BlogDetailActivity;
import com.business.victorehansone.Activity.CategoryPostActivity;
import com.business.victorehansone.Activity.LoginActivity;
import com.business.victorehansone.Activity.SearchActivity;
import com.business.victorehansone.Model.SearechModel;
import com.business.victorehansone.R;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>implements Filterable {
    private List<SearechModel> listdata;
    private Activity activity;
    String RECEIVED_VALUE="";
    private List<SearechModel> listdatafull;
    AlertDialog.Builder builder;
    public SearchAdapter(Activity activity, List<SearechModel> listdata) {
        this.activity = activity;
        this.listdata = listdata;
        listdatafull=new ArrayList<>(listdata);


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.search_list_item, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final SearechModel myListData = listdata.get(position);
        holder.title.setText(Html.fromHtml(listdata.get(position).getTitle()));
        builder = new AlertDialog.Builder(activity);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listdata.get(position).getVDH().equals("sorry"))
                {

                    Intent intent = new Intent(activity, BlogDetailActivity.class);
                    intent.putExtra("Id", listdata.get(position).getId());
                    intent.putExtra("titlename", listdata.get(position).getTitle());
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
        return listdata.size();
    }
    @Override
    public Filter getFilter() {
        return listfilter;
    }

    private Filter listfilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<SearechModel> filterList=new ArrayList<>();
            if (charSequence ==null || charSequence.length()==0)
            {
                filterList.addAll(listdatafull);
            }
            else
            {
                String filterpath=charSequence.toString().toLowerCase().trim();
                for (SearechModel item:listdatafull)
                {
                    if (item.getTitle().toLowerCase().contains(filterpath))
                    {
                        filterList.add(item);
                    }
//                    if (item.getPrefix_name().toLowerCase().contains(filterpath))
//                    {
//                        filterList.add(item);
//                    }
                }

            }
            FilterResults filterResults=new FilterResults();
            filterResults.values=filterList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            listdata.clear();
            listdata.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }

    };

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        CardView layout;
        TextView received_txt;
        RelativeLayout layoutt,UDH_layout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.layout = (CardView) itemView.findViewById(R.id.layout);
            this.title = (TextView) itemView.findViewById(R.id.title);


        }
    }
}