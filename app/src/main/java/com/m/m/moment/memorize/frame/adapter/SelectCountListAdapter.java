package com.m.m.moment.memorize.frame.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.m.m.moment.memorize.frame.R;
import com.m.m.moment.memorize.frame.database.DataBaseManager;
import com.m.m.moment.memorize.frame.dialog.ShowImageDialog;
import com.m.m.moment.memorize.frame.etc.numberCountClickListener;
import com.m.m.moment.memorize.frame.item.cardListItem;
import com.m.m.moment.memorize.frame.manager.DateManager;
import com.m.m.moment.memorize.frame.manager.TypefaceManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class SelectCountListAdapter extends BaseAdapter {


    Context mContext;
    DataBaseManager dataBaseManager;
    LayoutInflater inflater;
    TypefaceManager typefaceManager;

    TextView count;
    ImageButton countDown, countUp;


    //all cards.
    ArrayList<cardListItem> arrayList = new ArrayList<>();

    //card numbers.
    HashMap<Long, Integer> hashMap = new HashMap<>();

    private numberCountClickListener mListener;


    public SelectCountListAdapter(Context context, ArrayList<Long> IDList, numberCountClickListener listener) {
        this.mContext = context;
        this.dataBaseManager = new DataBaseManager(context);
        mListener = listener;

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
        convertView = inflater.inflate(R.layout.item_card_number_list, null, false);


        TextView title = convertView.findViewById(R.id.title);
        title.setTypeface(typefaceManager.getTypeNormal());
        TextView date = convertView.findViewById(R.id.date);
        date.setTypeface(typefaceManager.getTypeNormal());

        count = convertView.findViewById(R.id.number);

        count.setTypeface(typefaceManager.getTypeNormal());
        countUp = convertView.findViewById(R.id.count_up);
        countDown = convertView.findViewById(R.id.count_down);


        //set items,
        title.setText(arrayList.get(position).getTitle());
        date.setText(DateManager.getDateText(arrayList.get(position).getDate()));

        //set count.

        if (String.valueOf(hashMap.get(arrayList.get(position).getTime_id())).equals("null"))
            count.setText("0");
        else {
            count.setText(String.valueOf(hashMap.get(arrayList.get(position).getTime_id())));
        }


        countUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mListener.onClick(hashMap, arrayList.get(position).getTime_id())) {
                    addCardNumbers(arrayList.get(position).getTime_id());
                    count.setText(String.valueOf(hashMap.get(arrayList.get(position).getTime_id())));

                    /*
                    Iterator<Long> iterator = hashMap.keySet().iterator();
                    while (iterator.hasNext()) {
                        Long key = iterator.next();
                        cardListItem item = new DataBaseManager(mContext).getCardItem(key);
                        System.out.print("key=" + key);
                        System.out.print("title=" + item.getTitle());
                        System.out.println(" value=" + hashMap.get(key));
                    }*/
                }

            }
        });
        countDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mListener.onClick(hashMap, arrayList.get(position).getTime_id())) {
                    subCardNumbers(arrayList.get(position).getTime_id());
                    count.setText(String.valueOf(hashMap.get(arrayList.get(position).getTime_id())));

                    /*
                    Iterator<Long> iterator = hashMap.keySet().iterator();
                    while (iterator.hasNext()) {
                        Long key = iterator.next();
                        cardListItem item = new DataBaseManager(mContext).getCardItem(key);
                        System.out.print("key=" + key);
                        System.out.print("title=" + item.getTitle());
                        System.out.println(" value=" + hashMap.get(key));
                    }*/
                }

            }
        });


        //show Image
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowImageDialog showImageDialog = new ShowImageDialog(mContext, arrayList.get(position).getImg());
                showImageDialog.show();
            }
        });


        return convertView;
    }


    public int getCardNumbers(long id) {
        return hashMap.get(id);
    }

    public void addCardNumbers(long id) {
        if (hashMap.get(id) != null) {
            int count = hashMap.get(id);
            hashMap.put(id, count + 1);
        } else {//0 인경우.
            hashMap.put(id, 1);
        }
        notifyDataSetChanged();
    }

    public void subCardNumbers(long id) {

        if (hashMap.get(id) != null) {
            int count = hashMap.get(id);
            //2장 이상인 경우만.
            if (hashMap.get(id) > 1)
                hashMap.put(id, count - 1);

                //1장 인경우.
            else if (hashMap.get(id) == 1)
                hashMap.remove(id);
        }


        notifyDataSetChanged();
    }

    public HashMap getAllCardNumbers() {
        return hashMap;
    }


}
