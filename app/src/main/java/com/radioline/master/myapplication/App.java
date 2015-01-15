package com.radioline.master.myapplication;

import android.app.Application;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.parse.Parse;
import com.parse.ParseCrashReporting;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.radioline.master.basic.Actions;
import com.radioline.master.basic.Basket;
import com.radioline.master.basic.ParseGroups;
import com.radioline.master.basic.ParseItems;
import com.radioline.master.basic.ParseSetting;
import com.splunk.mint.Mint;

public class App extends Application {
    @Override
    public void onCreate() {
        Mint.initAndStartSession(this, getString(R.string.mint));
        //.resetViewBeforeLoading(true)
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                //.showStubImage(android.R.color.transparent)
                //.bitmapConfig(Bitmap.Config.RGB_565)
                //.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .cacheInMemory(true)
                        //.resetViewBeforeLoading(true)
                .cacheOnDisc(true)
                .build();
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
//
//                .defaultDisplayImageOptions(options)
//                .build();
//        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        int memClass = am.getMemoryClass();
//        final int memoryCacheSize = 1024 * 1024 * memClass / 8;
//        Executor downloadExecutor = Executors.newFixedThreadPool(5);
//        Executor cachedExecutor = Executors.newSingleThreadExecutor();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                //.taskExecutor(downloadExecutor)
                //.taskExecutorForCachedImages(cachedExecutor)
                //.memoryCache(new UsingFreqLimitedMemoryCache(memoryCacheSize)) // 2 Mb
                //.discCache(new TotalSizeLimitedDiscCache(cacheDir, 52428800))
                //.imageDownloader(new BaseImageDownloader(this, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)
                .defaultDisplayImageOptions(options)
                .build();

        ImageLoader.getInstance().init(config);


        super.onCreate();
        ParseCrashReporting.enable(this);
        //Parse.enableLocalDatastore(getApplicationContext());
        ParseObject.registerSubclass(Actions.class);
        ParseObject.registerSubclass(Basket.class);
        ParseObject.registerSubclass(ParseSetting.class);
        ParseObject.registerSubclass(ParseItems.class);
        ParseObject.registerSubclass(ParseGroups.class);
        Parse.initialize(this, "5pOXIrqgAidVKFx2mWnlMHj98NPYqbR37fOEkuuY", "oZII0CmkEklLvOvUQ64CQ6i4QjOzBIEGZfbXvYMG");
        ParseInstallation.getCurrentInstallation().saveInBackground();
//        ParseQuery.clearAllCachedResults();
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