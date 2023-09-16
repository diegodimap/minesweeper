package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {

    private static final int ROWS = 8;
    private static final int COLS = 8;
    private static final int MINES = 10;

    private static int[][] board = new int[ROWS][COLS];
    private static int[][] playBoard = new int[ROWS][COLS];

    private static List<Pair<Integer, Integer>> mines = new ArrayList<>();

    // 0 for unopened cells. -1 for mines
    static {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                board[row][col] = 0;
                playBoard[row][col] = 9;
            }
        }

        Random random = new Random();
        for (int i = 0; i < MINES; i++) {
            int row, col;
            do {
                row = random.nextInt(ROWS);
                col = random.nextInt(COLS);
            } while (board[row][col] == -1);

            board[row][col] = -1;
            mines.add(new Pair<>(row, col));
        }
    }

    // Function to count the number of mines around a cell
    private static int countMines(int row, int col) {
        int count = 0;
        for (int r = Math.max(0, row - 1); r <= Math.min(row + 1, ROWS - 1); r++) {
            for (int c = Math.max(0, col - 1); c <= Math.min(col + 1, COLS - 1); c++) {
                if (board[r][c] == -1) {
                    count++;
                }
            }
        }
        return count;
    }

    // Populate the board with the number of mines around each cell
    static {
        for (Pair<Integer, Integer> mine : mines) {
            int row = mine.getFirst();
            int col = mine.getSecond();
            for (int r = Math.max(0, row - 1); r <= Math.min(row + 1, ROWS - 1); r++) {
                for (int c = Math.max(0, col - 1); c <= Math.min(col + 1, COLS - 1); c++) {
                    if (board[r][c] != -1) {
                        board[r][c]++;
                    }
                }
            }
        }
    }

    // Function to display the game board in the console
    private static void displayBoard() {
        System.out.print("  ");
        for (int col = 0; col < COLS; col++) {
            System.out.print(col + " ");
        }
        System.out.println();

        for (int row = 0; row < ROWS; row++) {
            System.out.print(row + " ");
            for (int col = 0; col < COLS; col++) {
                if (board[row][col] == -1) {
                    System.out.print("* ");
                } else {
                    System.out.print(board[row][col] + " ");
                }
            }
            System.out.println();
        }
    }

    private static void displayPlayBoard() {
        System.out.println();
        System.out.println();
        System.out.print("  ");
        for (int col = 0; col < COLS; col++) {
            System.out.print(col + " ");
        }
        System.out.println();

        for (int row = 0; row < ROWS; row++) {
            System.out.print(row + " ");
            for (int col = 0; col < COLS; col++) {
                if (playBoard[row][col] == -1) {
                    System.out.print("* ");
                } else if (playBoard[row][col] == -2) {
                    System.out.print("  ");
                }else {
                    System.out.print(playBoard[row][col] + " ");
                }
            }
            System.out.println();
        }
    }

    private static void revealCell(int row, int col) {

        System.out.println("YOU SELECTED A " + board[row][col]);

        if(playBoard[row][col]!=9){
            System.out.println("You have already played that position!");
        }else if(board[row][col]==-1){
            System.out.println("YOU LOST");
            displayBoard();
            System.exit(0);
        }else if(doYouWin()){
            System.out.println("YOU WIN");
            System.exit(0);
        }else{
            System.out.println("CONTINUE GAME");

            playBoard[row][col] = countMines(row, col);
            //checking the surroundings
            reveal(row, col);
        }
    }

    public static void reveal(int row, int col){
        //check 8 sides
        if(row > 0 && row < 7 && col > 0 && col < 7 && board[row][col]==0){
            //top
            if(playBoard[row-1][col] == 9){
                playBoard[row-1][col] = board[row-1][col];
                if(board[row-1][col]==0) reveal(row-1, col);
            }
            //bottom
            if(playBoard[row+1][col] == 9){
                playBoard[row+1][col] = board[row+1][col];
                if(board[row+1][col]==0) reveal(row+1, col);
            }
            //left
            if(playBoard[row][col-1] == 9){
                playBoard[row][col-1] = board[row][col-1];
                if(board[row][col-1]==0) reveal(row, col-1);
            }
            //right
            if(playBoard[row][col+1] == 9){
                playBoard[row][col+1] = board[row][col+1];
                if(board[row][col+1]==0) reveal(row, col+1);
            }
            //top left
            if(playBoard[row-1][col-1] == 9){
                playBoard[row-1][col-1] = board[row-1][col-1];
                if(board[row-1][col]==0) reveal(row-1, col);
            }
            //top right
            if(playBoard[row-1][col+1] == 9){
                playBoard[row-1][col+1] = board[row-1][col+1];
                if(board[row-1][col]==0) reveal(row-1, col);
            }
            //bottom left
            if(playBoard[row+1][col-1] == 9){
                playBoard[row+1][col-1] = board[row+1][col-1];
                if(board[row-1][col]==0) reveal(row-1, col);
            }
            //bottom right
            if(playBoard[row+1][col+1] == 9){
                playBoard[row+1][col+1] = board[row+1][col+1];
                if(board[row-1][col]==0) reveal(row-1, col);
            }
        }//end 8 sides verification
    }

    public static boolean doYouWin(){
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if(playBoard[i][j]==9) return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        // Call the displayBoard function to display the initial state of the board
        displayBoard();
        Scanner read = new Scanner(System.in);

        while(true){
            displayPlayBoard();
            System.out.println("Type the ROW number you want to 'click'");
            int row = read.nextInt();
            System.out.println("Type the COLUMN number you want to 'click'");
            int col = read.nextInt();
            revealCell(row, col);
        }
        // Example of the function to be implemented, should return the board updated

    }

    // Utility class for a simple Pair implementation
    private static class Pair<K, V> {
        private final K first;
        private final V second;

        public Pair(K first, V second) {
            this.first = first;
            this.second = second;
        }

        public K getFirst() {
            return first;
        }

        public V getSecond() {
            return second;
        }
    }
}