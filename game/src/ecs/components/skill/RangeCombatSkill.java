package ecs.components.skill;

import ecs.components.PositionComponent;
import ecs.entities.Entity;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import starter.Game;
import tools.Point;

/**
 * Klasse fuer die Fernkampangriffe
 */
public class RangeCombatSkill implements ISkillFunction {
    private Entity projectile;

    /**
     * Liste der Texturen fuer Gummiebaerchen
     */
    public static final List<String> gummyBearTextures =
            List.of(
                    "gummyBear/gummyBear_green.png",
                    "gummyBear/gummyBear_orange.png",
                    "gummyBear/gummyBear_red.png");

    /**
     * Konstruktor fuer die Klasse RangeCombatSkill
     * @param projectile gewuenschtes Projektil
     */
    public RangeCombatSkill(Entity projectile) {
        this.projectile = projectile;
    }

    /**
     * Implementiert die Funktionsweise der Fernkampfangriffe
     * @param entity which uses the skill
     */
    @Override
    public void execute(Entity entity) {
        PositionComponent pc =
                (PositionComponent) entity.getComponent(PositionComponent.class).orElseThrow();
        Point start = pc.getPosition();

        if (projectile instanceof BumerangProjectile) {
            projectile =
                    new BumerangProjectile(
                            3, "character/knight/attack/bumerang", 5f, 0.3f, start, false);
            ((BumerangProjectile) projectile).setupPositionComponent(pc.getPosition());
            ((BumerangProjectile) projectile)
                    .setupVelocityAndProjectileComponent(pc, SkillTools.getCursorPositionAsPoint());
            ((BumerangProjectile) projectile).setupAnimationComponent();
            ((BumerangProjectile) projectile).setupHitboxComponent();
        }
        if (projectile instanceof GummyBearProjectile) {
            System.out.println("RangeCombatSkill gummybear");
            Random rng = new Random();

            projectile =
                    new GummyBearProjectile(
                            1,
                            gummyBearTextures.get(rng.nextInt(gummyBearTextures.size())),
                            5f,
                            0.3f,
                            start,
                            new HashSet<>());
            ((GummyBearProjectile) projectile).setupPositionComponent(pc.getPosition());
            ((GummyBearProjectile) projectile)
                    .setupVelocityAndProjectileComponent(pc, SkillTools.getCursorPositionAsPoint());
            ((GummyBearProjectile) projectile).setupAnimationComponent();
            ((GummyBearProjectile) projectile).setupHitboxComponent();
        }
        Game.addEntity(projectile);
    }
}
