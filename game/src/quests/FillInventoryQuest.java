package quests;

import ecs.components.InventoryComponent;

public class FillInventoryQuest extends Quest {

    private InventoryComponent ic;

    public FillInventoryQuest() {
        description = "Fülle alle Slots deines Inventars";
        rewardXP = 120;
        progress = 0;
    }

    @Override
    public void advanceProgress() {
        progress = 100f * ic.filledSlots() / ic.getMaxSize();
    }

    public void setInventoryComponent(InventoryComponent pIC) {
        ic = pIC;
    }
}
