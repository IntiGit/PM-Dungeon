package ecs.entities;

import dslToGame.AnimationBuilder;
import ecs.components.*;
import ecs.components.skill.*;
import ecs.components.xp.ILevelUp;
import ecs.components.xp.XPComponent;
import ecs.items.ItemData;
import ecs.items.Schuhe;
import ecs.items.Waffe;
import graphic.Animation;
import java.util.List;

/**
 * The Hero is the player character. It's entity in the ECS. This class helps to setup the hero with
 * all its components and attributes .
 */
public class Hero extends Entity implements ILevelUp {

    private final int fireballCoolDown = 5;
    private final int healCoolDown = 30;
    private final int speedCoolDown = 1;
    private final float xSpeed = 0.2f;
    private final float ySpeed = 0.2f;

    private final String pathToIdleLeft = "knight/idleLeft";
    private final String pathToIdleRight = "knight/idleRight";
    private final String pathToRunLeft = "knight/runLeft";
    private final String pathToRunRight = "knight/runRight";

    private Waffe weapon;
    private Schuhe shoes;

    /** Entity with Components */
    public Hero() {
        super();
        new PositionComponent(this);
        setupVelocityComponent();
        setupAnimationComponent();
        setupHitboxComponent();
        setupSkillComponent();
        new PlayableComponent(this);
        setupHealthComponent();
        setupXPComponent();
        setupInventoryComponent();
    }

    private void setupSkillComponent() {
        SkillComponent sc = new SkillComponent(this);
        sc.addSkill(
                new Skill(
                        new FireballSkill(SkillTools::getCursorPositionAsPoint),
                        fireballCoolDown,
                        1));
        setupCloseCombatSkill(sc);
    }

    public void setupCloseCombatSkill(SkillComponent sc) {
        sc.addSkill(
            new Skill(
                new CloseCombatSkill(
                    1,
                    weapon,
                    "",
                    1f,
                    0.1f),1,4));
    }

    private void setupRangeCombatSkills(SkillComponent sc) {
        PositionComponent pc = (PositionComponent) this.getComponent(PositionComponent.class).orElseThrow();
        sc.addSkill(
            new Skill(
                new RangeCombatSkill(new BumerangProjectile()),
                5f,
                5));
        sc.addSkill(
            new Skill(
                new RangeCombatSkill(new GummyBearProjectile()),
                5f,
                6));
    }

    private void setupVelocityComponent() {
        Animation moveRight = AnimationBuilder.buildAnimation(pathToRunRight);
        Animation moveLeft = AnimationBuilder.buildAnimation(pathToRunLeft);
        new VelocityComponent(this, xSpeed, ySpeed, moveLeft, moveRight);
    }

    private void setupAnimationComponent() {
        Animation idleRight = AnimationBuilder.buildAnimation(pathToIdleRight);
        Animation idleLeft = AnimationBuilder.buildAnimation(pathToIdleLeft);
        new AnimationComponent(this, idleLeft, idleRight);
    }

    private void setupHitboxComponent() {
        new HitboxComponent(
                this,
                (you, other, direction) -> System.out.println("heroCollisionEnter"),
                (you, other, direction) -> System.out.println("heroCollisionLeave"));
    }

    private void setupHealthComponent() {
        HealthComponent hc =
                new HealthComponent(
                        this,
                        10,
                        new OnHeroDeath(),
                        new Animation(List.of("knight_m_hit_anim_f0.png"), 300),
                        new Animation(List.of("knight_m_hit_anim_f0.png"), 300));

        hc.setCurrentHealthpoints(hc.getMaximalHealthpoints());
    }

    private void setupXPComponent() {
        new XPComponent(this, this);
    }

    private void setupInventoryComponent(){
        new InventoryComponent(this, 10);
    }

    public void equippWeapon(Waffe pWeapon) {
        weapon = pWeapon;
    }

    public void equippShoes(Schuhe pShoes) {
        shoes = pShoes;
    }

    public Waffe getWeapon() {
        return weapon;
    }

    public Schuhe getShoes() {
        return shoes;
    }

    @Override
    /**
     * Level Up verhalten des Helden Erhöht die maximalen Lebenspunkte des Helden Bei Level 5 bzw.
     * 10 werden neue Skills freigeschaltet
     */
    public void onLevelUp(long nexLevel) {
        System.out.println("Levelaustieg zu Level " + nexLevel);
        HealthComponent myHC =
                (HealthComponent) this.getComponent(HealthComponent.class).orElseThrow();
        int bonus = nexLevel % 5 == 0 ? 1 : 0;
        myHC.setMaximalHealthpoints(myHC.getMaximalHealthpoints() + bonus + 1);

        if (nexLevel == 2) {
            SkillComponent mySC =
                (SkillComponent) this.getComponent(SkillComponent.class).orElseThrow();
            setupRangeCombatSkills(mySC);
            System.out.println(
                "Fernkampf Angriffe freigeschaltet: "
                    + "\n"
                    + "Bumerang Angriff"
                    + "\n"
                    + "Taste: Q"
                    + "\n"
                    + "Gummiebaerchen Angriff"
                    + "\n"
                    + "Taste: R");
        }
        else if (nexLevel == 5) {
            SkillComponent mySC =
                    (SkillComponent) this.getComponent(SkillComponent.class).orElseThrow();
            mySC.addSkill(new Skill(new HealingSkill(), healCoolDown, 2));
            System.out.println(
                    "Heilungszauber freigeschaltet: "
                            + "\n"
                            + "Heilt dich um 30% bis 50% deiner maximalen Leben"
                            + "\n"
                            + "Cooldown: "
                            + healCoolDown
                            + "\n"
                            + "Kosten: Keine"
                            + "\n"
                            + "Taste: 2");
        } else if (nexLevel == 10) {
            SkillComponent mySC =
                    (SkillComponent) this.getComponent(SkillComponent.class).orElseThrow();
            mySC.addSkill(new Skill(new SpeedSkill(3), speedCoolDown, 3));

            System.out.println(
                    "Geschwindigkeitszauber freigeschaltet: "
                            + "\n"
                            + "Erhoeht oder verringert deine Geschwindigkeit zufaellig"
                            + "\n"
                            + "Cooldown: "
                            + speedCoolDown
                            + "\n"
                            + "Kosten: 3 Lebenspunkte"
                            + "\n"
                            + "Taste: 3");
        }
    }
}
