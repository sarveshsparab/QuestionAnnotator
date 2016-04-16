package semantics;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.tartarus.snowball.ext.PorterStemmer;
/**
 * @category Class
 * @purpose For stemming the words
 * @author Sarvesh
 */
public class Stemmer {
	private int verbose;
	private static Helper helper = new Helper();
	/**
     * @category Constructor 
     * @argument verbose : A integer flag for verbose printing
     * @author Sarvesh
     */
	public Stemmer(int verbose){
		this.verbose = verbose;
	}
	/**
     * @category Function(Private)
     * @argument word : String to be stemmed
     * @return Returns the stemmed form of the word
     * @author Sarvesh
     */
	private String SnowBallStemmer(String word) { //Snowball stemmer (Porter 2)
	    PorterStemmer state = new PorterStemmer(); 
	    state.setCurrent(word); 
	    state.stem(); 
	    return state.getCurrent(); 
	}
	/**
     * @category Function
     * @argument words : Linked list of words to be stemmed
     * @argument trie : The dictionary of english words used to verify the integrity of the stemmed word
     * @return A linked list of the corresponding stemmed words
     * @author Sarvesh
     */
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
