package ecs.entities;

import dslToGame.AnimationBuilder;
import ecs.components.*;
import graphic.Animation;
import quests.Quest;
import starter.Game;

public class QuestMaster extends Entity implements IInteraction {

    private final String pathToTexture = "";
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
        if(entity instanceof Hero h) {
            int s = h.getMyQuests().size();

            Game.questanzeige.QuestAcceptDeny(quest);
            Game.questScreenOpen = true;
            if(s != h.getMyQuests().size()) {
                removeComponent(InteractionComponent.class);
            }
        }
    }
}
