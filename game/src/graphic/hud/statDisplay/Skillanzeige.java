package graphic.hud.statDisplay;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import controller.ControllerLayer;
import controller.ScreenController;
import ecs.components.skill.Skill;
import ecs.components.skill.SkillComponent;
import ecs.entities.Entity;
import graphic.hud.FontBuilder;
import graphic.hud.LabelStyleBuilder;
import graphic.hud.ScreenImage;
import graphic.hud.ScreenText;
import starter.Game;
import tools.Constants;
import tools.Point;
import java.util.HashSet;
import java.util.Set;

public class Skillanzeige<T extends Actor> extends ScreenController<T> implements IHudElement{
    private Set<Skill> skillSet = new HashSet<>();
    private Set<T> text = new HashSet<>();
    private Set<T> images = new HashSet<>();

    public Skillanzeige() {
        this(new SpriteBatch());
    }

    /**
     * Creates a Screencontroller with a ScalingViewport which stretches the ScreenElements on
     * resize
     *
     * @param batch the batch which should be used to draw with
     */
    public Skillanzeige(SpriteBatch batch) {
        super(batch);
        showMenu();
    }

    @Override
    public void update(Entity e) {
        SkillComponent sc = (SkillComponent) e.getComponent(SkillComponent.class).orElseThrow();
        skillSet = sc.getSkillSet();
        for(T t : text) {
            remove(t);
        }
        for(T i : images) {
            remove(i);
        }
        text.clear();
        images.clear();
        int skillCount = 1;
        for(Skill s : skillSet) {
            ScreenText screenText =
                new ScreenText(
                    "Skill " + skillCount + ":",
                    new Point(0, 0),
                    3,
                    new LabelStyleBuilder(FontBuilder.DEFAULT_FONT)
                        .setFontcolor(Color.GOLDENROD)
                        .build());
            screenText.setFontScale(1.5f);
            screenText.setPosition(
                (Constants.WINDOW_WIDTH)/1.1f - screenText.getWidth(),
                screenText.getHeight() * skillCount * screenText.getFontScaleY() * 1.5f - screenText.getHeight() * 1.5f,
                Align.center | Align.bottom);
            text.add((T) screenText);
            add((T) screenText);

            ScreenImage screenImage =
                new ScreenImage("animation/missingTexture.png",new Point(0,0));
            screenImage.setPosition(
                (Constants.WINDOW_WIDTH)/1.1f + screenImage.getWidth(),
                screenText.getHeight() * skillCount * screenText.getFontScaleY() * 1.5f - screenText.getHeight() * 1.5f,
                Align.center | Align.bottom);
            images.add((T) screenImage);
            add((T) screenImage);
            skillCount++;
        }
        if(Game.inventoryOpen) {
            hideMenu();
        } else {
            showMenu();
        }
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
