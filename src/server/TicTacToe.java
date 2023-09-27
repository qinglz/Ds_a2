package server;

import server_interface.TicTacToeInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;



public class TicTacToe extends UnicastRemoteObject implements TicTacToeInterface {
    public TicTacToe() throws RemoteException {
        super();
    }
}
