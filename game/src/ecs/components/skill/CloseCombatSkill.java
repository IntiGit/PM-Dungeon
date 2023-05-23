package ecs.components.skill;

import dslToGame.AnimationBuilder;
import ecs.components.*;
import ecs.components.collision.ICollide;
import ecs.damage.Damage;
import ecs.damage.DamageType;
import ecs.entities.Entity;
import ecs.items.Waffe;
import graphic.Animation;
import starter.Game;
import tools.Point;

public class CloseCombatSkill implements ISkillFunction{

    private int dmg;
    private String pathToTexture;
    private float range;
    private float speed;
    public CloseCombatSkill(int dmg, Waffe weapon, String pathToTexture, float range, float speed) {
        this.dmg = weapon == null ? dmg : dmg + weapon.getDamage();
        this.pathToTexture = pathToTexture;
        this.range = range;
        this.speed = speed;
    }

    @Override
    public void execute(Entity entity) {
        AnimationComponent ac = (AnimationComponent) entity.getComponent(AnimationComponent.class).orElseThrow();
        String texture;
        int blickrichtung;
        String dir = "";
        if(ac.getIdleLeft().equals(ac.getCurrentAnimation())) {
            //Blickrichtung links
            texture = "animation/missingTexture.png";
            blickrichtung = -1;
            dir = "W";
        } else {
            //Blickrichtung rechts
            texture = "animation/missingTexture.png";
            blickrichtung = 1;
            dir = "E";
        }

        PositionComponent pc = (PositionComponent) entity.getComponent(PositionComponent.class).orElseThrow();
        ITargetSelection ts;
        if(blickrichtung < 0) {
            ts = () -> new Point(pc.getPosition().x - 1, pc.getPosition().y);
        } else if(blickrichtung > 0) {
            ts = () -> new Point(pc.getPosition().x + 1, pc.getPosition().y);
        } else {
            ts = pc::getPosition;
        }

        createProjectile(entity, texture, ts, this.range, this.speed, this.dmg, dir);

    }

    private void createProjectile(Entity entity,
                                  String pathToTexturesOfProjectile,
                                  ITargetSelection selectionFunction,
                                  float projectileRange,
                                  float projectileSpeed,
                                  int projectileDamage,
                                  String dir) {
        Entity projectile = new Entity();
        PositionComponent epc =
            (PositionComponent)
                entity.getComponent(PositionComponent.class)
                    .orElseThrow(
                        () -> new MissingComponentException("PositionComponent"));
        new PositionComponent(projectile, epc.getPosition());

        Animation animation = AnimationBuilder.buildAnimation(pathToTexturesOfProjectile);
        new AnimationComponent(projectile, animation);

        Point aimedOn = selectionFunction.selectTargetPoint();
        Point targetPoint =
            SkillTools.calculateLastPositionInRange(
                epc.getPosition(), aimedOn, projectileRange);
        Point velocity =
            SkillTools.calculateVelocity(epc.getPosition(), targetPoint, projectileSpeed);
        VelocityComponent vc =
            new VelocityComponent(projectile, velocity.x, velocity.y, animation, animation);
        new ProjectileComponent(projectile, epc.getPosition(), targetPoint);
        ICollide collide =
            (a, b, from) -> {
                if (b != entity) {
                    b.getComponent(HealthComponent.class)
                        .ifPresent(
                            hc -> {
                                applyKnockback(b, dir,2);
                                ((HealthComponent) hc).receiveHit(new Damage(projectileDamage, DamageType.NEUTRAL, null));
                                Game.removeEntity(projectile);
                            });
                }
            };

        new HitboxComponent(
            projectile, new Point(0.25f, 0.25f), new Point(1,1), collide, null);
    }

    private void applyKnockback(Entity e, String dir, int knockbackAmount) {
        VelocityComponent vc =
            (VelocityComponent) e.getComponent(VelocityComponent.class).orElseThrow();
        PositionComponent pc =
            (PositionComponent) e.getComponent(PositionComponent.class).orElseThrow();
        Point checkPosition;
        if(dir.equals("E")) {
            checkPosition = new Point(pc.getPosition().x + knockbackAmount,pc.getPosition().y);
            if(Game.currentLevel.getTileAt(checkPosition.toCoordinate()).isAccessible()) {
                vc.setCurrentXVelocity(knockbackAmount);
            }
        } else if(dir.equals("W")) {
            checkPosition = new Point(pc.getPosition().x - 2,pc.getPosition().y);
            if(Game.currentLevel.getTileAt(checkPosition.toCoordinate()).isAccessible()) {
                vc.setCurrentXVelocity(-knockbackAmount);
            }
        }
    }
}
