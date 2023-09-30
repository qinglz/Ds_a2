package client;
import server_interface.PlayerInterface;
import server_interface.TicTacToeInterface;
import server_interface.UserPoolInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Timer;
import java.util.TimerTask;

import static Constants.GameConstants.*;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;


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
        JButton quit = new JButton("Quit");
        JLabel info = new JLabel();
        JLabel countDown = new JLabel();
        Board game = new Board(p,countDown);
        quit.addActionListener(e -> {
            try {
                if(p.getStatus()==PLAYING){
                    p.surrender();
                    userPool.quitPlayer(curPlayer);
                    System.exit(0);
                }else {
                    userPool.quitPlayer(curPlayer);
                    System.exit(0);
                }
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        });
        ControlPanel controlPanel = new ControlPanel(quit,countDown,info);
        jFrame.getContentPane().add(controlPanel);
        jFrame.getContentPane().add(game);
        jFrame.getContentPane().setLayout(new GridLayout());
        jFrame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    if(p.getStatus()==PLAYING){
                        p.surrender();
                        userPool.quitPlayer(curPlayer);
                        System.exit(0);
                    }else {
                        userPool.quitPlayer(curPlayer);
                        System.exit(0);
                    }
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        jFrame.setBounds(500, 500, 600, 550);
        jFrame.setVisible(true);
        jFrame.setLocationRelativeTo(null);

        while (true){
            info.setText("Finding..");
            countDown.setText("Waiting for new player coming in..");
            game.refresh();
            Timer timer1 = new Timer();
            timer1.schedule(new TimerTask(){
                public void run() {
                    synchronized (p){
                        try {
                            if (p.getStatus()==OFFLINE){
                                p.notify();
                            }else if (p.getGame()!=null){
                                p.notify();

                            }
                            System.out.println("matching");
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }, 100, 500);

            synchronized (p){
                p.wait();
            }
            timer1.cancel();
            if (p.getStatus()==OFFLINE){
                return;
            }
            TicTacToeInterface t = p.getGame();

            game.activateGame(t,p.getSign());
            info.setText("Game Started!");
            System.out.println(p.getSign());

            Timer timer2 = new Timer();
            timer2.schedule(new TimerTask() {
                @Override
                public void run() {
                    synchronized (t){
                        try {
                            if(p.getStatus()==OFFLINE){
                                t.notify();
                            }else if(t.getGameStatus()==FINISHED){
                                game.updateBoard();
                                t.notify();
                            }else if(game.getCurRound()<t.getRoundNumber()){
                                game.updateBoard();
                                System.out.println("updated");
                            }
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }
            },100,500);

            synchronized (t){
                t.wait();
            }
            timer2.cancel();
            if (p.getStatus()==OFFLINE){
                return;
            }
            String result = t.getWinner();
            settle(result, curPlayer, userPool);
        }

//        userPool.quitPlayer(curPlayer);


    }
    public static void settle(String result, String player, UserPoolInterface userPool) throws RemoteException {
        if (result.equals(player)) {
            JOptionPane jOptionPane = new JOptionPane();
            int dialog = JOptionPane.showConfirmDialog(jOptionPane, "Game Over. Congratulation "+result+"! The winner is you.\nDo you want to start a new game?",
                    "Result", JOptionPane.YES_NO_OPTION);
            if (dialog == JOptionPane.NO_OPTION){
                userPool.quitPlayer(player);
                System.exit(0);
            }else {
                userPool.reloadPlayer(player);
            }
        } else if (result.equals(DRAW)) {
            JOptionPane jOptionPane = new JOptionPane();
            int dialog = JOptionPane.showConfirmDialog(jOptionPane, "Game Draw.\nDo you want to start a new game?", "Result", JOptionPane.YES_NO_OPTION);
            if (dialog == JOptionPane.NO_OPTION){
                userPool.quitPlayer(player);
                System.exit(0);
            }else {
                userPool.reloadPlayer(player);
            }
        }else if(!result.equals(UNKNOWN)){
            JOptionPane jOptionPane = new JOptionPane();
            int dialog = JOptionPane.showConfirmDialog(jOptionPane, "Game Over. Sorry (> <), winner is "+result+". Try to beat your opponent next time.\nDo you want to start a new game?",
                    "Result", JOptionPane.YES_NO_OPTION);
            if (dialog == JOptionPane.NO_OPTION){
                userPool.quitPlayer(player);
                System.exit(0);
            }else {
                userPool.reloadPlayer(player);
            }
        }
    }
}
