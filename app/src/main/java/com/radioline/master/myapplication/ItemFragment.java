package com.radioline.master.myapplication;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.radioline.master.adapter.ParseItemsViewAdapter;

public class ItemFragment extends Fragment {
    private View rootView;
    private ListView lvItem;
    private ParseItemsViewAdapter itemsViewAdapterAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_item,container,false);
        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        lvItem = (ListView) getView().findViewById(R.id.lvItem);



        itemsViewAdapterAdapter = new ParseItemsViewAdapter(this, getIntent().getStringExtra("parentid"));
        itemsViewAdapterAdapter.setAutoload(true);
        itemsViewAdapterAdapter.setPaginationEnabled(false);
        lvItem.setAdapter(itemsViewAdapterAdapter);
    }

}
