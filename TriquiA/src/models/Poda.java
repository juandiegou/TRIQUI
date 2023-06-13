package models;

public class Poda {

    private int SIZE;

    public Poda(int SIZE){
        this.SIZE = SIZE;
    }


    private static final int[][] WIN_PATTERNS = {
        {0, 1, 2},  // Filas
        {3, 4, 5},
        {6, 7, 8},
        {0, 3, 6},  // Columnas
        {1, 4, 7},
        {2, 5, 8},
        {0, 4, 8},  // Diagonales
        {2, 4, 6}
    };

     public int[] minimax(int[][] board, int currentPlayer) {
        int[] bestMove = new int[2];
        int bestScore = Integer.MIN_VALUE;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        for (int i = 0; i < this.SIZE; i++) {
            for (int j = 0; j < this.SIZE; j++) {
                if (board[i][j] == 0) {
                    board[i][j] = currentPlayer;
                    int score = minimaxRecursive(board, 0, false, currentPlayer, alpha, beta);
                    board[i][j] = 0;

                    if (score > bestScore) {
                        bestScore = score;
                        bestMove[0] = i;
                        bestMove[1] = j;
                    }

                    alpha = Math.max(alpha, bestScore);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
        }

        return bestMove;
    }

    public int minimaxRecursive(int[][] board, int depth, boolean isMaximizingPlayer, int currentPlayer, int alpha, int beta) {
        int score = evaluate(board);

        if (score == 10 || score == -10 || depth == this.SIZE * this.SIZE) {
            return score;
        }

        if (isMaximizingPlayer) {
            int bestScore = Integer.MIN_VALUE;

            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (board[i][j] == 0) {
                        board[i][j] = currentPlayer;
                        int currentScore = minimaxRecursive(board, depth + 1, false, currentPlayer == 1 ? -1 : 1, alpha, beta);
                        board[i][j] = 0;
                        bestScore = Math.max(bestScore, currentScore);
                        alpha = Math.max(alpha, bestScore);
                        if (beta <= alpha) {
                            break;
                        }
                    }
                }
            }

            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;

            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (board[i][j] == 0) {
                        board[i][j] = currentPlayer;
                        int currentScore = minimaxRecursive(board, depth + 1, true, currentPlayer == 1 ? -1 : 1, alpha, beta);
                        board[i][j] = 0;
                        bestScore = Math.min(bestScore, currentScore);
                        beta = Math.min(beta, bestScore);
                        if (beta <= alpha) {
                            break;
                        }
                    }
                }
            }

            return bestScore;
        }
    }

    public int evaluate(int[][] board) {
        for (int[] pattern : WIN_PATTERNS) {
            int sum = 0;
            boolean isEmptyCell = false;
            
            for (int i : pattern) {
                int row = i / this.SIZE;
                int col = i % this.SIZE;
                int cell = board[row][col];
                
                sum += cell;
                
                if (cell == 0) {
                    isEmptyCell = true;
                }
            }
            
            if (!isEmptyCell && (sum == SIZE || sum == -SIZE)) {
                if (sum == SIZE) {
                    return 10;   // Jugador 2 (Max) ha ganado
                } else {
                    return -10;  // Jugador 1 (Min) ha ganado
                }
            }
        }

        return 0;  // Empate
    }

}
