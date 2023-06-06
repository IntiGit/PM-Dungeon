package graphic.hud.statDisplay;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import controller.ScreenController;
import ecs.components.InventoryComponent;
import ecs.entities.Entity;
import ecs.items.*;
import graphic.hud.ScreenImage;
import starter.Game;
import tools.Point;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Inventaranzeige<T extends Actor> extends ScreenController<T> implements IHudElement{

    private InventoryComponent inventory;
    private List<?> bagInventory;
    private int selectedIndex;
    private ItemData selectedItem;
    private Set<T> images = new HashSet<>();
    private ScreenImage selection = new ScreenImage("hud/selected.png", new Point(0,0));

    public Inventaranzeige() {
        this(new SpriteBatch());
    }

    /**
     * Creates a Screencontroller with a ScalingViewport which stretches the ScreenElements on
     * resize
     *
     * @param batch the batch which should be used to draw with
     */
    public Inventaranzeige(SpriteBatch batch) {
        super(batch);
        hideMenu();
    }

    public void setupMenu() {
        for(T t : images) {
            remove(t);
        }
        images.clear();
        int itemCount = 1;
        for(ItemData item : inventory.getItems()) {
            ScreenImage img =
                new ScreenImage(item.getInventoryTexture().getAnimationFrames().get(0),new Point(0,0));
            img.setSize(32,32);
            img.setPosition(
                img.getWidth() * 2 * (itemCount - 1),
                img.getHeight()
            );
            images.add((T) img);
            add((T) img);
            itemCount++;
        }
        if(!inventory.getItems().isEmpty()) {
            selection.setPosition(0, 32);
            images.add((T) selection);
            add((T) selection);
            selectedItem = inventory.getItems().get(0);
            selectedIndex = 0;
        }

        Game.bagOpen = false;
    }

    public void selectNextItemHorizontal() {
        if(inventory.getItems().size() > 0) {
            selectedIndex = (selectedIndex + 1) % inventory.getItems().size();
            selectedItem = inventory.getItems().get(selectedIndex);
            selection.setPosition(selectedIndex * 64, 32);
        }
    }

    public void selectPreviousItemHorizontal() {
        if(inventory.getItems().size() > 0) {
            selectedIndex = (selectedIndex - 1) % inventory.getItems().size();
            if (selectedIndex < 0) {
                selectedIndex = inventory.getItems().size() - 1;
            }
            selectedItem = inventory.getItems().get(selectedIndex);
            selection.setPosition(selectedIndex * 64, 32);
        }
    }

    public void selectNextItemVertical() {
        if(bagInventory.size() > 0) {
            selectedIndex = (selectedIndex + 1) % bagInventory.size();
            selectedItem = (ItemData) bagInventory.get(selectedIndex);
            selection.setPosition(selection.getX(), 64 * (selectedIndex + 1) + 32);
        }
    }

    public void selectPreviousItemVertical() {
        if(bagInventory.size() > 0) {
            selectedIndex = (selectedIndex - 1) % bagInventory.size();
            if (selectedIndex < 0) {
                selectedIndex = bagInventory.size() - 1;
            }
            selectedItem = (ItemData) bagInventory.get(selectedIndex);
            selection.setPosition(selection.getX(), 64 * (selectedIndex+1) + 32);
        }
    }

    public void useItem() {
        if(selectedItem instanceof Trank) {
            selectedItem.triggerUse(Game.getHero().get());
            setupMenu();
        }
        else if(selectedItem instanceof Waffe || selectedItem instanceof Schuhe) {
            ((IToggleEquipp) selectedItem).toggleEquipp(Game.getHero().get());
        }
        else if(selectedItem instanceof Tasche<?> t) {
            bagInventory = t.getItemsInBag();
            int itemCount = 1;
            for (int i = 0; i < t.getItemsInBag().size(); i++) {
                ScreenImage img =
                    new ScreenImage(
                        t.getItemsInBag()
                            .get(i)
                            .getInventoryTexture()
                            .getAnimationFrames()
                            .get(0),
                        new Point(0,0));
                img.setSize(32,32);
                img.setPosition(
                    img.getWidth() * 2 * selectedIndex,
                    img.getHeight() * 2 * itemCount + img.getHeight()
                );
                images.add((T) img);
                add((T) img);
                itemCount++;
            }
            selectedIndex = -1;
            selectNextItemVertical();
            Game.bagOpen = true;
        }
    }

    @Override
    public void update(Entity e) {
        inventory = (InventoryComponent) e.getComponent(InventoryComponent.class).orElseThrow();

    }

    @Override
    public void showMenu() {
        setupMenu();
        this.forEach((Actor s) -> s.setVisible(true));
    }

    @Override
    public void hideMenu() {
        this.forEach((Actor s) -> s.setVisible(false));
    }
}
