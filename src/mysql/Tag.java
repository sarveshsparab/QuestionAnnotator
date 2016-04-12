package mysql;

public class Tag implements Comparable<Tag>{

    private String name;
    private int count;
    private String tagId;
    private double confidencePercentage;
    
    public Tag(String name, int count, String tId) {
        this.name = name;
        this.count = count;
        this.tagId = tId;
        this.confidencePercentage = 0;
    }
    
    public double getConfidencePercentage() {
        return confidencePercentage;
    }

    public void setConfidencePercentage(double confidencePercentage) {
        this.confidencePercentage = confidencePercentage;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((tagId == null) ? 0 : tagId.hashCode());
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
        Tag other = (Tag) obj;
        if (tagId == null) {
            if (other.tagId != null)
                return false;
        } else if (!tagId.equals(other.tagId))
            return false;
        return true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public void printTag() {
        System.out.println("Tag Name : " + name + " Occurence : " + count + " Tag ID : " + tagId + " Confidence Percentage : " + confidencePercentage);
    }

    @Override
    public int compareTo(Tag o) {
        int retVal = 0;
        if(this.getConfidencePercentage() > o.getConfidencePercentage()) {
            retVal = -1;
        } else {
            retVal = 1;
        }
        return retVal;
    }
}