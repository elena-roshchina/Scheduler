package example.starfox.sheduler;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class LastUpdateIDModel {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("message_id")
    @Expose
    private String messageId;
    @SerializedName("commit_id")
    @Expose
    private String commitId;

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

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getCommitId() {
        return commitId;
    }

    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

}
