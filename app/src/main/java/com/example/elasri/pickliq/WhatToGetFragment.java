package com.example.elasri.pickliq;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class WhatToGetFragment extends Fragment {
    TextView howmuch;
    ListView alcohols;
    Button close;
    ArrayList<String> name_alcohol;
    ArrayList<Integer> price_alcohol;
    ArrayList<String> brand_alcohol;
    ArrayList<String> subcategory_alcohol;
    ArrayList<String> range_alcohol;
    CustomAdapter customAdapter;

    public WhatToGetFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_what_to_get, container, false);
        DiscreteSeekBar discreteSeekBar1 = (DiscreteSeekBar) rootView.findViewById(R.id.seekbar);
        howmuch = (TextView)rootView.findViewById(R.id.howmuch);
        discreteSeekBar1.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int value) {
                howmuch.setText(String.valueOf(value));
                return value;
            }
        });
        final Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner);
        Button whattoget = (Button) rootView.findViewById(R.id.whattoget);
        whattoget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customAdapter=new CustomAdapter();
                name_alcohol = new ArrayList<String>();
                price_alcohol = new ArrayList<Integer>();
                brand_alcohol = new ArrayList<String>();
                subcategory_alcohol = new ArrayList<String>();
                range_alcohol = new ArrayList<String>();
                if(spinner.getSelectedItem().toString().equals("Choose the category")){
                    Log.d("NOCATEGORY1","NOCATEGORY1");
                    invokeWSPriceAlcohols(Integer.parseInt(howmuch.getText().toString()));
                }
                else{
                    invokeWSPriceandCategorytAlcohols(Integer.parseInt(howmuch.getText().toString()),spinner.getSelectedItem().toString());
                }
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View mView = getActivity().getLayoutInflater().inflate(R.layout.alcohol_search_result, null);
                alcohols = (ListView) mView.findViewById(R.id.listview);
                alcohols.setAdapter(customAdapter);
                close = (Button) mView.findViewById(R.id.close);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


            }
        });
        return  rootView;
    }

    private void invokeWSPriceandCategorytAlcohols(int price, String category) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = Config.IPURL+"search/"+price+"/"+category;
        client.get(getContext(), url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray alcohols) {
                try {
                    Log.d("LENGTH",""+alcohols.length());
                    for (int i = 0; i < alcohols.length(); i++) {
                        JSONObject alcohol = alcohols.getJSONObject(i);
                        String name  = alcohol.getString("name");
                        name_alcohol.add(name);
                        String subcategory  = alcohol.getString("subcategory");
                        subcategory_alcohol.add(subcategory);
                        String brand  = alcohol.getString("brand");
                        brand_alcohol.add(brand);
                        String range  = alcohol.getString("range");
                        range_alcohol.add(range);
                        Integer price = alcohol.getInt("price");
                        price_alcohol.add(price);
                        customAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
            }

        });
    }

    private void invokeWSPriceAlcohols(int price) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = Config.IPURL+"search/"+price;
        client.get(getContext(), url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray alcohols) {
                try {
                    Log.d("NOCATEGORY2",""+alcohols.length());
                    for (int i = 0; i < alcohols.length(); i++) {
                        JSONObject alcohol = alcohols.getJSONObject(i);
                        String name  = alcohol.getString("name");
                        name_alcohol.add(name);
                        String subcategory  = alcohol.getString("subcategory");
                        subcategory_alcohol.add(subcategory);
                        String brand  = alcohol.getString("brand");
                        brand_alcohol.add(brand);
                        String range  = alcohol.getString("range");
                        range_alcohol.add(range);
                        Integer price = alcohol.getInt("price");
                        price_alcohol.add(price);
                        customAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
            }

        });
    }


    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return name_alcohol == null ? 0 : name_alcohol.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = getActivity().getLayoutInflater().inflate(R.layout.infos_alcohol_item_dialog,null);
            TextView name = (TextView)convertView.findViewById(R.id.name);
            TextView brand = (TextView)convertView.findViewById(R.id.brand);
            TextView range = (TextView)convertView.findViewById(R.id.range);
            TextView price = (TextView)convertView.findViewById(R.id.price);
            name.setText(name_alcohol.get(position));
            brand.setText(brand_alcohol.get(position)+" - "+subcategory_alcohol.get(position));
            price.setText(String.valueOf(price_alcohol.get(position))+ " ₹");
            range.setText(range_alcohol.get(position) + " ml");
            return convertView;
        }
    }

}
