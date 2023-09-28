package client;
import server_interface.PlayerInterface;
import server_interface.TicTacToeInterface;
import server_interface.UserPoolInterface;

import javax.swing.*;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Timer;
import java.util.TimerTask;

import static Constants.GameConstants.FINISHED;
import static Constants.GameConstants.UNKNOWN;


public class ClientMain {
    public static void main(String[] args) throws NotBoundException, IOException, InterruptedException {
//        if (args.length!=2){
//            System.out.println("Invalid Arguments.");
//            return;
//        }
        String curPlayer = args[0];
//      todo: 用户名符号过滤，启动线程发heart beat。服务器端启动线程检测heart beat。
        Registry registry = LocateRegistry.getRegistry("localhost",8080);
        UserPoolInterface userPool = (UserPoolInterface) registry.lookup("userPool");
        System.out.println(userPool.sayHello());
        System.out.println(userPool.test2("几把物业"));

        PlayerInterface p = userPool.signIn(curPlayer);

        JFrame jFrame = new JFrame("Tic Tac Toe Game");
        Board game = new Board();
        jFrame.getContentPane().add(game);
        jFrame.setBounds(500, 500, 600, 550);
        jFrame.setVisible(true);
        jFrame.setLocationRelativeTo(null);


        Timer timer1 = new Timer();
        timer1.schedule(new TimerTask(){
            public void run() {
                synchronized (p){
                    try {
                        if (p.getGame()!=null){
                            p.notify();


                        }
                        System.out.println("matching");
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }, 1000, 1000);

        synchronized (p){
            p.wait();
        }

        timer1.cancel();
        TicTacToeInterface t = p.getGame();

        game.activateGame(t,p.getSign());

        System.out.println(p.getSign());

        Timer timer2 = new Timer();
        timer2.schedule(new TimerTask() {
            @Override
            public void run() {
                synchronized (t){
                    try {
                        if(t.getGameStatus()==FINISHED){
                            game.updateBoard();
                            t.notify();
                        }else if(game.getCurSign()!=t.getCurSign()){
                            game.updateBoard();
                            System.out.println("updated");
                        }
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        },1000,500);

        synchronized (t){
            t.wait();
        }
        timer2.cancel();
        int result = t.getWinner();
        game.showWinner(result);
        userPool.quitPlayer(curPlayer);


    }
}
