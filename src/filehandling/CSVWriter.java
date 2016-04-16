package filehandling;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import com.csvreader.CsvWriter;
/**
 * @category Class
 * @purpose FOr CSV files writing
 * @author Sarvesh
 */
public class CSVWriter {
	private String filePath;
	private File file;
	/**
     * @category Constructor 
     * @argument filePath : Sets the path from where to read the file
     * @author Sarvesh
     */
	public CSVWriter(String filePath) {
		this.filePath = filePath;
	}
	/**
     * @category Constructor 
     * @argument file : Uses a File class object to Set the path from where to read the file
     * @author Sarvesh
     */
	public CSVWriter(File file) {
		this.file = file;
		this.filePath = file.getPath();
	}
	/**
     * @category Function
     * @argument title : String to be written as a title
     * @argument headers : Linked list of header for the CSV file
     * @argument rows : Linked list of header for the CSV file
     * @argument headers : Linked list of header for the CSV file
     * @return A linked list of rows, which in themselves are linked lists of cells
     * @author Sarvesh
     */
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
