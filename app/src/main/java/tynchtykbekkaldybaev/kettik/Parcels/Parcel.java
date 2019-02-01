package tynchtykbekkaldybaev.kettik.Parcels;

public class Parcel {
    String from;
    String to;
    String date;

    Parcel () {
        from = to = date = "NaN";
    }

    Parcel (String f, String t, String d) {
        from = f;
        to = t;
        date = d;
    }
}
