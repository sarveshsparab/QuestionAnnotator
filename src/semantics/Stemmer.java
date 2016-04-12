package semantics;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.tartarus.snowball.ext.PorterStemmer;

public class Stemmer {
	private int verbose;
	private static Helper helper = new Helper();
	public Stemmer(int verbose){
		this.verbose = verbose;
	}
	private String SnowBallStemmer(String word) { //Snowball stemmer (Porter 2)
	    PorterStemmer state = new PorterStemmer(); 
	    state.setCurrent(word); 
	    state.stem(); 
	    return state.getCurrent(); 
	}
	public List<String> stemmatize(List<String> words, Trie trie){
		helper.printVerbose(verbose, "Stemming Starts....");
		List<String> stems = new LinkedList<String>();
		Iterator<String> itr = words.iterator(); 	
		while(itr.hasNext()) {
	         String wordOriginal = itr.next();
	         String wordStemmed = SnowBallStemmer((String)wordOriginal);
	        	 if(trie.isEnglishWord(wordStemmed)){
		        	 stems.add(wordStemmed);
		         }else{
		        	 stems.add(wordOriginal);
		         }
	    }
		helper.printVerbose(verbose, "Stemming Ends....");
		return stems;
	}
}
