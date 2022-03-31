package com.business.victorehansone.Fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.business.victorehansone.Activity.MainActivity;
import com.business.victorehansone.R;
import com.business.victorehansone.Util.Apis;
import com.business.victorehansone.Util.Method;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.business.victorehansone.databinding.ReceivedFragmentBinding;
import cz.msebera.android.httpclient.Header;

public class ReceivedFragment extends Fragment {
    ReceivedFragmentBinding binding;
    Method method;
    Bitmap bitmap;
    BitMatrix bitMatrix;
    int[] pixels;
    int offset;
    int x;
    int y;
    String hexColor = "#121212";
    public static String EditTextValue;
    public final static int QRcodeWidth = 500;

    public ReceivedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.received_fragment, container, false);
        MainActivity.txt_toolbartitle.setText("Recieve Currency");
        MainActivity.selctedrecy.setVisibility(View.GONE);
        method = new Method(getActivity());

        String ghgh = method.pref.getString(method.Address_Id, null);


        if (HomeFragment.TYTPE.equals("1")) {
           // Toast.makeText(getActivity(),"one", Toast.LENGTH_SHORT).show();
            String search = getArguments().getString("type");
            binding.addrtessTxt.setText(search.toString());
            binding.qrLayout.setVisibility(View.VISIBLE);
            EditTextValue = binding.addrtessTxt.getText().toString();

            try {
                bitmap = TextToImageEncode(EditTextValue);

                binding.qrCodeImage.setImageBitmap(bitmap);
                binding.qrCodeImage.setVisibility(View.VISIBLE);
            } catch (WriterException e) {
                e.printStackTrace();
            }
            HomeFragment.TYTPE="2";
        } else {
            try {
                //Toast.makeText(getActivity(), "two", Toast.LENGTH_SHORT).show();
                JSONArray itemArray = new JSONArray(method.pref.getString(method.Address_Id, null));
                if (itemArray.length() == 0) {

                    data(method.pref.getString(method.Id, null));

                } else {
                  //  Toast.makeText(getActivity(), "three", Toast.LENGTH_SHORT).show();
                    try {
                        JSONArray jsonArray = new JSONArray(method.pref.getString(method.Address_Id, null));
                        if (jsonArray.length() != 0) {
                            binding.addrtessTxt.setText(String.valueOf(itemArray.getString(0)));

                            binding.qrLayout.setVisibility(View.VISIBLE);
                            EditTextValue = binding.addrtessTxt.getText().toString();

                            try {
                                bitmap = TextToImageEncode(EditTextValue);

                                binding.qrCodeImage.setImageBitmap(bitmap);
                                binding.qrCodeImage.setVisibility(View.VISIBLE);
                            } catch (WriterException e) {
                                e.printStackTrace();
                            }


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        binding.linearCopyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", binding.addrtessTxt.getText().toString());
                Toast.makeText(getActivity(), "Address Copy", Toast.LENGTH_SHORT).show();
                clipboard.setPrimaryClip(clip);

            }
        });


        return binding.getRoot();
    }

    private void data(String ic_userId) {
        binding.progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        String url = Apis.ROOT_URLL + getString(R.string.create_allet) + "ic_userId=" + ic_userId;
        Log.e("Url------->", url);

        client.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);
                Log.e("Url------->", res.toString());
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    String status = jsonObject.getString("statusCode");
                    if (status.equalsIgnoreCase("200")) {
                        String result = jsonObject.getString("result");
                        if (method.isNetworkAvailable(getActivity())) {
                            binding.progressBar.setVisibility(View.VISIBLE);
                            login(method.pref.getString(method.ic_userName, null), method.pref.getString(method.Password, null));
                        } else {
                            method.alertBox(getResources().getString(R.string.internet_connection));
                        }

                        binding.addrtessTxt.setText(result);
                        binding.qrLayout.setVisibility(View.VISIBLE);
                        EditTextValue = binding.addrtessTxt.getText().toString();

                        try {
                            bitmap = TextToImageEncode(EditTextValue);

                            binding.qrCodeImage.setImageBitmap(bitmap);
                            binding.qrCodeImage.setVisibility(View.VISIBLE);
                        } catch (WriterException e) {
                            e.printStackTrace();
                        }
                    } else {
                        binding.progressBar.setVisibility(View.GONE);
                        String message = jsonObject.getString("statusMsg");
                        method.alertBox(message.toString());
                    }
                } catch (JSONException e) {
                    binding.progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }
    public void login(String email, String sendPassword) {

        binding.progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();

        String url = Apis.ROOT_URLL + getString(R.string.login_ic_user) + "ic_username=" + email + "&password=" + sendPassword;
        Log.e("Url------->", url);
        client.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);
                Log.e("Url------->", res.toString());
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    String status = jsonObject.getString("statusCode");
                    if (status.equalsIgnoreCase("200")) {
                        Toast.makeText(getActivity(), "Address created successfully", Toast.LENGTH_SHORT).show();
                        JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                        JSONArray itemArray = jsonObject1.getJSONArray("walletAddress");
                        method.editor.putString(method.Address_Id, itemArray.toString());
                        method.editor.commit();

                    } else {
                    }
                    binding.progressBar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                    binding.progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                binding.progressBar.setVisibility(View.GONE);
                method.alertBox(getResources().getString(R.string.something_went_wrong));
            }
        });
    }
    Bitmap TextToImageEncode(String Value) throws WriterException {

        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (y = 0; y < bitMatrixHeight; y++) {
            offset = y * bitMatrixWidth;

            for (x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        Color.parseColor(hexColor) : getResources().getColor(R.color.white);

            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

}
