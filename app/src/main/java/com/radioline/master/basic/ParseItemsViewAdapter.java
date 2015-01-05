package com.radioline.master.basic;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.johnpersano.supertoasts.SuperToast;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.ConfigCallback;
import com.parse.ParseConfig;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.radioline.master.myapplication.PicActivity;
import com.radioline.master.myapplication.R;

import java.text.DecimalFormat;

/**
 * Created by master on 04.01.2015.
 */
public class ParseItemsViewAdapter extends ParseQueryAdapter<ParseItems> {
    private Context context;
    private LayoutInflater inflater;
    private ImageLoader imageLoader;
    private ParseFile restAverage = null;
    private ParseFile restMax = null;
    private ParseFile restMin = null;

    public ParseItemsViewAdapter(Context context, final ParseGroups parseGroupId) {

        super(context, new ParseQueryAdapter.QueryFactory<ParseItems>() {
            public ParseQuery<ParseItems> create() {
                // Here we can configure a ParseQuery to display
                // only top-rated meals.
                ParseQuery query = new ParseQuery("ParseItems");
                query.whereEqualTo("parseGroupId", parseGroupId);
                query.orderByDescending("Name");
                return query;
            }
        });
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.imageLoader = ImageLoader.getInstance();
        ParseConfig.getInBackground(new ConfigCallback() {
            @Override
            public void done(ParseConfig config, ParseException e) {
                restAverage = config.getParseFile("RestAverage");
                restMax = config.getParseFile("RestMax");
                restMin = config.getParseFile("RestMin");
                Log.d("TAG", "Loading images files");
            }
        });
    }


    public ParseItemsViewAdapter(Context context, final String parentId) {

        super(context, new ParseQueryAdapter.QueryFactory<ParseItems>() {
            public ParseQuery<ParseItems> create() {
                // Here we can configure a ParseQuery to display
                // only top-rated meals.
                ParseQuery query = new ParseQuery("ParseItems");
                query.whereEqualTo("GroupId", parentId);
                query.whereEqualTo("Availability", true);
                query.orderByDescending("Name");
                return query;
            }
        });
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.imageLoader = ImageLoader.getInstance();
        ParseConfig.getInBackground(new ConfigCallback() {
            @Override
            public void done(ParseConfig config, ParseException e) {
                restAverage = config.getParseFile("RestAverage");
                restMax = config.getParseFile("RestMax");
                restMin = config.getParseFile("RestMin");
                Log.d("TAG", "Loading images files");
            }
        });
    }

    @Override
    public View getItemView(final ParseItems object, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.itemview, null);
            // Locate the TextViews in listview_item.xml
            holder.tvItemName = (TextView) view.findViewById(R.id.tvItemName);
            holder.tvItemUSD = (TextView) view.findViewById(R.id.tvItemUSD);
            holder.tvItemUAH = (TextView) view.findViewById(R.id.tvItemUAH);
            holder.ivItem = (ParseImageView) view.findViewById(R.id.ivItem);
            holder.ivRest = (ParseImageView) view.findViewById(R.id.ivRest);
            holder.btAdd = (Button) view.findViewById(R.id.btAdd);
            holder.btDel = (Button) view.findViewById(R.id.btDel);
            holder.tvQuantity = (TextView) view.findViewById(R.id.tvQuantity);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        super.getItemView(object, view, parent);

        this.setOnClickListener(holder.btAdd, object);
        this.setOnClickListener(holder.btDel, object);
        this.setOnClickListener(holder.ivItem, object);

        holder.tvItemName.setText(object.getName());
        DecimalFormat dec = new DecimalFormat("0.00");
        holder.tvItemUSD.setText("$ " + dec.format(object.getPrice()));
        holder.tvItemUAH.setText("₴ " + dec.format(object.getPriceUAH()));


        ParseFile photoFile = object.getImage();
        //Get singleton instance of ImageLoader

        if (photoFile != null) {
            //Load the image from the url into the ImageView.
            imageLoader.displayImage(photoFile.getUrl(), holder.ivItem);
        }

        if ((object.getStock() > 0) && (object.getStock() <= 5)) {
            imageLoader.displayImage(restMin.getUrl(), holder.ivRest);
        } else if ((object.getStock() > 5) && (object.getStock() <= 30)) {
            imageLoader.displayImage(restAverage.getUrl(), holder.ivRest);
        } else if (object.getStock() > 30) {
            imageLoader.displayImage(restMax.getUrl(), holder.ivRest);
        }



        // Listen for ListView Item Click
//        view.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                // Send single item click data to SingleItemView Class
//                Intent intent = new Intent(context, ItemActivity.class);
//                // Pass all data rank
//                intent.putExtra("ObjectId",
//                        (object.getObjectId()));
//                context.startActivity(intent);
//            }
//        });
        return view;


    }


    private void delItem(ParseItems finalitem) {
        ParseQuery<Basket> query = Basket.getQuery();
        query.fromLocalDatastore();
        query.whereEqualTo("productId", finalitem.getItemId());
        int currentcount = 0;
        try {
            Basket localbasket = query.getFirst();
            currentcount = localbasket.getQuantity() - 1;
            if (currentcount < 0) {
                currentcount = 0;
                localbasket.unpin();
            } else {
                localbasket.setQuantity(currentcount);
                localbasket.pin();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        SuperToast superToast = new SuperToast(context);
        superToast.setDuration(SuperToast.Duration.SHORT);
        superToast.setText("del: " + finalitem.getName() + "-1=" + currentcount);
        //superToast.setIconResource(R.drawable.image, SuperToast.IconPosition.LEFT);
        superToast.setIcon(R.drawable.del, SuperToast.IconPosition.LEFT);
        superToast.show();

        //Create a view here
//        LinearLayout v = new LinearLayout(context);
        //populate layout with your image and text or whatever you want to put in here

//        Toast toast = new Toast(context);
//        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
//        toast.setDuration(Toast.LENGTH_LONG);
//        toast.setView(v);
//        toast.setText("del: " + finalitem.getName() + "-1=" + currentcount);
//        toast.show();


        //Toast.makeText(context, , Toast.LENGTH_SHORT).show();
    }

    private void addItem(ParseItems finalitem) {

        ParseQuery<Basket> query = Basket.getQuery();
        query.fromLocalDatastore();
        query.whereEqualTo("productId", finalitem.getItemId());
        int currentcount = 1;
        try {
            Basket localbasket = query.getFirst();
            currentcount = localbasket.getQuantity() + 1;
            localbasket.setQuantity(currentcount);
            localbasket.pin();
        } catch (ParseException e) {
            e.printStackTrace();
            Basket basket = new Basket();
            basket.setProductId(finalitem.getItemId());
            basket.setName(finalitem.getName());
            basket.setRequiredpriceUSD(finalitem.getPrice());
            basket.setRequiredpriceUAH(finalitem.getPriceUAH());
            basket.setQuantity(1);
            try {
                basket.pin();
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
        }

        SuperToast superToast = new SuperToast(context);
        superToast.setDuration(SuperToast.Duration.SHORT);
        superToast.setText("add: " + finalitem.getName() + "+1=" + currentcount);
        //superToast.setIconResource(R.drawable.image, SuperToast.IconPosition.LEFT);
        superToast.setIcon(R.drawable.add, SuperToast.IconPosition.LEFT);
        superToast.show();

//        Toast.makeText(context, "add: " + finalitem.getName() + "+1=" + currentcount, Toast.LENGTH_SHORT).show();
    }

    private void setOnClickListener(View view, final ParseItems finalitem) {

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.ivItem:
                        Intent intent = new Intent(context, PicActivity.class);
                        intent.putExtra("itemid", finalitem.getItemId());
                        intent.putExtra("Name", finalitem.getName());
                        context.startActivity(intent);
                        break;
                    case R.id.btAdd:
                        addItem(finalitem);
                        break;
                    case R.id.btDel:
                        delItem(finalitem);
                        break;
                }

            }
        });
    }


//    public void filter(String charText) {
//        charText = charText.toLowerCase(Locale.getDefault());
//        itemslist.clear();
//        if (charText.length() == 0) {
//            itemslist.addAll(arraylist);
//        } else {
//            for (ParseItems wp : arraylist) {
//                if (wp.getName().toLowerCase(Locale.getDefault())
//                        .contains(charText)) {
//                    itemslist.add(wp);
//                }
//            }
//        }
//        notifyDataSetChanged();
//    }

    public class ViewHolder {
        TextView tvItemName;
        TextView tvItemUSD;
        TextView tvItemUAH;
        ParseImageView ivItem;
        ParseImageView ivRest;
        Button btAdd;
        Button btDel;
        TextView tvQuantity;
    }

}
