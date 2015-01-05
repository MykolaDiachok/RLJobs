package com.radioline.master.basic;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.radioline.master.myapplication.ItemActivity;
import com.radioline.master.myapplication.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by master on 04.01.2015.
 */
public class ParseItemsViewAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    ImageLoader imageLoader;
    private List<ParseItems> itemslist = null;
    private ArrayList<ParseItems> arraylist;

    public ParseItemsViewAdapter(Context context,
                                 List<ParseItems> itemslist) {
        this.context = context;
        this.itemslist = itemslist;
        inflater = LayoutInflater.from(context);
        this.arraylist = new ArrayList<ParseItems>();
        this.arraylist.addAll(itemslist);
        // imageLoader = new ImageLoader(context);
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return itemslist.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return itemslist.get(position);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link android.view.LayoutInflater#inflate(int, android.view.ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.itemview, null);
            // Locate the TextViews in listview_item.xml
            holder.tvItemName = (TextView) view.findViewById(R.id.tvItemName);
            holder.tvItemUSD = (TextView) view.findViewById(R.id.tvItemUSD);
            holder.tvItemUAH = (TextView) view.findViewById(R.id.tvItemUAH);
            holder.ivItem = (ImageView) view.findViewById(R.id.ivItem);
            holder.btAdd = (Button) view.findViewById(R.id.btAdd);
            holder.btDel = (Button) view.findViewById(R.id.btDel);
            holder.tvQuantity = (TextView) view.findViewById(R.id.tvQuantity);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.tvItemName.setText(itemslist.get(position).getName());
        DecimalFormat dec = new DecimalFormat("0.00");
        holder.tvItemUSD.setText("$ " + dec.format(itemslist.get(position).getPrice()));
        holder.tvItemUAH.setText("₴ " + dec.format(itemslist.get(position).getPriceUAH()));

        // Set the results into ImageView
        imageLoader.DisplayImage(itemslist.get(position).getFlag(),
                holder.flag);
        // Listen for ListView Item Click
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Send single item click data to SingleItemView Class
                Intent intent = new Intent(context, ItemActivity.class);
                // Pass all data rank
                intent.putExtra("ObjectId",
                        (itemslist.get(position).getObjectId()));
                context.startActivity(intent);
            }
        });
        return view;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        itemslist.clear();
        if (charText.length() == 0) {
            itemslist.addAll(arraylist);
        } else {
            for (ParseItems wp : arraylist) {
                if (wp.getName().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    itemslist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class ViewHolder {
        TextView tvItemName;
        TextView tvItemUSD;
        TextView tvItemUAH;
        ImageView ivItem;
        Button btAdd;
        Button btDel;
        TextView tvQuantity;
    }

}
