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

public class ContentsCardListAdapter extends BaseAdapter {

    Context mContext;
    DataBaseManager dataBaseManager;
    LayoutInflater inflater;
    TypefaceManager typefaceManager;

    ImageView mark;


    //all cards.
    ArrayList<cardListItem> arrayList;


    public ContentsCardListAdapter(Context context) {
        this.mContext = context;
        this.dataBaseManager = new DataBaseManager(context);


        //set array list.
        arrayList = dataBaseManager.getAllCardItemList();
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
        convertView = inflater.inflate(R.layout.item_contents_card_list, null, false);


        TextView title = convertView.findViewById(R.id.title);
        title.setTypeface(typefaceManager.getTypeNormal());

        TextView date = convertView.findViewById(R.id.date);
        date.setTypeface(typefaceManager.getTypeNormal());

        //set items,
        title.setText(arrayList.get(position).getTitle());
        date.setText(DateManager.getDateText(arrayList.get(position).getDate()));

        return convertView;
    }


}
