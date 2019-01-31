package tynchtykbekkaldybaev.kettik;

/**
 * Created by tynchtykbekkaldybaev on 20/01/2019.
 */

public class Driver_Info {
    int Id;
    String name;
    String surname;
    String birthDate;
    String gender;
    String price;
    String vehicleModel;
    String vehicleNumber;
    String phoneNumber;

    Driver_Info() {

    }

    Driver_Info(String nm, String snm, String bD, String gen, String pr, String vM, String vN, String pN, int id) {
        name = nm;
        surname = snm;
        birthDate = bD;
        gender = gen;
        price = pr;
        vehicleModel = vM;
        vehicleNumber = vN;
        phoneNumber = pN;
        Id = id;
    }
}