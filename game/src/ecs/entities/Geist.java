package ecs.entities;

import dslToGame.AnimationBuilder;
import ecs.components.AnimationComponent;
import ecs.components.PositionComponent;
import ecs.components.VelocityComponent;
import ecs.components.ai.AIComponent;
import ecs.components.ai.fight.FollowHeroAI;
import ecs.components.ai.idle.FleeFromHero;
import ecs.components.ai.transition.RangeTransition;
import graphic.Animation;

/** Klasse die den Geist darstellt */
public class Geist extends Entity {
    private float speed;
    private final String pathToIdleLeft = "ghost/idleLeft";
    private final String pathToIdleRight = "ghost/idleRight";
    private final String pathToRunLeft = "ghost/runLeft";
    private final String pathToRunRight = "ghost/runRight";
    private Grabstein grabstein;

    /** Konstruktor für die Klasse Geist */
    public Geist() {
        speed = 0.2f;

        setupVelocityComponent();
        setupPositionComponent();
        setupAIComponent();
        setupAnimationComponent();
    }

    private void setupVelocityComponent() {
        Animation moveRight = AnimationBuilder.buildAnimation(pathToRunRight);
        Animation moveLeft = AnimationBuilder.buildAnimation(pathToRunLeft);
        new VelocityComponent(this, speed, speed, moveLeft, moveRight);
    }

    private void setupPositionComponent() {
        new PositionComponent(this);
    }

    private void setupAIComponent() {
        new AIComponent(this, new FollowHeroAI(2), new FleeFromHero(2, 5), new RangeTransition(5));
    }

    private void setupAnimationComponent() {
        Animation idleRight = AnimationBuilder.buildAnimation(pathToIdleRight);
        Animation idleLeft = AnimationBuilder.buildAnimation(pathToIdleLeft);
        new AnimationComponent(this, idleLeft, idleRight);
    }

    /**
     * Setzt den Grabstein des Geistes fest
     *
     * @param gs neuer Grabstein des Geistes
     */
    public void setGrabstein(Grabstein gs) {
        grabstein = gs;
    }

    /**
     * Gibt den Grabstein des Geistes zurück
     *
     * @return Grabstein des Geistes
     */
    public Grabstein getGrabstein() {
        return grabstein;
    }
}
