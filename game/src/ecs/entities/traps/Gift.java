package ecs.entities.traps;

import dslToGame.AnimationBuilder;
import ecs.components.AnimationComponent;
import ecs.components.HitboxComponent;
import ecs.components.PositionComponent;
import ecs.entities.Entity;
import graphic.Animation;
import level.elements.tile.Tile;
import starter.Game;

public class Gift extends Falle{

    public Gift(int pDmg) {
        trapDmg = pDmg;
        active = true;

        pathToAnimationActive = "traps/poison";
        pathToAnimationInactive = "traps/poison";

        setupPositionComponent();
        setupAnimationComponent();
        setupHitboxComponent();
    }

    void setupPositionComponent() {
        new PositionComponent(this);
    }

    @Override
    void setupAnimationComponent() {
        Animation active = AnimationBuilder.buildAnimation(pathToAnimationActive);
        Animation inactive = AnimationBuilder.buildAnimation(pathToAnimationInactive);
        new AnimationComponent(this, active, inactive);
    }

    @Override
    void setupHitboxComponent() {
        new HitboxComponent(this, this, (a, b, from) -> {});
    }

    @Override
    public void onCollision(Entity a, Entity b, Tile.Direction from) {

    }
}
