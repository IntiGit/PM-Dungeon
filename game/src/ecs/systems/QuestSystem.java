package ecs.systems;

import ecs.entities.Hero;
import java.util.HashSet;
import java.util.Set;
import quests.Quest;
import starter.Game;

/**
 * Klasse für das Questsystem Ruft jeden Frame advanceProgress auf allen Quests des Helden auf Prüft
 * jeden Frame ob eine Quest des Helden abgeschlossen ist
 */
public class QuestSystem extends ECS_System {

    private Set<Quest> toRemove = new HashSet<>();

    @Override
    public void update() {
        Hero h = (Hero) Game.getHero().get();
        for (Quest q : h.getMyQuests()) {
            q.advanceProgress();
            if (q.isCompleted()) {
                h.receiveQuestReward(q);
                toRemove.add(q);
            }
        }
        for (Quest q : toRemove) {
            h.removeQuest(q);
        }
    }
}
