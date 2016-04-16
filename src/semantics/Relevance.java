package semantics;

import java.util.List;

import edu.cmu.lti.jawjaw.pobj.POS;
import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.data.Concept;
import edu.cmu.lti.ws4j.Relatedness;
import edu.cmu.lti.ws4j.RelatednessCalculator;
/**
 * @category Class
 * @purpose For calculating the relevance 
 * @author Sarvesh
 */
public class Relevance {   
	
	private ILexicalDatabase db;
	private RelatednessCalculator rc;
	/**
     * @category Constructor 
     * @argument db : Sets a lexical database for the WordNet
     * @argument rc : Sets the relatednessCalculator Algorithm to be used 
     * @author Sarvesh
     */
	public Relevance(ILexicalDatabase db,RelatednessCalculator rc){
		this.db = db;
		this.rc = rc;
	}
	/**
     * @category Function
     * @argument w1 : First word
     * @argument w2 : Second Word
     * @return A Double score value based on the relatedness algorithm chosen
     * @author Sarvesh
     */
	public double calcRelatedness(String w1,String w2){
    	List<POS[]> posPairs = rc.getPOSPairs();
        double maxScore = -1D;

        for(POS[] posPair: posPairs) {
            List<Concept> synsets1 = (List<Concept>)db.getAllConcepts(w1, posPair[0].toString());
            List<Concept> synsets2 = (List<Concept>)db.getAllConcepts(w2, posPair[1].toString());

            for(Concept synset1: synsets1) {
                for (Concept synset2: synsets2) {
                    Relatedness relatedness = rc.calcRelatednessOfSynset(synset1, synset2);
                    double score = relatedness.getScore();
                    if (score > maxScore) { 
                        maxScore = score;
                    }
                }
            }
        }
        if (maxScore == -1D) {
            maxScore = 0.0;
        }
        return maxScore;
    }
}
