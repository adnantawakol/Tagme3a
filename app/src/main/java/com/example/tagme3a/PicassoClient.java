package com.example.tagme3a;

import android.content.Context;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

/**
 * Created by YAT on 25/11/2017.
 */

public class PicassoClient {
    static String imgs;



    public static void downloadImage(Context c, String url, ImageView img)
    {

        if(url != null && url.length()>0)
        {

            Picasso.with(c).load(url).placeholder(R.drawable.logo).into(img);
        }
        else
        {
            Picasso.with(c).load(R.drawable.logo).into(img);
        }
    }
}