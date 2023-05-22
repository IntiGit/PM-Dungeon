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
import level.elements.tile.Tile;
import starter.Game;
import tools.Point;

import java.util.*;

public class GummyBearProjectile extends Entity implements ICollide {
    private int dmg;
    private String pathToTexture;
    private float range;
    private float speed;
    private Point start;
    private HashSet<Monster> visitedMonsters;

    public GummyBearProjectile() {

    }

    public GummyBearProjectile(int dmg, String pathToTexture, float range, float speed, Point start, HashSet<Monster> visited) {
        this.dmg = dmg;
        this.pathToTexture = pathToTexture;
        this.range = range;
        this.speed = speed;
        this.start = start;
        this.visitedMonsters = visited;
    }

    public void setupPositionComponent(Point p){
        new PositionComponent(this, p);
    }

    public void setupVelocityAndProjectileComponent(PositionComponent epc, Point aimedOn) {
        Animation move = AnimationBuilder.buildAnimation(pathToTexture);
        PositionComponent pc = (PositionComponent) this.getComponent(PositionComponent.class).orElseThrow();

        Point aimedOnPoint = aimedOn;
        Point targetPoint =
            SkillTools.calculateLastPositionInRange(
                epc.getPosition(), aimedOnPoint, range);

        Point velocity =
            SkillTools.calculateVelocity(epc.getPosition(), targetPoint, speed);
        new VelocityComponent(this, velocity.x, velocity.y, move, move);

        setupProjectileComponent(epc, targetPoint);
    }

    public void setupAnimationComponent() {
        Animation move = AnimationBuilder.buildAnimation(pathToTexture);
        new AnimationComponent(this, move);
    }

    public void setupHitboxComponent() {
        new HitboxComponent(
            this,
            new Point(0.1f, 0.1f),
            new Point(1,1),
            this,
            (you, other, direction) -> {});
    }

    public void setupProjectileComponent(PositionComponent epc, Point targetPoint) {
        new ProjectileComponent(this,epc.getPosition(),targetPoint);
    }

    private void createNextGummybear(Entity oldProjectile, Entity monster) {
        Entity projectile;
        PositionComponent pcProjectile =
            (PositionComponent) oldProjectile.getComponent(PositionComponent.class).orElseThrow();
        Point start = pcProjectile.getPosition();
        Random rng = new Random();
        projectile = new GummyBearProjectile(
            1,
            RangeCombatSkill.gummyBearTextures.get(rng.nextInt(RangeCombatSkill.gummyBearTextures.size())),
            5f,
            0.3f,
            start,
            visitedMonsters);

        Monster m = getNextMonster(oldProjectile);
        Point monsterPosition;
        if(m != null) {
            PositionComponent pcMonster =
                (PositionComponent) m.getComponent(PositionComponent.class).orElseThrow();
            monsterPosition = pcMonster.getPosition();
        } else {
            monsterPosition = pcProjectile.getPosition();
        }

        ((GummyBearProjectile) projectile).setupPositionComponent(pcProjectile.getPosition());
        ((GummyBearProjectile) projectile).setupVelocityAndProjectileComponent(pcProjectile, monsterPosition);
        ((GummyBearProjectile) projectile).setupAnimationComponent();
        ((GummyBearProjectile) projectile).setupHitboxComponent();

        Game.addEntity(projectile);
    }

    private Monster getNextMonster(Entity projectile) {
        List<Entity> monster = new ArrayList<>(
            Game.getEntities()
                .stream()
                .filter((e) -> e instanceof Monster m && !visitedMonsters.contains(m))
                .toList());
        monster.sort(Comparator.comparingInt(a -> AITools.calculatePath(a, projectile).getCount()));
        return (Monster) monster.get(0);
    }

    @Override
    public void onCollision(Entity a, Entity b, Tile.Direction from) {
        if (!b.equals(Game.getHero().get()) && b instanceof Monster m && !visitedMonsters.contains(m)) {
            b.getComponent(HealthComponent.class)
                .ifPresent(
                    hc -> {
                        visitedMonsters.add(m);
                        createNextGummybear(a,b);

                        ((HealthComponent) hc).receiveHit(new Damage(dmg, DamageType.NEUTRAL, null));
                        Game.removeEntity(a);
                    });
        }
    }

}
