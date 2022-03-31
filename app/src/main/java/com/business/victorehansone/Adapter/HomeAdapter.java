package com.business.victorehansone.Adapter;

import static com.business.victorehansone.Activity.MainActivity.textSize;
import static com.business.victorehansone.Util.Apis.ROOT_URLL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.telephony.CellSignalStrength;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.business.victorehansone.Activity.BlogDetailActivity;
import com.business.victorehansone.Activity.CategoryPostActivity;
import com.business.victorehansone.Activity.LoginActivity;
import com.business.victorehansone.Activity.MainActivity;
import com.business.victorehansone.Activity.SavePostListActivity;
import com.business.victorehansone.Activity.SubscribeActivity;
import com.business.victorehansone.Fragment.HomeFragment;
import com.business.victorehansone.Model.CommentModel;
import com.business.victorehansone.Model.HomeModel;

import com.business.victorehansone.R;
import com.business.victorehansone.Util.Apis;
import com.business.victorehansone.Util.Method;
import com.google.android.gms.ads.AdView;
import com.like.LikeButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.rilixtech.widget.countrycodepicker.Country;


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

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private List<HomeModel> listdata = new ArrayList<>();
    private Activity activity;
    private final List<HomeModel> filteredUserList;
    Method method;
    ArrayList<String> arrayList = new ArrayList<>();
    private List<Integer> numbers;
    private final int max = 30;
    AlertDialog.Builder builder;
    public static String tot_comments = "";
    private float ourFontsize = 14f;
    public static TextView TextView;
    public static int textSize;
    public HomeAdapter(Activity activity, List<HomeModel> listdata) {
        this.activity = activity;
        this.listdata = listdata;
        this.filteredUserList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_home_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final HomeModel myListData = listdata.get(position);
        method = new Method(activity);
        builder = new AlertDialog.Builder(activity);
        AsyncHttpClient client = new AsyncHttpClient();
        String Url = ROOT_URLL + activity.getString(R.string.comment_list_api) + "post=" + listdata.get(position).getId();
        client.addHeader("Authorization", "Basic dmhhZG1pbjpWSEBBZG1pbjEyMyE=");
        Log.e("comments string", "-------------------> " + Url);
        client.get(Url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (activity != null) {
                    Log.d("Response", new String(responseBody));
                    String res = new String(responseBody);
                    Log.e("total comments", "-------------------> " + res.toString());
                    try {
                        JSONArray jsonArray = new JSONArray(res);
                        if (jsonArray != null) {
                            tot_comments = String.valueOf(jsonArray.length());
                            Log.e("commenttxt", String.valueOf(tot_comments));
                            holder.comment_txt.setText(tot_comments + " Comments");

                            if (!TextUtils.isEmpty(method.pref.getString(method.layoutvalue,null))) {
                                if (method.pref.getString(method.layoutvalue, null).equals("2")) {
                                    holder.comment_txt.setEllipsize(TextUtils.TruncateAt.END);
                                    CharSequence comment_txt = holder.comment_txt.getText();
                                    String comment_txtbar = comment_txt.toString();
                                    String comment_txtdesiredString = comment_txtbar.substring(0, 3);
                                    holder.comment_txt.setText(comment_txtdesiredString);
                                }
                            }



                      //      holder.comment_txt.setText(tot_comments);
                        } else {
                            tot_comments = String.valueOf(0);
                            if (method.pref.getString(method.layoutvalue,null).equals("2")) {
                                holder.comment_txt.setEllipsize(TextUtils.TruncateAt.END);
                                CharSequence comment_txt = holder.comment_txt.getText();
                                String comment_txtbar = comment_txt.toString();
                                String comment_txtdesiredString = comment_txtbar.substring(0, 3);
                                holder.comment_txt.setText(comment_txtdesiredString);
                            }



//                                if (method.pref.getString(method.layoutvalue,null).equals("2"))
//                                {
//                                    holder.news_txt.setEllipsize(TextUtils.TruncateAt.END);
//                                    holder.news_txt.setMaxLines(4);
//                                    holder.vdh_txt.setEllipsize(TextUtils.TruncateAt.END);
//                                    CharSequence vdh_txt = holder.vdh_txt.getText();
//                                    String bar = vdh_txt.toString();
//                                    String desiredString = bar.substring(0,3);
//
//                                    holder.vdh_txt.setText(desiredString);
//                                    holder.date_txt.setEllipsize(TextUtils.TruncateAt.END);
//                                    CharSequence date_txt = holder.date_txt.getText();
//                                    String date_txtbar = date_txt.toString();
//                                    String date_txtdesiredString = date_txtbar.substring(0,3);
//                                    holder.date_txt.setText(date_txtdesiredString);
//                                    Log.e("jhgsjkdsa", String.valueOf(holder.comment_txt.getText().toString().length()));
//
//                                        holder.comment_txt.setEllipsize(TextUtils.TruncateAt.END);
//                                        CharSequence comment_txt = holder.comment_txt.getText();
//                                        String comment_txtbar = comment_txt.toString();
//                                        String comment_txtdesiredString = comment_txtbar.substring(0,3);
//                                        holder.comment_txt.setText(comment_txtdesiredString);
//
//
//
//
//                            }
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
        holder.news_txt.setText(Html.fromHtml(listdata.get(position).getNews(), new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                Log.e("jdsfhdsif", source.toString());
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

        Log.e("size", String.valueOf(holder.news_txt.getTextSize()));
        holder.unsave_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //    Toast.makeText(activity, "fdsdffs", Toast.LENGTH_SHORT).show();
                if (Method.isNetworkAvailable(activity)) {
                    arrayList.clear();
                    holder.progressBar.setVisibility(View.VISIBLE);
                    AsyncHttpClient client = new AsyncHttpClient();
                    RequestParams params = new RequestParams();
                    params.add("post_id", listdata.get(position).getId());
                    params.add("user_id", method.pref.getString(method.Id, null));
                    params.add("f", "post_like");
                    String url = Apis.ROOT_URLLL + activity.getString(R.string.login_ic_user);
                    String someData = "{\"post_id\":\"" + listdata.get(position).getId() + "\",\"user_id\":\"" + method.pref.getString(method.Id, null) + "\",\"f\":\"" + "post_like" + "\"}";
                    StringEntity entity = null;
                    try {
                        entity = new StringEntity(someData);
                        entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                    } catch (Exception e) {

                    }

                    client.addHeader("api-token", "4db248bc10877bef6f3008ef64e5c76a");
                    Log.e("Url------->", url);
                    client.post(activity, url, entity, "application/json", new AsyncHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            Log.d("Response", new String(responseBody));
                            String res = new String(responseBody);
                            Log.e("Url------->", res.toString());
                            try {
                                JSONObject jsonObject = new JSONObject(res);
                                String status = jsonObject.getString("status");
                                if (status.equalsIgnoreCase("200")) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        String post_id = jsonObject1.getString("post_id");
//                                        holder.unsave_button.setVisibility(View.GONE);
//                                        holder.save_button.setVisibility(View.VISIBLE);
                                        arrayList.add(post_id);
                                    }
                                    builder.setTitle("Save");
                                    builder.setCancelable(false);
                                    builder.setMessage("Post Saved Successfully!")
                                            .setPositiveButton(Html.fromHtml("<font color='#262626'>Ok</font>"), new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            })
                                            .setNegativeButton(Html.fromHtml("<font color='#262626'>View</font>"), new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    //  Action for 'NO' Button
                                                    dialog.cancel();
                                                    Intent ii = new Intent(activity, SavePostListActivity.class);
                                                    activity.startActivity(ii);
                                                    activity.overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                                                }
                                            });
                                    //Creating dialog box
                                    AlertDialog alert = builder.create();
                                    //Setting the title manually
                                    alert.setTitle(Html.fromHtml("<font color='#262626'>Save</font>"));
                                    alert.show();
                                    String s = TextUtils.join(",", arrayList);
                                    method.editor.putString(method.post_list_add, s.toString());
                                    method.editor.putBoolean(method.pref_login, true);
                                    method.editor.commit();
//                                String message = String.valueOf(Html.fromHtml(jsonObject.getString("message")));
//                                Toast.makeText(activity, message.toString(), Toast.LENGTH_SHORT).show();
                                } else {
                                    holder.progressBar.setVisibility(View.GONE);
                                    String message = String.valueOf(Html.fromHtml(jsonObject.getString("message")));
                                    method.alertBox(message);
                                }
                                holder.progressBar.setVisibility(View.GONE);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                holder.progressBar.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            holder.progressBar.setVisibility(View.GONE);
//                            method.alertBox(activity.getResources().getString(R.string.something_went_wrong));
                        }
                    });

                } else {
                    method.alertBox(activity.getResources().getString(R.string.internet_connection));
                }
            }

        });
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (method.pref.getBoolean(method.pref_login, false)) {
                 //   holder.unsave_button.setVisibility(View.VISIBLE);
                    if (method.pref.getString(method.current_plane, null).equals("free_subscription") || method.pref.getString(method.current_plane, null).equals("")) {
                        if (holder.UDH_layout.getVisibility() == View.VISIBLE) {


                            builder.setTitle("Alert");
                            builder.setMessage("This Content is only available for our VDH Ultra members.\n" +
                                    "\n" +
                                    "To become a new member, Click on Subscribe button below.")
                                    .setCancelable(false)
                                    .setPositiveButton(Html.fromHtml("<font color='#262626'>Ok</font>"), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {


                                            Intent intent = new Intent(activity, SubscribeActivity.class);
                                            activity.startActivity(intent);
                                            activity.overridePendingTransition(R.anim.right_enter, R.anim.left_out);


                                        }
                                    })
                                    .setNegativeButton(Html.fromHtml("<font color='#262626'>Cancel</font>"), new DialogInterface.OnClickListener() {
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
                        } else {

                            Log.e("nameeeeee", holder.vdh_txt.getText().toString());
                            Log.e("nameeeeee", holder.vdh_txt.getText().toString());
                            Intent intent = new Intent(activity, BlogDetailActivity.class);
                            intent.putExtra("Id", listdata.get(position).getId());
                            intent.putExtra("titlename", listdata.get(position).getHeadline());
                            intent.putExtra("name", CategoryPostActivity.CategoryName);
                            intent.putExtra("image", listdata.get(position).getImage());
                            intent.putExtra("date", holder.date_txt.getText().toString());
                            intent.putExtra("VDH", listdata.get(position).getVDH());
                            intent.putExtra("news", listdata.get(position).getLongnews());
                            intent.putExtra("link", listdata.get(position).getLink());
                            intent.putExtra("Type", "Home");
                            activity.startActivity(intent);
                            activity.overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                        }
                    } else {
                        Log.e("nameeeeee", holder.vdh_txt.getText().toString());
                        Log.e("nameeeeee", holder.vdh_txt.getText().toString());
                        Intent intent = new Intent(activity, BlogDetailActivity.class);
                        intent.putExtra("Id", listdata.get(position).getId());
                        intent.putExtra("titlename", listdata.get(position).getHeadline());
                        intent.putExtra("name", CategoryPostActivity.CategoryName);
                        intent.putExtra("image", listdata.get(position).getImage());
                        intent.putExtra("date", holder.date_txt.getText().toString());
                        intent.putExtra("VDH", listdata.get(position).getVDH());
                        intent.putExtra("news", listdata.get(position).getLongnews());
                        intent.putExtra("Type", "Home");
                        activity.startActivity(intent);
                        activity.overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                    }


                } else {

//                    holder.save_button.setVisibility(View.GONE);
//                    holder.unsave_button.setVisibility(View.GONE);
                    if (holder.UDH_layout.getVisibility() == View.VISIBLE) {
//                        holder.save_button.setVisibility(View.GONE);
//                        holder.unsave_button.setVisibility(View.GONE);

                        builder.setTitle("Alert");
                        builder.setMessage("This Content is only available for our VDH Ultra members. If you are an existing member, Please click on login button below to access this content.\n" +
                                "\n" +
                                "To become a new member, Click on Subscribe button below.")
                                .setCancelable(false)
                                .setPositiveButton(Html.fromHtml("<font color='#262626'>Ok</font>"), new DialogInterface.OnClickListener() {
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
                                .setNegativeButton(Html.fromHtml("<font color='#262626'>Cancel</font>"), new DialogInterface.OnClickListener() {
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

                    } else {
//                        holder.save_button.setVisibility(View.GONE);
//                        holder.unsave_button.setVisibility(View.GONE);
                        Log.e("nameeeeee", holder.vdh_txt.getText().toString());
                        Log.e("nameeeeee", holder.vdh_txt.getText().toString());
                        Intent intent = new Intent(activity, BlogDetailActivity.class);
                        intent.putExtra("Id", listdata.get(position).getId());
                        intent.putExtra("titlename", listdata.get(position).getHeadline());
                        intent.putExtra("name", CategoryPostActivity.CategoryName);
                        intent.putExtra("image", listdata.get(position).getImage());
                        intent.putExtra("date", holder.date_txt.getText().toString());
                        intent.putExtra("VDH", listdata.get(position).getVDH());
                        intent.putExtra("news", listdata.get(position).getLongnews());
                        intent.putExtra("Type", "Home");
                        activity.startActivity(intent);
                        activity.overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                    }
                }
            }
        });
        Log.e("titlesize", String.valueOf(method.pref.getInt(String.valueOf(method.inc),0)));
        Log.e("headlinesize", String.valueOf(method.pref.getInt(String.valueOf(method.dec),0)));
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
            holder.headline_txt.setTextSize(method.pref.getInt(String.valueOf(method.dec),0));
        }
        if (!TextUtils.isEmpty(method.pref.getString(method.layoutvalue,null))) {
            if (method.pref.getString(method.layoutvalue, null).equals("2")) {
                holder.news_txt.setEllipsize(TextUtils.TruncateAt.END);
                holder.news_txt.setMaxLines(4);
                holder.vdh_txt.setEllipsize(TextUtils.TruncateAt.END);
                CharSequence vdh_txt = holder.vdh_txt.getText();
                String bar = vdh_txt.toString();
                String desiredString = bar.substring(0, 3);
                holder.vdh_txt.setText(desiredString);

                holder.date_txt.setEllipsize(TextUtils.TruncateAt.END);
                CharSequence date_txt = holder.date_txt.getText();
                String date_txtbar = date_txt.toString();
                String date_txtdesiredString = date_txtbar.substring(0, 3);
                holder.date_txt.setText(date_txtdesiredString);
                Log.e("jhgsjkdsa", String.valueOf(holder.comment_txt.getText().toString().length()));
            }
        }


    }





    @Override
    public int getItemCount() {
        return listdata.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView vdh_txt, date_txt, comment_txt, headline_txt, news_txt, layout;
        ImageView image, share, save_button, unsave_button,upButton,downButton;
        RelativeLayout UDH_layout;
        LinearLayout planelyt;
        ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.image);
            vdh_txt = (TextView) itemView.findViewById(R.id.vdh_txt);
            date_txt = (TextView) itemView.findViewById(R.id.date_txt);
            comment_txt = (TextView) itemView.findViewById(R.id.comment_txt);
            headline_txt = (TextView) itemView.findViewById(R.id.headline_txt);
            news_txt = (TextView) itemView.findViewById(R.id.news_txt);
            UDH_layout = (RelativeLayout) itemView.findViewById(R.id.UDH_layout);
            layout = (TextView) itemView.findViewById(R.id.continuew_txt);
            share = (ImageView) itemView.findViewById(R.id.share);
            unsave_button = (ImageView) itemView.findViewById(R.id.unsave_button);
            save_button = (ImageView) itemView.findViewById(R.id.save_button);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
            upButton=itemView.findViewById(R.id.inc);
            downButton= itemView.findViewById(R.id.dec);

        }
    }


    public String date_formatnew(String strDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date varDate = dateFormat.parse(strDate);
//            dateFormat=new SimpleDateFormat("dd MMM EEE yyyy");
            dateFormat = new SimpleDateFormat("MMM dd, yyyy");
            System.out.println("Date :" + dateFormat.format(varDate));
            return dateFormat.format(varDate);
        } catch (Exception e) {
            e.printStackTrace();
            return strDate;
        }


    }

    private static class UserFilter extends Filter {

        private final HomeAdapter adapter;

        private final List<HomeModel> originalList;

        private final List<HomeModel> filteredList;

        private UserFilter(HomeAdapter adapter, List<HomeModel> originalList) {
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
            adapter.filteredUserList.addAll((ArrayList<HomeModel>) results.values);
            adapter.notifyDataSetChanged();
        }
    }


}