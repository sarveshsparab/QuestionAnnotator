package commandcentral;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.ws4j.RelatednessCalculator;
import mysql.Apprentice;
import mysql.Question;
import mysql.RelevantTag;
import preprocessing.SemanticPreProcess;
import semantics.*;

public class Pivot {
	
	private static ILexicalDatabase db = Config.db;
	private static RelatednessCalculator rc = Config.rc; 
	private static double algoMax = 1.0;
	private static Trie trie = Config.trie;
	private static Relevance relevance = new Relevance(db, rc);
	private static Apprentice connUtility = Config.apprentice;
	private static Scanner scanner = new Scanner(System.in);
	
	public static void main(String[] args) {
		try {
			trie.buildDict();
		} catch (IOException e) {
			e.printStackTrace();
		}
		SemanticPreProcess preProcess = new SemanticPreProcess(connUtility, trie);	
		
		System.out.println("Enter your question?"); //Retrieve data from two tables without duplicates in Android?
		//Can i open html page in android using java
		String userQues = scanner.nextLine();
		long lStartTime = new Date().getTime();
		
		/*String quesTags = Config.helper.listToString(connUtility.getQuestionTags(userQues),",");
		System.out.println("tags : "+quesTags);
		String userKeywords = Config.helper.listToString(preProcess.startPreprocessing(userQues, Config.basicPreProcessOrder),",");
		
		try {
			List<RelevantTag> relTags = connUtility.generateRelevantTags(quesTags, userKeywords);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		
		
		
		List<String> strUserToken = preProcess.startPreprocessing(userQues, Config.basicPreProcessOrder); 
		System.out.println(strUserToken);
		
		Iterator<Question> quesDbIterator = null;
		try {
			quesDbIterator = connUtility.getQuestionTitles().iterator();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		while(quesDbIterator.hasNext()){
			long innerStartTime = new Date().getTime();
			String quesStr = quesDbIterator.next().getTitle();
			List<String> strDBToken = preProcess.startPreprocessing(quesStr, Config.basicPreProcessOrder);
			
			List<String> tokenUnion = Config.helper.listUnion(strUserToken, strDBToken);
			List<Double> vect1 = Config.vectorizer.weightedVectorize(strUserToken,tokenUnion,Config.helper.getAlgoObjList(),Config.helper.getAlgoMax(),Config.algoCount,Config.semanticWeight);
			List<Double> vect2 = Config.vectorizer.weightedVectorize(strDBToken,tokenUnion,Config.helper.getAlgoObjList(),Config.helper.getAlgoMax(),Config.algoCount,Config.semanticWeight);
			//List<Double> vect1 = Config.vectorizer.vectorize(strUserToken,tokenUnion,new Relevance(Config.db, Config.rc),Config.rcMax);
	        //List<Double> vect2 = Config.vectorizer.vectorize(strDBToken,tokenUnion,new Relevance(Config.db, Config.rc),Config.rcMax);
			Double score = Config.scorer.cosSim(vect1, vect2, tokenUnion.size());
			System.out.println(quesStr);
			System.out.println(score);
			long innerEndTime = new Date().getTime();
			System.out.println("\n\n Completed Preprocessing in : " + (innerEndTime-innerStartTime) + " milliseconds");
			System.out.println("------------------------------------------------------");
			//String s = scanner.nextLine();
		}
		
		long lEndTime = new Date().getTime();
		System.out.println("\n\n Completed Preprocessing in : " + (lEndTime-lStartTime) + " milliseconds");
	}
}
