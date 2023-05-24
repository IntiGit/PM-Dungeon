package ecs.items;

import ecs.components.skill.Skill;
import ecs.components.skill.SkillComponent;
import ecs.entities.Entity;
import ecs.entities.Hero;
import graphic.Animation;

/**
 * Klasse die den Item Typ Waffe darstellt
 */
public class Waffe extends ItemData implements IToggleEquipp {

    private final int damage;

    /**
     * Konstruktor fuer die Klasse Waffe
     * @param itemType Item Typ des Items (Basic, Passive, Active)
     * @param inventoryTexture Textur des Items im Inventar
     * @param worldTexture Textur des Items im Spiel
     * @param itemName Name des Items
     * @param description Beschreibung des Items
     * @param pDamage Schaden der Waffe
     */
    public Waffe(
            ItemType itemType,
            Animation inventoryTexture,
            Animation worldTexture,
            String itemName,
            String description,
            int pDamage) {
        super(itemType, inventoryTexture, worldTexture, itemName, description);
        damage = pDamage;
        this.setOnCollect(new OnCollectAddToBag());
    }

    /**
     * Getter fuer den Schaden der Waffe
     * @return Schaden der Waffe
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Implementiert das ausruesten einer Waffe
     * @param e Entity die das Item ausruestet
     */
    @Override
    public void toggleEquipp(Entity e) {
        if (e instanceof Hero hero) {
            SkillComponent sc =
                    (SkillComponent) hero.getComponent(SkillComponent.class).orElseThrow();
            removeOldCloseCombatSkill(sc);
            if (hero.getWeapon() != null && hero.getWeapon().equals(this)) {
                hero.equippWeapon(null);
            } else {
                hero.equippWeapon(this);
            }
            hero.setupCloseCombatSkill(sc);
        }
    }

    private void removeOldCloseCombatSkill(SkillComponent sc) {
        for (Skill s : sc.getSkillSet()) {
            if (s.getSkillID() == 4) {
                sc.removeSkill(s);
                break;
            }
        }
    }
}
