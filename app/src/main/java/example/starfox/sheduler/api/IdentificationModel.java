package example.starfox.sheduler.api;

/*
* By http://www.jsonschema2pojo.org/
*/

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IdentificationModel {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("session")
    @Expose
    private String session;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public IdentificationModel withStatus(Boolean status) {
        this.status = status;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public IdentificationModel withMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public IdentificationModel withSession(String session) {
        this.session = session;
        return this;
    }

}


