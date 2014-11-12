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

import com.radioline.master.myapplication.R;
import com.radioline.master.soapconnector.Converts;
import com.radioline.master.soapconnector.DownloadImageInBackground;
import com.radioline.master.soapconnector.ImageDownloaderSOAP;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by master on 06.11.2014.
 */
public class ItemViewAdapter extends ArrayAdapter<Item> {

    Context context;
    private final ArrayList<Item> itemArrayList;
    DownloadImageInBackground dn;

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

        final int tposition = position;
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
            holder.btAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "button add: " + itemArrayList.get(tposition).getName(), Toast.LENGTH_SHORT).show();
                }
            });
            holder.btDel = (Button) rowView.findViewById(R.id.btDel);
            holder.btDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "button del: " + itemArrayList.get(tposition).getName(), Toast.LENGTH_SHORT).show();
                }
            });
            //
            //holder.tvCode = (TextView) rowView.findViewById(R.id.tvCode);

            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        holder.tvItemName.setText(itemArrayList.get(position).getName());
        String properties="";

//        if ((itemArrayList.get(position).getModel()!=null) && (itemArrayList.get(position).getModel().length()!=0)){
//            properties=properties+"<b>model:</b>"+itemArrayList.get(position).getModel()+"<br>";
//        }
//        String partnumber = itemArrayList.get(position).getPartNumber();
//        if ((partnumber!=null) && (partnumber.length()!=0)){
//            properties =properties+ "<b>part #:</b>"+partnumber+"<br>";
//        }


//        holder.tvProperties.setText(Html.fromHtml(properties));

        DecimalFormat dec = new DecimalFormat("0.00");

        holder.tvItemUSD.setText("$ " + dec.format(itemArrayList.get(position).getPrice()));
        holder.tvItemUAH.setText("₴ " + dec.format(itemArrayList.get(position).getPriceUAH()));




        //new DownloadImageInBackground(holder.imageView).execute(itemArrayList.get(position).getId());

        ImageDownloaderSOAP getimage = new ImageDownloaderSOAP();
        getimage.download(itemArrayList.get(position).getId(),holder.ivItem,null,false);

//        Converts tg1 = new Converts();
//        Bitmap bitmap = null;
//        try {
//            bitmap = tg1.getBitMapFromServer(itemArrayList.get(position).getId(),50,50,50,false);
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        if (bitmap!=null){
//        holder.imageView.setImageBitmap(bitmap);}


        //(holder.imageView).execute(itemArrayList.get(position).getId());


        //((ViewPager) container).addView(rowView);
        //      holder.tvCode.setText(groupArrayList.get(position).getCode());



        return rowView;





        //return super.getView(position, convertView, parent);
    }
}
