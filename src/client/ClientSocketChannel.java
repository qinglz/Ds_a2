package client;

import data_package.MyRequest;
import data_package.MyResponse;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientSocketChannel {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    public ClientSocketChannel(String host, int port) throws IOException, ClassNotFoundException {
        this.socket = new Socket(host,port);
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }
    public synchronized MyResponse getMeanings(String word) throws IOException, ClassNotFoundException {
        MyRequest myRequest = new MyRequest("meanings",word,null);
        out.writeObject(myRequest);
        return (MyResponse)in.readObject();

    }
    public synchronized MyResponse deleteWord(String word) throws IOException, ClassNotFoundException {
        MyRequest myRequest = new MyRequest("delete", word, null);
        out.writeObject(myRequest);
        return (MyResponse)in.readObject();

    }
    public synchronized MyResponse updateWord(String word, List<String> meanings) throws IOException, ClassNotFoundException {
        MyRequest myRequest = new MyRequest("update", word, meanings);
        out.writeObject(myRequest);
        return (MyResponse)in.readObject();
    }
    public synchronized MyResponse addWord(String word, List<String> meanings) throws IOException, ClassNotFoundException {
        MyRequest myRequest = new MyRequest("add", word, meanings);
        out.writeObject(myRequest);
        return (MyResponse)in.readObject();
    }
    public synchronized void close(){
        try {
            MyRequest myRequest = new MyRequest("exit", null,null);
            out.writeObject(myRequest);
            socket.close();
            in.close();
            out.close();
        } catch (IOException e) {
//            e.printStackTrace();
        }
    }
}
