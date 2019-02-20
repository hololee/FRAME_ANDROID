package com.m.m.moment.memorize.frame.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.m.m.moment.memorize.frame.activity.buy.BuyIntroActivity;
import com.m.m.moment.memorize.frame.activity.CardAddActivity;
import com.m.m.moment.memorize.frame.activity.CardEditActivity;
import com.m.m.moment.memorize.frame.R;
import com.m.m.moment.memorize.frame.activity.MainActivity;
import com.m.m.moment.memorize.frame.adapter.ContentsCardListAdapter;
import com.m.m.moment.memorize.frame.item.cardListItem;
import com.m.m.moment.memorize.frame.manager.SharedPreferenceManager;
import com.m.m.moment.memorize.frame.manager.TypefaceManager;


public class ListFragment extends Fragment {

    public static final int ADD_FLAG = 49760;


    int pageNumber;
    Context mContext;


    TextView title, btn_add;
    TextView buy_btn;
    ListView cardListView;

    TypefaceManager typefaceManager;
    SharedPreferenceManager sharedPreferenceManager;


    public ListFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();


        //list invalidate as lisView is refreshed.
        try {
            if (cardListView != null)  //prevent null data situation.
                cardListInit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mContext = getActivity().getApplicationContext();

        View list = inflater.inflate(R.layout.fragment_list, container, false);
        typefaceManager = new TypefaceManager(mContext);
        sharedPreferenceManager = new SharedPreferenceManager(mContext);

        //title set LIST
        title = list.findViewById(R.id.title);
        title.setTypeface(typefaceManager.getQuickTypeNormal());

        btn_add = list.findViewById(R.id.btn_add);
        btn_add.setTypeface(typefaceManager.getTypeNormal());
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //카드 추가 페이지로 이동.
                Intent addIntent = new Intent(mContext, CardAddActivity.class);
                startActivity(addIntent);
                getActivity().overridePendingTransition(R.anim.translate_right_appeared, R.anim.translate_right_disappeared);

            }
        });


        cardListView = list.findViewById(R.id.list);
        //invalidate card list.
        cardListInit();


        //리스트 클릭시 해당 페이지로 이동.
        cardListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.moveToSelectedPage(position + 2);// because of list and main.
            }
        });


        //card edit, delete mode.
        cardListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                long selectedId = ((cardListItem) cardListView.getAdapter().getItem(position)).getTime_id();
                Intent intent = new Intent(mContext, CardEditActivity.class);
                intent.putExtra("selectedId", selectedId);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.translate_right_appeared, R.anim.translate_right_disappeared);

                return true;
            }
        });


        //set buy
        //buy button setting
        buy_btn = list.findViewById(R.id.buy_btn);
        buy_btn.setTypeface(typefaceManager.getTypeNormal());

        //구매 버튼.
        buy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (sharedPreferenceManager.getUserAddress() != null && sharedPreferenceManager.getUserCallnumber() != null && sharedPreferenceManager.getUserName() != null && sharedPreferenceManager.getUserName().length() > 0 && sharedPreferenceManager.getUserCallnumber().length() > 0 && sharedPreferenceManager.getUserAddress().length() > 0) {


                    Intent settingIntent = new Intent(mContext, BuyIntroActivity.class);
                    startActivity(settingIntent);
                    getActivity().overridePendingTransition(R.anim.translate_right_appeared, R.anim.translate_right_disappeared);
                } else {
                    Toast.makeText(mContext, getString(R.string.alert_info_check), Toast.LENGTH_SHORT).show();
                }


            }
        });


        return list;
    }


    //card list invalidate.
    public void cardListInit() {

        ContentsCardListAdapter adapter = new ContentsCardListAdapter(mContext);
        cardListView.setAdapter(adapter);

    }

    public void initPage(int pageNumber) {
        this.pageNumber = pageNumber;
    }


}
