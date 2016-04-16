package semantics;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import commandcentral.Config;
@SuppressWarnings("unused")
/**
 * @category Class
 * @purpose For detecting and removing stopwords and punctuation marks
 * @author Sarvesh
 */
public class StopWordPunc {
	private int verbose;
	private Trie trie;
	private Set<String> stopWordSetStemmed;
	private Set<String> stopWordSet;
	private static Stemmer stemmer;
	private Set<String> puncsSet;
	/**
     * @category Constructor 
     * @argument verbose : A integer flag for verbose printing
     * @author Sarvesh
     */
	public StopWordPunc(int verbose, Trie trie){
		this.verbose = verbose;
		this.trie = trie;
		stemmer = new Stemmer(verbose);
		String[] stopwords = {"a", "as", "able", "about", "above", "according", "accordingly", "across", "actually", "after", "afterwards", "again", "against", "aint", "all", "allow", "allows", "almost", "alone", "along", "already", "also", "although", "always", "am", "among", "amongst", "an", "and", "another", "any", "anybody", "anyhow", "anyone", "anything", "anyway", "anyways", "anywhere", "apart", "appear", "appreciate", "appropriate", "are", "arent", "around", "as", "aside", "ask", "asking", "associated", "at", "available", "away", "awfully", "be", "became", "because", "become", "becomes", "becoming", "been", "before", "beforehand", "behind", "being", "believe", "below", "beside", "besides", "best", "better", "between", "beyond", "both", "brief", "but", "by", "cmon", "cs", "came", "can", "cant", "cannot", "cant", "cause", "causes", "certain", "certainly", "changes", "clearly", "co", "com", "come", "comes", "concerning", "consequently", "consider", "considering", "contain", "containing", "contains", "corresponding", "could", "couldnt", "course", "currently", "definitely", "described", "despite", "did", "didnt", "different", "do", "does", "doesnt", "doing", "dont", "done", "down", "downwards", "during", "each", "edu", "eg", "eight", "either", "else", "elsewhere", "enough", "entirely", "especially", "et", "etc", "even", "ever", "every", "everybody", "everyone", "everything", "everywhere", "ex", "exactly", "example", "except", "far", "few", "ff", "fifth", "first", "five", "followed", "following", "follows", "for", "former", "formerly", "forth", "four", "from", "further", "furthermore", "get", "gets", "getting", "given", "gives", "go", "goes", "going", "gone", "got", "gotten", "greetings", "had", "hadnt", "happens", "hardly", "has", "hasnt", "have", "havent", "having", "he", "hes", "hello", "help", "hence", "her", "here", "heres", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "hi", "him", "himself", "his", "hither", "hopefully", "how", "howbeit", "however", "i", "id", "ill", "im", "ive", "ie", "if", "ignored", "immediate", "in", "inasmuch", "inc", "indeed", "indicate", "indicated", "indicates", "inner", "insofar", "instead", "into", "inward", "is", "isnt", "it", "itd", "itll", "its", "its", "itself", "just", "keep", "keeps", "kept", "know", "knows", "known", "last", "lately", "later", "latter", "latterly", "least", "less", "lest", "let", "lets", "like", "liked", "likely", "little", "look", "looking", "looks", "ltd", "mainly", "many", "may", "maybe", "me", "mean", "meanwhile", "merely", "might", "more", "moreover", "most", "mostly", "much", "must", "my", "myself", "name", "namely", "nd", "near", "nearly", "necessary", "need", "needs", "neither", "never", "nevertheless", "new", "next", "nine", "no", "nobody", "non", "none", "noone", "nor", "normally", "not", "nothing", "novel", "now", "nowhere", "obviously", "of", "off", "often", "oh", "ok", "okay", "old", "on", "once", "one", "ones", "only", "onto", "or", "other", "others", "otherwise", "ought", "our", "ours", "ourselves", "out", "outside", "over", "overall", "own", "particular", "particularly", "per", "perhaps", "placed", "please", "plus", "possible", "presumably", "probably", "provides", "que", "quite", "qv", "rather", "rd", "re", "really", "reasonably", "regarding", "regardless", "regards", "relatively", "respectively", "right", "said", "same", "saw", "say", "saying", "says", "second", "secondly", "see", "seeing", "seem", "seemed", "seeming", "seems", "seen", "self", "selves", "sensible", "sent", "serious", "seriously", "seven", "several", "shall", "she", "should", "shouldnt", "since", "six", "so", "some", "somebody", "somehow", "someone", "something", "sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry", "specified", "specify", "specifying", "still", "sub", "such", "sup", "sure", "ts", "take", "taken", "tell", "tends", "th", "than", "thank", "thanks", "thanx", "that", "thats", "thats", "the", "their", "theirs", "them", "themselves", "then", "thence", "there", "theres", "thereafter", "thereby", "therefore", "therein", "theres", "thereupon", "these", "they", "theyd", "theyll", "theyre", "theyve", "think", "third", "this", "thorough", "thoroughly", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "took", "toward", "towards", "tried", "tries", "truly", "try", "trying", "twice", "two", "un", "under", "unfortunately", "unless", "unlikely", "until", "unto", "up", "upon", "us", "use", "used", "useful", "uses", "using", "usually", "value", "various", "very", "via", "viz", "vs", "want", "wants", "was", "wasnt", "way", "we", "wed", "well", "were", "weve", "welcome", "well", "went", "were", "werent", "what", "whats", "whatever", "when", "whence", "whenever", "where", "wheres", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whos", "whoever", "whole", "whom", "whose", "why", "will", "willing", "wish", "with", "within", "without", "wont", "wonder", "would", "would", "wouldnt", "yes", "yet", "you", "youd", "youll", "youre", "youve", "your", "yours", "yourself", "yourselves", "zero"};
		stopWordSet = new HashSet<String>(Arrays.asList(stopwords));
		stopWordSetStemmed = new HashSet<String>(stemmer.stemmatize(Arrays.asList(stopwords),trie));
		String[] puncs = {"?", "!", "'", "\"", "," , "_", "-" , "." , "*" , "$", "%"};
		puncsSet = new HashSet<String>(Arrays.asList(puncs));
	}
	/**
     * @category Function
     * @argument words : Linked list of words to be checked for stopword tokens
     * @return Returns the linked list of words without the stopwords
     * @author Sarvesh
     */
	public List<String> removeStop(List<String> words){
		Config.helper.printVerbose(verbose, "StopWord Removal Starts....");
		List<String> afterRemoval = new LinkedList<String>();
		Iterator<String> itr = words.iterator(); 	
		while(itr.hasNext()) {
	         String element = itr.next();
	         if(stopWordSet.contains(element) || stopWordSetStemmed.contains(element)){
	        	 
	         }else{
	        	 afterRemoval.add(element);
	         }
	    }
		Config.helper.printVerbose(verbose, "StopWord Removal Ends....");
		return afterRemoval;
	}
	/**
     * @category Function
     * @argument words : Linked list of words to be checked for punctuation tokens
     * @return Returns the linked list of words without the punctuations
     * @author Sarvesh
     */
	public List<String> removePunc(List<String> words){
		Config.helper.printVerbose(verbose, "Punctuation Removal Starts....");
		List<String> afterRemoval = new LinkedList<String>();
		Iterator<String> itr = words.iterator(); 	
		while(itr.hasNext()) {
	         String element = itr.next();
	         if(puncsSet.contains(element)){
	        	 
	         }else{
	        	 afterRemoval.add(element);
	         }
	    }
		Config.helper.printVerbose(verbose, "Punctuation Removal Ends....");
		return afterRemoval;
	}
}
