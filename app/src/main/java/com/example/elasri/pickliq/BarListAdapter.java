package com.example.elasri.pickliq;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by elasri on 10/02/2018.
 */

public class BarListAdapter extends RecyclerView.Adapter<BarListAdapter.MyViewHolder> implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    ArrayList<String> name_bar;
    ArrayList<Integer> id_bar;
    ArrayList<String> address_bar;
    ArrayList<Double> latitude_bar;
    ArrayList<Double> longitude_bar;
    ListView alcohols;
    Button close;
    ArrayList<String> name_alcohol;
    ArrayList<Integer> price_alcohol;
    ArrayList<String> brand_alcohol;
    ArrayList<String> subcategory_alcohol;
    ArrayList<String> range_alcohol;
    CustomBarAlcoholAdapter customAdapter;
    ProgressDialog progress;
    Handler handler;

    private static final int PERMISSION_REQUEST_CODE = 1;
    Context context;
    private final static int FADE_DURATION = 2000;

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private Location mLastLocation;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;

    private LocationRequest mLocationRequest;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView address;
        public ImageView map;
        public ImageView availability;


        public MyViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.name);
            address = (TextView) v.findViewById(R.id.address);
            map = (ImageView) v.findViewById(R.id.map);
            availability = (ImageView) v.findViewById(R.id.listalcohols);

        }
    }

    public BarListAdapter(Context context,ArrayList<Integer> id, ArrayList<String> name, ArrayList<String> address, ArrayList<Double> latitude, ArrayList<Double> longitude) {
        this.context = context;
        id_bar = id;
        name_bar = name;
        address_bar = address;
        latitude_bar = latitude;
        longitude_bar = longitude;
    }


    @Override
    public BarListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bar_item, parent, false);
        BarListAdapter.MyViewHolder vh = new BarListAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(BarListAdapter.MyViewHolder holder, final int position) {
        holder.name.setText(name_bar.get(position));
        holder.address.setText(address_bar.get(position));
        holder.map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = new ProgressDialog(context,R.style.MyAlertDialogStyle);
                progress.setMessage("Please wait, the map is loading..");
                progress.setCancelable(false);
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.show();

                handler = new Handler()
                {

                    @Override
                    public void handleMessage(Message msg)
                    {
                        progress.dismiss();
                        super.handleMessage(msg);
                    }

                };

                AsyncHttpClient client = new AsyncHttpClient();
                String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng="+latitude_bar.get(position)+","+longitude_bar.get(position)+"&sensor=true&key=AIzaSyD8vGO_oyDlbrMybQWxA3XrkmtsNho0mPU";
                client.get(context, url, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject info) {
                        try {
                            Log.d("LATLONG",latitude_bar.get(position)+" "+ longitude_bar.get(position));
                            JSONArray array1 = info.getJSONArray("results");
                            JSONObject item1 = array1.getJSONObject(0);
                            String address = item1.getString("formatted_address");
                            InvokeWSmyLocation(address,name_bar.get(position));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    }

                });

            }
        });

        holder.availability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name_alcohol = new ArrayList<String>();
                price_alcohol = new ArrayList<Integer>();
                brand_alcohol = new ArrayList<String>();
                subcategory_alcohol = new ArrayList<String>();
                range_alcohol = new ArrayList<String>();
                customAdapter=new CustomBarAlcoholAdapter();
                InvokePlaceAlcohols(id_bar.get(position));
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
                View mView = LayoutInflater.from(context).inflate(R.layout.alcohol_search_result, null);
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
        // Set the view to fade in
        setFadeAnimation(holder.itemView);
    }

    private void InvokePlaceAlcohols(Integer id) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = Config.IPURL+"getAvailability/"+id;
        client.get(context, url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray alcohols) {
                try {
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

    private void InvokeWSmyLocation(final String address, final String name) {
        SharedPreferences prefs = ((Activity) context).getPreferences(MODE_PRIVATE);
        AsyncHttpClient client = new AsyncHttpClient();
        Log.d("Mylocation",prefs.getString("lat",null)+" "+prefs.getString("long",null));
        String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng="+Double.parseDouble(prefs.getString("lat",null))+","+Double.parseDouble(prefs.getString("long",null))+"&sensor=true&key=AIzaSyD8vGO_oyDlbrMybQWxA3XrkmtsNho0mPU";
        client.get(context, url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject info) {
                try {
                    JSONArray array1 = info.getJSONArray("results");
                    JSONObject item1 = array1.getJSONObject(0);
                    String myaddress = item1.getString("formatted_address");
                    Intent barMap = new Intent(context, PlaceDirectionActivity.class);
                    barMap.putExtra("myaddress", myaddress);
                    barMap.putExtra("placeaddress", address);
                    barMap.putExtra("placename", name);
                    handler.sendEmptyMessage(0);
                    context.startActivity(barMap);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            }

        });
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


    private class CustomBarAlcoholAdapter extends BaseAdapter{
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
            convertView = LayoutInflater.from(context).inflate(R.layout.infos_alcohol_item_dialog,null);
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
