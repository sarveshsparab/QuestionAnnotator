package semantics;

import java.util.List;

public class Scorer {
	private static Helper helper = new Helper();
	
	public Double cosSim(List<Double> vect1,List<Double> vect2,int len){
		Double sim=0.0;
		Double len1 = helper.vectLength(vect1), len2 = helper.vectLength(vect2);
		if(len1!=0 && len2!=0)
			sim = helper.vectDotProduct(vect1, vect2, len)/(len1*len2);
		return sim;
	}
	
	
}
