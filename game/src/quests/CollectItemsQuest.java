package quests;

import starter.Game;

/** Klasse für Quests die das Sammeln von Items beinhaltet */
public class CollectItemsQuest extends Quest {

    private int curAmount;
    private int amount;

    /**
     * Konstruktor für die Klasse CollectItemsQuest
     *
     * @param amount ANzahl an Items die man sammeln muss
     */
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

    /**
     * Wird aufgerufen wenn ein Item aufgesammelt wird Erhöht den Zähler um 1 und updated die
     * Questanzeige
     */
    public void collectItem() {
        this.curAmount++;
        Game.questanzeige.showActiveQuests();
    }
}
