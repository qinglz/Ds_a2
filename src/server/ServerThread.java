package server;

import data_package.MyRequest;
import data_package.MyResponse;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class ServerThread extends Thread{
    private Socket socket;
    private ObjectOutputStream writer;
    private ObjectInputStream reader;

    public ServerThread(Socket socket) throws IOException{
        this.socket = socket;
        this.writer = new ObjectOutputStream(socket.getOutputStream());
        this.reader = new ObjectInputStream(socket.getInputStream());

    }
    public void run() {
        try {
            System.out.println("New connection accepted" + socket.getInetAddress() + ":" + socket.getPort());

            MyRequest myRequest;
            MyResponse myResponse;
            // 接收和发送数据，直到通信结束
//            while ((myRequest = (MyRequest)reader.readObject()) != null) {
//                System.out.println("from "+ socket.getInetAddress() + ":" + socket.getPort() + ">" + myRequest);
//                if (myRequest.getOperation().equalsIgnoreCase("exit")){
//                    break;
//                }else if (myRequest.getOperation().equalsIgnoreCase("add")){
//                    if (myRequest.getMeanings()!=null&&myRequest.getWord()!=null
//                            &&myRequest.getMeanings().size()>0&&myRequest.getWord().length()>0){
//                        try{
//                            boolean replicated = !dictionary.addNewWord(myRequest.getWord(),myRequest.getMeanings());
//                            if(replicated){
//                                myResponse = new MyResponse(4, "Replicated word", null);
//                            }else {
//                                myResponse = new MyResponse(0,"Succeed",null);
//                            }
//
//                        }catch (IOException e){
//                            myResponse = new MyResponse(2,"Fail to add word to dictionary file",null);
//                        }
//                    }else {
//                        myResponse = new MyResponse(1,"Invalid input",null);
//                    }
//                    writer.writeObject(myResponse);
//                }else if (myRequest.getOperation().equalsIgnoreCase("delete")){
//                    if (myRequest.getWord()!=null&myRequest.getWord().length()>0){
//                        try{
//                            boolean notFound = !dictionary.deleteWord(myRequest.getWord());
//                            if (notFound){
//                                myResponse = new MyResponse(5, "Cannot find the word", null);
//                            }else {
//                                myResponse = new MyResponse(0, "Succeed", null);
//                            }
//                        }catch (IOException e){
//                            myResponse = new MyResponse(3,"Fail to delete word",null);
//                        }
//                    }else {
//                        myResponse = new MyResponse(1,"Invalid input",null);
//                    }
//                    writer.writeObject(myResponse);
//                }else if (myRequest.getOperation().equalsIgnoreCase("update")){
//                    if (myRequest.getMeanings()!=null&&myRequest.getWord()!=null
//                            &&myRequest.getMeanings().size()>0&&myRequest.getWord().length()>0){
//                        try{
//                            boolean notFound = !dictionary.updateWord(myRequest.getWord(),myRequest.getMeanings());
//                            if(notFound){
//                                myResponse = new MyResponse(5, "Cannot find the word", null);
//                            }else {
//                                myResponse = new MyResponse(0, "Succeed", null);
//                            }
//                        }catch (IOException e){
//                            myResponse = new MyResponse(2,"Fail to update word",null);
//                        }
//                    }else {
//                        myResponse = new MyResponse(1,"Invalid input",null);
//                    }
//                    writer.writeObject(myResponse);
//                }else if (myRequest.getOperation().equalsIgnoreCase("meanings")){
//                    if (myRequest.getWord()!=null&&myRequest.getWord().length()>0){
//                        List<String> ms = dictionary.getMeaning(myRequest.getWord());
//                        if (ms==null){
//                            myResponse = new MyResponse(5,"Cannot find the word",null);
//                        }else {
//                            myResponse = new MyResponse(0,"Succeed",ms);
//                        }
//                    }else {
//                        myResponse = new MyResponse(1,"Invalid input",null);
//                    }
//                    writer.writeObject(myResponse);
//                }else {
//                    myResponse = new MyResponse(1,"Invalid input",null);
//                    writer.writeObject(myResponse);
//                }
//
//            }
        } catch (Exception e) {
            System.out.println("Socket Error, Client May Shut Down");
//            e.printStackTrace();
        } finally {
            try {
                // 断开连接
                if(socket != null) socket.close();
                this.writer.close();
                this.reader.close();
            } catch (IOException e) {
//                e.printStackTrace();
            }
        }
    }
}
