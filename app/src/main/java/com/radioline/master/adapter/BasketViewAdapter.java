package com.radioline.master.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.johnpersano.supertoasts.SuperToast;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.radioline.master.myapplication.R;
import com.radioline.master.parse.Basket;
import com.radioline.master.parse.ParseItems;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.concurrent.TimeUnit;

/**
 * Created by dyachok on 13.11.2014.
 */
public class BasketViewAdapter extends ParseQueryAdapter<Basket> {
    private final int[] bgColors = new int[]{Color.rgb(245, 245, 245), Color.rgb(224, 255, 255)};
    private Context context;
    private LayoutInflater inflater;
    private ImageLoader imageLoader;
    private LinkedHashSet mySet;

    public BasketViewAdapter(Context context) {

        super(context, new ParseQueryAdapter.QueryFactory<Basket>() {
            public ParseQuery create() {
                ParseQuery query = Basket.getQuery();
                query.setMaxCacheAge(TimeUnit.MINUTES.toMillis(10));
                query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
                query.whereEqualTo("user", ParseUser.getCurrentUser());
                query.whereNotEqualTo("sent", true);
                //query.fromLocalDatastore();
                query.whereGreaterThan("quantity", 0);
                query.orderByDescending("updateAt");
                //query.include("ParseItems");
                return query;
            }
        });
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.imageLoader = ImageLoader.getInstance();
        this.mySet = new LinkedHashSet();
    }

    // Customize the layout by overriding getItemView
    @Override
    public View getItemView(Basket object, View view, ViewGroup parent) {
        final ViewHolder holder;
        Log.d("getItemView", "Start");
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.itemview, null);
            // Locate the TextViews in listview_item.xml
            holder.tvItemName = (TextView) view.findViewById(R.id.tvItemName);
            holder.tvItemUSD = (TextView) view.findViewById(R.id.tvItemUSD);
            holder.tvItemUAH = (TextView) view.findViewById(R.id.tvItemUAH);
            holder.ivItem = (ImageView) view.findViewById(R.id.ivItem);
            //holder.ivRest = (ParseImageView) view.findViewById(R.id.ivRest);
            //holder.tvRest = (TextView) view.findViewById(R.id.tvRest);
            holder.btAdd = (Button) view.findViewById(R.id.btAdd);
            holder.btDel = (Button) view.findViewById(R.id.btDel);
            holder.tvQuantity = (TextView) view.findViewById(R.id.tvQuantity);
            //holder.position = this.position;
            view.setTag(holder);
            Log.d("getItemView", "view == null");
        } else {
            holder = (ViewHolder) view.getTag();
            Log.d("getItemView", "view.getTag");
        }
        super.getItemView(object, view, parent);

        mySet.add(object);
        Log.d("getItemView", "mySet.add(object)");
        int colorPosition = new ArrayList<ParseItems>(mySet).indexOf(object) % bgColors.length;
        Log.d("getItemView", "get colorPosition");
        view.setBackgroundColor(bgColors[colorPosition]);
        Log.d("getItemView", "setBackgroundColor");

        ParseItems parseItem = (ParseItems) object.getParseObject("parseItem");
        parseItem.fetchIfNeededInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                ParseFile photoFile = parseObject.getParseFile("image");
                //Get singleton instance of ImageLoader

                if (photoFile != null) {
                    //Load the image from the url into the ImageView.
                    imageLoader.displayImage(photoFile.getUrl(), holder.ivItem);
                }
            }
        });

        holder.tvItemName.setText(object.getName());
        DecimalFormat dec = new DecimalFormat("0.00");
        holder.tvItemUSD.setText("$ " + dec.format(object.getRequiredpriceUSD()));
        holder.tvItemUAH.setText("₴ " + dec.format(object.getRequiredpriceUAH()));
        holder.tvQuantity.setText("Quantity:" + String.valueOf(object.getQuantity()));


        this.setOnClickListener(holder.btAdd, object);
        this.setOnClickListener(holder.btDel, object);
        //this.setOnClickListener(holder.ivItem, object);

        Log.d("getItemView", "End");
        return view;
    }

    private void setOnClickListener(Button btListener, final Basket object) {
        //btListener.setTag(position);
        btListener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btAdd:
                        addItem(object);
                        break;
                    case R.id.btDel:
                        delItem(object);
                        break;
                }


            }
        });
    }

    private void addItem(Basket object) {
        final ParseItems finalitem = object.getParseItem();
        finalitem.fetchIfNeededInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                Log.d("ADD", "Start");
                ParseQuery<Basket> query = Basket.getQuery();
                query.setMaxCacheAge(TimeUnit.SECONDS.toMillis(1));
                query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
                query.whereEqualTo("parseItem", finalitem);
                query.whereEqualTo("user", ParseUser.getCurrentUser());
                query.whereNotEqualTo("sent", true);
                query.getFirstInBackground(new GetCallback<Basket>() {
                    @Override
                    public void done(final Basket basket, ParseException e) {
                        if (basket == null) {
                            final Basket addbasket = new Basket();
                            addbasket.setACL(new ParseACL(ParseUser.getCurrentUser()));
                            addbasket.setUser(ParseUser.getCurrentUser());
                            addbasket.setParseItem(finalitem);
                            addbasket.setProductId(finalitem.getItemId());
                            addbasket.setName(finalitem.getName());
                            addbasket.setRequiredpriceUSD(finalitem.getPrice());
                            addbasket.setRequiredpriceUAH(finalitem.getPriceUAH());
                            addbasket.setQuantity(1);

                            addbasket.saveEventually(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    notifyDataSetChanged();
                                    SuperToast superToast = new SuperToast(context);
                                    superToast.setDuration(SuperToast.Duration.VERY_SHORT);
                                    superToast.setText("add: " + finalitem.getName() + "+1");
                                    superToast.setIcon(R.drawable.add, SuperToast.IconPosition.LEFT);
                                    superToast.show();
                                }
                            });
                        } else {
                            basket.increment("quantity");
                            basket.saveEventually(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    notifyDataSetChanged();
                                    SuperToast superToast = new SuperToast(context);
                                    superToast.setDuration(SuperToast.Duration.VERY_SHORT);
                                    superToast.setText("add: " + finalitem.getName() + "+1=" + (basket.getQuantity()));
                                    superToast.setIcon(R.drawable.add, SuperToast.IconPosition.LEFT);
                                    superToast.show();
                                }
                            });
                        }
                    }
                });
            }
        });


//        //int pos = Integer.parseInt(v.getTag().toString());
//        //Item finalitem = itemArrayList.get(pos);
//        ParseQuery<Basket> query = Basket.getQuery();
//        //query.fromLocalDatastore();
//        query.whereEqualTo("productId", object.getProductId());
//        int currentcount = 1;
//        try {
//            Basket localbasket = query.getFirst();
//            currentcount = localbasket.getQuantity() + 1;
//            localbasket.setQuantity(currentcount);
//            localbasket.pinInBackground();
//            this.notifyDataSetChanged();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        Toast.makeText(context, "add: " + object.getName() + "+1=" + currentcount, Toast.LENGTH_SHORT).show();
    }

    private void delItem(Basket object) {
        final ParseItems finalitem = object.getParseItem();
        ParseQuery<Basket> query = Basket.getQuery();
        query.setMaxCacheAge(TimeUnit.SECONDS.toMillis(1));

        query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
        //query.fromLocalDatastore();
        query.whereEqualTo("parseItem", finalitem);
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.whereNotEqualTo("sent", true);
        query.getFirstInBackground(new GetCallback<Basket>() {
            @Override
            public void done(final Basket basket, ParseException e) {
                if (basket != null) {
                    basket.increment("quantity", -1);
                    basket.saveEventually(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            notifyDataSetChanged();
                            SuperToast superToast = new SuperToast(context);
                            superToast.setDuration(SuperToast.Duration.VERY_SHORT);
                            superToast.setText("del: " + finalitem.getName() + "-1=" + basket.getQuantity());
                            superToast.setIcon(R.drawable.del, SuperToast.IconPosition.LEFT);
                            superToast.show();
                        }
                    });
                    if (basket.getQuantity() <= 0) {
                        basket.deleteEventually(new DeleteCallback() {
                            @Override
                            public void done(ParseException e) {
                                notifyDataSetChanged();
                                SuperToast superToast = new SuperToast(context);
                                superToast.setDuration(SuperToast.Duration.VERY_SHORT);
                                superToast.setText("remove: " + finalitem.getName());
                                superToast.setIcon(R.drawable.del, SuperToast.IconPosition.LEFT);
                                superToast.show();
                            }
                        });
                    }
                }
            }
        });


//        ParseQuery<Basket> query = Basket.getQuery();
//        //query.fromLocalDatastore();
//        query.whereEqualTo("productId", object.getProductId());
//        int currentcount = 0;
//        try {
//            Basket localbasket = query.getFirst();
//            currentcount = localbasket.getQuantity() - 1;
//            if (currentcount < 0) {
//                currentcount = 0;
//            }
//            localbasket.setQuantity(currentcount);
//            localbasket.pinInBackground();
//            //localbasket.unpinInBackground();
//            this.notifyDataSetChanged();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        Toast.makeText(context, "del: " + object.getName() + "-1=" + currentcount, Toast.LENGTH_SHORT).show();
    }

    /**
     * Notifies the attached observers that the underlying data has been changed
     * and any View reflecting the data set should refresh itself.
     */
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
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
