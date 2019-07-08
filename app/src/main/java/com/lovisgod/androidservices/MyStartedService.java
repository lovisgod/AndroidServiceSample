package com.lovisgod.androidservices;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

public class MyStartedService extends Service {
    public static final String TAG = MyStartedService.class.getSimpleName();
    MyAssyncTask myAssyncTask = new MyAssyncTask();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: Thread name" + Thread.currentThread().getName());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: Thread name" + Thread.currentThread().getName());
        int sleeptime = intent.getIntExtra("sleeptime", 1);
        //perform whatever task we want to perform here and it must not be too long so it does not block UI
       myAssyncTask.execute(sleeptime);
//        return START_REDELIVER_INTENT; //the service will be restarted and the intent delivered to this service will not be null
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: Thread name" + Thread.currentThread().getName());
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: Thread name" + Thread.currentThread().getName());

        if(myAssyncTask.getStatus() == AsyncTask.Status.RUNNING){
            Log.d(TAG, "onDestroy: Async task still running");

            myAssyncTask.cancel(true);

        }
    }

    class MyAssyncTask extends AsyncTask<Integer, String, String>{
        public String TAG = MyStartedService.class.getSimpleName();

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param "voids" The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "onPreExecute: Thread name" + Thread.currentThread().getName());
        }

        @Override //perform long running task
        protected String doInBackground(Integer... integers) {
            Log.d(TAG, "ondoInBackground: Thread name" + Thread.currentThread().getName());
            int  sleeptime = integers[0];
            int ctr = 1;
            while (ctr <= sleeptime){
                publishProgress("Counter is now " + ctr);
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                  e.printStackTrace();
                }
                ctr++;
            }
            return "Counter stopped at " + ctr + " " + "seconds";
        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            Log.d(TAG, "onPostExecute: Thread name" + Thread.currentThread().getName());
            Intent intent = new Intent("action.service.to.activity");
            intent.putExtra("startServiceResult", str);
            sendBroadcast(intent);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Toast.makeText(MyStartedService.this, "value is " + values[0], Toast.LENGTH_SHORT).show(); //get the first index of the values
            Log.d(TAG, "onProgressUpdate: Thread name" + Thread.currentThread().getName());
        }
    }
}
