package quests;

import ecs.entities.Entity;
import ecs.entities.monsters.Monster;
import starter.Game;

import java.util.Set;
import java.util.stream.Collectors;

public class KillAllMonstersQuest extends Quest {

    private Set<Monster> monsters;
    private int amountToKill = 0;

    public KillAllMonstersQuest() {
        description = "Besiege alle Monster eines Levels";
        rewardXP = 150;
        progress = 0;
    }

    @Override
    public void advanceProgress() {
        setMonsterSet(Game.getEntities());
        if(monsters != null) {
            progress = 100 * (float) (amountToKill - monsters.size()) / amountToKill;
            Game.questanzeige.showActiveQuests();
        }
    }

    public void setMonsterSet(Set<Entity> entities) {
        monsters =
            entities.stream()
                .filter((m) -> m instanceof Monster)
                .map((m) -> (Monster) m)
                .collect(Collectors.toSet());
    }

    public void setAmountToKill() {
        amountToKill = Game.getEntities().stream()
            .filter((m) -> m instanceof Monster)
            .map((m) -> (Monster) m)
            .collect(Collectors.toSet())
            .size();
    }
}
