package server;

import server_interface.TicTacToeInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import static Constants.GameConstants.*;


public class TicTacToe extends UnicastRemoteObject implements TicTacToeInterface {
//    private static final int CONTINUE = 0;
//    private static final int INVALID = -1;
//    private static final int DRAW = 3;
//    private static final int X = 1;
//    private static final int O = 2;
//    private static final int EMPTY = 0;
    private int curSign = X;

    private int roundNumber = 1;
//    private int totalCells = 9;
    private final int totalRows = 3;
    private final int totalColumns = 3;
    private int gameStatus;
    private final int [][] gameBoard;

    private Player playerX;
    private Player playerO;

    private String winner;
    public TicTacToe() throws RemoteException {
        super();
        this.gameBoard = new int[totalRows][totalColumns];
        this.gameStatus = RUNNING;
        this.winner = UNKNOWN;
    }

    private void changeSign(){
        if(this.curSign == X){
            this.curSign = O;
        }else {
            this.curSign = X;
        }
    }

    private boolean checkForWinner() {
        return checkAllRows() || checkAllColumns() || checkTheDiagonals();
    }
    private boolean checkAllRows() {
        if(this.gameBoard[0][0]== X&&this.gameBoard[0][1]== X&&this.gameBoard[0][2]== X){
            return true;
        }else if (this.gameBoard[1][0]== X&&this.gameBoard[1][1]== X&&this.gameBoard[1][2]== X){
            return true;
        }else if (this.gameBoard[2][0]== X&&this.gameBoard[2][1]== X&&this.gameBoard[2][2]== X){
            return true;
        }else if (this.gameBoard[0][0]== O&&this.gameBoard[0][1]== O&&this.gameBoard[0][2]== O){
            return true;
        }else if (this.gameBoard[1][0]== O&&this.gameBoard[1][1]== O&&this.gameBoard[1][2]== O){
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

    public void setGameStatus(int gameStatus) {
        this.gameStatus = gameStatus;
    }

    public void surrender(int sign){
        this.gameStatus = FINISHED;
        if(sign == X){
            this.winner = playerO.getName();
        }else {
            this.winner = playerX.getName();
        }
    }
    public void unexpectedDraw(){
        this.gameStatus = FINISHED;
        this.winner = UNEXPECTED_DRAW;
    }

    public Player getOpponent(int sign){
        if(sign==X){
            return this.playerO;
        }else {
            return this.playerX;
        }
    }
    @Override
    public synchronized void makeAMove(int sign, int row, int column){
        if (sign==this.curSign && this.gameBoard[row][column] == EMPTY&&this.gameStatus==RUNNING){
            this.gameBoard[row][column]=sign;
            if(checkForWinner()){
                this.gameStatus = FINISHED;
                if(curSign == X){
                    this.winner = playerX.getName();
                }else {
                    this.winner = playerO.getName();
                }
//                this.winner = this.curSign;
            }else if(gameDraw()){
                this.gameStatus = FINISHED;
                this.winner = DRAW;
            }else {
                System.out.println("change sign");
                changeSign();
                roundNumber++;
            }
        }
        for (int i =0;i<totalRows;i++){
            for (int j = 0;j<totalColumns;j++){
                System.out.print(this.gameBoard[i][j]);
            }
            System.out.println();
        }
        System.out.println("--------------------");


    }

    @Override
    public int[][] getGameBoard() throws RemoteException {
        return this.gameBoard;
    }

    @Override
    public int getGameStatus() {
        return gameStatus;
    }

    @Override
    public String getWinner() {
        return winner;
    }


    @Override
    public int getCurSign() {
        return curSign;
    }

    @Override
    public String hello() {
        return "hello";
    }

    public void setPlayerX(Player playerX) {
        this.playerX = playerX;
    }

    @Override
    public int getRoundNumber() {
        return roundNumber;
    }

    public void setPlayerO(Player playerO) {
        this.playerO = playerO;
    }
}
