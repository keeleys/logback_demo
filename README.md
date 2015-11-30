## logback 分文件分级别的的测试项目



* 根据`日志级别`区分
* 根据日志名称**`name`**区分
* 根据`日志内容`区分

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jun on 2015/11/30.
 */
public class ServiceCtrl {
    private Logger log = LoggerFactory.getLogger(ServiceCtrl.class);

    private Logger syslog = LoggerFactory.getLogger("syslog");

    public void login(){
        String userName = "admin";
        String password = "123";

        //日志级别
        log.info("service方法 进入登陆方法");
        //日志内容
        log.info("client方法 登陆账户{} ,登陆密码{}",userName,password);
        log.error("service方法 账户{}登陆失败",userName);

        //根据日志名
        syslog.info("我是syslog 我只记录在syslog.txt里面");

    }
}

```

xml配置
```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration debug="false">
    <contextName>weixin-web</contextName>
    <!--定义日志文件的存储地址 勿在 LogBack 的配置中使用相对路径-->
    <property name="LOG_HOME" value="C:\\home\\log\\test_logback" />
    <!--格式化输出：%d表示日期，%thread表示线程名，%-5level：级别从左显示5个字符宽度%msg：日志消息，%n是换行符-->
    <property name="print_pattern" value="%date [%thread] %-5level %logger{50} - %msg%n" />
    <!--<property file="src\main\resources\app.properties" /> -->
    <!-- 控制台输出 -->
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <pattern>${print_pattern}</pattern>
        </encoder>
    </appender>



    <appender name="INFO_FILE"  class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!--
          返回DENY，日志将立即被抛弃不再经过其他过滤器；
          返回NEUTRAL，有序列表里的下个过滤器过接着处理日志；
          返回ACCEPT，日志会被立即处理，不再经过剩余过滤器
          -->
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>ERROR</level>
            <onMatch>DENY</onMatch>
            <onMismatch>ACCEPT</onMismatch>
        </filter>
        <!-- 按照每天生成日志文件 -->
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!--日志文件输出的文件名-->
            <FileNamePattern>${LOG_HOME}/info.log.%d{yyyy-MM-dd}.log</FileNamePattern>
            <!--日志文件保留天数-->
            <MaxHistory>30</MaxHistory>
        </rollingPolicy>
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <pattern>${print_pattern}</pattern>
        </encoder>
        <!--日志文件最大的大小-->
        <triggeringPolicy class="ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy">
            <MaxFileSize>100MB</MaxFileSize>
        </triggeringPolicy>
    </appender>
    <appender name="ERR_FILE"  class="ch.qos.logback.core.rolling.RollingFileAppender">

        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>ERROR</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!--日志文件输出的文件名-->
            <FileNamePattern>${LOG_HOME}/error.log.%d{yyyy-MM-dd}.log</FileNamePattern>
            <!--日志文件保留天数-->
            <MaxHistory>30</MaxHistory>
        </rollingPolicy>
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <pattern>${print_pattern}</pattern>
        </encoder>
        <!--日志文件最大的大小-->
        <triggeringPolicy class="ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy">
            <MaxFileSize>10MB</MaxFileSize>
        </triggeringPolicy>
    </appender>


    <appender name="SERVICE_FILE"  class="ch.qos.logback.core.rolling.RollingFileAppender">
        <filter class="ch.qos.logback.core.filter.EvaluatorFilter">
            <evaluator>
                <expression>return message.contains("service方法");</expression>
            </evaluator>
            <OnMatch>ACCEPT</OnMatch>
            <OnMismatch>DENY</OnMismatch>
        </filter>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <FileNamePattern>${LOG_HOME}/service.log.%d{yyyy-MM-dd}.log</FileNamePattern>
            <MaxHistory>30</MaxHistory>
        </rollingPolicy>
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <pattern>${print_pattern}</pattern>
        </encoder>
    </appender>

    <appender name="syslogFile" class="ch.qos.logback.core.FileAppender">
        <file>${LOG_HOME}/syslogFile.log</file>
        <append>true</append>
        <encoder>
            <pattern>${print_pattern}</pattern>
        </encoder>
    </appender>
    <!-- show parameters for hibernate sql 专为 Hibernate 定制 -->
    <logger name="org.hibernate.type.descriptor.sql.BasicBinder"  level="TRACE" />
    <logger name="org.hibernate.type.descriptor.sql.BasicExtractor"  level="DEBUG" />
    <logger name="org.hibernate.SQL" level="DEBUG" />
    <logger name="org.hibernate.engine.QueryParameters" level="DEBUG" />
    <logger name="org.hibernate.engine.query.HQLQueryPlan" level="DEBUG" />

    <!--myibatis log configure-->
    <logger name="com.apache.ibatis" level="TRACE"/>
    <logger name="java.sql.Connection" level="DEBUG"/>
    <logger name="java.sql.Statement" level="DEBUG"/>
    <logger name="java.sql.PreparedStatement" level="DEBUG"/>


    <logger name="syslog" level="INFO" additivity="false">
        <appender-ref ref="syslogFile" />
    </logger>

    <!-- 日志输出级别 -->
    <root level="DEBUG">
        <appender-ref ref="STDOUT" />
        <appender-ref ref="SERVICE_FILE" />
        <appender-ref ref="ERR_FILE" />
        <appender-ref ref="INFO_FILE" />
    </root>
</configuration>
```

## 参考文档
[http://www.ttianjun.com/2015/10/30/logback](http://www.ttianjun.com/2015/10/30/logback)

[http://blog.csdn.net/mydeman/article/details/6716925](http://blog.csdn.net/mydeman/article/details/6716925)