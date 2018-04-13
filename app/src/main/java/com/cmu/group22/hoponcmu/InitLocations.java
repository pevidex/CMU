package com.cmu.group22.hoponcmu;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class InitLocations extends BaseAdapter {
    private Context context;
    private final String[][] locationItems;

    public InitLocations(Context context, String[][] locationItems){
        this.context = context;
        this.locationItems = locationItems;
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
        textView.setText(locationItems[position][0]);
        ImageView imageView = (ImageView) listView.findViewById(R.id.grid_locations_image);
        //int imageId = context.getResources().getIdentifier(locationItems[position][1], null, context.getPackageName());
        //imageView.setImageResource(imageId);
        imageView.setImageResource(R.drawable.home_icon);//TODO:fix me

        return listView;
    }

    @Override
    public int getCount(){
        return locationItems.length;
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
