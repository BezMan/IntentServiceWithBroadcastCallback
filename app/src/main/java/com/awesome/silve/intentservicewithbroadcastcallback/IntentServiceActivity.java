package com.awesome.silve.intentservicewithbroadcastcallback;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class IntentServiceActivity extends Activity {

    private MyWebRequestReceiver receiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        IntentFilter filter = new IntentFilter(MyWebRequestReceiver.PROCESS_RESPONSE);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new MyWebRequestReceiver();
        registerReceiver(receiver, filter);

        Button addButton = (Button) findViewById(R.id.sendRequest);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent msgIntent = new Intent(IntentServiceActivity.this, MyWebRequestService.class);

                msgIntent.putExtra(MyWebRequestService.REQUEST_STRING, "https://www.facebook.com");
                startService(msgIntent);

                msgIntent.putExtra(MyWebRequestService.REQUEST_STRING, "http://www.ebay.com");
                startService(msgIntent);

                msgIntent.putExtra(MyWebRequestService.REQUEST_STRING, "http://www.google.com");
                startService(msgIntent);
            }
        });

    }

    @Override
    public void onDestroy() {
        this.unregisterReceiver(receiver);
        super.onDestroy();
    }

    public class MyWebRequestReceiver extends BroadcastReceiver {

        public static final String PROCESS_RESPONSE = "com.as400samplecode.intent.action.PROCESS_RESPONSE";

        @Override
        public void onReceive(Context context, Intent intent) {
            String responseString = intent.getStringExtra(MyWebRequestService.RESPONSE_STRING);
            String reponseMessage = intent.getStringExtra(MyWebRequestService.RESPONSE_MESSAGE);

            TextView myTextView = (TextView) findViewById(R.id.response);
            myTextView.setText(responseString);

            WebView myWebView = (WebView) findViewById(R.id.myWebView);
            myWebView.getSettings().setJavaScriptEnabled(true);
            try {
                myWebView.loadData(URLEncoder.encode(reponseMessage,"utf-8").replaceAll("\\+"," "), "text/html", "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

//            open other app, using its package name:
//            Intent launchIntent = getPackageManager().getLaunchIntentForPackage("actionitem.test.silve.breezometer");
//            if (launchIntent != null) {
//                startActivity(launchIntent);//null pointer check in case package name was not found
//            }



        }


    }
}
