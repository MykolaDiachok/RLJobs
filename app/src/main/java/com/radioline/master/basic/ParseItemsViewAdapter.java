package com.radioline.master.basic;

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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by master on 04.01.2015.
 */
public class ParseItemsViewAdapter extends ParseQueryAdapter<ParseObject> {
    private final int[] bgColors = new int[]{Color.rgb(245, 245, 245), Color.rgb(224, 255, 255)};
    private Context context;
    private LayoutInflater inflater;
    private ImageLoader imageLoader;
    private LinkedHashSet mySet;


    public ParseItemsViewAdapter(Context context, final ParseGroups parseGroupId) {

        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery<ParseObject> create() {
                ParseQuery query = new ParseQuery("ParseItems");
                query.setMaxCacheAge(TimeUnit.HOURS.toMillis(3));
                query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
                query.include("Basket");
                query.include("Basket.parseItem");
                query.whereEqualTo("parseGroupId", parseGroupId);
                query.whereEqualTo("Availability", true);
                query.orderByAscending("Name");
                return query;
            }
        });
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.imageLoader = ImageLoader.getInstance();
        this.mySet = new LinkedHashSet();

    }

    public ParseItemsViewAdapter(Context context, final String parentId) {

        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery<ParseObject> create() {

                ParseQuery query = new ParseQuery("ParseItems");
                query.setMaxCacheAge(TimeUnit.HOURS.toMillis(3));
                query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
                query.include("Basket");
                query.include("Basket.parseItem");
                query.whereEqualTo("GroupId", parentId);
                query.whereEqualTo("Availability", true);
                query.orderByAscending("Name");

                return query;
            }
        });
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.imageLoader = ImageLoader.getInstance();
        this.mySet = new LinkedHashSet();

    }

    public ParseItemsViewAdapter(Context context, final String parentId, final String searchData) {
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery<ParseObject> create() {

                List a = Arrays.asList(searchData.replace("|", "\\|").replace(".", "\\.").split("\\s+"));
                String forReg = "";
                for (int i = 0; i < a.size(); i++) {
                    forReg = forReg + "(?=.*" + a.get(i).toString() + ")";
                }

                ParseQuery query = new ParseQuery("ParseItems");
                query.setMaxCacheAge(TimeUnit.HOURS.toMillis(3));
                query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
                query.include("Basket");
                query.include("Basket.parseItem");
                query.whereEqualTo("GroupId", parentId);
                query.whereEqualTo("Availability", true);
                query.whereMatches("Name", forReg, "i");
                query.orderByAscending("Name");
                return query;
            }
        });
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.imageLoader = ImageLoader.getInstance();
        this.mySet = new LinkedHashSet();

    }

    @Override
    public View getItemView(final ParseObject object, View view, ViewGroup parent) {
        final ViewHolder holder;
        Log.d("getItemView", "Start");
        //position = +1;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.itemview, null);
            // Locate the TextViews in listview_item.xml
            holder.tvItemName = (TextView) view.findViewById(R.id.tvItemName);
            holder.tvItemUSD = (TextView) view.findViewById(R.id.tvItemUSD);
            holder.tvItemUAH = (TextView) view.findViewById(R.id.tvItemUAH);
            holder.ivItem = (ImageView) view.findViewById(R.id.ivItem);
            //holder.ivRest = (ParseImageView) view.findViewById(R.id.ivRest);
            holder.tvRest = (TextView) view.findViewById(R.id.tvRest);
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

        ParseItems parseItem = (ParseItems) object;
        Basket basketItem = (Basket) object.getParseObject("Basket");

        this.setOnClickListener(holder.btAdd, parseItem);
        this.setOnClickListener(holder.btDel, parseItem);
        this.setOnClickListener(holder.ivItem, parseItem);
        if (basketItem != null) {
            holder.tvQuantity.setText(basketItem.getQuantity());
        }
        holder.tvItemName.setText(parseItem.getName());
        DecimalFormat dec = new DecimalFormat("0.00");
        holder.tvItemUSD.setText("$ " + dec.format(parseItem.getPrice()));
        holder.tvItemUAH.setText("₴ " + dec.format(parseItem.getPriceUAH()));


        ParseFile photoFile = parseItem.getImage();
        //Get singleton instance of ImageLoader

        if (photoFile != null) {
            //Load the image from the url into the ImageView.
            imageLoader.displayImage(photoFile.getUrl(), holder.ivItem);
        }
        if (object != null) {
            int cStock = parseItem.getStock();
            if ((cStock > 0) && (cStock <= 5)) {
                holder.tvRest.setBackgroundColor(Color.RED);
                //imageLoader.displayImage(restMin.getUrl(), holder.ivRest);
            } else if ((cStock > 5) && (cStock <= 30)) {
                holder.tvRest.setBackgroundColor(Color.YELLOW);
                //imageLoader.displayImage(restAverage.getUrl(), holder.ivRest);
            } else if (cStock > 30) {
                holder.tvRest.setBackgroundColor(Color.rgb(69, 139, 116));
                //imageLoader.displayImage(restMax.getUrl(), holder.ivRest);
            }
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
        Log.d("getItemView", "End");
        return view;


    }

    private void delItem(final ParseItems finalitem) {
        ParseQuery<Basket> query = Basket.getQuery();
        query.setMaxCacheAge(TimeUnit.MINUTES.toMillis(10));
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
//        int currentcount = 0;
//        try {
//            Basket localbasket = query.getFirst();
//            currentcount = localbasket.getQuantity() - 1;
//            if (currentcount < 0) {
//                currentcount = 0;
//                localbasket.deleteInBackground();
//                //localbasket.saveEventually();
//            } else {
//                localbasket.setQuantity(currentcount);
//                localbasket.saveEventually();
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//
//        SuperToast superToast = new SuperToast(context);
//        superToast.setDuration(SuperToast.Duration.VERY_SHORT);
//        superToast.setText("del: " + finalitem.getName() + "-1=" + currentcount);
//        superToast.setIcon(R.drawable.del, SuperToast.IconPosition.LEFT);
//        superToast.show();
    }

    private void addItem(final ParseItems finalitem) {
        Log.d("ADD", "Start");
        ParseQuery<Basket> query = Basket.getQuery();
        query.setMaxCacheAge(TimeUnit.MINUTES.toMillis(10));
        query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
        //query.fromLocalDatastore();
        query.whereEqualTo("parseItem", finalitem);
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.whereNotEqualTo("sent", true);
        query.getFirstInBackground(new GetCallback<Basket>() {
            @Override
            public void done(final Basket basket, ParseException e) {
                if (basket == null) {
                    Basket addbasket = new Basket();
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

    private void setOnClickListener(View view, final ParseItems finalitem) {

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.ivItem:
                        //Пока отменил, ищу другую реализацию
//                        Intent intent = new Intent(context, PicActivity.class);
//                        intent.putExtra("itemid", finalitem.getItemId());
//                        intent.putExtra("Name", finalitem.getName());
//                        context.startActivity(intent);
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
        //Integer position;
        TextView tvItemName;
        TextView tvItemUSD;
        TextView tvItemUAH;
        TextView tvRest;
        ImageView ivItem;

        Button btAdd;
        Button btDel;
        TextView tvQuantity;
    }

}
