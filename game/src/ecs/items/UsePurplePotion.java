package ecs.items;

import com.badlogic.gdx.utils.Timer;
import ecs.entities.Entity;
import ecs.entities.Hero;

public class UsePurplePotion implements IOnUse{

    @Override
    public void onUse(Entity e, ItemData item) {
        if(e instanceof Hero h) {
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
    }
}
