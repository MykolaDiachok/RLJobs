package com.radioline.master.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.badoo.mobile.util.WeakHandler;
import com.radioline.master.basic.Group;
import com.radioline.master.basic.GroupViewAdapter;
import com.radioline.master.soapconnector.Converts;
import com.splunk.mint.Mint;

import java.util.concurrent.ExecutionException;


public class SecondGroupActivity extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private ListView lvSecond;
    private WeakHandler handler = new WeakHandler();
    private ProgressDialog dialog;
    private GroupViewAdapter groupViewAdapter;

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

        Mint.initAndStartSession(this, getString(R.string.mint));
        //Mint.enableDebug();

        setContentView(R.layout.activity_secondgroup);
        lvSecond = (ListView) findViewById(R.id.lvSecond);
        lvSecond.setOnItemClickListener(this);
        this.setTitle(getIntent().getStringExtra("Name"));
//        Converts tg = new Converts();
//        try {
//            GroupViewAdapter groupViewAdapter = new GroupViewAdapter(this, tg.getGroupsArrayListFromServer(getIntent().getStringExtra("parentid")));
//            listView.setAdapter(groupViewAdapter);
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
                    groupViewAdapter = new GroupViewAdapter(SecondGroupActivity.this, tg.getGroupsArrayListFromServer(getIntent().getStringExtra("parentid")));

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                handler.post(new Runnable() {
                    public void run() {
                        if (dialog != null) {
                            if (dialog.isShowing()) {
                                try {
                                    dialog.dismiss();
                                } catch (IllegalArgumentException e) {
                                    e.printStackTrace();
                                }
                                ;
                            }
                        }
                        if (groupViewAdapter != null) {
                            lvSecond.setAdapter(groupViewAdapter);
                        }
                    }
                });
            }
        };

        t.start();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_secondgroup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_search:
                intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
            case R.id.action_scan:
                intent = new Intent(this, ScanActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_basket:
                intent = new Intent(this, BasketActivity.class);
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
        Group itemgroup = (Group) adapterView.getItemAtPosition(position);
        Intent intent = new Intent(this, ItemActivity.class);
        intent.putExtra("parentid", itemgroup.getId());
        intent.putExtra("Name", itemgroup.getName());
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {

    }
}
