package com.adam4cz.tfcseasonnotifier;

import com.adam4cz.tfcseasonnotifier.config.ClientConfig;
import com.adam4cz.tfcseasonnotifier.util.CalendarChecker;
import com.adam4cz.tfcseasonnotifier.util.MessageSender;

import net.dries007.tfc.util.calendar.Month;
import net.minecraft.network.protocol.game.ServerPacketListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TFCSeasonNotifier.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventHandler {
    
    private static long lastCheckTick = 0;
    private static Month lastMonth = null;

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!ClientConfig.isOnJoinEnabled()) {
            TFCSeasonNotifier.debugLog("Join notification is disabled");
            return;
        }

        if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) return;

        ServerLevel serverLevel = serverPlayer.serverLevel();

        if (serverLevel.dimension() != Level.OVERWORLD || !serverPlayer.isAlive()) return;

        MinecraftServer server = serverPlayer.getServer();
        if (server == null) return;

        Runnable sendNotification = new Runnable() {
            @Override
            public void run() {
                if (!serverPlayer.isAlive()) return;

                ServerPacketListener connection = serverPlayer.connection;

                if (connection == null) {
                    server.execute(this);
                    TFCSeasonNotifier.debugLog("Connection is null, re-scheduling notification for player: " + serverPlayer.getName().getString());
                    return;
                }

                Month currentMonth = CalendarChecker.getCurrentMonth(serverLevel);
                
                MessageSender.sendMessageToPlayer(serverPlayer, currentMonth);
                TFCSeasonNotifier.debugLog("Join notification executed for player: " + serverPlayer.getName().getString());
            }
        };

        server.execute(sendNotification);
    }
    
    @SubscribeEvent
    public static void onWorldTick(TickEvent.LevelTickEvent event) {
        TFCSeasonNotifier.debugLog("event.level.dimension(): " + event.level.dimension());
        TFCSeasonNotifier.debugLog("event.phase: " + event.phase);

        // Initial conditions
        if (event.level.dimension() != Level.OVERWORLD || event.phase != TickEvent.Phase.START || !(event.level instanceof ServerLevel serverLevel)) return;

        long currentDayTime = serverLevel.getDayTime();

        // Continue only if a minute pass in the game
        if ((currentDayTime - lastCheckTick) < CalendarChecker.TICKS_IN_MINUTE) {
            TFCSeasonNotifier.debugLog("currentDayTime: " + currentDayTime);
            TFCSeasonNotifier.debugLog("lastCheckTick: " + lastCheckTick);
            return;
        }
        
        lastCheckTick = currentDayTime;
        
        Month currentMonth = CalendarChecker.getCurrentMonth(serverLevel);
        
        TFCSeasonNotifier.debugLog("currentDayTime: " + currentDayTime);
        TFCSeasonNotifier.debugLog("lastMonth: " + (lastMonth != null ? lastMonth.name() : "null" ));
        TFCSeasonNotifier.debugLog("currentMonth: " + currentMonth.name());

        // Continute only if current month is different from last detected month
        if(currentMonth != lastMonth) {
            lastMonth = currentMonth;

            for (ServerPlayer serverPlayer : serverLevel.players()) {
                ServerPacketListener connection = serverPlayer.connection;
                // Skip players who are not fully connected yet
                if (connection == null) continue;
                
                MessageSender.sendMessageToPlayer(serverPlayer, currentMonth);
                
                TFCSeasonNotifier.debugLog("MessageSender.sendMessageToPlayer executed for player: " + serverPlayer.getName().getString());
            }
        }
    }
    
}
