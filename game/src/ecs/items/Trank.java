package ecs.items;

import graphic.Animation;

public class Trank extends ItemData{

    public Trank( ItemType itemType,
                   Animation inventoryTexture,
                   Animation worldTexture,
                   String itemName,
                   String description,
                   IOnUse onUse) {
        super( itemType,
            inventoryTexture,
            worldTexture,
            itemName,
            description);
        this.setOnCollect(new OnCollectAddToBag());
        this.setOnUse(onUse);
    }
}
