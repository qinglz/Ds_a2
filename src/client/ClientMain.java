package client;
import java.io.*;


public class ClientMain {
    public static void main(String[] args) {
        if (args.length!=2){
            System.out.println("Invalid Arguments.");
            return;
        }
        try{
        ClientSocketChannel channel = new ClientSocketChannel(args[0], Integer.parseInt(args[1]));
//        ClientWindow clientWindow = new ClientWindow(channel);

        }catch (IOException | ClassNotFoundException | IllegalArgumentException e){
            System.out.println("Cannot connect to serve, make sure you run server first.");
//            e.printStackTrace();
        }
    }
}
