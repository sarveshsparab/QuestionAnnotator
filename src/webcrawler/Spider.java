package webcrawler;
import java.util.HashSet;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.Scanner;

import java.io.*;

public class Spider {
	
	//private static final int MAX_PAGES_TO_SEARCH = 50;
	private static final int MAX_QUESTIONS_TO_CRAWL = 50000;
	private static final String FilePathPrefix = "C:/Users/Impulse/Desktop/Crawled Data/";
	
	private Set<String> pagesVisited = new HashSet<String>();
    private List<String> pagesToVisit = new LinkedList<String>();
    
    private Set<Integer> questionIdSet = new HashSet<Integer>();
    private Set<Integer> personIdSet = new HashSet<Integer>();
    private Map<String,Integer> tag2Id = new HashMap<String,Integer>();
    
    private String refineUrl( String url )
    {
    	String retVal=url;
    	int i = url.indexOf("/?");
    	if( i != -1 )
    	{
    		retVal = url.substring( 0 , i  );
    	}
    	return retVal;
    }
    
    private String refineTag( String tag )
    {
    	String retVal=tag;
    	int i = tag.lastIndexOf(">");
    	if( tag.contains("<img") && i != -1 )
    	{
    		retVal = tag.substring(i+1);
    	}
    	return retVal;
    }
    
    private void populateTag2IdMap()
    {
    	try
    	{
    		// Open the file
    		FileInputStream fstream = new FileInputStream(FilePathPrefix + "Tags.csv");
    		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

    		String strLine;
    		
    		strLine = br.readLine();	//Initial Line Skipped

    		//Read File Line By Line
    		while ((strLine = br.readLine()) != null)
    		{
    			strLine = strLine.trim();
    			int i,id = 0;
    			for( i=0 ; strLine.charAt(i) != ',' ; ++i )
    			{
    				id = id*10 + ( strLine.charAt(i)-'0' );
    			}
    			
    			++i;
    			String tag="";
    			while( strLine.charAt(i) != ',' )
    			{
    				tag = tag + strLine.charAt(i);
    				++i;
    			}
    			
    			//System.out.println("#"+ tag + "##" + id);
    			
    			tag2Id.put(tag, id);
    		}
    		
    		br.close();
    	}
    	catch( Exception e )
    	{
    		e.printStackTrace();
    		System.out.println("CANNOT OPEN "+ FilePathPrefix +"Tags.csv");
    		System.exit(0);
    	}
    	
    }
    
    public void collectData( String url )
    {
    	int numPagesCrawled=0;
    	populateTag2IdMap();
    	try
    	{
    		/*
    		FileWriter fileWriter = new FileWriter(FilePathPrefix + "CrawledData.txt");
    		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
    		*/
    		FileWriter quesFileWriter = new FileWriter(FilePathPrefix + "Ques.csv");
    		BufferedWriter quesBufferedWriter = new BufferedWriter(quesFileWriter);
    		quesBufferedWriter.write( "quesId,title,personId" );
    		quesBufferedWriter.newLine();
    		
    		FileWriter userFileWriter = new FileWriter(FilePathPrefix + "Users.csv");
    		BufferedWriter userBufferedWriter = new BufferedWriter(userFileWriter);
    		userBufferedWriter.write( "personId,userName" );
    		userBufferedWriter.newLine();
    		
    		FileWriter relationFileWriter = new FileWriter(FilePathPrefix + "Relation.csv");
    		BufferedWriter relationBufferedWriter = new BufferedWriter(relationFileWriter);
    		relationBufferedWriter.write( "quesId,tagId" );
    		relationBufferedWriter.newLine();
    		
    		int questionsCrawled = 0;
    		char choice='a';
    		Scanner scan = new Scanner(System.in);
    	
	    	while( choice!='x' && questionsCrawled < MAX_QUESTIONS_TO_CRAWL )//this.pagesVisited.size() < MAX_PAGES_TO_SEARCH)
	        {
	            String currentUrl;
	            SpiderLeg leg = new SpiderLeg();
	            if(this.pagesToVisit.isEmpty())
	            {
	                currentUrl = url;
	                this.pagesVisited.add(url);
	            }
	            else
	            {
	            	currentUrl = "abc";
	            	while( ! currentUrl.contains("stackoverflow.com") )
	                currentUrl = this.nextUrl();
	            }
	            
	            System.out.println( questionsCrawled + " collected. "+numPagesCrawled+" visited.   Crawling : " + currentUrl);
		        boolean success = leg.crawl(currentUrl); // Lots of stuff happening here. Look at the crawl method in
	            ++numPagesCrawled;
		        // SpiderLeg
	            if(success)
	            {
	            	int quesId = getQuestionIdFromURL(currentUrl);
		            
	            	UserDetails details = leg.retrieveUserDetails();
	            	
	            	if( ! questionIdSet.contains( quesId ) && details.userId > 0 )
	            	{
	            		questionIdSet.add( quesId );
	            		++questionsCrawled;
	            		
		            	quesBufferedWriter.write( quesId +"," + leg.retrieveQues()
		            								+ "," + details.userId );
		            	quesBufferedWriter.newLine();
		            	
		            	if( ! personIdSet.contains(details.userId) )
		            	{
		            		
		            		userBufferedWriter.write( details.userId + "," + details.userName );
		            		userBufferedWriter.newLine();
		            		personIdSet.add(details.userId);
		            	}
		            	
		            	Set<String> tags = leg.retrieveTags();
		                for( String tag : tags )
		                {
		                	tag = refineTag(tag);
		                	
		                	if( tag2Id.containsKey(tag) )
		                	{
		                		relationBufferedWriter.write( quesId + "," + tag2Id.get(tag) );
		                		relationBufferedWriter.newLine();
		                	}
		                	
		                }
		            	
		            }
		            
		            
		            if( questionsCrawled%100 == 0 )
		            {
		            	quesBufferedWriter.close();
		            	userBufferedWriter.close();
		            	relationBufferedWriter.close();
		            	
		            	File quesFile =new File(FilePathPrefix + "Ques.csv");
		            	quesFileWriter = new FileWriter(quesFile,true);
		            	quesBufferedWriter = new BufferedWriter(quesFileWriter);
		            	
		            	File userFile =new File(FilePathPrefix + "Users.csv");
		            	userFileWriter = new FileWriter(userFile,true);
		            	userBufferedWriter = new BufferedWriter(userFileWriter);
		            	
		            	File relationFile =new File(FilePathPrefix + "Relation.csv");
		            	relationFileWriter = new FileWriter(relationFile,true);
		            	relationBufferedWriter = new BufferedWriter(relationFileWriter);
		            	
		            }
		            
		            /*
		            if( scan.hasNext() )
		            {
		            	choice = scan.next(".").charAt(0);
		            	if( choice == 'x' )
		            	{
		            		System.out.println("\n "+questionsCrawled+" questions have been crawled.");
		            		System.out.println("\n Are you sure you want to stop ? (y/n)");
		            		choice = scan.next(".").charAt(0);
		            		if( choice == 'y' )
		            			choice='x';
		            	}
		            }
		            */
	            }
	            
	            this.pagesToVisit.addAll(leg.getLinks());
	        }
	    	scan.close();
	    	//bufferedWriter.close();
	    	quesBufferedWriter.close();
	    	userBufferedWriter.close();
	    	relationBufferedWriter.close();
	    }
    	catch(IOException ex) {
            System.out.println( "Error writing to file." );
            // Or we could just do this:
            // ex.printStackTrace();
        }
    }
    
    private String nextUrl()
    {
        String nextUrl;
        do
        {
            nextUrl = this.pagesToVisit.remove(0);
            nextUrl = refineUrl( nextUrl );
            
        } while(this.pagesVisited.contains(nextUrl));
        this.pagesVisited.add(nextUrl);
        return nextUrl;
    }
    
    private int getQuestionIdFromURL( String URL )
    {
    	int retVal=0,index = URL.indexOf("/questions/") + 11;
    	
    	while( URL.charAt(index) != '/' && index < URL.length() )
    	{
    		retVal = retVal*10 + (URL.charAt(index++) - '0');
    	}
    	return retVal;
    }

}