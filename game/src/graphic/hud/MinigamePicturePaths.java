package graphic.hud;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MinigamePicturePaths {
    private static Random rng = new Random();

    private static List<List<String>> pictureList = new ArrayList<>();

    private static List<String> test = List.of(
        "minigame/testPuzzle/1.png",
        "minigame/testPuzzle/2.png",
        "minigame/testPuzzle/3.png",
        "minigame/testPuzzle/4.png",
        "minigame/testPuzzle/5.png",
        "minigame/testPuzzle/6.png",
        "minigame/testPuzzle/7.png",
        "minigame/testPuzzle/8.png",
        "minigame/testPuzzle/empty.png"
            );

    public static void setupPictureList() {
        pictureList.add(test);
    }

    public static List<String> getRandomPicture() {
        return pictureList.get(rng.nextInt(0, pictureList.size()));
    }
}
