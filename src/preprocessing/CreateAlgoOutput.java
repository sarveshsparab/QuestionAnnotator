package preprocessing;

import java.io.File;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import commandcentral.Config;
import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.ws4j.RelatednessCalculator;
import filehandling.CSVWriter;
import mysql.Apprentice;
import mysql.Question;
import semantics.Relevance;

public class CreateAlgoOutput {
	private List<String> rowList = null;
	private double score;
	private List<String> tokenUnion = null;
	private List<String> token1 = null;
	private List<String> token2 = null;
	private List<Double> vect1 = null;
	private List<Double> vect2 = null;	
	private Set<Integer> quesConsidered = null;
	private Set<Integer> quesTrained = null;
	private ILexicalDatabase db = null;
	private List<RelatednessCalculator> rcList = null;
	private List<Double> algoMaxList = null;
	private Question ques1 = null;
	private Question ques2 = null;
	private HashSet<String> tagsList = null;
	private Random randomQues;
	private List<Question> ques1Objects = null;
	private List<Question> ques2Objects = null;
	private Apprentice connUtility = null;
	
	public CreateAlgoOutput(SemanticPreProcess preProcess, Apprentice connUtility) {
		tokenUnion = new LinkedList<String>();
		token1 = new LinkedList<String>();
		token2 = new LinkedList<String>();
		vect1 = new LinkedList<Double>();
		vect2 = new LinkedList<Double>();
		quesConsidered = new HashSet<Integer>();
		quesTrained = new HashSet<Integer>();
		db = Config.db;
		rcList = Config.helper.getAlgoObjList();
		algoMaxList = Config.helper.getAlgoMax();
		score = 0.0;
		ques1 = null;
		ques2 = null;
		tagsList = new HashSet<String>();
		randomQues = new Random();
		ques1Objects = new LinkedList<Question>();
		ques2Objects = new LinkedList<Question>();
		this.connUtility = connUtility;
	}
	
	public void startPreprocessing(){
		System.out.println("Started preprocessing... ");
		long lStartTime = new Date().getTime();
		int minQues = 0;
		//int doneAmount = 0 ,everyPercentCount = 0, statusPrint = 1;
		if(Config.writeAlgoOutput){
			try {
				ques1Objects = connUtility.getQuestionTitles();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			//everyPercentCount = Config.writeFilesCount*Config.statusDisplayPercent/100;
			
			while(Config.helper.getCountOfFiles(Config.algoCsvFolder) <= Config.writeFilesCount){
				int fileNo = randomQues.nextInt(Config.writeFilesCount);
				int index = randomQues.nextInt(ques1Objects.size());
				while(quesConsidered.contains(index)){
					index = randomQues.nextInt(ques1Objects.size());
				}
				quesConsidered.add(index);
				ques1 = ques1Objects.get(index);
				token1 = Config.helper.stringToList(ques1.getKeywords(), ",");				
				tagsList.addAll(connUtility.getQuestionTags(ques1.getTitle()));
				
				try {
					tagsList.addAll(connUtility.getTagsForQuestion(Integer.valueOf(ques1.getQuestionId())));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
				try {
					ques2Objects = connUtility.getQuestionTitlesWithTags(Config.helper.hashsetToString(tagsList,","));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				System.out.println("Total Rows  :"+ques2Objects.size());
				minQues = (ques2Objects.size()<Config.quesToTrain)? ques2Objects.size() : Config.quesToTrain;
				rowList = new LinkedList<String>();
				quesTrained = new HashSet<Integer>();;
				for(int q=0; q<minQues; q++){
					
					int innerIndex = randomQues.nextInt(ques2Objects.size());
					while(quesTrained.contains(innerIndex)){
						innerIndex = randomQues.nextInt(ques2Objects.size());
					}
					quesTrained.add(innerIndex);
					ques2 = ques2Objects.get(innerIndex);
					token2 = Config.helper.stringToList(ques2.getKeywords(), ",");
					tokenUnion = Config.helper.listUnion(token1, token2);
					
					String row = "";
					row += ques2.getTitle();
					for(int i=0;i<Config.algoCount;i++){
						vect1 = Config.vectorizer.vectorize(token1,tokenUnion,new Relevance(db, rcList.get(i)),algoMaxList.get(i));
						vect2 = Config.vectorizer.vectorize(token2,tokenUnion,new Relevance(db, rcList.get(i)),algoMaxList.get(i));
						score = Config.scorer.cosSim(vect1, vect2, tokenUnion.size());
						row += Config.algoDelimiter+String.valueOf(score);
					}
					rowList.add(row);
					System.out.print("Rows computed : "+q+"||  Percent done : "+(((double)q)/minQues)+"\r");
				}
				int newFileNo = fileNo;
				File file = new File(Config.algoCsvFolder+newFileNo+".csv");
				while(file.exists()){
					newFileNo++;
					file = new File(Config.algoCsvFolder+newFileNo+".csv");
				}
				CSVWriter csvWriter = new CSVWriter(file);
				csvWriter.writeToFile(ques1.getTitle(), Config.algoHeaders, rowList, Config.algoDelimiter);
				ques2Objects = null;
//				doneAmount++;
//				if(doneAmount >= (everyPercentCount*statusPrint)){
//					int loopLimit = ((int)Math.floor(doneAmount/(everyPercentCount/Config.statusDisplayPercent)));
//					System.out.print("|");
//					for(int i=0;i<loopLimit;i++)
//						System.out.print("=");
//					for(int i=100;i>loopLimit;i--)
//						System.out.print(" ");
//					System.out.print("| - "+loopLimit+" percent done\r");
//					statusPrint++;
//				}
			}
		}
		long lEndTime = new Date().getTime();
		System.out.println("\n Completed Preprocessing in : " + (lEndTime-lStartTime) + " milliseconds");
	}
}
