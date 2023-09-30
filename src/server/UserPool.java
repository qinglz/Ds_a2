package server;


import server_interface.PlayerInterface;
import server_interface.UserPoolInterface;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import static Constants.GameConstants.*;


public class UserPool extends UnicastRemoteObject implements UserPoolInterface {


    private Map<Player, Integer> status;
//    private Map<String, Integer> scores;

//    private Map<String, Player>
//    private Registry registry;
    public UserPool() throws IOException {
        super();
//        this.registry = registry;
//        loadPlayers();
        this.status = new HashMap<>();
        startMatching();


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
            if (this.status.get(newPlayer)==OFFLINE){
                newPlayer.setStatus(WAITING);
                this.status.remove(newPlayer);
                this.status.put(newPlayer,WAITING);
            }else if(this.status.get(newPlayer)==PLAYING){
                return null;
            }else if(this.status.get(newPlayer)==RECONNECTING){
                newPlayer.setStatus(PLAYING);
                newPlayer.rejoin();
                this.status.remove(newPlayer);
                this.status.put(newPlayer,PLAYING);
            }
        }else {
            newPlayer = new Player(name);
            newPlayer.setStatus(WAITING);
            this.status.put(newPlayer,WAITING);
//            refreshFile();
        }
        return newPlayer;

    }

    private Player getPlayerByName(String name){
        for(Player p : this.status.keySet()){
            if (p.getName().equals(name)){
                return p;
            }
        }
        return null;
    }

    public synchronized void match() throws RemoteException {
        Player single = null;
        Set<Map.Entry<Player, Integer>> info = new HashSet<>(this.status.entrySet());
        for(Map.Entry<Player, Integer> pStatus: info){
            if (pStatus.getValue()==WAITING){
                if (single == null){
                    single = pStatus.getKey();
                }else {
                    TicTacToe newGame = new TicTacToe();
                    newGame.setPlayerX(single);
                    newGame.setPlayerO(pStatus.getKey());
                    single.setGame(newGame);
                    pStatus.getKey().setGame(newGame);
                    single.setSign(X);
                    pStatus.getKey().setSign(O);
                    single.setStatus(PLAYING);
                    pStatus.getKey().setStatus(PLAYING);
                    this.status.remove(single);
                    this.status.put(single, PLAYING);
                    this.status.remove(pStatus.getKey());
                    this.status.put(pStatus.getKey(),PLAYING);
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
        this.status.remove(player);
        this.status.put(player, OFFLINE);



    }

    @Override
    public synchronized void reloadPlayer(String name) throws RemoteException {
        Player player = getPlayerByName(name);
        assert player != null;
        player.setStatus(WAITING);
        player.setGame(null);
        player.setSign(UNASSIGNED);
        this.status.remove(player);
        this.status.put(player,WAITING);

    }


}
