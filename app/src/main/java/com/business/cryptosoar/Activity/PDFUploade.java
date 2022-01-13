package com.business.cryptosoar.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.business.cryptosoar.Adapter.PaymentOptAdapter;
import com.business.cryptosoar.R;
import com.business.cryptosoar.Util.Apis;
import com.business.cryptosoar.Util.FilePath;
import com.business.cryptosoar.Util.Method;
import com.business.cryptosoar.Util.SingleUploadBroadcsatReceiver;
import com.business.cryptosoar.databinding.ActivityPdfuploadeBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.LogHandler;
import com.loopj.android.http.RequestParams;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.UUID;

import cz.msebera.android.httpclient.Header;

public class PDFUploade extends AppCompatActivity implements SingleUploadBroadcsatReceiver.Delegate {
    private ActivityPdfuploadeBinding binding;
    public static Dialog bottomSheetDialogg;
    Method method;
    private String image_profile;
    String bank_account_number="";
    String bank_ifsc_code="";
    String bank_branch_name="";
    String UserId,plan_type,fund_amount,payment_method_id,crypto_type_id,crypto_protocol_id;
    final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 101;
    private boolean is_profile;
    private boolean is_pdf;
    private int PICK_PDF_REQUEST = 1;
    private Uri filePath;
    private static final int STORAGE_PERMISSION_CODE = 123;
    private final SingleUploadBroadcsatReceiver uploadReceiver =
            new SingleUploadBroadcsatReceiver();
    private static final int PERMISSION_REQ_CODE = 1 << 4;
    private String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };





    //Uri to store the image uri

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(PDFUploade.this,R.layout.activity_pdfuploade);
        fullscreen();

        method=new Method(PDFUploade.this);
        requestStoragePermission();
       UserId=method.pref.getString(method.Id,null);
         plan_type=getIntent().getStringExtra("planeType");
         fund_amount=getIntent().getStringExtra("FundAmount");
         payment_method_id=getIntent().getStringExtra("PaymentId");
         crypto_type_id=getIntent().getStringExtra("CryptoTypeId");
         crypto_protocol_id=getIntent().getStringExtra("CryptoProtocolId");


        binding.tranProofBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialogg = new Dialog(PDFUploade.this) {
                    @Override
                    public boolean onTouchEvent(MotionEvent event) {
                        // Tap anywhere to close dialog.
                        bottomSheetDialogg.dismiss();
                        return true;
                    }
                };
                bottomSheetDialogg.requestWindowFeature(Window.FEATURE_NO_TITLE);
                bottomSheetDialogg.setContentView(R.layout.fragment_camera_bottomsheet);
                TextView Camera = bottomSheetDialogg.findViewById(R.id.camera_txt);
                TextView Galary = bottomSheetDialogg.findViewById(R.id.galary_txt);
                TextView cancle = bottomSheetDialogg.findViewById(R.id.cancle);
                TextView pdftext=bottomSheetDialogg.findViewById(R.id.pdf_txt);

                Camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImagePicker.Companion.with(PDFUploade.this)
                                .cameraOnly()
                                .crop()
                                .maxResultSize(1000,1000)
                                .start(20);
                        bottomSheetDialogg.dismiss();
                    }
                });
                Galary.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ImagePicker.Companion.with(PDFUploade.this)
                                .galleryOnly()
                                .crop()

                                .maxResultSize(1000,1000)
                                .start(20);
                        bottomSheetDialogg.dismiss();
                    }
                });
                pdftext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showFileChooser();
                        bottomSheetDialogg.dismiss();
                    }
                });



                cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialogg.dismiss();
                    }
                });
                bottomSheetDialogg.show();
                bottomSheetDialogg.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                bottomSheetDialogg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                bottomSheetDialogg.getWindow().getAttributes().windowAnimations=R.style.Dialoganimation;
                bottomSheetDialogg.getWindow().setGravity(Gravity.BOTTOM);
            }
        });
        binding.requerstNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String UserId=method.pref.getString(method.Id,null);
                String plan_type=getIntent().getStringExtra("planeType");
                String fund_amount=getIntent().getStringExtra("FundAmount");
                String payment_method_id=getIntent().getStringExtra("PaymentId");
                String crypto_type_id=getIntent().getStringExtra("CryptoTypeId");
                String crypto_protocol_id=getIntent().getStringExtra("CryptoProtocolId");

                if (is_profile)
                {
                    Log.e("UserId", UserId);
                    Log.e("PlaneType", plan_type);
                    Log.e("FundAmount", fund_amount);
                    Log.e("PaymentId", payment_method_id);
                    Log.e("CryptoTypeId", crypto_type_id);
                    Log.e("CryptoProtocolId", crypto_protocol_id);
                    Log.e("IFSC", getIntent().getStringExtra("BankIfscCode"));

                   AddPaymentSetting(crypto_type_id, UserId, plan_type, fund_amount,  payment_method_id,crypto_type_id,crypto_protocol_id, bank_account_number, getIntent().getStringExtra("BankIfscCode"), bank_branch_name, image_profile);

                }
                else if (is_pdf)
                {
                    Multipart();
                }
                else
                {
                     method.alertBox("Please Select Trasection Proof");
                }
            }
        });
        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.right_enter,R.anim.left_out);
                finish();
            }
        });
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==20) {
            if (resultCode == RESULT_OK) {

                is_profile = true;
                // galleryImages = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
                image_profile = data.getData().getPath();
                Uri uri = Uri.fromFile(new File(data.getData().getPath()));
                Glide.with(PDFUploade.this).load(uri).centerCrop().into(binding.image);
                if (image_profile.equals(""))
                {
                    binding.image.setVisibility(View.GONE);
                }
                else
                {
                    binding.image.setVisibility(View.VISIBLE);
                }
              }
        }

        else if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            is_pdf = true;
            image_profile = data.getData().getPath();
            filePath = data.getData();
            Log.e("","***************"+filePath);
            Glide.with(PDFUploade.this).load(filePath).centerCrop().into(binding.image);
            if (image_profile.equals(""))
            {
                binding.image.setVisibility(View.GONE);
            }
            else
            {
                binding.image.setVisibility(View.VISIBLE);

            }
        }



    }


    public void Multipart() {
        String path = FilePath.getRealPath(this, filePath);
        if (path == null) {
            Toast.makeText(this, "Please move your .pdf file to internal storage and retry", Toast.LENGTH_LONG).show();
        } else {
//            Log.e("","***************"+path.toString());
            //Uploading code
            try {
                String uploadId = UUID.randomUUID().toString();
                uploadReceiver.setDelegate((SingleUploadBroadcsatReceiver.Delegate) this);
                uploadReceiver.setUploadID(uploadId);
                Toast.makeText(this, "Uploading file. Please wait...", Toast.LENGTH_SHORT).show();
                //Creating a multi part request
                new MultipartUploadRequest(this, uploadId,Apis.ROOT_URL + getString(R.string.add_userfund_api))
                        .addFileToUpload(path, "document_file") //Adding file
                        .addParameter("user_address_id",crypto_type_id)
                        .addParameter("user_id", UserId)
                        .addParameter("plan_type", plan_type)
                        .addParameter("fund_amount", fund_amount)
                        .addParameter("payment_method_id", payment_method_id)
                        .addParameter("crypto_protocol_id", crypto_protocol_id)
                        .addParameter("bank_account_number", bank_account_number)
                        .addParameter("bank_ifsc_code", bank_ifsc_code)
                        .addParameter("bank_branch_name", bank_branch_name)
                        .addParameter("crypto_type_id ", crypto_type_id)
                        .setMaxRetries(2)
                        .startUpload(); //Starting the upload
                Log.e("","***************"+path);
                Log.e("URl-->","***************"+Apis.ROOT_URL + getString(R.string.add_userfund_api));

            } catch (Exception exc) {
                Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }





    //Add Fund Api
    public void AddPaymentSetting(String Id,String UserId,String plan_type, String fund_amount, String payment_method_id,String crypto_type_id,
                                  String crypto_protocol_id,String bank_account_number,
                                  String bank_ifsc_code,String bank_branch_name,
                                  String document_file) {
        binding.progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("user_address_id", Id);
        params.add("user_id", UserId);
        params.add("plan_type", plan_type);
        params.add("fund_amount", fund_amount);
        params.add("payment_method_id", payment_method_id);
        params.add("crypto_protocol_id", crypto_protocol_id);
        params.add("bank_account_number", bank_account_number);
        params.add("bank_ifsc_code", bank_ifsc_code);
        params.add("bank_branch_name", bank_branch_name);
        params.add("crypto_type_id",crypto_type_id);
        try {
            if (is_profile) {
                params.put("document_file", new File(document_file));
            }
            else {
                params.add("document_file",document_file);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }



        String url = Apis.ROOT_URL + getString(R.string.add_userfund_api);
        Log.e("Url------->",url);






    ;
        client.post(url,params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);
                Log.e("Url------->",res.toString());
                // Toast.makeText(getApplicationContext(),  new String(responseBody), Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            String id = jsonObject1.getString("id");
                            String user_id = jsonObject1.getString("user_id");
                            String fund_plan_type = jsonObject1.getString("fund_plan_type");
                            String fund_amount = jsonObject1.getString("fund_amount");
                            String payment_method_id = jsonObject1.getString("payment_method_id");
                            String crypto_type_id = jsonObject1.getString("crypto_type_id");
                            String crypto_protocol_id = jsonObject1.getString("crypto_protocol_id");
                            String bank_account_number = jsonObject1.getString("bank_account_number");
                            String bank_ifsc_code = jsonObject1.getString("bank_ifsc_code");
                            String bank_branch_name = jsonObject1.getString("bank_branch_name");
                            String statuss = jsonObject1.getString("status");
                            String document_file = jsonObject1.getString("document_file");
                            String is_deleted = jsonObject1.getString("is_deleted");
                            String created_date = jsonObject1.getString("created_date");
                            String updated_date = jsonObject1.getString("updated_date");
                            Intent intent=new Intent(PDFUploade.this,MainActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.right_enter,R.anim.left_out);
                        }

                    }
                    else {
                        binding.progressBar.setVisibility(View.GONE);
                        String message = jsonObject.getString("message");
                        Toast.makeText(PDFUploade.this, message.toString(), Toast.LENGTH_SHORT).show();
                    }
                    binding.progressBar.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    binding.progressBar.setVisibility(View.GONE);
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                binding.progressBar.setVisibility(View.GONE);
                method.alertBox(getResources().getString(R.string.something_went_wrong));
            }
        });
    }










    public void uploadMultipart() {
        //getting name for the image
       // String name = editText.getText().toString().trim();
        Log.e("olohkgof",filePath.toString());
        //getting the actual path of the image
        String path = FilePath.getRealPath(this, filePath);
        Log.e("olohkgof",path.toString());

        if (path == null) {
            Toast.makeText(this, "Please move your .pdf file to internal storage and retry", Toast.LENGTH_LONG).show();
        } else {
            //Uploading code
            try {
                String uploadId = UUID.randomUUID().toString();
                uploadReceiver.setDelegate((SingleUploadBroadcsatReceiver.Delegate) this);
                uploadReceiver.setUploadID(uploadId);
                //Creating a multi part request
                new MultipartUploadRequest(this, uploadId,Apis.ROOT_URL + getString(R.string.add_userfund_api))
                        .addFileToUpload(path, "document_file") //Adding file
                        .addParameter("user_address_id",crypto_type_id)
                        .addParameter("user_id", UserId)
                        .addParameter("plan_type", plan_type)
                        .addParameter("fund_amount", fund_amount)
                        .addParameter("payment_method_id", payment_method_id)
                        .addParameter("crypto_protocol_id", crypto_protocol_id)
                        .addParameter("bank_account_number", bank_account_number)
                        .addParameter("bank_ifsc_code", bank_ifsc_code)
                        .addParameter("bank_branch_name", bank_branch_name)
                        .addParameter("crypto_type_id ", crypto_type_id)
                        .setMaxRetries(2)
                        .startUpload(); //Starting the upload
                Log.e("","***************"+path);
            } catch (Exception exc) {
                Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }









    public void fullscreen() {

        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

    }
    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST);
    }
    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        uploadReceiver.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        uploadReceiver.unregister(this);
    }

    @Override
    public void onProgress(int progress) {
//        Toast.makeText(this, "progress = " + progress, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProgress(long uploadedBytes, long totalBytes) {
        binding.progressBar.setVisibility(View.VISIBLE);
        Toast.makeText(this, "pdf uploaded is on progess  till wait ..", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(Exception exception) {
        binding.progressBar.setVisibility(View.GONE);
        Toast.makeText(this, "Error Occured Try again", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCompleted(int serverResponseCode, byte[] serverResponseBody) {
        binding.progressBar.setVisibility(View.GONE);
        Toast.makeText(this, "pdf uploaded successfully", Toast.LENGTH_SHORT).show();
        Log.e("Resp----->",serverResponseBody.toString());
        Intent intent=new Intent(PDFUploade.this,MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_enter,R.anim.left_out);

    }

    @Override
    public void onCancelled() {
        Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
    }

}