package com.radioline.master.myapplication;

import android.app.Application;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.parse.Parse;
import com.parse.ParseCrashReporting;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.radioline.master.basic.Basket;
import com.radioline.master.basic.ParseGroups;
import com.radioline.master.basic.ParseItems;
import com.radioline.master.basic.ParseSetting;

public class App extends Application {
    @Override
    public void onCreate() {

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.rllogo)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(options)
                .build();
        ImageLoader.getInstance().init(config);


        super.onCreate();
        ParseCrashReporting.enable(this);
        Parse.enableLocalDatastore(getApplicationContext());
        ParseObject.registerSubclass(Basket.class);
        ParseObject.registerSubclass(ParseSetting.class);
        ParseObject.registerSubclass(ParseItems.class);
        ParseObject.registerSubclass(ParseGroups.class);
        Parse.initialize(this, "5pOXIrqgAidVKFx2mWnlMHj98NPYqbR37fOEkuuY", "oZII0CmkEklLvOvUQ64CQ6i4QjOzBIEGZfbXvYMG");
        ParseInstallation.getCurrentInstallation().saveInBackground();
//        ParseConfig.getInBackground(new ConfigCallback() {
//            @Override
//            public void done(ParseConfig config, ParseException e) {
//                ParseFile restAverage = config.getParseFile("RestAverage");
//
//                ParseFile restMax = config.getParseFile("RestMax");
//                ParseFile restMin = config.getParseFile("RestMin");
//                Log.d("TAG", "Loading images files");
//            }
//        });

    }
}