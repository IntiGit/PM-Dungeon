package quests;

import ecs.entities.Entity;
import ecs.entities.monsters.Monster;

import java.util.Set;
import java.util.stream.Collectors;

public class KillAllMonstersQuest extends Quest {

    private Set<Monster> monsters;
    private int amount;

    public KillAllMonstersQuest() {
        description = "Besiege alle Monster eines Levels";
        rewardXP = 150;
        progress = 0;
    }

    @Override
    public void advanceProgress() {
        progress = 100 * (float)(amount - monsters.size()) / amount;
    }

    public void setMonsterSet(Set<Entity> entities) {
        monsters =
            entities.stream()
                .filter((m) -> m instanceof Monster)
                .map((m) -> (Monster) m)
                .collect(Collectors.toSet());
        amount = monsters.size();
    }
}
