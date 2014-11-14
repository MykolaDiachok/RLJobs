package com.radioline.master.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.badoo.mobile.util.WeakHandler;

import com.parse.Parse;
import com.radioline.master.SwipeDetector.SwipeDetector;
import com.radioline.master.basic.Item;
import com.radioline.master.basic.ItemViewAdapter;
import com.radioline.master.soapconnector.Converts;
import com.splunk.mint.Mint;

import java.util.concurrent.ExecutionException;


public class ItemActivity extends Activity implements AdapterView.OnItemClickListener {


    //http://stackoverflow.com/questions/4373485/android-swipe-on-list
    private ListView lvItem;
    private WeakHandler handler = new WeakHandler();
    private ProgressDialog dialog;
    private ItemViewAdapter itemViewAdapter;
    private SwipeDetector swipeDetector;

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
        swipeDetector = new SwipeDetector();
        lvItem = (ListView)findViewById(R.id.lvItem);
        lvItem.setOnItemClickListener(this);
        lvItem.setOnTouchListener(swipeDetector);
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
                        if (dialog!=null){
                            if (dialog.isShowing()){
                                try {
                                    dialog.dismiss();
                                }  catch (IllegalArgumentException e){
                                    e.printStackTrace();
                                };
                            }
                        }
                        if (itemViewAdapter!=null){
                        lvItem.setAdapter(itemViewAdapter);}
                    }
                });
            }
        };

        t.start();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_item, menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_scan:
                intent = new Intent(this, ScanActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_basket:
                intent = new Intent(this,BasketActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Item item = (Item) adapterView.getItemAtPosition(position);
//        if (swipeDetector.swipeDetected()){
//            if((swipeDetector.getAction()== SwipeDetector.Action.RL)||(swipeDetector.getAction()== SwipeDetector.Action.LR)){
//                Toast.makeText(ItemActivity.this,"swipe:"+item.getName()+" +1",Toast.LENGTH_SHORT).show();
//
//            }
//        } else {
            Intent intent = new Intent(this,PicActivity.class);
            intent.putExtra("itemid",item.getId());
            intent.putExtra("Name",item.getName());
            startActivity(intent);
//        }



    }
}
