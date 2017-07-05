## Mybaties 中文文档 
Author: *GGR*     
Time: *2017/7/4*    
QQ: 3095764372
---
核心文件：mybaties-cofig.xml 和 xxxMapper.xml

* **Configuration XML（mybaties-cofig.xml）**
 
  虽然官方并未强制要求必须将文件命名为mybaties-config.xml,但是还是强烈建议将之命名为mybaties-config.xml,
  **Configuration XML**中包括了许多设置mybaties工作的Settings和Properties,我们从mybaties配置文件的DTD文件
  中可以看出整个Configuration包含了这几大元素:
    * properties
    * settings
    * typeAliases
    * objectFactory
    * plugins
    * enviroments
        * environment
          * transactionManager
          * dataSource
    * databasedIdProvider
    * mappers
    
        ```dtd
       <!-- mybaties-cofig.xml的dtd约束-->
        <!ELEMENT configuration (properties?, settings?,
                typeAliases?,typeHandlers?, objectFactory?,
                objectWrapperFactory?,reflectorFactory?, plugins?,
                environments?, databaseIdProvider?, mappers?)>
        ```
---
* **properties**
  > 这是一种一扩展,可插拔(可替换)的配置选项,通过一个Java Properties文件或者通过Properties文件子元素
  标签动态设定属性值。
  
  也就是说,可以把配置信息放到一个Properties文件中再引入,使用的时候只需要通过Properties文件里面的key
  通过 <font color=green size=4>**${key}**</font> 进行占位便可,加载时会进行动态替换。
  即如下方式使用数据项：
   ```xml
     <environments default="development">
           <environment id="development">
               <transactionManager type="JDBC"/>
               <dataSource type="POOLED">
                   <property name="driver" value="${jdbc.driver}"/>
                   <property name="url" value="${jdbc.url}"/>
                   <property name="username" value="${jdbc.username}"/>
                   <property name="password" value="${jdbc.password}"/>
               </dataSource>
           </environment>
   ```
  
  这里提供了3种方式进行配置：
  
  * 直接通过外部文件引入的方式
    ```properties
        jdbc.driver=com.mysql.jdbc.Driver
        ###这里设置了数据库编码，如果是xml中那么&要进行转义(&amp)
        jdbc.url=jdbc:mysql://localhost:3306/ggshiro?useUnicode=true&characterEncoding=utf8
        jdbc.username=root
        jdbc.password=362427gg
    ```
    这种方式的话只需要在mybaties-cofig.xml引入便可
       ```xml
         <properties resource="config/db.properties" />
         <!-- 或者-->
        <!-- <properties url="db.properties的url地址"/>-->
       ```
  * 在mybaties-cofig.xml文件里通过内部子标签设置元素值
      
      ```xml
        <properties resource="config/db.properties">
                <property name="jdbc.username" value="ggr"/>
                <property name="password" value="ggr"/>
            </properties>
      ```
  
  * 在创建SessionFactoryBulider的时候通过builder从外部引入Properties
  文件。
     ```java
     InputStream inputStream = Resources.getResourceAsStream("config/mybatis-config.xml");
     InputStream inputStream2 = Resources.getResourceAsStream("config/db.properties");
     Properties properties = new Properties();
     properties.load(inputStream2);
     SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
     SqlSessionFactory  sessionFactory = builder.build(inputStream,properties);  
    ```    
    
    <font color=green size=5>注意:</FONT>
    通过这3种方式是存在加载顺序的问题的,首先进行加载的
     * 肯定先读取到配置文件内部的子元素标签的值
     * 再读取<font color=green size=4>**\<Properties\>**</FONT>标签的<font color=green size=4>**url**</FONT>或<font color=green size=4>**resources**</FONT>加载Properties实例
     * 最后读取通过参数方式 **SqlFactoryBuilder build(InputStream inputStream, <font color=green size=4>**Properties properties**</FONT>)** 传入的Properties实例的内容
    
    任何一个环节里面如果后一次读取的数据已经存在就会覆盖原来的值。通过测试也的确验证了这一点。
    
----
* **settings(全局参数设置)**
    
    注意:大多数情况下，【这些参数使用它们的默认值即可】
    为满足应用特定的需求，MyBatis默认的全局参数设置可以被覆盖掉，如下所示：
    ```xml
      <settings>
              <!-- 这个配置使全局的映射器启用或禁用缓存 -->
              <setting name="cacheEnabled" value="true" />
      
              <!-- 全局启用或禁用延迟加载。当禁用时，所有关联对象都会即时加载 -->
              <setting name="lazyLoadingEnabled" value="true" />
      
              <!-- 允许或不允许多种结果集从一个单独的语句中返回（需要适合的驱动） -->
              <setting name="multipleResultSetsEnabled" value="true" /> 
      
              <!-- 使用列标签代替列名。不同的驱动在这方便表现不同。参考驱动文档或充分测试两种方法来决定所使用的驱动 -->
              <setting name="useColumnLabel" value="true" /> 
      
              <!-- 允许JDBC支持生成的键。需要适合的驱动。 -->
              <setting name="useGeneratedKeys" value="false" /> 
      
              <!-- 指定MyBatis如何自动映射列到字段/属性。PARTIAL只会自动映射简单、没有嵌套的结果。FULL会自动映射任意复杂的结果（嵌套的或其他情况） -->
              <setting name="autoMappingBehavior" value="PARTIAL" />
              
              <!-- 配置默认的执行器。SIMPLE执行器没有什么特别之处。REUSE执行器重用预处理语句。BATCH执行器重用语句和批量更新 -->
              <setting name="defaultExecutorType" value="SIMPLE" /> 
      
              !-- 设置超时时间，它决定驱动等待一个数据库响应的时间 -->
              <setting name="defaultStatementTimeout" value="25000" /> 
              
              <!-- 允许在嵌套语句中使用分页（RowBounds）默认false -->
              <setting name="safeRowBoundsEnabled" value="false" /> 
      
              <!-- 是否开启自动驼峰命名规则（camel case）映射，即从经典数据库列名 A_COLUMN 到经典 Java 属性名 aColumn 的类似映射。默认false -->
              <setting name="mapUnderscoreToCamelCase" value="false" /> 
      
              <!-- MyBatis 利用本地缓存机制（Local Cache）防止循环引用（circular references）和加速重复嵌套查询。 默认值为 SESSION，这种情况下会缓存一个会话中执行的所有查询。 若设置值为 STATEMENT，本地会话仅用在语句执行上，对相同 SqlSession 的不同调用将不会共享数据。 -->
              <setting name="localCacheScope" value="SESSION" /> 
      
              <!-- 当没有为参数提供特定的 JDBC 类型时，为空值指定 JDBC 类型。 某些驱动需要指定列的 JDBC 类型，多数情况直接用一般类型即可，比如 NULL、VARCHAR 或 OTHER。 -->
              <setting name="jdbcTypeForNull" value="OTHER" />
      
              <!-- 指定对象的哪个方法触发一次延迟加载。 -->
              <setting name="lazyLoadTriggerMethods" value="equals,clone,hashCode ,toString" /> 
      
              <!-- CGLIB | JAVASSIST 默认CGLIB(MyBatis 3.3 or above)  -->
              <!-- 指定 Mybatis 创建具有延迟加载能力的对象所用到的代理工具。 -->
              <setting name="proxyFactory" value="JAVASSIST" /> 
            
              <!-- 当启用时，对任意延迟属性的调用会使带有延迟加载属性的对象完整加载；反之，每种属性将会按需加载。 -->
              <setting name="aggressiveLazyLoading" value="true" /> 
                
              <!-- 指定 MyBatis 所用日志的具体实现，未指定时将自动查找。 -->
              <setting name="logImpl" value="LOG4J " /> 
      
              <!-- 指定 MyBatis 增加到日志名称的前缀。值可以是任意字符串 -->
              <setting name="logPrefix" value="LOG4J " /> 
      
              <!-- 指定当结果集中值为 null 的时候是否调用映射对象的 setter（map 对象时为 put）方法，这对于有 Map.keySet() 依赖或 null 值初始化的时候是有用的。注意基本类型（int、boolean等）是不能设置成 null 的。 默认false-->
              <setting name="callSettersOnNulls" value="false " /> 
        </settings> 
    ```
    
---
* **typeAliases** 
  
    通过给一个类起一个别名，从而通过别名访问到该类模板,如果没有给一个类取别名,那么在需要使用到该类的Mapper XML配置文件中,
    对于*resultType*和*parameterType*属性值，我们需要使用JavaBean 的完全限定名。
    如下：
    ```xml
      <!--按照姓名查找用户-->
         <select id="findUserByName" resultType="user">
             select* from SYSTEM_USER where user_name = #{user_name}
         </select>
          
         <!--更新用户信息-->
         <update id="updateUser" parameterType="User">
            UPDATE SYSTEM_USER set user_name=#{user_name},user_passwd=#{user_passwd},user_status=#{user_status} where user_id = #{user_id}
         </update>
    ``` 
    
    同样Mybaties也提供了3种方式取一个别名
    
    * 在配置文件里面直接配置
     ```xml
      <typeAliases>
         <typeAlias type="com.ggr.pojo.User" alias="User" />
      </typeAliases>
     ```
     
    * 直接通过包扫描
     
     ```xml   
       <typeAliases>
          <typeAlias type="com.ggr.pojo.User" alias="User" />
          <package name="com.ggr.pojo"/>
       </typeAliases>
     ```
     
    *使用注解(使用注解的话必须通过包扫描扫描到该类这样才能扫描到注解)
    ```java
      package com.ggr.pojo;
      import org.apache.ibatis.type.Alias; 
      import java.io.Serializable;

      @Alias("MUSER")
      public class User implements Serializable {
          private String user_id;//用户id
          private String user_name;//用户名
          private String user_passwd;//用户密码
          private String user_status;//用户状态
          // getter/setter
    ```
     
    <font color=green size=5>注意：</FONT> `<typeAlias type="com.ggr.pojo.User" alias="User" />` 和 `<package name="com.ggr.pojo"/>` 可以同时使用,因为Mybaties 的`TypeAliasRegistry`
     内部注册是key就是别名，所以一个类可以注册多个别名,默认情况下,如果使用package的话它会直接将该类的非全限定名小写作为key,当然如果使用了注解的话
     就会取代这个默认取名机制，采用注解里面的别名，但是还是会将其注解值全小写。
     尽管别名极大方便了xml文件的书写，但是强烈建议使用全限定名,这样可以方便我们排错已经查看类信息。
     
     **别名在注册时会进行全小写后再放入注册的map集合中去的。
     别名使用的时候时大小写不敏感,因为`KEY`最后都是会转化为全小写存储的。**
   
     mybaties自己保留的一些默认的别名,取别名要避免这些字段。
     
     Alias| Mapped Type
     -----|-----------
     _byte| byte
     _long |long
     _short| short
     _int |int
     _integer |int
     _double |double
     _float |float
     _boolean| boolean
     string |String
     byte |Byte
     long |Long
     short|Short
     int |Integer
     integer |Integer
     double |Double
     float| Float
     boolean |Boolean
     date| Date
     decimal| BigDecimal
     bigdecimal |BigDecimal
     object |Object
     map| Map
     hashmap |HashMap
     list| List
     arraylist |ArrayList
     collection |Collection
     iterator| Iterator
     
---
* **typeHandlers**
  
   不论是在mybaties的预处理语句中传入`java类型`的参数时我们需要将java类型的数据转换为`jdbc类型`的数据,还是在将从数据库获取的jdbc数据
  转换为java类型数据。我们都离不开typeHandlers,它是mybaties实现`ORM`的关键。
  
  类型处理器 	|Java 类型 	|JDBC 类型
  ----------|-----------|---------
  BooleanTypeHandler| 	java.lang.Boolean, boolean| 	数据库兼容的 BOOLEAN
  ByteTypeHandler |	java.lang.Byte, byte 	|数据库兼容的 NUMERIC 或 BYTE
  ShortTypeHandler |	java.lang.Short, short |	数据库兼容的 NUMERIC 或 SHORT INTEGER
  IntegerTypeHandler |	java.lang.Integer, int |	数据库兼容的 NUMERIC 或 INTEGER
  LongTypeHandler 	|java.lang.Long, long 	|数据库兼容的 NUMERIC 或 LONG INTEGER
  FloatTypeHandler |	java.lang.Float, float| 	数据库兼容的 NUMERIC 或 FLOAT
  DoubleTypeHandler |	java.lang.Double, double |	数据库兼容的 NUMERIC 或 DOUBLE
  BigDecimalTypeHandler 	|java.math.BigDecimal |	数据库兼容的 NUMERIC 或 DECIMAL
  StringTypeHandler |	java.lang.String |	CHAR, VARCHAR
  ClobReaderTypeHandler |	java.io.Reader |	-
  ClobTypeHandler |	java.lang.String 	|CLOB, LONGVARCHAR
  NStringTypeHandler |	java.lang.String |	NVARCHAR, NCHAR
  NClobTypeHandler |	java.lang.String |	NCLOB
  BlobInputStreamTypeHandler 	|java.io.InputStream |	-
  ByteArrayTypeHandler| 	byte[] 	|数据库兼容的字节流类型
  BlobTypeHandler |	byte[] 	BLOB, |LONGVARBINARY
  DateTypeHandler |	java.util.Date |	TIMESTAMP
  DateOnlyTypeHandler |	java.util.Date| 	DATE
  TimeOnlyTypeHandler |	java.util.Date |	TIME
  SqlTimestampTypeHandler| 	java.sql.Timestamp |	TIMESTAMP
  SqlDateTypeHandler |	java.sql.Date |	DATE
  SqlTimeTypeHandler |	java.sql.Time |	TIME
  ObjectTypeHandler |	Any |	OTHER 或未指定类型
  EnumTypeHandler |	Enumeration Type |	VARCHAR-任何兼容的字符串类型，存储枚举的名称（而不是索引）
  EnumOrdinalTypeHandler |	Enumeration Type |	任何兼容的 NUMERIC 或 DOUBLE 类型，存储枚举的索引（而不是名称）。 
  
  mybaties 提供了大量的默认的类型处理器,基本上足够我们使用,但是由于应用的多样性，不可避免的我们需要使用到一些非标准内的数据类型，
  这个时候我们就需要自定义类型处理器了。
  
  自定义类型处理器很简单,mybaties提供了两种方式。
  
  * 通过实现`org.apache.ibatis.type.TypeHandler`接口
    
  
  * 通过继承`org.apache.ibatis.type.BaseTypeHandler`类
    ```java
            public class PhoneNumber{ 
            			private String countryCode; 
            			private String stateCode; 
            			private String number; 
            			public PhoneNumber(){ 
            			} 
            			public PhoneNumber(String countryCode, String stateCode, String number) { 
            				this.countryCode = countryCode; 
            				this.stateCode = stateCode; 
            				this.number = number; 
            			} 
            			public String getAsString() { 
            				return countryCode + "-" + stateCode + "-" + number; 
            			} 
            			// Setters and getters 
            		} 
            		 
            		public class Student{ 
            			private Integer id; 
            			private String name; 
            			private String email; 
            			private PhoneNumber phone; 
            			// Setters and getters 
            		} 
    ```
    
    xml配置:
    ```xml
      <!--按照姓名查找用户-->
        <select id="findUserByName" resultType="user">
            select* from SYSTEM_USER where user_name = #{user_name}
        </select>
    
        <!--更新用户信息-->
        <update id="updateUser" parameterType="User">
           UPDATE SYSTEM_USER set user_name=#{user_name},
           user_passwd=#{user_passwd},user_status=#{user_status},
           user_phone=#{user_phone} where user_id = #{user_id}
        </update>
   	```
   	
   	这里，参数对象中的属性`user_phone`的值需要传递给`#{user_phone}`；而参数对象的属性user_phone是 PhoneNumber类型。但是，MyBatis 并不知道该怎样来处理这个类型的对象。
    为了让MyBatis明白怎样处理这个自定义的Java对象类型，如PhoneNumber，我们可以创建一个自定义的类型处理器,MyBatis提供了抽象类`BaseTypeHandler<T>` ，我们可以继承此类创建自定义类型处理器。
     
    ```java
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
                
    ``` 
    注意:使用ps.setString()和rs.getString()方法是 :因为在数据库的表中user_phone列是VARCHAR类型。
 
	最后一旦我们实现了自定义的类型处理器，我们需要在mybatis-config.xml中注册它：
	```xml
	    <typeHandlers>
           <typeHandler handler="com.ggr.pojo.PhoneNumberTypeHandelr"/>
        </typeHandlers>
    ```
    没有自定义类型处理器的话，的话会报<font color=red>Type handler was null on parameter mapping for property 'user_phone'.</font>的错。
    注册PhoneTypeHandler后, MyBatis就能够将PhoneNumber类型的对象值存储到VARCHAR类型的列上。
    
---
* **environments元素**

    MyBatis 可以配置成适应多种环境，这种机制有助于将 SQL 映射应用于多种数据库之中， 
    现实情况下有多种理由需要这么做。例如，开发(DEV)、测试(TEST)和生产环境(PRODUCT)
    需要有不同的配置,或者共享相同 Schema 的多个生产数据库， 想使用相同的 SQL 映射。
    许多类似的用例。
    
    不过不要忘记，尽管可以配置多个环境，但是每个`SqlSessionFactory`实例的话只能使用其中的
    一个，所有如果你想连接多少个数据库，就需要多少个`SqlSessionFactory`实例。
    
    为了只指定哪一种环境只需要将它作为参数传发给SqlSessionFactoryBuilder便可。可以接受环境参数的方法是：
    ```java
      SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(reader, environment);
      SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(reader, environment,properties);
    ```
    如果忽略环境参数，就会默认加载默认的环境。
    ```xml
      <environments default="shoppingcart"> 
      		  <environment id="shoppingcart"> 
      			<transactionManager type="MANAGED" /> 
      			<dataSource type="JNDI"> 
      			  <property name="data_source" value="java:comp/jdbc/ShoppingcartDS" /> 
      			</dataSource> 
      		  </environment> 
      		  <environment id="reports"> 
      			<transaction Managertype="MANAGED" /> 
      			<dataSource type="JNDI"> 
      			  <property name="data_source" value="java:comp/jdbc/ReportsDS" /> 
      			</dataSource> 
      		  </environment> 
      		</environments> 
    ```
    
 * **transactionManager**
 
    MyBatis支持两种类型的事务管理器：`JDBC` 和`MANAGED`. 
    JDBC事务管理器的使用，是在【jdbc程序】负责管理数据库连接的生命周期（提交、回退等等）的时候。
    如果将`TransactionManager` 属性设置成`JDBC`，MyBatis内部将使用`JdbcTransactionFactory`类创建`TransactionManager`。
    
    例如，部署到ApacheTomcat的应用程序，需要应用程序自己管理事务。
    `MANAGED` 事务管理器的使用，是在【应用服务器】负责管理数据库连接生命周期的时候。如果将`TransactionManager`属性设置成`MANAGED`时，
    MyBatis内部使用`ManagedTransactionFactory` 类创建事务管理器`TransactionManager`。例如，当一个Java EE的应用程序部署在类似`JBoss，
    WebLogic，GlassFish`应用服务器上时，它们会使用EJB进行应用服务器的事务管理能力。在这些管理环境中，你可以使用MANAGED事务管理器。
    注：`Managed` 是托管的意思，即我们编写的应用程序本身不去管理事务，而是把事务管理交给应用所在的服务器进行管理。
      
    
    
 * **dataSource**
    
    `dataSource`的类型`type`属性可以配置成其内置类型之一，如`UNPOOLED，POOLED，JNDI`。
    如果将类型设置成`UNPOOLED`，MyBatis会为每一个数据库操作创建一个新的连接，使用完了并关闭它，该方式适用于只有小规模数量并发用户的简单应用程序上。
             
    如果将属性设置成`POOLED`，MyBatis会创建一个数据库连接池，连接池中的一个连接将会被用作数据库操作。一旦数据库操作完成，MyBatis会将此连接返回给连接池.
    在开发或测试环境中，经常使用此种方式。
             
    如果将类型设置成JNDI(Java Naming and Directory Interface , Java命名和目录接口, 是SUN公司提供的一种标准的Java命名系统接口)，
    MyBatis从在应用服务器向配置好的JNDI数据源dataSource获取数据库连接。在生产环境中，优先考虑这种方式。
    	 

---
* **databaseIdProvider**
  
  MyBatis 可以根据不同的数据库厂商执行不同的语句，这种多厂商的支持是基于映射语句中的 `databaseId` 属性。
  MyBatis 会加载不带 databaseId 属性和带有匹配当前数据库 databaseId 属性的所有语句。 
  如果同时找到带有 databaseId 和不带 databaseId 的相同语句，则后者会被舍弃。
  为支持多厂商特性只要像下面这样在 mybatis-config.xml 文件中加入 `databaseIdProvider` 即可：
  `<databaseIdProvider type="DB_VENDOR" />`
  这里的 `DB_VENDOR` 会通过 `DatabaseMetaData#getDatabaseProductName()` 返回的字符串进行设置。
  由于通常情况下这个字符串都非常长而且相同产品的不同版本会返回不同的值，所以最好通过设置属性别名来使其变短，如下：
   ```xml
      <databaseIdProvider type="DB_VENDOR">
        <property name="SQL Server" value="sqlserver"/>
        <property name="DB2" value="db2"/>        
        <property name="Oracle" value="oracle" />
      </databaseIdProvider>
   ``` 

  在有 properties 时，`DB_VENDOR databaseIdProvider` 的将被设置为第一个能匹配数据库产品名称的属性键对应的值，如果没有匹配的属性将会设置为 "null"。 
  在这个例子中，如果 `getDatabaseProductName()` 返回“Oracle (DataDirect)”，`databaseId` 将被设置为"oracle"。  
  你可以通过实现接口 `org.apache.ibatis.mapping.DatabaseIdProvider` 并在 mybatis-config.xml 中注册来构建自己的 DatabaseIdProvider：
   ```java
    public interface DatabaseIdProvider {
        void setProperties(Properties p);
        String getDatabaseId(DataSource dataSource) throws SQLException;
      }

   ``` 
---  
* **映射器（mappers）**

    既然 MyBatis 的行为已经由上述元素配置完了，我们现在就要定义 SQL 映射语句了。但是首先我们需要告诉 MyBatis 到哪里去找到这些语句。
    Java 在自动查找这方面没有提供一个很好的方法，所以最佳的方式是告诉 MyBatis 到哪里去找映射文件。你可以使用相对于类路径的资源引用， 
    或完全限定资源定位符（包括 file:/// 的 URL），或类名和包名等。例如：    
 
   ```xml
     <!-- Using classpath relative resources -->
       <mappers>
           <mapper resource="org/mybatis/builder/AuthorMapper.xml"/>
           <mapper resource="org/mybatis/builder/BlogMapper.xml"/>
           <mapper resource="org/mybatis/builder/PostMapper.xml"/>
         </mappers>
   ```
   ```xml
    <!-- Using url fully qualified paths -->
       <mappers>
         <mapper url="file:///var/mappers/AuthorMapper.xml"/>
         <mapper url="file:///var/mappers/BlogMapper.xml"/>
         <mapper url="file:///var/mappers/PostMapper.xml"/>
       </mappers> 
   ```
   ```xml
     <!-- Using mapper interface classes -->
       <mappers>
         <mapper class="org.mybatis.builder.AuthorMapper"/>
         <mapper class="org.mybatis.builder.BlogMapper"/>
         <mapper class="org.mybatis.builder.PostMapper"/>
       </mappers>
   ```
   ```xml
    <!-- Register all interfaces in a package as mappers -->
       <mappers>
         <package name="org.mybatis.builder"/>
       </mappers>
   ```
   以上每一个<mapper> 标签都可以从不同类型的资源中加载映射mapper：
   resource属性用来指定在classpath中的mapper文件。
   url属性用来通过完全文件系统路径或者web URL地址来指向mapper文件
   强烈建议使用`<mapper resource="org/mybatis/builder/AuthorMapper.xml"/>`,class方式的话就是已经有实现的类才有用。
   
   
   
    
  
  