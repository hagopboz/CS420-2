/**
 *
 * @author hagop_000
 */
import java.util.*;

public class NQueen {

    private int[][] board;
    private int m;
    private int[] column;   
    private int nonAttack = 0;
    private final Random RAND = new Random();

    public NQueen(int n) {
        board = new int[n][n];
        column = new int[n];
        m = n;
    }

    public NQueen(int[] columnMark) {
        m = columnMark.length;
        board = new int[m][m];
        column = columnMark.clone();

        genBoard(columnMark);
    }

    public NQueen() {
        board = new int[8][8];
        column = new int[8];
        m = 8;
    }

    public int getnonAttack() {
        return nonAttack;
    }

    public int[] getColumnMark() {
        return column;
    }

    public void setColumnMark(int[] columnMark) {
    	genBoard(columnMark);
    }

    public void printBoard() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < m; ++j) {
                sb.append(board[i][j]);
                sb.append(" ");
            }
            sb.append("\n");
        }
        System.out.println(sb.toString());
    }

    public void genBoard() {
    	clearBoard();
        int[] col = new int[m];

        for (int nQueen = 0; nQueen < m; ++nQueen) {
            int i = RAND.nextInt(m);
            int j = RAND.nextInt(m);
            if (col[j] == 0) {
                board[i][j] = 1;
                column[j] = i;
                ++col[j];
            }
            else {
                --nQueen;
            }
        }
        nonAttack = countNonAttacks();
    }

    public void genBoard(int[] columnMark) {
    	clearBoard();
        column = columnMark;

        for (int idx = 0; idx < m; ++idx) {
            int i = columnMark[idx];
            int j = idx;
            board[i][j] = 1;
        }
        nonAttack = countNonAttacks();
    }

    public void clearBoard() {
        for (int i = 0; i < m; ++i) {
            column[i] = 0;
            for (int j = 0; j < m; ++j)
                board[i][j] = 0;
        }
        nonAttack = 0;
    }

    private int countLineAttacks(int[] line) {
        int numOfAttacks = 0;
        for (int elm : line)
            if (elm > 1) {
                numOfAttacks += elm;
            }
        return numOfAttacks;
    }

    public int countAttacks() {
        int[] row = new int[m];
        int[] col = new int[m];
        int[] dia1 = new int[2 * m - 1];
        int[] dia2 = new int[2 * m - 1];

        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < m; ++j) {
                if (board[i][j] != 0) {
                    ++row[i];
                    ++col[j];
                    ++dia1[i + j];
                    ++dia2[m - 1 + j - i];
                }
            }
        }
        int numOfAttacks = countLineAttacks(row);
        numOfAttacks += countLineAttacks(col);
        numOfAttacks += countLineAttacks(dia1);
        numOfAttacks += countLineAttacks(dia2);
        
        return numOfAttacks;
    }

    public int countNonAttacks() {
        int nonAttack = 0;
        for (int j1 = 0; j1 < m; ++j1) {
            int numOfAttacks = -1;
            int i1 = column[j1];
            for (int j2 = 0; j2 < m; ++j2 ) {
                int i2 = column[j2];
                if ((i1 == i2) || (i1 + j1 == i2 + j2) || (i1 - j1 == i2 - j2)) {
                    ++numOfAttacks;
                }
            }
            if (numOfAttacks == 0) {
                ++nonAttack;
            }
        }
        return nonAttack;
    }

    public boolean goalState() {
        return (countNonAttacks() == m);
    }

    public static void main(String[] args) {
        String choice = "";

        while (!choice.equals("x") && !choice.equals("X")) {
            System.out.println("(a)Hill Climbing Algorithm");
            System.out.println("(b)Genetic Algorithm");
            Scanner keyboard = new Scanner(System.in);
            choice = keyboard.nextLine();

            if (choice.equals("a") || choice.equals("A")) {
                HillClimbing.run();
            } else if (choice.equals("b") || choice.equals("B")) {
                Genetic.run();
            }

            System.out.println("Enter x to exit, Else continue: ");
            keyboard = new Scanner(System.in);
            choice = keyboard.nextLine();
        }
    }
}
