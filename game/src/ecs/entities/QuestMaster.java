package ecs.entities;

import dslToGame.AnimationBuilder;
import ecs.components.*;
import ecs.systems.ECS_System;
import graphic.Animation;
import quests.Quest;
import starter.Game;

/**
 * Klasse die einen NPC realisiert, der Quests vergibt
 */
public class QuestMaster extends Entity implements IInteraction {

    private final String pathToTexture = "character/questMaster/questmaster.png";
    private Quest quest;

    /**
     * Konstruktpr für die Klasse QuestMaster
     * @param questToGive Quest die dem Spieler vorgeschlagen werden soll
     */
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
        Game.questanzeige.QuestAcceptDeny(quest);
        Game.questScreenOpen = true;
        removeComponent(InteractionComponent.class);
        Game.systems.forEach(ECS_System::toggleRun);
    }
}
