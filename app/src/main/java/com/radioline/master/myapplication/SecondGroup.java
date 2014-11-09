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

import com.radioline.master.basic.Group;
import com.radioline.master.basic.GroupViewAdapter;
import com.radioline.master.soapconnector.Converts;

import java.util.concurrent.ExecutionException;


public class SecondGroup extends Activity implements AdapterView.OnItemClickListener {

    private ListView lvSecond;
    private Handler handler = new Handler();
    private ProgressDialog dialog;
    private GroupViewAdapter groupViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_group);
        lvSecond = (ListView)findViewById(R.id.lvSecond);
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
                    groupViewAdapter = new GroupViewAdapter(SecondGroup.this, tg.getGroupsArrayListFromServer(getIntent().getStringExtra("parentid")));

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                handler.post(new Runnable() {
                    public void run() {
                        dialog.dismiss();
                        lvSecond.setAdapter(groupViewAdapter);
                    };
                });
            }
        };

        t.start();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_second_group, menu);
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
        Group itemgroup = (Group) adapterView.getItemAtPosition(position);
        Intent intent = new Intent(this,ItemActivity.class);
        intent.putExtra("parentid",itemgroup.getId());
        intent.putExtra("Name",itemgroup.getName());
        startActivity(intent);
    }

}
