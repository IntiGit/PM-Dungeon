package graphic.hud.statDisplay;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import controller.ScreenController;
import ecs.components.xp.XPComponent;
import ecs.entities.Entity;
import graphic.hud.FontBuilder;
import graphic.hud.LabelStyleBuilder;
import graphic.hud.ScreenText;
import tools.Constants;
import tools.Point;

public class Levelanzeige<T extends Actor> extends ScreenController<T> implements IHudElement {

    private int lvl = 0;
    private ScreenText levelText;

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
        levelText =
            new ScreenText(
                "Level: " + lvl,
                new Point(0, 0),
                3,
                new LabelStyleBuilder(FontBuilder.DEFAULT_FONT)
                    .setFontcolor(Color.CHARTREUSE)
                    .build());
        levelText.setFontScale(2);
        levelText.setPosition(
            levelText.getWidth() / 2,
            Constants.WINDOW_HEIGHT - levelText.getHeight() * 2,
            Align.center | Align.bottom);
        add((T) levelText);

        showMenu();
    }


    @Override
    public void update(Entity e) {
        XPComponent xpC = (XPComponent) e.getComponent(XPComponent.class).orElseThrow();
        lvl = (int) xpC.getCurrentLevel();
        levelText.setText("Level: " + lvl);
    }

    @Override
    public void showMenu() {
        this.forEach((Actor s) -> s.setVisible(true));
    }

    @Override
    public void hideMenu() {
        this.forEach((Actor s) -> s.setVisible(false));
    }
}
