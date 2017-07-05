package com.ggr;

import com.ggr.dao.UserMapper;
import com.ggr.pojo.PhoneNumber;
import com.ggr.pojo.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * Created by GuiRunning on 2017/7/4.
 */
public class testDemo {

    public static void main(String[] args) throws IOException {
        InputStream inputStream = Resources.getResourceAsStream("config/mybatis-config.xml");

        InputStream inputStream2 = Resources.getResourceAsStream("config/db.properties");
        Properties properties = new Properties();
        properties.load(inputStream2);
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        SqlSessionFactory  sessionFactory = builder.build(inputStream);

        /* SqlSessionFactory  sessionFactory = builder.build(inputStream,properties);*/

        SqlSession sqlSession = sessionFactory.openSession(true);
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        List<User> list = userMapper.findAllUser();
        System.out.println(list.toArray());
        User user = userMapper.findUserByName("2");
        System.out.println(user.toString());
        User user2 = new User();
        user2.setUser_id("1");
        user2.setUser_name("2");
        user2.setUser_status("2");
        user2.setUser_passwd("ggr");
        user2.setUser_phone(new PhoneNumber("086-12595-10086"));
        userMapper.updateUser(user2);
    }
}
