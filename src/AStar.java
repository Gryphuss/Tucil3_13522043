package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class AStar {

    public static int getAStarNodes(Set<String> wordPool, String start, String goal) {
        PriorityQueue<Node> nodeQueue = new PriorityQueue<>(Comparator.comparingInt(n -> n.f));
        Map<String, Integer> nodeCosts = new HashMap<>();
        Set<String> visitedNode = new HashSet<>();
        int totalNodes = 1;

        int heuristic = heuristicss(start, goal);
        nodeQueue.add(new Node(start, new ArrayList<>(List.of(start)), 0, heuristic));

        while (!nodeQueue.isEmpty()) {
            Node current = nodeQueue.poll();

            if (current.word.equals(goal)) {
                return totalNodes;
            }

            int currentCost = current.f;

            if (!visitedNode.contains(current.word)
                    || currentCost < nodeCosts.getOrDefault(current.word, 999999)) {
                visitedNode.add(current.word);
                nodeCosts.put(current.word, currentCost);

                for (String neighbor : getNeighbors(current.word, wordPool)) {
                    int newCost = currentCost + 1;
                    int newHeuristic = heuristicss(neighbor, goal);
                    List<String> newPath = new ArrayList<>(current.path);
                    newPath.add(neighbor);

                    if (!nodeCosts.containsKey(neighbor) || newCost < nodeCosts.get(neighbor)) {
                        totalNodes += 1;
                        nodeQueue.add(new Node(neighbor, newPath, newCost, newHeuristic));
                    }
                }
            }
        }

        return totalNodes;
    }

    public static List<String> aStarSearch(Set<String> wordPool, String startWord, String endWord) {
        PriorityQueue<Node> searchQueue = new PriorityQueue<>(Comparator.comparingInt(n -> n.f));
        Map<String, Integer> nodeCosts = new HashMap<>();
        Set<String> visitedNode = new HashSet<>();

        int heuristic = heuristicss(startWord, endWord);
        searchQueue.add(new Node(startWord, new ArrayList<>(List.of(startWord)), 0, heuristic));

        while (!searchQueue.isEmpty()) {
            Node current = searchQueue.poll();

            if (current.word.equals(endWord)) {
                return current.path;
            }

            int currentCost = current.f;

            if (!visitedNode.contains(current.word)
                    || currentCost < nodeCosts.getOrDefault(current.word, 999999)) {
                visitedNode.add(current.word);
                nodeCosts.put(current.word, currentCost);

                for (String neighbor : getNeighbors(current.word, wordPool)) {
                    int newCost = currentCost + 1;
                    int newHeuristic = heuristicss(neighbor, endWord);
                    List<String> newPath = new ArrayList<>(current.path);
                    newPath.add(neighbor);

                    if (!nodeCosts.containsKey(neighbor) || newCost < nodeCosts.get(neighbor)) {
                        searchQueue.add(new Node(neighbor, newPath, newCost, newHeuristic));
                    }
                }
            }
        }

        return null;
    }

    public static int heuristicss(String word1, String word2) {
        int diff = 0;
        for (int i = 0; i < word1.length(); i++) {
            if (word1.charAt(i) != word2.charAt(i)) {
                diff++;
            }
        }
        return diff;
    }

    public static Set<String> getNeighbors(String word, Set<String> wordPool) {
        Set<String> neighbors = new HashSet<>();
        char[] chars = word.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            char originalChar = chars[i];
            for (char c = 'a'; c <= 'z'; c++) {
                if (c != originalChar) {
                    chars[i] = c;
                    String newWord = new String(chars);
                    if (wordPool.contains(newWord)) {
                        neighbors.add(newWord);
                    }
                }
            }
            chars[i] = originalChar;
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
        int g;
        int h;
        int f;

        Node(String word, List<String> path, int g, int h) {
            this.word = word;
            this.path = path;
            this.g = g;
            this.h = h;
            this.f = g + h;
        }
    }
}
