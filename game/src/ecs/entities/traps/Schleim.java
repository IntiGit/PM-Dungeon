package ecs.entities.traps;

import com.badlogic.gdx.utils.Timer;
import dslToGame.AnimationBuilder;
import ecs.components.*;
import ecs.components.skill.Skill;
import ecs.components.skill.SkillComponent;
import ecs.damage.Damage;
import ecs.damage.DamageType;
import ecs.entities.Entity;
import ecs.entities.Hero;
import ecs.entities.monsters.Monster;
import graphic.Animation;
import level.elements.tile.Tile;
import starter.Game;
import tools.Point;

import java.util.logging.Logger;

/** Klasse die die Falle Schleim darstellt */
public class Schleim extends Falle {

    private final Logger slimeLogger = Logger.getLogger(this.getClass().getName());
    /**
     * Konstruktor für die Klasse Schleim
     *
     * @param pDmg Schaden den die Falle zufügen soll
     */
    public Schleim(int pDmg) {
        trapDmg = pDmg;
        active = true;

        pathToAnimationActive = "traps/slime";
        pathToAnimationInactive = "traps/slime";

        setupPositionComponent();
        setupAnimationComponent();
        setupHitboxComponent();
    }

    /** Erstellt die PositionComponent für das Monster */
    @Override
    void setupPositionComponent() {
        new PositionComponent(this);
    }

    /** Erstellt dieAnimationComponent für das Monster */
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
     *     <p>Der Held wird verlangsamt. Nach 3 Sekunden wird seine normale Geschwindigkeit wieder
     *     hergestellt. Der Cooldown aller Skills des Helden wird aktiviert. Der Held verliert
     *     Lebenspunkte.
     */
    @Override
    public void onCollision(Entity a, Entity b, Tile.Direction from) {
        if (b instanceof Hero h) {
            if(h.getShoes() == null) {
                VelocityComponent vc =
                    (VelocityComponent) b.getComponent(VelocityComponent.class).orElseThrow();
                float speed = vc.getXVelocity();
                vc.setXVelocity(0.05f);
                vc.setYVelocity(0.05f);

                Timer.schedule(
                    new Timer.Task() {
                        @Override
                        public void run() {
                            vc.setXVelocity(speed);
                            vc.setYVelocity(speed);
                        }
                    },
                    3f);
                SkillComponent sc = (SkillComponent) b.getComponent(SkillComponent.class).orElseThrow();
                for (Skill s : sc.getSkillSet()) {
                    s.activateCoolDown();
                }
                HealthComponent hc =
                    (HealthComponent) b.getComponent(HealthComponent.class).orElseThrow();
                hc.receiveHit(new Damage(trapDmg, DamageType.NEUTRAL, a));
                slimeLogger.info(
                    "Lebenspunkte betragen nun " + (hc.getCurrentHealthpoints() - trapDmg));
                Game.removeEntity(a);
            } else {
                if(h.getShoes().getItemName().equals("Noch dickere Treter")) {
                    Gift g = new Gift(3);
                    PositionComponent pcG = (PositionComponent) g.getComponent(PositionComponent.class).orElseThrow();
                    PositionComponent pcA = (PositionComponent) a.getComponent(PositionComponent.class).orElseThrow();
                    pcG.setPosition(pcA.getPosition());
                }
                Game.removeEntity(a);
            }
        }
    }
}
