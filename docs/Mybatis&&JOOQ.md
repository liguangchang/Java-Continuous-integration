# Mybatis&&JOOQ&&JPA

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

