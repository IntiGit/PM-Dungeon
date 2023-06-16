package ecs.entities;

import dslToGame.AnimationBuilder;
import ecs.components.*;
import ecs.systems.ECS_System;
import graphic.Animation;
import quests.Quest;
import starter.Game;

public class QuestMaster extends Entity implements IInteraction {

    private final String pathToTexture = "animation/missingTexture.png";
    private Quest quest;

    public QuestMaster(Quest questToGive) {
        quest = questToGive;

        setupPositionComponent();
        setupAnimationComponent();
        setupHitboxComponent();
        setupInteractionComponent();
    }

    private void setupPositionComponent() {
        new PositionComponent(this);
    }

    private void setupAnimationComponent() {
        Animation idle = AnimationBuilder.buildAnimation(pathToTexture);
        new AnimationComponent(this, idle, idle);
    }

    private void setupHitboxComponent() {
        new HitboxComponent(this);
    }

    private void setupInteractionComponent() {
        new InteractionComponent(this,1,true,this);
    }

    @Override
    public void onInteraction(Entity entity) {
        System.out.println(entity.getClass().getSimpleName());

        Hero h = (Hero) Game.getHero().get();
        int s = h.getMyQuests().size();

        Game.questanzeige.QuestAcceptDeny(quest);
        Game.questScreenOpen = true;
        removeComponent(InteractionComponent.class);
        Game.systems.forEach(ECS_System::toggleRun);
    }
}
