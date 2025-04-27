package com.adam4cz.tfcseasonnotifier;

import com.adam4cz.tfcseasonnotifier.util.CalendarChecker;
import com.adam4cz.tfcseasonnotifier.util.MessageSender;

import net.dries007.tfc.util.calendar.Month;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TFCSeasonNotifier.MOD_ID)
public class ForgeEventHandler {
    
    private static long lastCheckTick = 0;
    private static Month lastMonth = null;

    @SubscribeEvent
    public static void onWorldTick(TickEvent.LevelTickEvent event) {
        // Initial conditions
        if (event.level.dimension() != Level.OVERWORLD || event.phase != TickEvent.Phase.START || !(event.level instanceof ServerLevel serverLevel)) return;

        long currentDayTime = serverLevel.getDayTime();
        
        // Continue only if a minute pass in the game
        if ((currentDayTime - lastCheckTick) < CalendarChecker.TICKS_IN_MINUTE) return;
        
        lastCheckTick = currentDayTime;
        
        Month currentMonth = CalendarChecker.getCurrentMonth(serverLevel);
        
        // Debug log
        TFCSeasonNotifier.LOGGER.info("Current day time: " + currentDayTime + ", Last month: " + (lastMonth != null ? lastMonth.name() : "null" ) + ", Current month: " + currentMonth.name());

        // Continute only if current month is different from last detected month
        if(currentMonth != lastMonth) {
            lastMonth = currentMonth;
            MessageSender.sendMessageToPlayers(serverLevel, currentMonth);
        }

    }
}
