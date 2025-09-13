package com.adam4cz.tfcseasonnotifier.config;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

import com.adam4cz.tfcseasonnotifier.TFCSeasonNotifier;

import net.dries007.tfc.util.calendar.Month;
import net.minecraft.ChatFormatting;
import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {

    // Enum for colors in the config file
    public enum ConfigColor {
        
        INHERIT, BLACK, DARK_BLUE, DARK_GREEN, DARK_AQUA, DARK_RED, DARK_PURPLE, GOLD, GRAY, DARK_GRAY, BLUE, GREEN, AQUA, RED, LIGHT_PURPLE, YELLOW, WHITE;

        public ChatFormatting toChatFormatting() {
            if (this == INHERIT) {
                return null;
            }
            return ChatFormatting.valueOf(this.name());
        }

        public static ConfigColor[] valuesWithoutInherit() {
            return Arrays.stream(values())
                        .filter(c -> c != INHERIT)
                        .toArray(ConfigColor[]::new);
        }
    }

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.BooleanValue GLOBAL_ENABLED;
    public static final ForgeConfigSpec.BooleanValue JOIN_ENABLED;
    public static final ForgeConfigSpec.BooleanValue DEBUG_ENABLED;
    public static final ForgeConfigSpec.ConfigValue<ConfigColor> TITLE_COLOR;
    public static final ForgeConfigSpec.ConfigValue<ConfigColor> SUBTITLE_COLOR;
    public static final ForgeConfigSpec.IntValue FADE_IN;
    public static final ForgeConfigSpec.IntValue STAY;
    public static final ForgeConfigSpec.IntValue FADE_OUT;

    public static final Map<Month, ForgeConfigSpec.BooleanValue> MONTH_ENABLED = new EnumMap<>(Month.class);
    public static final Map<Month, ForgeConfigSpec.ConfigValue<ConfigColor>> MONTH_TITLE_COLOR = new EnumMap<>(Month.class);
    public static final Map<Month, ForgeConfigSpec.ConfigValue<ConfigColor>> MONTH_SUBTITLE_COLOR = new EnumMap<>(Month.class);

    public static final ForgeConfigSpec.BooleanValue USE_LANG;
    public static final Map<Month, ForgeConfigSpec.ConfigValue<String>> MONTH_CUSTOM_TITLE = new EnumMap<>(Month.class);
    public static final Map<Month, ForgeConfigSpec.ConfigValue<String>> MONTH_CUSTOM_SUBTITLE = new EnumMap<>(Month.class);

    public static final ForgeConfigSpec CONFIG;

    public static final Map<Month, ForgeConfigSpec.ConfigValue<String>> TITLES = new EnumMap<>(Month.class);
    public static final Map<Month, ForgeConfigSpec.ConfigValue<String>> SUBTITLES = new EnumMap<>(Month.class);

    // Definition of the config file structure
    static {
        BUILDER.comment("General settings").push("general");
        GLOBAL_ENABLED = BUILDER.comment("Enable or disable all notifications (default: true)").define("enabled", true);
        DEBUG_ENABLED = BUILDER.comment("Debug mode (default: false)").define("debug", false);
        JOIN_ENABLED = BUILDER.comment("Show additional notification on world join (default: true)").define("on_join_message", true);
        USE_LANG = BUILDER.comment("Use game language for titles and subtitles. Use false to use custom messages defined in months section (default: true)").define("game_lang", true);
        BUILDER.pop();

        BUILDER.comment("Style settings").push("style");
        TITLE_COLOR = BUILDER.comment("Default title color (default: WHITE)").defineEnum("title_color", ConfigColor.WHITE, ConfigColor.valuesWithoutInherit());
        SUBTITLE_COLOR = BUILDER.comment("Default subtitle color (default: GRAY)").defineEnum("subtitle_color", ConfigColor.GRAY, ConfigColor.valuesWithoutInherit());
        FADE_IN = BUILDER.comment("Fade-in time in ticks (default: 10)").defineInRange("fade_in", 10, 0, 100);
        STAY = BUILDER.comment("Duration in ticks (default: 60)").defineInRange("duration", 60, 0, 400);
        FADE_OUT = BUILDER.comment("Fade-out time in ticks (default: 10)").defineInRange("fade_out", 10, 0, 100);
        BUILDER.pop();

        BUILDER.comment("Notification settings for each month").push("months");
        for (Month month : Month.values()) {
            BUILDER.push(month.name().toLowerCase());
            MONTH_ENABLED.put(month, BUILDER.comment("Enable notification for " + capitalize(month.name())).define("enabled", true));
            MONTH_TITLE_COLOR.put(month, BUILDER.comment("Color for month title. Use INHERIT to use default title color (default: INHERIT)").defineEnum("title_color", ConfigColor.INHERIT, ConfigColor.values()));
            MONTH_SUBTITLE_COLOR.put(month, BUILDER.comment("Color for month subtitle. Use INHERIT to use default subtitle color (default: INHERIT)").defineEnum("subtitle_color", ConfigColor.INHERIT, ConfigColor.values()));
            MONTH_CUSTOM_TITLE.put(month, BUILDER.comment("Custom month title message").define("custom_title", capitalize(month.name())));
            MONTH_CUSTOM_SUBTITLE.put(month, BUILDER.comment("Custom month subtitle message").define("custom_subtitle", capitalize(month.name()) + " is here!"));
            BUILDER.pop();
        }
        BUILDER.pop();

        CONFIG = BUILDER.build();

        TFCSeasonNotifier.debugLog("Configuration file structure created");
    }

    public static Boolean isEnabledGlobally() {
        return GLOBAL_ENABLED.get();
    }

    public static Boolean isDebugEnabled() {
        return DEBUG_ENABLED.get();
    }

    public static Boolean isOnJoinEnabled() {
        return JOIN_ENABLED.get();
    }

    public static Boolean isLangEnabled() {
        return USE_LANG.get();
    }

    public static Boolean isMonthEnabled(Month month) {
        return MONTH_ENABLED.get(month).get();
    }

    public static ChatFormatting getTitleColor(Month month) {
        ConfigColor value = MONTH_TITLE_COLOR.get(month).get();

        if (value == ConfigColor.INHERIT) {
            return TITLE_COLOR.get().toChatFormatting();
        } else {
            return value.toChatFormatting();
        }
    }
    
    public static ChatFormatting getSubtitleColor(Month month) {
        ConfigColor value = MONTH_SUBTITLE_COLOR.get(month).get();

        if (value == ConfigColor.INHERIT) {
            return SUBTITLE_COLOR.get().toChatFormatting();
        } else {
            return value.toChatFormatting();
        }
    }

    public static String getTitle(Month month) {
        return TITLES.get(month).get();
    }

    public static String getSubtitle(Month month) {
        return SUBTITLES.get(month).get();
    }

    private static String capitalize(String input) {
        String value = input.toLowerCase();
        return Character.toUpperCase(value.charAt(0)) + value.substring(1);
    }
}
