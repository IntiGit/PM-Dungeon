package graphic.hud.statDisplay;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import controller.ScreenController;
import ecs.components.HealthComponent;
import ecs.entities.Entity;
import ecs.entities.monsters.Monster;
import starter.Game;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class MonsterLebensanzeige extends ScreenController implements IHudElement {

    private Set<Monster> monster = new HashSet<>();

    /**
     * Creates a Screencontroller with a ScalingViewport which stretches the ScreenElements on
     * resize
     *
     * @param batch the batch which should be used to draw with
     */
    public MonsterLebensanzeige(SpriteBatch batch) {
        super(batch);
    }

    @Override
    public void update(Entity e) {
        monster = Game.getEntities()
            .stream()
            .filter((m) -> m instanceof Monster)
            .map((mon) -> (Monster) mon)
            .collect(Collectors.toSet());
    }
}
