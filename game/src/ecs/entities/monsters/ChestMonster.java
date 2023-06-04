package ecs.entities.monsters;

import dslToGame.AnimationBuilder;
import ecs.components.*;
import ecs.components.ai.AIComponent;
import ecs.components.ai.fight.CollideAI;
import ecs.components.ai.transition.SelfDefendTransition;
import ecs.entities.Chest;
import ecs.entities.Entity;
import ecs.items.ItemData;
import graphic.Animation;
import starter.Game;
import tools.Point;

import java.util.List;

public class ChestMonster extends Monster implements IInteraction {

    private final ItemData loot;

    public ChestMonster(ItemData pLoot) {
        loot = pLoot;
        speed = 0.15f;

        pathToIdleLeft = "knight/idleLeft";
        pathToIdleRight = "knight/idleRight";
        pathToRunLeft = "knight/runLeft";
        pathToRunRight = "knight/runRight";

        setupAnimationComponent();
        setupVelocityComponent();
        setupPositionComponent();
        setupHitboxComponent();
        setupAIComponent();
        setupHealthComponent();
        setupInteractionComponent();
    }

    @Override
    void setupAnimationComponent() {
        Animation idleRight = AnimationBuilder.buildAnimation(pathToIdleRight);
        Animation idleLeft = AnimationBuilder.buildAnimation(pathToIdleLeft);
        new AnimationComponent(this, idleLeft, idleRight);
    }

    @Override
    void setupVelocityComponent() {
        Animation moveRight = AnimationBuilder.buildAnimation(pathToRunRight);
        Animation moveLeft = AnimationBuilder.buildAnimation(pathToRunLeft);
        new VelocityComponent(this, speed, speed, moveLeft, moveRight);
    }

    @Override
    void setupPositionComponent() {
        new PositionComponent(this);
    }

    @Override
    void setupHitboxComponent() {
        new HitboxComponent(this, (you, other, direction) -> {}, (you, other, direction) -> {});
    }

    @Override
    void setupAIComponent() {
        new AIComponent(this, new CollideAI(3f), (entity)-> {}, new SelfDefendTransition());
    }

    @Override
    void setupHealthComponent() {
        new HealthComponent(
            this,
            12,
            (e) -> {
                PositionComponent pc = (PositionComponent) e.getComponent(PositionComponent.class).orElseThrow();
                Game.addEntity(new Chest(List.of(loot), pc.getPosition()));
            },
            new Animation(List.of("knight_m_hit_anim_f0.png"), 300),
            new Animation(List.of("knight_m_hit_anim_f0.png"), 300));
    }

    private void setupInteractionComponent() {
        new InteractionComponent(this, Chest.defaultInteractionRadius,false, this);
    }

    @Override
    public void onInteraction(Entity entity) {
        AIComponent aiC = (AIComponent) this.getComponent(AIComponent.class).orElseThrow();
        aiC.setTransitionAI(e -> true);
    }
}
