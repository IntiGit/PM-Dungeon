package graphic.hud.statDisplay;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import controller.ScreenController;
import ecs.components.xp.XPComponent;
import ecs.entities.Entity;

public class Levelanzeige<T extends Actor> extends ScreenController<T> implements IHudElement {

    private int lvl = 0;

    public Levelanzeige() {
        this(new SpriteBatch());
    }

    /**
     * Creates a Screencontroller with a ScalingViewport which stretches the ScreenElements on
     * resize
     *
     * @param batch the batch which should be used to draw with
     */
    public Levelanzeige(SpriteBatch batch) {
        super(batch);
    }


    @Override
    public void update(Entity e) {
        XPComponent xpC = (XPComponent) e.getComponent(XPComponent.class).orElseThrow();
        lvl = (int) xpC.getCurrentLevel();
    }

    @Override
    public void showMenu() {

    }

    @Override
    public void hideMenu() {

    }
}
