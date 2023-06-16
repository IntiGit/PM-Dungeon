package quests;

public abstract class Quest {
    protected String description;
    protected int rewardXP;
    protected float progress;

    public abstract void advanceProgress();

    public int getRewardXP() {
        return rewardXP;
    }

    public String getDescription() {
        return description;
    }

    protected boolean isCompleted(){
        return Math.round(progress) == 100;
    }
}
