package configuration;

import com.badlogic.gdx.Input;
import configuration.values.ConfigIntValue;

@ConfigMap(path = {"keyboard"})
public class KeyboardConfig {

    public static final ConfigKey<Integer> MOVEMENT_UP =
            new ConfigKey<>(new String[] {"movement", "up"}, new ConfigIntValue(Input.Keys.W));
    public static final ConfigKey<Integer> MOVEMENT_DOWN =
            new ConfigKey<>(new String[] {"movement", "down"}, new ConfigIntValue(Input.Keys.S));
    public static final ConfigKey<Integer> MOVEMENT_LEFT =
            new ConfigKey<>(new String[] {"movement", "left"}, new ConfigIntValue(Input.Keys.A));
    public static final ConfigKey<Integer> MOVEMENT_RIGHT =
            new ConfigKey<>(new String[] {"movement", "right"}, new ConfigIntValue(Input.Keys.D));
    public static final ConfigKey<Integer> INVENTORY_OPEN =
            new ConfigKey<>(new String[] {"inventory", "open"}, new ConfigIntValue(Input.Keys.I));
    public static final ConfigKey<Integer> INTERACT_WORLD =
            new ConfigKey<>(new String[] {"interact", "world"}, new ConfigIntValue(Input.Keys.E));
    public static final ConfigKey<Integer> FIRST_SKILL =
            new ConfigKey<>(new String[] {"skill", "1"}, new ConfigIntValue(Input.Keys.NUM_1));
    public static final ConfigKey<Integer> SECOND_SKILL =
            new ConfigKey<>(new String[] {"skill", "2"}, new ConfigIntValue(Input.Keys.NUM_2));
    public static final ConfigKey<Integer> THIRD_SKILL =
            new ConfigKey<>(new String[] {"skill", "3"}, new ConfigIntValue(Input.Keys.NUM_3));
    public static final ConfigKey<Integer> CLOSE_COMBAT_SKILL =
            new ConfigKey<>(new String[] {"skill", "4"}, new ConfigIntValue(Input.Keys.SPACE));
    public static final ConfigKey<Integer> RANGE_COMBAT_SKILL_1 =
            new ConfigKey<>(new String[] {"skill", "5"}, new ConfigIntValue(Input.Keys.Q));
    public static final ConfigKey<Integer> RANGE_COMBAT_SKILL_2 =
            new ConfigKey<>(new String[] {"skill", "6"}, new ConfigIntValue(Input.Keys.R));
    public static final ConfigKey<Integer> INVENTORY_NAVIGATE_LEFT =
            new ConfigKey<>(
                    new String[] {"inventory", "selectLeftItem"},
                    new ConfigIntValue(Input.Keys.LEFT));
    public static final ConfigKey<Integer> INVENTORY_NAVIGATE_RIGHT =
            new ConfigKey<>(
                    new String[] {"inventory", "selectRightItem"},
                    new ConfigIntValue(Input.Keys.RIGHT));
    public static final ConfigKey<Integer> INVENTORY_NAVIGATE_UP =
            new ConfigKey<>(
                    new String[] {"inventory", "selectUpItem"}, new ConfigIntValue(Input.Keys.UP));
    public static final ConfigKey<Integer> INVENTORY_NAVIGATE_DOWN =
            new ConfigKey<>(
                    new String[] {"inventory", "selectDownItem"},
                    new ConfigIntValue(Input.Keys.DOWN));
    public static final ConfigKey<Integer> INVENTORY_USE_ITEM =
            new ConfigKey<>(
                    new String[] {"inventory", "useItem"}, new ConfigIntValue(Input.Keys.ENTER));
}
