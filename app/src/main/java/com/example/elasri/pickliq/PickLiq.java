package com.example.elasri.pickliq;

import android.app.Application;

import com.google.android.gms.ads.MobileAds;

/**
 * Created by elasri on 06/03/2018.
 */

public class PickLiq extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // initialize the AdMob app
        MobileAds.initialize(this, getString(R.string.admob_app_id));
    }
}
