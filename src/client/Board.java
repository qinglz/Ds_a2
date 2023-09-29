package client;

import server_interface.PlayerInterface;
import server_interface.TicTacToeInterface;

import java.awt.GridLayout;
import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import static Constants.GameConstants.*;

public class Board extends JPanel {

    private boolean active;
    private int playerSign;
    private boolean moved;

    private int curRound = 0;

    private int curSign;

    public int getCurRound() {
        return curRound;
    }

    //    private final int totalCells = 9;
    private final int totalRows = 3;
    private final int totalColumns = 3;
    private final JButton[][] boardButtons = new JButton[totalRows][totalColumns];
    private TicTacToeInterface curGame;
    private PlayerInterface player;

    public Board(PlayerInterface player) {
        GridLayout ticTacToeGridLayout = new GridLayout(totalRows, totalColumns);
        setLayout(ticTacToeGridLayout);
        createButtons();
        this.playerSign = UNASSIGNED;
        this.curGame = null;
        this.active = false;
        this.curSign = UNASSIGNED;
        this.player = player;
    }
    public void createButtons() {
        for (int i = 0; i < totalRows; i++) {
            for (int j = 0;j<totalColumns;j++){
                boardButtons[i][j] = new JButton();
                boardButtons[i][j].setText("");
                int finalI = i;
                int finalJ = j;
                boardButtons[i][j].addActionListener(e -> {
                    if (this.active&&this.curSign==this.playerSign){
                        try {

                            curGame.makeAMove(this.playerSign, finalI, finalJ);
                            this.moved = true;
//                            showWinner(result);
                        } catch (RemoteException ex) {
                            throw new RuntimeException(ex);
                        }
                    }

                });
                boardButtons[i][j].setEnabled(false);
                add(boardButtons[i][j]);
            }

        }
    }
    //                JButton clickedBtn = (JButton) e.getSource();
//                clickedBtn.setText(String.valueOf(playerSign));
//                clickedBtn.setEnabled(false);
//                if (playerSign == 'x')
//                    playerSign = 'o';
//                else
//                    playerSign = 'x';
    public void activateGame(TicTacToeInterface curGame, int playerSign) {
        this.curGame = curGame;
        this.playerSign = playerSign;
        for (int i = 0;i<totalRows;i++){
            for (int j = 0;j<totalColumns;j++){
                this.boardButtons[i][j].setEnabled(true);
            }
        }
        this.active = true;
    }


    public void updateBoard() throws RemoteException {
        this.curRound = curGame.getRoundNumber();
        this.curSign = this.curGame.getCurSign();
        int[][] board = this.curGame.getGameBoard();
        for(int i = 0;i<totalRows;i++){
            for (int j = 0;j<totalColumns;j++){
                if(board[i][j]==X){
                    this.boardButtons[i][j].setText("X");
                    this.boardButtons[i][j].setEnabled(false);
                }else if(board[i][j]==O){
                    this.boardButtons[i][j].setText("O");
                    this.boardButtons[i][j].setEnabled(false);
                }
            }
        }
        if(curSign==playerSign&&this.curGame.getGameStatus()==RUNNING){
            this.moved = false;
            Timer countDown = new Timer();
            final int[] i = {30};
            countDown.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        if(curGame.getGameStatus()==FINISHED){
                            countDown.cancel();
                        }else if (moved){
                            countDown.cancel();
                        }else if (i[0]==0){
                            makeRandomMove(board);
                            countDown.cancel();
                        }else {
                            i[0]--;
                            System.out.println(i[0]);
                        }
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }

                }
            },0,1000);
        }
    }

    public int getCurSign() {
        return curSign;
    }

    private void makeRandomMove(int [][] gameboard) throws RemoteException {
        for(int i = 0; i<totalRows;i++){
            for (int j = 0; j<totalColumns;j++){
                if(gameboard[i][j]==EMPTY){
                    this.curGame.makeAMove(this.playerSign,i,j);
                    this.moved = true;
                    return;
                }
            }
        }

    }

//    public static void main(String[] args) {
//        JFrame jFrame = new JFrame("Tic Tac Toe Game");
//        jFrame.getContentPane().add(new Board());
//        jFrame.setBounds(500, 500, 600, 550);
//        jFrame.setVisible(true);
//        jFrame.setLocationRelativeTo(null);
//    }
}