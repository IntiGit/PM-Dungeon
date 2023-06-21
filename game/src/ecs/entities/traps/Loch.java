package ecs.entities.traps;

import dslToGame.AnimationBuilder;
import ecs.components.*;
import ecs.components.skill.SkillComponent;
import ecs.components.xp.XPComponent;
import ecs.entities.Entity;
import ecs.entities.Hero;
import graphic.Animation;
import java.util.logging.Logger;
import level.elements.tile.Tile;
import starter.Game;

/** Klasse die die Falle Loch darstellt */
public class Loch extends Falle {

    private Logger lochLogger = Logger.getLogger(this.getClass().getName());
    /**
     * Konstruktor für die Klasse Loch
     *
     * @param pDmg Schaden den die Falle zufügen soll
     */
    public Loch(int pDmg) {
        trapDmg = pDmg;
        active = true;

        pathToAnimationActive = "traps/hole";
        pathToAnimationInactive = "traps/hole";

        setupPositionComponent();
        setupAnimationComponent();
        setupHitboxComponent();
    }

    /** Erstellt die PositionComponent für das Monster */
    @Override
    void setupPositionComponent() {
        new PositionComponent(this);
    }

    /** Erstellt die AnimationComponent für das Monster */
    @Override
    void setupAnimationComponent() {
        Animation active = AnimationBuilder.buildAnimation(pathToAnimationActive);
        Animation inactive = AnimationBuilder.buildAnimation(pathToAnimationInactive);
        new AnimationComponent(this, active, inactive);
    }

    /** Erstellt die HitboxComponent für das Monster */
    @Override
    void setupHitboxComponent() {
        new HitboxComponent(this, this, (a, b, from) -> {});
    }

    /**
     * Methode die implementiert was bei Kollision passiert
     *
     * @param a is the current Entity
     * @param b is the Entity with whom the Collision happened
     * @param from the direction from a to b
     *     <p>Die XP des Helden werden auf 0 gesetzt und sein Level um 2 verringert. Sollte er dabei
     *     unter ein bestimmtes Level fallen, wird der entsprechende Skill aus dem Skillset des
     *     Helden entfernt. Es werden auch seine maximalen Leben wieder heruntergesetzt.
     *     Abschließend wird die Falle entfernt
     */
    @Override
    public void onCollision(Entity a, Entity b, Tile.Direction from) {
        if (b instanceof Hero) {
            XPComponent xpc = (XPComponent) b.getComponent(XPComponent.class).orElseThrow();
            long lvl = xpc.getCurrentLevel();
            xpc.setCurrentXP(0);
            xpc.setCurrentLevel(Math.max(0, xpc.getCurrentLevel() - 2));

            if (xpc.getCurrentLevel() < 2) {
                SkillComponent sc =
                        (SkillComponent) b.getComponent(SkillComponent.class).orElseThrow();
                sc.getSkillSet().removeIf(s -> s.getSkillID() == 5 || s.getSkillID() == 6);
                ((Hero) b).notifyObservers();
            }
            if (xpc.getCurrentLevel() < 5) {
                SkillComponent sc =
                        (SkillComponent) b.getComponent(SkillComponent.class).orElseThrow();
                sc.getSkillSet().removeIf(s -> s.getSkillID() == 2);
                ((Hero) b).notifyObservers();
            } else if (xpc.getCurrentLevel() < 10) {
                SkillComponent sc =
                        (SkillComponent) b.getComponent(SkillComponent.class).orElseThrow();
                sc.getSkillSet().removeIf(s -> s.getSkillID() == 3);
                ((Hero) b).notifyObservers();
            }
            lochLogger.info("Abgestiegen zu Level " + xpc.getCurrentLevel());

            HealthComponent hc =
                    (HealthComponent) b.getComponent(HealthComponent.class).orElseThrow();
            hc.setMaximalHealthpoints(
                    hc.getMaximalHealthpoints() - (int) (lvl - xpc.getCurrentLevel()));
            lochLogger.info("maximale Lebenspunkte verringert zu " + hc.getMaximalHealthpoints());

            Game.removeEntity(a);
        }
    }
}
