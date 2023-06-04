package graphic.hud.statDisplay;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import controller.ScreenController;
import ecs.components.HealthComponent;
import ecs.entities.Entity;

public class Lebensanzeige extends ScreenController implements IHudElement {

    private int hp = 10;

    /**
     * Creates a Screencontroller with a ScalingViewport which stretches the ScreenElements on
     * resize
     *
     * @param batch the batch which should be used to draw with
     */
    public Lebensanzeige(SpriteBatch batch) {
        super(batch);
    }

    @Override
    public void update(Entity e) {
        HealthComponent hc = (HealthComponent) e.getComponent(HealthComponent.class).orElseThrow();
        hp = hc.getCurrentHealthpoints();
    }
}
