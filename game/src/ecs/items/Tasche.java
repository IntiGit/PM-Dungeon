package ecs.items;

import graphic.Animation;
import java.util.ArrayList;
import java.util.List;

/**
 * generische Klasse die den Item Typ Tasche darstellt
 * @param <T> Item Typ des Inhalts der Tasche
 */
public class Tasche<T extends ItemData> extends ItemData {

    private List<T> inventory;
    private Class<T> contentType;
    private int size;

    /**
     * Konstruktor fuer die Klasse Tasche
     * @param itemType
     * @param inventoryTexture
     * @param worldTexture
     * @param itemName
     * @param description
     * @param pContentType
     * @param pSize
     */
    public Tasche(
            ItemType itemType,
            Animation inventoryTexture,
            Animation worldTexture,
            String itemName,
            String description,
            Class<T> pContentType,
            int pSize) {
        super(itemType, inventoryTexture, worldTexture, itemName, description);

        inventory = new ArrayList<>();
        contentType = pContentType;
        size = pSize;
    }

    /**
     * Fuegt ein Item der Tasche hinzu
     * @param item Item das hinzugefuegt werden soll
     */
    public void addItem(T item) {
        if (!isFull()) {
            inventory.add(item);
        }
    }

    /**
     * Entfernt ein Item aus der Tasche
     * @param item Item das entfernt werden soll
     */
    public void removeItem(T item) {
        inventory.remove(item);
    }

    /**
     * Getter fuer den Inhalt der Tasche
     * @return Klasse des Inhalts
     */
    public Class<T> getContentType() {
        return contentType;
    }

    /**
     * Getter fuer den Tascheninhalt
     * @return Liste der Items in der Tasche
     */
    public List<T> getItemsInBag() {
        return inventory;
    }

    /**
     * Getter fuer die groesse der Tasche
     * @return groesse der Tasche
     */
    public int getBagSize() {
        return size;
    }

    /**
     * Ueberprueft ob die Tasche voll ist
     * @return true wenn die Tasche voll ist
     */
    public boolean isFull() {
        return inventory.size() == size;
    }

    /**
     * Ueberprueft ob die Tasche leer ist
     * @return true wenn die Tasche leer ist
     */
    public boolean isEmpty() {
        return inventory.size() == 0;
    }
}
