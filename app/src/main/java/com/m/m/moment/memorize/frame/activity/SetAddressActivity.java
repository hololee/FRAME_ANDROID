package com.m.m.moment.memorize.frame.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.m.m.moment.memorize.frame.R;
import com.m.m.moment.memorize.frame.manager.TypefaceManager;

public class SetAddressActivity extends BaseActivity {

    TextView address1;
    EditText address2;

    WebView webView;

    Button button;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_address);

        address1 = findViewById(R.id.address1);
        address2 = findViewById(R.id.address2);
        webView = findViewById(R.id.webView);
        button = findViewById(R.id.button);

        //set typeface.
        TypefaceManager typefaceManager = new TypefaceManager(getApplicationContext());
        address1.setTypeface(typefaceManager.getTypeNormal());
        address2.setTypeface(typefaceManager.getTypeNormal());
        button.setTypeface(typefaceManager.getTypeNormal());


        handler = new Handler();
        init_web();


        //주소 내보내기.
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("address", address1.getText().toString() + " " + address2.getText().toString());
                setResult(RESULT_OK, intent);
                finish();

            }
        });


    }

    private void init_web() {

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.addJavascriptInterface(new AndroidBridge(), "frameAddress");
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl("http://lccandol.cafe24.com/set_address.php");

    }


    private class AndroidBridge {

        @JavascriptInterface
        public void setAddress(final String arg1, final String arg2, final String arg3) {

            handler.post(new Runnable() {

                @Override

                public void run() {

                    address1.setText(String.format("(%s) %s %s", arg1, arg2, arg3));
                    init_web();

                }

            });

        }

    }

}
