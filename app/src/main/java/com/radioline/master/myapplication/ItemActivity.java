package com.radioline.master.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.badoo.mobile.util.WeakHandler;
import com.parse.ParseQueryAdapter;
import com.radioline.master.basic.Item;
import com.radioline.master.basic.ItemViewAdapter;
import com.radioline.master.basic.ParseItems;
import com.radioline.master.basic.ParseItemsViewAdapter;
import com.splunk.mint.Mint;

import java.util.ArrayList;


public class ItemActivity extends Activity implements AdapterView.OnItemClickListener {


    //http://stackoverflow.com/questions/4373485/android-swipe-on-list
    private ListView lvItem;
    private EditText tvSearch;
    private WeakHandler handler = new WeakHandler();
    private ProgressDialog dialog;
    private ItemViewAdapter itemViewAdapter;
    private Thread t;
    private ArrayList<Item> itemArray;

    private ParseQueryAdapter<ParseItems> mainAdapter;
    private ParseItemsViewAdapter itemsViewAdapterAdapter;
    private final TextWatcher mTextEditorWatcher = new TextWatcher() {


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }


        @Override
        public void afterTextChanged(Editable s) {
            itemsViewAdapterAdapter = new ParseItemsViewAdapter(ItemActivity.this, getIntent().getStringExtra("parentid"), s.toString());
            itemsViewAdapterAdapter.loadObjects();
            lvItem.setAdapter(itemsViewAdapterAdapter);
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
        if ((t != null) && (t.isAlive())) {
            t.interrupt();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Mint.initAndStartSession(this, getString(R.string.mint));
        setContentView(R.layout.activity_item);

        this.setTitle(getIntent().getStringExtra("Name"));
        tvSearch = (EditText) findViewById(R.id.etSearch);
        tvSearch.addTextChangedListener(mTextEditorWatcher);



        lvItem = (ListView) findViewById(R.id.lvItem);
        lvItem.setOnItemClickListener(this);


        itemsViewAdapterAdapter = new ParseItemsViewAdapter(this, getIntent().getStringExtra("parentid"));
        itemsViewAdapterAdapter.setAutoload(true);
        itemsViewAdapterAdapter.setPaginationEnabled(false);
        lvItem.setAdapter(itemsViewAdapterAdapter);

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
        Boolean rtvalue = true;
        switch (item.getItemId()) {
            case R.id.action_search:
                intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                rtvalue = true;
                break;
            case R.id.action_scan:
                intent = new Intent(this, ScanActivity.class);
                startActivity(intent);
                rtvalue = true;
                break;
            case R.id.action_basket:
                intent = new Intent(this, BasketActivity.class);
                startActivity(intent);
                rtvalue = true;
                break;
            case R.id.action_settings:
                rtvalue = true;
                break;
            case R.id.action_refresh:
                itemsViewAdapterAdapter.loadObjects();
                lvItem.setAdapter(itemsViewAdapterAdapter);
//                loadData();
                rtvalue = true;
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return rtvalue;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        ParseItems item = (ParseItems) adapterView.getItemAtPosition(position);

        Intent intent = new Intent(this, PicActivity.class);
        intent.putExtra("objectId", item.getObjectId());
        intent.putExtra("Name", item.getName());
        startActivity(intent);


    }

}
