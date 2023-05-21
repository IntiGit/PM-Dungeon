package ecs.items;

import graphic.Animation;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class ItemFactory {
    private static final List<String> missingTexture = List.of("animation/missingTexture.png");

    private final List<ItemData> allItems =
        List.of(
            new Waffe(
                ItemType.Passive,
                new Animation(missingTexture, 1),
                new Animation(missingTexture, 1),
                "Rostige Klinge",
                "Ein rostiges Kurzschwert.",
                3),
            new Waffe(
                ItemType.Passive,
                new Animation(missingTexture, 1),
                new Animation(missingTexture, 1),
                "Jade Klinge",
                "Ein scharfes, aus Jade geschmiedetes Schwert.",
                5),
            new Trank(
                ItemType.Active,
                new Animation(missingTexture, 1),
                new Animation(missingTexture, 1),
                "Wasser",
                "Hydriert und Heilt dich etwas...Trinken ist wichtig",
                new UseWater()),
            new Trank(
                ItemType.Active,
                new Animation(missingTexture, 1),
                new Animation(missingTexture, 1),
                "Lila Gebräu",
                "Verleiht dir kurzzeitig unglaubliche Kraft.",
                new UsePurplePotion()),
            new Schuhe(
                ItemType.Passive,
                new Animation(missingTexture, 1),
                new Animation(missingTexture, 1),
                "Dicke Treter",
                "Schuhe die sich zum zertreten von Fallen eignen"),
            new Schuhe(
                ItemType.Passive,
                new Animation(missingTexture, 1),
                new Animation(missingTexture, 1),
                "Noch dickere Treter",
                "Schwere Schuhe, einst getragen von einem Zyklop. Von der Sole tropft Gift."),
            new Tasche<Waffe>(
                ItemType.Basic,
                new Animation(missingTexture, 1),
                new Animation(missingTexture, 1),
                "Waffen Tasche",
                "Tasche zum Tragen von Waffen",
                Waffe.class,
                3),
            new Tasche<Trank>(
                ItemType.Basic,
                new Animation(missingTexture, 1),
                new Animation(missingTexture, 1),
                "Trank Tasche",
                "Tasche zum Tragen von Tränken",
                Trank.class,
                3),
            new Tasche<Schuhe>(
                ItemType.Basic,
                new Animation(missingTexture, 1),
                new Animation(missingTexture, 1),
                "Schuh Tasche",
                "Tasche zum Tragen von Schuhen",
                Schuhe.class,
                3)
            );

    private Set<String> types = Set.of("Waffe", "Trank", "Schuhe", "Tasche");

    private Random rng = new Random();

    public ItemData getRandomItem() {
        return allItems.get(rng.nextInt(allItems.size()));
    }

    public ItemData getRandomOfType(ItemData type) {
        if(!types.contains(type.getClass().getSimpleName())) {
            return null;
        }
        ItemData item = getRandomItem();
        while(!type.getClass().getSimpleName().equals(item.getClass().getSimpleName())) {
            item = getRandomItem();
        }
        return item;

    }
}
