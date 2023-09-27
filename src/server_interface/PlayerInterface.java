package server_interface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PlayerInterface extends Remote {
    TicTacToeInterface getGame() throws RemoteException;
}
