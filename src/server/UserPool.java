package server;


import server_interface.UserPoolInterface;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;


public class UserPool extends UnicastRemoteObject implements UserPoolInterface {
    private static final int OFFLINE = 0;
    private static final int WAITING = 1;
    private static final int PLAYING = 2;
    private static final int RECONNECTING = 3;

    private Map<String, Integer> status;
    private Map<String, Integer> scores;
    private final String path;
    private Registry registry;
    public UserPool(String path, Registry registry) throws IOException {
        super();
        this.registry = registry;
        this.path = path;
        loadPlayers();


    }

    private void loadPlayers() throws IOException {
        this.status = new HashMap<>();
        this.scores = new HashMap<>();
        String newline;
        String[] newlineS;
        BufferedReader reader = new BufferedReader(new FileReader(this.path));
        while((newline = reader.readLine())!=null){
            newlineS = newline.split(";");
            this.status.put(newlineS[0], OFFLINE);
            this.scores.put(newlineS[0],Integer.parseInt(newlineS[1]));
        }
        reader.close();

    }
    private void refreshFile() throws IOException{
        BufferedWriter writer = new BufferedWriter(new FileWriter(this.path));
        for(Map.Entry<String, Integer> info: this.scores.entrySet()){
            writer.write(info.getKey()+";"+info.getValue()+";");
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
    public String signIn(String name) throws IOException {
        Player newPlayer;
        if(this.status.containsKey(name)){
            newPlayer = new Player(name, this.scores.get(name));
        }else {
            newPlayer = new Player(name);
            this.status.put(name,WAITING);
            this.scores.put(name, 0);
            refreshFile();
        }
        this.registry.rebind(name, newPlayer);

        return name;
    }


}
