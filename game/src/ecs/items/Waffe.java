package ecs.items;

import ecs.entities.Entity;
import ecs.entities.Hero;
import graphic.Animation;
import starter.Game;

import java.time.chrono.HijrahEra;
import java.util.HashMap;

public class Waffe extends ItemData implements IToggleEquipp{

    private final int damage;

    public Waffe( ItemType itemType,
                  Animation inventoryTexture,
                  Animation worldTexture,
                  String itemName,
                  String description,
                  int pDamage) {
        super( itemType,
               inventoryTexture,
               worldTexture,
               itemName,
               description);
        damage = pDamage;
        this.setOnCollect(new OnCollectAddToBag());
    }

    public int getDamage() {
        return damage;
    }

    @Override
    public void toggleEquipp(Entity e) {
        if(e instanceof Hero hero) {
            if(hero.getWeapon() != null && hero.getWeapon().equals(this)) {
                hero.equippWeapon(null);
            } else {
                hero.equippWeapon(this);
            }
        }
    }
}
