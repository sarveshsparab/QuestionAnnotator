package filehandling;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.csvreader.CsvReader;

public class CSVReader {
	private String filePath;
	private String[] headers;
	public String[] getHeaders() {
		return headers;
	}
	
	public CSVReader(String filePath) {
		this.filePath = filePath;
		headers = null;
	}
	
	public List<LinkedList<String>> readRowsIntoList(boolean titlePresent, boolean headersPresent){
		List<LinkedList<String>> rowsList = new LinkedList<LinkedList<String>>();

		try {
			CsvReader csvReader = new CsvReader(filePath);
			if(titlePresent){
				csvReader.skipLine();
			}
			if(headersPresent){
				csvReader.readHeaders();
				headers = csvReader.getHeaders();
			}
			while (csvReader.readRecord()){
				LinkedList<String> row = new LinkedList<String>();
				for(int i=0;i<headers.length;i++){
					row.add(i, csvReader.get(csvReader.getHeader(i)));
				}
				rowsList.add(row);
			}
			csvReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return rowsList;
	}
}

