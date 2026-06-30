package game;

public class GameState {

    public int corruption = 0;
    public int gold = 0;
    public int day = 1;
    public boolean questAccepted = false;
    public boolean questCompleted = false;
    public boolean oldManQuestFinished = false;
    public boolean visitedForest = false;
    public boolean ringFound = false;
    public boolean deagolKilled = false;
    public String location = "Город";
    public enum Location {
        CITY,
        FOREST,
        CAVE,
        RIVER
    }
}
