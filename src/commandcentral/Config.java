package commandcentral;

import java.util.Arrays;
import java.util.List;
import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.RelatednessCalculator;
import edu.cmu.lti.ws4j.impl.HirstStOnge;
import edu.cmu.lti.ws4j.impl.JiangConrath;
import edu.cmu.lti.ws4j.impl.LeacockChodorow;
import edu.cmu.lti.ws4j.impl.Lesk;
import edu.cmu.lti.ws4j.impl.Lin;
import edu.cmu.lti.ws4j.impl.Path;
import edu.cmu.lti.ws4j.impl.Resnik;
import edu.cmu.lti.ws4j.impl.WuPalmer;
import mysql.Apprentice;
import semantics.Helper;
import semantics.Lemmatizer;
import semantics.Scorer;
import semantics.Stemmer;
import semantics.Tokenizer;
import semantics.Trie;
import semantics.Vectorizer;

public class Config {
	
	//Semantic Algorithms
	public static ILexicalDatabase db = new NictWordNet();
	public static RelatednessCalculator wup = new WuPalmer(db);
	public static RelatednessCalculator res = new Resnik(db);
	public static RelatednessCalculator jcn = new JiangConrath(db);
	public static RelatednessCalculator lin = new Lin(db);
	public static RelatednessCalculator lch = new LeacockChodorow(db);
	public static RelatednessCalculator path = new Path(db);
	public static RelatednessCalculator lesk = new Lesk(db);
	public static RelatednessCalculator hso = new HirstStOnge(db);
	public static RelatednessCalculator rc = lin; 
	public static double rcMax = 1.0;
	
	//Semantic Weights
	public static String semanticWeight = "0.1666667|0.1666667|0.1666667|0.1666667|0.1666667|0.1666667";
	
	//MySql Parameters
	public static String dbUsername = "avengers";
	public static String dbPassword = "sadrvm";
	public static String dbHost = "localhost";
	public static int dbPort = 3307;
	public static String dbName = "fyproject";
	
	//Paths
	public static String neo4jPath = "data/neoData";
	public static String algoCsvFolder = "data/preProcess/CSVFiles/";
	public static String wordsListPath = "data/EnglishWords/words.txt";
	public static String weightOutputPath = "data/preProcess/Output/weightResult.txt";
	
	//Delimiters
	public static String algoDelimiter = " ASVAARNVTEISKHA ";
	
	//Constants
	public static int verbose = 0;
	public static int maxTagsOutput = 50;
	public static boolean writeAlgoOutput = true;
	public static int quesToTrain = 50;
	public static int writeFilesCount = 10000;
	public static int algoCount = 6;
	public static int statusDisplayPercent = 1;
	public static String basicPreProcessOrder = "GET_TAGS|LEMMATIZE|REMOVE_STOP_WORDS|TOKENIZE|REMOVE_PUNCTUATION|STEMMATIZE|HASH|SORT";
	
	//Miscellaneous
	public static List<String> algoHeaders = Arrays.asList("Question","WUP","RES","JCN","LIN","LCH","PATH","LESK","HSO");
	
	//Class objects
	public static Helper helper = new Helper();
	public static Tokenizer tokenizer = new Tokenizer(verbose);
	public static Lemmatizer lemmatizer = new Lemmatizer(verbose);
	public static Stemmer stemmer = new Stemmer(verbose);
	public static Vectorizer vectorizer = new Vectorizer(verbose);
	public static Trie trie = new Trie();
	public static Scorer scorer = new Scorer();
	public static Apprentice apprentice = new Apprentice();
}
