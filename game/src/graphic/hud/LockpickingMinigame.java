package graphic.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import controller.ScreenController;
import ecs.components.InteractionComponent;
import ecs.components.InventoryComponent;
import ecs.components.skill.SkillTools;
import ecs.entities.Chest;
import ecs.entities.Entity;
import ecs.entities.Hero;
import ecs.items.ItemData;
import ecs.items.Schluessel;
import graphic.hud.statDisplay.IHudElement;
import java.util.*;
import java.util.logging.Logger;
import starter.Game;
import tools.Constants;
import tools.Point;

/**
 * Klasse welche das Lockpicking-Spiel anzeigt
 *
 * @param <T>
 */
public class LockpickingMinigame<T extends Actor> extends ScreenController<T>
        implements IHudElement {

    private final Logger minigameLogger = Logger.getLogger(this.getClass().getName());

    private ScreenImage[][] picture;
    /*      { [1][2][_] }       oben  = j-1  unten  = j+1
     *      { [3][4][5] }       links = i-1  rechts = i+1
     *      { [6][7][8] }
     */

    private ScreenImage[][] solved;
    private Chest chest;
    private Set<T> images = new HashSet<>();
    private boolean hasStarted = false;
    private boolean completed = false;

    private int emptyX;
    private int emptyY;

    private Random rng = new Random();

    List<String> picturePath;

    /** Konstruktor für die Klasse LockpickingMinigame */
    public LockpickingMinigame() {
        this(new SpriteBatch());
    }

    /**
     * Creates a Screencontroller with a ScalingViewport which stretches the ScreenElements on
     * resize
     *
     * @param batch the batch which should be used to draw with
     */
    public LockpickingMinigame(SpriteBatch batch) {
        super(batch);
        MinigamePicturePaths.setupPictureList();
        hideMenu();
    }

    /**
     * Startet das Lockpicking-Spiel
     *
     * @param c Kiste die der Spieler gerade versucht zu öffnen
     */
    public void startNewGame(Chest c) {
        picture = new ScreenImage[3][3];
        solved = new ScreenImage[3][3];
        chest = c;
        hasStarted = true;
        completed = false;
        emptyX = 2;
        emptyY = 0;
        picturePath = MinigamePicturePaths.getRandomPicture();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                picture[j][i] = new ScreenImage(picturePath.get(i * 3 + j), new Point(0, 0));
                solved[j][i] = picture[j][i];
            }
        }
        shuffle();
        minigameLogger.info(
                "Bring die einzelnen Teile wieder in die richtige Reihenfolge \n"
                        + "Du kannst ein Teil durch anklicken auf das leere Feld verschieben");
        Hero h = (Hero) Game.getHero().get();
        InventoryComponent ic =
                (InventoryComponent) h.getComponent(InventoryComponent.class).orElseThrow();
        for (ItemData i : ic.getItems()) {
            if (i instanceof Schluessel) {
                ic.removeItem(i);
                picture = solved;
            }
        }
    }

    private void shuffle() {
        int iterations = rng.nextInt(10, 31);
        for (int i = 0; i < iterations; i++) {
            findAndDoRandomSwap();
        }
        if (emptyX == 2 && emptyY == 0) {
            findAndDoRandomSwap();
        }
    }

    private void findAndDoRandomSwap() {
        List<int[]> swapWith = new ArrayList<>();

        if (emptyY - 1 >= 0) swapWith.add(new int[] {emptyX, emptyY - 1}); // Wenn oben existiert
        if (emptyY + 1 <= 2) swapWith.add(new int[] {emptyX, emptyY + 1}); // Wenn unten existiert
        if (emptyX - 1 >= 0) swapWith.add(new int[] {emptyX - 1, emptyY}); // Wenn links existiert
        if (emptyX + 1 <= 2) swapWith.add(new int[] {emptyX + 1, emptyY}); // Wenn rechts existiert

        int[] swapIndex = swapWith.get(rng.nextInt(0, swapWith.size()));
        swap(picture, swapIndex);
    }

    private void swap(ScreenImage[][] pic, int[] swapWithIndex) {
        ScreenImage tmp = pic[emptyX][emptyY];
        pic[emptyX][emptyY] = pic[swapWithIndex[0]][swapWithIndex[1]];
        pic[swapWithIndex[0]][swapWithIndex[1]] = tmp;

        emptyX = swapWithIndex[0];
        emptyY = swapWithIndex[1];
    }

    /**
     * Findet das Bild auf das Spieler geklickt hat und tauscht es falls möglich mit dem leeren
     * Platz
     */
    public void getTileClickedOn() {
        if (!hasStarted) {
            return;
        }
        Vector3 mouseWorldPosition =
                new Vector3(
                        SkillTools.getCursorPositionAsPoint().x,
                        SkillTools.getCursorPositionAsPoint().y,
                        0);

        Vector3 mouseScreenPosition = Game.camera.project(mouseWorldPosition);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                ScreenImage si = picture[j][i];
                float siLeft = si.getX();
                float siRight = si.getX() + si.getWidth() * si.getScaleX();

                float siBottom = si.getY();
                float siTop = si.getY() + si.getHeight() * si.getScaleY();

                if (mouseScreenPosition.x > siLeft && mouseScreenPosition.x < siRight) {
                    if (mouseScreenPosition.y > siBottom && mouseScreenPosition.y < siTop) {
                        swapIfPossible(picture, new int[] {j, i});
                        return;
                    }
                }
            }
        }
    }

    private void swapIfPossible(ScreenImage[][] pic, int[] swapIndex) {
        boolean canSwap = false;

        if (emptyX - 1 == swapIndex[0] && emptyY == swapIndex[1]) canSwap = true; // schau links
        else if (emptyX + 1 == swapIndex[0] && emptyY == swapIndex[1])
            canSwap = true; // schau rechts
        else if (emptyX == swapIndex[0] && emptyY - 1 == swapIndex[1]) canSwap = true; // schau oben
        else if (emptyX == swapIndex[0] && emptyY + 1 == swapIndex[1])
            canSwap = true; // schau unten

        if (canSwap) {
            swap(picture, swapIndex);
        }
    }

    private boolean checkIfComplete() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (picture != null && picture[j][i] != null) {
                    if (!picture[j][i].equals(solved[j][i])) {
                        return false;
                    }
                }
            }
        }
        minigameLogger.info("Geschafft! Die Truhe ist nun offen");
        chest.setLocked(false);
        InteractionComponent iac =
                (InteractionComponent) chest.getComponent(InteractionComponent.class).orElseThrow();
        iac.triggerInteraction();
        return true;
    }

    /**
     * Gibt an ob das Lockpicking-Spiel gelöst wurde
     *
     * @return true wenn das Lockpicking-Spiel gelöst ist
     */
    public boolean gameIsCompleted() {
        return completed;
    }

    /** Beendet das Lockpicking-Spiel */
    public void endGame() {
        completed = false;
        hasStarted = false;
        picture = null;
        solved = null;
        hideMenu();
    }

    @Override
    public void update(Entity e) {
        for (T t : images) {
            remove(t);
        }
        images.clear();
        if (picture != null) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    ScreenImage si = picture[j][i];
                    if (si != null) {
                        if (picturePath.get(0).equals("minigame/testPuzzel/1.png")) {
                            si.setScale(4);
                        } else {
                            si.setScale(1);
                        }

                        si.setPosition(
                                Constants.WINDOW_WIDTH / 4
                                        + (j + 1) * si.getWidth() * si.getScaleX(),
                                Constants.WINDOW_HEIGHT / 1.5f
                                        - (i + 1) * si.getHeight() * si.getScaleY(),
                                Align.center | Align.bottom);
                        add((T) si);
                        images.add((T) si);
                    }
                }
            }
        }
        if (!Arrays.deepEquals(picture, solved)) {
            ScreenText quit =
                    new ScreenText(
                            "Drücke ESC um das Spiel abzubrechen",
                            new Point(0, 0),
                            1,
                            new LabelStyleBuilder(FontBuilder.DEFAULT_FONT)
                                    .setFontcolor(Color.LIGHT_GRAY)
                                    .build());
            quit.setFontScale(2);
            quit.setPosition(
                    quit.getWidth() / 1.3f,
                    Constants.WINDOW_HEIGHT - quit.getHeight() * 4f,
                    Align.center | Align.bottom);
            add((T) quit);
            images.add((T) quit);
        }
        if (hasStarted && checkIfComplete()) {
            hasStarted = false;
            completed = true;
        }
        if (!hasStarted && completed) {
            ScreenText text =
                    new ScreenText(
                            "Geschafft\nDrücke\ndie\nlinke\nMaustaste",
                            new Point(0, 0),
                            1,
                            new LabelStyleBuilder(FontBuilder.DEFAULT_FONT)
                                    .setFontcolor(Color.RED)
                                    .build());
            text.setFontScale(2);
            text.setPosition(
                    text.getWidth(),
                    Constants.WINDOW_HEIGHT - text.getHeight() * 2f,
                    Align.center | Align.bottom);
            add((T) text);
            images.add((T) text);
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
