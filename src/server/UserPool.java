package server;


import server_interface.PlayerInterface;
import server_interface.UserPoolInterface;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import static Constants.GameConstants.*;


public class UserPool extends UnicastRemoteObject implements UserPoolInterface {


//    private Map<Player, Integer> status;
    private List<Player> players;
//    private Map<String, Integer> scores;

//    private Map<String, Player>
//    private Registry registry;
    public UserPool() throws IOException {
        super();
//        this.registry = registry;
//        loadPlayers();
//        this.status = new HashMap<>();
        this.players = new ArrayList<>();
        startMatching();
        startPlayersUpdate();


    }

//    private void loadPlayers() throws IOException {
//        this.status = new HashMap<>();
////        this.scores = new HashMap<>();
//        String newline;
//        String[] newlineS;
//        BufferedReader reader = new BufferedReader(new FileReader(this.path));
//        while((newline = reader.readLine())!=null){
//            newlineS = newline.split(";");
//            Player newPlayer = new Player(newlineS[0], Integer.parseInt(newlineS[1]));
//            this.status.put(newPlayer, OFFLINE);
////            this.scores.put(newlineS[0],Integer.parseInt(newlineS[1]));
//        }
//        reader.close();
//
//    }
//    private void refreshFile() throws IOException{
//        BufferedWriter writer = new BufferedWriter(new FileWriter(this.path));
//        for(Player p : this.status.keySet()){
//            writer.write(p.getName()+";"+p.getRankPoint()+";");
//            writer.newLine();
//        }
//        writer.close();
//    }


    @Override
    public String sayHello() throws RemoteException {
        return "阿米诺斯";
    }

    @Override
    public String test2(String msg) throws RemoteException {
        return msg+"新年好";
    }

    @Override
    public synchronized PlayerInterface signIn(String name) throws IOException {
        Player newPlayer = null;
        if((newPlayer = getPlayerByName(name))!=null){
            if (newPlayer.getStatus()==OFFLINE){
                newPlayer.setStatus(WAITING);
//                newPlayer.setReconnectTime(0);
//                this.status.remove(newPlayer);
//                this.status.put(newPlayer,WAITING);
            }else if(newPlayer.getStatus() ==PLAYING){
                return null;
            }else if(newPlayer.getStatus()==RECONNECTING){
                newPlayer.setReconnectTime(0);
                newPlayer.setStatus(PLAYING);
                newPlayer.rejoin();
//                this.status.remove(newPlayer);
//                this.status.put(newPlayer,PLAYING);
            }
        }else {
            newPlayer = new Player(name);
            newPlayer.setStatus(WAITING);
            this.players.add(newPlayer);
//            this.status.put(newPlayer,WAITING);
//            refreshFile();
        }
        return newPlayer;

    }

    private Player getPlayerByName(String name){
        for(Player p : this.players){
            if (p.getName().equals(name)){
                return p;
            }
        }
        return null;
    }

    public synchronized void match() throws RemoteException {
        Player single = null;
        for(Player p : players){
            if (p.getStatus() == WAITING){
                if (single == null){
                    single = p;
                }else {
                    TicTacToe newGame = new TicTacToe();
                    newGame.setPlayerX(single);
                    newGame.setPlayerO(p);
                    single.setGame(newGame);
                    p.setGame(newGame);
                    single.setSign(X);
                    p.setSign(O);
                    single.setStatus(PLAYING);
                    p.setStatus(PLAYING);
                    single = null;

                }

            }
        }

    }
    public void startMatching(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    match();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, 0, 1000L * 3);

    }

    @Override
    public synchronized void quitPlayer(String name){
        Player player = getPlayerByName(name);
        assert player != null;
        player.setGame(null);
        player.setSign(UNASSIGNED);
        player.setStatus(OFFLINE);
//        this.status.remove(player);
//        this.status.put(player, OFFLINE);



    }

    @Override
    public synchronized void reloadPlayer(String name) throws RemoteException {
        Player player = getPlayerByName(name);
        assert player != null;
        player.setStatus(WAITING);
        player.setGame(null);
        player.setSign(UNASSIGNED);
//        this.status.remove(player);
//        this.status.put(player,WAITING);

    }

    public synchronized void playersStatusUpdate(){
        for(Player p : players){
            if (p.getStatus()==WAITING) {
                if (!p.isAlive()){
                    p.setGame(null);
                    p.setSign(UNASSIGNED);
                    p.setStatus(OFFLINE);
                    System.out.println(p.getName()+" shuts down.");
//                    this.status.remove(pStatus.getKey());
//                    this.status.put(pStatus.getKey(), OFFLINE);
                }
            }else if (p.getStatus()==PLAYING){
                if (!p.isAlive()){
                    p.pause();
                    p.setStatus(RECONNECTING);
                    System.out.println(p.getName()+" shuts down. Wait for reconnecting");
                }
            }else if (p.getStatus()==RECONNECTING){
                if (p.getReconnectTime()>=10){
                    Player op = p.getOpponent();
                    if (op.getStatus()==RECONNECTING){
                        op.setGame(null);
                        op.setSign(UNASSIGNED);
                        op.setStatus(OFFLINE);
                        op.setReconnectTime(0);
                    }
                    p.makeGameDraw();
                    p.setGame(null);
                    p.setSign(UNASSIGNED);
                    p.setStatus(OFFLINE);
                    p.setReconnectTime(0);
                    System.out.println(p.getName()+" can not reconnect.");

                }else {
                    p.increaseReconnectTime();
                }
            }
        }
    }
    public void startPlayersUpdate(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    playersStatusUpdate();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, 0, 3000);
    }


}
