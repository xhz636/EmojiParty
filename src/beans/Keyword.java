package beans;

public class Keyword {

    private String keyword;
    private String ID;
    private int weight;

    public Keyword() {

    }

    public Keyword(String keyword, String ID, int weight) {
        this.keyword = keyword;
        this.ID = ID;
        this.weight = weight;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getID() {
        return ID;
    }

    public int getWeight() {
        return weight;
    }
}
