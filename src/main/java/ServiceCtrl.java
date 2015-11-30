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
