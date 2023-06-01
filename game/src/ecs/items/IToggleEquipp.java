package ecs.items;

import ecs.entities.Entity;

/**
 * Interface fuer Items die man ausruesten kann
 */
public interface IToggleEquipp {

    /**
     * Methode die das Verhalten von Items beim Ausruesten implementiert
     * @param e Entity die das Item ausruestet
     */
    void toggleEquipp(Entity e);
}
