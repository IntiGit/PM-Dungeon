package ecs.components.skill;
import ecs.components.HealthComponent;
import ecs.components.VelocityComponent;
import ecs.damage.Damage;
import ecs.damage.DamageType;
import ecs.entities.Entity;

import java.util.Random;

/**
 * Skill des Helden
 * Erhöht/Verringert die Geschwindigkeit des Helden
 * Range: 0.1 bis 0.4
 */
public class SpeedSkill implements ISkillFunction{

    private final int healthCost;
    Random randomizer = new Random();

    /**
     * Konstruktor
     * @param pHealthCost Kosten für den Skill
     */
    public SpeedSkill(int pHealthCost) {
        this.healthCost = pHealthCost;
    }

    /**
     * Führt den SpeedSkill aus
     * @param entity which uses the skill
     */
    @Override
    public void execute(Entity entity) {
        VelocityComponent myVC = (VelocityComponent) entity.getComponent(VelocityComponent.class).orElseThrow();
        HealthComponent myHC = (HealthComponent) entity.getComponent(HealthComponent.class).orElseThrow();
        myHC.receiveHit(new Damage(healthCost, DamageType.NEUTRAL, entity));
        float newXYSpeed = randomizer.nextFloat(0.1f, 0.4f);
        myVC.setXVelocity(newXYSpeed);
        myVC.setYVelocity(newXYSpeed);

        System.out.println("Lebenspunkte: " + (myHC.getCurrentHealthpoints() - healthCost));
    }
}
