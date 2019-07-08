package com.lovisgod.androidservices;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.Log;

public class MyintentService extends IntentService {
    private static final String TAG = MyintentService.class.getSimpleName();

    public MyintentService() {
        super("MyWorkerThread"); //the name in the super constructor is the name of our thread
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: Thread name" + Thread.currentThread().getName());
    }

    /**
     * This method is invoked on the worker thread with a request to process.
     * Only one Intent is processed at a time, but the processing happens on a
     * worker thread that runs independently from other application logic.
     * So, if this code takes a long time, it will hold up other requests to
     * the same IntentService, but it will not hold up anything else.
     * When all requests have been handled, the IntentService stops itself,
     * so you should not call {@link #stopSelf}.
     *
     * @param intent The value passed to {@link
     *               Context#startService(Intent)}.
     *               This may be null if the service is being restarted after
     *               its process has gone away; see
     *               {@link Service#onStartCommand}
     *               for details.
     */



    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.i(TAG, "onHandleIntent: Thread name" + Thread.currentThread().getName());
        int  sleeptime = intent.getIntExtra("sleepTime", 1);
        ResultReceiver resultReceiver = intent.getParcelableExtra("reciever"); //get th result reciever as parcelable from the intent
        int ctr = 1;
        while (ctr <= sleeptime){
            Log.i(TAG,"Counter is now " + ctr);
            try{
                Thread.sleep(1000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            ctr++;
        }

        Bundle bundle = new Bundle();
        bundle.putString("resultIntentService", "Counter Stopped at " + ctr + "seconds");
        resultReceiver.send(3, bundle);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: Thread name" + Thread.currentThread().getName());
    }
}
