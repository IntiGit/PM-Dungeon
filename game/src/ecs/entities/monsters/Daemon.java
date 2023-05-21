package ecs.entities.monsters;

import dslToGame.AnimationBuilder;
import ecs.components.*;
import ecs.components.ai.AIComponent;
import ecs.components.ai.idle.RadiusWalk;
import graphic.Animation;

import java.util.List;

/** Klasse die das Monster Daemon darstellt */
public class Daemon extends Monster {

    /** Konstruktor für die Klasse Daemon */
    public Daemon() {
        speed = 0.2f;

        pathToIdleLeft = "chort/idleLeft";
        pathToIdleRight = "chort/idleRight";
        pathToRunLeft = "chort/runLeft";
        pathToRunRight = "chort/runRight";

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
        new AIComponent(this, (entity) -> {}, new RadiusWalk(6, 5), (entity) -> false);
    }

    @Override
    void setupHealthComponent() {
        new HealthComponent(
            this,
            10,
            (e) -> {},
            new Animation(List.of("chort_idle_anim_f0.png"), 300),
            new Animation(List.of("chort_idle_anim_f0.png"), 300));
    }
}
