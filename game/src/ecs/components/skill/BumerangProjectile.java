package ecs.components.skill;

import dslToGame.AnimationBuilder;
import ecs.components.*;
import ecs.components.collision.ICollide;
import ecs.damage.Damage;
import ecs.damage.DamageType;
import ecs.entities.Entity;
import ecs.entities.monsters.Monster;
import graphic.Animation;
import level.elements.tile.Tile;
import starter.Game;
import tools.Point;

public class BumerangProjectile extends Entity implements ICollide {
    private int dmg;
    private String pathToTexture;
    private float range;
    private float speed;
    private Point start;

    public BumerangProjectile() {

    }

    public BumerangProjectile(int dmg, String pathToTexture, float range, float speed, Point start) {
        this.dmg = dmg;
        this.pathToTexture = pathToTexture;
        this.range = range;
        this.speed = speed;
        this.start = start;
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


    @Override
    public void onCollision(Entity a, Entity b, Tile.Direction from) {
        if (!b.equals(Game.getHero().get())) {
                b.getComponent(HealthComponent.class)
                    .ifPresent(
                        hc -> {
                            ((HealthComponent) hc).receiveHit(new Damage(dmg, DamageType.NEUTRAL, null));
                        });
        }
    }
}
