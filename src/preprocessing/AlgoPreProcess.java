package preprocessing;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import commandcentral.Config;
import filehandling.CSVReader;
import filehandling.TXTWriter;

public class AlgoPreProcess {
	private List<LinkedList<String>> fileAsList = null;
	private List<String> eachRow = null;
	private List<HashMap<Integer,Double>> algoOrders = new LinkedList<HashMap<Integer,Double>>();
	private HashMap<Integer,Double> algoMap = null;
	private List<LinkedList<Integer>> algoKeyOrdered = null;
	private LinkedList<Integer> keyOrdered = null;
	private double[][][] distMat = new double[Config.writeFilesCount][Config.algoCount][Config.algoCount];
	private double[] weightMat = new double[Config.algoCount];
	private double minDist,maxDist,rangeMax;
	private Set<Integer> clusterSet = null;
	
	public void startPreprocessing(){
		System.out.println("Started preprocessing... ");
		long lStartTime = new Date().getTime();
		String[] headers = null;
		
		for(int fileNo = 1; fileNo <= Config.writeFilesCount ; fileNo++){
			fileAsList = new LinkedList<LinkedList<String>>();
			algoKeyOrdered = new LinkedList<LinkedList<Integer>>();
			clusterSet = new HashSet<Integer>();
			CSVReader csvReader = new CSVReader(Config.algoCsvFolder+fileNo+".csv");
			fileAsList = csvReader.readRowsIntoList(true,true);
			maxDist = rangeMax = 0.0;
			minDist = (Math.floor(fileAsList.size()/2.0))*(2*(fileAsList.size()-1)+(Math.floor(fileAsList.size()/2.0)-1)*(-2));
			headers = csvReader.getHeaders();
			for(int algo=1;algo<=Config.algoCount && algo<headers.length;algo++){
				algoMap = new HashMap<Integer,Double>();
				keyOrdered = new LinkedList<Integer>();
				for(int row=0;row<fileAsList.size();row++){
					eachRow = fileAsList.get(row);
					algoMap.put(row, Double.parseDouble(eachRow.get(algo)));
				}
				algoMap = (HashMap<Integer, Double>) Config.helper.sortMapByValue(algoMap);
				for (Map.Entry<?, ?> entry : algoMap.entrySet()) {
					keyOrdered.add((Integer) entry.getKey());
				}
				algoOrders.add((algo-1),algoMap);
				algoKeyOrdered.add((algo-1),keyOrdered);
			}
			for(int i=0;i<algoKeyOrdered.size();i++){
				for(int j=0;j<i && j<algoKeyOrdered.size();j++){
					distMat[fileNo-1][i][j] = Config.helper.getVectorDistance(algoKeyOrdered.get(i),algoKeyOrdered.get(j) , 1.0);
					if(distMat[fileNo-1][i][j]<minDist)
						minDist = distMat[fileNo-1][i][j];
					if(distMat[fileNo-1][i][j]>maxDist)
						maxDist = distMat[fileNo-1][i][j];
				}
			}
			rangeMax = Math.floor((maxDist-minDist)/Config.algoCount+minDist);
			for(int i=0;i<algoKeyOrdered.size();i++){
				for(int j=0;j<i && j<algoKeyOrdered.size();j++){
					if(distMat[fileNo-1][i][j]>=minDist && distMat[fileNo-1][i][j]<=rangeMax){
						if(!clusterSet.contains(i))
							clusterSet.add(i);
						if(!clusterSet.contains(j))
							clusterSet.add(j);
					}
				}
			}
			for(int i=0;i<Config.algoCount;i++){
				if(clusterSet.contains(i))
					weightMat[i] += 1;
				else
					weightMat[i] += 0;
			}
		}
		String weightStr = "";
		double netWeight = 0.0;
		for(int i=0;i<Config.algoCount;i++){
			netWeight += weightMat[i];
		}
		for(int i=0;i<Config.algoCount;i++){
			weightStr += String.valueOf(weightMat[i]/netWeight) + " ";
		}
		TXTWriter txtWriter = new TXTWriter(Config.weightOutputPath);
		txtWriter.writeToFile(weightStr);
		long lEndTime = new Date().getTime();
		System.out.println("\n Completed Preprocessing in : " + (lEndTime-lStartTime) + " milliseconds");
	}
	
}
