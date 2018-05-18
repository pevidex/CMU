package com.cmu.group22.hoponcmu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import classes.Location;

public class InitLocations extends BaseAdapter {
    private Context context;
    private final List<Location> locations;

    public InitLocations(Context context, List<Location> locations){
        this.context = context;
        this.locations = new ArrayList<>(locations);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View listView;

        if(convertView == null){
            listView = new View(context);
            listView = inflater.inflate(R.layout.locations,null);
        }else{
            listView = (View) convertView;
        }


        TextView textView = (TextView) listView.findViewById(R.id.grid_locations_label);
        textView.setText(locations.get(position).getName());
//        ImageView imageView = (ImageView) listView.findViewById(R.id.grid_locations_image);


        //int imageId = context.getResources().getIdentifier(locationItems[position][1], null, context.getPackageName());
        //imageView.setImageResource(imageId);
        //imageView.setImageResource(R.drawable.home_icon);//TODO:fix me

        return listView;
    }

    @Override
    public int getCount(){
        return locations.size();
    }

    @Override
    public Object getItem(int i){
        return null;
    }

    @Override
    public long getItemId(int i){
        return 0;
    }

}
