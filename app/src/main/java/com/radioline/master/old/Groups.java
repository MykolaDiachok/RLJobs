package com.radioline.master.old;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.radioline.master.adapter.GroupsExpandableListAdapter;
import com.radioline.master.myapplication.ItemActivity;
import com.radioline.master.myapplication.R;
import com.radioline.master.parse.ParseGroups;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Groups extends Activity {

    GroupsExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<ParseGroups> listDataHeader;
    HashMap<ParseGroups, List<ParseGroups>> listDataChild;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                ParseGroups itemgroup = listDataChild.get(
                        listDataHeader.get(groupPosition)).get(
                        childPosition);

                Intent intent = new Intent(getApplicationContext(), ItemActivity.class);
                intent.putExtra("parentid", itemgroup.getGroupid());
                intent.putExtra("Name", itemgroup.getName());
                startActivity(intent);
                return false;
            }
        });
        // preparing list data
        prepareListData();

        listAdapter = new GroupsExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

    }

    private void prepareListData() {
        listDataHeader = new ArrayList<ParseGroups>();
        listDataChild = new HashMap<ParseGroups, List<ParseGroups>>();

        ParseQuery queryFirst = ParseGroups.getQuery();
        queryFirst.setMaxCacheAge(TimeUnit.HOURS.toMillis(4));
        queryFirst.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
        queryFirst.whereEqualTo("parentid", "00000000-0000-0000-0000-000000000000");
        queryFirst.whereEqualTo("Enable", true);
        queryFirst.orderByAscending("sortcode");
        queryFirst.findInBackground(new FindCallback<ParseGroups>() {
            @Override
            public void done(List<ParseGroups> list, ParseException e) {
                for (final ParseGroups parseGroups : list) {
                    listDataHeader.add(parseGroups);

                    ParseQuery querySecond = ParseGroups.getQuery();
                    querySecond.setMaxCacheAge(TimeUnit.HOURS.toMillis(4));
                    querySecond.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
                    querySecond.whereEqualTo("parseGroupId", parseGroups);
                    querySecond.whereEqualTo("Enable", true);
                    querySecond.orderByAscending("sortcode");
//                    try {
//
//                        listDataChild.put(parseGroups,querySecond.find());
//                    } catch (ParseException e1) {
//                        e1.printStackTrace();
//                    }
                    querySecond.findInBackground(new FindCallback<ParseGroups>() {
                        @Override
                        public void done(List<ParseGroups> list, ParseException e) {
                            listDataChild.put(parseGroups, list);
                            //listAdapter.notifyDataSetChanged();
                        }
                    });


                }
                listAdapter.notifyDataSetChanged();
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_groups, menu);
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
