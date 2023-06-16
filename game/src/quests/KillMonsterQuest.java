package quests;

import ecs.entities.Entity;
import ecs.entities.monsters.Monster;

public class KillMonsterQuest extends Quest {

    private int killCount;
    private int amount;
    private Monster monster;

    public KillMonsterQuest(Monster m, int amount){
        this.amount = amount;
        this.monster = m;
        description = "Besiege " + amount + " " + this.monster.getClass().getSimpleName() + " Monster";
        rewardXP = 100;
        progress = 0;
    }

    @Override
    public void advanceProgress() {
        progress = 100f * killCount / this.amount;
    }

    public void addToKillcount(Entity e) {
        if(e.getClass().equals(monster.getClass())) {
            killCount++;
        }
    }
}
