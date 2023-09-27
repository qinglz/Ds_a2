package server;

import server_interface.PlayerInterface;
import server_interface.TicTacToeInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Player extends UnicastRemoteObject implements PlayerInterface {
    private String name;
    private int rankPoint;
    private TicTacToe game;

    private int sign;

    public Player(String name) throws RemoteException {
        super();
        this.name = name;
        this.rankPoint = 0;
        this.game = null;
        this.sign = -1;
    }
    public Player(String name, int rankPoint) throws RemoteException {
        super();
        this.name = name;
        this.rankPoint = rankPoint;
        this.game = null;
        this.sign = -1;

    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRankPoint() {
        return rankPoint;
    }

    public void setRankPoint(int rankPoint) {
        this.rankPoint = rankPoint;
    }

    public void setGame(TicTacToe game) {
        this.game = game;
    }

    @Override
    public TicTacToeInterface getGame() throws RemoteException {
        return this.game;
    }
}
