package client;
import server.UserPool;
import server_interface.PlayerInterface;
import server_interface.TicTacToeInterface;
import server_interface.UserPoolInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Timer;
import java.util.TimerTask;

import static Constants.GameConstants.*;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;


public class ClientMain {
    public static void main(String[] args) throws InterruptedException {
//        if (args.length!=2){
//            System.out.println("Invalid Arguments.");
//            return;
//        }
        if (args.length!=3){
            System.out.println("Wrong argument number");
            return;
        }
        String curPlayer = args[0];
        PlayerInterface p0 = null;
        UserPoolInterface userPool0 = null;
        try {
            Registry registry = LocateRegistry.getRegistry(args[1],Integer.parseInt(args[2]));
            userPool0 = (UserPoolInterface) registry.lookup("userPool");


            p0 = userPool0.signIn(curPlayer);
            if (p0==null){
                System.out.println("Cannot sign in with same username");
                return;
            }
        }catch (Exception e){
            System.out.println("Cannot connect to server. Make sure your ip and port are correct.");
            return;
        }

        PlayerInterface p = p0;
        UserPoolInterface userPool = userPool0;
        Timer beatTimer = new Timer();
        beatTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    p.heartbeat();
                } catch (RemoteException e) {
                    System.out.println("Server Shut Down");
                    beatTimer.cancel();
                }
            }
        },0,1000);

        JFrame jFrame = new JFrame("Tic Tac Toe Game");
        JButton quit = new JButton("Quit");
        JLabel info = new JLabel();
        JLabel countDown = new JLabel();
        Board game = new Board(countDown);
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
                System.out.println("Server Shut Down");
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
                    System.out.println("Server Shut Down");
//                    throw new RuntimeException(ex);
                }
            }
        });
        jFrame.setBounds(500, 500, 600, 550);
        jFrame.setVisible(true);
        jFrame.setLocationRelativeTo(null);
        try {
            while (true){
                info.setText("<html>"+p.getProfile()+"<br>Finding..");
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
                                timer1.cancel();
                                System.out.println("Server Shut Down");
                                p.notify();
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
                info.setText("<html>"+p.getProfile()+"<br>Game Started!<br>Your opponent is "+p.getOpponentProfile());
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
                                }else if (game.getGameStatus()!=t.getGameStatus()){
                                    game.updateBoard();
                                }
                            } catch (RemoteException e) {
                                timer2.cancel();
                                System.out.println("Server Shut Down");
                                t.notify();

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
//            p.setStatus(CHOOSING);
                settle(result, curPlayer, userPool);
            }
        }catch (Exception e){
            JDialog errDialog = new JDialog(jFrame);
            errDialog.setBounds(20,20,400,100);
            errDialog.setLayout(new FlowLayout(FlowLayout.LEFT,10,20));
            errDialog.setLocationRelativeTo(jFrame);
            errDialog.add(new JLabel("Fail To Connect Server"));
            errDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            errDialog.setVisible(true);
            Thread.sleep(5000);
            System.exit(0);
        }


//        userPool.quitPlayer(curPlayer);


    }
    public static void settle(String result, String player, UserPoolInterface userPool) throws RemoteException {
        userPool.quitPlayer(player);
        if (result.equals(player)) {
            JOptionPane jOptionPane = new JOptionPane();
            int dialog = JOptionPane.showConfirmDialog(jOptionPane, "Game Over. Congratulation "+result+"! The winner is you.\nDo you want to start a new game?",
                    "Result", JOptionPane.YES_NO_OPTION);
            if (dialog == JOptionPane.NO_OPTION){
//                userPool.quitPlayer(player);
                System.exit(0);
            }else {
                userPool.reloadPlayer(player);
            }
        } else if (result.equals(DRAW)) {
            JOptionPane jOptionPane = new JOptionPane();
            int dialog = JOptionPane.showConfirmDialog(jOptionPane, "Game Draw.\nDo you want to start a new game?", "Result", JOptionPane.YES_NO_OPTION);
            if (dialog == JOptionPane.NO_OPTION){
//                userPool.quitPlayer(player);
                System.exit(0);
            }else {
                userPool.reloadPlayer(player);
            }
        } else if (result.equals(UNEXPECTED_DRAW)) {
            JOptionPane jOptionPane = new JOptionPane();
            int dialog = JOptionPane.showConfirmDialog(jOptionPane, "Game Draw due to the opponent cannot reconnect.\nDo you want to start a new game?", "Result", JOptionPane.YES_NO_OPTION);
            if (dialog == JOptionPane.NO_OPTION){
//                userPool.quitPlayer(player);
                System.exit(0);
            }else {
                userPool.reloadPlayer(player);
            }
        } else if(!result.equals(UNKNOWN)){
            JOptionPane jOptionPane = new JOptionPane();
            int dialog = JOptionPane.showConfirmDialog(jOptionPane, "Game Over. Sorry (> <), winner is "+result+". Try to beat your opponent next time.\nDo you want to start a new game?",
                    "Result", JOptionPane.YES_NO_OPTION);
            if (dialog == JOptionPane.NO_OPTION){
//                userPool.quitPlayer(player);
                System.exit(0);
            }else {
                userPool.reloadPlayer(player);
            }
        }
    }
}
