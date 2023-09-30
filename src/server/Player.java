package server;

import server_interface.PlayerInterface;
import server_interface.TicTacToeInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import static Constants.GameConstants.*;

public class Player extends UnicastRemoteObject implements PlayerInterface {
    private String name;
    private int rankPoint;
    private TicTacToe game;

    private int reconnectTime;

    private int beats;

    private int status;
    private int sign;

    public Player(String name) throws RemoteException {
        super();
        this.name = name;
        this.beats=0;
        this.rankPoint = 0;
        this.game = null;
        this.sign = UNASSIGNED;
        this.status = OFFLINE;
        this.reconnectTime = 0;
    }

    public void rejoin(){
        assert this.game!=null;
        if (this.game.getGameStatus()==PAUSED){
            this.game.setGameStatus(RUNNING);
        }else {
            this.game.setGameStatus(PAUSED);
        }
    }

    public void pause(){
        assert this.game!=null;
        if (this.game.getGameStatus()==RUNNING){
            this.game.setGameStatus(PAUSED);
        }else {
            this.game.setGameStatus(DOUBLE_PAUSED);
        }
    }
    @Override
    public String getName() {
        return this.name;
    }

    public Player getOpponent(){
        return this.game.getOpponent(this.sign);
    }

    @Override
    public void heartbeat() throws RemoteException {
        this.beats++;
    }

    public void makeGameDraw(){
        this.game.unexpectedDraw();
    }

    public boolean isAlive(){
        boolean alive = this.beats>0;
        this.beats = 0;
        return alive;
    }

    public int getReconnectTime() {
        return reconnectTime;
    }

    public void setReconnectTime(int reconnectTime) {
        this.reconnectTime = reconnectTime;
    }

    public void increaseReconnectTime(){
        this.reconnectTime++;
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

    public void surrender(){
        this.game.surrender(this.sign);
    }
    @Override
    public void setSign(int sign) {
        this.sign = sign;
    }
    @Override
    public int getSign() {
        return sign;
    }

    @Override
    public int getStatus(){return status;}
    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public TicTacToeInterface getGame() throws RemoteException {
        return this.game;
    }
}
