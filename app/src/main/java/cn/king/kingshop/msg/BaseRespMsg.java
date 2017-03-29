package cn.king.kingshop.msg;

import java.io.Serializable;

/**
 * Created by king on 2017/3/25.
 */

public class BaseRespMsg implements Serializable {

    public static final int STATUS_SUCCESS = 1;
    public static final int STATUS_EEROR = 0;
    public static final String MSG_SUCCESS = "success";

    protected int status = STATUS_SUCCESS;
    protected String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
