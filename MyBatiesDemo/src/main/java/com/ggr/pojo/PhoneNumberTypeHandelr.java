package com.ggr.pojo;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by GuiRunning on 2017/7/5.
 */
public class PhoneNumberTypeHandelr extends BaseTypeHandler<PhoneNumber> {


    //遇到PhoneNumber参数的时候应该如何在ps中设置值
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, PhoneNumber phoneNumber, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i,phoneNumber.getAsString());
    }

    //查询中遇到PhoneNumber类型的应该如何封装(使用列名封装)
    public PhoneNumber getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return new PhoneNumber(resultSet.getString(s));
    }

    //查询中遇到PhoneNumber类型的应该如何封装(使用列下标封装)
    public PhoneNumber getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return new PhoneNumber(resultSet.getString(i));
    }
    //CallableStatement使用中遇到了PhoneNumber类型的应该如何封装
    public PhoneNumber getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return new PhoneNumber(callableStatement.getString(i));
    }
}
