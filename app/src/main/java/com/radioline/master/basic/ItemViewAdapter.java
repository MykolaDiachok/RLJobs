package com.radioline.master.basic;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.radioline.master.myapplication.R;
import com.radioline.master.soapconnector.Converts;
import com.radioline.master.soapconnector.DownloadImageInBackground;
import com.radioline.master.soapconnector.ImageDownloaderSOAP;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by master on 06.11.2014.
 */
public class ItemViewAdapter extends ArrayAdapter<Item> {

    Context context;
    private final ArrayList<Item> itemArrayList;


    public ItemViewAdapter(Context context, ArrayList<Item> itemsArrayList) {
        super(context, R.layout.itemview, itemsArrayList);

        this.context = context;
        this.itemArrayList = itemsArrayList;
    }



    static class ViewHolder {
        public TextView tvItemName;
        //public TextView tvProperties;
        public TextView tvItemUSD;
        public TextView tvItemUAH;
        public ImageView ivItem;
        public Button btAdd;
        public Button btDel;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //final int tposition = position;
        ViewHolder holder;

        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.itemview, null, true);
            holder = new ViewHolder();
            holder.tvItemName = (TextView) rowView.findViewById(R.id.tvItemName);
            //holder.tvProperties = (TextView) rowView.findViewById(R.id.tvProperties);
            holder.tvItemUSD = (TextView) rowView.findViewById(R.id.tvItemUSD);
            holder.tvItemUAH = (TextView) rowView.findViewById(R.id.tvItemUAH);
            holder.ivItem = (ImageView) rowView.findViewById(R.id.ivItem);
            holder.btAdd = (Button) rowView.findViewById(R.id.btAdd);
            final Item finalitem = itemArrayList.get(position);
            holder.btAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ParseQuery<Basket> query = Basket.getQuery();
                    query.fromLocalDatastore();
                    query.whereEqualTo("productId", finalitem.getId());
                    int currentcount = 1;
                    try {
                        Basket localbasket = query.getFirst();
                        currentcount = localbasket.getQuantity()+1;
                        localbasket.setQuantity(currentcount);
                        localbasket.pin();
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Basket basket = new Basket();
                        basket.setProductId(finalitem.getId());
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

                    Toast.makeText(context, "add: " + finalitem.getName()+"+1="+currentcount, Toast.LENGTH_SHORT).show();

                }
            });
            holder.btDel = (Button) rowView.findViewById(R.id.btDel);
            holder.btDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ParseQuery<Basket> query = Basket.getQuery();
                    query.fromLocalDatastore();
                    query.whereEqualTo("productId", finalitem.getId());
                    int currentcount = 0;
                    try {
                        Basket localbasket = query.getFirst();
                        currentcount = localbasket.getQuantity()-1;
                        localbasket.setQuantity(currentcount);
                        localbasket.pin();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (currentcount<1) {
                        ParseQuery<Basket> query2 = Basket.getQuery();
                        query2.fromLocalDatastore();
                        query2.whereEqualTo("productId", finalitem.getId());
                        try {
                            Basket localbasket = query2.getFirst();
                            localbasket.delete();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    Toast.makeText(context, "del: " + finalitem.getName()+"-1="+currentcount, Toast.LENGTH_SHORT).show();
                }
            });
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        holder.tvItemName.setText(itemArrayList.get(position).getName());


        DecimalFormat dec = new DecimalFormat("0.00");

        holder.tvItemUSD.setText("$ " + dec.format(itemArrayList.get(position).getPrice()));
        holder.tvItemUAH.setText("₴ " + dec.format(itemArrayList.get(position).getPriceUAH()));

        ImageDownloaderSOAP getimage = new ImageDownloaderSOAP();
        getimage.download(itemArrayList.get(position).getId(),holder.ivItem,null,false);

        return rowView;

    }
}
