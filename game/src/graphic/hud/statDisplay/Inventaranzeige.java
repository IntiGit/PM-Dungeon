package graphic.hud.statDisplay;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.StringBuilder;
import controller.ScreenController;
import ecs.components.InventoryComponent;
import ecs.entities.Entity;
import ecs.items.*;
import graphic.hud.FontBuilder;
import graphic.hud.LabelStyleBuilder;
import graphic.hud.ScreenImage;
import graphic.hud.ScreenText;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import starter.Game;
import tools.Constants;
import tools.Point;

/**
 * Klasse welche eine graphische Anzeige des Inventars des Helden realisiert
 *
 * @param <T>
 */
public class Inventaranzeige<T extends Actor> extends ScreenController<T> implements IHudElement {

    private InventoryComponent inventory;
    private List<?> bagInventory;
    private int selectedIndex;
    private ItemData selectedItem;
    private Set<T> images = new HashSet<>();
    private Set<T> text = new HashSet<>();
    private ScreenImage selection = new ScreenImage("hud/selected.png", new Point(0, 0));

    /** Konstruktor für die Klasse Inventaranzeige */
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

    private void setupMenu() {
        for (T t : images) {
            remove(t);
        }
        images.clear();
        int itemCount = 1;
        createBackground();
        for (ItemData item : inventory.getItems()) {

            ScreenImage img =
                    new ScreenImage(
                            item.getInventoryTexture().getAnimationFrames().get(0),
                            new Point(0, 0));
            img.setSize(32, 32);
            img.setPosition(img.getWidth() * 2 * (itemCount - 1), img.getHeight());
            images.add((T) img);
            add((T) img);
            itemCount++;
        }
        if (!inventory.getItems().isEmpty()) {
            selection.setPosition(0, 32);
            images.add((T) selection);
            add((T) selection);
            selectedItem = inventory.getItems().get(0);
            selectedIndex = 0;
        }
        showItemDescription();
        Game.bagOpen = false;
    }

    private void createBackground() {
        for (int i = 1; i < 11; i++) {
            ScreenImage bg = new ScreenImage("hud/InventoryBackground.png", new Point(0, 0));
            bg.setSize(32, 32);
            bg.setPosition(bg.getWidth() * 2 * (i - 1), bg.getHeight());
            images.add((T) bg);
            add((T) bg);
        }
    }

    /** Wählt das nächste Item im Inventar aus Schrittrichtung rechts -> */
    public void selectNextItemHorizontal() {
        if (inventory.getItems().size() > 0) {
            selectedIndex = (selectedIndex + 1) % inventory.getItems().size();
            selectedItem = inventory.getItems().get(selectedIndex);
            selection.setPosition(selectedIndex * 64, 32);
        }
        showItemDescription();
    }

    /** Wählt das vorherige Item im Inventar aus Schrittrichtung links <- */
    public void selectPreviousItemHorizontal() {
        if (inventory.getItems().size() > 0) {
            selectedIndex = (selectedIndex - 1) % inventory.getItems().size();
            if (selectedIndex < 0) {
                selectedIndex = inventory.getItems().size() - 1;
            }
            selectedItem = inventory.getItems().get(selectedIndex);
            selection.setPosition(selectedIndex * 64, 32);
        }
        showItemDescription();
    }

    /** Wählt das nächste Item in einer Tasche aus Schrittrichtung hoch */
    public void selectNextItemVertical() {
        if (bagInventory.size() > 0) {
            selectedIndex = (selectedIndex + 1) % bagInventory.size();
            selectedItem = (ItemData) bagInventory.get(selectedIndex);
            selection.setPosition(selection.getX(), 64 * (selectedIndex + 1) + 32);
        }
        showItemDescription();
    }

    /** Wählt das vorherige Item in einer Tasche aus Schrittrichtung runter */
    public void selectPreviousItemVertical() {
        if (bagInventory.size() > 0) {
            selectedIndex = (selectedIndex - 1) % bagInventory.size();
            if (selectedIndex < 0) {
                selectedIndex = bagInventory.size() - 1;
            }
            selectedItem = (ItemData) bagInventory.get(selectedIndex);
            selection.setPosition(selection.getX(), 64 * (selectedIndex + 1) + 32);
        }
        showItemDescription();
    }

    /** Benutzt/Verwendet das aktuell ausgewählte Item */
    public void useItem() {
        if (selectedItem instanceof Trank) {
            selectedItem.triggerUse(Game.getHero().get());
            selectedItem = null;
            setupMenu();
        } else if (selectedItem instanceof Waffe || selectedItem instanceof Schuhe) {
            ((IToggleEquipp) selectedItem).toggleEquipp(Game.getHero().get());
        } else if (selectedItem instanceof Tasche<?> t) {
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
                                new Point(0, 0));
                img.setSize(32, 32);
                img.setPosition(
                        img.getWidth() * 2 * selectedIndex,
                        img.getHeight() * 2 * itemCount + img.getHeight());
                images.add((T) img);
                add((T) img);
                itemCount++;
            }
            selectedIndex = -1;
            selectNextItemVertical();
            Game.bagOpen = true;
        }
    }

    private void showItemDescription() {
        for (T t : text) {
            remove(t);
        }
        text.clear();
        if (selectedItem != null) {
            ScreenText name =
                    createScreenText(
                            3f,
                            Color.LIGHT_GRAY,
                            (Constants.WINDOW_WIDTH) / 2f,
                            (Constants.WINDOW_HEIGHT));
            text.add((T) name);
            add((T) name);

            String[] descriptionParts = selectedItem.getDescription().split(" ");
            StringBuilder s = new StringBuilder();
            float x = 0f;
            for (int i = 0; i < descriptionParts.length; i++) {
                s.append(descriptionParts[i] + " ");
                if ((i + 1) % 5 == 0 || i == descriptionParts.length - 1) {
                    ScreenText description =
                            new ScreenText(
                                    s.toString(),
                                    new Point(0, 0),
                                    2,
                                    new LabelStyleBuilder(FontBuilder.DEFAULT_FONT)
                                            .setFontcolor(Color.WHITE)
                                            .build());
                    description.setFontScale(2f);
                    if (i < 5) x = description.getWidth();
                    float y =
                            (Constants.WINDOW_HEIGHT)
                                    - description.getHeight() * description.getFontScaleY()
                                    - name.getHeight() * name.getFontScaleY();
                    y = y - description.getHeight() * 2 * ((i / 5));

                    description.setPosition(x, y, Align.center | Align.bottom);
                    text.add((T) description);
                    add((T) description);

                    s.clear();
                }
            }
        }
    }

    private ScreenText createScreenText(float fontScale, Color color, float xPos, float yPos) {
        ScreenText name =
                new ScreenText(
                        selectedItem.getItemName(),
                        new Point(0, 0),
                        fontScale,
                        new LabelStyleBuilder(FontBuilder.DEFAULT_FONT)
                                .setFontcolor(color)
                                .build());
        name.setFontScale(fontScale);
        name.setPosition(
                xPos - name.getWidth(),
                yPos - name.getHeight() * name.getFontScaleY(),
                Align.center | Align.bottom);
        return name;
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
