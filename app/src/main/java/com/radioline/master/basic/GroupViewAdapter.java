package com.radioline.master.basic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.radioline.master.myapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Master on 01.11.2014.
 */
public class GroupViewAdapter extends ArrayAdapter<Group> {
    private Context context;
    private final ArrayList<Group> groupArrayList;

    public GroupViewAdapter(Context context, ArrayList<Group> groupssArrayList) {
        super(context, R.layout.groupview, groupssArrayList);

        this.context = context;
        this.groupArrayList = groupssArrayList;
    }

    static class ViewHolder {
        public TextView tvName;
       // public TextView tvId;
        //public TextView tvCode;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ViewHolder holder;
        // Очищает сущетсвующий шаблон, если параметр задан
        // Работает только если базовый шаблон для всех классов один и тот же
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.groupview, null, true);
            holder = new ViewHolder();
            holder.tvName = (TextView) rowView.findViewById(R.id.tvName);
            //holder.tvId = (TextView) rowView.findViewById(R.id.tvId);
            //holder.tvCode = (TextView) rowView.findViewById(R.id.tvCode);

            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        holder.tvName.setText(groupArrayList.get(position).getName());
//        holder.tvId.setText(groupArrayList.get(position).getId());
  //      holder.tvCode.setText(groupArrayList.get(position).getCode());



        return rowView;





        //return super.getView(position, convertView, parent);
    }
}
