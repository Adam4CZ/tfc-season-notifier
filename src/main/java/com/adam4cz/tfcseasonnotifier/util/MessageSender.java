package com.adam4cz.tfcseasonnotifier.util;

import com.adam4cz.tfcseasonnotifier.TFCSeasonNotifier;
import com.adam4cz.tfcseasonnotifier.config.ClientConfig;

import net.dries007.tfc.util.calendar.Month;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.server.level.ServerPlayer;

public class MessageSender {

    public static void sendMessageToPlayer(ServerPlayer serverPlayer, Month currentMonth) {
        if (!ClientConfig.isEnabledGlobally()) {
            TFCSeasonNotifier.debugLog("Notifications are disabled globally");
            return;
        }

        if (!ClientConfig.MONTH_ENABLED.get(currentMonth).get()) {
            TFCSeasonNotifier.debugLog("Notifications are disabled for " + currentMonth.name());
            return;
        }
        
        String key = currentMonth.name().toLowerCase();
        String title = (ClientConfig.isLangEnabled()) ? I18n.get("titles.tfcseasonnotifier." + key) : ClientConfig.MONTH_CUSTOM_TITLE.get(currentMonth).get();
        String subtitle = (ClientConfig.isLangEnabled()) ? I18n.get("subtitles.tfcseasonnotifier." + key) : ClientConfig.MONTH_CUSTOM_SUBTITLE.get(currentMonth).get();
        ChatFormatting titleColor = ClientConfig.getTitleColor(currentMonth);
        ChatFormatting subtitleColor = ClientConfig.getSubtitleColor(currentMonth);
        Integer fadeIn = ClientConfig.FADE_IN.get();
        Integer stay = ClientConfig.STAY.get();
        Integer fadeOut = ClientConfig.FADE_OUT.get();
        
        TFCSeasonNotifier.debugLog("title: " + title);
        TFCSeasonNotifier.debugLog("subtitle: " + subtitle);
        TFCSeasonNotifier.debugLog("titleColor: " + titleColor.toString());
        TFCSeasonNotifier.debugLog("subtitleColor: " + subtitleColor.toString());
        TFCSeasonNotifier.debugLog("fadeIn: " + fadeIn);
        TFCSeasonNotifier.debugLog("stay: " + stay);
        TFCSeasonNotifier.debugLog("fadeOut: " + fadeOut);

        serverPlayer.connection.send(new ClientboundSetTitleTextPacket(Component.literal(title).setStyle(Style.EMPTY.withColor(titleColor))));
        serverPlayer.connection.send(new ClientboundSetSubtitleTextPacket(Component.literal(subtitle).setStyle(Style.EMPTY.withColor(subtitleColor))));
        serverPlayer.connection.send(new ClientboundSetTitlesAnimationPacket(fadeIn, stay, fadeOut));
            
        TFCSeasonNotifier.debugLog("Message sent to " + serverPlayer.getName().getString());
    }
    
}
