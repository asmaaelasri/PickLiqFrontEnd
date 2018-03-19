package com.example.elasri.pickliq;

import android.os.Bundle;
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

import cz.msebera.android.httpclient.Header;

public class BeverageOutletFragment extends Fragment {
    ArrayList<String> name_outlet = new ArrayList<String>();
    ArrayList<String> address_outlet = new ArrayList<String>();
    ArrayList<Double> latitude_outlet = new ArrayList<Double>();
    ArrayList<Double> longitude_outlet = new ArrayList<Double>();

    BeverageOutletAdapter adapter;



    public BeverageOutletFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        invokeWSAllOutlets();

    }

    private void invokeWSAllOutlets() {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = Config.IPURL+"getAllOutlets";
        client.get(getContext(), url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray outlets) {
                try {
                    for (int i = 0; i < outlets.length(); i++) {
                        JSONObject outlet = outlets.getJSONObject(i);
                        String name  = outlet.getString("name");
                        String address = outlet.getString("address");
                        double latitude = outlet.getDouble("latitude");
                        double longitude = outlet.getDouble("longitude");
                        name_outlet.add(name);
                        address_outlet.add(address);
                        latitude_outlet.add(latitude);
                        longitude_outlet.add(longitude);
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
        View rootView =  inflater.inflate(R.layout.fragment_beverage_outlets, container, false);
        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.outlet_recycler_view);
        rv.setHasFixedSize(true);
        adapter = new BeverageOutletAdapter(this.getContext(),name_outlet,address_outlet,latitude_outlet,longitude_outlet);
        rv.setAdapter(adapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(mLayoutManager);
        return rootView;
    }




}
