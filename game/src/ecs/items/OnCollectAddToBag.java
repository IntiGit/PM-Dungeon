package ecs.items;

import ecs.components.InventoryComponent;
import ecs.components.ItemComponent;
import ecs.entities.Entity;
import starter.Game;

/** OnCollect Verhalten fuer Items die einer eventuellen Tasche hinzugefuegt werden sollen */
public class OnCollectAddToBag implements IOnCollect {

    private Class<?> typetocheck;

    /**
     * Implementiert das einfuegen des Items in das Inventar bzw. die passende Tasche
     *
     * @param worldItem
     * @param whoCollected
     */
    public void onCollect(Entity worldItem, Entity whoCollected) {

        ItemData data =
                worldItem
                        .getComponent(ItemComponent.class)
                        .map(ItemComponent.class::cast)
                        .get()
                        .getItemData();

        if (data.getItemName().equals("Rostige Klinge")
                || data.getItemName().equals("Jade Klinge")) {
            typetocheck = Waffe.class;
        } else if (data.getItemName().equals("Wasser")
                || data.getItemName().equals("Lila Gebräu")) {
            typetocheck = Trank.class;
        } else if (data.getItemName().equals("Dicke Treter")
                || data.getItemName().equals("Noch dickere Treter")) {
            typetocheck = Schuhe.class;
        }

        Game.getHero()
                .ifPresent(
                        hero -> {
                            if (whoCollected.equals(hero)) {
                                hero.getComponent(InventoryComponent.class)
                                        .ifPresent(
                                                (x) -> {
                                                    if (addToBag(
                                                            (InventoryComponent) x,
                                                            typetocheck,
                                                            worldItem)) {
                                                        Game.removeEntity(worldItem);
                                                    } else if (((InventoryComponent) x)
                                                            .addItem(data)) {
                                                        Game.removeEntity(worldItem);
                                                    }
                                                });
                            }
                        });
    }

    private Tasche<ItemData> checkForBag(InventoryComponent ic, Class<?> type) {
        for (ItemData item : ic.getItems()) {
            if (item instanceof Tasche bag) {
                if (bag.getContentType().equals(type) && !bag.isFull()) {
                    return bag;
                }
            }
        }
        return null;
    }

    private boolean addToBag(InventoryComponent ic, Class<?> type, Entity worldItem) {
        Tasche<ItemData> bag = checkForBag(ic, type);
        if (bag != null) {
            ItemData data =
                    worldItem
                            .getComponent(ItemComponent.class)
                            .map(ItemComponent.class::cast)
                            .get()
                            .getItemData();
            if (bag.getContentType().equals(Waffe.class)) {
                Waffe w =
                        new Waffe(
                                data.getItemType(),
                                data.getInventoryTexture(),
                                data.getWorldTexture(),
                                data.getItemName(),
                                data.getDescription(),
                                data.getItemName().equals("Rostige Klinge") ? 3 : 5);
                bag.addItem(w);
            } else if (bag.getContentType().equals(Trank.class)) {
                Trank t =
                        new Trank(
                                data.getItemType(),
                                data.getInventoryTexture(),
                                data.getWorldTexture(),
                                data.getItemName(),
                                data.getDescription(),
                                data.getOnUse());
                bag.addItem(t);
            } else if (bag.getContentType().equals(Schuhe.class)) {
                Schuhe s =
                        new Schuhe(
                                data.getItemType(),
                                data.getInventoryTexture(),
                                data.getWorldTexture(),
                                data.getItemName(),
                                data.getDescription());
                bag.addItem(s);
            }
            return true;
        }
        return false;
    }
}
