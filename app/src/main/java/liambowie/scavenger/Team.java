package liambowie.scavenger;

/**
 * Created by Liam on 30/04/2016.
 */
public class Team {

    private String teamName;
    private int score;

    public Team(String teamName, int score) {
        this.teamName = teamName;
        this.score = score;
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
}
