package semantics;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class PartsOfSpeech {
	private MaxentTagger tagger;
	public PartsOfSpeech() {
		tagger = new MaxentTagger("edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger");
	}
	public static void main(String[] args) throws IOException,
	 ClassNotFoundException {

	 
	}
	public List<String[]> getPOSList(String sentence){
		List<String[]> POSList = new LinkedList<String[]>();
		String tagged = tagger.tagString(sentence);
		System.out.println(tagged);
		
		return POSList;
	}
}
