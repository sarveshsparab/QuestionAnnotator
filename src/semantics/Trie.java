package semantics;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import commandcentral.Config;
import semantics.dictionary.TrieNode;

public class Trie {
	
	private TrieNode root;
	 
    public Trie() {
        root = new TrieNode();
    }
    //populating the dictionary
    public void buildDict() throws IOException {
    	String filepath=Config.wordsListPath;
    	FileInputStream fstream = new FileInputStream(filepath);
    	BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
    	String strLine;
    	
    	while ((strLine = br.readLine()) != null){
    		HashMap<Character, TrieNode> children = root.getChildren();
            for(int i=0; i<strLine.length(); i++){
                char c = strLine.charAt(i);
                TrieNode trieNode;
                if(children.containsKey(c)){
                	trieNode = children.get(c);
                }else{
                	trieNode = new TrieNode(c);
                    children.put(c, trieNode);
                }
                children = trieNode.getChildren();
                if(i==strLine.length()-1) //set leaf node
                	trieNode.setLeaf(true);    
            }
    	  //System.out.println (strLine);
    	}
    	br.close();        
    }
 
    // Returns if the word is in the trie.
    public boolean isEnglishWord(String word) {
        TrieNode trieNode = searchNode(word);
        if(trieNode != null && trieNode.isLeaf()) 
            return true;
        else
            return false;
    }
    // Returns if there is any word in the trie
    // that starts with the given prefix.
    @SuppressWarnings("unused")
	private boolean startsWith(String prefix) {
        if(searchNode(prefix) == null) 
            return false;
        else
            return true;
    }
    private TrieNode searchNode(String str){
        Map<Character, TrieNode> children = root.getChildren(); 
        TrieNode trieNode = null;
        for(int i=0; i<str.length(); i++){
            char c = str.charAt(i);
            if(children.containsKey(c)){
            	trieNode = children.get(c);
                children = trieNode.getChildren();
            }else{
                return null;
            }
        }
        return trieNode;
    }

}


