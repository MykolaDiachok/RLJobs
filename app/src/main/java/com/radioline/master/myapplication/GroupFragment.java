package com.radioline.master.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.radioline.master.adapter.GroupsExpandableListAdapter;
import com.radioline.master.myapplication.R;
import com.radioline.master.parse.ParseGroups;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class GroupFragment extends Fragment {
    private View rootView;
    private GroupsExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private List<ParseGroups> listDataHeader;
    private HashMap<ParseGroups, List<ParseGroups>> listDataChild;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        // get the listview
        expListView = (ExpandableListView)getView().findViewById(R.id.lvExp);
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                ParseGroups itemgroup = listDataChild.get(
                        listDataHeader.get(groupPosition)).get(
                        childPosition);
                Bundle bundle = new Bundle();
                bundle.putString("parentid", itemgroup.getGroupid());
                Fragment frmItems = new ItemFragment();
                frmItems.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.container, frmItems).addToBackStack(null).commit();
//                Intent intent = new Intent(getApplicationContext(), ItemActivity.class);
//                intent.putExtra("parentid", itemgroup.getGroupid());
//                intent.putExtra("Name", itemgroup.getName());
//                startActivity(intent);
                return false;
            }
        });
        // preparing list data
        prepareListData();

        listAdapter = new GroupsExpandableListAdapter(getView().getContext(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_group,container,false);




        return rootView;
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


}


