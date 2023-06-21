package graphic.hud;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** Klasse in der Pfade zu Bildern abgespeichert sind Bilder für das Lockpicking-Spiel */
public class MinigamePicturePaths {
    private static Random rng = new Random();

    private static List<List<String>> pictureList = new ArrayList<>();

    private static List<String> test =
            List.of(
                    "minigame/testPuzzel/1.png",
                    "minigame/testPuzzel/2.png",
                    "minigame/testPuzzel/empty.png",
                    "minigame/testPuzzel/3.png",
                    "minigame/testPuzzel/4.png",
                    "minigame/testPuzzel/5.png",
                    "minigame/testPuzzel/6.png",
                    "minigame/testPuzzel/7.png",
                    "minigame/testPuzzel/8.png");

    private static List<String> blumenBild =
            List.of(
                    "minigame/blumenpuzzle/blumen1.png",
                    "minigame/blumenpuzzle/blumen2.png",
                    "minigame/blumenpuzzle/blumenEmpty.png",
                    "minigame/blumenpuzzle/blumen3.png",
                    "minigame/blumenpuzzle/blumen4.png",
                    "minigame/blumenpuzzle/blumen5.png",
                    "minigame/blumenpuzzle/blumen6.png",
                    "minigame/blumenpuzzle/blumen7.png",
                    "minigame/blumenpuzzle/blumen8.png");

    /** speichert alle Bilder in einer Liste */
    public static void setupPictureList() {
        pictureList.add(test);
        pictureList.add(blumenBild);
    }

    /**
     * Gibt eine zufällige Liste aus Pfaden aus der Liste der Bilder zurück
     *
     * @return eine Liste aus Pfaden zu Bildern
     */
    public static List<String> getRandomPicture() {
        return pictureList.get(rng.nextInt(0, pictureList.size()));
    }
}
