package ecs.components.ai.idle;

import com.badlogic.gdx.ai.pfa.GraphPath;
import ecs.components.ai.AITools;
import ecs.entities.Entity;
import level.elements.tile.Tile;
import tools.Constants;

/**
 * Klasse für das AI-Verhalten FleeFromHero
 *
 * <p>idle-Methode implementiert das Verhalten Entity flieht bzw. entfernt sich vom Helden
 */
public class FleeFromHero implements IIdleAI {

    private final float radius;
    private GraphPath<Tile> path;
    private int distToHero;
    private final int breakTime;
    private int currentBreak = 0;

    /**
     * Konstruktor für die Klasse FleeFromHero
     *
     * @param pBreakTime Zeit in Sekunden bis ein neuer Pfad gesucht werden soll
     * @param pSearchRadius Radius in dem nach einer neuen Position gesucht werden soll
     */
    public FleeFromHero(int pBreakTime, int pSearchRadius) {
        breakTime = pBreakTime * Constants.FRAME_RATE;
        radius = pSearchRadius;
    }

    /**
     * FleeFromHero-Verhalten Implementierung
     *
     * @param entity associated entity
     */
    @Override
    public void idle(Entity entity) {
        if (path == null || AITools.pathFinishedOrLeft(entity, path)) {
            if (currentBreak >= breakTime) {
                currentBreak = 0;

                GraphPath<Tile> pathToHero = AITools.calculatePathToHero(entity);
                Tile heroTile = pathToHero.get(pathToHero.getCount() - 1);
                distToHero = pathToHero.getCount();
                int newDist = -1;

                while (newDist < distToHero) {
                    GraphPath<Tile> tryPath =
                            AITools.calculatePathToRandomTileInRange(entity, radius);
                    Tile myNewTile = tryPath.get(tryPath.getCount() - 1);

                    newDist =
                            AITools.calculatePath(
                                            myNewTile.getCoordinate(), heroTile.getCoordinate())
                                    .getCount();

                    path = tryPath;
                }

                idle(entity);
            }

            currentBreak++;

        } else AITools.move(entity, path);
    }
}
