package tynchtykbekkaldybaev.kettik.MyTrips;

/**
 * Created by tynchtykbekkaldybaev on 03/03/2019.
 */

public class MyTrip {
    int tripId;
    String from;
    String to;
    String date;
    String  time;
    int free;

    public MyTrip(int id, String f, String t, String d, int fre, String tm) {
        tripId = id;
        from = f;
        to = t;
        date = d;
        free = fre;
        time = tm;
    }
}
