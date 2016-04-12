package semantics;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import commandcentral.Config;
import edu.cmu.lti.ws4j.RelatednessCalculator;

public class Vectorizer {
	private int verbose;
	private Helper helper = new Helper();
	public Vectorizer(int verbose){
		this.verbose = verbose;
	}
	public List<Double> vectorize(List<String> str, List<String> union, Relevance relevance, Double algoMax){
		helper.printVerbose(verbose, "Vectorization Starts....");
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
		helper.printVerbose(verbose, "Vectorization Ends....");
		return vect;
	}
	
	public List<Double> weightedVectorize(List<String> str, List<String> union, List<RelatednessCalculator> algoList, List<Double> algoMax, int algoCount, String algoWeights){
		helper.printVerbose(verbose, "Vectorization Starts....");
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
		helper.printVerbose(verbose, "Vectorization Ends....");
		return vect;
	}
	
}
