package ecs.items;

import com.badlogic.gdx.utils.Timer;
import ecs.components.InventoryComponent;
import ecs.entities.Entity;
import ecs.entities.Hero;

public class UsePurplePotion implements IOnUse {

    @Override
    public void onUse(Entity e, ItemData item) {
        if (e instanceof Hero h) {
            h.setplusDmg(5);
            Timer.schedule(
                    new Timer.Task() {
                        @Override
                        public void run() {
                            h.setplusDmg(1);
                        }
                    },
                    5f);
        }
        InventoryComponent ic =
                (InventoryComponent) e.getComponent(InventoryComponent.class).orElseThrow();
        if (!ic.removeItem(item)) {
            removeFromBag(item, ic);
        }
    }

    private void removeFromBag(ItemData item, InventoryComponent ic) {
        for (ItemData i : ic.getItems()) {
            if (i instanceof Tasche bag) {
                if (bag.getContentType().equals(Trank.class)) {
                    for (Object trank : bag.getItemsInBag()) {
                        if (trank.equals(item)) {
                            bag.removeItem(item);
                            break;
                        }
                    }
                }
            }
        }
    }
}
