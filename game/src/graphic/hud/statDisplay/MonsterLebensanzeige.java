package graphic.hud.statDisplay;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import controller.ScreenController;
import ecs.components.HealthComponent;
import ecs.components.PositionComponent;
import ecs.components.ai.AIComponent;
import ecs.entities.Entity;
import ecs.entities.monsters.ChestMonster;
import ecs.entities.monsters.Monster;
import graphic.hud.FontBuilder;
import graphic.hud.LabelStyleBuilder;
import graphic.hud.ScreenText;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import starter.Game;
import tools.Point;

/**
 * Klasse welche eine graphische Lebensanzeige für alle Monster realisiert
 *
 * @param <T>
 */
public class MonsterLebensanzeige<T extends Actor> extends ScreenController<T>
        implements IHudElement {

    private Set<Monster> monster = new HashSet<>();
    private Set<T> anzeigen = new HashSet<>();
    ScreenText hpText;

    /** Konstruktor für die Klasse Monster */
    public MonsterLebensanzeige() {
        this(new SpriteBatch());
    }

    /**
     * Creates a Screencontroller with a ScalingViewport which stretches the ScreenElements on
     * resize
     *
     * @param batch the batch which should be used to draw with
     */
    public MonsterLebensanzeige(SpriteBatch batch) {
        super(batch);
        showMenu();
    }

    @Override
    public void update(Entity e) {
        for (T t : anzeigen) {
            remove(t);
        }
        anzeigen.clear();
        monster =
                Game.getEntities().stream()
                        .filter((m) -> m instanceof Monster)
                        .map((mon) -> (Monster) mon)
                        .collect(Collectors.toSet());

        for (Monster m : monster) {
            if (m instanceof ChestMonster c) {
                AIComponent aic = (AIComponent) c.getComponent(AIComponent.class).orElseThrow();
                if (!aic.getTransitionAI().isInFightMode(m)) {
                    continue;
                }
            }
            PositionComponent pc =
                    (PositionComponent) m.getComponent(PositionComponent.class).orElseThrow();
            if (Game.camera.isPointInFrustum(pc.getPosition().x, pc.getPosition().y)) {
                Vector3 screenPosition =
                        Game.camera.project(
                                new Vector3(pc.getPosition().x, pc.getPosition().y, 0f));
                HealthComponent hc =
                        (HealthComponent) m.getComponent(HealthComponent.class).orElseThrow();
                hpText =
                        new ScreenText(
                                hc.getCurrentHealthpoints() + "",
                                new Point(0, 0),
                                3,
                                new LabelStyleBuilder(FontBuilder.DEFAULT_FONT)
                                        .setFontcolor(Color.RED)
                                        .build());
                hpText.setFontScale(1);
                hpText.setPosition(screenPosition.x, screenPosition.y, Align.center | Align.bottom);
                anzeigen.add((T) hpText);
                add((T) hpText);
            }
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
