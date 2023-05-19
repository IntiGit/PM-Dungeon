package ecs.entities;

import dslToGame.AnimationBuilder;
import ecs.components.*;
import ecs.components.ai.AITools;
import ecs.components.xp.XPComponent;
import graphic.Animation;
import starter.Game;

/** Klasse die den Grabstein darstellt */
public class Grabstein extends Entity implements IInteraction {

    private final String texture = "objects.tombstone";
    private int healAmount;
    private int xpAmount;
    private float radius;
    private Geist geist;

    /**
     * Konstruktor für die Klasse Grabstein
     *
     * @param pHealAmount Anzahl an Lebenspunkten um die der Held geheilt wird
     * @param pXPAmount Anzahl an XP die der Held erhält
     * @param pRadius Radius der Interaktion
     */
    public Grabstein(int pHealAmount, int pXPAmount, float pRadius) {
        healAmount = pHealAmount;
        xpAmount = pXPAmount;
        radius = pRadius;

        setupInteractionComponent();
        setupAnimationComponent();
        setupPositionComponent();
    }

    private void setupInteractionComponent() {
        new InteractionComponent(this, radius, false, this);
    }

    private void setupAnimationComponent() {
        Animation idle = AnimationBuilder.buildAnimation(texture);
        new AnimationComponent(this, idle, idle);
    }

    private void setupPositionComponent() {
        new PositionComponent(this);
    }

    /**
     * Setzt den Geist dieses Grabsteins fest
     *
     * @param g neuer Geist für diesen Grabstein
     */
    public void setGeist(Geist g) {
        geist = g;
    }

    /**
     * Gibt den Geist diese Grabsteins zurück
     *
     * @return Geist dieses Grabsteins
     */
    public Geist getGeist() {
        return geist;
    }

    /**
     * Methode, die implementiert was bei Interaktion passieren soll
     *
     * @param entity entity mit dem interagiert wird
     *     <p>Es wird überprüft ob Geist und Held bei einander stehen. Falls ja wird zufällig eine
     *     Belohnung für den Helden bestimmt. Entweder er erhält Lebenspunkte oder XP.
     */
    @Override
    public void onInteraction(Entity entity) {
        PositionComponent pcHero =
                (PositionComponent)
                        Game.getHero().get().getComponent(PositionComponent.class).orElseThrow();
        PositionComponent pcGeist =
                (PositionComponent) geist.getComponent(PositionComponent.class).orElseThrow();

        if (AITools.inRange(pcHero.getPosition(), pcGeist.getPosition(), 2)) {
            if (Math.random() < 0.5) {
                System.out.println("Du erhältst " + xpAmount + " XP");
                XPComponent xpc =
                        (XPComponent)
                                Game.getHero().get().getComponent(XPComponent.class).orElseThrow();
                xpc.addXP(xpAmount);
            } else {
                System.out.println("Du erhaeltst " + healAmount + " Lebenspunkte");
                HealthComponent hc =
                        (HealthComponent)
                                Game.getHero()
                                        .get()
                                        .getComponent(HealthComponent.class)
                                        .orElseThrow();
                hc.setCurrentHealthpoints(hc.getCurrentHealthpoints() + healAmount);
                System.out.println(
                        "aktuelle Lebenspunte: "
                                + hc.getCurrentHealthpoints()
                                + "/"
                                + hc.getMaximalHealthpoints());
            }
        }
    }
}
