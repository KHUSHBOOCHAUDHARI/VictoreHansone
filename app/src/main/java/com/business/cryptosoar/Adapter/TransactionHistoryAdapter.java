package com.business.cryptosoar.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
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
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.business.cryptosoar.Fragment.WalletFragment;
import com.business.cryptosoar.Model.TransactionModel;
import com.business.cryptosoar.R;
import com.business.cryptosoar.Util.Apis;
import com.business.cryptosoar.Util.Method;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TransactionHistoryAdapter extends RecyclerView.Adapter<TransactionHistoryAdapter.ViewHolder>{
    private List<TransactionModel> listdata;
    private Activity activity;
    public static  Dialog bottomSheetDialogg;
    Method method;
    TextView nodat;
    TextView send_btn;
    ProgressBar progressBardialog;
    public static String ID="";
    public static String TYPE="";
    public TransactionHistoryAdapter(Activity activity, List<TransactionModel> listdata) {
        this.activity = activity;
        this.listdata = listdata;
        method=new Method(activity);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.all_transection_list, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final TransactionModel myListData = listdata.get(position);
         holder.title.setText(listdata.get(position).getTitle());
        holder.totalamount_txt.setText("$"+listdata.get(position).getTotal_amt());
        holder.date_txt.setText(listdata.get(position).getDate());
        holder.status.setText(listdata.get(position).getStatus());
     if (listdata.get(position).getType().equalsIgnoreCase("interest"))
        {

            holder.totalamount_lable.setText("Month");
            holder.totalamount_txt.setText(date_format(listdata.get(position).getDate()));

            holder.date_lable.setText("Interest Amount");
            holder.date_txt.setText("$"+listdata.get(position).getTotal_amt());
            holder.planelyt.setVisibility(View.GONE);

        }

        else if (listdata.get(position).getTransaction_type().equalsIgnoreCase("addfund"))
        {

            holder.plane_txt.setText(listdata.get(position).getType());
            if (listdata.get(position).getType().equalsIgnoreCase("monthly"))
            {
                holder.plane_txt.setText("Monthly");
            }
            else if (listdata.get(position).getType().equalsIgnoreCase("reinvestment"))
            {
                holder.plane_txt.setText("Reinvestment");
            }

        }
        else
        {
            holder.planelyt.setVisibility(View.GONE);
        }



        holder.UserPinCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ID=listdata.get(position).getId();
                TYPE=listdata.get(position).getTransaction_type();
                //     Toast.makeText(activity, ID.toString(), Toast.LENGTH_SHORT).show();

                if (listdata.get(position).getTitle().equalsIgnoreCase("Interest Credited"))
                {

                    bottomSheetDialogg = new Dialog(activity) {
                        @Override
                        public boolean onTouchEvent(MotionEvent event) {
                            // Tap anywhere to close dialog.
                            bottomSheetDialogg.dismiss();
                            return true;
                        }
                    };


                    bottomSheetDialogg.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    bottomSheetDialogg.setContentView(R.layout.dialog_detail);

                    nodat=bottomSheetDialogg.findViewById(R.id.nodata);
                    progressBardialog=bottomSheetDialogg.findViewById(R.id.progressBardialog);
                    send_btn = bottomSheetDialogg.findViewById(R.id.send_btn);

                    TextView paymentop_text=bottomSheetDialogg.findViewById(R.id.paymentop_text);
                    TextView paymentop_tag=bottomSheetDialogg.findViewById(R.id.paymentop_tag);
                    TextView created_tag=bottomSheetDialogg.findViewById(R.id.created_tag);
                    TextView created_text=bottomSheetDialogg.findViewById(R.id.created_text);
                    TextView totlaamount_tag=bottomSheetDialogg.findViewById(R.id.totlaamount_tag);
                    TextView totlaamount_text=bottomSheetDialogg.findViewById(R.id.totlaamount_text);
                    TextView cryptotype_tag=bottomSheetDialogg.findViewById(R.id.cryptotype_tag);
                    TextView cryptotype_text=bottomSheetDialogg.findViewById(R.id.cryptotype_text);
                    TextView protocol_tag=bottomSheetDialogg.findViewById(R.id.protocol_tag);
                    TextView protocol_text=bottomSheetDialogg.findViewById(R.id.protocol_text);
                    TextView cryptoaddress_tag=bottomSheetDialogg.findViewById(R.id.cryptoaddress_tag);
                    TextView cryptoaddress_text=bottomSheetDialogg.findViewById(R.id.cryptoaddress_text);
                    RelativeLayout cancelationlayout=bottomSheetDialogg.findViewById(R.id.cancelationlayout);
                    TextView cancelationtext=bottomSheetDialogg.findViewById(R.id.cancelation_text);
                    TextView decline_text=bottomSheetDialogg.findViewById(R.id.decline_text);
                    RelativeLayout declineMessagelayout=bottomSheetDialogg.findViewById(R.id.declineMessagelayout);



                    paymentop_tag.setText("");
                    progressBardialog.setVisibility(View.VISIBLE);
                    created_text.setText(date_formatnew(listdata.get(position).getDate()));
                    paymentop_tag.setText(listdata.get(position).getTitle());
                    totlaamount_text.setText("$"+listdata.get(position).getTotal_amt());
                    totlaamount_tag.setText("Interest Amount");
                    cryptoaddress_tag.setVisibility(View.GONE);
                    cryptotype_tag.setVisibility(View.GONE);
                    cryptotype_text.setVisibility(View.GONE);
                    protocol_text.setVisibility(View.GONE);
                    protocol_tag.setVisibility(View.GONE);
                    cryptoaddress_tag.setVisibility(View.GONE);
                    cryptotype_text.setVisibility(View.GONE);
                    cancelationlayout.setVisibility(View.GONE);
                    declineMessagelayout.setVisibility(View.GONE);




                    progressBardialog.setVisibility(View.GONE);







                    bottomSheetDialogg.show();
                    bottomSheetDialogg.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    bottomSheetDialogg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    bottomSheetDialogg.getWindow().getAttributes().windowAnimations=R.style.Dialoganimation;
                    bottomSheetDialogg.getWindow().setGravity(Gravity.CENTER);
                }
                else if (listdata.get(position).getTransaction_type().equalsIgnoreCase("withdraw"))
                {

                    bottomSheetDialogg = new Dialog(activity) {
                        @Override
                        public boolean onTouchEvent(MotionEvent event) {
                            // Tap anywhere to close dialog.
                            bottomSheetDialogg.dismiss();
                            return true;
                        }
                    };


                    bottomSheetDialogg.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    bottomSheetDialogg.setContentView(R.layout.dialog_detail);

                    nodat=bottomSheetDialogg.findViewById(R.id.nodata);
                    progressBardialog=bottomSheetDialogg.findViewById(R.id.progressBardialog);
                    send_btn = bottomSheetDialogg.findViewById(R.id.send_btn);

                    TextView paymentop_text=bottomSheetDialogg.findViewById(R.id.paymentop_text);
                    TextView paymentop_tag=bottomSheetDialogg.findViewById(R.id.paymentop_tag);
                    TextView created_tag=bottomSheetDialogg.findViewById(R.id.created_tag);
                    TextView created_text=bottomSheetDialogg.findViewById(R.id.created_text);
                    TextView totlaamount_tag=bottomSheetDialogg.findViewById(R.id.totlaamount_tag);
                    TextView totlaamount_text=bottomSheetDialogg.findViewById(R.id.totlaamount_text);
                    TextView cryptotype_tag=bottomSheetDialogg.findViewById(R.id.cryptotype_tag);
                    TextView cryptotype_text=bottomSheetDialogg.findViewById(R.id.cryptotype_text);
                    TextView protocol_tag=bottomSheetDialogg.findViewById(R.id.protocol_tag);
                    TextView protocol_text=bottomSheetDialogg.findViewById(R.id.protocol_text);
                    TextView cryptoaddress_tag=bottomSheetDialogg.findViewById(R.id.cryptoaddress_tag);
                    TextView cryptoaddress_text=bottomSheetDialogg.findViewById(R.id.cryptoaddress_text);
                    TextView bankname=bottomSheetDialogg.findViewById(R.id.bankname);
                    RelativeLayout cancelationlayout=bottomSheetDialogg.findViewById(R.id.cancelationlayout);
                    TextView cancelationtext=bottomSheetDialogg.findViewById(R.id.cancelation_text);
                    TextView decline_text=bottomSheetDialogg.findViewById(R.id.decline_text);
                    RelativeLayout declineMessagelayout=bottomSheetDialogg.findViewById(R.id.declineMessagelayout);


                    progressBardialog.setVisibility(View.VISIBLE);

                    progressBardialog.setVisibility(View.VISIBLE);
                    AsyncHttpClient client = new AsyncHttpClient();
                    RequestParams params = new RequestParams();
                    params.add("user_id", method.pref.getString(method.Id,null));
                    params.add("id", ID);
                    params.add("type", TYPE);
                    String url = Apis.ROOT_URL+ activity.getString(R.string.detail_api);
                    Log.e("Parms",params.toString());
                    Log.e("Url---->",url.toString());
                    // client.addHeader("API-TOKEN", getString(R.string.API_TOKEN));
                    client.post(url,params,new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            if (activity!= null) {
                                Log.d("Response", new String(responseBody));
                                String res = new String(responseBody);
                                try {
                                    JSONObject jsonObject = new JSONObject(res);
                                    String status = jsonObject.getString("status");
                                    if (status.equalsIgnoreCase("true")) {
                                        JSONObject jsonObject1=jsonObject.getJSONObject("data");
                                        String id = jsonObject1.getString("id");
                                        String type = jsonObject1.getString("type");
                                        String fund_plan_type = jsonObject1.getString("withdraw_type");
                                        String fund_amount = jsonObject1.getString("withdraw_amount");
                                        String document_file = jsonObject1.getString("document_file");
                                        String end_contract_charge = jsonObject1.getString("end_contract_charge");
                                        String end_contract_interest = jsonObject1.getString("end_contract_interest");

                                        String created_date = jsonObject1.getString("created_date");
                                        String payment_method_name = jsonObject1.getString("payment_method_name");
                                        String crypto_type_name = jsonObject1.getString("crypto_type_name");

                                        String crypto_type_sname = jsonObject1.getString("crypto_type_sname");
                                        String protocol_name = jsonObject1.getString("protocol_name");
                                        String admin_protocol_address = jsonObject1.getString("admin_protocol_address");
                                        String admin_bank_account_number = jsonObject1.getString("admin_bank_account_number");
                                        String admin_bank_name = jsonObject1.getString("admin_bank_name");
                                        String admin_bank_ifsc_code = jsonObject1.getString("admin_bank_ifsc_code");
                                        String admin_contact_person_name = jsonObject1.getString("admin_contact_person_name");
                                        String admin_contact_number = jsonObject1.getString("admin_contact_number");
                                        String admin_state_city = jsonObject1.getString("admin_state_city");
                                        String user_address_label = jsonObject1.getString("user_address_label");
                                        String user_crypto_address = jsonObject1.getString("user_crypto_address");
                                        String user_name = jsonObject1.getString("user_name");
                                        String user_email = jsonObject1.getString("user_email");
                                        String user_contact_person_name = jsonObject1.getString("user_contact_person_name");
                                        String user_contact_number = jsonObject1.getString("user_contact_number");
                                        String user_state_city = jsonObject1.getString("user_state_city");
                                        String decline_message=jsonObject1.getString("decline_message");


                                        if (payment_method_name.equals("Bank Transfer"))
                                        {
                                            paymentop_text.setText(payment_method_name);
                                            created_text.setText(date_formatnew(created_date));
                                            totlaamount_text.setText("$"+fund_amount);
                                            cryptotype_text.setText(admin_bank_account_number);
                                            cryptotype_tag.setText("Account Number");
                                            protocol_text.setText(admin_bank_ifsc_code);
                                            protocol_tag.setText("IFSC Code");
                                            bankname.setText(admin_bank_name);
                                            cryptoaddress_tag.setText("Bank Name");
                                            cancelationtext.setText(end_contract_charge);
                                            if (!decline_message.equals(""))
                                            {
                                                decline_text.setText(decline_message.toString());
                                                declineMessagelayout.setVisibility(View.VISIBLE);
                                            }
                                            else
                                            {
                                                declineMessagelayout.setVisibility(View.GONE);
                                            }
                                            //  Glide.with(activity).load(document_file).into(document);

                                        }
                                        else if (payment_method_name.equals("Cash"))
                                        {
                                            paymentop_text.setText(payment_method_name);
                                            created_text.setText(date_formatnew(created_date));
                                            totlaamount_text.setText("$"+fund_amount);
                                            cryptotype_text.setText(user_contact_person_name);
                                            cryptotype_tag.setText("Full Name");
                                            protocol_text.setText(user_contact_number);
                                            protocol_tag.setText("Contact Number");
                                            bankname.setText(user_state_city);
                                            cryptoaddress_tag.setText("State/City");
                                            cancelationtext.setText(end_contract_charge);
                                            if (!decline_message.equals(""))
                                            {
                                                decline_text.setText(decline_message.toString());
                                            }
                                            else
                                            {
                                                declineMessagelayout.setVisibility(View.GONE);
                                            }
                                            //Glide.with(activity).load(document_file).into(document);

                                        }
                                        else
                                        {
                                            paymentop_text.setText(payment_method_name);
                                            created_text.setText(date_formatnew(created_date));
                                            totlaamount_text.setText("$"+fund_amount);
                                            cryptotype_text.setText(crypto_type_name);
                                            protocol_text.setText(protocol_name);
                                            cryptoaddress_text.setText(admin_protocol_address);
                                            cancelationtext.setText(end_contract_charge);
                                            if (!decline_message.equals(""))
                                            {
                                                decline_text.setText(decline_message.toString());
                                                Toast.makeText(activity, decline_message.toString(), Toast.LENGTH_SHORT).show();
                                            }
                                            else
                                            {
                                                declineMessagelayout.setVisibility(View.GONE);
                                            }

                                        }















                                    }

                                    else
                                    {
                                        String Message = jsonObject.getString("message");

                                        nodat.setVisibility(View.VISIBLE);

                                    }

                                    progressBardialog.setVisibility(View.GONE);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    progressBardialog.setVisibility(View.GONE);
                                    nodat.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            progressBardialog.setVisibility(View.GONE);
                            nodat.setVisibility(View.VISIBLE);
                        }
                    });











                    bottomSheetDialogg.show();
                    bottomSheetDialogg.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    bottomSheetDialogg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    bottomSheetDialogg.getWindow().getAttributes().windowAnimations=R.style.Dialoganimation;
                    bottomSheetDialogg.getWindow().setGravity(Gravity.CENTER);
                }
                else if (listdata.get(position).getTitle().equalsIgnoreCase("Referral Bonus"))
                {

                    bottomSheetDialogg = new Dialog(activity) {
                        @Override
                        public boolean onTouchEvent(MotionEvent event) {
                            // Tap anywhere to close dialog.
                            bottomSheetDialogg.dismiss();
                            return true;
                        }
                    };


                    bottomSheetDialogg.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    bottomSheetDialogg.setContentView(R.layout.dialog_detail);

                    nodat=bottomSheetDialogg.findViewById(R.id.nodata);
                    progressBardialog=bottomSheetDialogg.findViewById(R.id.progressBardialog);
                    send_btn = bottomSheetDialogg.findViewById(R.id.send_btn);

                    TextView paymentop_text=bottomSheetDialogg.findViewById(R.id.paymentop_text);
                    TextView paymentop_tag=bottomSheetDialogg.findViewById(R.id.paymentop_tag);
                    TextView created_tag=bottomSheetDialogg.findViewById(R.id.created_tag);
                    TextView created_text=bottomSheetDialogg.findViewById(R.id.created_text);
                    TextView totlaamount_tag=bottomSheetDialogg.findViewById(R.id.totlaamount_tag);
                    TextView totlaamount_text=bottomSheetDialogg.findViewById(R.id.totlaamount_text);
                    TextView cryptotype_tag=bottomSheetDialogg.findViewById(R.id.cryptotype_tag);
                    TextView cryptotype_text=bottomSheetDialogg.findViewById(R.id.cryptotype_text);
                    TextView protocol_tag=bottomSheetDialogg.findViewById(R.id.protocol_tag);
                    TextView protocol_text=bottomSheetDialogg.findViewById(R.id.protocol_text);
                    TextView cryptoaddress_tag=bottomSheetDialogg.findViewById(R.id.cryptoaddress_tag);
                    TextView cryptoaddress_text=bottomSheetDialogg.findViewById(R.id.cryptoaddress_text);
                    RelativeLayout cancelationlayout=bottomSheetDialogg.findViewById(R.id.cancelationlayout);
                    TextView cancelationtext=bottomSheetDialogg.findViewById(R.id.cancelation_text);
                    TextView decline_text=bottomSheetDialogg.findViewById(R.id.decline_text);
                    RelativeLayout declineMessagelayout=bottomSheetDialogg.findViewById(R.id.declineMessagelayout);


                    paymentop_tag.setText("");
                    progressBardialog.setVisibility(View.VISIBLE);
                    created_text.setText(date_formatnew(listdata.get(position).getDate()));
                    paymentop_tag.setText(listdata.get(position).getTitle());
                    totlaamount_text.setText("$"+listdata.get(position).getTotal_amt());
                    totlaamount_tag.setText("Referral Amount");
                    cryptoaddress_tag.setVisibility(View.GONE);
                    cryptotype_tag.setVisibility(View.GONE);
                    cryptotype_text.setVisibility(View.GONE);
                    protocol_text.setVisibility(View.GONE);
                    protocol_tag.setVisibility(View.GONE);
                    cryptoaddress_tag.setVisibility(View.GONE);
                    cryptotype_text.setVisibility(View.GONE);
                    cancelationlayout.setVisibility(View.GONE);

                    declineMessagelayout.setVisibility(View.GONE);



                    progressBardialog.setVisibility(View.GONE);







                    bottomSheetDialogg.show();
                    bottomSheetDialogg.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    bottomSheetDialogg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    bottomSheetDialogg.getWindow().getAttributes().windowAnimations=R.style.Dialoganimation;
                    bottomSheetDialogg.getWindow().setGravity(Gravity.CENTER);
                }

                else
                {
                    bottomSheetDialogg = new Dialog(activity) {
                        @Override
                        public boolean onTouchEvent(MotionEvent event) {
                            // Tap anywhere to close dialog.
                            bottomSheetDialogg.dismiss();
                            return true;
                        }
                    };


                    bottomSheetDialogg.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    bottomSheetDialogg.setContentView(R.layout.dialog_detail);

                    nodat=bottomSheetDialogg.findViewById(R.id.nodata);
                    progressBardialog=bottomSheetDialogg.findViewById(R.id.progressBardialog);
                    send_btn = bottomSheetDialogg.findViewById(R.id.send_btn);
                    ImageView document=bottomSheetDialogg.findViewById(R.id.document);
                    document.setVisibility(View.VISIBLE);
                    TextView paymentop_text=bottomSheetDialogg.findViewById(R.id.paymentop_text);
                    TextView paymentop_tag=bottomSheetDialogg.findViewById(R.id.paymentop_tag);
                    TextView created_tag=bottomSheetDialogg.findViewById(R.id.created_tag);
                    TextView created_text=bottomSheetDialogg.findViewById(R.id.created_text);
                    TextView totlaamount_tag=bottomSheetDialogg.findViewById(R.id.totlaamount_tag);
                    TextView totlaamount_text=bottomSheetDialogg.findViewById(R.id.totlaamount_text);
                    TextView cryptotype_tag=bottomSheetDialogg.findViewById(R.id.cryptotype_tag);
                    TextView cryptotype_text=bottomSheetDialogg.findViewById(R.id.cryptotype_text);
                    TextView protocol_tag=bottomSheetDialogg.findViewById(R.id.protocol_tag);
                    TextView protocol_text=bottomSheetDialogg.findViewById(R.id.protocol_text);
                    TextView cryptoaddress_tag=bottomSheetDialogg.findViewById(R.id.cryptoaddress_tag);
                    TextView cryptoaddress_text=bottomSheetDialogg.findViewById(R.id.cryptoaddress_text);
                    TextView bankname=bottomSheetDialogg.findViewById(R.id.bankname);
                    RelativeLayout cancelationlayout=bottomSheetDialogg.findViewById(R.id.cancelationlayout);
                    TextView cancelationtext=bottomSheetDialogg.findViewById(R.id.cancelation_text);
                    TextView decline_text=bottomSheetDialogg.findViewById(R.id.decline_text);
                    RelativeLayout declineMessagelayout=bottomSheetDialogg.findViewById(R.id.declineMessagelayout);
                    progressBardialog.setVisibility(View.VISIBLE);

                    progressBardialog.setVisibility(View.VISIBLE);
                    AsyncHttpClient client = new AsyncHttpClient();
                    RequestParams params = new RequestParams();
                    params.add("user_id", method.pref.getString(method.Id,null));
                    params.add("id", ID);
                    params.add("type", TYPE);
                    String url = Apis.ROOT_URL+ activity.getString(R.string.detail_api);
                    Log.e("Parms",params.toString());
                    Log.e("Url---->",url.toString());
                    // client.addHeader("API-TOKEN", getString(R.string.API_TOKEN));
                    client.post(url,params,new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            if (activity!= null) {
                                Log.d("Response", new String(responseBody));
                                String res = new String(responseBody);
                                try {
                                    JSONObject jsonObject = new JSONObject(res);
                                    String status = jsonObject.getString("status");
                                    if (status.equalsIgnoreCase("true")) {
                                        JSONObject jsonObject1=jsonObject.getJSONObject("data");
                                        String id = jsonObject1.getString("id");
                                        String type = jsonObject1.getString("type");
                                        String fund_plan_type = jsonObject1.getString("fund_plan_type");
                                        String fund_amount = jsonObject1.getString("fund_amount");
                                        String document_file = jsonObject1.getString("document_file");
                                        String created_date = jsonObject1.getString("created_date");
                                        String payment_method_name = jsonObject1.getString("payment_method_name");
                                        String crypto_type_name = jsonObject1.getString("crypto_type_name");

                                        String crypto_type_sname = jsonObject1.getString("crypto_type_sname");
                                        String protocol_name = jsonObject1.getString("protocol_name");
                                        String admin_protocol_address = jsonObject1.getString("admin_protocol_address");
                                        String admin_bank_account_number = jsonObject1.getString("admin_bank_account_number");
                                        String admin_bank_name = jsonObject1.getString("admin_bank_name");
                                        String admin_bank_ifsc_code = jsonObject1.getString("admin_bank_ifsc_code");
                                        String admin_contact_person_name = jsonObject1.getString("admin_contact_person_name");
                                        String admin_contact_number = jsonObject1.getString("admin_contact_number");
                                        String admin_state_city = jsonObject1.getString("admin_state_city");
                                        String user_address_label = jsonObject1.getString("user_address_label");
                                        String user_crypto_address = jsonObject1.getString("user_crypto_address");
                                        String user_name = jsonObject1.getString("user_name");
                                        String user_email = jsonObject1.getString("user_email");
                                        String user_contact_person_name = jsonObject1.getString("user_contact_person_name");
                                        String user_contact_number = jsonObject1.getString("user_contact_number");
                                        String user_state_city = jsonObject1.getString("user_state_city");

                                        String decline_message=jsonObject1.getString("decline_message");



                                        if (payment_method_name.equals("Bank Transfer"))
                                        {
                                            paymentop_text.setText(payment_method_name);
                                            created_text.setText(date_formatnew(created_date));
                                            totlaamount_text.setText("$"+fund_amount);
                                            cryptotype_text.setText(admin_bank_account_number);
                                            cryptotype_tag.setText("Account Number");
                                            protocol_text.setText(admin_bank_ifsc_code);
                                            protocol_tag.setText("IFSC Code");
                                            bankname.setText(admin_bank_name);
                                            cryptoaddress_tag.setText("Bank Name");
                                            cancelationlayout.setVisibility(View.GONE);
                                            if (!decline_message.equals(""))
                                            {
                                                decline_text.setText(decline_message.toString());
                                            }
                                            else
                                            {
                                                declineMessagelayout.setVisibility(View.GONE);
                                            }
                                            Glide.with(activity).load(document_file).into(document);

                                        }
                                        else if (payment_method_name.equals("Cash"))
                                        {
                                            paymentop_text.setText(payment_method_name);
                                            created_text.setText(date_formatnew(created_date));
                                            totlaamount_text.setText("$"+fund_amount);
                                            cryptotype_text.setText(admin_contact_person_name);
                                            cryptotype_tag.setText("Full Name");
                                            protocol_text.setText(admin_contact_number);
                                            protocol_tag.setText("Contact Number");
                                            bankname.setText(admin_state_city);
                                            cryptoaddress_tag.setText("State/City");
                                            cancelationlayout.setVisibility(View.GONE);
                                            if (!decline_message.equals(""))
                                            {
                                                decline_text.setText(decline_message.toString());
                                            }
                                            else
                                            {
                                                declineMessagelayout.setVisibility(View.GONE);
                                            }
                                            Glide.with(activity).load(document_file).into(document);

                                        }
                                        else
                                        {
                                            paymentop_text.setText(payment_method_name);
                                            created_text.setText(date_formatnew(created_date));
                                            totlaamount_text.setText("$"+fund_amount);
                                            cryptotype_text.setText(crypto_type_name);
                                            protocol_text.setText(protocol_name);
                                            bankname.setText("");
                                            cryptoaddress_text.setText(admin_protocol_address);
                                            cancelationlayout.setVisibility(View.GONE);
                                            if (!decline_message.equals(""))
                                            {
                                                decline_text.setText(decline_message.toString());
                                            }
                                            else
                                            {
                                                declineMessagelayout.setVisibility(View.GONE);
                                            }
                                            Glide.with(activity).load(document_file).into(document);

                                        }


                                    }

                                    else
                                    {
                                        String Message = jsonObject.getString("message");

                                        nodat.setVisibility(View.VISIBLE);

                                    }

                                    progressBardialog.setVisibility(View.GONE);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    progressBardialog.setVisibility(View.GONE);
                                    nodat.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            progressBardialog.setVisibility(View.GONE);
                            nodat.setVisibility(View.VISIBLE);
                        }
                    });











                    bottomSheetDialogg.show();
                    bottomSheetDialogg.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    bottomSheetDialogg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    bottomSheetDialogg.getWindow().getAttributes().windowAnimations=R.style.Dialoganimation;
                    bottomSheetDialogg.getWindow().setGravity(Gravity.CENTER);

                }
            }
        });




        if (listdata.get(position).getStatus().equalsIgnoreCase("accept"))
        {
            holder.status.setText("Completed");

        }
        else if (listdata.get(position).getStatus().equalsIgnoreCase("request"))
        {
            holder.status.setText("Pending");
            holder.status.setTextColor(Color.parseColor("#FF7373"));

        }
        else if (listdata.get(position).getStatus().equalsIgnoreCase("completed"))
        {
            holder.status.setText("Completed");
        }
        else if (listdata.get(position).getStatus().equalsIgnoreCase("complited"))
        {
            holder.status.setText("Completed");
        }
        else if (listdata.get(position).getStatus().equalsIgnoreCase("decline"))
        {
            holder.status.setText("Payment Decline");
            holder.status.setTextColor(Color.parseColor("#FF7373"));
        }
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title,totalamount_lable,totalamount_txt,date_txt,date_lable,status,plane_txt;
        RelativeLayout UserPinCardView;
        LinearLayout planelyt;
        public ViewHolder(View itemView) {
            super(itemView);

            this.totalamount_lable = (TextView) itemView.findViewById(R.id.totalamount_lable);
            this.date_lable = (TextView) itemView.findViewById(R.id.date_lable);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.totalamount_txt = (TextView) itemView.findViewById(R.id.totalamount_txt);
            this.date_txt = (TextView) itemView.findViewById(R.id.date_txt);
            this.status = (TextView) itemView.findViewById(R.id.status);
            this.plane_txt=(TextView) itemView.findViewById(R.id.plane_txt);
            this.planelyt=(LinearLayout) itemView.findViewById(R.id.planelyt);
            this.UserPinCardView=(RelativeLayout) itemView.findViewById(R.id.UserPinCardView);
        }
    }
    public void AlertDialogDetail() {

    }

    public String date_format(String strDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date varDate = dateFormat.parse(strDate);
//            dateFormat=new SimpleDateFormat("dd MMM EEE yyyy");
            dateFormat = new SimpleDateFormat("MMMM-yyyy");
            System.out.println("Date :" + dateFormat.format(varDate));
            return dateFormat.format(varDate);
        } catch (Exception e) {
            e.printStackTrace();
            return strDate;
        }


    }

    public String date_formatnew(String strDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date varDate = dateFormat.parse(strDate);
//            dateFormat=new SimpleDateFormat("dd MMM EEE yyyy");
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            System.out.println("Date :" + dateFormat.format(varDate));
            return dateFormat.format(varDate);
        } catch (Exception e) {
            e.printStackTrace();
            return strDate;
        }


    }

}