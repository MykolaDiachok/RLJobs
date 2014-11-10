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

import com.google.zxing.client.android.Intents;
import com.radioline.master.basic.Group;
import com.radioline.master.basic.GroupViewAdapter;
import com.radioline.master.soapconnector.Converts;
import com.splunk.mint.Mint;


import java.util.concurrent.ExecutionException;


public class Login extends Activity implements View.OnClickListener,AdapterView.OnItemClickListener {


    private ListView listView;
    final String LOG_TAG = "myLogs";
    private Handler handler = new Handler();
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

        Mint.initAndStartSession(this, "3b65ddeb");
        //Mint.enableDebug();

        setContentView(R.layout.activity_login);
        listView = (ListView)findViewById(R.id.listView);

        listView.setOnItemClickListener(this);


        dialog = ProgressDialog.show(this, getString(R.string.ProgressDialogTitle),
                getString(R.string.ProgressDialogMessage));
        Thread t = new Thread() {
            public void run() {
                Converts tg = new Converts();
                try {
                    groupViewAdapter = new GroupViewAdapter(Login.this, tg.getGroupsArrayListFromServer());

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                handler.post(new Runnable() {
                    public void run() {
                        dialog.dismiss();
                        if (groupViewAdapter!=null){
                        listView.setAdapter(groupViewAdapter);}
                    };
                });
            }
        };

        t.start();




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        switch (item.getItemId()) {
            case R.id.action_scan:
                Intent intent = new Intent(this,ScanActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.bGetGroups:
//                Converts tg = new Converts();
//                try {
//                    GroupViewAdapter groupViewAdapter = new GroupViewAdapter(this, tg.getGroupsArrayListFromServer());
//                    listView.setAdapter(groupViewAdapter);
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                break;
//            case R.id.btLoadPNG:
////                Converts tg1 = new Converts();
////                try {
////                    Bitmap tbmp = tg1.getBitMapFromServer("aaa",100,100,50,true);
////                    imageView2.setImageBitmap(tbmp);
////                } catch (ExecutionException e) {
////                    e.printStackTrace();
////                } catch (InterruptedException e) {
////                    e.printStackTrace();
////                }
//
//                break;
//        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Group itemgroup = (Group) adapterView.getItemAtPosition(position);
        Intent intent = new Intent(this,SecondGroup.class);
        intent.putExtra("parentid",itemgroup.getId());
        intent.putExtra("Name",itemgroup.getName());
        startActivity(intent);
    }


}
