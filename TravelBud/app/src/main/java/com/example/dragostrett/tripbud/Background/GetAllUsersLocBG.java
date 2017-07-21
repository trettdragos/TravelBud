package com.example.dragostrett.tripbud.Background;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;

import com.example.dragostrett.tripbud.BasicInfo.DistanceCalculator;
import com.example.dragostrett.tripbud.BasicInfo.MeetInfo;
import com.example.dragostrett.tripbud.BasicInfo.TripInfo;
import com.example.dragostrett.tripbud.BasicInfo.UserInfo;
import com.example.dragostrett.tripbud.LogInActivity;
import com.example.dragostrett.tripbud.MainActivity;
import com.example.dragostrett.tripbud.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mysql.jdbc.PreparedStatement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.LinkedList;

/**
 * Created by DragosTrett on 27.05.2017.
 */

public class GetAllUsersLocBG extends AsyncTask<String, Integer, String> {
    Context context;
    GoogleMap map;
    LinkedList<LatLng> a = new LinkedList<>();
    LinkedList<String> name = new LinkedList<>();
    LinkedList<Boolean> visibility = new LinkedList<>();
    public GetAllUsersLocBG(Context context, GoogleMap show){
        this.context=context;this.map = show;
    }
    @Override
    protected String doInBackground(String... params) {
        Connection con=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con= (Connection) DriverManager.getConnection(DBConnection.getUrl(), DBConnection.getUser(), DBConnection.getPassword());
            ps= (PreparedStatement) con.prepareStatement("SELECT * FROM table1 WHERE trip=?");
            ps.setString(1, UserInfo.getTrip());
            rs=ps.executeQuery();
            rs.next();
            do{
                LatLng n= new LatLng(Double.parseDouble(rs.getString("latitudine")), Double.parseDouble(rs.getString("longitudine")));
                a.push(n);
                name.push(rs.getString("username"));
                String aux=rs.getString("visible");
                if(aux.equals("1"))
                    visibility.push(true);
                else visibility.push(false);
            }while (rs.next());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result){
        MainActivity.mMap.clear();
        if(!TripInfo.getCircleRange().equals(0)){
            MainActivity.circle = MainActivity.mMap.addCircle(new CircleOptions()
                    .center(TripInfo.getCircleCenter())
                    .radius(TripInfo.getCircleRange())
                    .strokeColor(Color.RED)
                    .fillColor(0x00000000));
        }
        if (!TripInfo.getMeet().equals("")) {
            MainActivity.meet = MainActivity.mMap.addMarker(new MarkerOptions().position(MeetInfo.getPosition()).title(TripInfo.getMeet()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        }
        LatLng sydney = UserInfo.getUserLoc();
        MainActivity.user = MainActivity.mMap.addMarker(new MarkerOptions().position(sydney).title(UserInfo.getUsername()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        boolean everyoneInTheCircle=true;
        for(int j=0; j<a.size(); j++){
            if(!name.get(j).equals(UserInfo.getUsername()) && !name.get(j).equals("") && (visibility.get(j)|| UserInfo.getType().equals("1")))
            MainActivity.mMap.addMarker(new MarkerOptions().position(a.get(j)).title(name.get(j)));
            if(UserInfo.getType().equals("1")){
                if(!TripInfo.getCircleRange().equals(0)){
                    if(DistanceCalculator.CalculationByDistance(TripInfo.getCircleCenter(), a.get(j))>TripInfo.getCircleRange()/1000 && !name.get(j).equals(UserInfo.getUsername())){
                        everyoneInTheCircle=false;
                        //Log.e("notif", String.valueOf(TripInfo.getCircleRange())+ " " + String.valueOf(DistanceCalculator.CalculationByDistance(TripInfo.getCircleCenter(), a.get(j))));
                        NotificationCompat.Builder mBuilder =
                                new NotificationCompat.Builder(context)
                                        .setSmallIcon(R.drawable.x)
                                        .setContentTitle("User out of permited range")
                                        .setContentText(name.get(j)+ " is out of range")
                                        .setOngoing(false)
                                        .setAutoCancel(true)
                                        .setPriority(Notification.PRIORITY_MIN)
                                        .setDefaults(Notification.DEFAULT_ALL);

                        NotificationManager mNotifyMgr =
                                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
                        mNotifyMgr.notify(1, mBuilder.build());
                    }
                }
            }
        }if(everyoneInTheCircle) LogInActivity.cancelNotification();
    }
}
