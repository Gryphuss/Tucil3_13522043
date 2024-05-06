package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class WordLadder {

    public static void main(String[] args) {
        Set<String> wordPool = loadWordPool("word_pool3.txt");

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Select your preferred pathfinding algorithm:");
            System.out.println("1. UCS");
            System.out.println("2. GBFS");
            System.out.println("3. A*");
            System.out.println("0. Exit");

            System.out.print("Your Choice: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input.");
                scanner.next();
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 0) {
                System.out.println("BYE");
                break;
            }

            if (choice < 1 || choice > 3) {
                System.out.println("Invalid choice.");
                continue;
            }

            System.out.print("Enter start word: ");
            String startWord = scanner.nextLine().trim().toLowerCase();

            System.out.print("Enter end word: ");
            String endWord = scanner.nextLine().trim().toLowerCase();

            if (!wordPool.contains(startWord) || !wordPool.contains(endWord)) {
                System.out.println("One or both words are not in the word pool. Try again.");
                continue;
            }

            List<String> path = null;
            long tStart, tEnd;
            long tFin = 0;
            int totalNodes = 0;
            switch (choice) {
                case 1:
                    tStart = System.currentTimeMillis();
                    path = UCS.uniformCostSearch(wordPool, startWord, endWord);
                    tEnd = System.currentTimeMillis();
                    tFin = tEnd - tStart;
                    totalNodes = UCS.getUCSNodes(wordPool, startWord, endWord);
                    break;
                case 2:
                    tStart = System.currentTimeMillis();
                    path = GBFS.greedyBestFirstSearch(wordPool, startWord, endWord);
                    tEnd = System.currentTimeMillis();
                    tFin = tEnd - tStart;
                    totalNodes = GBFS.getGBFSNodes(wordPool, startWord, endWord);
                    break;
                case 3:
                    tStart = System.currentTimeMillis();
                    path = AStar.aStarSearch(wordPool, startWord, endWord);
                    tEnd = System.currentTimeMillis();
                    tFin = tEnd - tStart;
                    totalNodes = AStar.getAStarNodes(wordPool, startWord, endWord);
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
                    continue;
            }

            // Display the result
            if (path != null) {
                System.out.println("Word ladder found:");
                System.out.println(String.join("\n", path));
                System.out.println("Word ladder found with a path of " + path.size() + " step.");
                System.out.println("Total nodes visited: " + totalNodes);
                System.out.println("Time elapsed: " + tFin + " ms");

                Runtime runtime = Runtime.getRuntime();
                long finalTotalMemory = runtime.totalMemory();
                long finalFreeMemory = runtime.freeMemory();
                long finalUsedMemory = finalTotalMemory - finalFreeMemory;

                System.out.println("Memory used: " + finalUsedMemory / 1000 + " Kilo bytes");
            } else {
                System.out.println("No word ladder found.");
            }
        }

        scanner.close();
    }

    public static Set<String> loadWordPool(String filePath) {
        Set<String> wordPool = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                wordPool.add(line.trim().toLowerCase());
            }
        } catch (IOException e) {
            System.out.println("Error loading word pool: " + e.getMessage());
        }
        return wordPool;
    }
}
