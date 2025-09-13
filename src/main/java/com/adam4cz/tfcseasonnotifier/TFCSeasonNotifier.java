package com.adam4cz.tfcseasonnotifier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.adam4cz.tfcseasonnotifier.config.ClientConfig;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(TFCSeasonNotifier.MOD_ID)
public class TFCSeasonNotifier {

    public static final String MOD_ID = "tfc_season_notifier";

    public static final Logger LOGGER = LogManager.getLogger();

    @SuppressWarnings("removal")
    public TFCSeasonNotifier() {
        // Add listener for setup and register mod config
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.CONFIG);
    }

    public void commonSetup(final FMLCommonSetupEvent event) {
        // Send a log when the mod setup is done
        LOGGER.info("TFCSeasonNotifier Common Setup");
    }

    public static void debugLog(String message) {
        if (ClientConfig.isDebugEnabled()) {
            LOGGER.debug(message);
        }
    }

}
