package filehandling;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import com.csvreader.CsvWriter;

public class CSVWriter {
	private String filePath;
	
	public CSVWriter(String filePath) {
		this.filePath = filePath;
	}
	
	public void writeToFile(String title,List<String> headers, List<String> rows,String delimiter){
		
		boolean alreadyExists = new File(filePath).exists();
		Pattern pattern = Pattern.compile(delimiter);
		try {
			CsvWriter csvOutput = new CsvWriter(new FileWriter(filePath, true), ',');
			if (!alreadyExists){
				if(title!=null){
					csvOutput.write(title);
					csvOutput.endRecord();
				}
				if(headers!=null){
					for(int i=0;i<headers.size();i++)
						csvOutput.write(headers.get(i));
					csvOutput.endRecord();
				}
			}
			for(int i =0;i<rows.size();i++){
				List<String> eachRow = new LinkedList<String>(Arrays.asList(pattern.split(rows.get(i))));
				for(int j=0;j<eachRow.size();j++){
					csvOutput.write(eachRow.get(j));
				}
				csvOutput.endRecord();
			}
			csvOutput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
