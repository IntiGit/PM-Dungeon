package ecs.items;

import ecs.components.HealthComponent;
import ecs.components.InventoryComponent;
import ecs.entities.Entity;

public class UseWater implements IOnUse{
    @Override
    public void onUse(Entity e, ItemData item) {
        HealthComponent hc = (HealthComponent) e.getComponent(HealthComponent.class).orElseThrow();
        hc.setCurrentHealthpoints(hc.getCurrentHealthpoints() + 1);

        InventoryComponent ic = (InventoryComponent) e.getComponent(InventoryComponent.class).orElseThrow();
        if(!ic.removeItem(item)) {
            removeFromBag(item, ic);
        }
    }

    private void removeFromBag(ItemData item, InventoryComponent ic) {
        for(ItemData i : ic.getItems()) {
            if(i instanceof Tasche bag) {
                if(bag.getContentType().equals(Trank.class)) {
                    for(Object trank : bag.getItemsInBag()) {
                        if(trank.equals(item)) {
                            bag.removeItem(item);
                            break;
                        }
                    }
                }
            }
        }
    }
}
