package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {
    public static void main(String args[]){
        if (args.length!=2) {
            System.out.println("Invalid Arguments.");
            return;
        }
        ServerSocket serverSocket = null;
        try{
            int port = Integer.parseInt(args[0]);
            serverSocket = new ServerSocket(port);
            System.out.println("Server is running now.");
        } catch (IOException |IllegalArgumentException e) {
//            e.printStackTrace();
            System.out.println("Server running failed.");
            return;
        }
        while (true) {
            Socket socket;
            try {
                // 接教客户连接
                socket = serverSocket.accept();
                // 创建一个工作线程
                Thread workThread = new Thread(new ServerThread(socket));
                // 启动工作线程
                workThread.start();
            } catch (IOException e) {
                System.out.println("Fail To Start WorkerThread.");
//                e.printStackTrace();
            }
        }
    }

}
