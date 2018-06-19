
package example.starfox.sheduler;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MarksModel {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("number")
    @Expose
    private Integer number;
    @SerializedName("subjects_list")
    @Expose
    private List<SubjectsList> subjectsList = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public List<SubjectsList> getSubjectsList() {
        return subjectsList;
    }

    public void setSubjectsList(List<SubjectsList> subjectsList) {
        this.subjectsList = subjectsList;
    }

}
