package com.example.dragostrett.tripbud.Background;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.dragostrett.tripbud.TripActivity;
import com.example.dragostrett.tripbud.BasicInfo.TripInfo;
import com.mysql.jdbc.PreparedStatement;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by DragosTrett on 25.05.2017.
 */

public class CreateTrip extends AsyncTask<String, Integer, String> {
    Context context;
    public CreateTrip(Context context){this.context=context;}

    @Override
    protected String doInBackground(String... params) {
        Connection con=null;
        PreparedStatement ps=null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con= (Connection) DriverManager.getConnection(DBConnection.getUrl(), DBConnection.getUser(), DBConnection.getPassword());
            ps= (PreparedStatement) con.prepareStatement("INSERT INTO trips (name, place, organizator, meet, number_users, circleRange, LatitudineCentru, LongitudineCentru, startDate, endDate) VALUES (?,?,?, '', '1', '0', '0', '0', ?, ?)");
            ps.setString(1, TripInfo.getNameTrip());
            ps.setString(2, TripInfo.getPlace());
            ps.setString(3, TripInfo.getOrganizator());
            ps.setString(4, TripInfo.getStartDate().toString());
            ps.setString(5, TripInfo.getEndDate().toString());
            ps.executeUpdate();
            TripInfo.setMeet("");
            ps= (PreparedStatement) con.prepareStatement("UPDATE table1 SET trip=? WHERE username=?");
            ps.setString(1, TripInfo.getNameTrip());
            ps.setString(2, TripInfo.getOrganizator());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(String result){
            Toast.makeText(context, "Trip Created",
                    Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, TripActivity.class);
        context.startActivity(intent);
    }
}
