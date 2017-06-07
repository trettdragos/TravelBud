package com.example.dragostrett.tripbud;

/**
 * Created by DragosTrett on 24.05.2017.
 */

public class TripInfo {
    //class for trip information easy acces
    public static String idTrip;
    public static String nameTrip="";
    public static String place;
    public static String organizator;
    public static String meet="";

    @Override
    public String toString() {
        return "TripInfo{}";
    }

    public static boolean inATrip;

    public static boolean isInATrip() {
        return inATrip;
    }

    public static void setInATrip(boolean inATrip) {
        TripInfo.inATrip = inATrip;
    }

    public static String getNumber_users() {
        return number_users;
    }

    public static void setNumber_users(String number_users) {
        TripInfo.number_users = number_users;
    }

    public static String getIdTrip() {
        return idTrip;
    }

    public static void setIdTrip(String idTrip) {
        TripInfo.idTrip = idTrip;
    }

    public static String getNameTrip() {
        return nameTrip;
    }

    public static void setNameTrip(String nameTrip) {
        TripInfo.nameTrip = nameTrip;
    }

    public static String getPlace() {
        return place;
    }

    public static void setPlace(String place) {
        TripInfo.place = place;
    }

    public static String getOrganizator() {
        return organizator;
    }

    public static void setOrganizator(String organizator) {
        TripInfo.organizator = organizator;
    }

    public static String getMeet() {
        return meet;
    }

    public static void setMeet(String meet) {
        TripInfo.meet = meet;
    }

    public static String number_users;
}
