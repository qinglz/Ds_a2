package client;
import server_interface.PlayerInterface;
import server_interface.UserPoolInterface;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class ClientMain {
    public static void main(String[] args) throws NotBoundException, IOException {
//        if (args.length!=2){
//            System.out.println("Invalid Arguments.");
//            return;
//        }
        String curPlayer = "jbwy";
//      todo: 用户名符号过滤
        Registry registry = LocateRegistry.getRegistry("localhost",8080);
        UserPoolInterface userPool = (UserPoolInterface) registry.lookup("userPool");
        System.out.println(userPool.sayHello());
        System.out.println(userPool.test2("几把物业"));
        PlayerInterface p = userPool.signIn(curPlayer);
//        System.out.println(p.getClass());

    }
}
