package server;

import server_interface.PlayerInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Player extends UnicastRemoteObject implements PlayerInterface {
    private String name;
    private int rankPoint;
    public Player(String name) throws RemoteException {
        super();
        this.name = name;
        this.rankPoint = 0;
    }
    public Player(String name, int rankPoint) throws RemoteException {
        super();
        this.name = name;
        this.rankPoint = rankPoint;
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


}
