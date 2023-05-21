package ecs.items;

import ecs.components.skill.Skill;
import ecs.components.skill.SkillComponent;
import ecs.entities.Entity;
import ecs.entities.Hero;
import graphic.Animation;

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
            SkillComponent sc = (SkillComponent) hero.getComponent(SkillComponent.class).orElseThrow();
            removeOldCloseCombatSkill(sc);
            if(hero.getWeapon() != null && hero.getWeapon().equals(this)) {
                hero.equippWeapon(null);
            } else {
                hero.equippWeapon(this);
            }
            hero.setupCloseCombatSkill(sc);
        }
    }

    private void removeOldCloseCombatSkill(SkillComponent sc) {
        for(Skill s : sc.getSkillSet()) {
            if(s.getSkillID() == 4) {
                sc.removeSkill(s);
                break;
            }
        }
    }
}
