package graphic.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import controller.ScreenController;
import ecs.components.IOnDeathFunction;
import ecs.entities.Entity;
import tools.Constants;
import tools.Point;

public class GameOverMenu<T extends Actor> extends ScreenController<T> implements IOnDeathFunction {

    /** Creates a new PauseMenu with a new Spritebatch */
    public GameOverMenu() {
        this(new SpriteBatch());
    }

    /** Creates a new PauseMenu with a given Spritebatch */
    public GameOverMenu(SpriteBatch batch) {
        super(batch);
        ScreenText gameOverText =
            new ScreenText(
                "GAME OVER",
                new Point(0, 0),
                3,
                new LabelStyleBuilder(FontBuilder.DEFAULT_FONT)
                    .setFontcolor(Color.RED)
                    .build());
        gameOverText.setFontScale(3);
        gameOverText.setPosition(
            (Constants.WINDOW_WIDTH) / 2f - gameOverText.getWidth(),
            (Constants.WINDOW_HEIGHT) / 1.5f + gameOverText.getHeight(),
            Align.center | Align.bottom);
        add((T) gameOverText);

        ScreenText screenText =
            new ScreenText(
                "Drücke X um das Spiel zu beenden \n" +
                    "Oder U um von Vorne zu beginnen",
                new Point(0, 0),
                1,
                new LabelStyleBuilder(FontBuilder.DEFAULT_FONT)
                    .setFontcolor(Color.CYAN)
                    .build());
        screenText.setFontScale(1);
        screenText.setPosition(
            (Constants.WINDOW_WIDTH) / 2f,
            (Constants.WINDOW_HEIGHT) / 1.5f - (Constants.WINDOW_HEIGHT)/2f,
            Align.center | Align.bottom);
        add((T) screenText);
        hideMenu();
    }

    /** shows the Menu */
    public void showMenu() {
        this.forEach((Actor s) -> s.setVisible(true));
    }

    /** hides the Menu */
    public void hideMenu() {
        this.forEach((Actor s) -> s.setVisible(false));
    }

    @Override
    public void onDeath(Entity entity) {
        showMenu();
    }
}
