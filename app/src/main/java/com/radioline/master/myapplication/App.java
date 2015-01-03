package com.radioline.master.myapplication;

import android.app.Application;
import android.util.Log;

import com.parse.ConfigCallback;
import com.parse.Parse;
import com.parse.ParseConfig;
import com.parse.ParseCrashReporting;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.radioline.master.basic.Basket;
import com.radioline.master.basic.ParseItems;
import com.radioline.master.basic.ParseSetting;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ParseCrashReporting.enable(this);
        Parse.enableLocalDatastore(getApplicationContext());
        ParseObject.registerSubclass(Basket.class);
        ParseObject.registerSubclass(ParseSetting.class);
        ParseObject.registerSubclass(ParseItems.class);
        Parse.initialize(this, "5pOXIrqgAidVKFx2mWnlMHj98NPYqbR37fOEkuuY", "oZII0CmkEklLvOvUQ64CQ6i4QjOzBIEGZfbXvYMG");
        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParseConfig.getInBackground(new ConfigCallback() {
            @Override
            public void done(ParseConfig config, ParseException e) {
                ParseFile restAverage = config.getParseFile("RestAverage");
                restAverage.saveInBackground();
                ParseFile restMax = config.getParseFile("RestMax");
                ParseFile restMin = config.getParseFile("RestMin");
                Log.d("TAG", "Loading images files");
            }
        });

    }
}