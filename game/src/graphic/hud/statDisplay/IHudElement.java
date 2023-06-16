package graphic.hud.statDisplay;

import ecs.entities.Entity;

/** Interface welches von jedem HUD Element implementiert werden soll */
public interface IHudElement {

    /**
     * Benachrichtigt das HudElement, das es Änderungen gab
     *
     * @param e Entity bei dem sich etwas geändert hat
     */
    void update(Entity e);

    /** Methode zum anzeigen des Menüs */
    void showMenu();
    /** Methode zum verstecken des Menüs */
    void hideMenu();
}
