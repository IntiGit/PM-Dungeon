package graphic.hud.statDisplay;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import controller.ScreenController;
import ecs.components.HealthComponent;
import ecs.entities.Entity;
import graphic.hud.FontBuilder;
import graphic.hud.LabelStyleBuilder;
import graphic.hud.ScreenImage;
import graphic.hud.ScreenText;
import tools.Point;

/**
 * Klasse welche eine graphische Lebensanzeige für den Helden realisiert
 *
 * @param <T>
 */
public class Lebensanzeige<T extends Actor> extends ScreenController<T> implements IHudElement {

    private int hp = 10;
    private ScreenText hpText;

    /** Konstruktor für die Klasse Lebensanzeige */
    public Lebensanzeige() {
        this(new SpriteBatch());
    }

    /**
     * Creates a Screencontroller with a ScalingViewport which stretches the ScreenElements on
     * resize
     *
     * @param batch the batch which should be used to draw with
     */
    public Lebensanzeige(SpriteBatch batch) {
        super(batch);

        ScreenImage screenImage = new ScreenImage("hud/Lebensanzeige.png", new Point(0, 0));
        screenImage.setPosition(screenImage.getWidth() / 2, 0, Align.center | Align.bottom);
        add((T) screenImage);

        hpText =
                new ScreenText(
                        hp + "",
                        new Point(0, 0),
                        3,
                        new LabelStyleBuilder(FontBuilder.DEFAULT_FONT)
                                .setFontcolor(Color.RED)
                                .build());
        hpText.setFontScale(3);
        hpText.setPosition(
                screenImage.getWidth() / 2, hpText.getHeight(), Align.center | Align.bottom);
        add((T) hpText);

        showMenu();
    }

    @Override
    public void update(Entity e) {
        HealthComponent hc = (HealthComponent) e.getComponent(HealthComponent.class).orElseThrow();
        hp = hc.getCurrentHealthpoints();
        hpText.setText(hp > 0 ? hp + "" : "0");
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
