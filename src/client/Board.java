package client;

import server_interface.PlayerInterface;
import server_interface.TicTacToeInterface;

import java.awt.GridLayout;
import java.rmi.RemoteException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import static Constants.GameConstants.*;

public class Board extends JPanel {

    private boolean active;
    private int playerSign;



    private int curSign;
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
        this.curSign = this.curGame.getCurSign();
    }

    public int getCurSign() {
        return curSign;
    }

//    public static void main(String[] args) {
//        JFrame jFrame = new JFrame("Tic Tac Toe Game");
//        jFrame.getContentPane().add(new Board());
//        jFrame.setBounds(500, 500, 600, 550);
//        jFrame.setVisible(true);
//        jFrame.setLocationRelativeTo(null);
//    }
}