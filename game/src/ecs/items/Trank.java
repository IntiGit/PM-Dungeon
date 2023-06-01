package ecs.items;

import graphic.Animation;

/**
 * Klasse die den Item Typ Trank darstellt
 */
public class Trank extends ItemData {

    /**
     * Konstruktor der Klasse Trank
     * @param itemType Item Typ des Items (Basic, Passive, Active)
     * @param inventoryTexture Textur des Items im Inventar
     * @param worldTexture Textur des Items im Spiel
     * @param itemName Name des Items
     * @param description Beschreibung des Items
     * @param onUse Methode die implementiert was beim benutzen des Items passiert
     */
    public Trank(
            ItemType itemType,
            Animation inventoryTexture,
            Animation worldTexture,
            String itemName,
            String description,
            IOnUse onUse) {
        super(itemType, inventoryTexture, worldTexture, itemName, description);
        this.setOnCollect(new OnCollectAddToBag());
        this.setOnUse(onUse);
    }
}
