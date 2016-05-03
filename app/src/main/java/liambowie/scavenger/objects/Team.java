package liambowie.scavenger.objects;

/**
 * Created by Liam on 30/04/2016.
 */
public class Team {

    private String teamName;
    private String key;
    private int score;

    public Team(){}

    public Team(String teamName) {
        this.teamName = teamName;
        this.score = 0;
    }

    public Team(String teamName, String key) {
        this.teamName = teamName;
        this.key = key;
        this.score = 0;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void add(int points){
        this.score += points;
    }

    public String getKey() { return key; }

    public void setKey(String key) { this.key = key; }
}
