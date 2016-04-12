package semantics;

import java.io.StringReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.TokenizerFactory;

public class Tokenizer {
	public TokenizerFactory<CoreLabel> tf; 
	private int verbose;
	private static Helper helper = new Helper();
	public Tokenizer(int verbose){
		this.verbose = verbose;
	}
	private List<CoreLabel> makeTokens (String sentence){
		if(tf == null){
			tf = PTBTokenizer.factory(new CoreLabelTokenFactory(),"untokenizable=noneDelete");
		}
		List<CoreLabel> tokenWords = tf.getTokenizer(new StringReader(sentence)).tokenize();
		return tokenWords;
	}
	public List<String> tokenize (String sentence){
		helper.printVerbose(verbose, "Tokenizing Starts....");
		List<String> tokens = new LinkedList<String>();
		List<CoreLabel> tokenWords = makeTokens(sentence);
		Iterator<CoreLabel> itr = tokenWords.iterator(); 	
		while(itr.hasNext()) {
	         CoreLabel element = itr.next();
	         tokens.add(element.word());
	    }
		helper.printVerbose(verbose, "Tokenizing Starts....");
		return tokens;
	}
}
