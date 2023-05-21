package ecs.items;

import graphic.Animation;

import java.util.ArrayList;
import java.util.List;

public class Tasche<T extends ItemData> extends ItemData {

    private List<T> inventory;
    private Class<T> contentType;
    private int size;

    public Tasche( ItemType itemType,
                   Animation inventoryTexture,
                   Animation worldTexture,
                   String itemName,
                   String description,
                   Class<T> pContentType,
                   int pSize) {
        super( itemType,
               inventoryTexture,
               worldTexture,
               itemName,
               description);

        inventory = new ArrayList<>();
        contentType = pContentType;
        size = pSize;

    }

    public void addItem(T item) {
        if(!isFull()) {
            inventory.add(item);
        }
    }

    public void removeItem(T item) {
        inventory.remove(item);
    }

    public Class<T> getContentType() {
        return contentType;
    }

    public List<T> getItemsInBag() {
        return inventory;
    }

    public int getBagSize() {
        return size;
    }

    public boolean isFull() {
        return inventory.size() == size;
    }

    public boolean isEmpty() {
        return inventory.size() == 0;
    }
}
