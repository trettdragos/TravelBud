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
 * get all others users info and places everything on the map
 */

public class GetAllUsersLocBG extends AsyncTask<String, Integer, String> {
    Context context;
    GoogleMap map;
    LinkedList<LatLng> a = new LinkedList<>();//list for all users loc
    LinkedList<String> name = new LinkedList<>();//list for names
    LinkedList<Boolean> visibility = new LinkedList<>();//list to check if user should be shown on the map
    public GetAllUsersLocBG(Context context, GoogleMap show){
        this.context=context;this.map = show;//get context
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
        MainActivity.mMap.clear();//clear map
        if(!TripInfo.getCircleRange().equals(0)){//add range circle
            MainActivity.circle = MainActivity.mMap.addCircle(new CircleOptions()
                    .center(TripInfo.getCircleCenter())
                    .radius(TripInfo.getCircleRange())
                    .strokeColor(Color.RED)
                    .fillColor(0x00000000));
        }
        if (!TripInfo.getMeet().equals("")) {//add meeting point
            MainActivity.meet = MainActivity.mMap.addMarker(new MarkerOptions().position(MeetInfo.getPosition()).title(TripInfo.getMeet()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        }
        //add user
        MainActivity.user = MainActivity.mMap.addMarker(new MarkerOptions().position(UserInfo.getUserLoc()).title(UserInfo.getUsername()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        boolean everyoneInTheCircle=true;
        for(int j=0; j<a.size(); j++){//put all users on the map an if the user is admi, notifi for users out of the range
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
        }if(everyoneInTheCircle) LogInActivity.cancelNotification();//cancel ntif once everyne is back in the circle
    }
}
