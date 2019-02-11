package tynchtykbekkaldybaev.kettik.IamDriver;

public class Passenger {
    String from;
    String to;
    String date;
    int passengers;

    Passenger () {
        from = to = date = "NaN";
        passengers = 0;
    }

    Passenger (String f, String t, String d, int p) {
        from = f;
        to = t;
        date = d;
        passengers = p;
    }
}
