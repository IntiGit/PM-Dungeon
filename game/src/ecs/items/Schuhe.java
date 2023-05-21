package ecs.items;

import ecs.entities.Entity;
import ecs.entities.Hero;
import graphic.Animation;

public class Schuhe extends ItemData implements IToggleEquipp{

    public Schuhe( ItemType itemType,
                   Animation inventoryTexture,
                   Animation worldTexture,
                   String itemName,
                   String description) {
        super( itemType,
            inventoryTexture,
            worldTexture,
            itemName,
            description);
        this.setOnCollect(new OnCollectAddToBag());
    }

    @Override
    public void toggleEquipp(Entity e) {
        if(e instanceof Hero hero) {
            if(hero.getShoes() != null && hero.getShoes().equals(this)) {
                hero.equippShoes(null);
            } else {
                hero.equippShoes(this);
            }
        }
    }
}
