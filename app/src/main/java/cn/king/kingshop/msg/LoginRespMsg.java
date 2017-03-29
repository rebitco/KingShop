package cn.king.kingshop.msg;

import cn.king.kingshop.bean.User;

/**
 * Created by king on 2017/3/25.
 */

public class LoginRespMsg extends BaseRespMsg {

    private String token;
    private User user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
