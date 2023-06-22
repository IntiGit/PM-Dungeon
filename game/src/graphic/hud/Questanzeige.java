package graphic.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import controller.ScreenController;
import ecs.entities.Entity;
import ecs.entities.Hero;
import graphic.hud.statDisplay.IHudElement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import quests.Quest;
import starter.Game;
import tools.Constants;
import tools.Point;

/**
 * Klasse welche die aktiven Quests als ScreenText anzeigt
 *
 * @param <T>
 */
public class Questanzeige<T extends Actor> extends ScreenController<T> implements IHudElement {

    private final Logger questLogger = Logger.getLogger(this.getClass().getName());
    private List<Quest> questList = new ArrayList<>();

    private Set<T> questAcceptText = new HashSet<>();
    private Set<T> screenTexts = new HashSet<>();
    private Quest toCheck;

    /** Konstruktor für die Klasse Questanzeige */
    public Questanzeige() {
        this(new SpriteBatch());
    }

    /**
     * Creates a Screencontroller with a ScalingViewport which stretches the ScreenElements on
     * resize
     *
     * @param batch the batch which should be used to draw with
     */
    public Questanzeige(SpriteBatch batch) {
        super(batch);
        showActiveQuests();
        showMenu();
    }

    @Override
    public void update(Entity e) {
        Hero h = (Hero) Game.getHero().get();
        questList = h.getMyQuests();
        showActiveQuests();
        if (Game.questScreenOpen) {
            QuestAcceptDeny(toCheck);
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

    /** Zeigt alle Quests an die der Held gerade hat */
    public void showActiveQuests() {
        removeAll();
        int count = 1;
        for (Quest q : questList) {
            ScreenText questText =
                    new ScreenText(
                            (int) q.getProgress() + "% " + q.getDescription(),
                            new Point(0, 0),
                            1,
                            new LabelStyleBuilder(FontBuilder.DEFAULT_FONT)
                                    .setFontcolor(Color.RED)
                                    .build());
            questText.setFontScale(1);
            questText.setPosition(
                    Constants.WINDOW_WIDTH / 2,
                    Constants.WINDOW_HEIGHT - count * (questText.getHeight() * 1.5f),
                    Align.center | Align.bottom);
            add((T) questText);
            count++;
            screenTexts.add((T) questText);
        }
    }

    /**
     * Zeigt eine Quest und die Möglichkeit zum Annehmen/Ablehnen der Quest an
     *
     * @param q Quest die Angenommen/Abgelehnt werden soll
     */
    public void QuestAcceptDeny(Quest q) {
        toCheck = q;
        ScreenText questText =
                new ScreenText(
                        q.getDescription(),
                        new Point(0, 0),
                        2,
                        new LabelStyleBuilder(FontBuilder.DEFAULT_FONT)
                                .setFontcolor(Color.GOLD)
                                .build());
        questText.setPosition(
                Constants.WINDOW_WIDTH / 2,
                Constants.WINDOW_HEIGHT / 1.7f + questText.getHeight(),
                Align.center | Align.bottom);
        add((T) questText);
        ScreenText accept =
                new ScreenText(
                        "Akzeptieren: U",
                        new Point(0, 0),
                        2,
                        new LabelStyleBuilder(FontBuilder.DEFAULT_FONT)
                                .setFontcolor(Color.GREEN)
                                .build());
        accept.setPosition(
                Constants.WINDOW_WIDTH / 2 - accept.getWidth() * 2,
                Constants.WINDOW_HEIGHT / 2 - questText.getHeight() * 2,
                Align.center | Align.bottom);
        add((T) accept);
        ScreenText deny =
                new ScreenText(
                        "Ablehnen: X",
                        new Point(0, 0),
                        2,
                        new LabelStyleBuilder(FontBuilder.DEFAULT_FONT)
                                .setFontcolor(Color.RED)
                                .build());
        deny.setPosition(
                Constants.WINDOW_WIDTH / 2 + deny.getWidth() * 2,
                Constants.WINDOW_HEIGHT / 2 - questText.getHeight() * 2,
                Align.center | Align.bottom);
        add((T) deny);

        questAcceptText.add((T) questText);
        questAcceptText.add((T) accept);
        questAcceptText.add((T) deny);
    }

    /** Fügt eine akzeptierte Quest dem Helden hinzu */
    public void acceptQuest() {
        Hero h = (Hero) Game.getHero().get();
        h.addQuest(toCheck);
        clearAcceptDenyText();
        questLogger.info("Quest " + toCheck.getDescription() + " angenommen");
    }

    /** Löscht vor jedem Zeichnen die Anzeige für das akzeptieren/ablehnen */
    public void clearAcceptDenyText() {
        for (T t : questAcceptText) {
            remove(t);
        }
        questAcceptText.clear();
    }

    /** Entfert vor dem Zeichnen die alten Zeichnungen/ScreenTexte */
    private void removeAll() {
        for (T t : screenTexts) {
            remove(t);
        }
        screenTexts.clear();
    }
}
