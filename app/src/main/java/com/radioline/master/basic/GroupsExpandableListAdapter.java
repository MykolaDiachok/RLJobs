package com.radioline.master.basic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.radioline.master.myapplication.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by mikoladyachok on 1/14/15.
 */
public class GroupsExpandableListAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private List<ParseGroups> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<ParseGroups, List<ParseGroups>> _listDataChild;

    private ImageLoader imageLoader;
    private DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.ic_folderupload)
            .showImageForEmptyUri(android.R.color.transparent)
            .showImageOnFail(R.drawable.ic_folderutil)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .build();

    public GroupsExpandableListAdapter(Context context, List<ParseGroups> listDataHeader,
                                       HashMap<ParseGroups, List<ParseGroups>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.imageLoader = ImageLoader.getInstance();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int childrenCount = 0;
        if (this._listDataChild != null) {
            childrenCount = this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .size();
        }

        return childrenCount;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final ViewHolderFirstGroup holder;
        ParseGroups cGroup = (ParseGroups) getGroup(groupPosition);
        String headerTitle = cGroup.getName();
        if (convertView == null) {
            holder = new ViewHolderFirstGroup();
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.groupview, null);

            holder.tvGroupName = (TextView) convertView.findViewById(R.id.tvName);
            holder.ivGroup = (ParseImageView) convertView.findViewById(R.id.imageView3);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderFirstGroup) convertView.getTag();
        }

        holder.tvGroupName.setText(cGroup.getString("name"));
        holder.tvGroupName.setTypeface(null, Typeface.BOLD);
        holder.tvGroupName.setText(headerTitle);
        ParseFile photoFile = cGroup.getParseFile("image");

        String imageurl = "";
        if (photoFile != null) {
            imageurl = photoFile.getUrl();
        }

        imageLoader.displayImage(imageurl, holder.ivGroup, options);


        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ParseGroups cGroup = (ParseGroups) getChild(groupPosition, childPosition);
        final String childText = cGroup.getName();

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.groupview_second, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.tvName);

        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public class ViewHolderFirstGroup {
        TextView tvGroupName;
        ParseImageView ivGroup;
    }
}
