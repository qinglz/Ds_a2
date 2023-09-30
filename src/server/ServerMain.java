package server;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerMain {
    public static void main(String[] args) throws IOException {
        Registry registry = LocateRegistry.createRegistry(8080);
        UserPool userPool = new UserPool();
        registry.rebind("userPool", userPool);
        System.out.println("Server is running");

    }

}
