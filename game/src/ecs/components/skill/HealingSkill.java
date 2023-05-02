package ecs.components.skill;

import ecs.components.HealthComponent;
import ecs.entities.Entity;
import java.util.Random;

/** Skill des Helden Heilt den Helden um 30 bis 50 Prozent seiner maximalen Leben */
public class HealingSkill implements ISkillFunction {

    private Random randomizer = new Random();

    /** Konstruktor */
    public HealingSkill() {}

    /**
     * Heilt den Helden und gibt die aktuellen Lebenspunkte aus
     *
     * @param entity which uses the skill
     */
    @Override
    public void execute(Entity entity) {
        HealthComponent myHC =
                (HealthComponent) entity.getComponent(HealthComponent.class).orElseThrow();
        int healAmountPercent = randomizer.nextInt(30, 51);
        myHC.setCurrentHealthpoints(
                myHC.getCurrentHealthpoints()
                        + healAmountPercent * myHC.getMaximalHealthpoints() / 100);

        System.out.println("Lebenspunkte: " + myHC.getCurrentHealthpoints());
    }
}
