package ecs.systems;

import ecs.entities.Hero;
import quests.Quest;
import starter.Game;

public class QuestSystem extends ECS_System{

    @Override
    public void update() {
        Hero h = (Hero) Game.getHero().get();
        h.getMyQuests()
            .forEach(Quest::advanceProgress);
    }
}
