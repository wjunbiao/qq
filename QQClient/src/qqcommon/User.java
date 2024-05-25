package qqcommon;

import java.io.Serializable;

/**
 * @author 王俊彪
 * @version 1.0
 * 表示一个用户/客户信息
 */
public class User implements Serializable {
    private String userId;//用户名id/用户名
    private String password;//用户密码

    public User() {


    }

    private static final long serialVersionUID = 1L;//增强兼容性
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }
}
