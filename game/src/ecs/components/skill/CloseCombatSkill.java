package ecs.components.skill;

import dslToGame.AnimationBuilder;
import ecs.components.AnimationComponent;
import ecs.components.HealthComponent;
import ecs.components.HitboxComponent;
import ecs.components.MissingComponentException;
import ecs.components.PositionComponent;
import ecs.components.VelocityComponent;
import ecs.components.collision.ICollide;
import ecs.damage.Damage;
import ecs.damage.DamageType;
import ecs.entities.Entity;
import ecs.entities.Hero;
import ecs.items.Waffe;
import graphic.Animation;
import starter.Game;
import tools.Point;

/** Klasse fuer die Nahkampf Skills */
public class CloseCombatSkill implements ISkillFunction {

    private int dmg;
    private String pathToTexture;
    private float range;
    private float speed;
    private boolean hasAttacked = false;

    /**
     * Konstruktor fuer die Klasse CloseCombatSkill
     *
     * @param dmg Schaden des Projektils
     * @param weapon Waffe die der Held traegt
     * @param pathToTexture Pfad zur Textur der Waffe
     * @param range Reichweite des Projektils
     * @param speed Geschwindigkeit des Projektils
     */
    public CloseCombatSkill(int dmg, Waffe weapon, String pathToTexture, float range, float speed) {
        this.dmg = weapon == null ? dmg : dmg + weapon.getDamage();
        this.pathToTexture = pathToTexture;
        this.range = range;
        this.speed = speed;
    }

    /**
     * Implementiert die Funktionsweise der Skills
     *
     * @param entity which uses the skill
     */
    @Override
    public void execute(Entity entity) {
        AnimationComponent ac =
                (AnimationComponent) entity.getComponent(AnimationComponent.class).orElseThrow();
        String texture = "animation/empty.png";
        int blickrichtung;
        String dir = "";
        if (ac.getIdleLeft().equals(ac.getCurrentAnimation())) {
            // Blickrichtung links
            if (entity instanceof Hero h && h.getWeapon() != null) {
                if (h.getWeapon().getItemName().equals("Jade Klinge")) {
                    texture = "character/knight/attack/swords/katana/attackLeft";
                }
                if (h.getWeapon().getItemName().equals("Rostige Klinge")) {
                    texture = "character/knight/attack/swords/rostigeKlinge/attackLeft";
                }
            }
            blickrichtung = -1;
            dir = "W";
        } else {
            // Blickrichtung rechts
            if (entity instanceof Hero h && h.getWeapon() != null) {
                if (h.getWeapon().getItemName().equals("Jade Klinge")) {
                    texture = "character/knight/attack/swords/katana/attackRight";
                }
                if (h.getWeapon().getItemName().equals("Rostige Klinge")) {
                    texture = "character/knight/attack/swords/rostigeKlinge/attackRight";
                }
            }
            blickrichtung = 1;
            dir = "E";
        }

        PositionComponent pc =
                (PositionComponent) entity.getComponent(PositionComponent.class).orElseThrow();
        ITargetSelection ts;
        if (blickrichtung < 0) {
            ts = () -> new Point(pc.getPosition().x - 1, pc.getPosition().y);
        } else if (blickrichtung > 0) {
            ts = () -> new Point(pc.getPosition().x + 1, pc.getPosition().y);
        } else {
            ts = pc::getPosition;
        }
        int plusDmg = 0;
        if (entity instanceof Hero h) plusDmg = h.getplusDmg();

        createProjectile(entity, texture, ts, this.range, this.speed, this.dmg + plusDmg, dir);
    }

    private void createProjectile(
            Entity entity,
            String pathToTexturesOfProjectile,
            ITargetSelection selectionFunction,
            float projectileRange,
            float projectileSpeed,
            int projectileDamage,
            String dir) {
        hasAttacked = false;
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
                    if (b != entity && !hasAttacked) {
                        b.getComponent(HealthComponent.class)
                                .ifPresent(
                                        hc -> {
                                            applyKnockback(b, dir, 2);
                                            ((HealthComponent) hc)
                                                    .receiveHit(
                                                            new Damage(
                                                                    projectileDamage,
                                                                    DamageType.NEUTRAL,
                                                                    null));
                                            hasAttacked = true;
                                        });
                    }
                };

        new HitboxComponent(projectile, new Point(0.25f, 0.25f), new Point(1, 1), collide, null);
    }

    private void applyKnockback(Entity e, String dir, int knockbackAmount) {
        VelocityComponent vc =
                (VelocityComponent) e.getComponent(VelocityComponent.class).orElseThrow();
        PositionComponent pc =
                (PositionComponent) e.getComponent(PositionComponent.class).orElseThrow();
        Point checkPosition;
        if (dir.equals("E")) {
            checkPosition = new Point(pc.getPosition().x + knockbackAmount, pc.getPosition().y);
            if (Game.currentLevel.getTileAt(checkPosition.toCoordinate()).isAccessible()) {
                vc.setCurrentXVelocity(knockbackAmount);
            }
        } else if (dir.equals("W")) {
            checkPosition = new Point(pc.getPosition().x - 2, pc.getPosition().y);
            if (Game.currentLevel.getTileAt(checkPosition.toCoordinate()).isAccessible()) {
                vc.setCurrentXVelocity(-knockbackAmount);
            }
        }
    }
}
