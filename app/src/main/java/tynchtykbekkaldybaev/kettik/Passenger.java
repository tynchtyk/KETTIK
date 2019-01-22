package tynchtykbekkaldybaev.kettik;

public class Passenger {
    String from;
    String to;
    String date;

    Passenger () {
        from = to = date = "NaN";
    }

    Passenger (String f, String t, String d) {
        from = f;
        to = t;
        date = d;
    }
}
