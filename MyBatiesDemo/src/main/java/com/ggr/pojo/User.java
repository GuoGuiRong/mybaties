package com.ggr.pojo;

import org.apache.ibatis.type.Alias;

import java.io.Serializable;

/**
 * Created by GuiRunning on 2017/7/4.
 */

@Alias("MUSER")
public class User implements Serializable {

    private String user_id;//用户id
    private String user_name;//用户名
    private String user_passwd;//用户密码
    private String user_status;//用户状态
    private PhoneNumber user_phone;//用户联系电话

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }

    public String getUser_status() {
        return user_status;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setUser_passwd(String user_passwd) {
        this.user_passwd = user_passwd;
    }

    public String getUser_passwd() {
        return user_passwd;
    }

    public void setUser_phone(PhoneNumber user_phone) {
        this.user_phone = user_phone;
    }

    public PhoneNumber getUser_phone() {
        return user_phone;
    }

    @Override
    public String toString() {
        return user_id+","+user_name+","+user_passwd+","+user_status+","+user_phone.toString();
    }
}
