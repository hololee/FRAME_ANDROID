package com.m.m.moment.memorize.frame.activity.buy;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.m.m.moment.memorize.frame.R;
import com.m.m.moment.memorize.frame.activity.BaseActivity;
import com.m.m.moment.memorize.frame.adapter.SelectedCardListAdapter;
import com.m.m.moment.memorize.frame.manager.TypefaceManager;

import java.util.ArrayList;

public class PayResultActivity extends BaseActivity {

    ArrayList<Long> selectedItemIdList;
    ListView list;

    TextView pay_text1, pay_text2, order_id_text, order_price_text, order_type_text, cafe, email_ssub;
    TextView backBtn;

    LinearLayout emailBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_result);

        list = findViewById(R.id.list);
        emailBox = findViewById(R.id.email_box);

        pay_text1 = findViewById(R.id.pay_text1);
        pay_text2 = findViewById(R.id.pay_text2);
        order_id_text = findViewById(R.id.order_id_text);
        order_price_text = findViewById(R.id.order_price_text);
        order_type_text = findViewById(R.id.order_type_text);
        cafe = findViewById(R.id.cafe);
        email_ssub = findViewById(R.id.email_ssub);

        backBtn = findViewById(R.id.button);

        //set typeface.
        TypefaceManager typefaceManager = new TypefaceManager(getApplicationContext());
        pay_text1.setTypeface(typefaceManager.getTypeNormal());
        pay_text2.setTypeface(typefaceManager.getTypeNormal());
        order_id_text.setTypeface(typefaceManager.getTypeNormal());
        order_price_text.setTypeface(typefaceManager.getTypeNormal());
        order_type_text.setTypeface(typefaceManager.getTypeNormal());
        cafe.setTypeface(typefaceManager.getQuickTypeNormal());
        email_ssub.setTypeface(typefaceManager.getTypeNormal());
        backBtn.setTypeface(typefaceManager.getTypeNormal());


        selectedItemIdList = (ArrayList<Long>) getIntent().getSerializableExtra("list");
        String orderId = getIntent().getStringExtra("order_id");
        String orderPrice = getIntent().getStringExtra("order_price");
        String orderType = getIntent().getStringExtra("order_type");

        //데이터 세팅.
        TextView orderIdText = findViewById(R.id.order_id);
        TextView orderPriceText = findViewById(R.id.order_price);
        TextView orderTypeText = findViewById(R.id.order_type);

        orderIdText.setText(orderId);
        orderPriceText.setText(orderPrice);
        orderTypeText.setText(orderType);


        //set selected list.
        SelectedCardListAdapter adapter = new SelectedCardListAdapter(PayResultActivity.this, selectedItemIdList, null);
        list.setAdapter(adapter);


        //카페이동.
        emailBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://cafe.naver.com/framelife"));
                startActivity(intent);
            }
        });


        //back.
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //back main page.
                finish();


            }
        });


    }


    @Override
    public void onBackPressed() {
        //back main page.
        finish();
    }
}
