package ecs.items;

import graphic.Animation;

public class Schluessel extends ItemData{

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
