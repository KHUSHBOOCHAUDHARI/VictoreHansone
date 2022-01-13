package com.business.cryptosoar.Adapter;

import static com.business.cryptosoar.Activity.SignupActivity.getEditTextFilter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.business.cryptosoar.Fragment.WithdrowFragment;
import com.business.cryptosoar.Model.CryproMethod;
import com.business.cryptosoar.Model.PaymentOptModel;
import com.business.cryptosoar.R;
import com.business.cryptosoar.Util.Method;
import com.hbb20.CountryCodePicker;

import java.util.ArrayList;
import java.util.List;

public class WithdrowPaymentOptAdapter extends RecyclerView.Adapter<WithdrowPaymentOptAdapter.ViewHolder> {
    private List<PaymentOptModel> listdata;
    public static Activity activity;
    private List<CryproMethod> cryproMethod;
    private RecyclerView mRecyclerView;
    public static String method = "0";
    Method methodd;
    private int amountOfItemsSelected = 0;
    private int row_of_index = -1;
    public static String ContactPersonName = "";
    public static String ContactNumber = "";
    public static String City = "";
    public static String AccountNumber = "";
    public static String IFSC = "";
    public static String BranchName = "";
    private String phone_code;
    public WithdrowPaymentOptAdapter(Activity activity, List<PaymentOptModel> listdata) {
        this.activity = activity;
        this.listdata = listdata;
        cryproMethod = new ArrayList<CryproMethod>();
        methodd = new Method(activity);
    }

    @Override public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        mRecyclerView = recyclerView;

    }
    @Override public WithdrowPaymentOptAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.withdrow_peyment_opt_list, parent, false);
        return new WithdrowPaymentOptAdapter.ViewHolder(view);

    }
    @Override public void onBindViewHolder(WithdrowPaymentOptAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final PaymentOptModel myListData = listdata.get(position);
        holder.name_txt.setText(listdata.get(position).getName());

        cryproMethod = myListData.getArrayList();

        Log.e("Datat------", cryproMethod.toString());
        Log.e("Datatkk------", cryproMethod.toString());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        holder.layout_recy.setLayoutManager(layoutManager);
        WithDrowCryproTypeMethodAdaptet adapter = new WithDrowCryproTypeMethodAdaptet(activity, cryproMethod);
        holder.layout_recy.setAdapter(adapter);

        holder.contact_person_txt.setFilters(new InputFilter[]{getEditTextFilter()});
        holder.state_city_txt.setFilters(new InputFilter[]{getEditTextFilter()});
        holder.branchname_txt.setFilters(new InputFilter[]{getEditTextFilter()});
        phone_code = holder.ccp.getSelectedCountryCode();

        holder.ccp.registerCarrierNumberEditText(holder.contact_number_txt);
        holder.ccp.setNumberAutoFormattingEnabled(false);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                           method = listdata.get(position).getId();
                holder.relativeLayout.setBackgroundTintList(ContextCompat.getColorStateList(activity, R.color.toolbar));
                if (holder.relativeLayout.isSelected() == false) {
                    holder.relativeLayout.setBackgroundTintList(ContextCompat.getColorStateList(activity, R.color.colorPrimaryDark));
                    holder.relativeLayout.setSelected(true);
                    amountOfItemsSelected++;
                } else if (holder.relativeLayout.isSelected() == true) {
                    holder.relativeLayout.setBackgroundTintList(ContextCompat.getColorStateList(activity, R.color.toolbar));
                    holder.relativeLayout.setSelected(false);
                    amountOfItemsSelected--;
                }

                if (amountOfItemsSelected > 1) {

                }


                if (listdata.get(position).getId().equals("3")) {
                    holder.relativeLayout.setBackgroundTintList(ContextCompat.getColorStateList(activity, R.color.toolbar));

                    if (holder.kkk.getVisibility() == View.VISIBLE) {
                        holder.coin_lyt.setVisibility(View.GONE);
                        holder.kkk.setVisibility(View.GONE);
                        holder.layout_recy.setVisibility(View.GONE);
                        holder.cryptocurrancyme_logop.setVisibility(View.GONE);
                        holder.cryptocurrancypo_logo.setVisibility(View.VISIBLE);
                    } else if (holder.kkk.getVisibility() == View.GONE) {
                        holder.coin_lyt.setVisibility(View.GONE);
                        holder.kkk.setVisibility(View.VISIBLE);
                        holder.layout_recy.setVisibility(View.GONE);
                        holder.cryptocurrancyme_logop.setVisibility(View.VISIBLE);
                        holder.cryptocurrancypo_logo.setVisibility(View.GONE);
                    }

                    holder.banknumber_txt.setInputType(InputType.TYPE_CLASS_NUMBER);
                    holder.ifsc_txt.setInputType(InputType.TYPE_CLASS_TEXT);
                    holder.branchname_txt.setInputType(InputType.TYPE_CLASS_TEXT);
                    if (!WithdrowFragment.user_bank_account_number.equals("")) {
                        holder.banknumber_txt.setText(WithdrowFragment.user_bank_account_number);

                    }

                    if (!WithdrowFragment.user_bank_ifsc.equals("")) {
                        holder.ifsc_txt.setText(WithdrowFragment.user_bank_ifsc);
                        AccountNumber = holder.banknumber_txt.getText().toString();
                        IFSC = holder.ifsc_txt.getText().toString();
                    }

                    if (!WithdrowFragment.user_bank_name.equals("")) {
                        holder.branchname_txt.setText(WithdrowFragment.user_bank_name);

                        BranchName = holder.branchname_txt.getText().toString();
                    }


                    holder.banknumber_txt.addTextChangedListener(new TextWatcher() {

                        public void afterTextChanged(Editable s) {
                        }

                        public void beforeTextChanged(CharSequence s, int start,
                                                      int count, int after) {
                        }

                        public void onTextChanged(CharSequence s, int start,
                                                  int before, int count) {
                            AccountNumber = holder.banknumber_txt.getText().toString();
                        }
                    });

                    holder.ifsc_txt.addTextChangedListener(new TextWatcher() {

                        public void afterTextChanged(Editable s) {
                        }

                        public void beforeTextChanged(CharSequence s, int start,
                                                      int count, int after) {
                        }

                        public void onTextChanged(CharSequence s, int start,
                                                  int before, int count) {
                            IFSC = holder.ifsc_txt.getText().toString();
                        }
                    });
                    holder.branchname_txt.addTextChangedListener(new TextWatcher() {

                        public void afterTextChanged(Editable s) {
                        }

                        public void beforeTextChanged(CharSequence s, int start,
                                                      int count, int after) {
                        }

                        public void onTextChanged(CharSequence s, int start,
                                                  int before, int count) {
                            BranchName = holder.branchname_txt.getText().toString();
                        }
                    });


                } else if (listdata.get(position).getId().equals("2")) {
                    holder.relativeLayout.setBackgroundTintList(ContextCompat.getColorStateList(activity, R.color.toolbar));
                    holder.kkk.setVisibility(View.GONE);
                    if (holder.coin_lyt.getVisibility() == View.VISIBLE) {

                        holder.coin_lyt.setVisibility(View.GONE);
                        holder.kkk.setVisibility(View.GONE);
                        holder.layout_recy.setVisibility(View.GONE);
                        holder.cryptocurrancyme_logop.setVisibility(View.GONE);
                        holder.cryptocurrancypo_logo.setVisibility(View.VISIBLE);
                    } else if (holder.coin_lyt.getVisibility() == View.GONE) {

                        holder.coin_lyt.setVisibility(View.VISIBLE);
                        holder.kkk.setVisibility(View.GONE);
                        holder.layout_recy.setVisibility(View.GONE);
                        holder.cryptocurrancyme_logop.setVisibility(View.VISIBLE);
                        holder.cryptocurrancypo_logo.setVisibility(View.GONE);
                    }

                    if (!WithdrowFragment.user_contact_person_name.equals("")) {
                        holder.contact_person_txt.setText(WithdrowFragment.user_contact_person_name);
                        ContactPersonName = holder.contact_person_txt.getText().toString();
                    } else {
                        holder.contact_person_txt.setHint("Contact Person Name");
                    }
                    if (!WithdrowFragment.user_contact_number.equals("")) {
                        holder.contact_number_txt.setText(WithdrowFragment.user_contact_number);
                        ContactNumber = holder.contact_number_txt.getText().toString();
                    } else {
                        holder.contact_number_txt.setHint("Contact Number");
                    }
                    if (!WithdrowFragment.user_state_city.equals("")) {
                        holder.state_city_txt.setText(WithdrowFragment.user_state_city);
                        City = holder.state_city_txt.getText().toString();
                    } else {
                        holder.state_city_txt.setHint("City/State");
                    }


                    holder.contact_person_txt.addTextChangedListener(new TextWatcher() {

                        public void afterTextChanged(Editable s) {
                        }

                        public void beforeTextChanged(CharSequence s, int start,
                                                      int count, int after) {
                        }

                        public void onTextChanged(CharSequence s, int start,
                                                  int before, int count) {
                            ContactPersonName = holder.contact_person_txt.getText().toString();
                        }
                    });

                    holder.contact_number_txt.addTextChangedListener(new TextWatcher() {

                        public void afterTextChanged(Editable s) {
                        }

                        public void beforeTextChanged(CharSequence s, int start,
                                                      int count, int after) {
                        }

                        public void onTextChanged(CharSequence s, int start,
                                                  int before, int count) {
                            ContactNumber = holder.contact_number_txt.getText().toString();
                        }
                    });
                    holder.state_city_txt.addTextChangedListener(new TextWatcher() {

                        public void afterTextChanged(Editable s) {
                        }

                        public void beforeTextChanged(CharSequence s, int start,
                                                      int count, int after) {
                        }

                        public void onTextChanged(CharSequence s, int start,
                                                  int before, int count) {
                            City = holder.state_city_txt.getText().toString();


                        }
                    });


                }
                else {
                    holder.relativeLayout.setBackgroundTintList(ContextCompat.getColorStateList(activity, R.color.toolbar));


                    if (holder.layout_recy.getVisibility() == View.GONE) {


                        holder.layout_recy.setVisibility(View.VISIBLE);
                        holder.kkk.setVisibility(View.GONE);
                        holder.coin_lyt.setVisibility(View.GONE);
                        holder.cryptocurrancyme_logop.setVisibility(View.VISIBLE);
                        holder.cryptocurrancypo_logo.setVisibility(View.GONE);
                    } else if (holder.layout_recy.getVisibility() == View.VISIBLE) {
                        holder.layout_recy.setVisibility(View.GONE);
                        holder.kkk.setVisibility(View.GONE);
                        holder.coin_lyt.setVisibility(View.GONE);
                        holder.cryptocurrancyme_logop.setVisibility(View.GONE);
                        holder.cryptocurrancypo_logo.setVisibility(View.VISIBLE);

                    }
                }
            }
        });


    }
    @Override public int getItemCount() {
        return listdata.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name_txt;
        RecyclerView layout_recy;
        public static CountryCodePicker ccp;
        RelativeLayout relativeLayout, kkk, coin_lyt;
        ImageView cryptocurrancypo_logo, cryptocurrancyme_logop;
        EditText contact_person_txt, contact_number_txt, state_city_txt, banknumber_txt, ifsc_txt, branchname_txt;

        public ViewHolder(View itemView) {
            super(itemView);
            this.name_txt = (TextView) itemView.findViewById(R.id.name_txt);
            this.layout_recy = (RecyclerView) itemView.findViewById(R.id.layout_recy);
            this.relativeLayout = (RelativeLayout) itemView.findViewById(R.id.cryptocurrancy_lyt);
            this.cryptocurrancyme_logop = (ImageView) itemView.findViewById(R.id.cryptocurrancyme_logo);
            this.cryptocurrancypo_logo = (ImageView) itemView.findViewById(R.id.cryptocurrancypo_logo);
            this.kkk = (RelativeLayout) itemView.findViewById(R.id.kkk);
            this.coin_lyt = (RelativeLayout) itemView.findViewById(R.id.coin_lyt);
            this.contact_person_txt = (EditText) itemView.findViewById(R.id.contact_person_txt);
            this.contact_number_txt = (EditText) itemView.findViewById(R.id.contact_number_txt);
            this.state_city_txt = (EditText) itemView.findViewById(R.id.state_city_txt);
            banknumber_txt = (EditText) itemView.findViewById(R.id.banknumber_txt);
            ifsc_txt = (EditText) itemView.findViewById(R.id.ifsc_txt);
            branchname_txt = (EditText) itemView.findViewById(R.id.branchname_txt);
            ccp=itemView.findViewById(R.id.ccp);

        }

    }
}