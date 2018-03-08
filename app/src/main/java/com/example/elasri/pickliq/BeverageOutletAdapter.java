package com.example.elasri.pickliq;

import android.content.Context;
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

public class BeverageOutletAdapter extends RecyclerView.Adapter<BeverageOutletAdapter.MyViewHolder>{
    ArrayList<String> name_outlet;
    ArrayList<String> address_outlet;
    ArrayList<Double> latitude_outlet;
    ArrayList<Double> longitude_outlet;

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

    public BeverageOutletAdapter(Context context, ArrayList<String> name, ArrayList<String> address, ArrayList<Double> longitude, ArrayList<Double> latitude){
        this.context=context;
        name_outlet = name;
        address_outlet = address;
        latitude_outlet = latitude;
        longitude_outlet = longitude;
    }


    @Override
    public BeverageOutletAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.outlet_item, parent, false);
        BeverageOutletAdapter.MyViewHolder vh= new BeverageOutletAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(BeverageOutletAdapter.MyViewHolder holder, int position) {
        holder.name.setText(name_outlet.get(position));
        holder.address.setText(address_outlet.get(position));
        holder.map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        return name_outlet == null ? 0 : name_outlet.size();
    }
}
