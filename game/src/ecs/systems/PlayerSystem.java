package ecs.systems;

import com.badlogic.gdx.Gdx;
import configuration.KeyboardConfig;
import ecs.components.MissingComponentException;
import ecs.components.PlayableComponent;
import ecs.components.VelocityComponent;
import ecs.components.skill.Skill;
import ecs.components.skill.SkillComponent;
import ecs.entities.Entity;
import ecs.tools.interaction.InteractionTool;
import starter.Game;

import java.util.Set;

/** Used to control the player */
public class PlayerSystem extends ECS_System {

    private record KSData(Entity e, PlayableComponent pc, VelocityComponent vc) {}

    @Override
    public void update() {
        Game.getEntities().stream()
            .flatMap(e -> e.getComponent(PlayableComponent.class).stream())
            .map(pc -> buildDataObject((PlayableComponent) pc))
            .forEach(this::checkKeystroke);
    }

    private void checkKeystroke(KSData ksd) {
        if (Gdx.input.isKeyPressed(KeyboardConfig.MOVEMENT_UP.get()))
            ksd.vc.setCurrentYVelocity(1 * ksd.vc.getYVelocity());
        else if (Gdx.input.isKeyPressed(KeyboardConfig.MOVEMENT_DOWN.get()))
            ksd.vc.setCurrentYVelocity(-1 * ksd.vc.getYVelocity());
        else if (Gdx.input.isKeyPressed(KeyboardConfig.MOVEMENT_RIGHT.get()))
            ksd.vc.setCurrentXVelocity(1 * ksd.vc.getXVelocity());
        else if (Gdx.input.isKeyPressed(KeyboardConfig.MOVEMENT_LEFT.get()))
            ksd.vc.setCurrentXVelocity(-1 * ksd.vc.getXVelocity());

        if (Gdx.input.isKeyPressed(KeyboardConfig.INTERACT_WORLD.get()))
            InteractionTool.interactWithClosestInteractable(ksd.e);

            // check skills
        else if (Gdx.input.isKeyPressed(KeyboardConfig.FIRST_SKILL.get())) {
            executeSkill(ksd, 1);
        }
        else if (Gdx.input.isKeyPressed(KeyboardConfig.SECOND_SKILL.get()))
            executeSkill(ksd, 2);
        else if (Gdx.input.isKeyPressed(KeyboardConfig.THIRD_SKILL.get()))
            executeSkill(ksd, 3);
    }

    private KSData buildDataObject(PlayableComponent pc) {
        Entity e = pc.getEntity();

        VelocityComponent vc =
            (VelocityComponent)
                e.getComponent(VelocityComponent.class)
                    .orElseThrow(PlayerSystem::missingVC);

        return new KSData(e, pc, vc);
    }

    private static MissingComponentException missingVC() {
        return new MissingComponentException("VelocityComponent");
    }

    private void executeSkill(KSData ksd, int pSkillID) {
        SkillComponent heroSC = (SkillComponent) ksd.e.getComponent(SkillComponent.class).orElseThrow();
        Set<Skill> heroSkills = heroSC.getSkillSet();
        for(Skill s : heroSkills) {
            if(s.getSkillID() == pSkillID) {
                s.execute(ksd.e);
            }
        }
    }
}
