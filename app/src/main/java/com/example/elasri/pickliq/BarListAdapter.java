package com.example.elasri.pickliq;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by elasri on 10/02/2018.
 */

public class BarListAdapter extends RecyclerView.Adapter<BarListAdapter.MyViewHolder>{
    ArrayList<String> name_bar;
    ArrayList<String> address_bar;
    ArrayList<Double> latitude_bar;
    ArrayList<Double> longitude_bar;

    Context context;
    private final static int FADE_DURATION = 2000;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public TextView address;
        public ImageView map;


        public MyViewHolder(View v){
            super(v);
            name = (TextView)v.findViewById(R.id.name);
            address = (TextView)v.findViewById(R.id.address);
            map = (ImageView)v.findViewById(R.id.map);
        }
    }

    public BarListAdapter(Context context, ArrayList<String> name, ArrayList<String> address, ArrayList<Double> longitude, ArrayList<Double> latitude){
        this.context=context;
        name_bar = name;
        address_bar = address;
        latitude_bar = latitude;
        longitude_bar = longitude;
    }


    @Override
    public BarListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bar_item, parent, false);
        BarListAdapter.MyViewHolder vh= new BarListAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(BarListAdapter.MyViewHolder holder, final int position) {
        holder.name.setText(name_bar.get(position));
        holder.address.setText(address_bar.get(position));
        holder.map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent barMap = new Intent(context,BarMapsActivity.class);
                barMap.putExtra("lat",latitude_bar.get(position));
                barMap.putExtra("long",longitude_bar.get(position));
                barMap.putExtra("name",name_bar.get(position));
                context.startActivity(barMap);


            }
        });

        // Set the view to fade in
        setFadeAnimation(holder.itemView);
    }

    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }

    @Override
    public int getItemCount() {
        return name_bar == null ? 0 : name_bar.size();
    }
}
