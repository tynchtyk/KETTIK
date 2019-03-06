package tynchtykbekkaldybaev.kettik.Parcels;

/**
 * Created by tynchtykbekkaldybaev on 03/03/2019.
 */

public class Parcel_Info {
    int Id;
    String name;
    String surname;
    String birthDate;
    String gender;
    String phoneNumber;

    Parcel_Info() {

    }

    public Parcel_Info(String nm, String snm, String bD, String gen, String pN, int id) {
        name = nm;
        surname = snm;
        birthDate = bD;
        gender = gen;
        phoneNumber = pN;
        Id = id;
    }
}
