package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class GBFS {

    public static int getGBFSNodes(Set<String> wordPool, String start, String goal) {
        PriorityQueue<Node> nodeQueue = new PriorityQueue<>(Comparator.comparingInt(n -> n.heuristic));
        Set<String> visitedNode = new HashSet<>();
        Map<String, String> nodeOrigin = new HashMap<>();
        int totalNodes = 1;

        nodeQueue.add(new Node(start, heuristic(start, goal)));

        while (!nodeQueue.isEmpty()) {
            Node current = nodeQueue.poll();
            nodeQueue.clear();
            if (current.word.equals(goal)) {
                return totalNodes;
            }

            if (!visitedNode.contains(current.word)) {
                visitedNode.add(current.word);
                for (String neighbor : getNeighbors(current.word, wordPool)) {
                    if (!visitedNode.contains(neighbor)) {
                        totalNodes += 1;
                        nodeOrigin.put(neighbor, current.word);
                        nodeQueue.add(new Node(neighbor, heuristic(neighbor, goal)));
                    }
                }
            }
        }

        return totalNodes;
    }

    public static List<String> greedyBestFirstSearch(Set<String> wordPool, String startWord, String goalWord) {
        // Queue<Node> nodeQueue = new LinkedList<>();
        PriorityQueue<Node> nodeQueue = new PriorityQueue<>(Comparator.comparingInt(n -> n.heuristic));
        Set<String> visitedNode = new HashSet<>();
        Map<String, String> nodeOrigin = new HashMap<>();

        nodeQueue.add(new Node(startWord, heuristic(startWord, goalWord)));

        while (!nodeQueue.isEmpty()) {
            Node current = nodeQueue.poll();
            nodeQueue.clear();
            if (current.word.equals(goalWord)) {
                return whatsPath(nodeOrigin, current.word);
            }

            if (!visitedNode.contains(current.word)) {
                visitedNode.add(current.word);
                for (String neighbor : getNeighbors(current.word, wordPool)) {
                    if (!visitedNode.contains(neighbor)) {
                        nodeOrigin.put(neighbor, current.word);
                        nodeQueue.add(new Node(neighbor, heuristic(neighbor, goalWord)));
                    }
                }
            }
        }

        return null;
    }

    public static int heuristic(String word1, String word2) {
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
            e.printStackTrace();
        }
        return wordPool;
    }

    public static List<String> whatsPath(Map<String, String> cameFrom, String current) {
        List<String> path = new ArrayList<>();
        while (current != null) {
            path.add(current);
            current = cameFrom.get(current);
        }
        Collections.reverse(path);
        return path;
    }

    static class Node {
        String word;
        int heuristic;

        Node(String word, int heuristic) {
            this.word = word;
            this.heuristic = heuristic;
        }
    }
}
