package ecs.components.skill;

import ecs.components.PositionComponent;
import ecs.entities.Entity;
import starter.Game;
import tools.Point;

public class RangeCombatSkill implements ISkillFunction{
    private Entity projectile;
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
                "animation/missingTexture.png",
                5f,
                0.3f,
                start);
            ((BumerangProjectile) projectile).setupPositionComponent(pc.getPosition());
            ((BumerangProjectile) projectile).setupVelocityAndProjectileComponent(pc, SkillTools.getCursorPositionAsPoint());
            ((BumerangProjectile) projectile).setupAnimationComponent();
            ((BumerangProjectile) projectile).setupHitboxComponent();
        }
        Game.addEntity(projectile);
    }
}
