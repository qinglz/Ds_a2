package server_interface;


import server.Player;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface UserPoolInterface extends Remote {

    String sayHello() throws RemoteException;


    String test2(String msg) throws RemoteException;

    PlayerInterface signIn(String name) throws IOException;

    void quitPlayer(String name) throws RemoteException;
    void reloadPlayer(String name) throws RemoteException;
}
