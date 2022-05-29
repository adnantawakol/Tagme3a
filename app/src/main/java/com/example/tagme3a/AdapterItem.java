package com.example.tagme3a;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterItem extends ArrayAdapter<item> {

    Context c;
    ArrayList<item> ass;

    public AdapterItem(Context context, ArrayList<item> cont) {
        super(context, R.layout.item_layout,cont);
        c=context;
        ass=cont;
    }

    class Holder
    {
        ImageView imgItem;
        TextView txtvItemName, txtvPrice, txtvdesc;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position

        item data = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view

        Holder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {

            viewHolder = new Holder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_layout, parent, false);

            viewHolder.txtvItemName = convertView.findViewById(R.id.txtvItemName);
            viewHolder.txtvPrice = convertView.findViewById(R.id.txtvPrice);
            viewHolder.imgItem = convertView.findViewById(R.id.imgItem);
            viewHolder.txtvdesc = convertView.findViewById(R.id.txtvDescBuild);


            convertView.setTag(viewHolder);

        } else {
            viewHolder = (Holder) convertView.getTag();
        }
        PicassoClient.downloadImage(c,data.getiImage(),viewHolder.imgItem);
        viewHolder.txtvItemName.setText(data.getiName());
        viewHolder.txtvPrice.setText(data.getiPrice() + " LE");
        if(data.getiDescription() == null) {
            viewHolder.txtvdesc.setText("No Description for this item.");
            viewHolder.txtvdesc.setTextColor(Color.rgb(72,72,72));
        }
        else if (data.getiDescription().length() < 50 || data.getiDescription().split(" ").length <= 10)
            viewHolder.txtvdesc.setText(data.getiDescription());
        else
            //viewHolder.txtvdesc.setText(data.getiDescription());
        {
            String[] arr = data.getiDescription().split(" ");
            String desc = "";
            for(int i =0; i<10; i++)
                desc+=(" "+arr[i]);
            viewHolder.txtvdesc.setText(desc + "...");
        }



        // Return the completed view to render on screen
        return convertView;
    }


}
