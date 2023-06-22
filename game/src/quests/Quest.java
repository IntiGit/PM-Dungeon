package quests;

/** Oberklasse für alle Quests */
public abstract class Quest {
    /** Name bzw. Titel der Quest */
    protected String title;

    /** Beschreibung der Quest */
    protected String description;

    /** XP Menge die man als Belohnung kriegt */
    protected int rewardXP;

    /** Fortschritt der Quest in Prozent [0.00% - 100.00%] */
    protected float progress;

    /** Prüft Bedingungen für den Fortschriit der Quest und setzt den Fortschritt */
    public abstract void advanceProgress();

    /**
     * Getter für den Fortschritt der Quest
     *
     * @return Fortschritt der Quest in Prozent
     */
    public float getProgress() {
        return progress;
    }

    /**
     * Getter für die Belohnungs-XP der Quest
     *
     * @return
     */
    public int getRewardXP() {
        return rewardXP;
    }

    /**
     * Getter für die Beschreibung der Quest
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * Prüft ob die Quest abgeschlossen ist
     *
     * @return true wenn progress = 100 (gerundet)
     */
    public boolean isCompleted() {
        return Math.round(progress) == 100;
    }
}
