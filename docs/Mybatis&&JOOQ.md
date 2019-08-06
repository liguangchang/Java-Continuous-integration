# Mybatis&&JOOQ&&JPA

## mybatis

#### 工作原理

```
解析配置
mapperElement()解析mapper映射文件
mapperedStatements保存mapperedstatement
创建sqlSessionFactory
创建sqlSession sqlSession根据stateMent id获取mapperedStatement对象 委托执行器执行操作


```

#### 缓存

```
Mybatis一级缓存的作用域是同一个SqlSession
在同一个sqlSession中两次执行相同的sql语句
第一次执行完毕会将数据库中查询的数据写到缓存（内存）
第二次会从缓存中获取数据将不再从数据库查询，从而提高查询效率。
当一个sqlSession结束后该sqlSession中的一级缓存也就不存在了。
Mybatis默认开启一级缓存。
每次查询会先从缓存区域找，如果找不到从数据库查询，查询到数据将数据写入缓存。
Mybatis内部存储缓存使用一个HashMap，key为hashCode+sqlId+Sql语句。value为从查询出来映射生成的java对象
sqlSession执行insert、update、delete等操作commit提交后会清空缓存区域。


Mybatis二级缓存是多个SqlSession共享的，其作用域是mapper的同一个namespace
不同的sqlSession两次执行相同namespace下的sql语句且向sql中传递参数也相同即最终执行相同的sql语句
第一次执行完毕会将数据库中查询的数据写到缓存（内存）
第二次会从缓存中获取数据将不再从数据库查询，从而提高查询效率。
Mybatis默认没有开启二级缓存需要在setting全局参数中配置开启二级缓存。

相同namespace的mapper查询数据放在同一个区域
如果使用mapper代理方法则每个mapper的namespace都不同，此时可以理解为二级缓存区域是根据mapper划分。
每次查询会先从缓存区域找，如果找不到从数据库查询，查询到数据将数据写入缓存。
Mybatis内部存储缓存使用一个HashMap，key为hashCode+sqlId+Sql语句。value为从查询出来映射生成的java对象
sqlSession执行insert、update、delete等操作commit提交后会清空缓存区域。
```

```
二级缓存要序列化
insert、update、delete 后要刷新 否则会出现脏数据
```



## jooq

```
JOOQ在代码层面要比Mybatis简洁得多，而且性能也非常优异,JOOQ是基于Java访问关系型数据库的工具包，它具有轻量、简单、并且足够灵活的特点，通过JOOQ我们可以轻松的使用Java面向对象的语法来实现各种复杂的SQL。
```

#### springboot集成jooq

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jooq</artifactId>
</dependency>
```

```
<plugin>
<groupId>org.jooq</groupId>
<artifactId>jooq-codegen-maven</artifactId>
<executions>
    <execution>
        <id>default</id>
        <phase>generate-sources</phase>
        <goals>
            <goal>generate</goal>
        </goals>
        <configuration>
            <jdbc>
                <driver>com.mysql.jdbc.Driver</driver>
                <url>jdbc:mysql://127.0.0.1:3306</url>
                <user>root</user>
                <password><![CDATA[123456]]></password>
            </jdbc>
            <generator>
                <name>org.jooq.util.DefaultGenerator</name>
                <generate>
                    <instanceFields>true</instanceFields>
                    <pojos>true</pojos>
                    <daos>true</daos>
                </generate>
                <database>
                    <inputSchema>jets</inputSchema>
                    <name>org.jooq.util.mysql.MySQLDatabase</name>
                    <includes>.*</includes>
                    <excludes>schema_version</excludes>
                </database>
                <target>
                    <packageName>${app.package}</packageName>
                    <directory>target/generated-sources/jooq</directory>
                </target>
            </generator>
        </configuration>
    </execution>
</executions>
</plugin>
```

