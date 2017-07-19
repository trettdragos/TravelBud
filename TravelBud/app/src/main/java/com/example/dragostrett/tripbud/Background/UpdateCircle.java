package com.example.dragostrett.tripbud.Background;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.dragostrett.tripbud.BasicInfo.TripInfo;
import com.example.dragostrett.tripbud.BasicInfo.UserInfo;
import com.example.dragostrett.tripbud.MainActivity;
import com.example.dragostrett.tripbud.TripActivity;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import java.sql.DriverManager;

/**
 * Created by DragosTrett on 12.07.2017.
 */

public class UpdateCircle extends AsyncTask<String, Integer, String> {
        Context context;
    public UpdateCircle(Context context){
        this.context=context;
    }

    @Override
    protected String doInBackground(String... params) {
        Connection con=null;
        PreparedStatement ps=null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con= (Connection) DriverManager.getConnection(DBConnection.getUrl(), DBConnection.getUser(), DBConnection.getPassword());
            ps= (PreparedStatement) con.prepareStatement("UPDATE trips SET LongitudineCentru=?, LatitudineCentru=?, circleRange=? WHERE name=?");
            ps.setString(1, String.valueOf(TripInfo.getCircleCenter().longitude));
            ps.setString(2, String.valueOf(TripInfo.getCircleCenter().latitude));
            ps.setString(3, String.valueOf(TripInfo.getCircleRange()));
            ps.setString(4, UserInfo.getTrip());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public void onPostExecute(String rsult){
        MainActivity.start();
        Toast.makeText(context, "Range updated", Toast.LENGTH_SHORT).show();
        TripActivity.tripAc.finishAndRemoveTask();
    }
}
