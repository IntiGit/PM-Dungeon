package quests;

public class CollectItemsQuest extends Quest {

    private int amount;

    public CollectItemsQuest(int amount) {
        this.amount = amount;
        description = "Sammle " + amount + " Items auf";
        rewardXP = 60;
        progress = 0;
    }

    @Override
    public void advanceProgress() {
        progress = 100f / this.amount;
    }

    public void collectItem() {
        this.amount++;
    }
}
