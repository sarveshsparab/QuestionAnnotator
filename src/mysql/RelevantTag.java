package mysql;

import java.util.ArrayList;
import java.util.List;

public class RelevantTag {
    /*private String tagName;
    private String tagId;
    private int tagOccurence;
    private double semanticCloseness;
    private double intersectionPercentage;*/
    private Tag tag;
    private List<Question> questionsContainingTheTag;
    
    public RelevantTag(Tag tag) {
        this.tag = tag;
        questionsContainingTheTag = new ArrayList<Question>();
    }

    public void addQuestionContainingTheTag(Question question) {
        questionsContainingTheTag.add(question);
    }
    
    public void printRelevantTag() {
        tag.printTag();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((tag == null) ? 0 : tag.hashCode());
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
        RelevantTag other = (RelevantTag) obj;
        if (tag == null) {
            if (other.tag != null)
                return false;
        } else if (!tag.equals(other.tag))
            return false;
        return true;
    }
    
    
}