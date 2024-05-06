package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class UCS {

    public static int getUCSNodes(Set<String> wordPool, String start, String goal) {
        PriorityQueue<Node> searchQueue = new PriorityQueue<>(Comparator.comparingInt(n -> n.cost));
        Set<String> visitedNode = new HashSet<>();
        int totalNodes = 1;

        searchQueue.add(new Node(start, new ArrayList<>(List.of(start)), 0));

        while (!searchQueue.isEmpty()) {
            Node current = searchQueue.poll();

            if (current.word.equals(goal)) {
                return totalNodes;
            }

            if (!visitedNode.contains(current.word)) {
                visitedNode.add(current.word);

                for (String neighbor : getNeighbors(current.word, wordPool)) {
                    if (!visitedNode.contains(neighbor)) {
                        totalNodes += 1;
                        List<String> newPath = new ArrayList<>(current.path);
                        newPath.add(neighbor);
                        searchQueue.add(new Node(neighbor, newPath, current.cost + 1));
                    }
                }
            }
        }

        return totalNodes; // No path found
    }

    public static List<String> uniformCostSearch(Set<String> wordPool, String startWord, String endWord) {
        PriorityQueue<Node> searchQueue = new PriorityQueue<>(Comparator.comparingInt(n -> n.cost));
        Set<String> visitedNode = new HashSet<>();

        searchQueue.add(new Node(startWord, new ArrayList<>(List.of(startWord)), 0));

        while (!searchQueue.isEmpty()) {
            Node current = searchQueue.poll();

            if (current.word.equals(endWord)) {
                return current.path;
            }

            if (!visitedNode.contains(current.word)) {
                visitedNode.add(current.word);

                for (String neighbor : getNeighbors(current.word, wordPool)) {
                    if (!visitedNode.contains(neighbor)) {
                        List<String> newPath = new ArrayList<>(current.path);
                        newPath.add(neighbor);
                        searchQueue.add(new Node(neighbor, newPath, current.cost + 1));
                    }
                }
            }
        }

        return null;
    }

    public static Set<String> getNeighbors(String word, Set<String> wordPool) {
        Set<String> neighbors = new HashSet<>();
        char[] chars = word.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            char ori = chars[i];
            for (char c = 'a'; c <= 'z'; c++) {
                if (c != ori) {
                    chars[i] = c;
                    String newW = new String(chars);
                    if (wordPool.contains(newW)) {
                        neighbors.add(newW);
                    }
                }
            }
            chars[i] = ori;
        }

        return neighbors;
    }

    public static Set<String> loadWordPool(String filePath) {
        Set<String> wordPool = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                wordPool.add(line.trim());
            }
        } catch (IOException e) {
            // e.printStackace();
        }
        return wordPool;
    }

    static class Node {
        String word;
        List<String> path;
        int cost;

        Node(String word, List<String> path, int cost) {
            this.word = word;
            this.path = path;
            this.cost = cost;
        }
    }
}
