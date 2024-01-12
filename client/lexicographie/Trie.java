package client.lexicographie;

import java.util.Map;

public class Trie {

    private TrieNode root;

    public Trie() {
        this.root = new TrieNode();
    }

    public void insert(String word) {
        TrieNode current = root;
        for (char ch : word.toLowerCase().toCharArray()) {
            current.getChildren().putIfAbsent(ch, new TrieNode());
            current = current.getChildren().get(ch);
        }
        current.isEndOfWord = true;
    }

    public boolean search(String word) {
        TrieNode current = root;
        for (char ch : word.toLowerCase().toCharArray()) {
            if (!current.getChildren().containsKey(ch)) {
                return false;
            }
            current = current.getChildren().get(ch);
        }
        return current.isEndOfWord;
    }

    public void findAllWordsWithPrefix(String prefix, TrieNode node, StringBuilder currentWord,
            String[] result) {
        if (node.isEndOfWord) {
            result[0] += currentWord.toString() + " ";
        }

        for (Map.Entry<Character, TrieNode> entry : node.getChildren().entrySet()) {
            char nextChar = entry.getKey();
            TrieNode nextNode = entry.getValue();

            findAllWordsWithPrefix(prefix + nextChar, nextNode,
                    new StringBuilder(currentWord).append(nextChar), result);
        }
    }

    public String findAllWordsWithPrefix(String prefix) {
        TrieNode node = root;
        StringBuilder currentWord = new StringBuilder();
        String[] result = { "" };

        for (char ch : prefix.toCharArray()) {
            if (!node.getChildren().containsKey(ch)) {
                return result[0];
            }
            currentWord.append(ch);
            node = node.getChildren().get(ch);
        }

        findAllWordsWithPrefix(prefix, node, currentWord, result);
        return result[0];
    }
}
