package tynchtykbekkaldybaev.kettik.MyTrips;

/**
 * Created by tynchtykbekkaldybaev on 03/03/2019.
 */

public class MyTrip_Info {
    public int Id;
    public String name;
    public String surname;
    public String birthDate;
    public String gender;
    public String price;
    public String vehicleModel;
    public String vehicleNumber;
    public String phoneNumber;
    public boolean parcelFlag;

    public MyTrip_Info(String nm, String snm, String bD, String gen, String pr, String vM, String vN, String pN, int id, boolean pf) {
        name = nm;
        surname = snm;
        birthDate = bD;
        gender = gen;
        price = pr;
        vehicleModel = vM;
        vehicleNumber = vN;
        phoneNumber = pN;
        Id = id;
        parcelFlag = pf;
    }
}
