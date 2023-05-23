package ecs.components.skill;

import ecs.components.PositionComponent;
import ecs.entities.Entity;
import starter.Game;
import tools.Point;

import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class RangeCombatSkill implements ISkillFunction{
    private Entity projectile;

    public static final List<String> gummyBearTextures =
        List.of(
            "gummyBear/gummyBear_green.png",
            "gummyBear/gummyBear_orange.png",
            "gummyBear/gummyBear_red.png");

    public RangeCombatSkill(Entity projectile) {
       this.projectile = projectile;
    }

    @Override
    public void execute(Entity entity) {
        PositionComponent pc =
            (PositionComponent) entity.getComponent(PositionComponent.class).orElseThrow();
        Point start = pc.getPosition();

        if(projectile instanceof BumerangProjectile) {
            projectile = new BumerangProjectile(
                3,
                "character/knight/attack/bumerang",
                5f,
                0.3f,
                start,
                false);
            ((BumerangProjectile) projectile).setupPositionComponent(pc.getPosition());
            ((BumerangProjectile) projectile).setupVelocityAndProjectileComponent(pc, SkillTools.getCursorPositionAsPoint());
            ((BumerangProjectile) projectile).setupAnimationComponent();
            ((BumerangProjectile) projectile).setupHitboxComponent();
        }
        if(projectile instanceof GummyBearProjectile) {
            System.out.println("RangeCombatSkill gummybear");
            Random rng = new Random();

            projectile = new GummyBearProjectile(
                1,
                gummyBearTextures.get(rng.nextInt(gummyBearTextures.size())),
                5f,
                0.3f,
                start,
                new HashSet<>());
            ((GummyBearProjectile) projectile).setupPositionComponent(pc.getPosition());
            ((GummyBearProjectile) projectile).setupVelocityAndProjectileComponent(pc, SkillTools.getCursorPositionAsPoint());
            ((GummyBearProjectile) projectile).setupAnimationComponent();
            ((GummyBearProjectile) projectile).setupHitboxComponent();
        }
        Game.addEntity(projectile);
    }
}
