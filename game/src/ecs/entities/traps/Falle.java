package ecs.entities.traps;

import ecs.components.collision.ICollide;
import ecs.entities.Entity;

/** Abstrakte Klasse die alle allgemeinen Attribute und Methoden einer Falle kapselt */
public abstract class Falle extends Entity implements ICollide {

    /** Gibt an ob die Falle aktiv oder inaktiv ist */
    protected boolean active;

    /** Schaden die die Falle zufügt */
    protected int trapDmg;

    /** Pfade zu den Animationen der Falle */
    protected String pathToAnimationActive;

    protected String pathToAnimationInactive;

    /** Erstellt die PositionComponent für das Monster */
    abstract void setupPositionComponent();

    /** Erstellt die AnimationComponent für das Monster */
    abstract void setupAnimationComponent();

    /** Erstellt die HitboxComponent für das Monster */
    abstract void setupHitboxComponent();
}
