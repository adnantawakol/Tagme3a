package com.example.tagme3a;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterCategory extends ArrayAdapter<category> {

    Context c;
    ArrayList<category> ass;

    public AdapterCategory(Context context, ArrayList<category> cont) {
        super(context, R.layout.category_layout,cont);
        c=context;
        ass=cont;
    }

    class Holder
    {
        ImageView imgdept;
        TextView txtname;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position

        category data = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view

        Holder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {

            viewHolder = new Holder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.category_layout, parent, false);

            viewHolder.txtname = (TextView) convertView.findViewById(R.id.txtcatname);

           viewHolder.imgdept = (ImageView) convertView.findViewById(R.id.imgcat);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (Holder) convertView.getTag();
        }
        PicassoClient.downloadImage(c,data.getCatImg(),viewHolder.imgdept);
        viewHolder.txtname.setText(data.getCatName());


        // Return the completed view to render on screen
        return convertView;
    }


}
