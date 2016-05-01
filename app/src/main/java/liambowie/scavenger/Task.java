package liambowie.scavenger;

/**
 * Created by Liam on 01/05/2016.
 */
public class Task {

    private String name;
    private String desc;
    private int score;

    public Task() {}

    public Task(String name, String desc, int score) {
        this.name = name;
        this.desc = desc;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
