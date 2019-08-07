package com.dyny.baseconnector.task;

//import com.dyny.baseconnector.server.tcp.GmsWsMsgServerHandler;
//import com.dyny.baseconnector.server.tcp.TcpServerStarter;
//import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @Auther: lane
 * @Date: 2019-04-01 08:56
 * @Description:
 * @Version 1.0.0
 */
@Component
@Order(1)
public class StartedTask implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
//        TcpServerStarter wsServerStarter = new TcpServerStarter(6789, new GmsWsMsgServerHandler());
//        wsServerStarter.start();
    }

}
