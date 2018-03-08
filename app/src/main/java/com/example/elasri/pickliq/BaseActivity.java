package com.example.elasri.pickliq;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class BaseActivity extends AppCompatActivity {
    FragmentManager manager;
    Toolbar toolbar;
    Context context;
    ListView alcohols;
    Button close;
    MaterialSearchView searchView;
    ArrayList<String> name_alcohol;
    ArrayList<Alcohol> specificalcohol;
    CustomAdapter customAdapter;
    private AdView mAdView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("975F1588F637E6BD92C0032A93AB4784")
                .build();
        mAdView.loadAd(adRequest);
        Log.e("AAA",adRequest.isTestDevice(BaseActivity.this)+"");
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdClosed() {
                Toast.makeText(getApplicationContext(), "Ad is closed!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Toast.makeText(getApplicationContext(), "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLeftApplication() {
                Toast.makeText(getApplicationContext(), "Ad left application!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
        });
        invokeWSAllAlcohols();
        final Dialog dialog = new Dialog(BaseActivity.this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_alert_agreement);
        dialog.show();
        final Button agree = (Button) dialog.findViewById(R.id.agree);
        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        BeverageOutletFragment outletlist= new BeverageOutletFragment();
        manager= getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.content,outletlist,outletlist.getTag()).commit();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        //searchView.setVoiceSearch(false);
        searchView.setCursorDrawable(R.drawable.custom_cursor);
        searchView.setEllipsize(true);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String name) {
                customAdapter=new CustomAdapter();
                invokeWSSpecificAlcohol(name);
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(BaseActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.alcohol_search_result, null);
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
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
            }

            @Override
            public void onSearchViewClosed() {
            }
        });
    }

    private void invokeWSSpecificAlcohol(String name) {
        specificalcohol= new ArrayList<Alcohol>();
        AsyncHttpClient client = new AsyncHttpClient();
        String url = Config.IPURL+"/getAlcoholByName"+"/"+name;
        client.get(this, url, new JsonHttpResponseHandler() {
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
                        String category = alcohol.getString("category");
                        Integer price = alcohol.getInt("price");
                        specificalcohol.add(new Alcohol(name,brand,subcategory,range,price,category));
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

    private void invokeWSAllAlcohols() {
        name_alcohol = new ArrayList<String>();
        AsyncHttpClient client = new AsyncHttpClient();
        String url = Config.IPURL+"/getAllAlcohols";
        client.get(this, url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray alcohols) {
                try {
                    Log.d("LENGTH",""+alcohols.length());
                    for (int i = 0; i < alcohols.length(); i++) {
                        JSONObject alcohol = alcohols.getJSONObject(i);
                        String name  = alcohol.getString("name");
                        name_alcohol.add(name);
                    }
                    String[] storeAlcohol = new String[name_alcohol.size()];
                    storeAlcohol = name_alcohol.toArray(storeAlcohol);
                    searchView.setSuggestions(storeAlcohol);
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
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }


   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);
                }
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.action_more://this item has your app icon
                View menuItemView = findViewById(R.id.action_more);
                PopupMenu popupMenu = new PopupMenu(this, menuItemView);
                popupMenu.inflate(R.menu.more);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.menu_about:
                                final Dialog dialogabout = new Dialog(BaseActivity.this);
                                dialogabout.setCancelable(false);
                                dialogabout.setContentView(R.layout.custom_about);
                                dialogabout.show();
                                final Button closeabout = (Button) dialogabout.findViewById(R.id.closeabout);
                                closeabout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogabout.dismiss();
                                    }
                                });
                                return true;
                            case R.id.menu_drydays:
                                final Dialog dialogdrydays = new Dialog(BaseActivity.this);
                                dialogdrydays.setCancelable(false);
                                dialogdrydays.setContentView(R.layout.custom_drydays);
                                dialogdrydays.show();
                                final Button closedrydays = (Button) dialogdrydays.findViewById(R.id.closedrydays);
                                closedrydays.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogdrydays.dismiss();
                                    }
                                });
                                return true;

                        }
                        return true;
                    }
                });
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_barlist:
                    getSupportActionBar().setElevation(6);
                    BarListFragment barlist= new BarListFragment();
                    manager= getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.content,barlist,barlist.getTag()).commit();
                    return true;
                case R.id.menu_events:
                    getSupportActionBar().setElevation(6);
                    EventsFragment eventlist= new EventsFragment();
                    manager= getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.content,eventlist,eventlist.getTag()).commit();
                    return true;
                case R.id.menu_outlets:
                    getSupportActionBar().setElevation(6);
                    BeverageOutletFragment outletlist= new BeverageOutletFragment();
                    manager= getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.content,outletlist,outletlist.getTag()).commit();
                    return true;
                case R.id.menu_pricelist:
                    getSupportActionBar().setElevation(0);
                    PriceListFragment pricelist= new PriceListFragment();
                    manager= getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.content,pricelist,pricelist.getTag()).commit();
                    return true;
                case R.id.menu_whattoget:
                    getSupportActionBar().setElevation(6);
                    WhatToGetFragment whattoget= new WhatToGetFragment();
                    manager= getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.content,whattoget,whattoget.getTag()).commit();
                    return true;
            }
            return false;
        }

    };

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return specificalcohol == null ? 0 : specificalcohol.size();
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
            convertView = getLayoutInflater().inflate(R.layout.infos_alcohol_item_dialog,null);
            TextView name = (TextView)convertView.findViewById(R.id.name);
            TextView brand = (TextView)convertView.findViewById(R.id.brand);
            TextView range = (TextView)convertView.findViewById(R.id.range);
            TextView price = (TextView)convertView.findViewById(R.id.price);
            name.setText(specificalcohol.get(position).getName());
            brand.setText(specificalcohol.get(position).getBrand()+" - "+specificalcohol.get(position).getSubcategory()+" - "+specificalcohol.get(position).getCategory());
            price.setText(String.valueOf(specificalcohol.get(position).getPrice())+ " ₹");
            range.setText(specificalcohol.get(position).getRange()+ " ml");
            return convertView;
        }
    }

}
