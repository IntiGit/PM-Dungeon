package ecs.components.skill;

import dslToGame.AnimationBuilder;
import ecs.components.AnimationComponent;
import ecs.components.HealthComponent;
import ecs.components.HitboxComponent;
import ecs.components.PositionComponent;
import ecs.components.VelocityComponent;
import ecs.components.collision.ICollide;
import ecs.damage.Damage;
import ecs.damage.DamageType;
import ecs.entities.Entity;
import graphic.Animation;
import level.elements.tile.Tile;
import starter.Game;
import tools.Point;

/** Klasse die ein Bumerang Projektil fuer den Fernkampf modeliert */
public class BumerangProjectile extends Entity implements ICollide {
    private int dmg;
    private String pathToTexture;
    private float range;
    private float speed;
    private Point start;
    private boolean returning;

    /** Leerer Konstruktor */
    public BumerangProjectile() {}

    /**
     * Konstuktor fuer das Bumerang Projektil
     *
     * @param dmg Schaden des Projektils
     * @param pathToTexture Pfad zur Textur des Projektils
     * @param range Reichweite des Projektils
     * @param speed Geschwindigkeit des Projektils
     * @param start Startpunkt des Projektils
     * @param returning bestimmt ob das Projektil gerade zurueck fliegt
     */
    public BumerangProjectile(
            int dmg,
            String pathToTexture,
            float range,
            float speed,
            Point start,
            boolean returning) {
        this.dmg = dmg;
        this.pathToTexture = pathToTexture;
        this.range = range;
        this.speed = speed;
        this.start = start;
        this.returning = returning;
    }

    /**
     * Erzeugt eine neue PositionComponent
     *
     * @param p Punkt an dem das Projektil sein soll
     */
    public void setupPositionComponent(Point p) {
        new PositionComponent(this, p);
    }

    /**
     * Erzeugt eine neue Velocity- und ProjectileComponent
     *
     * @param epc PositionComponent
     * @param aimedOn Vorlaeufiger Zielpunkt des Projektils
     */
    public void setupVelocityAndProjectileComponent(PositionComponent epc, Point aimedOn) {
        Animation move = AnimationBuilder.buildAnimation(pathToTexture);
        PositionComponent pc =
                (PositionComponent) this.getComponent(PositionComponent.class).orElseThrow();

        Point aimedOnPoint = aimedOn;
        Point targetPoint =
                SkillTools.calculateLastPositionInRange(epc.getPosition(), aimedOnPoint, range);

        Point velocity = SkillTools.calculateVelocity(epc.getPosition(), targetPoint, speed);
        new VelocityComponent(this, velocity.x, velocity.y, move, move);

        setupProjectileComponent(epc, targetPoint);
    }

    /** Erzeugt eine neue AnimationComponent */
    public void setupAnimationComponent() {
        Animation move = AnimationBuilder.buildAnimation(pathToTexture);
        new AnimationComponent(this, move);
    }

    /** Erzeugt eine neue HitboxComponent */
    public void setupHitboxComponent() {
        new HitboxComponent(
                this, new Point(0.1f, 0.1f), new Point(1, 1), this, (you, other, direction) -> {});
    }

    /** Erzeugt eine neue ProjectileComponent */
    public void setupProjectileComponent(PositionComponent epc, Point targetPoint) {
        new ProjectileComponent(this, epc.getPosition(), targetPoint);
    }

    /**
     * Implementiert Verhalten bei Kollision
     *
     * @param a is the current Entity
     * @param b is the Entity with whom the Collision happened
     * @param from the direction from a to b
     */
    @Override
    public void onCollision(Entity a, Entity b, Tile.Direction from) {
        if (!b.equals(Game.getHero().get())) {
            b.getComponent(HealthComponent.class)
                    .ifPresent(
                            hc -> {
                                applyKnockback(b, 2);
                                ((HealthComponent) hc)
                                        .receiveHit(new Damage(dmg, DamageType.NEUTRAL, null));
                            });
        } else if (returning) {
            Game.removeEntity(a);
        }
    }

    private void applyKnockback(Entity e, int knockbackAmount) {
        VelocityComponent vc =
                (VelocityComponent) e.getComponent(VelocityComponent.class).orElseThrow();
        PositionComponent pcE =
                (PositionComponent) e.getComponent(PositionComponent.class).orElseThrow();
        PositionComponent pcH =
                (PositionComponent)
                        Game.getHero().get().getComponent(PositionComponent.class).orElseThrow();
        Point directionVector =
                Point.getUnitDirectionalVector(pcE.getPosition(), pcH.getPosition());
        Point checkPosition =
                new Point(
                        pcE.getPosition().x + directionVector.x * knockbackAmount,
                        pcE.getPosition().y + directionVector.y * knockbackAmount);
        if (Game.currentLevel.getTileAt(checkPosition.toCoordinate()).isAccessible()) {
            vc.setCurrentXVelocity(directionVector.x * knockbackAmount);
            vc.setCurrentYVelocity(directionVector.y * knockbackAmount);
        }
    }
}
