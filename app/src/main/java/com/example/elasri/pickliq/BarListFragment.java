package com.example.elasri.pickliq;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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


public class BarListFragment extends Fragment {
    ArrayList<Integer> id_bar = new ArrayList<Integer>();
    ArrayList<String> name_bar = new ArrayList<String>();
    ArrayList<String> address_bar = new ArrayList<String>();
    ArrayList<Double> latitude_bar = new ArrayList<Double>();
    ArrayList<Double> longitude_bar = new ArrayList<Double>();

    BarListAdapter adapter;
    FragmentManager manager;


    public BarListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        invokeWSAllPlaces();

    }

    private void invokeWSAllPlaces() {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = Config.IPURL+"getAllPlaces";
        client.get(getContext(), url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray places) {
                try {
                    Log.d("LENGTH",""+places.length());
                    for (int i = 0; i < places.length(); i++) {
                        JSONObject place = places.getJSONObject(i);
                        Integer id  = place.getInt("placeId");
                        id_bar.add(id);
                        String name  = place.getString("name");
                        Log.d("Name",name);
                        String address = place.getString("address");
                        Log.d("Address",address);
                        double latitude = place.getDouble("latitude");
                        Log.d("Latitude", String.valueOf(latitude));
                        double longitude = place.getDouble("longitude");
                        Log.d("Longitude", String.valueOf(longitude));
                        name_bar.add(name);
                        Log.d("LENGHT TAB1", String.valueOf(name_bar.size()));
                        address_bar.add(address);
                        latitude_bar.add(latitude);
                        longitude_bar.add(longitude);
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
        View rootView =  inflater.inflate(R.layout.fragment_bar_list, container, false);
        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.bar_recycler_view);
        rv.setHasFixedSize(true);
        adapter = new BarListAdapter(this.getContext(),id_bar,name_bar,address_bar,latitude_bar,longitude_bar);
        rv.setAdapter(adapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(mLayoutManager);
        return rootView;
    }


}
