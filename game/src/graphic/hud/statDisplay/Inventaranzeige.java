package graphic.hud.statDisplay;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import controller.ScreenController;
import ecs.components.InventoryComponent;
import ecs.entities.Entity;

public class Inventaranzeige extends ScreenController implements IHudElement{

    InventoryComponent inventory;
    /**
     * Creates a Screencontroller with a ScalingViewport which stretches the ScreenElements on
     * resize
     *
     * @param batch the batch which should be used to draw with
     */
    public Inventaranzeige(SpriteBatch batch) {
        super(batch);
    }

    @Override
    public void update(Entity e) {
        inventory = (InventoryComponent) e.getComponent(InventoryComponent.class).orElseThrow();
    }
}
