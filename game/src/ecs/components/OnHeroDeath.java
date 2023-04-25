package ecs.components;

import ecs.entities.Entity;
import starter.Game;

public class OnHeroDeath implements IOnDeathFunction{

    @Override
    public void onDeath(Entity entity) {
        Game.activateGameOver();
    }
}
