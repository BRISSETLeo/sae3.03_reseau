package client.lexicographie;

import java.util.HashMap;
import java.util.Map;

public class TrieNode {

    private Map<Character, TrieNode> children;
    boolean isEndOfWord;

    public TrieNode() {
        this.children = new HashMap<>();
        this.isEndOfWord = false;
    }

    public Map<Character, TrieNode> getChildren() {
        return this.children;
    }
    
}