package semantics;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import commandcentral.Config;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
/**
 * @category Class
 * @purpose For determining the parts of speech within a sentence
 * @author Sarvesh
 */
public class PartsOfSpeech {
	private MaxentTagger tagger;
	/**
     * @category Constructor 
     * @purpose Initializes the tagger file
     * @author Sarvesh
     */
	public PartsOfSpeech() {
		tagger = new MaxentTagger("edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger");
	}
	public static void main(String[] args) throws IOException,
	 ClassNotFoundException {
		PartsOfSpeech partsOfSpeech = new PartsOfSpeech();
		partsOfSpeech.getPOSList("can i use html in android using java");
	 
	}
	/**
     * @category Function
     * @argument sentence : A string sentence which needs to be analysed for parts of speech
     * @return A linked list of string array which contains the word and POS Tag
     * @author Sarvesh
     */
	public List<String[]> getPOSList(String sentence){
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
	/**
     * @category Function (Private)
     * @argument POSTag : A string Tag abbreviation
     * @return A string full form of the tag meaning
     * @author Sarvesh
     */
	private String handlePOSTagForms(String POSTag){
		String retVal = null;
		retVal = (retVal==null)?((POSTag.equals("CC"))?"CONJUNCTION":null):retVal;
		retVal = (retVal==null)?((POSTag.equals("CD"))?"CARDINAL_NUMBER":null):retVal;
		retVal = (retVal==null)?((POSTag.equals("DT"))?"DETERMINER":null):retVal;
		retVal = (retVal==null)?((POSTag.equals("EX"))?"EXISTENSIAL":null):retVal;
		retVal = (retVal==null)?((POSTag.equals("FW"))?"FOREIGN_WORD":null):retVal;
		retVal = (retVal==null)?((POSTag.equals("IN"))?"PREPOSITION":null):retVal;
		retVal = (retVal==null)?((POSTag.equals("JJ") || POSTag.equals("JJR") || POSTag.equals("JJS"))?"ADJECTIVE":null):retVal;
		retVal = (retVal==null)?((POSTag.equals("LS"))?"LIST_ITEM":null):retVal;
		retVal = (retVal==null)?((POSTag.equals("MD"))?"MODAL":null):retVal;
		retVal = (retVal==null)?((POSTag.equals("NN") || POSTag.equals("NNS") || POSTag.equals("NNP") || POSTag.equals("NNPS"))?"NOUN":null):retVal;
		retVal = (retVal==null)?((POSTag.equals("PDT"))?"PREDETERMINER":null):retVal;
		retVal = (retVal==null)?((POSTag.equals("POS"))?"POSSESSIVE":null):retVal;
		retVal = (retVal==null)?((POSTag.equals("PRP") || POSTag.equals("PRP$"))?"PRONOUN":null):retVal;
		retVal = (retVal==null)?((POSTag.equals("RB") || POSTag.equals("RBR") || POSTag.equals("RBS"))?"ADVERB":null):retVal;
		retVal = (retVal==null)?((POSTag.equals("RP"))?"PARTICLE":null):retVal;
		retVal = (retVal==null)?((POSTag.equals("SYM"))?"SYMBOL":null):retVal;
		retVal = (retVal==null)?((POSTag.equals("TO"))?"TO":null):retVal;
		retVal = (retVal==null)?((POSTag.equals("UH"))?"INTERJECTION":null):retVal;
		retVal = (retVal==null)?((POSTag.equals("VB") || POSTag.equals("VBD") || POSTag.equals("VBG") || POSTag.equals("VBN") || POSTag.equals("VBP") || POSTag.equals("VBZ"))?"VERB":null):retVal;
		retVal = (retVal==null)?((POSTag.equals("WDT"))?"WHDETERMINER":null):retVal;
		retVal = (retVal==null)?((POSTag.equals("WP") || POSTag.equals("WP$"))?"WHPRONOUN":null):retVal;
		retVal = (retVal==null)?((POSTag.equals("WRB"))?"WHADVERB":null):retVal;
		return (retVal==null)?("ERROR"):(retVal);
	}
}
