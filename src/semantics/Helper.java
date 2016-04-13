					package semantics;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import commandcentral.Config;
import edu.cmu.lti.ws4j.RelatednessCalculator;

public class Helper {
	
    private List<RelatednessCalculator> rcList;
    private int algoType=0;
    private List<Double> algoMax;
	
    public Helper(){
    	rcList = new LinkedList<RelatednessCalculator>();
    	algoMax = new LinkedList<Double>();
    	rcList.add(0, Config.wup);
    	rcList.add(1, Config.res);
    	rcList.add(2, Config.jcn);
    	rcList.add(3, Config.lin);
    	rcList.add(4, Config.lch);
    	rcList.add(5, Config.path);
    	rcList.add(6, Config.lesk);
    	rcList.add(7, Config.hso);
    	algoMax.add(0, 1.0);
    	algoMax.add(1, 15.0);
    	algoMax.add(2, 100.0);
    	algoMax.add(3, 1.0);
    	algoMax.add(4, 5.0);
    	algoMax.add(5, 1.0);
    	algoMax.add(6, 100.0);
    	algoMax.add(7, 16.0);
    }
	public int getAlgoType(){
		return algoType;
	}
	public List<Double> getAlgoMax(){
		return algoMax;
	}
	public List<RelatednessCalculator> getAlgoObjList(){
		return rcList;
	}

	public void printVerbose(int verbose,String text){
		if(verbose==1)
			System.out.println(text);
	}
	
	public String listToString(List<String> s, String delimiter){
		StringBuffer ret = new StringBuffer("");
        for (int i = 0; s != null && i < s.size(); i++) {
            ret.append(s.get(i));
            if (i < s.size() - 1) {
                ret.append(delimiter);
            }
        }
        return ret.toString();	
	}
	
	public String hashsetToString(HashSet<String> set, String delimiter){
		StringBuffer ret = new StringBuffer("");
		int i=0;
		for(String s:set){
			i++;
			ret.append(s);
            if (i < set.size()) {
                ret.append(delimiter);
            }
        }
        return ret.toString();	
	}
	
	public String[] listToStringArr(List<String> list){
		int size = list.size(),i;
		String[] arr = new String[size];
		for(i=0;i<size;i++){
			arr[i] = list.get(i);
		}
		return arr;
	}
	
	public List<String> stringToList(String str, String delimiter){
		List<String> list = new LinkedList<String>(Arrays.asList(str.split(delimiter)));
		return list;
	}
	
	public void printList(List<?> list){
		Iterator<?> itr = list.iterator();
		int count = 0;
		while(itr.hasNext()) {
	         Object element = itr.next();
	         System.out.println(++count+" : "+element);
	    }
	}
	
	public List<String> listUnion(List<String> list1,List<String> list2){
		List<String> union;
		Set<String> set = new HashSet<String>();
		set.addAll(list1);
		set.addAll(list2);
		union = new LinkedList<String>(set);
		Collections.sort(union,String.CASE_INSENSITIVE_ORDER);
		return union;
	}
	
	public Double vectLength(List<Double> vect){
		Double len=0.0,data;
		Iterator<Double> itr = vect.iterator();
		while(itr.hasNext()){
			data = (Double)itr.next();
			len += data*data;
		}
		return Math.sqrt(len);
	}
	
	public Double vectDotProduct(List<Double> vect1,List<Double> vect2,int len){
		Double prod=0.0;
		int i;
		for(i=0;i<len;i++){
			prod += vect1.get(i)*vect2.get(i);
		}
		return prod;
	}
	
	public void printMap(Map<?, ?> map) {
		for (Map.Entry<?, ?> entry : map.entrySet()) {
			System.out.println("[Key] : " + entry.getKey() 
                                      + " [Value] : " + entry.getValue());
		}
	}
	
	public Map<Integer, Double> sortMapByValue(Map<Integer, Double> unsortMap) {

		List<Map.Entry<Integer, Double>> list = new LinkedList<Map.Entry<Integer, Double>>(unsortMap.entrySet());

		Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>() {
			public int compare(Map.Entry<Integer, Double> o1,Map.Entry<Integer, Double> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});
		Map<Integer, Double> sortedMap = new LinkedHashMap<Integer, Double>();
		for (Iterator<Map.Entry<Integer, Double>> it = list.iterator(); it.hasNext();) {
			Map.Entry<Integer, Double> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
	
	public double getVectorDistance(List<Integer> vect1,List<Integer> vect2,double power){
		double dist = 0.0;
		for(int i=0;i<vect1.size();i++){
			dist += Math.abs(vect1.get(i)-vect2.get(i));
			//dist += Math.pow(dist , 1.0);
		}
		//dist = Math.pow(dist, (1/power));
		return dist;
	}
	public int getCountOfFiles(String dirPath){
		File dir = new File(dirPath);
		return dir.listFiles().length;
	}
}
