package com.adam4cz.tfcseasonnotifier.config;

import com.adam4cz.tfcseasonnotifier.TFCSeasonNotifier;

import java.util.EnumMap;
import java.util.Map;

import net.dries007.tfc.util.calendar.Month;
import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec CONFIG;

    public static final Map<Month, ForgeConfigSpec.ConfigValue<String>> TITLES = new EnumMap<>(Month.class);
    public static final Map<Month, ForgeConfigSpec.ConfigValue<String>> SUBTITLES = new EnumMap<>(Month.class);

    // Definition of the config file structure
    static {
        BUILDER.comment("This section defines the title and subtitle for each month in the game.");

        BUILDER.push("messages");

        for (Month month : Month.values()) {
            String key = month.name().toLowerCase();
            TITLES.put(month, BUILDER.define(key + ".title", capitalize(month.name())));
            SUBTITLES.put(month, BUILDER.define(key + ".subtitle", defaultSubtitleFor(month)));
        }

        BUILDER.pop();

        CONFIG = BUILDER.build();
    }

    public static String getTitle(Month month) {
        ForgeConfigSpec.ConfigValue<String> configValue = TITLES.get(month);
        if (configValue == null) {
            TFCSeasonNotifier.LOGGER.warn("Missing config value for title of month: {}", month);
            return capitalize(month.name());
        }
        return configValue.get();
    }

    public static String getSubtitle(Month month) {
        ForgeConfigSpec.ConfigValue<String> configValue = SUBTITLES.get(month);
        if (configValue == null) {
            TFCSeasonNotifier.LOGGER.warn("Missing config value for subtitle of month: {}", month);
            return defaultSubtitleFor(month);
        }
        return configValue.get();
    }

    private static String capitalize(String input) {
        String lower = input.toLowerCase();
        return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
    }

    private static String defaultSubtitleFor(Month month) {
        return switch (month) {
            case JANUARY -> "Mid-winter. Rely on reserves.";
            case FEBRUARY -> "Late winter. Spring is near.";
            case MARCH -> "Spring begins. Time to plant.";
            case APRIL -> "Sow seeds and nurture plants.";
            case MAY -> "Crops are growing strong.";
            case JUNE -> "Summer starts. Maintain your farm.";
            case JULY -> "Mid-summer heat and harvests.";
            case AUGUST -> "Late summer. Peak harvest time.";
            case SEPTEMBER -> "Prepare food storage.";
            case OCTOBER -> "Autumn harvest continues.";
            case NOVEMBER -> "Finish harvesting and prepare.";
            case DECEMBER -> "Winter is here. Stay warm!";
            default -> "A quiet month.";
        };
    }
}
