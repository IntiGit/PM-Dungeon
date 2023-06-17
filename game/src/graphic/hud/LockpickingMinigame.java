package graphic.hud;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import controller.ScreenController;
import ecs.components.skill.SkillTools;
import ecs.entities.Chest;
import ecs.entities.Entity;
import graphic.hud.statDisplay.IHudElement;
import starter.Game;
import tools.Constants;
import tools.Point;

import java.util.*;

public class LockpickingMinigame<T extends Actor> extends ScreenController<T> implements IHudElement {

    private ScreenImage[][] picture = new ScreenImage[3][3];
    /*      { [1][2][3] }       oben  = j-1  unten  = j+1
     *      { [4][5][6] }       links = i-1  rechts = i+1
     *      { [7][8][_] }
     */

    private ScreenImage[][] solved = new ScreenImage[3][3];
    private Chest chest;
    private Set<T> images = new HashSet<>();
    private boolean hasStarted = false;
    private boolean completed = false;

    private int emptyX;
    private int emptyY;

    private Random rng = new Random();

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

    public void startNewGame(Chest c) {
        chest = c;
        hasStarted = true;
        completed = false;
        emptyX = 2;
        emptyY = 2;
        List<String> picturePath = MinigamePicturePaths.getRandomPicture();
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                picture[j][i] = new ScreenImage(picturePath.get(i * 3 + j), new Point(0, 0));
                solved[j][i] = picture[j][i];
            }
        }
        shuffle();
    }

    private void shuffle() {
        int iterations = rng.nextInt(10,31);
        for(int i = 0; i < iterations; i++) {
            findAndDoRandomSwap();
        }
    }

    private void findAndDoRandomSwap() {
        List<int[]> swapWith= new ArrayList<>();

        if(emptyY-1 >= 0) swapWith.add(new int[]{emptyX, emptyY-1}); //Wenn oben existiert
        if(emptyY+1 <= 2) swapWith.add(new int[]{emptyX, emptyY+1}); //Wenn unten existiert
        if(emptyX-1 >= 0) swapWith.add(new int[]{emptyX-1,emptyY}); //Wenn links existiert
        if(emptyX+1 <= 2) swapWith.add(new int[]{emptyX+1,emptyY}); //Wenn rechts existiert

        int[] swapIndex = swapWith.get(rng.nextInt(0,swapWith.size()));
        swap(picture,swapIndex);

    }

    private void swap(ScreenImage[][] pic, int[] swapWithIndex) {
        ScreenImage tmp = pic[emptyX][emptyY];
        pic[emptyX][emptyY] = pic[swapWithIndex[0]][swapWithIndex[1]];
        pic[swapWithIndex[0]][swapWithIndex[1]] = tmp;

        emptyX = swapWithIndex[0];
        emptyY = swapWithIndex[1];
    }

    public void getTileClickedOn() {
        if(!hasStarted) {
            return;
        }
        Vector3 mouseWorldPosition =
            new Vector3(
                SkillTools.getCursorPositionAsPoint().x,
                SkillTools.getCursorPositionAsPoint().y,
                0);

        Vector3 mouseScreenPosition = Game.camera.project(mouseWorldPosition);

        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                ScreenImage si = picture[j][i];
                float siLeft = si.getX();
                float siRight = si.getX() + si.getWidth() * si.getScaleX();

                float siBottom = si.getY();
                float siTop = si.getY() + si.getHeight() * si.getScaleY();

                if(mouseScreenPosition.x > siLeft && mouseScreenPosition.x < siRight) {
                    if(mouseScreenPosition.y > siBottom && mouseScreenPosition.y < siTop) {
                        swapIfPossible(picture, new int[]{j, i});
                        return;
                    }
                }
            }
        }

    }

    private void swapIfPossible(ScreenImage[][] pic, int[] swapIndex) {
        boolean canSwap = false;

        if(emptyX-1 == swapIndex[0] && emptyY == swapIndex[1]) canSwap = true;  //schau links
        else if(emptyX+1 == swapIndex[0] && emptyY == swapIndex[1]) canSwap = true;  //schau rechts
        else if(emptyX == swapIndex[0] && emptyY-1 == swapIndex[1]) canSwap = true;  //schau oben
        else if(emptyX == swapIndex[0] && emptyY+1 == swapIndex[1]) canSwap = true;  //schau unten

        if(canSwap) {
            swap(picture, swapIndex);
        }
    }

    private boolean checkIfComplete() {
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                if(picture != null && picture[j][i] != null) {
                    if (!picture[j][i].equals(solved[j][i])) {
                        return false;
                    }
                }
            }
        }
        chest.setLocked(false);
        chest.dropItems(chest);
        return true;
    }

    public boolean gameIsCompleted() {
        return completed;
    }

    @Override
    public void update(Entity e) {
        for(T t : images) {
            remove(t);
        }
        images.clear();
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                ScreenImage si = picture[j][i];
                if(si != null) {
                    si.setScale(4);
                    si.setPosition(
                        Constants.WINDOW_WIDTH / 4 + (j + 1) * si.getWidth() * si.getScaleX(),
                        Constants.WINDOW_HEIGHT / 1.5f - (i + 1) * si.getHeight() * si.getScaleY(),
                        Align.center | Align.bottom
                    );
                    add((T) si);
                    images.add((T) si);
                }
            }
        }
        if(hasStarted && checkIfComplete()) {
            hasStarted = false;
            completed = true;
            System.out.println("COMPLETE");
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
