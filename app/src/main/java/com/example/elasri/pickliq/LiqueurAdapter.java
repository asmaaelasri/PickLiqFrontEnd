package com.example.elasri.pickliq;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by elasri on 25/02/2018.
 */

public class LiqueurAdapter extends RecyclerView.Adapter<LiqueurAdapter.MyViewHolder> {
    ArrayList<Alcohol> alcohollist;


    Context context;
    private final static int FADE_DURATION = 2000;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public TextView brand;
        public TextView range;
        public TextView price;

        public MyViewHolder(View v){
            super(v);
            name = (TextView)v.findViewById(R.id.name);
            brand = (TextView)v.findViewById(R.id.brand);
            range = (TextView)v.findViewById(R.id.range);
            price = (TextView)v.findViewById(R.id.price);
        }
    }

    public LiqueurAdapter(Context context,ArrayList<Alcohol> alcohols){
        this.context=context;
        alcohollist = alcohols;

    }

    @Override
    public LiqueurAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.infos_alcohol_item, parent, false);
        LiqueurAdapter.MyViewHolder vh= new LiqueurAdapter.MyViewHolder(v);
        return  vh;
    }

    @Override
    public void onBindViewHolder(LiqueurAdapter.MyViewHolder holder, int position) {
        holder.name.setText(alcohollist.get(position).getName());
        holder.brand.setText(alcohollist.get(position).getBrand()+" - "+alcohollist.get(position).getSubcategory());
        holder.price.setText(String.valueOf(alcohollist.get(position).getPrice())+ " ₹");
        holder.range.setText(alcohollist.get(position).getRange() + "ml");
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
        return alcohollist == null ? 0 : alcohollist.size();

    }

}
