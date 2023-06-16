package ecs.systems;

import ecs.entities.Hero;
import quests.Quest;
import starter.Game;

import java.util.HashSet;
import java.util.Set;

public class QuestSystem extends ECS_System{

    public static Set<Quest> toRemove = new HashSet<>();
    @Override
    public void update() {
        Hero h = (Hero) Game.getHero().get();
        for(Quest q : h.getMyQuests()) {
            q.advanceProgress();
            if(q.isCompleted()) {
                h.receiveQuestReward(q);
                toRemove.add(q);
            }
        }
        for(Quest q : toRemove) {
            h.removeQuest(q);
        }
    }
}
