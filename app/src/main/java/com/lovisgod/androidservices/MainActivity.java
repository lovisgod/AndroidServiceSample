package com.lovisgod.androidservices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView startedservicetext, intentservicetext;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startedservicetext = findViewById(R.id.startedservice_text);
        intentservicetext = findViewById(R.id.intentservice_text);
    }

    public void startService(View view) {
        Intent intent = new Intent(this, MyStartedService.class);
        intent.putExtra("sleeptime", 10);
        startService(intent);
    }

    public void stopService(View view) {
        Intent intent = new Intent(this, MyStartedService.class);
        stopService(intent);
    }

    public void startintentservice(View view) {
        ResultReceiver resultReceiver = new MyresultReciever(null); //you can pass in null into the params as handler
        Intent intent = new Intent(this, MyintentService.class);
        intent.putExtra("sleepTime", 10);
        intent.putExtra("reciever", resultReceiver); // pass the resultreciever as a parcelable object
        startService(intent);
    }



    // register the Broadcast reciever with intent filter


    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.service.to.activity");
        registerReceiver(mystartedserviceReciever, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(mystartedserviceReciever);
    }

    public BroadcastReceiver mystartedserviceReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getStringExtra("startServiceResult");
            startedservicetext.setText(result);
        }
    };

    // this inner class handles ResultReciever
    public class MyresultReciever extends ResultReceiver{

        /**
         * Create a new ResultReceive to receive results.  Your
         * {@link #onReceiveResult} method will be called from the thread running
         * <var>handler</var> if given, or from an arbitrary thread if null.
         *
         * @param handler
         */
        public MyresultReciever(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == 3 && resultData != null){
                final String result = resultData.getString("resultIntentService");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        intentservicetext.setText(result);
                    }
                });
            }
        }
    }
}
