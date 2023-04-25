package ecs.components;

import ecs.entities.Entity;
import starter.Game;

/**
 * OnDeath Funktion
 * wird ausgeführt sobald der Held stirbt
 */
public class OnHeroDeath implements IOnDeathFunction{

    /**
     * Beendet das Spiel mit Aufruf von Game.activateGameOver();
     * @param entity Entity that has died
     */
    @Override
    public void onDeath(Entity entity) {
        Game.activateGameOver();
    }
}
