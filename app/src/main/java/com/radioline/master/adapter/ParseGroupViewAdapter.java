package com.radioline.master.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.radioline.master.myapplication.R;
import com.radioline.master.parse.ParseGroups;
import com.radioline.master.parse.ParseItems;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by master on 13.01.2015.
 */
public class ParseGroupViewAdapter extends ParseQueryAdapter<ParseGroups> {
    private final int[] bgColors = new int[]{Color.rgb(245, 245, 245), Color.rgb(224, 255, 255)};
    private Context context;
    private LayoutInflater inflater;
    private ImageLoader imageLoader;
    private LinkedHashSet mySet;
    private DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.ic_folderupload)
            .showImageForEmptyUri(R.drawable.ic_folderutil)
            .showImageOnFail(R.drawable.ic_folderutil)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .build();

    public ParseGroupViewAdapter(Context context) {
        super(context, new ParseQueryAdapter.QueryFactory<ParseGroups>() {
            public ParseQuery create() {
                ParseQuery query = ParseGroups.getQuery();
                query.setMaxCacheAge(TimeUnit.HOURS.toMillis(4));
                query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
                query.whereEqualTo("parentid", "00000000-0000-0000-0000-000000000000");
                query.whereEqualTo("Enable", true);
                query.orderByAscending("sortcode");
                return query;
            }
        });
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.imageLoader = ImageLoader.getInstance();
        this.mySet = new LinkedHashSet();
    }

    public ParseGroupViewAdapter(Context context, Boolean all) {
        super(context, new ParseQueryAdapter.QueryFactory<ParseGroups>() {
            public ParseQuery create() {
                ParseQuery queryFirst = ParseGroups.getQuery();
                queryFirst.setMaxCacheAge(TimeUnit.HOURS.toMillis(4));
                queryFirst.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
                queryFirst.whereEqualTo("parentid", "00000000-0000-0000-0000-000000000000");
                queryFirst.whereEqualTo("Enable", true);
                //queryFirst.orderByAscending("sortcode");

                ParseQuery querySecond = ParseGroups.getQuery();
                querySecond.setMaxCacheAge(TimeUnit.HOURS.toMillis(4));
                querySecond.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
                querySecond.whereMatchesQuery("parseGroupId", queryFirst);
                querySecond.whereEqualTo("Enable", true);


                List<ParseQuery<ParseGroups>> queries = new ArrayList<ParseQuery<ParseGroups>>();
                queries.add(queryFirst);
                queries.add(querySecond);

                ParseQuery<ParseGroups> mainQuery = ParseQuery.or(queries);


                return mainQuery;
            }
        });
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.imageLoader = ImageLoader.getInstance();
        this.mySet = new LinkedHashSet();
    }


    public ParseGroupViewAdapter(Context context, final String parentId) {
        super(context, new ParseQueryAdapter.QueryFactory<ParseGroups>() {
            public ParseQuery create() {
                ParseQuery query = ParseGroups.getQuery();
                query.setMaxCacheAge(TimeUnit.HOURS.toMillis(4));
                query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
                query.whereEqualTo("parentid", parentId);
                query.orderByAscending("sortcode");
                return query;
            }
        });
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.imageLoader = ImageLoader.getInstance();
        this.mySet = new LinkedHashSet();
    }

    /**
     * Notifies the attached observers that the underlying data has been changed
     * and any View reflecting the data set should refresh itself.
     */
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public View getItemView(ParseGroups object, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.groupview, null);
            holder.tvGroupName = (TextView) view.findViewById(R.id.tvName);
            holder.ivGroup = (ParseImageView) view.findViewById(R.id.imageView3);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();

        }
        super.getItemView(object, view, parent);
        mySet.add(object);
        int colorPosition = new ArrayList<ParseItems>(mySet).indexOf(object) % bgColors.length;
        view.setBackgroundColor(bgColors[colorPosition]);

        holder.tvGroupName.setText(object.getString("name"));
        ParseFile photoFile = object.getParseFile("image");
//        holder.ivGroup.setParseFile(photoFile);
//        holder.ivGroup.loadInBackground();
        //ImageLoader imageLoader = ImageLoader.getInstance();
        String imageurl = "";
        if (photoFile != null) {
            imageurl = photoFile.getUrl();
        }


        imageLoader.displayImage(imageurl, holder.ivGroup, options);


        return view;
    }

    public class ViewHolder {
        TextView tvGroupName;
        ParseImageView ivGroup;
    }

}
