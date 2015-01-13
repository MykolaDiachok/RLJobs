package com.radioline.master.basic;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.radioline.master.myapplication.LoginActivity;

/**
 * Created by mikoladyachok on 11/26/14.
 */
public class BaseValues {


    public static String GetValue(String key) {
        Context applicationContext = LoginActivity.getContextOfApplication();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        return prefs.getString(key, "");

    }


    public static void SetValue(String Key, String Value) {
        Context applicationContext = LoginActivity.getContextOfApplication();
        SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext).edit();
        prefs.putString(Key, Value);
        prefs.commit();
    }

//    public static String GetValue(String key) {
//        String rt = "";
//        ParseQuery<ParseSetting> query = ParseSetting.getQuery();
//        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(1));
//        query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
//        //query.fromLocalDatastore();
//        query.whereEqualTo("key", key);
//        try {
//            ParseSetting getSet = query.getFirst();
//            if (getSet.isDataAvailable()) {
//                rt = getSet.getValue().toString();
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        } catch (ClassCastException e) {
//            e.printStackTrace();
//        }
//        return rt;
//    }
//
//    public static void SetValue(String Key, String Value) {
//        ParseQuery<ParseSetting> queryPID = ParseSetting.getQuery();
//        //queryPID.fromLocalDatastore();
//        queryPID.whereEqualTo("key", Key);
//        try {
//            if ((queryPID.count() > 0)) {
//                ParseSetting getSet = queryPID.getFirst();
//                getSet.setValue(Value);
//                getSet.setACL(new ParseACL(ParseUser.getCurrentUser()));
//                getSet.saveInBackground();
//            } else {
//                ParseSetting setUID = new ParseSetting();
//                setUID.setKey(Key);
//
//                setUID.setValue(Value);
//                setUID.setACL(new ParseACL(ParseUser.getCurrentUser()));
//                setUID.saveInBackground();
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//    }

}
