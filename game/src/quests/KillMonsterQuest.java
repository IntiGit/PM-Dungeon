package quests;

import ecs.entities.Entity;
import ecs.entities.monsters.Monster;
import starter.Game;

/**
 * Klasse für eine Quest in der eine bestimmte Anzahl eines bestimmten Monsters besiegt werden muss
 */
public class KillMonsterQuest extends Quest {

    private int killCount;
    private int amount;
    private Monster monster;

    /**
     * Konstruktor für die Klasse KillMonsterQuest
     *
     * @param m Monsterart die besiegt werden muss
     * @param amount Anzahl an Monstern die besiegt werden muss
     */
    public KillMonsterQuest(Monster m, int amount) {
        this.amount = amount;
        this.monster = m;
        description =
                "Besiege " + amount + " " + this.monster.getClass().getSimpleName() + " Monster";
        rewardXP = 100;
        progress = 0;
    }

    @Override
    public void advanceProgress() {
        progress = 100f * killCount / this.amount;
        Game.questanzeige.showActiveQuests();
    }

    /**
     * Erhöht den killCount killCOunt zeigt wie viele Monster bereits besiegt wurden
     *
     * @param e Monster, welches gerade besiegt wurde
     */
    public void addToKillcount(Entity e) {
        if (e.getClass().equals(monster.getClass())) {
            killCount++;
        }
    }
}
