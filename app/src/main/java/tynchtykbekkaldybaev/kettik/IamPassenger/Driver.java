package tynchtykbekkaldybaev.kettik.IamPassenger;

/**
 * Created by tynchtykbekkaldybaev on 20/01/2019.
 */

public class Driver {
    int tripId;
    String from;
    String to;
    String date;
    String time;
    int free, maxspace;


    Driver () {
        from = to = date = "NaN";
        free = maxspace = 0;
    }

    public Driver(int id, String f, String t, String d, int fr, String tm) {
        tripId = id;
        from = f;
        to = t;
        date = d;
        free = fr;
        time = tm;
    }
}