package quests;

import ecs.components.InventoryComponent;
import starter.Game;

/**
 * Klasse für eine Quest, in der man das Inventar füllen muss
 */
public class FillInventoryQuest extends Quest {

    private InventoryComponent ic;

    /**
     * Konstruktor für die Klasse FillInventoryQuest
     */
    public FillInventoryQuest() {
        description = "Fuelle alle Slots deines Inventars";
        rewardXP = 120;
        progress = 0;
    }

    @Override
    public void advanceProgress() {
        if(ic != null) {
            progress = 100f * ic.filledSlots() / ic.getMaxSize();
            Game.questanzeige.showActiveQuests();
        }
    }

    /**
     * Setzt die InventoryComponent
     * Verweist auf das Inventar welches gefüllt werden soll
     * @param pIC InventoryComponent dessen Inventar gefüllt werden soll
     */
    public void setInventoryComponent(InventoryComponent pIC) {
        ic = pIC;
    }
}
