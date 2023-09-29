package server_interface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PlayerInterface extends Remote {
    TicTacToeInterface getGame() throws RemoteException;

    int getSign() throws RemoteException;

    void setSign(int sign) throws RemoteException;

    void setStatus(int status) throws RemoteException;

    int getStatus() throws RemoteException;

    void surrender() throws RemoteException;

    String getName() throws RemoteException;
}
