package models;

import java.io.Serializable;

public class Board implements Serializable {
    public char[][] game = new char[3][3];
    public int[][] cost = new int[3][3];;

    public Board() {
    }

    public Board(char[][] game, int [][] cost){
        this.game = game;
        this.cost = cost;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    /**
     * @param i    posición de la fila de la matriz
     * @param j    posición de la columna de ka matriz
     * @param type tipo de marca que se realizará en la matriz O ó X
     */
    public void setMark(int i, int j, char type) {
        if (game[i][j] == (char) 0) {
            game[i][j] = type;
            if (type == 'X') {
                cost[i][j] = 1;
                game[i][j] = 'X';
            }
            if (type == 'O') {
                cost[i][j] = -1;
                game[i][j] = 'O';
            }
        }
    }

    public void unSetMark(int i, int j) {
        if (game[i][j] != (char) 0) {
            game[i][j] = (char) 0;
            cost[i][j] = 0;
        }
    }

    public boolean checkBoard() {
        return verifyColums() | verifyRows() | verifyDiagonals();

    }

    public boolean verifyColums() {
        for (int i = 0; i < game.length; i++) {
            if (verifyColum(i))
                return true;
        }
        return false;
    }

    public boolean verifyRows() {
        for (int i = 0; i < game.length; i++) {
            if (verifyRow(i))
                return true;
        }
        return false;

    }

    public boolean verifyDiagonals() {
        int countX = 0;
        int countO = 0;
        for (int i = 0; i < game.length; i++) {
            if (game[i][i] == 'X') {
                countX++;
            }
            if (game[i][i] == 'O') {
                countO++;
            }
        }

        if(game[0][2] == 'X' && game [1][1] == 'X' && game[2][0]=='X'){
            return true;
        }
        if(game[0][2] == 'O' && game [1][1] == 'O' && game[2][0]=='O'){
            return true;
        }

        return countX == 3 | countO == 3;
    }

    private boolean verifyRow(int index) {
        int countX = 0;
        int countO = 0;
        for (int i = 0; i < game[index].length; i++) {
            if (game[index][i] == 'X') {
                countX++;
            }
            if (game[index][i] == 'O') {
                countO++;
            }
        }
        return countX == 3 | countO == 3;
    }

    private boolean verifyColum(int index) {
        int countX = 0;
        int countO = 0;
        for (int i = 0; i < game.length; i++) {
            if (game[i][index] == 'X') {
                countX++;
            }
            if (game[i][index] == 'O') {
                countO++;
            }
        }
        return countX == 3 | countO == 3;
    }

    /**
     * @param i una posicion de fila
     * @param J una posicion de columna
     * @return si se encuentra vacia o no
     */
    public boolean validateMark(int i, int j) {
        return this.game[i][j] == '\u0000';
    }

    public int getRowSpace(int row) {
        int space = -1;
        int i = 0;
        while (i < this.cost.length) {
            if (this.cost[row][i] == 0) {
                return i;
            }
            i++;
        }
        return space;
    }

    public int getColSpace(int col) {
        int space = -1;
        int i = 0;
        while (i < this.cost.length) {
            if (this.cost[i][col] == 0) {
                return i;
            }
            i++;
        }
        return space;
    }

    private void printBoard() {
        System.out.println("\n-------------------------------------------------");
        for (char[] element : this.game) {
            for (char value : element) {
                System.out.print("|\t" + value + "\t|");
            }
            System.out.println("\n-------------------------------------------------");
        }
    }

}
