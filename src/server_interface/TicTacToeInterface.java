package server_interface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TicTacToeInterface extends Remote {
    String hello() throws RemoteException;

    int makeAMove(int sign, int row, int column) throws RemoteException;

    int[][] getGameBoard() throws RemoteException;
}
