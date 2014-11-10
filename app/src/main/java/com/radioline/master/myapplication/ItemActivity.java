package com.radioline.master.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.radioline.master.basic.Item;
import com.radioline.master.basic.ItemViewAdapter;
import com.radioline.master.soapconnector.Converts;
import com.splunk.mint.Mint;

import java.util.concurrent.ExecutionException;


public class ItemActivity extends Activity implements AdapterView.OnItemClickListener {

    private ListView lvItem;
    private Handler handler = new Handler();
    private ProgressDialog dialog;
    private ItemViewAdapter itemViewAdapter;

    @Override
    protected void onResume() {
        super.onResume();
        Mint.startSession(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Mint.closeSession(this);
        Mint.flush();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Mint.initAndStartSession(this, "3b65ddeb");
        //Mint.enableDebug();

        setContentView(R.layout.activity_item);

        this.setTitle(getIntent().getStringExtra("Name"));

        lvItem = (ListView)findViewById(R.id.lvItem);
        lvItem.setOnItemClickListener(this);
//        Converts tg = new Converts();
//        try {
//            ItemViewAdapter itemViewAdapter = new ItemViewAdapter(this, tg.getItemsArrayListFromServer(getIntent().getStringExtra("parentid")));
//            lvItem.setAdapter(itemViewAdapter);
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        dialog = ProgressDialog.show(this, getString(R.string.ProgressDialogTitle),
                getString(R.string.ProgressDialogMessage));
        Thread t = new Thread() {
            public void run() {
                Converts tg = new Converts();
                try {
                    itemViewAdapter = new ItemViewAdapter(ItemActivity.this, tg.getItemsArrayListFromServer(getIntent().getStringExtra("parentid")));

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                handler.post(new Runnable() {
                    public void run() {
                        dialog.dismiss();
                        lvItem.setAdapter(itemViewAdapter);
                    }
                });
            }
        };

        t.start();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item, menu);
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Item item = (Item) adapterView.getItemAtPosition(position);
        Intent intent = new Intent(this,PicActivity.class);
        intent.putExtra("itemid",item.getId());
        intent.putExtra("Name",item.getName());
        startActivity(intent);
    }
}
