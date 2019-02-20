package com.m.m.moment.memorize.frame.activity.buy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.m.m.moment.memorize.frame.R;
import com.m.m.moment.memorize.frame.activity.BaseActivity;
import com.m.m.moment.memorize.frame.adapter.SelectedCardListAdapter;
import com.m.m.moment.memorize.frame.database.DataBaseManager;
import com.m.m.moment.memorize.frame.manager.SharedPreferenceManager;
import com.m.m.moment.memorize.frame.manager.TypefaceManager;

import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import kr.co.bootpay.Bootpay;
import kr.co.bootpay.BootpayAnalytics;
import kr.co.bootpay.BootpayDialog;
import kr.co.bootpay.CancelListener;
import kr.co.bootpay.CloseListener;
import kr.co.bootpay.ConfirmListener;
import kr.co.bootpay.DoneListener;
import kr.co.bootpay.ErrorListener;
import kr.co.bootpay.ReadyListener;
import kr.co.bootpay.enums.PG;

public class PayActivity extends BaseActivity {


    private  String TYPE_PREMIUM;

    HashMap cardsCounts;

    ArrayList<Long> selectedItemIdList = new ArrayList<>();
    DataBaseManager manager;
    ListView list;

    SharedPreferenceManager sharedPreferenceManager;

    Button payBtn;

    private String deliveryType;
    //price
    private int itsUpToYou = 0;

    TextView nameEdit, phoneEdit, addressEdit;
    TextView message, guide_text, name_tag, phone_tag, address_tag, total_text, cards_text, delivery_text;
    TextView cards, delivery;
    //TextView basic, premium;
    TextView total;

    DecimalFormat format;

    long createdOrderId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        //set default.
        TYPE_PREMIUM = getString(R.string.delivery_type);
        deliveryType = TYPE_PREMIUM;


        format = new DecimalFormat("#,###");

        list = findViewById(R.id.list);

        message = findViewById(R.id.message);
        guide_text = findViewById(R.id.guide_text);
        //send_type_text = findViewById(R.id.send_type_text);
        //send_guide = findViewById(R.id.send_guide);
        name_tag = findViewById(R.id.name_tag);
        phone_tag = findViewById(R.id.phone_tag);
        address_tag = findViewById(R.id.address_tag);
        total_text = findViewById(R.id.total_text);
        cards_text = findViewById(R.id.cards_text);
        delivery_text = findViewById(R.id.delivery_text);

        nameEdit = findViewById(R.id.name_text);
        phoneEdit = findViewById(R.id.phone_text);
        addressEdit = findViewById(R.id.address_text);

        total = findViewById(R.id.total);

        cards = findViewById(R.id.cards);
        delivery = findViewById(R.id.delivery);

        // basic = findViewById(R.id.basic);
        // premium = findViewById(R.id.premium);

        cardsCounts = (HashMap) getIntent().getSerializableExtra("list");
        createdOrderId = getIntent().getLongExtra("order_id", 0);


        //id lists.
        Iterator<Long> iterator = cardsCounts.keySet().iterator();
        while (iterator.hasNext()) {
            Long key = iterator.next();
            //cardListItem item = new DataBaseManager(getApplicationContext()).getCardItem(key);
            selectedItemIdList.add(key);
            //set init price. card_price + basic_delivery_price;
            itsUpToYou += 500 * (int) cardsCounts.get(key);
        }


        //discount when over the 3M
        if (itsUpToYou >= 30000) {
            Toast.makeText(this, getString(R.string.discount), Toast.LENGTH_SHORT).show();

            //set init price. card_price + basic_delivery_price;
            cards.setText(format.format(itsUpToYou));
            delivery.setText(format.format(0));

        } else {
            Toast.makeText(this, getString(R.string.discount), Toast.LENGTH_SHORT).show();

            //add delivery cost.
            itsUpToYou += 3000;

            //set init price. card_price + basic_delivery_price;
            cards.setText(format.format(itsUpToYou - 3000));
            delivery.setText(format.format(3000));
        }


        //set selected list.
        SelectedCardListAdapter selectedCardListAdapter = new SelectedCardListAdapter(getApplicationContext(), selectedItemIdList, cardsCounts);
        list.setAdapter(selectedCardListAdapter);


        //set typeface.
        TypefaceManager typefaceManager = new TypefaceManager(getApplicationContext());
        message.setTypeface(typefaceManager.getTypeNormal());
        guide_text.setTypeface(typefaceManager.getTypeNormal());
        //send_type_text.setTypeface(typefaceManager.getTypeNormal());
        //send_guide.setTypeface(typefaceManager.getTypeNormal());
        name_tag.setTypeface(typefaceManager.getTypeNormal());
        phone_tag.setTypeface(typefaceManager.getTypeNormal());
        address_tag.setTypeface(typefaceManager.getTypeNormal());
        total_text.setTypeface(typefaceManager.getTypeNormal());
        cards_text.setTypeface(typefaceManager.getTypeNormal());
        delivery_text.setTypeface(typefaceManager.getTypeNormal());
        nameEdit.setTypeface(typefaceManager.getTypeNormal());
        phoneEdit.setTypeface(typefaceManager.getTypeNormal());
        addressEdit.setTypeface(typefaceManager.getTypeNormal());
        total.setTypeface(typefaceManager.getTypeNormal());
        // basic.setTypeface(typefaceManager.getTypeNormal());
        // premium.setTypeface(typefaceManager.getTypeNormal());
        cards.setTypeface(typefaceManager.getTypeNormal());
        delivery.setTypeface(typefaceManager.getTypeNormal());

        //init payment.
        BootpayAnalytics.init(this, "5c0e133f396fa60d33bd2c2a");


        //db 메니져.
        manager = new DataBaseManager(getApplicationContext());

        //설정 메니져. setting data.
        sharedPreferenceManager = new SharedPreferenceManager(getApplicationContext());
        nameEdit.setText(sharedPreferenceManager.getUserName());
        phoneEdit.setText(sharedPreferenceManager.getUserCallnumber());
        addressEdit.setText(sharedPreferenceManager.getUserAddress());

        //total text;
        total.setText(String.valueOf(format.format(itsUpToYou)));

        /*
         //set send type.
         basic.setTextColor(getResources().getColor(R.color.midBlack));
         basic.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
        if (deliveryType == TYPE_PREMIUM) {
        basic.setTextColor(getResources().getColor(R.color.midBlack));
        premium.setTextColor(getResources().getColor(R.color.midGray));
        deliveryType = TYPE_NORMAL;
        //set price. card_price + premium_delivery_price;
        itsUpToYou = 500 * selectedItemIdList.size() + 500;
        //setPrice
        total.setText(format.format(itsUpToYou));
        delivery.setText(format.format(500));
        }
        }
        });
         premium.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
        if (deliveryType == TYPE_NORMAL) {
        basic.setTextColor(getResources().getColor(R.color.midGray));
        premium.setTextColor(getResources().getColor(R.color.midBlack));
        deliveryType = TYPE_PREMIUM;
        //set price. card_price + basic_delivery_price;
        itsUpToYou = 500 * selectedItemIdList.size() + 2000;
        //setPrice
        total.setText(format.format(itsUpToYou));
        delivery.setText(format.format(2000));

        }
        }
        });
         */

        //pay btn.
        payBtn = findViewById(R.id.button);
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(PayActivity.this);
                builder.setMessage(getString(R.string.alert_pay));
                builder.setPositiveButton(getString(R.string.dialog_positive), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // 결제호출
                        BootpayDialog.Builder builder = Bootpay.init(PayActivity.this);
                        builder.setApplicationId("5c0e133f396fa60d33bd2c2a") // 해당 프로젝트(안드로이드)의 application id 값
                                .setPG(PG.KCP) // 결제할 PG 사
                                .setUserName(sharedPreferenceManager.getUserName())
                                .setUserAddr(sharedPreferenceManager.getUserAddress())
                                .setUserPhone(sharedPreferenceManager.getUserCallnumber()) // 구매자 전화번호
                                .setOrderId(String.valueOf(createdOrderId)) // 결제 고유번호
                                .setPrice(itsUpToYou) // 결제할 금액
                                .setQuotas(new int[]{0}) // 일시불, 2개월, 3개월 할부 허용, 할부는 최대 12개월까지 사용됨 (5만원 이상 구매시 할부허용 범위)
                                .onConfirm(new ConfirmListener() { // 결제가 진행되기 바로 직전 호출되는 함수로, 주로 재고처리 등의 로직이 수행
                                    @Override
                                    public void onConfirm(@Nullable String message) {
                                        //   if (0 < stuck); // 재고가 있을 경우.
                                        Bootpay.confirm(message);
                                        Log.d("confirm", message);
                                    }
                                })
                                .onDone(new DoneListener() { // 결제완료시 호출, 아이템 지급 등 데이터 동기화 로직을 수행합니다
                                    @Override
                                    public void onDone(@Nullable String message) {
                                        Log.d("done", message);

                                        Toast.makeText(PayActivity.this, getString(R.string.alert_pay_confirm), Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(getApplicationContext(), PayResultActivity.class);
                                        intent.putExtra("list", selectedItemIdList);
                                        intent.putExtra("order_id", String.valueOf(createdOrderId));
                                        intent.putExtra("order_price", format.format(itsUpToYou));
                                        intent.putExtra("order_type", deliveryType);
                                        startActivity(intent);
                                        finish();
                                        overridePendingTransition(R.anim.translate_left_appeared, R.anim.translate_left_disappeared);


                                    }
                                })
                                .onReady(new ReadyListener() { // 가상계좌 입금 계좌번호가 발급되면 호출되는 함수입니다.
                                    @Override
                                    public void onReady(@Nullable String message) {
                                        Log.d("ready", message);

                                        Toast.makeText(PayActivity.this, getString(R.string.alert_imagine_bank), Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(getApplicationContext(), PayResultActivity.class);
                                        intent.putExtra("list", selectedItemIdList);
                                        intent.putExtra("order_id", String.valueOf(createdOrderId));
                                        intent.putExtra("order_price", format.format(itsUpToYou));
                                        intent.putExtra("order_type", deliveryType);
                                        startActivity(intent);
                                        finish();
                                        overridePendingTransition(R.anim.translate_left_appeared, R.anim.translate_left_disappeared);


                                    }
                                })
                                .onCancel(new CancelListener() { // 결제 취소시 호출
                                    @Override
                                    public void onCancel(@Nullable String message) {
                                        Log.d("cancel", message);
                                        Toast.makeText(PayActivity.this, getString(R.string.alert_cancel), Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .onError(new ErrorListener() { // 에러가 났을때 호출되는 부분
                                    @Override
                                    public void onError(@Nullable String message) {
                                        Log.d("error", message);
                                        Toast.makeText(PayActivity.this, getString(R.string.alert_pay_error), Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .onClose(new CloseListener() { //결제창이 닫힐때 실행되는 부분
                                    @Override
                                    public void onClose(String message) {
                                        Log.d("close", message);
                                    }
                                });

                        //결제 아이템 이름설정.
                        String itemName = "";
                        /*
                         if (deliveryType.equals(TYPE_NORMAL)) {
                         itemName = createdOrderId + " N." + selectedItemIdList.size();
                         builder.setName(itemName) // 결제할 상품명
                         .addItem("FRAME CARD", selectedItemIdList.size(), "ITEM_CARDS", 500) // 주문정보에 담길 상품정보, 통계를 위해 사용
                         .addItem("FRAME DELIVERY BASIC", 1, "DELIVERY_BASIC", 500); // 주문정보에 담길 상품정보, 통계를 위해 사용


                         } else if (deliveryType.equals(TYPE_PREMIUM)) {
                         itemName = createdOrderId + " P." + selectedItemIdList.size();
                         builder.setName(itemName) // 결제할 상품명
                         .addItem("FRAME CARD", selectedItemIdList.size(), "ITEM_CARDS", 500) // 주문정보에 담길 상품정보, 통계를 위해 사용
                         .addItem("FRAME DELIVERY PREMIUM", 1, "DELIVERY_PREMIUM", 2000); // 주문정보에 담길 상품정보, 통계를 위해 사용
                         }
                         */
                        itemName = createdOrderId + " P." + selectedItemIdList.size();
                        builder.setName(itemName) // 결제할 상품명
                                .addItem("FRAME CARD", selectedItemIdList.size(), "ITEM_CARDS", 500) // 주문정보에 담길 상품정보, 통계를 위해 사용
                                .addItem("FRAME DELIVERY PREMIUM", 1, "DELIVERY_PREMIUM", 3000); // 주문정보에 담길 상품정보, 통계를 위해 사용


                        builder.show();

                    }
                });
                builder.setNegativeButton(getString(R.string.dialog_negative), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();


            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.translate_right_appeared, R.anim.translate_right_disappeared);
    }


    /*
    @Override
    public void onClick(HashMap cardCounts, long id) {

        //clear
        itsUpToYou = 0;

        Iterator<Long> iterator = cardCounts.keySet().iterator();
        while (iterator.hasNext()) {
            Long key = iterator.next();
            cardListItem item = new DataBaseManager(getApplicationContext()).getCardItem(key);
            //System.out.print("key=" + key);
            //System.out.print("title=" + item.getTitle());
            //System.out.println(" value=" + cardCounts.get(key));

            itsUpToYou += (int) cardCounts.get(key) * 500;
        }

        //배송비.
        itsUpToYou += 3000;

        //카드값 표시.
        cards.setText(format.format(itsUpToYou - 3000));
        //총비용 표시.
        total.setText(String.valueOf(format.format(itsUpToYou)));

    }*/


    //count up or down.





    /*

    //업로드 데이터.
    public void uploadPaymentData(final long order_id, final String order_time, final String order_name, final String order_phone, final String order_address, final String order_price, final String order_type) {

        //데이터 가져오기.
        StringRequest req = new StringRequest(Request.Method.POST, "http://lccandol.cafe24.com/alert_cards.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.get("return_code").equals("1111")) {
                        //정상적인 업로드시.

                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), getString(R.string.upload_error), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                //주문정보 보내기.
                Map<String, String> params = new HashMap<>();
                params.put("order_id", String.valueOf(order_id));
                params.put("order_time", order_time);
                params.put("order_name", order_name);
                params.put("order_phone", order_phone);
                params.put("order_address", order_address);
                params.put("order_price", String.valueOf(order_price));
                params.put("order_type", order_type);

                return params;
            }
        };

        Application.getInstance().addToRequestQueue(req);


    }
*/

}
