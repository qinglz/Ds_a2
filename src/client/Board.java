package client;

import server_interface.TicTacToeInterface;

import java.awt.GridLayout;
import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;

import static Constants.GameConstants.*;

public class Board extends JPanel {

    private int playerSign;
    private boolean moved;

    private int curRound = 0;

    private int curSign;


    private int gameStatus;

    //    private final int totalCells = 9;
    private final int totalRows = 3;
    private final int totalColumns = 3;
    private final JButton[][] boardButtons = new JButton[totalRows][totalColumns];
    private TicTacToeInterface curGame;
//    private PlayerInterface player;

    private JLabel timeCount;

    public Board(JLabel timeCount) {
        GridLayout ticTacToeGridLayout = new GridLayout(totalRows, totalColumns);
        setLayout(ticTacToeGridLayout);
        createButtons();
        this.playerSign = UNASSIGNED;
        this.curGame = null;
//        this.active = false;
        this.curSign = UNASSIGNED;
        this.gameStatus = NOT_START;
//        this.player = player;
        this.timeCount = timeCount;
    }
    public void refresh(){
        this.playerSign = UNASSIGNED;
        this.curGame = null;
//        this.active = false;
        this.curSign = UNASSIGNED;
        this.gameStatus = NOT_START;
        this.curRound = 0;
        for (int i = 0; i < totalRows; i++) {
            for (int j = 0;j<totalColumns;j++){
                assert boardButtons[i][j]!=null;
                boardButtons[i][j].setText("");
                boardButtons[i][j].setEnabled(true);
            }
        }

    }

    public void createButtons() {
        for (int i = 0; i < totalRows; i++) {
            for (int j = 0;j<totalColumns;j++){
                boardButtons[i][j] = new JButton();
                boardButtons[i][j].setText("");
                int finalI = i;
                int finalJ = j;
                boardButtons[i][j].addActionListener(e -> {
                    if (this.gameStatus==RUNNING&&this.curSign==this.playerSign){
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
        this.gameStatus = RUNNING;
    }


    public void updateBoard() throws RemoteException {
        this.curRound = curGame.getRoundNumber();
        this.curSign = this.curGame.getCurSign();
        this.gameStatus = this.curGame.getGameStatus();
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
        if(curSign==playerSign&&this.gameStatus==RUNNING){
            this.moved = false;
            Timer countDown = new Timer();
            final int[] i = {20};
            this.timeCount.setText("Your turn!\nTime Remaining: "+i[0]);
            countDown.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        if(gameStatus!=RUNNING){
                            countDown.cancel();
                        }else if (moved){
                            countDown.cancel();
                        }else if (i[0]==0){
                            makeRandomMove(board);
                            countDown.cancel();
                        }else {
                            timeCount.setText("Your turn!\nTime Remaining: "+i[0]);
                            System.out.println(i[0]);
                            i[0]--;
                        }
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }

                }
            },0,1000);
        }else if(curSign!=playerSign&&this.gameStatus==RUNNING){
            this.timeCount.setText("Wait for opponent's move");
        }else if (this.gameStatus==PAUSED){
            this.timeCount.setText("Opponent shuts down, please wait for reconnecting");
        }
    }


    public int getCurRound() {
        return curRound;
    }

    public int getGameStatus() {
        return gameStatus;
    }

    private void makeRandomMove(int [][] gameBoard) throws RemoteException {
        for(int i = 0; i<totalRows;i++){
            for (int j = 0; j<totalColumns;j++){
                if(gameBoard[i][j]==EMPTY){
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