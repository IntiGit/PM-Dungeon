package quests;

import starter.Game;

public class CollectItemsQuest extends Quest {

    private int curAmount;
    private int amount;

    public CollectItemsQuest(int amount) {
        this.amount = amount;
        description = "Sammle " + amount + " Items auf";
        rewardXP = 60;
        progress = 0;
    }

    @Override
    public void advanceProgress() {
        progress = 100f * curAmount / this.amount;
        Game.questanzeige.showActiveQuests();
    }

    public void collectItem() {
        this.curAmount++;
        Game.questanzeige.showActiveQuests();
    }
}
