package custom.sdk.com.myfacebook.model;

/**
 * Created by Shiva on 08-06-2017.
 */

public class EmploymentDetails {

    String name;
    String location;
    String designation;
    String start_date;
    String end_date;

    public void setEmployerName(String mName) {
name = mName;
    }

    public void setEmployerLocation(String loc) {
location = loc;
    }

    public void setDesignation(String position) {
designation = position;
    }

    public void setStartDate(String date) {
start_date = date;
    }

    public void setEndDate(String date) {
end_date = date;
    }
}
