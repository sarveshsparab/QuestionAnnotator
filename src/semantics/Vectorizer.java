package semantics;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import commandcentral.Config;
import edu.cmu.lti.ws4j.RelatednessCalculator;
/**
 * @category Class
 * @purpose For creating vectors from tokens
 * @author Sarvesh
 */
public class Vectorizer {
	private int verbose;
	/**
     * @category Constructor 
     * @argument verbose : A integer flag for verbose printing
     * @author Sarvesh
     */
	public Vectorizer(int verbose){
		this.verbose = verbose;
	}
	/**
     * @category Function
     * @argument str : linked list of tokens in a string
     * @argument union : linked list of tokens formed after union of two strings
     * @argument relevance : object which sets which algo to use
     * @argument algoMax : Maximum value for the algo used
     * @return A vector in form of linked list based on algo
     * @author Sarvesh
     */
	public List<Double> vectorize(List<String> str, List<String> union, Relevance relevance, Double algoMax){
		Config.helper.printVerbose(verbose, "Vectorization Starts....");
		int i;
		List <Double> vect = new LinkedList<Double>();
		Set<String> hashStr = new HashSet<String>();
		hashStr.addAll(str);
		
		for(i=0;i<union.size();i++){
			String unionWord = union.get(i);
			if(hashStr.contains(unionWord)){
				vect.add(i, algoMax);
			}else{
				Double rel,max = 0.0;
				Iterator<String> itr = hashStr.iterator();
				while(itr.hasNext()){
					rel = relevance.calcRelatedness(unionWord, (String)itr.next());
					max = rel > max ? rel : max;
				}
				vect.add(i, max);
			}
		}
		Config.helper.printVerbose(verbose, "Vectorization Ends....");
		return vect;
	}
	/**
     * @category Function
     * @argument str : linked list of tokens in a string
     * @argument union : linked list of tokens formed after union of two strings
     * @argument algoList : Linked list of objects of respective algorithms
     * @argument algoMax : Linked list of the respective algo maximums 
     * @argument algoCount : Number of algo used to get semantic score
     * @argument algoWeight : The weightage importance of each algo used
     * @return A vector in form of linked list based on algo
     * @author Sarvesh
     */
	public List<Double> weightedVectorize(List<String> str, List<String> union, List<RelatednessCalculator> algoList, List<Double> algoMax, int algoCount, String algoWeights){
		Config.helper.printVerbose(verbose, "Vectorization Starts....");
		int i;
		List <Double> vect = new LinkedList<Double>();
		Set<String> hashStr = new HashSet<String>();
		hashStr.addAll(str);
		List<String> algoWts = new LinkedList<String>(Arrays.asList(algoWeights.split("\\|")));
		
		double netVal = 0.0;
		
		for(i=0;i<union.size();i++){
			String unionWord = union.get(i);
			if(hashStr.contains(unionWord)){
				netVal = 0.0;
				for(int j=0;j<algoCount;j++)
					netVal += algoMax.get(j)*Double.parseDouble(algoWts.get(j));
				vect.add(i, netVal);
			}else{
				double max = 0.0;
				Iterator<String> itr = hashStr.iterator();
				while(itr.hasNext()){
					netVal = 0.0;
					String word = itr.next();
					for(int j=0;j<algoCount;j++)
						netVal += new Relevance(Config.db, algoList.get(j)).calcRelatedness(unionWord, word)*Double.parseDouble(algoWts.get(j));
					max = netVal > max ? netVal : max;
				}
				vect.add(i, max);
			}
		}
		Config.helper.printVerbose(verbose, "Vectorization Ends....");
		return vect;
	}
	
}
