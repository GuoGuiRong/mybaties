package com.ggr.dao;

import com.ggr.pojo.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by GuiRunning on 2017/7/4.
 */
public interface UserMapper {
    @Select("select user_id,user_name,user_passwd,user_status from system_user")
    public List<User> findAllUser();

    public User findUserByName(String name);

    public void updateUser(User user);
}
