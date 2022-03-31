package com.business.victorehansone.Adapter;

import static com.business.victorehansone.Util.Apis.ROOT_URLL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.business.victorehansone.Activity.BlogDetailActivity;
import com.business.victorehansone.Activity.CategoryPostActivity;
import com.business.victorehansone.Activity.LoginActivity;
import com.business.victorehansone.Activity.SubscribeActivity;
import com.business.victorehansone.Fragment.HomeFragment;
import com.business.victorehansone.Model.HomeModel;
import com.business.victorehansone.Model.SavePostModel;
import com.business.victorehansone.R;
import com.business.victorehansone.Util.Apis;
import com.business.victorehansone.Util.Method;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class SavePostAdapter extends RecyclerView.Adapter<SavePostAdapter.ViewHolder> {
    private List<SavePostModel> listdata;
    private Activity activity;
    private final List<SavePostModel> filteredUserList;
    private UserFilter userFilter;
    Method method;
    public static String tot_comments="";
    private List<Integer> numbers;
    private final int max = 30;
    AlertDialog.Builder builder;
    public static int textSize;
    public SavePostAdapter(Activity activity, List<SavePostModel> listdata) {
        this.activity = activity;
        this.listdata = listdata;

        this.filteredUserList = new ArrayList<>();
    }

    private void generateNumbers()
    {
        numbers = new ArrayList<>();

        for(int x=1;x<=max;x++)
        {
            numbers.add(x);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.savepost_list, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        final SavePostModel myListData = listdata.get(position);
        method = new Method(activity);
        builder = new AlertDialog.Builder(activity);



        AsyncHttpClient client = new AsyncHttpClient();
        String Url = ROOT_URLL + activity.getString(R.string.comment_list_api) + "post=" + listdata.get(position).getId();
        client.addHeader("Authorization","Basic dmhhZG1pbjpWSEBBZG1pbjEyMyE=");
        Log.e("comments string","-------------------> " + Url);
        client.get(Url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                if (activity != null) {
                    Log.d("Response", new String(responseBody));
                    String res = new String(responseBody);
                    Log.e("total comments","-------------------> " + res.toString());
                    try {
                        JSONArray jsonArray = new JSONArray(res);
                        if (jsonArray != null) {
                            tot_comments = String.valueOf(jsonArray.length());
                            Log.e("commenttxt", String.valueOf(tot_comments));
                            holder.comment_txt.setText(tot_comments+" Comments");
                            //holder.comment_txt.setText(tot_comments);
                        } else {
                            tot_comments = String.valueOf(0);
                            //holder.comment_txt.setText(tot_comments);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });




        holder.vdh_txt.setText(listdata.get(position).getVDH());
        holder.date_txt.setText(date_formatnew(listdata.get(position).getDate()));
    //    holder.comment_txt.setText(listdata.get(position).getComment());
        holder.news_txt.setText(Html.fromHtml(listdata.get(position).getNews(), new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                Log.e("jdsfhdsif", source.toString());
                //   holder.image.setImageDrawable(Drawable.createFromPath(source));

                return null;
            }
        }, null));
        holder.headline_txt.setText(Html.fromHtml(listdata.get(position).getHeadline()));
        Glide.with(activity).load(listdata.get(position).getImage()).into(holder.image);
        Log.e("sdfsdfsd", holder.vdh_txt.getText().toString());

        if (holder.vdh_txt.getText().toString().equals("sorry")) {
            holder.UDH_layout.setVisibility(View.GONE);
        } else {
            holder.UDH_layout.setVisibility(View.VISIBLE);
        }
        textSize= (int) holder.news_txt.getTextSize();

        if (method.pref.getInt(String.valueOf(method.inc),0) == 0)
        {
            holder.news_txt.setTextSize(12);

        }
        else
        {
            holder.news_txt.setTextSize(method.pref.getInt(String.valueOf(method.inc),0));

        }

        if (method.pref.getInt(String.valueOf(method.dec),0) == 0)
        {
            holder.headline_txt.setTextSize(14);
        }
        else
        {
            holder.news_txt.setTextSize(method.pref.getInt(String.valueOf(method.dec),0));
        }

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(method.pref.getBoolean(method.pref_login, false))
                {
                    if (method.pref.getString(method.current_plane,null).equals("free_subscription") || method.pref.getString(method.current_plane,null).equals(""))
                    {
                        if (holder.UDH_layout.getVisibility()==View.VISIBLE) {


                            builder.setTitle("VDH Ultra");
                            builder.setMessage("This Content is only available for our VDH Ultra members.\n" +
                                    "\n" +
                                    "To become a new member, Click on Subscribe button below.")
                                    .setCancelable(false)
                                    .setPositiveButton(Html.fromHtml("<font color='#262626'>Yes</font>"), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {





                                            Intent intent = new Intent(activity, SubscribeActivity.class);
                                                                                   activity.startActivity(intent);
                                            activity.overridePendingTransition(R.anim.right_enter, R.anim.left_out);

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
                        else
                        {

                            Log.e("nameeeeee",holder.vdh_txt.getText().toString());
                            Log.e("nameeeeee",holder.vdh_txt.getText().toString());
                            Intent intent = new Intent(activity, BlogDetailActivity.class);
                            intent.putExtra("Id", listdata.get(position).getId());
                            intent.putExtra("titlename", listdata.get(position).getHeadline());
                            intent.putExtra("name", CategoryPostActivity.CategoryName);
                            intent.putExtra("image",listdata.get(position).getImage());
                            intent.putExtra("date",holder.date_txt.getText().toString());
                            intent.putExtra("VDH",listdata.get(position).getVDH());
                            intent.putExtra("news",listdata.get(position).getLongnews());
                            intent.putExtra("Type","Home");
                            activity.startActivity(intent);
                            activity.overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                        }
                    }

                    else
                    {
                        Log.e("nameeeeee",holder.vdh_txt.getText().toString());
                        Log.e("nameeeeee",holder.vdh_txt.getText().toString());
                        Intent intent = new Intent(activity, BlogDetailActivity.class);
                        intent.putExtra("Id", listdata.get(position).getId());
                        intent.putExtra("titlename", listdata.get(position).getHeadline());
                        intent.putExtra("name", CategoryPostActivity.CategoryName);
                        intent.putExtra("image",listdata.get(position).getImage());
                        intent.putExtra("date",holder.date_txt.getText().toString());
                        intent.putExtra("VDH",listdata.get(position).getVDH());
                        intent.putExtra("news",listdata.get(position).getLongnews());
                        intent.putExtra("Type","Home");
                        activity.startActivity(intent);
                        activity.overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                    }
                }
                else
                {
                    if (holder.UDH_layout.getVisibility()==View.VISIBLE) {


                        builder.setTitle("VDH Ultra");
                        builder.setMessage("This Content is only available for our VDH Ultra members. If you are an existing member, Please click on login button below to access this content.\n" +
                                "\n" +
                                "To become a new member, Click on Subscribe button below.")
                                .setCancelable(false)
                                .setPositiveButton(Html.fromHtml("<font color='#262626'>Yes</font>"), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {


                                    method.editor.putBoolean(method.pref_login, false);
                                    method.editor.clear();
                                    method.editor.commit();



                                        Intent intent = new Intent(activity, LoginActivity.class);
                                        activity.startActivity(intent);
                                        activity.overridePendingTransition(R.anim.right_enter, R.anim.left_out);

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
                    else
                    {

                        Log.e("nameeeeee",holder.vdh_txt.getText().toString());
                        Log.e("nameeeeee",holder.vdh_txt.getText().toString());
                        Intent intent = new Intent(activity, BlogDetailActivity.class);
                        intent.putExtra("Id", listdata.get(position).getId());
                        intent.putExtra("titlename", listdata.get(position).getHeadline());
                        intent.putExtra("name", CategoryPostActivity.CategoryName);
                        intent.putExtra("image",listdata.get(position).getImage());
                        intent.putExtra("date",holder.date_txt.getText().toString());
                        intent.putExtra("VDH",listdata.get(position).getVDH());
                        intent.putExtra("news",listdata.get(position).getLongnews());
                        intent.putExtra("Type","Home");
                        activity.startActivity(intent);
                        activity.overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                    }
                }




            }
        });
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody = listdata.get(position).getLink();
                String shareSub = "Your subject";
                myIntent.putExtra(Intent.EXTRA_SUBJECT, shareBody);
                myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                activity.startActivity(Intent.createChooser(myIntent, "Share using"));
            }
        });


    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public static TextView vdh_txt, date_txt, comment_txt, headline_txt, news_txt;
        ImageView image,share,save_button,unsave_button;
        RelativeLayout layout,UDH_layout;
        LinearLayout planelyt;

        public ViewHolder(View itemView) {
            super(itemView);
          this.image=(ImageView) itemView.findViewById(R.id.image);

            this.vdh_txt = (TextView) itemView.findViewById(R.id.vdh_txt);
            this.date_txt = (TextView) itemView.findViewById(R.id.date_txt);
            this.comment_txt = (TextView) itemView.findViewById(R.id.comment_txt);
            this.headline_txt = (TextView) itemView.findViewById(R.id.headline_txt);
            this.news_txt = (TextView) itemView.findViewById(R.id.news_txt);
            this.UDH_layout=(RelativeLayout) itemView.findViewById(R.id.UDH_layout);
            this.layout=(RelativeLayout) itemView.findViewById(R.id.layout);
            this.share=(ImageView) itemView.findViewById(R.id.share);
            this.unsave_button=(ImageView) itemView.findViewById(R.id.unsave_button);
            this.save_button=(ImageView) itemView.findViewById(R.id.save_button);
         //   this.starButton=(LikeButton) itemView.findViewById(R.id.heart_button);
            ButterKnife.bind(this, itemView);
        }
    }


    public String date_formatnew(String strDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date varDate = dateFormat.parse(strDate);
//            dateFormat=new SimpleDateFormat("dd MMM EEE yyyy");
            dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
            System.out.println("Date :" + dateFormat.format(varDate));
            return dateFormat.format(varDate);
        } catch (Exception e) {
            e.printStackTrace();
            return strDate;
        }


    }
    private static class UserFilter extends Filter {

        private final SavePostAdapter adapter;

        private final List<HomeModel> originalList;

        private final List<HomeModel> filteredList;

        private UserFilter(SavePostAdapter adapter, List<HomeModel> originalList) {
            super();
            this.adapter = adapter;
            this.originalList = new LinkedList<>(originalList);
            this.filteredList = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            final FilterResults results = new FilterResults();

            if (constraint.length() == 0) {
                filteredList.addAll(originalList);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();

                for (final HomeModel user : originalList) {
                    if (user.getHeadline().contains(filterPattern)) {
                        filteredList.add(user);
                    }
                }
            }
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.filteredUserList.clear();
            adapter.filteredUserList.addAll((ArrayList<SavePostModel>) results.values);
            adapter.notifyDataSetChanged();
        }
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }




}