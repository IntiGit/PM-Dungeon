package quests;

import ecs.components.InventoryComponent;
import starter.Game;

public class FillInventoryQuest extends Quest {

    private InventoryComponent ic;

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

    public void setInventoryComponent(InventoryComponent pIC) {
        ic = pIC;
    }
}
