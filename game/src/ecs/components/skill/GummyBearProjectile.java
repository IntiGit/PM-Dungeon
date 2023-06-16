package ecs.components.skill;

import dslToGame.AnimationBuilder;
import ecs.components.*;
import ecs.components.ai.AITools;
import ecs.components.collision.ICollide;
import ecs.damage.Damage;
import ecs.damage.DamageType;
import ecs.entities.Entity;
import ecs.entities.monsters.Monster;
import graphic.Animation;
import java.util.*;
import level.elements.tile.Tile;
import starter.Game;
import tools.Point;

public class GummyBearProjectile extends Entity implements ICollide {
    private int dmg;
    private String pathToTexture;
    private float range;
    private float speed;
    private Point start;
    private HashSet<Monster> visitedMonsters;

    /** Leerer Konstruktor */
    public GummyBearProjectile() {}

    /**
     * Konstruktor fuer die Klasse GummyBearProjectile
     *
     * @param dmg Schaden des Projektils
     * @param pathToTexture Pfad zur Textur des Projektils
     * @param range Reichweite des Projektils
     * @param speed Geschwindikeit des Projektils
     * @param start Startpunkt des Projektils
     * @param visited Set an bereits getroffenen Monstern
     */
    public GummyBearProjectile(
            int dmg,
            String pathToTexture,
            float range,
            float speed,
            Point start,
            HashSet<Monster> visited) {
        this.dmg = dmg;
        this.pathToTexture = pathToTexture;
        this.range = range;
        this.speed = speed;
        this.start = start;
        this.visitedMonsters = visited;
    }

    /**
     * Erstellt eine neue PositionComponent
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

    /** Erstellt eine neue AnimationComponent */
    public void setupAnimationComponent() {
        Animation move = AnimationBuilder.buildAnimation(pathToTexture);
        new AnimationComponent(this, move);
    }

    /** Erstellt eine neue HitboxComponent */
    public void setupHitboxComponent() {
        new HitboxComponent(
                this, new Point(0.1f, 0.1f), new Point(1, 1), this, (you, other, direction) -> {});
    }

    /**
     * Erstellt eine neue ProjectileComponent
     *
     * @param epc PositionComponent
     * @param targetPoint Zielpunkt
     */
    public void setupProjectileComponent(PositionComponent epc, Point targetPoint) {
        new ProjectileComponent(this, epc.getPosition(), targetPoint);
    }

    private void createNextGummybear(Entity oldProjectile) {
        Entity projectile;
        PositionComponent pcProjectile =
                (PositionComponent)
                        oldProjectile.getComponent(PositionComponent.class).orElseThrow();
        Point start = pcProjectile.getPosition();
        Random rng = new Random();
        projectile =
                new GummyBearProjectile(
                        1,
                        RangeCombatSkill.gummyBearTextures.get(
                                rng.nextInt(RangeCombatSkill.gummyBearTextures.size())),
                        5f,
                        0.3f,
                        start,
                        visitedMonsters);

        Monster m = getNextMonster(oldProjectile);
        Point monsterPosition;
        if (m != null) {
            PositionComponent pcMonster =
                    (PositionComponent) m.getComponent(PositionComponent.class).orElseThrow();
            monsterPosition = pcMonster.getPosition();
        } else {
            monsterPosition = pcProjectile.getPosition();
        }

        ((GummyBearProjectile) projectile).setupPositionComponent(pcProjectile.getPosition());
        ((GummyBearProjectile) projectile)
                .setupVelocityAndProjectileComponent(pcProjectile, monsterPosition);
        ((GummyBearProjectile) projectile).setupAnimationComponent();
        ((GummyBearProjectile) projectile).setupHitboxComponent();

        Game.addEntity(projectile);
    }

    private Monster getNextMonster(Entity projectile) {
        List<Entity> monster =
                new ArrayList<>(
                        Game.getEntities().stream()
                                .filter(
                                        (e) ->
                                                e instanceof Monster m
                                                        && !visitedMonsters.contains(m))
                                .toList());
        monster.sort(Comparator.comparingInt(a -> AITools.calculatePath(a, projectile).getCount()));
        return !monster.isEmpty() ? (Monster) monster.get(0) : null;
    }

    /**
     * Implementiert was bei Kollision passieren soll
     *
     * @param a is the current Entity
     * @param b is the Entity with whom the Collision happened
     * @param from the direction from a to b
     */
    @Override
    public void onCollision(Entity a, Entity b, Tile.Direction from) {
        if (!b.equals(Game.getHero().get())
                && b instanceof Monster m
                && !visitedMonsters.contains(m)) {
            b.getComponent(HealthComponent.class)
                    .ifPresent(
                            hc -> {
                                visitedMonsters.add(m);
                                createNextGummybear(a);
                                applyKnockback(b, 2);
                                ((HealthComponent) hc)
                                        .receiveHit(new Damage(dmg, DamageType.NEUTRAL, null));
                                Game.removeEntity(a);
                            });
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
