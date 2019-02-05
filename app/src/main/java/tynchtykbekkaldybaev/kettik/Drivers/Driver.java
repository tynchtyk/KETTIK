package tynchtykbekkaldybaev.kettik.Drivers;

/**
 * Created by tynchtykbekkaldybaev on 20/01/2019.
 */

public class Driver {
    String from;
    String to;
    String date;
    int free, maxspace;


    Driver () {
        from = to = date = "NaN";
        free = maxspace = 0;
    }

    public Driver(String f, String t, String d, int fr, int mx) {
        from = f;
        to = t;
        date = d;
        free = fr;
        maxspace = mx;
    }
}