/**
 *
 * @author hagop_000
 */
import java.util.*;

public class Genetic {

    private int n1;
    private List<NQueen> pop;
    private int[] popChance;
    private int popSize;
    private Random RAND = new Random();
    private boolean isBatchTest = false;

    public Genetic(int n, int initPopSize) {
        n1 = n;
        pop = new ArrayList<>();
        popChance = new int[initPopSize];
        popSize = initPopSize;

        for (int i = 0; i < initPopSize; ++i) {
            NQueen child = new NQueen(n);
            child.genBoard();
            pop.add(child);
        }
    }

    public void setBatchTest(boolean isBatchTest) {
        this.isBatchTest = isBatchTest;
    }

    class FitnessComparator implements Comparator<NQueen> {
        @Override
        public int compare(NQueen q1, NQueen q2) {
            return q1.getnonAttack() - q2.getnonAttack();
        }
    }


    private void Fitness() {
        Collections.sort(pop, new FitnessComparator());
        int sum = 0;
        int i = 0;
        for (NQueen child : pop) {
            sum += child.getnonAttack() + 1;
            popChance[i] = sum;
            ++i;
        }
    }

    private List<NQueen> randomSelect() {
        int sum = popChance[popSize-1];

        int parent1 = 0;
        int rand = RAND.nextInt(sum);
        for (; parent1 < popSize; ++parent1) {
            if (rand < popChance[parent1]) {
                break;
            }
        }
        int parent2 = parent1;
        while (parent2 == parent1) {
            rand = RAND.nextInt(sum);
            for (parent2 = 0; parent2 < popSize; ++parent2) {
                if (rand < popChance[parent2]) {
                    break;
                }
            }
        }
        List<NQueen> parents = new ArrayList<>();
        parents.add(pop.get(parent1));
        parents.add(pop.get(parent2));
        
        return parents;
    }

    private List<NQueen> reproduce(List<NQueen> parents) {
        int[] dna1 = parents.get(0).getColumnMark().clone();
        int[] dna2 = parents.get(1).getColumnMark().clone();

        int splice = RAND.nextInt(n1);
        int start, end;
        if (RAND.nextBoolean()) {
            start = 0;
            end = splice + 1;
        } else {
            start = splice;
            end = n1;
        }

        for (int i = start; i < end; ++i) {
            int temp = dna1[i];
            dna1[i] = dna2[i];
            dna2[i] = temp;
        }

        List<NQueen> children = new ArrayList<>();
        children.add(new NQueen(dna1));
        children.add(new NQueen(dna2));

        return children;
    }

    private NQueen mutate(NQueen child, int mutationRate) {
        if (RAND.nextInt(mutationRate) < 1) {
            int[] dna = child.getColumnMark().clone();
            dna[RAND.nextInt(n1)] = RAND.nextInt(n1);
            child.setColumnMark(dna);
        }
        return child;
    }

    public int breed(int maxGeneration) {
        int generation = 0;
        int maxNonAttacks = 0;
        while (generation < maxGeneration) {
            Fitness();
            List<NQueen> newPopulation = new ArrayList<>();

            for (int pair = 0; pair < popSize / 2; ++pair) {
                List<NQueen> parents = randomSelect();
                List<NQueen> children = reproduce(parents);
                newPopulation.addAll(children);
            }

            for (NQueen child : newPopulation) {
                child = mutate(child, 10);

                if (child.getnonAttack() > maxNonAttacks) {
                    maxNonAttacks = child.getnonAttack();
                }

                if (child.goalState()) {
                    if (!isBatchTest) {
                        child.printBoard();
                    }
                    return generation;
                }
            }

            pop = newPopulation;
            popSize = newPopulation.size();
            popChance = new int[popSize];
            ++generation;
            if (!isBatchTest && generation % 10000 == 0) {
                System.out.println(generation + " Generation: current non-attack queens = " + maxNonAttacks);
            }
        }
        return generation;
    }

    public static void run() {
        int maxGeneration = 10000;
        int maxRound = 2;

        String choice;
        System.out.println("(Genetic)Select mode: ");
        System.out.println("(a)One run");
        System.out.println("(b)Batch run");
        Scanner keyboard = new Scanner(System.in);
        choice = keyboard.nextLine();
        if (choice.equals("a") || choice.equals("A")) {
            while (!choice.equals("x") && !choice.equals("X")) {
                System.out.println("(Genetic)Number of Queens: ");
                keyboard = new Scanner(System.in);
                int n = Integer.valueOf(keyboard.nextLine());
                System.out.println("(Genetic)Size of population (even number): ");
                keyboard = new Scanner(System.in);
                int populationSize = Integer.valueOf(keyboard.nextLine());
                Genetic ga = new Genetic(n, populationSize);
                int generation = ga.breed(maxGeneration);

                if (generation < maxGeneration) {
                    System.out.printf("Goal reached at %d generation.\n", generation); 
                } else {
                    System.out.println("Failed to find a goal.");
                }
                System.out.println("(Genetic)X to exit Genetic, else continue...");
                keyboard = new Scanner(System.in);
                choice = keyboard.nextLine();
            }
        } else if (choice.equals("b") || choice.equals("B")) {
            System.out.println("(Genetic)Number of Queens: ");
            keyboard = new Scanner(System.in);
            int n = Integer.valueOf(keyboard.nextLine());

            for (; n >= 4; --n) {
                long totalGeneration = 0;
                System.out.println("Number of queens: " + n);
                for (int round = 0; round < maxRound; ++round) {
                    Genetic ga = new Genetic(n, 4);
                    ga.setBatchTest(true);
                    int generation = ga.breed(maxGeneration);
                    
                    if (generation < maxGeneration) {
                        System.out.printf("Goal reached at %d generation(s).\n", generation);
                    } else { 
                        System.out.println("Couldn't find goal.");
                    }
                    totalGeneration += generation;
                }

                System.out.println("number of queens = " + n + ", round = " + maxRound
                        + ", average generation = " + totalGeneration / maxRound);

                maxRound *= 2;
            }
        }

    }

}