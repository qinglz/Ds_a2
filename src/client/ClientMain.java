package client;
import server_interface.PlayerInterface;
import server_interface.UserPoolInterface;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class ClientMain {
    public static void main(String[] args) throws NotBoundException, IOException, InterruptedException {
//        if (args.length!=2){
//            System.out.println("Invalid Arguments.");
//            return;
//        }
        String curPlayer = args[0];
//      todo: 用户名符号过滤，启动线程发heart beat。服务器端启动线程检测heart beat。
        Registry registry = LocateRegistry.getRegistry("localhost",8080);
        UserPoolInterface userPool = (UserPoolInterface) registry.lookup("userPool");
        System.out.println(userPool.sayHello());
        System.out.println(userPool.test2("几把物业"));
        PlayerInterface p = userPool.signIn(curPlayer);
        while(true){
            System.out.println(p.getGame()==null);
            Thread.sleep(1000);
        }
//        System.out.println(p.getClass());

    }
}
