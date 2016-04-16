package semantics;

import java.util.List;
import commandcentral.Config;
/**
 * @category Class
 * @purpose To get score between two vectors
 * @author Sarvesh
 */
public class Scorer {
	/**
     * @category Function
     * @argument vect1 : Vectorized form of first sentence
     * @argument vect2 : Vectorized form of Second sentence
     * @return A Double score value based on cosine similarity
     * @author Sarvesh
     */
	public Double cosSim(List<Double> vect1,List<Double> vect2,int len){
		Double sim=0.0;
		Double len1 = Config.helper.vectLength(vect1), len2 = Config.helper.vectLength(vect2);
		if(len1!=0 && len2!=0)
			sim = Config.helper.vectDotProduct(vect1, vect2, len)/(len1*len2);
		return sim;
	}
	
	
}
