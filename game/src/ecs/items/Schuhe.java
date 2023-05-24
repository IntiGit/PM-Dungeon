package ecs.items;

import ecs.entities.Entity;
import ecs.entities.Hero;
import graphic.Animation;

/**
 * Klasse die den Item Typ Schuhe darstellt
 */
public class Schuhe extends ItemData implements IToggleEquipp {

    /**
     * Konstruktor fuer die Klasse Schuhe
     * @param itemType Item Typ des Items (Basic, Passive, Active)
     * @param inventoryTexture Textur des Items im Inventar
     * @param worldTexture Textur des Items im Spiel
     * @param itemName Name des Items
     * @param description Beschreibung des Items
     */
    public Schuhe(
            ItemType itemType,
            Animation inventoryTexture,
            Animation worldTexture,
            String itemName,
            String description) {
        super(itemType, inventoryTexture, worldTexture, itemName, description);
        this.setOnCollect(new OnCollectAddToBag());
    }

    /**
     * Implementiert das ausruesten von Schuhen
     * @param e Entity die das Item ausruestet
     */
    @Override
    public void toggleEquipp(Entity e) {
        if (e instanceof Hero hero) {
            if (hero.getShoes() != null && hero.getShoes().equals(this)) {
                hero.equippShoes(null);
            } else {
                hero.equippShoes(this);
            }
        }
    }
}
