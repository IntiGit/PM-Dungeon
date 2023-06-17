package graphic.hud;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import controller.ScreenController;
import ecs.entities.Entity;
import graphic.hud.statDisplay.IHudElement;
import tools.Constants;
import tools.Point;

import java.util.*;

public class LockpickingMinigame<T extends Actor> extends ScreenController<T> implements IHudElement {

    private ScreenImage[][] picture = new ScreenImage[3][3];
    /*      { [1][2][3] }       oben  = i-1  unten  = i+1
     *      { [4][5][6] }       links = j-1  rechts = j+1
     *      { [7][8][_] }
     */

    private Set<T> images = new HashSet<>();

    private int emptyX = 2;
    private int emptyY = 2;

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
        showMenu();
    }

    public void startNewGame() {
        List<String> picturePath = MinigamePicturePaths.getRandomPicture();
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                picture[i][j] = new ScreenImage(picturePath.get(i * 3 + j), new Point(0, 0));
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
        int i = emptyY;
        int j = emptyX;
        List<int[]> swapWith= new ArrayList<>();

        if(i-1 >= 0) swapWith.add(new int[]{i-1,j}); //Wenn oben existiert
        if(i+1 <= 2) swapWith.add(new int[]{i+1,j}); //Wenn unten existiert
        if(j-1 >= 0) swapWith.add(new int[]{i,j-1}); //Wenn links existiert
        if(j+1 <= 2) swapWith.add(new int[]{i,j+1}); //Wenn rechts existiert

        int[] swapIndex = swapWith.get(rng.nextInt(0,swapWith.size())); // y,x

        swap(picture,swapIndex);

    }

    private void swap(ScreenImage[][] pic, int[] swapWithIndex) {
        ScreenImage tmp = pic[emptyY][emptyX];
        pic[emptyY][emptyX] = pic[swapWithIndex[0]][swapWithIndex[1]];
        pic[swapWithIndex[0]][swapWithIndex[1]] = tmp;

        emptyY = swapWithIndex[0];
        emptyX = swapWithIndex[1];
    }

    @Override
    public void update(Entity e) {
        for(T t : images) {
            remove(t);
        }
        images.clear();
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                ScreenImage si = picture[i][j];
                if(si != null) {
                    si.setPosition(
                        Constants.WINDOW_WIDTH / 4 + (i + 1) * si.getWidth() * 2,
                        Constants.WINDOW_HEIGHT / 3 + (j + 1) * si.getHeight() * 2,
                        Align.center | Align.bottom
                    );
                    add((T) si);
                    images.add((T) si);
                }
            }
        }
    }

    @Override
    public void showMenu() {

    }

    @Override
    public void hideMenu() {

    }
}
