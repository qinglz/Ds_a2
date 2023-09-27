package server_interface;


import server.Player;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface UserPoolInterface extends Remote {

    public String sayHello() throws RemoteException;


    public String test2(String msg) throws RemoteException;

    public PlayerInterface signIn(String name) throws IOException;

}
