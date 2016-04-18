package semantics;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import commandcentral.Config;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class PartsOfSpeech {
	private static MaxentTagger tagger;
	public PartsOfSpeech() {
		tagger = new MaxentTagger("edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger");
	}
	public static void main(String[] args) throws IOException,
	 ClassNotFoundException {
		PartsOfSpeech partsOfSpeech = new PartsOfSpeech();
		partsOfSpeech.getPOSList("can i open html in android using java");
	 
	}
	public static List<String[]> getPOSList(String sentence){
		List<String[]> POSList = new LinkedList<String[]>();
		String tagged = tagger.tagString(sentence);
		List<String> tempList = Config.helper.stringToList(tagged, " ");
		for(int i=0;i<tempList.size();i++){
			String [] POSArray = tempList.get(i).split("_");
			POSArray[1] = handlePOSTagForms(POSArray[1]);
			POSList.add(POSArray);
		}
		for(int i=0;i<POSList.size();i++){
			String [] POSArray = POSList.get(i);
			for(int j=0;j<POSArray.length;j++){
				System.out.print(POSArray[j]+" ");				
			}
			System.out.println("");
		}
		return POSList;
	}
	
	public static String handlePOSTagForms(String POSTag){
		String retVal = null;
		if(ret)
		retVal = (retVal==null)?((POSTag.equals("CC"))?"CONJUNCTION":null):null;
		retVal = (retVal==null)?((POSTag.equals("CD"))?"CARDINAL_NUMBER":null):null;
		retVal = (retVal==null)?((POSTag.equals("DT"))?"DETERMINER":null):null;
		retVal = (retVal==null)?((POSTag.equals("EX"))?"EXISTENSIAL":null):null;
		retVal = (retVal==null)?((POSTag.equals("FW"))?"FOREIGN_WORD":null):null;
		retVal = (retVal==null)?((POSTag.equals("IN"))?"PREPOSITION":null):null;
		retVal = (retVal==null)?((POSTag.equals("JJ") || POSTag.equals("JJR") || POSTag.equals("JJS"))?"ADJECTIVE":null):null;
		retVal = (retVal==null)?((POSTag.equals("LS"))?"LIST_ITEM":null):null;
		retVal = (retVal==null)?((POSTag.equals("MD"))?"MODAL":null):null;
		retVal = (retVal==null)?((POSTag.equals("NN") || POSTag.equals("NNS") || POSTag.equals("NNP") || POSTag.equals("NNPS"))?"NOUN":null):null;
		retVal = (retVal==null)?((POSTag.equals("PDT"))?"PREDETERMINER":null):null;
		retVal = (retVal==null)?((POSTag.equals("POS"))?"POSSESSIVE":null):null;
		retVal = (retVal==null)?((POSTag.equals("PRP") || POSTag.equals("PRP$"))?"PRONOUN":null):null;
		retVal = (retVal==null)?((POSTag.equals("RB") || POSTag.equals("RBR") || POSTag.equals("RBS"))?"ADVERB":null):null;
		retVal = (retVal==null)?((POSTag.equals("RP"))?"PARTICLE":null):null;
		retVal = (retVal==null)?((POSTag.equals("SYM"))?"SYMBOL":null):null;
		retVal = (retVal==null)?((POSTag.equals("TO"))?"TO":null):null;
		retVal = (retVal==null)?((POSTag.equals("UH"))?"INTERJECTION":null):null;
		retVal = (retVal==null)?((POSTag.equals("VB") || POSTag.equals("VBD") || POSTag.equals("VBG") || POSTag.equals("VBN") || POSTag.equals("VBP") || POSTag.equals("VBZ"))?"VERB":null):null;
		retVal = (retVal==null)?((POSTag.equals("WDT"))?"WHDETERMINER":null):null;
		retVal = (retVal==null)?((POSTag.equals("WP") || POSTag.equals("WP$"))?"WHPRONOUN":null):null;
		retVal = (retVal==null)?((POSTag.equals("WRB"))?"WHADVERB":null):null;
		return (retVal==null)?("ERROR"):(retVal);
	}
}
