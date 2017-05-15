/**
 *
 * @author hagop_000
 */
import java.util.*;

public class HillClimbing {

    private int n1;
    private NQueen problem;

    private boolean isBatchTest = false;

    public HillClimbing(int n) {
        n1 = n;

        problem = new NQueen(n);
        problem.genBoard();
    }

    public void setBatchTest(boolean isBatchTest) {
        this.isBatchTest = isBatchTest;
    }

    public boolean solve() {
        boolean foundNextStep = true;
        int currentNonAttacks = 0;
        NQueen currentState = null;

        while (!problem.goalState() && foundNextStep) {
            foundNextStep = false;
            int[] columnMark = problem.getColumnMark();

            for (int j = 0; j < n1; ++j) {
                for (int i = 0; i < n1; ++i) {
                    if (i != columnMark[j]) {
                        int[] possibleMove = columnMark.clone();
                        possibleMove[j] = i;

                        NQueen possibleState = new NQueen(possibleMove);

                        int possibleNonAttacks = possibleState.countNonAttacks();

                        if (possibleNonAttacks > currentNonAttacks) {
                            currentNonAttacks = possibleNonAttacks;
                            currentState = possibleState;

                            foundNextStep = true;
                        }
                    }
                }
            }

            problem = currentState;
        }

        if (!isBatchTest)
            problem.printBoard();

        if (problem.goalState())
            return true;
        else
            return false;
    }

    public static void run() {
        String choice;
        System.out.println("(Hill-Climbing)Select mode: ");
        System.out.println("(a)One run");
        System.out.println("(b)Batch run");
        Scanner keyboard = new Scanner(System.in);
        choice = keyboard.nextLine();
        if (choice.equals("a") || choice.equals("A")) {
            while (!choice.equals("x") && !choice.equals("X")) {
                System.out.println("(Hill-CLimbing)Please specify the number of queens:");

                keyboard = new Scanner(System.in);
                int n = Integer.valueOf(keyboard.nextLine());

                HillClimbing hc = new HillClimbing(n);
                if (hc.solve())
                    System.out.println("Problem solved");
                else
                    System.out.println("Failed to solve problem, try again");

                System.out.println("(Hill-Climbing)X to exit, else continue...");
                keyboard = new Scanner(System.in);
                choice = keyboard.nextLine();
            }

        } else if (choice.equals("b") || choice.equals("B")) {
            System.out.println("(Hill-Climbing)Please specify the number of queens:");

            keyboard = new Scanner(System.in);
            int n = Integer.valueOf(keyboard.nextLine());

            int numOfSolved = 0;
            for (int i = 0; i < 100; ++i) {
                HillClimbing hc = new HillClimbing(n);
                hc.setBatchTest(true);
                if (hc.solve())
                    ++numOfSolved;
            }

            System.out.println(numOfSolved + " out of 100 solved.");
        }
    }
}