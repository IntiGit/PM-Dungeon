package graphic.hud.statDisplay;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import controller.ScreenController;
import ecs.components.skill.Skill;
import ecs.components.skill.SkillComponent;
import ecs.entities.Entity;

import java.util.HashSet;
import java.util.Set;

public class Skillanzeige extends ScreenController implements IHudElement{
    private Set<Skill> skillSet = new HashSet<>();

    /**
     * Creates a Screencontroller with a ScalingViewport which stretches the ScreenElements on
     * resize
     *
     * @param batch the batch which should be used to draw with
     */
    public Skillanzeige(SpriteBatch batch) {
        super(batch);
    }

    @Override
    public void update(Entity e) {
        SkillComponent sc = (SkillComponent) e.getComponent(SkillComponent.class).orElseThrow();
        skillSet = sc.getSkillSet();
    }
}
