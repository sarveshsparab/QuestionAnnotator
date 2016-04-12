package preprocessing;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import commandcentral.Config;
import mysql.Apprentice;
//import neo4j.Utilities;
import semantics.Lemmatizer;
import semantics.Stemmer;
import semantics.StopWordPunc;
import semantics.Tokenizer;
import semantics.Trie;

public class SemanticPreProcess {
	private Tokenizer tokenizer;
	private Lemmatizer lemmatizer;
	private Stemmer stemmer;
	private Trie trie;
	private StopWordPunc stopword;
	private Apprentice connUtility;
	private List<String> quesTags = null;
	
	public SemanticPreProcess(Apprentice connUtility, Trie trie) {
		tokenizer = Config.tokenizer;
		lemmatizer = Config.lemmatizer;
		stopword = new StopWordPunc(Config.verbose,trie);
		stemmer = Config.stemmer;
		this.trie = trie;
		this.connUtility = connUtility;
	}

	public List<String> getQuesTags() {
		return quesTags;
	}

	public List<String> startPreprocessing(String str, String processOrder){
		List<String> strTokens = new LinkedList<String>();
		List<String> order = new LinkedList<String>(Arrays.asList(processOrder.split("\\|")));
		quesTags = new LinkedList<String>();
		for(int i=0; i<order.size();i++){
			String orderEntity = order.get(i);
			if(orderEntity.equals("GET_TAGS")){
				quesTags = connUtility.getQuestionTags(str);
				str = connUtility.getRemainingQuestion();
			}else if(orderEntity.equals("LEMMATIZE")){
				strTokens = lemmatizer.lemmatize(str);
			}else if(orderEntity.equals("REMOVE_STOP_WORDS")){
				strTokens = stopword.removeStop(strTokens);
			}else if(orderEntity.equals("TOKENIZE")){
				strTokens = tokenizer.tokenize(Config.helper.listToString(strTokens, " "));
			}else if(orderEntity.equals("REMOVE_PUNCTUATION")){
				strTokens = stopword.removePunc(strTokens);
			}else if(orderEntity.equals("STEMMATIZE")){
				strTokens = stemmer.stemmatize(strTokens,trie);
			}else if(orderEntity.equals("HASH")){
				strTokens.addAll(quesTags);
				Set<String> strUserHash = new HashSet<String>(strTokens);
				strTokens = new LinkedList<String>(strUserHash);
			}else if(orderEntity.equals("SORT")){
				Collections.sort(strTokens,String.CASE_INSENSITIVE_ORDER);
			}
		}		
		return strTokens;
	}
}
