package preprocessing;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

import commandcentral.Config;
import mysql.Apprentice;
//import neo4j.Utilities;
import semantics.Trie;

public class PivotPreProcess {
	private static CreateAlgoOutput algoOutput = null;
	private static AlgoPreProcess algoPreProcess = null;
	private static Scanner scanner = new Scanner(System.in);
	private static Trie trie = Config.trie;
	private static Apprentice connUtility = Config.apprentice;
	
	public static void main(String[] args) {
		try {
			trie.buildDict();
		} catch (IOException e) {
			e.printStackTrace();
		}
		SemanticPreProcess preProcess = new SemanticPreProcess(connUtility, trie);
		algoOutput = new CreateAlgoOutput(preProcess,connUtility);
		algoPreProcess = new AlgoPreProcess();
		displayMenu();
		int choice = scanner.nextInt();
		switch(choice){
			case 1:
				try {
					connUtility.addKeywordsToQuestion(preProcess);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			case 2:
				break;
			case 3:
				algoOutput.startPreprocessing();
				break;
			case 43:
				algoPreProcess.startPreprocessing();
				break;
			case 5:
				
				break;
			case 6:
				//connUtility.createCommonTagsRelationships();
				break;
			default:
				break;
		}
	}
	public static void displayMenu(){
		System.out.println("************************************************************");
		System.out.println("***                PreProcessing Central                 ***");
		System.out.println("***                                                      ***");
		System.out.println("*** 1. Modify Crawled .CSV files                         ***");
		System.out.println("*** 2. Load data into Neo4J Database                     ***");
		System.out.println("*** 3. Create Algorithm Output files                     ***");
		System.out.println("*** 4. Obtain Algorithm Weights                          ***");
		System.out.println("*** 5. Create Tag to Tag Weights                         ***");
		System.out.println("*** 6. Create Question to Question Weights               ***");
		System.out.println("***                                                      ***");
		System.out.println("************************************************************");
		System.out.print("Pick an option : ");
	}
}
