package ecs.components.skill;

import ecs.components.HealthComponent;
import ecs.entities.Entity;

import java.util.Random;

public class HealingSkill implements ISkillFunction{

    private Random randomizer = new Random();
    public HealingSkill() {

    }


    @Override
    public void execute(Entity entity) {
        HealthComponent myHC = (HealthComponent) entity.getComponent(HealthComponent.class).orElseThrow();
        int healAmountPercent = randomizer.nextInt(30,51);
        myHC.setCurrentHealthpoints(myHC.getCurrentHealthpoints() +
            healAmountPercent * myHC.getMaximalHealthpoints() / 100);

        System.out.println("Lebenspunkte: " + myHC.getCurrentHealthpoints());
    }
}
