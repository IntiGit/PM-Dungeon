package graphic.hud.statDisplay;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import controller.ScreenController;
import ecs.components.InventoryComponent;
import ecs.entities.Entity;

public class Inventaranzeige<T extends Actor> extends ScreenController<T> implements IHudElement{

    InventoryComponent inventory;

    public Inventaranzeige() {
        this(new SpriteBatch());
    }

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

    @Override
    public void showMenu() {

    }

    @Override
    public void hideMenu() {

    }
}
