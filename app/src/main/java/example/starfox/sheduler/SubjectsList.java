
package example.starfox.sheduler;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubjectsList {

    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("staff")
    @Expose
    private String staff;
    @SerializedName("day")
    @Expose
    private String day;
    @SerializedName("mark")
    @Expose
    private String mark;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStaff() {
        return staff;
    }

    public void setStaff(String staff) {
        this.staff = staff;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

}
