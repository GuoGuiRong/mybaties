# mybaties 一些关于学习和使用mybaties信息

- mybaties中模糊查询的几种写法
 
  - 使用sql的字符串拼接函数
  ```sql
    select id,name from tableName where name like CONCAT('%',CONCAT(#{like},'%'));
  ```
  - 使用$代替#
  ```sql
      select id,name from tableName where name like '%${like}%';
  ```
  
  - Java里面拼接
  ```text
    String like = "%" + text + "%"; 
  ```
  有的时候我们希望匹配时不分大小写，这个时候就有UPPER,LOWER
  ```sql
      //全转换为大写匹配
      select id,name from tableName where UPPER(name) like UPPER(CONCAT('%',CONCAT(#{like},'%')));
  ```
  或者
  ```sql
        //全转换为小写匹配
        select id,name from tableName where LOWER (name) like LOWER(CONCAT('%',CONCAT(#{like},'%')));
    ```

-  mybaties中#和$的区别

  1 #是将传入的值当做字符串的形式，
  ```sql
    select id,name,age from student where id =#{id}
    //当前端把id值1,传入到后台的时候,就相当于 
    select id,name,age from student where id ='1'
  ```
    
  2 $是将传入的数据直接显示生成sql语句
  ```sql
      select id,name,age from student where id =#{id}
      //当前端把id值1,传入到后台的时候,就相当于 
      select id,name,age from student where id =1
    ```
    
  3 使用#可以很大程度上防止sql注入。(语句的拼接)
 
  4 但是如果使用在order by 中就需要使用 $.
 
  5 在大多数情况下还是经常使用#，但在不同情况下必须使用$. 
 
  我觉得#与的区别最大在于：#{} 传入值时，sql解析时，参数是带引号的，而${}传入值时，sql解析时，参水是不带引号的。
 
 一, 理解mybatis中 $与#
    在mybatis中的$与#都是在sql中动态的传入参数。

    select id,name,age from student where name=#{name}  //这个name是动态的，可变的。当你传入什么样的值，就会根据你传入的值执行sql语句。

 二:使用$与#
 
    #{}: 解析为一个 JDBC 预编译语句（prepared statement）的参数标记符，一个 #{ } 被解析为一个参数占位符 。
 
    ${}: 仅仅为一个纯碎的 string 替换，在动态 SQL 解析阶段将会进行变量替换。
 
    //name-->cy
    select id,name,age from student where name=#{name}   -- name='cy'
 
    select id,name,age from student where name=${name}    -- name=cy