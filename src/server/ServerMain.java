package server;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerMain {
    public static void main(String[] args) {
        if (args.length!=2){
            System.out.println("Wrong argument number");
        }
        try {
            Registry registry = LocateRegistry.createRegistry(Integer.parseInt(args[1]));
            UserPool userPool = new UserPool();
            registry.rebind("userPool", userPool);
            System.out.println("Server is running");
        }catch (Exception e){
            System.out.println("Create registry failed");
        }


    }

}
