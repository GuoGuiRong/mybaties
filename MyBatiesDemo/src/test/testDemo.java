package com.ggr;

import com.ggr.dao.UserMapper;
import com.ggr.pojo.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * Created by GuiRunning on 2017/7/4.
 */
public class testDemo {

    @Test
    public void test() throws IOException {
        InputStream inputStream = Resources.getResourceAsStream("config/mybatis-config.xml");

        InputStream inputStream2 = Resources.getResourceAsStream("config/db.properties");
        Properties properties = new Properties();
        properties.load(inputStream2);
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        SqlSessionFactory  sessionFactory = builder.build(inputStream);

        /* SqlSessionFactory  sessionFactory = builder.build(inputStream,properties);*/

        SqlSession sqlSession = sessionFactory.openSession(true);
        UserMapper userMapper = sqlSession.getMapper(com.ggr.dao.UserMapper.class);
        List<User> list = userMapper.findAllUser();
        System.out.println(list.toArray());
        User user = userMapper.findUserByName("ggr");
        System.out.println(user);
        userMapper.updateUser(new User());
    }
}
