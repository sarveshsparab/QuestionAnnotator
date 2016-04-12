package mysql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RelevantQuestion implements Comparable<RelevantQuestion> {

    private Question question;
    private List<Tag> uncommonTags;
    private List<Tag> commonTags;
    //private List<Tag> highConfidenceTags;
    private double semanticCloseness;
    private double totalConfidencePercentage;
    
    public RelevantQuestion(Question question, double semanticCloseness) {
        uncommonTags = new ArrayList<Tag>();
        totalConfidencePercentage = 0;
        commonTags = new ArrayList<Tag>();
        //highConfidenceTags = new ArrayList<Tag>();
        this.question = question;
        this.semanticCloseness = semanticCloseness;
    }
    
    public void sortUncommonTags() {
        Collections.sort(uncommonTags);
    }
    
    public void increaseTotalConfidencePercentage(double amountToIncreaseBy) {
        totalConfidencePercentage += amountToIncreaseBy;
    }

    public List<Tag> getUncommonTags() {
        return uncommonTags;
    }

    public void setUncommonTags(List<Tag> uncommonTags) {
        this.uncommonTags = uncommonTags;
    }

    public List<Tag> getCommonTags() {
        return commonTags;
    }

    public void setCommonTags(List<Tag> commonTags) {
        this.commonTags = commonTags;
    }

    public void printRelevantQuestion() {
        System.out.println("--------------------------------");
        System.out.println("Question : ");
        question.printQuestion();
        System.out.println("Semantic closeness : " + semanticCloseness);
        System.out.println("Uncommon Tags Size : " + uncommonTags.size());
        for(Tag tag : uncommonTags) {
            tag.printTag();
        }
        System.out.println("Common Tags Size : " + commonTags.size());
        for(Tag tag : commonTags) {
            tag.printTag();
        }
        System.out.println("Total Confidence Percentage " + totalConfidencePercentage);
        System.out.println("--------------------------------");
    }
    
    public void removeUncommonTag(Tag tag) {
        uncommonTags.remove(tag);
    }
    
    public void addCommonTag(Tag tag) {
        commonTags.add(tag);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((question == null) ? 0 : question.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RelevantQuestion other = (RelevantQuestion) obj;
        if (question == null) {
            if (other.question != null)
                return false;
        } else if (!question.equals(other.question))
            return false;
        return true;
    }

    public void addUncommonTag(Tag tag) {
        uncommonTags.add(tag);
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public double getSemanticCloseness() {
        return semanticCloseness;
    }

    public void setSemanticCloseness(double semanticCloseness) {
        this.semanticCloseness = semanticCloseness;
    }

    public int getNumCommonTags() {
        return commonTags.size();
    }
    
    public double getTotalConfidencePercentage() {
        return totalConfidencePercentage;
    }

    public void setTotalConfidencePercentage(double totalConfidencePercentage) {
        this.totalConfidencePercentage = totalConfidencePercentage;
    }

    @Override
    public int compareTo(RelevantQuestion relevantQuestion) {
        int retVal = 0;
        if(this.getTotalConfidencePercentage() > relevantQuestion.getTotalConfidencePercentage()) {
            retVal = -1;
        } else if(this.getTotalConfidencePercentage() == relevantQuestion.getTotalConfidencePercentage()) {
            if(this.getNumCommonTags() > relevantQuestion.getNumCommonTags()) {
                retVal = -1;
            } else if (this.getNumCommonTags() == relevantQuestion.getNumCommonTags()) {
                if(this.getSemanticCloseness() > relevantQuestion.getSemanticCloseness()) {
                    retVal = -1;
                } else {
                    retVal = 1;
                }
            } else {
                retVal = 1;
            }
        } else {
            retVal = 1;
        }
        return retVal;
    }
}