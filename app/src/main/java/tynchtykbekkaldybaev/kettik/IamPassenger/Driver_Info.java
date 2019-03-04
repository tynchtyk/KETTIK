package tynchtykbekkaldybaev.kettik.IamPassenger;

/**
 * Created by tynchtykbekkaldybaev on 20/01/2019.
 */

public class Driver_Info {
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
    Driver_Info() {

    }

    public Driver_Info(String nm, String snm, String bD, String gen, String pr, String vM, String vN, String pN, int id, boolean parcel) {
        name = nm;
        surname = snm;
        birthDate = bD;
        gender = gen;
        price = pr;
        vehicleModel = vM;
        vehicleNumber = vN;
        phoneNumber = pN;
        Id = id;
        parcelFlag = parcel;
    }
}