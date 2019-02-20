package com.m.m.moment.memorize.frame.activity.buy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.m.m.moment.memorize.frame.R;
import com.m.m.moment.memorize.frame.activity.BaseActivity;
import com.m.m.moment.memorize.frame.adapter.SelectCountListAdapter;
import com.m.m.moment.memorize.frame.database.DataBaseManager;
import com.m.m.moment.memorize.frame.dialog.LoadingDialog;
import com.m.m.moment.memorize.frame.etc.Application;
import com.m.m.moment.memorize.frame.etc.Maker;
import com.m.m.moment.memorize.frame.etc.VolleyMultipartRequest;
import com.m.m.moment.memorize.frame.etc.numberCountClickListener;
import com.m.m.moment.memorize.frame.item.cardListItem;
import com.m.m.moment.memorize.frame.manager.DateManager;
import com.m.m.moment.memorize.frame.manager.FileManager;
import com.m.m.moment.memorize.frame.manager.SharedPreferenceManager;
import com.m.m.moment.memorize.frame.manager.TypefaceManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BuyActivity extends BaseActivity implements numberCountClickListener {


    TextView message, button, guide_text;
    ListView list;


    HashMap cardsCounts;
    ArrayList<Long> ItemIdList;


    DataBaseManager manager;

    SharedPreferenceManager sharedPreferenceManager;

    LoadingDialog loadingDialog;

    //주문번호.
    long createdOrderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        cardsCounts = new HashMap();
        //  selectedItemIdList = new ArrayList<>();
        ItemIdList = new ArrayList<>();

        list = findViewById(R.id.list);

        loadingDialog = new LoadingDialog(BuyActivity.this);

        message = findViewById(R.id.message);
        guide_text = findViewById(R.id.guide_text);
        button = findViewById(R.id.button);


        TypefaceManager typefaceManager = new TypefaceManager(getApplicationContext());

        //set typeface.
        message.setTypeface(typefaceManager.getTypeNormal());
        guide_text.setTypeface(typefaceManager.getTypeNormal());
        button.setTypeface(typefaceManager.getTypeNormal());


        //db 메니져.
        manager = new DataBaseManager(getApplicationContext());

        //데이터 가져오기.
        for (cardListItem item : manager.getAllCardItemList()) {
            ItemIdList.add(item.getTime_id());
        }


        //설정 메니져. setting data.
        sharedPreferenceManager = new SharedPreferenceManager(getApplicationContext());


        //선택되었는지 확인, 확인되면 전송., 이메일 전송을 다이얼로그로 확인하고 확인되면 주문정보창으로 넘어가기.
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cardsCounts.size() >= 1 && cardsCounts.size() <= 20) {
                    //카드 장 수 확인.

                    //데이터 보내기.
                    send();


                } else {

                    Toast toast = Toast.makeText(BuyActivity.this, getString(R.string.alert_selected_count), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                }

            }
        });

        //set selected list.
        SelectCountListAdapter adapter = new SelectCountListAdapter(BuyActivity.this, ItemIdList, this);
        list.setAdapter(adapter);


        //이미지 보이기.
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                cardListItem item = (cardListItem) list.getAdapter().getItem(position);
                Toast.makeText(BuyActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();


            }
        });


    }


    //uplaod data.
    private void send() {

        Log.d("state", "ok");


        loadingDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();

                createdOrderId = (Calendar.getInstance().getTimeInMillis() / 2) + ((int) (Math.random() * 20000) + 1);
                //the number of selected items are less then 10.


                //set cards data.
                DataBaseManager manager = new DataBaseManager(getApplicationContext());


                //이미지 압축하고, 확장자 변경.
                //이미지 생성.
                Iterator<Long> iterator = cardsCounts.keySet().iterator();
                while (iterator.hasNext()) {
                    Long key = iterator.next();

                    //0인 이미지 들은 제외.
                    if ((int) cardsCounts.get(key) == 0) {

                    } else {
                        cardListItem item = manager.getCardItem(key);
                        new Maker(BuyActivity.this).makeCard(item.getTime_id(), item.getImg(), item.getTitle(), DateManager.getDateText(item.getDate()), item.getContent(), (int) cardsCounts.get(key));

                    }
                }


                //커버 이미지 생성 및 이동.
                new Maker(BuyActivity.this).makeCover();


                //data file 생성 및 이동, data.txt
                //주문번호, 성명, 연락처, 주소, 카드 장수
                String data = "주문번호 : " + createdOrderId +
                        "\n성명 : " + new SharedPreferenceManager(getApplicationContext()).getUserName() +
                        "\n연락처 : " + new SharedPreferenceManager(getApplicationContext()).getUserCallnumber() +
                        "\n주소 : " + new SharedPreferenceManager(getApplicationContext()).getUserAddress();
                new FileManager(getApplicationContext()).CreateDataPath(data);


                //이미지 압축.
                new FileManager(getApplicationContext()).zipData(createdOrderId);



                //서버로 전송.
                VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, "http://lccandol.cafe24.com/imageUpload.php", new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        String resultResponse = new String(response.data);


                        //success, fail
                        if (resultResponse.equals("success")){


                            //전송완료시.
                            Toast.makeText(getApplicationContext(), "전송성공!", Toast.LENGTH_SHORT).show();


                            //mm/upload 폴더 삭제.
                            new FileManager(getApplicationContext()).remove(getFilesDir() + "/MM/upload");

                            //zip 파일 지우기.
                            new FileManager(getApplicationContext()).removeZipData(createdOrderId);


                            // 업로드시 주문 정보 창으로 .
                            Intent intent = new Intent(getApplicationContext(), PayActivity.class);
                            intent.putExtra("order_id", createdOrderId);
                            intent.putExtra("list", cardsCounts);
                            startActivity(intent);
                            overridePendingTransition(R.anim.translate_left_appeared, R.anim.translate_left_disappeared);
                            finish();

                            //화면 감추기.
                            loadingDialog.dismiss();




                        }else if(resultResponse.equals("fail")){

                            Toast.makeText(getApplicationContext(), "데이터 업로드에 실패하였습니다.", Toast.LENGTH_SHORT).show();

                        }else{

                            Toast.makeText(getApplicationContext(), "알수없는 오류가 발생했습니다.\nframe_service@naver.com 으로 알려주세요.", Toast.LENGTH_SHORT).show();

                        }



                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();

                        //파일 이름용.
                        params.put("name", String.valueOf(createdOrderId));

                        return params;
                    }

                    @Override
                    protected Map<String, DataPart> getByteData() {
                        Map<String, DataPart> params = new HashMap<>();

                        File file = new File(getFilesDir() + "/"+createdOrderId+".zip");
                        //init array with file length
                        byte[] bytesArray = new byte[(int) file.length()];

                        FileInputStream fis = null;
                        try {
                            fis = new FileInputStream(file);
                            fis.read(bytesArray); //read file into bytes[]
                            fis.close();

                            //zip data 전송,.
                            params.put("data", new DataPart(createdOrderId + ".zip", bytesArray, "application/octet-stream"));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        return params;
                    }
                };

                Application.getInstance().addToRequestQueue(multipartRequest);


            }
        }).start();



    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.translate_left_appeared, R.anim.translate_left_disappeared);
    }

    @Override
    public boolean onClick(HashMap cardCounts, long id) {

        if (cardCounts.size() <= 19) {
            //추가 가능할때.
            cardsCounts = cardCounts;


            return true;
        } else {

            Toast toast = Toast.makeText(BuyActivity.this, getString(R.string.alert_selected_count), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

            return false;
        }
    }
}
