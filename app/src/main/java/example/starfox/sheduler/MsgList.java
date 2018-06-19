
package example.starfox.sheduler;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MsgList {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("stamp")
    @Expose
    private String stamp;
    @SerializedName("dst_type")
    @Expose
    private String dstType;
    @SerializedName("dst_id")
    @Expose
    private String dstId;
    @SerializedName("msg")
    @Expose
    private String msg;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStamp() {
        return stamp;
    }

    public void setStamp(String stamp) {
        this.stamp = stamp;
    }

    public String getDstType() {
        return dstType;
    }

    public void setDstType(String dstType) {
        this.dstType = dstType;
    }

    public String getDstId() {
        return dstId;
    }

    public void setDstId(String dstId) {
        this.dstId = dstId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
