package com.example.tagme3a;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tagme3a.ui.gallery.GalleryFragment;

import java.sql.Connection;
import java.util.ArrayList;

public class AdapterBuild extends ArrayAdapter<build> {

    Context c;
    ArrayList<build> ass;

    public AdapterBuild(Context context, ArrayList<build> cont) {
        super(context, R.layout.build_layout,cont);
        c=context;
        ass=cont;
    }

    class Holder
    {
        //ImageView imgdept;
        TextView txtBuildName, txtUserBuild, txtDate, txtBuildTotal, txtvDeleteBuild;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position

        build data = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view

        Holder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {

            viewHolder = new Holder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.build_layout, parent, false);

            viewHolder.txtBuildName = (TextView) convertView.findViewById(R.id.txtBuildName);
            viewHolder.txtUserBuild = (TextView) convertView.findViewById(R.id.txtUserBuild);
            viewHolder.txtDate = (TextView) convertView.findViewById(R.id.txtDate);
            viewHolder.txtBuildTotal = (TextView) convertView.findViewById(R.id.txtBuildTotal);
            viewHolder.txtvDeleteBuild = convertView.findViewById(R.id.txtvDeleteBuild);

           //viewHolder.imgdept = (ImageView) convertView.findViewById(R.id.imgcat);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (Holder) convertView.getTag();
        }
        //PicassoClient.downloadImage(c,data.getCatImg(),viewHolder.imgdept);
        viewHolder.txtBuildName.setText(data.getBuildName());
        if( data.getUserID().equals(LoginActivity.ID))
            viewHolder.txtUserBuild.setText("Made by: You");
        else
            viewHolder.txtUserBuild.setText("Made by: " + data.getUserName());

        viewHolder.txtDate.setText("Created on: " + data.getDate());
        viewHolder.txtBuildTotal.setText("Total price:\n" + data.getTotal());
        viewHolder.txtvDeleteBuild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder AlertDeleteBuild = new AlertDialog.Builder(c)
                        .setTitle("Delete this build")
                        .setMessage("Are you sure you'd like to delete this build?")
                        .setNegativeButton("No",null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Database DB = new Database();
                                Connection conn = DB.connect();
                                String dml = DB.Rundml("Delete from build where buildID = '"+ data.getBuildID()+"'");

                                if (dml == "ok") {
                                    Toast.makeText(c, "Build deleted succesfully", Toast.LENGTH_SHORT).show();
                                    remove(getItem(position));      //remove item from adapter directly to not need refresh
                                    notifyDataSetChanged();
                                }

                                else
                                    Toast.makeText(c, "Error in deleting" + dml, Toast.LENGTH_SHORT).show();
                            }
                        });
                AlertDeleteBuild.create().show();
            }
        });


        // Return the completed view to render on screen
        return convertView;
    }


}
