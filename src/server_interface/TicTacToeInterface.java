package server_interface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TicTacToeInterface extends Remote {
    String hello() throws RemoteException;

    void makeAMove(int sign, int row, int column) throws RemoteException;

    int[][] getGameBoard() throws RemoteException;

    int getCurSign() throws RemoteException;

    int getGameStatus() throws RemoteException;

    int getWinner() throws RemoteException;
}
