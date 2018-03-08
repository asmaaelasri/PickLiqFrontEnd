package com.example.elasri.pickliq;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import cz.msebera.android.httpclient.Header;


public class BreezerFragment extends Fragment {
    ArrayList<Alcohol> alcohollist = new ArrayList<Alcohol>();
    BreezerAdapter adapter;

    public BreezerFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        invokeWSAllBreezeAlcohol();

    }

    private void invokeWSAllBreezeAlcohol() {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = Config.IPURL+"getCategoryAlcohol/Breezer/";
        client.get(getContext(), url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray alcohols) {
                try {
                    Log.d("LENGTH",""+alcohols.length());
                    for (int i = 0; i < alcohols.length(); i++) {
                        JSONObject alcohol = alcohols.getJSONObject(i);
                        String name  = alcohol.getString("name");
                        String subcategory  = alcohol.getString("subcategory");
                        String brand  = alcohol.getString("brand");
                        String range  = alcohol.getString("range");
                        Integer price = alcohol.getInt("price");
                        alcohollist.add(new Alcohol(name,brand,subcategory,range,price));
                        adapter.notifyDataSetChanged();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_breezer, container, false);
        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.breezer_recycler_view);
        final FloatingActionButton sort = (FloatingActionButton) rootView.findViewById(R.id.sort);
        final FloatingActionButton alphabetic = (FloatingActionButton) rootView.findViewById(R.id.alphabetic);
        final FloatingActionButton numeric = (FloatingActionButton) rootView.findViewById(R.id.numeric);
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(alphabetic.getVisibility() == View.VISIBLE && numeric.getVisibility() == View.VISIBLE){
                    alphabetic.setVisibility(View.GONE);
                    numeric.setVisibility(View.GONE);
                }
                else{
                    alphabetic.setVisibility(View.VISIBLE);
                    numeric.setVisibility(View.VISIBLE);
                }
            }
        });
        alphabetic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(alcohollist, new Comparator<Alcohol>(){
                    public int compare(Alcohol alc1, Alcohol alc2) {
                        return alc1.getName().compareToIgnoreCase(alc2.getName());
                    }
                });
                adapter.notifyDataSetChanged();
            }
        });
        numeric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(alcohollist, new Comparator<Alcohol>(){
                    public int compare(Alcohol alc1, Alcohol alc2) {
                        return alc1.getPrice().compareTo(alc2.getPrice());
                    }
                });
                adapter.notifyDataSetChanged();
            }
        });
        rv.setHasFixedSize(true);
        adapter = new BreezerAdapter(this.getContext(),alcohollist);
        rv.setAdapter(adapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(mLayoutManager);
        return rootView;
    }


}
