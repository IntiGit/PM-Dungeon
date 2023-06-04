package graphic.hud.statDisplay;

import ecs.entities.Entity;

public interface IHudElement {

    void update(Entity e);
    void showMenu();
    void hideMenu();
}
