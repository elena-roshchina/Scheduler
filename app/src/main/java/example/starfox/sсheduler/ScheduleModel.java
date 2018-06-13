package example.starfox.sсheduler;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ScheduleModel {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("group")
    @Expose
    private String group;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("staff")
    @Expose
    private String staff;
    @SerializedName("subj")
    @Expose
    private String subj;
    @SerializedName("short_subj")
    @Expose
    private String shortSubj;
    @SerializedName("room")
    @Expose
    private String room;
    @SerializedName("kind")
    @Expose
    private String kind;
    @SerializedName("wday")
    @Expose
    private String wday;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStaff() {
        return staff;
    }

    public void setStaff(String staff) {
        this.staff = staff;
    }

    public String getSubj() {
        return subj;
    }

    public void setSubj(String subj) {
        this.subj = subj;
    }

    public String getShortSubj() {
        return shortSubj;
    }

    public void setShortSubj(String shortSubj) {
        this.shortSubj = shortSubj;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getWday() {
        return wday;
    }

    public void setWday(String wday) {
        this.wday = wday;
    }

}

