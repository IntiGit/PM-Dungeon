package ecs.components.ai.fight;

import com.badlogic.gdx.ai.pfa.GraphPath;
import ecs.components.ai.AITools;
import ecs.entities.Entity;
import level.elements.tile.Tile;
import tools.Constants;

/**
 * Klasse für das AI-Verhalten FollowHero
 *
 * <p>fight-Methode implementiert das Verhalten Entity verfolgt den Helden
 */
public class FollowHeroAI implements IFightAI {

    GraphPath<Tile> path;
    private int currentBreak = 0;
    private int breakTime;

    public FollowHeroAI(int pBreakTime) {
        breakTime = pBreakTime * Constants.FRAME_RATE;
    }

    /**
     * FollowHero-Verhalten Implementierung
     *
     * @param entity associated entity
     */
    @Override
    public void fight(Entity entity) {
        if (path == null || AITools.pathFinishedOrLeft(entity, path)) {
            if (currentBreak >= breakTime) {
                currentBreak = 0;
                path = AITools.calculatePathToHero(entity);
                fight(entity);
            }

            currentBreak++;

        } else AITools.move(entity, path);
    }
}
