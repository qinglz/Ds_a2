package server;


import server_interface.PlayerInterface;
import server_interface.UserPoolInterface;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class UserPool extends UnicastRemoteObject implements UserPoolInterface {
    private static final int OFFLINE = 0;
    private static final int WAITING = 1;
    private static final int PLAYING = 2;
    private static final int RECONNECTING = 3;

    private Map<Player, Integer> status;
//    private Map<String, Integer> scores;

//    private Map<String, Player> activitedPlayers = new HashMap<>();
    private final String path;
//    private Registry registry;
    public UserPool(String path, Registry registry) throws IOException {
        super();
//        this.registry = registry;
        this.path = path;
        loadPlayers();
        startMatching();


    }

    private void loadPlayers() throws IOException {
        this.status = new HashMap<>();
//        this.scores = new HashMap<>();
        String newline;
        String[] newlineS;
        BufferedReader reader = new BufferedReader(new FileReader(this.path));
        while((newline = reader.readLine())!=null){
            newlineS = newline.split(";");
            Player newPlayer = new Player(newlineS[0], Integer.parseInt(newlineS[1]));
            this.status.put(newPlayer, OFFLINE);
//            this.scores.put(newlineS[0],Integer.parseInt(newlineS[1]));
        }
        reader.close();

    }
    private void refreshFile() throws IOException{
        BufferedWriter writer = new BufferedWriter(new FileWriter(this.path));
        for(Player p : this.status.keySet()){
            writer.write(p.getName()+";"+p.getRankPoint()+";");
            writer.newLine();
        }
        writer.close();
    }


    @Override
    public String sayHello() throws RemoteException {
        return "阿米诺斯";
    }

    @Override
    public String test2(String msg) throws RemoteException {
        return msg+"新年好";
    }

    @Override
    public PlayerInterface signIn(String name) throws IOException {
        Player newPlayer = null;
        if((newPlayer = getPlayerByName(name))!=null){
            if (this.status.get(newPlayer)==OFFLINE){
                this.status.remove(newPlayer);
                this.status.put(newPlayer,WAITING);
            }

        }else {
            newPlayer = new Player(name);
            this.status.put(newPlayer,WAITING);
            refreshFile();
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

    public void match() throws RemoteException {
        Player single = null;
        for(Map.Entry<Player, Integer> pStatus: this.status.entrySet()){
            if (pStatus.getValue()==WAITING){
                if (single == null){
                    single = pStatus.getKey();
                }else {
                    TicTacToe newGame = new TicTacToe();
                    single.setGame(newGame);
                    pStatus.getKey().setGame(newGame);

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



}
