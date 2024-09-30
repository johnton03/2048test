package com.example.game2048;

import java.util.Random;

public class GameUtils {

    public static void addRandomTile(int[][] board) {
        Random rand = new Random();
        int row, col;
        do {
            row = rand.nextInt(board.length);
            col = rand.nextInt(board[0].length);
        } while (board[row][col] != 0);
        board[row][col] = rand.nextInt(10) == 0 ? 4 : 2;
    }

    public static boolean checkGameOver(int[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 0) {
                    return false;
                }
                if (i < board.length - 1 && board[i][j] == board[i + 1][j]) {
                    return false;
                }
                if (j < board[i].length - 1 && board[i][j] == board[i][j + 1]) {
                    return false;
                }
            }
        }
        return true;
    }
}
