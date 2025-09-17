package dev.spirocs.discordbridge.listener;

import dev.spirocs.discordbridge.Main;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class MinecraftChatListener implements Listener {

    private final Main plugin;

    public MinecraftChatListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        String player = event.getPlayer().getName();
        String plain = PlainTextComponentSerializer.plainText().serialize(event.message());

        String msg = plugin.formatMcToDc(player, plain);

        String channelId = plugin.getChannelId();
        if (channelId == null || channelId.isEmpty() || plugin.getJDA() == null) return;

        MessageChannel ch = plugin.getJDA().getChannelById(MessageChannel.class, channelId);
        if (ch != null) {
            ch.sendMessage(msg).queue(); // async
        }
    }
}