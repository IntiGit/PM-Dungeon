package ecs.items;

import graphic.Animation;

/** Klasse die das Item Schlüssel darstellt */
public class Schluessel extends ItemData {

    /**
     * Konstruktor für die Klasse Schluessel
     *
     * @param itemType Item Typ des Items (Basic, Passive, Active)
     * @param inventoryTexture Textur des Items im Inventar
     * @param worldTexture Textur des Items im Spiel
     * @param itemName Name des Items
     * @param description Beschreibung des Items
     */
    public Schluessel(
            ItemType itemType,
            Animation inventoryTexture,
            Animation worldTexture,
            String itemName,
            String description) {
        super(itemType, inventoryTexture, worldTexture, itemName, description);
        this.setOnCollect(new OnCollectAddToBag());
    }
}
