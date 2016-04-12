package mysql; 
import java.sql.*; 
import java.util.ArrayList; 
import java.util.Arrays; 
import java.util.Collections; 
import java.util.Date; 
import java.util.HashMap; 
import java.util.HashSet; 
import java.util.Iterator; 
import java.util.LinkedList; 
import java.util.List; 
import java.util.Map; 
import java.util.Set; 
import java.util.TreeMap; 
import java.util.TreeSet; 
 
import commandcentral.Config; 
import preprocessing.SemanticPreProcess; 
import semantics.Relevance; 
 
 
 
public class Apprentice  
{ 
    private Connectdb conn; 
    private String remainingQuestion; 
    private List<String> relevantTags; 
    private Map<Integer, List<LinkedList<Integer>>> powerset = new HashMap<>(); 
    private List<String> tags; 
    private Set<String> simpleTags; 
    private Map<String, List<String>> complexTags; 
    private Connection myConn = null; 
    private int maxNumberOfTags = Config.maxTagsOutput; 
    private int numQuestions = 0; 
    private int localNumQuestions = 0; 
     
    public Apprentice() 
    { 
        conn = new Connectdb(); 
        myConn = conn.getConnection(); 
        remainingQuestion = ""; 
         
        relevantTags = new ArrayList<String>(); 
         
        simpleTags = new TreeSet<String>(); 
        complexTags = new HashMap<String, List<String>>(); 
        try { 
            tags = getTagNamesFromDB(); 
        } catch (SQLException e) { 
            e.printStackTrace(); 
        } 
        if(tags != null) { 
            createTagHashAndSet(tags, simpleTags, complexTags); 
        } 
         
    } 
     
    public ResultSet queryRun(String query)  
    { 
        ResultSet rs=null; 
        try { 
            if(myConn!=null){ 
            Statement myStmt = myConn.createStatement(); 
            rs = myStmt.executeQuery(query); 
            } 
        } catch (SQLException e) { 
            e.printStackTrace(); 
        } 
        return rs; 
    } 
     
    public int updateRun(String query)  
    { 
        int rowsUpdated=-1; 
        try { 
            if(myConn!=null){ 
            Statement myStmt = myConn.createStatement(); 
            rowsUpdated = myStmt.executeUpdate(query); 
            } 
        } catch (SQLException e) { 
            e.printStackTrace(); 
        } 
         
        return rowsUpdated; 
    } 
     
    public List<Question> getQuestionTitles() throws SQLException { 
        List<Question> questionTitles = new ArrayList<Question>(); 
        String query = "SELECT * FROM question"; 
        ResultSet rs = queryRun(query); 
        while(rs.next()) { 
            questionTitles.add(new Question(String.valueOf(rs.getInt("qid")),rs.getString("title"),String.valueOf(rs.getInt("uid")),rs.getString("keywords"))); 
        } 
        return questionTitles; 
    } 
     
    public String getRemainingQuestion() { 
        return remainingQuestion; 
    } 
     
    private List<String> getTagNamesFromDB() throws SQLException { 
        String sqlQuery = "SELECT * FROM tag"; 
        ResultSet rs = queryRun(sqlQuery); 
        List<String> tagNames = new ArrayList<String>(); 
        if(rs!=null){ 
        while(rs.next()) { 
            tagNames.add(rs.getString("tname")); 
        } 
        } 
        return tagNames; 
    } 
     
    public List<String> getQuestionTags(String question) { 
        relevantTags = new ArrayList<String>(); 
        remainingQuestion = ""; 
        Set<String> wordsInTags = new TreeSet<String>(); 
        String[] questionWords = question.replace("-", " ").split(" "); 
        int currentWord = 0; 
        while(currentWord < questionWords.length) { 
             
            //System.out.println("|" + questionWords[currentWord] + "|"); 
            if(simpleTags.contains((questionWords[currentWord]).toLowerCase())) { 
                relevantTags.add((questionWords[currentWord]).toLowerCase()); 
                if(!wordsInTags.contains((questionWords[currentWord]).toLowerCase())) { 
                    wordsInTags.add((questionWords[currentWord]).toLowerCase()); 
                } 
            } 
            if((currentWord != questionWords.length - 1) && complexTags.containsKey(questionWords[currentWord])) { 
                List<String> remainingTagParts = complexTags.get(questionWords[currentWord]); 
                //System.out.println("HEY" + remainingTagParts); 
                for(int i = 0; i < remainingTagParts.size(); i++) { 
                     
                    String[] currentRemainingTagParts = remainingTagParts.get(i).split(" "); 
                    //System.out.println("LENGTH" + currentRemainingTagParts.length); 
                    int j = 0; 
                    currentWord++; 
                    boolean isMatching = true; 
                    while(isMatching && j < currentRemainingTagParts.length) { 
                        if(questionWords[currentWord].equalsIgnoreCase(currentRemainingTagParts[j])) { 
                            j++; 
                            currentWord++; 
                        } else { 
                            isMatching = false; 
                        } 
                        if(currentWord == questionWords.length && j != currentRemainingTagParts.length) { 
                            isMatching = false; 
                        } 
                    } 
                    currentWord = currentWord - j - 1; 
                    if(isMatching) { 
                        //System.out.println("ADDING" + remainingTagParts.get(i)); 
                        String complexTag = (questionWords[currentWord] + " " + remainingTagParts.get(i)).replace(" ", "-");  
                        relevantTags.add(complexTag); 
                        for(int k = 0; k < currentRemainingTagParts.length; k++) { 
                            if(!wordsInTags.contains(currentRemainingTagParts[k])) { 
                                wordsInTags.add(currentRemainingTagParts[k]); 
                            } 
                        } 
                        wordsInTags.add(questionWords[currentWord]); 
                    } 
                     
                } 
            } 
            currentWord++; 
        } 
        //printRelevantTags(relevantTags); 
        for(int i = 0; i < questionWords.length; i++) { 
            if(!wordsInTags.contains(questionWords[i].toLowerCase())) { 
                remainingQuestion += questionWords[i] + " "; 
            } 
        } 
        //System.out.println(remainingQuestion); 
        return relevantTags; 
    } 
     
     
    private void createTagHashAndSet(List<String> tags, Set<String> simpleTags, Map<String, List<String>> complexTags) {         
        for(int i = 0; i < tags.size(); i++) { 
            String tagName = tags.get(i); 
            if(!tags.get(i).contains("-")) { 
                simpleTags.add(tags.get(i)); 
            } else { 
                String[] tagNameParts = tags.get(i).split("-"); 
                if(complexTags.containsKey(tagNameParts[0])) { 
                    List<String> remainingPartOfTag = complexTags.get(tagNameParts[0]); 
                    remainingPartOfTag.add(tagName.substring(tagNameParts[0].length() + 1, tagName.length()).replace("-", " ")); 
                } else { 
                    List<String> remainingPartOfTag = new ArrayList<String>(); 
                    remainingPartOfTag.add(tagName.substring(tagNameParts[0].length() + 1, tagName.length()).replace("-", " ")); 
                    complexTags.put(tagNameParts[0], remainingPartOfTag); 
                } 
            } 
        } 
        //printSimpleTags(simpleTags); 
        //printComplexTags(complexTags); 
    } 
     
    @SuppressWarnings("unused") 
    private void printRelevantTags(List<String> relevantTags) { 
        System.out.println("The relevant tags are:"); 
        for(int i = 0; i < relevantTags.size(); i++) { 
            System.out.println(relevantTags.get(i)); 
        } 
    } 
     
    @SuppressWarnings("unused") 
    private void printComplexTags(Map<String, List<String>> complexTags) { 
        for(String tagKey : complexTags.keySet()) { 
            List<String> remainingPartOfTag = complexTags.get(tagKey); 
            System.out.println(tagKey + "-> "); 
            for(int i = 0; i < remainingPartOfTag.size(); i++) { 
                System.out.println(remainingPartOfTag.get(i) + " "); 
            } 
        } 
    } 
     
    @SuppressWarnings("unused") 
    private void printSimpleTags(Set<String> simpleTags) { 
        Iterator<String> itr = simpleTags.iterator(); 
        while(itr.hasNext()) { 
            System.out.println(itr.next()); 
        } 
    } 
     
    public void addKeywordsToQuestion(SemanticPreProcess preProcess) throws SQLException{ 
        ResultSet rs=null; 
        String ques=null; 
        long lStartTime = new Date().getTime(); 
        int doneAmount = 0 ,everyPercentCount = 0, statusPrint = 1, noOfRows = 0; 
        int qid; 
        String query="Select * from question where keywords=''"; 
        rs=queryRun(query); 
        rs.last(); 
        noOfRows = rs.getRow(); 
        rs.beforeFirst(); 
        everyPercentCount = noOfRows*Config.statusDisplayPercent/100; 
        if(rs!=null){ 
          while(rs.next()){ 
            ques=rs.getString("title"); 
            qid=rs.getInt("qid"); 
            List<String>Tokens = preProcess.startPreprocessing(ques, Config.basicPreProcessOrder);  
            String quesTokens = String.join(",",Tokens ); 
            updateRun("UPDATE question SET keywords = \"" + quesTokens + "\" where qid="+ qid); 
             
            doneAmount++; 
            if(doneAmount >= (everyPercentCount*statusPrint)){ 
                int loopLimit = ((int)Math.floor(doneAmount/(everyPercentCount/Config.statusDisplayPercent))); 
                System.out.print("|"); 
                for(int i=0;i<loopLimit;i++) 
                    System.out.print("="); 
                for(int i=100;i>loopLimit;i--) 
                    System.out.print(" "); 
                System.out.print("| - "+loopLimit+" percent done\r"); 
                statusPrint++; 
            } 
          } 
        } 
        long lEndTime = new Date().getTime(); 
        System.out.println("\n Completed Preprocessing in : " + (lEndTime-lStartTime) + " milliseconds"); 
    } 
     
    public List<Question> getQuestionTitlesWithTags(String tags) throws SQLException { 
        String conditionsForQuery = tags.replaceAll(",", "' OR tname='"); 
        conditionsForQuery = "tname='" + conditionsForQuery + "'"; 
        String query = "SELECT * FROM question WHERE qid IN " 
                + "(SELECT DISTINCT qid FROM questiontag WHERE tid IN " 
                + "(SELECT tid FROM tag WHERE " + conditionsForQuery + "))"; 
        List<Question> questionTitles = new ArrayList<Question>(); 
        ResultSet rs = queryRun(query); 
        if(rs != null) { 
            while(rs.next()) { 
                questionTitles.add(new Question(String.valueOf(rs.getInt("qid")),rs.getString("title"),String.valueOf(rs.getInt("uid")),rs.getString("keywords"))); 
            } 
        } 
        return questionTitles; 
    } 
 
    public HashSet<String> getTagsForQuestion(int qid) throws SQLException 
     { 
         ResultSet rs=queryRun("Select tid from questiontag where qid=" + qid); 
         List<Integer>tids=new LinkedList<Integer>(); 
         HashSet<String>tagList=new HashSet<String>(); 
          
         while(rs.next()) 
         { 
             tids.add(rs.getInt("tid")); 
         } 
         for(int i=0;i<tids.size();i++) 
         { 
             rs=queryRun("Select tname from tag where tid=" + tids.get(i)); 
             //Tag tag=new Tag(rs.getString("tname")); 
             rs.next(); 
             tagList.add(rs.getString("tname"));      
         } 
         return tagList; 
     } 
     
    public int getTagId(String tagName){ 
        String query="SELECT tid from tag where tname=" + "\"" + tagName + "\""; 
        int tid=-1; 
        ResultSet rs=queryRun(query); 
        try { 
            rs.next(); 
            tid=rs.getInt("tid"); 
        } catch (SQLException e) { 
            e.printStackTrace(); 
        } 
        return tid; 
    } 
     
    public Map<Integer,Double> getConfidenceForTags(String Tags, int limit) throws SQLException{ 
        Map<Integer,Double> TM=new TreeMap<Integer,Double>(); 
        List<String> tagList = Arrays.asList(Tags.split(",")); 
        List<Integer> tids=new LinkedList<Integer>(); 
        ResultSet rs = null; 
        for(int i=0;i<tagList.size();i++) 
            tids.add(getTagId(tagList.get(i))); 
        powerset(tids); 
        List<String> allQueries = getConfidenceQuery(powerset, limit); 
        for(int i=0;i<allQueries.size();i++){ 
            rs = queryRun(allQueries.get(i)); 
            if(rs!=null){ 
                while(rs.next()){ 
                    //double ratio=TM.get(rs.getInt("tagid")); 
                    if(TM.containsKey(rs.getInt("tagid"))) 
                    { 
                        if(TM.get(rs.getInt("tagid"))<rs.getDouble("confidence")) 
                        { 
                            TM.remove(rs.getInt("tagid")); 
                            TM.put(rs.getInt("tagid"), rs.getDouble("confidence")); 
                        } 
                    } 
                    TM.put(rs.getInt("tagid"), rs.getDouble("confidence")); 
                } 
            } 
            System.out.println("Query "+(i+1)+" done!"); 
        } 
        for(int i=0;i<tids.size();i++) 
        { 
            if(TM.containsKey(tids.get(i))) 
            { 
                TM.remove(tids.get(i)); 
            } 
        } 
        return Config.helper.sortMapByValue(TM); 
    } 
    void powerset(List<Integer> src) { 
        powerset(new LinkedList<Integer>(), src); 
    } 
     
    void powerset(LinkedList<Integer> prefix, List<Integer> src) { 
        if (src.size() > 0) { 
            prefix = new LinkedList<>(prefix); //create a copy to not modify the orig 
            src = new LinkedList<>(src); //copy 
            Integer curr = src.remove(0); 
            collectResult(prefix, curr); 
            powerset(prefix, src); 
            prefix.add(curr); 
            powerset(prefix, src); 
        } 
    } 
     
    void collectResult(LinkedList<Integer> prefix, Integer curr) { 
        prefix = new LinkedList<>(prefix); //copy 
        prefix.add(curr); 
        List<LinkedList<Integer>> addTo; 
        if (powerset.get(prefix.size()) == null) { 
            List<LinkedList<Integer>> newList = new LinkedList<>(); 
            addTo = newList; 
        } else { 
            addTo = powerset.get(prefix.size()); 
        } 
        addTo.add(prefix); 
        powerset.put(prefix.size(), addTo); 
    } 
    public List<String> getConfidenceQuery(Map<Integer, List<LinkedList<Integer>>> powerset, int limit){ 
        List<String> queries = new LinkedList<String>(); 
        String query = ""; 
        for(int i=1;i<=powerset.size();i++){ 
            List<LinkedList<Integer>> outerList= new LinkedList<LinkedList<Integer>>(); 
            outerList = powerset.get(i); 
            for(int j=0;j<outerList.size();j++){ 
                List<Integer> innerList = new LinkedList<Integer>(); 
                innerList = outerList.get(j); 
                query = ""; 
                query += "SELECT qt1.tid AS tagid,"; 
                query += "count(qt"+(innerList.size()+1)+".qid)/(select count(*) FROM "; 
                for(int k=0;k<innerList.size();k++){ 
                    query += "questiontag qti"+(k+1)+" "; 
                    if(k!=(innerList.size()-1)){ 
                        query += ", "; 
                    } 
                } 
                query += "WHERE "; 
                for(int k=0;k<innerList.size();k++){ 
                    query += "qti"+(k+1)+".tid="+innerList.get(k)+" "; 
                    if(k!=(innerList.size()-1)){ 
                        query += "AND "; 
                    } 
                } 
                for(int k=1;k<innerList.size();k++){ 
                    query += "AND qti"+(k)+".qid=qti"+(k+1)+".qid "; 
                } 
                query += " ) AS confidence FROM "; 
                for(int k=0;k<(innerList.size()+1);k++){ 
                    query += "questiontag qt"+(k+1)+" "; 
                    if(k!=innerList.size()){ 
                        query += ", "; 
                    } 
                } 
                query += "WHERE "; 
                for(int k=0;k<innerList.size();k++){ 
                    query += "qt"+(k+2)+".tid="+innerList.get(k)+" "; 
                    if(k!=(innerList.size()-1)){ 
                        query += "AND "; 
                    } 
                } 
                for(int k=0;k<innerList.size();k++){ 
                    query += "AND qt1.tid!=qt"+(k+2)+".tid "; 
                } 
                for(int k=0;k<innerList.size();k++){ 
                    query += "AND qt"+(k+1)+".qid=qt"+(k+2)+".qid "; 
                } 
                query += "GROUP BY qt1.tid "; 
                query += "ORDER BY count(qt"+(innerList.size()+1)+".qid) DESC "; 
                query += "LIMIT "+limit; 
                queries.add(query); 
            } 
        } 
        return queries; 
    } 
     
    public List<RelevantTag> generateRelevantTags(String tags, String userKeywords) throws SQLException { 
        List<Tag> relevanceTags = new ArrayList<Tag>(); 
        int limit = 10; 
        Map<Integer,Double> highConfidenceTags = getConfidenceForTags(tags, limit); 
        System.out.println("Got high confidence Tags...."); 
        Set<RelevantQuestion> relevantQuestions = getQuestionsWithTags(tags, highConfidenceTags, userKeywords); 
        System.out.println("Number of questions found " + relevantQuestions.size()); 
        List<RelevantQuestion> reducedRelevantQuestions = new ArrayList<RelevantQuestion>(); 
        Iterator<RelevantQuestion> relevantQuestionsIterator = relevantQuestions.iterator(); 
        int numTags = 0; 
        while((numTags < this.maxNumberOfTags) && (relevantQuestionsIterator.hasNext())) { 
            RelevantQuestion currentRelevantQuestion = relevantQuestionsIterator.next(); 
            reducedRelevantQuestions.add(currentRelevantQuestion); 
            System.out.println("Number of Tags " + numTags); 
            for(Tag tag : currentRelevantQuestion.getUncommonTags()) { 
                if(!relevanceTags.contains(tag)) { 
                    relevanceTags.add(tag); 
                    numTags++; 
                } 
            } 
            //numTags += currentRelevantQuestion.getUncommonTags().size(); 
        } 
        List<RelevantTag> relevantTags = new ArrayList<RelevantTag>(); 
        System.out.println("Maximum number of Tags : " + maxNumberOfTags); 
        System.out.println("Number of questions : " + reducedRelevantQuestions.size()); 
        setSemanticClosenessAndSort(reducedRelevantQuestions, userKeywords); 
        for(RelevantQuestion relevantQuestion : reducedRelevantQuestions) { 
            for(Tag tag : relevantQuestion.getUncommonTags()) { 
                RelevantTag currentRelevantTag = new RelevantTag(tag); 
                if(!relevantTags.contains(currentRelevantTag)) { 
                    relevantTags.add(currentRelevantTag); 
                } 
            } 
        } 
        System.out.println("The number of output tags: " + relevantTags.size()); 
        printRelevantTagsFromCluster(relevantTags); 
        return relevantTags; 
    }  
 
    public void setSemanticClosenessAndSort(List<RelevantQuestion> reducedRelevantQuestions, String userKeywords) { 
        int i = 0; 
        for(RelevantQuestion relevantQuestion : reducedRelevantQuestions) { 
            System.out.println("Sending question " + relevantQuestion.getQuestion().getQuestionId()); 
            double score = getSemanticCloseness(relevantQuestion.getQuestion().getKeywords(), userKeywords); 
            relevantQuestion.setSemanticCloseness(score); 
            i++; 
            System.out.println("Num Ques : " + i); 
        } 
        Collections.sort(reducedRelevantQuestions); 
    } 
     
    public int getTidForTag(String tagName) throws SQLException { 
        int retVal = -1; 
        ResultSet rs = null; 
        String query = "SELECT tid FROM tag WHERE tname='" + tagName + "'"; 
        rs = queryRun(query); 
        if(rs != null) { 
            rs.next(); 
            retVal = rs.getInt("tid"); 
            //System.out.println("Current Tag ID: " + currentTagId); 
        } 
        return retVal; 
    } 
     
public Tag getTagWithTid(int tid) throws SQLException { 
        String query = "SELECT tname, occurence FROM tag WHERE tid=" + tid; 
        ResultSet rs = queryRun(query); 
        Tag retVal = null; 
        if(rs != null) { 
            rs.next(); 
            int occurence = rs.getInt("occurence"); 
            String tagName = rs.getString("tname"); 
            retVal = new Tag(tagName, occurence, tid + ""); 
        } 
        return retVal;     
    } 
 
    public void findOtherTagsForRelevantQuestion(int currentTagId, RelevantQuestion relevantQuestion, String qid, Map<Integer, Double> highConfidenceTags) throws SQLException { 
        //System.out.println("Finding other tags for questions " + qid); 
        String query = "SELECT tid FROM questiontag WHERE qid=" + Integer.parseInt(qid); 
        ResultSet rs = queryRun(query); 
        if(rs != null) { 
            //System.out.println("1"); 
            while(rs.next()) { 
                int tagId = rs.getInt("tid"); 
                Tag currentTag = getTagWithTid(tagId); 
                if(currentTagId != tagId) { 
                    //System.out.println("Here " + tagName); 
                    relevantQuestion.addUncommonTag(currentTag); 
                    if(highConfidenceTags.containsKey(tagId)) { 
                        currentTag.setConfidencePercentage(highConfidenceTags.get(tagId)); 
                        relevantQuestion.increaseTotalConfidencePercentage(highConfidenceTags.get(tagId)); 
                    } 
                } else { 
                    relevantQuestion.addCommonTag(currentTag); 
                } 
            } 
        } 
    } 
 
public double getSemanticCloseness(String userKeywords, String otherKeywords) { 
        List<String> tokenUser = new LinkedList<String>(); 
        List<String> tokenOther = new LinkedList<String>(); 
        tokenUser = Config.helper.stringToList(userKeywords, ","); 
        tokenOther = Config.helper.stringToList(otherKeywords, ","); 
        List<String> tokenUnion = Config.helper.listUnion(tokenUser, tokenOther); 
        //List<Double> vect1 = Config.vectorizer.weightedVectorize(tokenUser,tokenUnion,Config.helper.getAlgoObjList(),Config.helper.getAlgoMax(),Config.algoCount,Config.semanticWeight); 
        //List<Double> vect2 = Config.vectorizer.weightedVectorize(tokenOther,tokenUnion,Config.helper.getAlgoObjList(),Config.helper.getAlgoMax(),Config.algoCount,Config.semanticWeight); 
        List<Double> vect1 = Config.vectorizer.vectorize(tokenUser,tokenUnion,new Relevance(Config.db, Config.rc),Config.rcMax); 
        List<Double> vect2 = Config.vectorizer.vectorize(tokenOther,tokenUnion,new Relevance(Config.db, Config.rc),Config.rcMax); 
         
        Double score = Config.scorer.cosSim(vect1, vect2, tokenUnion.size()); 
        return score; 
    } 
 
public void updateRelevantQuestions(int currentTagId, String qid, Map<String, RelevantQuestion> relevantQuestions, Set<RelevantQuestion> setRelevantQuestions, Map<Integer, Double> highConfidenceTags, String userKeywords) throws SQLException { 
        if(!relevantQuestions.containsKey(qid)) { 
            //Get other details of the question 
            String query = "SELECT * FROM question WHERE qid=" + Integer.parseInt(qid); 
            ResultSet rs = queryRun(query); 
            if(rs != null) { 
                rs.next(); 
                //System.out.println("QID : " + qid); 
                numQuestions++; 
                System.out.println("Number of questions done " + numQuestions); 
                String title = rs.getString("title"); 
                String uid = rs.getInt("uid") + ""; 
                String keywords = rs.getString("keywords"); 
                Question currentQuestion = new Question(qid, title, uid, keywords); 
                //Get semantic closeness 
                //double semanticCloseness = getSemanticCloseness(userKeywords,keywords); 
                double semanticCloseness = 0; 
                RelevantQuestion currentRelevantQuestion = new RelevantQuestion(currentQuestion, semanticCloseness); 
                //hereee 
                findOtherTagsForRelevantQuestion(currentTagId, currentRelevantQuestion, qid, highConfidenceTags); 
                //System.out.println("CURRENT "); 
                //currentRelevantQuestion.printRelevantQuestion(); 
                relevantQuestions.put(qid, currentRelevantQuestion); 
                //setRelevantQuestions.add(currentRelevantQuestion); 
            } 
        } else { 
            RelevantQuestion currentRelevantQuestion = relevantQuestions.get(qid); 
            //setRelevantQuestions.remove(currentRelevantQuestion); 
            //can be avoided 
            Tag tagToRemove = getTagWithTid(currentTagId); 
            currentRelevantQuestion.removeUncommonTag(tagToRemove); 
            currentRelevantQuestion.addCommonTag(tagToRemove); 
            //relevantQuestions.put(qid, currentRelevantQuestion); 
            //setRelevantQuestions.add(currentRelevantQuestion); 
        } 
    } 
 
public void findRelevantQuestionsForTagId(int tagId, Map<String, RelevantQuestion> relevantQuestions, Set<RelevantQuestion> setRelevantQuestions, Map<Integer, Double> highConfidenceTags, String userKeywords) throws SQLException { 
        String query = "SELECT qid FROM questiontag WHERE tid=" + tagId; 
        ResultSet rs = queryRun(query); 
        if(rs != null) { 
            while(rs.next()) { 
                //Get current question id 
                String currentQuestionId = rs.getInt("qid") + ""; 
                //System.out.println(currentQuestionId); 
                //Check if the question is already present in the map 
                updateRelevantQuestions(tagId, currentQuestionId, relevantQuestions, setRelevantQuestions, highConfidenceTags, userKeywords); 
                localNumQuestions++; 
                System.out.println("Num questions seen : " + localNumQuestions); 
            } 
        } 
    } 
 
    public Set<RelevantQuestion> getQuestionsWithTags(String tags, Map<Integer, Double> highConfidenceTags, String userKeywords) throws SQLException { 
        String[] tagList = tags.split(","); 
        int currentTagId = -1; 
        Map<String, RelevantQuestion> relevantQuestions = new TreeMap<String, RelevantQuestion>(); 
        Set<RelevantQuestion> setRelevantQuestions = new TreeSet<RelevantQuestion>(); 
        for(int i = 0; i < tagList.length; i++) { 
            currentTagId = getTidForTag(tagList[i]); 
            if(currentTagId != -1) { 
                localNumQuestions = 0; 
                //Get the questions containing the current tag 
                findRelevantQuestionsForTagId(currentTagId, relevantQuestions, setRelevantQuestions, highConfidenceTags, userKeywords); 
                System.out.println("Processing done for tag " + tagList[i]); 
            } 
        } 
        createSortedSetOfRelevantQuestions(setRelevantQuestions, relevantQuestions); 
        System.out.println("Number of Questions in cluster : " + numQuestions); 
        System.out.println("Size of Set : " + setRelevantQuestions.size()); 
        return setRelevantQuestions; 
    } 
     
    public void printRelevantTagsFromCluster(List<RelevantTag> relevantTags) { 
        for(RelevantTag relevantTag : relevantTags) { 
            relevantTag.printRelevantTag(); 
        } 
    } 
     
    public void createSortedSetOfRelevantQuestions(Set<RelevantQuestion> setRelevantQuestions, Map<String, RelevantQuestion> relevantQuestions) { 
        for(String key : relevantQuestions.keySet()) { 
            RelevantQuestion relevantQuestion = relevantQuestions.get(key); 
            relevantQuestion.sortUncommonTags(); 
            setRelevantQuestions.add(relevantQuestion); 
        } 
    } 
}