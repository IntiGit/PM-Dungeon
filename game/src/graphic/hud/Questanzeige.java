package graphic.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import controller.ScreenController;
import ecs.entities.Entity;
import ecs.entities.Hero;
import graphic.hud.statDisplay.IHudElement;
import quests.Quest;
import starter.Game;
import tools.Constants;
import tools.Point;

import java.util.ArrayList;
import java.util.List;


public class Questanzeige<T extends Actor> extends ScreenController<T> implements IHudElement {

    private List<Quest> questList = new ArrayList<>();
    private Quest toCheck;

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
        this.clear();
        showActiveQuests();
        showMenu();

    }

    @Override
    public void update(Entity e) {
        Hero h = (Hero) Game.getHero().get();
        questList = h.getMyQuests();
        showActiveQuests();
        if(Game.questScreenOpen) {
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

    private void showActiveQuests() {
        clear();
        int count = 1;
        for(Quest q : questList) {
            ScreenText questText =
                new ScreenText(
                    q.getProgress() + "% " + q.getDescription(),
                    new Point(0, 0),
                    3,
                    new LabelStyleBuilder(FontBuilder.DEFAULT_FONT)
                        .setFontcolor(Color.RED)
                        .build());
            questText.setFontScale(2);
            questText.setPosition(
                Constants.WINDOW_WIDTH - (questText.getWidth() * 1.5f),
                Constants.WINDOW_HEIGHT - count * (questText.getHeight() * 1.5f),
                Align.center | Align.bottom);
            add((T) questText);
        }
    }

    public void QuestAcceptDeny(Quest q) {
        toCheck = q;
        ScreenText questText =
            new ScreenText(
                q.getDescription(),
                new Point(0, 0),
                2,
                new LabelStyleBuilder(FontBuilder.DEFAULT_FONT)
                    .setFontcolor(Color.RED)
                    .build());
        questText.setPosition(
            Constants.WINDOW_WIDTH / 2 - questText.getWidth(),
            Constants.WINDOW_HEIGHT / 2 - questText.getHeight(),
            Align.center | Align.bottom);
        add((T) questText);
        ScreenText accept =
            new ScreenText(
                "Akzeptieren: U",
                new Point(0, 0),
                2,
                new LabelStyleBuilder(FontBuilder.DEFAULT_FONT)
                    .setFontcolor(Color.RED)
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
    }

    public void acceptQuest() {
        Hero h = (Hero) Game.getHero().get();
        h.addQuest(toCheck);
    }
}
