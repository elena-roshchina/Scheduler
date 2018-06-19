
package example.starfox.sheduler;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessagesModel {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("number")
    @Expose
    private Integer number;
    @SerializedName("msg_list")
    @Expose
    private List<MsgList> msgList = null;

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

    public List<MsgList> getMsgList() {
        return msgList;
    }

    public void setMsgList(List<MsgList> msgList) {
        this.msgList = msgList;
    }

}
