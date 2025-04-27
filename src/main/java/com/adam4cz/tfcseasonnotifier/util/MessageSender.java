package com.adam4cz.tfcseasonnotifier.util;

import com.adam4cz.tfcseasonnotifier.TFCSeasonNotifier;
import com.adam4cz.tfcseasonnotifier.config.CommonConfig;

import net.dries007.tfc.util.calendar.Month;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class MessageSender {

    public static void sendMessageToPlayers(ServerLevel serverLevel, Month currentMonth) {
        MinecraftServer server = serverLevel.getServer();
        String title = CommonConfig.getTitle(currentMonth);
        String subtitle = CommonConfig.getSubtitle(currentMonth);

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            player.connection.send(new ClientboundSetTitleTextPacket(Component.literal(title)));
            player.connection.send(new ClientboundSetSubtitleTextPacket(Component.literal(subtitle)));
            player.connection.send(new ClientboundSetTitlesAnimationPacket(10, 60, 10));
            
            // Debug log
            TFCSeasonNotifier.LOGGER.info("Message sent to " + player.getName().getString());
        }
    }
}
