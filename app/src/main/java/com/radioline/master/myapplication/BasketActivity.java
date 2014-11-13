package com.radioline.master.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;
import com.radioline.master.basic.BasketViewAdapter;
import com.radioline.master.myapplication.R;
import com.splunk.mint.Mint;

public class BasketActivity extends Activity {

    private ParseQueryAdapter<ParseObject> mainAdapter;
    private ListView lvBasket;
    private BasketViewAdapter basketViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Mint.initAndStartSession(this, "3b65ddeb");

        setContentView(R.layout.activity_basket);

        mainAdapter = new ParseQueryAdapter<ParseObject>(this, "basket");
        basketViewAdapter = new BasketViewAdapter(this);
        lvBasket = (ListView) findViewById(R.id.lvBasket);
        lvBasket.setAdapter(basketViewAdapter);
        basketViewAdapter.loadObjects();
        mainAdapter.loadObjects();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_basket, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
