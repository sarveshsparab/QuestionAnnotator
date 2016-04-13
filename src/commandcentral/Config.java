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
	
	/**
	 * @category Semantic Variables
	 * @variable db : initializes the WordNet Lexical Database
	 * @variable rc : sets the semantic algorithm which will run when single algorithm chosen
	 * @variable rcMax : sets the maximum possible algorithm value
	 * @variable semanticWeight : Sets the weights for the various semantic algorithms
	 * @author Sarvesh
	 */
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
	
	public static String semanticWeight = "0.1666667|0.1666667|0.1666667|0.1666667|0.1666667|0.1666667";
	
	/**
	 * @category My-SQL Variables
	 * @variable dbUsername : My-SQL Username
	 * @variable dbPassword : My-SQL Password
	 * @variable dbHost : My-SQL Host Address
	 * @variable dbPort : My-SQL Server Port
	 * @variable dbName : My-SQL Database Name
	 * @author Sarvesh
	 */
	public static String dbUsername = "avengers";
	public static String dbPassword = "sadrvm";
	public static String dbHost = "localhost";
	public static int dbPort = 3307;
	public static String dbName = "fyproject";
	
	/**
	 * @category Path Variables
	 * @variable algoCsvFolder : Stores the .CSV files for algo-weight Preprocessing
	 * @variable wordsListPath : Location of dictionary for Trie
	 * @variable weightOutputPath : Location of output of algo-weights
	 * @author Sarvesh
	 */
	public static String algoCsvFolder = "data/preProcess/CSVFiles/";
	public static String wordsListPath = "data/EnglishWords/words.txt";
	public static String weightOutputPath = "data/preProcess/Output/weightResult.txt";
	
	/**
	 * @category Path Variables
	 * @variable algoDelimiter : The delimiter between strings
	 * @author Sarvesh
	 */
	public static String algoDelimiter = " ASVAARNVTEISKHA ";
	
	/**
	 * @category Constant Variables
	 * @variable verbose : Flag to enable detailed printing
	 * @variable maxTagsOutput : To stop after we obtain this amount of tags
	 * @variable writeAlgoOutput : Flag to enable output to be written
	 * @variable quesToTrain : Number of questions to train in the cluster
	 * @variable writeFilesCount : Number of .CSV files to be written for training purpose
	 * @variable algoCount : The number of algo considered when multiple algo used for semantic measure
	 * @variable statusDisplayPercent : Progress bar status granularity measure
	 * @variable basicPreProcessOrder : The order in which semantic pre-processing will happen
	 * @author Sarvesh
	 */
	public static int verbose = 0;
	public static int maxTagsOutput = 50;
	public static boolean writeAlgoOutput = true;
	public static int quesToTrain = 50;
	public static int writeFilesCount = 10000;
	public static int algoCount = 6;
	public static int statusDisplayPercent = 1;
	public static String basicPreProcessOrder = "GET_TAGS|LEMMATIZE|REMOVE_STOP_WORDS|TOKENIZE|REMOVE_PUNCTUATION|STEMMATIZE|HASH|SORT";
	
	/**
	 * @category Miscellaneous Variables
	 * @variable algoHeaders : Algo headers for .CSV files
	 * @author Sarvesh
	 */
	public static List<String> algoHeaders = Arrays.asList("Question","WUP","RES","JCN","LIN","LCH","PATH","LESK","HSO");
	
	/**
	 * @category Class Variables
	 * Here those classes are instantiated whose objects aren't supposed to be created anywhere else
	 * @author Sarvesh
	 */
	public static Helper helper = new Helper();
	public static Tokenizer tokenizer = new Tokenizer(verbose);
	public static Lemmatizer lemmatizer = new Lemmatizer(verbose);
	public static Stemmer stemmer = new Stemmer(verbose);
	public static Vectorizer vectorizer = new Vectorizer(verbose);
	public static Trie trie = new Trie();
	public static Scorer scorer = new Scorer();
	public static Apprentice apprentice = new Apprentice();
}
