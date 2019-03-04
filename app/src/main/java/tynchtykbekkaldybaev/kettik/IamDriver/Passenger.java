package tynchtykbekkaldybaev.kettik.IamDriver;

public class Passenger {
    int requestId;
    String from;
    String to;
    String date;
    String time;
    int passengers;

    Passenger () {
        from = to = date = "NaN";
        passengers = 0;
    }

    Passenger (int id, String f, String t, String d, String tm, int p) {
        requestId = id;
        from = f;
        to = t;
        date = d;
        time = tm;
        passengers = p;
    }
}
