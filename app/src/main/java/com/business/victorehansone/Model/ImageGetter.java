package com.business.victorehansone.Model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;

import com.business.victorehansone.R;

public class ImageGetter implements Html.ImageGetter {
    private Context cnt;
    public Drawable getDrawable(String source) {
        int id;

        id = cnt.getResources().getIdentifier(source, "drawable", cnt.getPackageName());

        if (id == 0) {
            // the drawable resource wasn't found in our package, maybe it is a stock android drawable?
            id = cnt.getResources().getIdentifier(source, "drawable", "android");
        }

        if (id == 0) {
            // prevent a crash if the resource still can't be found
            return null;
        }
        else {
            Drawable d = cnt.getResources().getDrawable(id);
            d.setBounds(0,0,d.getIntrinsicWidth(),d.getIntrinsicHeight());
            return d;
        }
    }

}