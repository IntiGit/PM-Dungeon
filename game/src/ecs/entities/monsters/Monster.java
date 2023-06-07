package ecs.entities.monsters;

import ecs.entities.Entity;

/** Abstrakte Klasse die alle allgemeinen Attribute und Methoden eines Monsters kapselt */
public abstract class Monster extends Entity {

    /** Geschwindigkeit des Monsters */
    protected float speed;

    /** Pfade zu den Animationen der Monster */
    protected String pathToIdleLeft;

    protected String pathToIdleRight;
    protected String pathToRunLeft;
    protected String pathToRunRight;

    /** Erstellt die AnimationComponent für das Monster */
    abstract void setupAnimationComponent();

    /** Erstellt die VelocityComponent für das Monster */
    abstract void setupVelocityComponent();

    /** Erstellt die PositionComponent für das Monster */
    abstract void setupPositionComponent();

    /** Erstellt die HitboxComponent für das Monster */
    abstract void setupHitboxComponent();

    /** Erstellt die AIComponent für das Monster */
    abstract void setupAIComponent();

    abstract void setupHealthComponent();
}
