package com.radioline.master.basic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.johnpersano.supertoasts.SuperToast;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.radioline.master.myapplication.PicActivity;
import com.radioline.master.myapplication.R;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

/**
 * Created by master on 04.01.2015.
 */
public class ParseItemsViewAdapter extends ParseQueryAdapter<ParseItems> {
    private final int[] bgColors = new int[]{Color.YELLOW, Color.BLUE};
    private Context context;
    private LayoutInflater inflater;
    private ImageLoader imageLoader;
    private ParseFile restAverage = null;
    private ParseFile restMax = null;
    private ParseFile restMin = null;
    private Integer position = 0;

    public ParseItemsViewAdapter(Context context, final ParseGroups parseGroupId) {

        super(context, new ParseQueryAdapter.QueryFactory<ParseItems>() {
            public ParseQuery<ParseItems> create() {
                // Here we can configure a ParseQuery to display
                // only top-rated meals.
                ParseQuery query = new ParseQuery("ParseItems");
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
//        ParseConfig.getInBackground(new ConfigCallback() {
//            @Override
//            public void done(ParseConfig config, ParseException e) {
//                restAverage = config.getParseFile("RestAverage");
//                restMax = config.getParseFile("RestMax");
//                restMin = config.getParseFile("RestMin");
//                Log.d("TAG", "Loading images files");
//            }
//        });
    }


    public ParseItemsViewAdapter(Context context, final String parentId) {

        super(context, new ParseQueryAdapter.QueryFactory<ParseItems>() {
            public ParseQuery<ParseItems> create() {
                // Here we can configure a ParseQuery to display
                // only top-rated meals.
                ParseQuery query = new ParseQuery("ParseItems");
                query.include("Basket");
                query.include("Basket.parseItem");
                query.whereEqualTo("GroupId", parentId);
                query.whereEqualTo("Availability", true);
                query.orderByDescending("Name");
                return query;
            }
        });
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.imageLoader = ImageLoader.getInstance();
//        ParseConfig.getInBackground(new ConfigCallback() {
//            @Override
//            public void done(ParseConfig config, ParseException e) {
//                restAverage = config.getParseFile("RestAverage");
//                restMax = config.getParseFile("RestMax");
//                restMin = config.getParseFile("RestMin");
//                Log.d("TAG", "Loading images files");
//            }
//        });
    }

    public ParseItemsViewAdapter(Context context, final String parentId, final String searchData) {


        super(context, new ParseQueryAdapter.QueryFactory<ParseItems>() {
            public ParseQuery<ParseItems> create() {
                // Here we can configure a ParseQuery to display
                // only top-rated meals.
                List a = Arrays.asList(searchData.replace("|", "\\|").replace(".", "\\.").split("\\s+"));
                String forReg = "";
                for (int i = 0; i < a.size(); i++) {
                    forReg = forReg + "(?=.*" + a.get(i).toString() + ")";
                }

                ParseQuery query = new ParseQuery("ParseItems");
                query.include("Basket");
                query.include("Basket.parseItem");
                query.whereEqualTo("GroupId", parentId);
                query.whereEqualTo("Availability", true);
                query.whereMatches("Name", forReg, "i");
//                query.whereContainedIn("Name", );
                query.orderByAscending("Name");
                return query;
            }
        });
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.imageLoader = ImageLoader.getInstance();
//        ParseConfig.getInBackground(new ConfigCallback() {
//            @Override
//            public void done(ParseConfig config, ParseException e) {
//                restAverage = config.getParseFile("RestAverage");
//                restMax = config.getParseFile("RestMax");
//                restMin = config.getParseFile("RestMin");
//                Log.d("TAG", "Loading images files");
//            }
//        });
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getItemView(final ParseItems object, View view, ViewGroup parent) {
        final ViewHolder holder;
        Integer t = this.getCount();
        position = +1;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.itemview, null);
            // Locate the TextViews in listview_item.xml
            holder.tvItemName = (TextView) view.findViewById(R.id.tvItemName);
            holder.tvItemUSD = (TextView) view.findViewById(R.id.tvItemUSD);
            holder.tvItemUAH = (TextView) view.findViewById(R.id.tvItemUAH);
            holder.ivItem = (ParseImageView) view.findViewById(R.id.ivItem);
            //holder.ivRest = (ParseImageView) view.findViewById(R.id.ivRest);
            holder.tvRest = (TextView) view.findViewById(R.id.tvRest);
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
        if (object != null) {
            if ((object.getStock() > 0) && (object.getStock() <= 5)) {
                holder.tvRest.setBackgroundColor(Color.RED);
                //imageLoader.displayImage(restMin.getUrl(), holder.ivRest);
            } else if ((object.getStock() > 5) && (object.getStock() <= 30)) {
                holder.tvRest.setBackgroundColor(Color.YELLOW);
                //imageLoader.displayImage(restAverage.getUrl(), holder.ivRest);
            } else if (object.getStock() > 30) {
                holder.tvRest.setBackgroundColor(Color.GREEN);
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
                localbasket.unpinInBackground();
            } else {
                localbasket.setQuantity(currentcount);
                localbasket.pinInBackground();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        SuperToast superToast = new SuperToast(context);
        superToast.setDuration(SuperToast.Duration.VERY_SHORT);
        superToast.setText("del: " + finalitem.getName() + "-1=" + currentcount);
        superToast.setIcon(R.drawable.del, SuperToast.IconPosition.LEFT);
        superToast.show();
    }

    private void addItem(ParseItems finalitem) {
        Log.d("ADD", "Start");
        ParseQuery<Basket> query = Basket.getQuery();
        query.fromLocalDatastore();
        query.whereEqualTo("productId", finalitem.getItemId());
        Log.d("ADD", "local query start");
        int currentcount = 1;
        try {
            Basket localbasket = query.getFirst();
            currentcount = localbasket.getQuantity() + 1;
            localbasket.setQuantity(currentcount);
            localbasket.pinInBackground();
        } catch (ParseException e) {
            e.printStackTrace();
            Basket basket = new Basket();
            basket.setParseItem(finalitem);
            basket.setProductId(finalitem.getItemId());
            basket.setName(finalitem.getName());
            basket.setRequiredpriceUSD(finalitem.getPrice());
            basket.setRequiredpriceUAH(finalitem.getPriceUAH());
            basket.setQuantity(1);
            //try {
            basket.pinInBackground();
//            } catch (ParseException e1) {
//                e1.printStackTrace();
//            }
        }
        Log.d("ADD", "local query end");
        Log.d("ADD", "Toast start");
        SuperToast superToast = new SuperToast(context);
        superToast.setDuration(SuperToast.Duration.VERY_SHORT);
        superToast.setText("add: " + finalitem.getName() + "+1=" + currentcount);
        superToast.setIcon(R.drawable.add, SuperToast.IconPosition.LEFT);
        superToast.show();
        Log.d("ADD", "Toast end");
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
        TextView tvRest;
        ParseImageView ivItem;
        ParseImageView ivRest;
        Button btAdd;
        Button btDel;
        TextView tvQuantity;
    }

}
