package com.m.m.moment.memorize.frame.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.m.m.moment.memorize.frame.R;
import com.m.m.moment.memorize.frame.database.DataBaseManager;
import com.m.m.moment.memorize.frame.item.cardListItem;
import com.m.m.moment.memorize.frame.manager.DateManager;
import com.m.m.moment.memorize.frame.manager.TypefaceManager;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.Nullable;

public class SelectedCardListAdapter extends BaseAdapter {


    Context mContext;
    DataBaseManager dataBaseManager;
    LayoutInflater inflater;
    TypefaceManager typefaceManager;

    ImageView mark;
    HashMap mHashMap;


    //all cards.
    ArrayList<cardListItem> arrayList = new ArrayList<>();


    public SelectedCardListAdapter(Context context, ArrayList<Long> IDList, @Nullable HashMap hashMap) {
        this.mContext = context;
        this.dataBaseManager = new DataBaseManager(context);

        if (hashMap != null)
            mHashMap = hashMap;

        //set array list.
        for (int i = 0; i < IDList.size(); i++) {
            arrayList.add(dataBaseManager.getCardItem(IDList.get(i)));
        }

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        typefaceManager = new TypefaceManager(mContext);


    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.item_card_list, null, false);


        TextView title = convertView.findViewById(R.id.title);
        title.setTypeface(typefaceManager.getTypeNormal());
        TextView date = convertView.findViewById(R.id.date);
        date.setTypeface(typefaceManager.getTypeNormal());

        //hide choice icon.
        ImageView mark = convertView.findViewById(R.id.mark);
        ImageView mark_line = convertView.findViewById(R.id.mark_line);

        mark.setVisibility(View.GONE);
        mark_line.setVisibility(View.GONE);


        //set items,
        title.setText(arrayList.get(position).getTitle());

        if (mHashMap == null)
            date.setText(DateManager.getDateText(arrayList.get(position).getDate()));
        else
            date.setText(mHashMap.get(arrayList.get(position).getTime_id()) + " 장");

        return convertView;
    }


}
