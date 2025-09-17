package dev.spirocs.discordbridge.listener;

import dev.spirocs.discordbridge.Main;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class DiscordListener extends ListenerAdapter {

    private final Main plugin;

    public DiscordListener(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        // Filter: richtiger Channel + optional Guild + (Bots ignorieren)
        if (plugin.isIgnoreBots() && event.getAuthor().isBot()) return;

        String channelId = plugin.getChannelId();
        if (channelId == null || channelId.isEmpty()) return;
        if (!event.isFromGuild()) return;
        if (!event.getChannel().getId().equals(channelId)) return;

        String wantedGuild = plugin.getGuildId();
        if (wantedGuild != null && !wantedGuild.isBlank() && !event.getGuild().getId().equals(wantedGuild))
            return;

        Message msg = event.getMessage();
        if (msg.getContentRaw().isBlank()) return;

        String line = plugin.formatDcToMc(event.getAuthor().getName(), msg.getContentDisplay());

        // sicher im Server-Thread broadcasten
        Bukkit.getScheduler().runTask(plugin, () ->
                Bukkit.broadcast(Component.text(line))
        );
    }
}