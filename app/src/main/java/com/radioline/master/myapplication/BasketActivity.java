package com.radioline.master.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.radioline.master.basic.Basket;
import com.radioline.master.basic.BasketViewAdapter;

import java.util.List;

public class BasketActivity extends Activity {

    //private ParseQueryAdapter<ParseObject> mainAdapter;
    private ListView lvBasket;
    private BasketViewAdapter basketViewAdapter;


    @Override
    protected void onResume() {

        basketViewAdapter.notifyDataSetChanged();
        basketViewAdapter.loadObjects();
        lvBasket.setAdapter(basketViewAdapter);
        basketViewAdapter.loadObjects();
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_basket);


        basketViewAdapter = new BasketViewAdapter(this);
        basketViewAdapter.setAutoload(true);
        basketViewAdapter.setPaginationEnabled(false);
        lvBasket = (ListView) findViewById(R.id.lvBasket);

        lvBasket.setAdapter(basketViewAdapter);

        lvBasket.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("onItemClick", "click");
                int t = view.getId();
            }
        });


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
        Boolean rtvalue = true;
        switch (item.getItemId()) {
            case R.id.action_settings:
                rtvalue = true;
                break;
            case R.id.action_dispatch:
                intent = new Intent(this, DispatchActivity.class);
                startActivity(intent);
                rtvalue = true;
                break;
            case R.id.action_clearbasket:
                ParseQuery<Basket> query = Basket.getQuery();
                query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
                query.whereEqualTo("user", ParseUser.getCurrentUser());
                query.whereNotEqualTo("sent", true);
                query.findInBackground(new FindCallback<Basket>() {
                    @Override
                    public void done(List<Basket> baskets, ParseException e) {
                        ParseObject.deleteAllInBackground(baskets, new DeleteCallback() {
                            @Override
                            public void done(ParseException e) {
                                basketViewAdapter.loadObjects();
                            }
                        });
                    }
                });

                rtvalue = true;
                break;
            case R.id.action_refresh:
                basketViewAdapter.loadObjects();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }


        return rtvalue;
    }


}
