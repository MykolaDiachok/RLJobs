package com.radioline.master.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.radioline.master.basic.Item;
import com.radioline.master.basic.ItemViewAdapter;
import com.radioline.master.soapconnector.Converts;
import com.splunk.mint.Mint;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class ScanActivity extends Activity implements AdapterView.OnItemClickListener {
    private Button btScan;
    private ListView lvScan;
    private Handler handler = new Handler();
    private ProgressDialog dialog;
    private ItemViewAdapter itemViewAdapter;
    private String contents;

    final Runnable showToastMessage = new Runnable() {
        public void run() {
            Toast.makeText(ScanActivity.this, "It's barcode="+contents+" not found in database, perhaps the item is not available", Toast.LENGTH_SHORT).show();
        }
    };


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
        Mint.initAndStartSession(ScanActivity.this, "3b65ddeb");
        //Mint.enableDebug();
        setContentView(R.layout.activity_scan);

        lvScan = (ListView) findViewById(R.id.lvScan);
        lvScan.setOnItemClickListener(this);

        btScan = (Button) findViewById(R.id.btScan);
        btScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                IntentIntegrator integrator = new IntentIntegrator(ScanActivity.this);

                //integrator.addExtra("SCAN_WIDTH", 640);
                //integrator.addExtra("SCAN_HEIGHT", 480);
                //integrator.addExtra("SCAN_MODE", "ONE_D_MODE");
                integrator.addExtra("SCAN_MODE", "PRODUCT_MODE");
                integrator.addExtra("SCAN_FORMATS", "EAN_13");//EAN_13,CODE_128
                //customize the prompt message before scanning
                integrator.addExtra("PROMPT_MESSAGE", "Scanner Start!");
                integrator.initiateScan(IntentIntegrator.PRODUCT_CODE_TYPES);
                //Intent intent = integrator.createScanIntent();
                //startActivityForResult(intent, IntentIntegrator.REQUEST_CODE);

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (result != null) {
            contents = result.getContents();
            if (contents != null) {
                //textViewScan.setText(contents);

                dialog = ProgressDialog.show(this, getString(R.string.ProgressDialogTitle),
                        getString(R.string.ProgressDialogMessage));
                Thread t = new Thread() {
                    public void run() {
                        Converts tg = new Converts();
                        try {
                            ArrayList<Item> item = tg.getItemsArrayListFromServerWithBarcode(contents, false);
                            if (item==null){
//                                Context context = getApplicationContext();
//                                CharSequence text = "It's barcode=" + contents+" not found in database, perhaps the item is not available";
//                                int duration = Toast.LENGTH_SHORT;
//
//                                Toast toast = Toast.makeText(ScanActivity.this, text, duration);
//                                toast.show();
                                handler.post(showToastMessage);
                            }
                             else{
                                itemViewAdapter = new ItemViewAdapter(ScanActivity.this, item);}

                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        handler.post(new Runnable() {
                            public void run() {
                                dialog.dismiss();
                                lvScan.setAdapter(itemViewAdapter);
                            }
                        });
                    }
                };

                t.start();



            } else {
                //  String T = getString(R.string.result_failed_why);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scan, menu);
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
