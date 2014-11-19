package com.radioline.master.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.radioline.master.basic.Basket;
import com.radioline.master.basic.BasketViewAdapter;
import com.radioline.master.basic.ParseGroups;
import com.splunk.mint.Mint;

import java.text.DecimalFormat;
import java.util.List;

public class BasketActivity extends Activity {

    private ParseQueryAdapter<ParseObject> mainAdapter;
    private ListView lvBasket;
    private BasketViewAdapter basketViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Mint.initAndStartSession(this, "3b65ddeb");

        ParseObject.registerSubclass(Basket.class);
        //ParseObject.registerSubclass(ParseGroups.class);
        //Parse.enableLocalDatastore(getApplicationContext());

        Parse.initialize(this, "5pOXIrqgAidVKFx2mWnlMHj98NPYqbR37fOEkuuY", "oZII0CmkEklLvOvUQ64CQ6i4QjOzBIEGZfbXvYMG");

        setContentView(R.layout.activity_basket);


        basketViewAdapter = new BasketViewAdapter(this);
        lvBasket = (ListView) findViewById(R.id.lvBasket);
        lvBasket.setAdapter(basketViewAdapter);
        basketViewAdapter.loadObjects();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_basket, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_dispatch:
                intent = new Intent(this, DispatchActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_clearbasket:
//                ParseQuery<Basket> query = Basket.getQuery();
//                query.fromLocalDatastore();
//                try {
//                    List<Basket> basketList = query.find();
//                    for (Basket ibasket : basketList) {
//                        ibasket.unpinInBackground("Basket", new DeleteCallback() {
//                            public void done(ParseException e) {
//                                if (e == null) {
//                                    // myObjectWasDeletedSuccessfully();
//                                    //basketViewAdapter.loadObjects();
//
//                                    basketViewAdapter.notifyDataSetChanged();
//                                } else {
//                                    // myObjectDeleteDidNotSucceed();
//                                }
//                            }
//                        });
//                    }
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
                ParseObject.unpinAllInBackground(new DeleteCallback() {
                    @Override
                    public void done(ParseException e) {
                        basketViewAdapter.notifyDataSetChanged();
                        basketViewAdapter.loadObjects();
                    }
                });
//                ParseObject.unpinAllInBackground("Basket", new DeleteCallback() {
//                    @Override
//                    public void done(ParseException e) {
//                        basketViewAdapter.notifyDataSetChanged();
//                        basketViewAdapter.loadObjects();
//                    }
//                });

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
