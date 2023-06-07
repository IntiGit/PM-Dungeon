package ecs.entities.monsters;

import dslToGame.AnimationBuilder;
import ecs.components.*;
import ecs.components.ai.AIComponent;
import ecs.components.ai.fight.CollideAI;
import ecs.components.ai.transition.SelfDefendTransition;
import ecs.damage.Damage;
import ecs.damage.DamageType;
import ecs.entities.Chest;
import ecs.entities.Entity;
import ecs.entities.Hero;
import ecs.items.ItemData;
import graphic.Animation;
import java.util.List;
import starter.Game;

/** Klasse die ein Kistenmonster darstellt. */
public class ChestMonster extends Monster implements IInteraction {

    private final ItemData loot;

    /**
     * Konstruktor für das Kistenmonster
     *
     * @param pLoot Item, welches das Monster beim besiegen droppen soll
     */
    public ChestMonster(ItemData pLoot) {
        loot = pLoot;
        speed = 0.15f;

        pathToIdleLeft = "chestMonster/idleLeft";
        pathToIdleRight = "chestMonster/idleRight";
        pathToRunLeft = "chestMonster/runLeft";
        pathToRunRight = "chestMonster/runRight";

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
        new HitboxComponent(
                this,
                (you, other, direction) -> {
                    if (other instanceof Hero h) {
                        HealthComponent hc =
                                (HealthComponent)
                                        h.getComponent(HealthComponent.class).orElseThrow();
                        hc.receiveHit(new Damage(1, DamageType.NEUTRAL, you));
                    }
                },
                (you, other, direction) -> {});
    }

    @Override
    void setupAIComponent() {
        new AIComponent(this, new CollideAI(3f), (entity) -> {}, new SelfDefendTransition());
    }

    @Override
    void setupHealthComponent() {
        new HealthComponent(
                this,
                6,
                (e) -> {
                    PositionComponent pc =
                            (PositionComponent)
                                    e.getComponent(PositionComponent.class).orElseThrow();
                    Game.addEntity(new Chest(List.of(loot), pc.getPosition()));
                },
                new Animation(List.of("chestMonster/idleLeft/ChestMonster_anim_f0.png"), 300),
                new Animation(List.of("chestMonster/idleLeft/ChestMonster_anim_f0.png"), 300));
    }

    private void setupInteractionComponent() {
        new InteractionComponent(this, Chest.defaultInteractionRadius, false, this);
    }

    @Override
    public void onInteraction(Entity entity) {
        AIComponent aiC = (AIComponent) this.getComponent(AIComponent.class).orElseThrow();
        aiC.setTransitionAI(e -> true);
    }
}
