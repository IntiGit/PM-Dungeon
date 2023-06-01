package ecs.entities.monsters;

import dslToGame.AnimationBuilder;
import ecs.components.*;
import ecs.components.ai.AIComponent;
import ecs.components.ai.fight.CollideAI;
import ecs.components.ai.idle.PatrouilleWalk;
import ecs.components.ai.transition.RangeTransition;
import graphic.Animation;
import java.util.List;

/** Klasse die das Monster Skelett darstellt */
public class Skelett extends Monster {

    /** Konstruktor für die Klasse Skelett */
    public Skelett() {
        speed = 0.1f;

        pathToIdleLeft = "skeleton/idleLeft";
        pathToIdleRight = "skeleton/idleRight";
        pathToRunLeft = "skeleton/runLeft";
        pathToRunRight = "skeleton/runRight";

        setupAnimationComponent();
        setupVelocityComponent();
        setupPositionComponent();
        setupHitboxComponent();
        setupAIComponent();
        setupHealthComponent();
    }

    /** Erstellt die AnimationComponent für das Monster */
    @Override
    void setupAnimationComponent() {
        Animation idleRight = AnimationBuilder.buildAnimation(pathToIdleRight);
        Animation idleLeft = AnimationBuilder.buildAnimation(pathToIdleLeft);
        new AnimationComponent(this, idleLeft, idleRight);
    }

    /** Erstellt die VelocityComponent für das Monster */
    @Override
    void setupVelocityComponent() {
        Animation moveRight = AnimationBuilder.buildAnimation(pathToRunRight);
        Animation moveLeft = AnimationBuilder.buildAnimation(pathToRunLeft);
        new VelocityComponent(this, speed, speed, moveLeft, moveRight);
    }

    /** Erstellt die PositionComponent für das Monster */
    @Override
    void setupPositionComponent() {
        new PositionComponent(this);
    }

    /** Erstellt die HitboxComponent für das Monster */
    @Override
    void setupHitboxComponent() {
        new HitboxComponent(this, (you, other, direction) -> {}, (you, other, direction) -> {});
    }

    /** Erstellt die AIComponent für das Monster */
    @Override
    void setupAIComponent() {
        new AIComponent(
                this,
                new CollideAI(1),
                new PatrouilleWalk(4, 2, 5, PatrouilleWalk.MODE.BACK_AND_FORTH),
                new RangeTransition(3));
    }

    @Override
    void setupHealthComponent() {
        new HealthComponent(
                this,
                5,
                (e) -> {},
                new Animation(List.of("skelet_idle_anim_f0.png"), 300),
                new Animation(List.of("skelet_idle_anim_f0.png"), 300));
    }
}
