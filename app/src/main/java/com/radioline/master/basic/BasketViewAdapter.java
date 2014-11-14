package com.radioline.master.basic;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;



import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.radioline.master.myapplication.R;
import com.radioline.master.soapconnector.ImageDownloaderSOAP;

import java.text.DecimalFormat;

/**
 * Created by dyachok on 13.11.2014.
 */
public class BasketViewAdapter extends ParseQueryAdapter<Basket> {

    Context context;

    public BasketViewAdapter(Context context) {
        // Use the QueryFactory to construct a PQA that will only show
        // Todos marked as high-pri

        super(context, new ParseQueryAdapter.QueryFactory<Basket>() {
            public ParseQuery create() {
                ParseQuery query = Basket.getQuery();
                query.fromLocalDatastore();
                //query.whereEqualTo("highPri", true);
                return query;
            }
        });
        this.context = context;
    }


    // Customize the layout by overriding getItemView
    @Override
    public View getItemView(Basket object, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.itemview, null);
        }

        super.getItemView(object, v, parent);
        ImageView itemImage = (ImageView) v.findViewById(R.id.ivItem);
        ImageDownloaderSOAP getimage = new ImageDownloaderSOAP();
        getimage.download(object.getProductId(),itemImage,null,false);


        TextView tvItemName = (TextView) v.findViewById(R.id.tvItemName);
        tvItemName.setText(object.getName());

        DecimalFormat dec = new DecimalFormat("0.00");
        TextView tvItemUSD = (TextView) v.findViewById(R.id.tvItemUSD);
        tvItemUSD.setText("$ " + dec.format(object.getQuantity()*object.getRequiredpriceUSD()));

        TextView tvItemUAH = (TextView) v.findViewById(R.id.tvItemUAH);
        tvItemUAH.setText("₴ " + dec.format(object.getQuantity()*object.getRequiredpriceUAH()));

        TextView tvQuantity = (TextView) v.findViewById(R.id.tvQuantity);
        tvQuantity.setText(String.valueOf(object.getQuantity()));

//        Button btAdd = (Button) v.findViewById(R.id.btAdd);
//        btAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                ParseObject basket = new ParseObject("basket");
//                basket.put("productid", object.getString("productid"));
//                basket.put("quantity", 1);
//                basket.put("requiredpriceUSD", itemArrayList.get(tposition).getPrice());
//                basket.put("requiredpriceUAH", object.getDouble(""));
//                basket.pinInBackground();
//
//
//                Toast.makeText(context, "button add: " + itemArrayList.get(tposition).getName(), Toast.LENGTH_SHORT).show();
//
//
//
//
//            }
//        });
//        Button btDel = (Button) v.findViewById(R.id.btDel);
//        btDel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ParseObject basket = new ParseObject("basket");
//                basket.put("productid", itemArrayList.get(tposition).getId());
//                basket.put("quantity", -1);
//                basket.put("requiredprice", itemArrayList.get(tposition).getPrice());
//                basket.pinInBackground();
//                Toast.makeText(context, "button del: " + itemArrayList.get(tposition).getName(), Toast.LENGTH_SHORT).show();
//            }
//        });


        return v;
    }



}
