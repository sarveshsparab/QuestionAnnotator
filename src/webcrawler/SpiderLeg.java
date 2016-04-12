package webcrawler;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SpiderLeg {
	
	// We'll use a fake USER_AGENT so the web server thinks the robot is a normal web browser.
    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    private List<String> links = new LinkedList<String>();
    private Document htmlDocument;


    /**
     * This performs all the work. It makes an HTTP request, checks the response, and then gathers
     * up all the links on the page. Perform a searchForWord after the successful crawl
     * 
     * @param url
     *            - The URL to visit
     * @return whether or not the crawl was successful
     */
    public boolean crawl(String url)
    {

    	boolean retVal=true;
        try
        {
        	System.setProperty("http.proxyHost", "172.31.16.10");
        	System.setProperty("http.proxyPort", "8080");
            Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
            Document htmlDocument = connection.get();
            this.htmlDocument = htmlDocument;
            if(connection.response().statusCode() == 200) // 200 is the HTTP OK status code
                                                          // indicating that everything is great.
            {
                //System.out.println("\n**Visiting** Received web page at " + url);
            }
            if(!connection.response().contentType().contains("text/html"))
            {
                //System.out.println("**Failure** Retrieved something other than HTML");
                retVal = false;
            }
            Elements linksOnPage = htmlDocument.select("a[href]");
            //System.out.println("Found (" + linksOnPage.size() + ") links");
            for(Element link : linksOnPage)
            {
                this.links.add(link.absUrl("href"));
            }
        }
        catch(IOException ioe)
        {
            // We were not successful in our HTTP request
            retVal = false;
            //System.out.println("HTTP ERROR");
        }
        
        if( url.contains("stackoverflow.com/questions/") == false )
        {
        	retVal = false;
        }
        
        if( retVal == true )
        {
        	Element questionHeader = htmlDocument.getElementById("question-header");
        	if( questionHeader == null )
        	{
        		retVal = false;
        	}
        	else
        	{
	        	Elements questionElems = questionHeader.getElementsByClass("question-hyperlink");
	        	if( questionElems.first() == null )
	        	{
	        		retVal = false;
	        	}
        	}
        }
        return retVal;
    }
    
    
    public String retrieveQues()
    {
    	//return "DUMMY QUES";
    	
    	String retVal;
    	Elements questionElems = htmlDocument.getElementsByClass("question-hyperlink");
    	if( questionElems.first() == null )
    	{
    		retVal = "DUMMY";
    	}
    	else
    	{
    		retVal = questionElems.first().html();
    	}
    	retVal = retVal.replace(',', ' ');
    	return retVal;
    }
    
    
    public Set<String> retrieveTags()
    {
    	/*Set<String> retVal = new HashSet<String>();
    	retVal.add("tag1");
    	retVal.add("tag2");
    	return retVal;
    	*/
    	
    	Set<String> retVal = new HashSet<String>();
    	Elements tagElements = htmlDocument.getElementsByClass("post-tag");  
		for (Element tagElem : tagElements) {
		  
		    String tagValue;
		    if( tagElem == null )
		    {
		    	tagValue = "DUMMY";
		    }
		    else
		    {
		    	tagValue = tagElem.html();
		    }
		    retVal.add(tagValue);
		}
		return retVal;
    	
    }
    
    public String retrieveDescription()
    {
    	//return "DUMMY QUES";
    	
    	String retVal;
    	Elements descriptionElems = htmlDocument.getElementsByClass("post-text");
    	if( descriptionElems.first() == null )
    	{
    		retVal = "DUMMY";
    	}
    	else
    	{
    		retVal = descriptionElems.first().html();
    	}
    	return retVal;
    }
    
    public UserDetails retrieveUserDetails()
    {
    	UserDetails retVal = new UserDetails();
    	
    	Elements userInfo = htmlDocument.getElementsByClass("user-info");
    	for (Element infoElem : userInfo) {
  		  
		    if( infoElem != null )
		    {
		    	String html = infoElem.html();
		    	if( html.contains("asked") )
		    	{
		    		int index = html.indexOf("<a href=\"/users/") + 16;
		    		retVal.userId=0;
		    		while( html.charAt(index) != '/' )
		    		{
		    			retVal.userId = retVal.userId*10 + (html.charAt(index++)-'0');
		    		}
		    		index++;
		    		retVal.userName="";
		    		while( html.charAt(index) != '\"' )
		    		{
		    			retVal.userName = retVal.userName + html.charAt(index++);
		    		}
		    		if( retVal.userId < 0 )
		    		{
		    			retVal.userId = -1;  // USER ACCOUNT DELETED.
		    		}
		    	}
		    }
		}
    	
    	return retVal;
    }
    
    
    public List<String> getLinks()
    {
        return this.links;
    }

}
