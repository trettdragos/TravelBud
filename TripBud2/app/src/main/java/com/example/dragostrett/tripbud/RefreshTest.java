package com.example.dragostrett.tripbud;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by DragosTrett on 07.06.2017.
 */

public class RefreshTest extends AsyncTask<String, Integer, String> {
    Context context;
    public RefreshTest(Context context){
        this.context=context;
    }
    @Override
    protected String doInBackground(String... params) {
        return null;
    }
    @Override
    protected void onPostExecute(String result){
        MainActivity.refresh();
        MainActivity.timer.start();
    }
}
