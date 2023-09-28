package server;

import server_interface.TicTacToeInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;



public class TicTacToe extends UnicastRemoteObject implements TicTacToeInterface {
    private static final int CONTINUE = 0;
    private static final int INVALID = -1;
    private static final int DRAW = 3;
    private static final int X = 1;
    private static final int O = 2;
    private static final int EMPTY = 0;
    private int playerSign = X;
//    private int totalCells = 9;
    private final int totalRows = 3;
    private final int totalColumns = 3;

    private int [][] gameBoard;
    public TicTacToe() throws RemoteException {
        super();
        this.gameBoard = new int[totalRows][totalColumns];
    }

    @Override
    public int makeAMove(int sign, int row, int column){
        if (sign!=this.playerSign){
            return INVALID;
        }
        if (this.gameBoard[row][column]!=EMPTY){
            return INVALID;
        }
        this.gameBoard[row][column]=sign;
        if(checkForWinner()){
            return this.playerSign;
        }else if(gameDraw()){
            return DRAW;
        }
        changeSign();

        return CONTINUE;
    }

    @Override
    public int[][] getGameBoard() throws RemoteException {
        return this.gameBoard;
    }

    private void changeSign(){
        if(this.playerSign==X){
            this.playerSign=O;
        }else {
            this.playerSign=X;
        }
    }

    private boolean checkForWinner() {
        return checkAllRows() || checkAllColumns() || checkTheDiagonals();
    }
    private boolean checkAllRows() {
        if(this.gameBoard[0][0]==X&&this.gameBoard[0][1]==X&&this.gameBoard[0][2]==X){
            return true;
        }else if (this.gameBoard[1][0]==X&&this.gameBoard[1][1]==X&&this.gameBoard[1][2]==X){
            return true;
        }else if (this.gameBoard[2][0]==X&&this.gameBoard[2][1]==X&&this.gameBoard[2][2]==X){
            return true;
        }else if (this.gameBoard[0][0]==O&&this.gameBoard[0][1]==O&&this.gameBoard[0][2]==O){
            return true;
        }else if (this.gameBoard[1][0]==O&&this.gameBoard[1][1]==O&&this.gameBoard[1][2]==O){
            return true;
        }else return this.gameBoard[2][0] == O && this.gameBoard[2][1] == O && this.gameBoard[2][2] == O;
    }
    private boolean checkAllColumns() {
        if(this.gameBoard[0][0]==X&&this.gameBoard[1][0]==X&&this.gameBoard[2][0]==X){
            return true;
        }else if (this.gameBoard[0][1]==X&&this.gameBoard[1][1]==X&&this.gameBoard[2][1]==X){
            return true;
        }else if (this.gameBoard[0][2]==X&&this.gameBoard[1][2]==X&&this.gameBoard[2][2]==X){
            return true;
        }else if (this.gameBoard[0][0]==O&&this.gameBoard[1][0]==O&&this.gameBoard[2][0]==O){
            return true;
        }else if (this.gameBoard[0][1]==O&&this.gameBoard[1][1]==O&&this.gameBoard[2][1]==O){
            return true;
        }else return this.gameBoard[0][2] == O && this.gameBoard[1][2] == O && this.gameBoard[2][2] == O;
    }
    private boolean checkTheDiagonals() {
        if(this.gameBoard[0][0]==X&&this.gameBoard[1][1]==X&&this.gameBoard[2][2]==X){
            return true;
        }else if (this.gameBoard[0][2]==X&&this.gameBoard[1][1]==X&&this.gameBoard[2][0]==X){
            return true;
        }else if (this.gameBoard[0][0]==O&&this.gameBoard[1][1]==O&&this.gameBoard[2][2]==O){
            return true;
        }else return this.gameBoard[0][2] == O && this.gameBoard[1][1] == O && this.gameBoard[2][0] == O;
    }
    private boolean gameDraw(){
        boolean draw = true;
        for (int i = 0; i<totalRows;i++){
            for(int j=0; j<totalColumns;j++){
                if (this.gameBoard[i][j] == EMPTY) {
                    draw = false;
                    break;
                }
            }
        }
        return draw;
    }

    @Override
    public String hello() {
        return "hello";
    }
}
