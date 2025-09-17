package dev.spirocs.discordbridge;

import dev.spirocs.discordbridge.commands.DiscordBridgeCommand;
import dev.spirocs.discordbridge.listener.DiscordListener;
import dev.spirocs.discordbridge.listener.MinecraftChatListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.EnumSet;

public class Main extends JavaPlugin {

    private JDA jda;
    private String channelId;
    private String guildId;
    private String mcToDcFormat;
    private String dcToMcFormat;
    private boolean ignoreBots;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadConfigValues();

        // JDA starten (asynchron)
        try {
            jda = JDABuilder.createLight(getConfig().getString("discord.token"), EnumSet.of(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT))
                    .build();

            // Listener für Discord -> Minecraft
            jda.addEventListener(new DiscordListener(this));

            getLogger().info("JDA initialisiert.");
        } catch (Exception e) {
            getLogger().severe("Fehler beim Start von JDA: " + e.getMessage());
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (getCommand("discordbridge") != null) {
            DiscordBridgeCommand discordBridgeCommand = new DiscordBridgeCommand(this);
            getCommand("discordbridge").setExecutor(discordBridgeCommand);
        } else {
            getLogger().severe("Command 'discordbridge' fehlt in plugin.yml!");
        }

        // Listener für Minecraft -> Discord
        Bukkit.getPluginManager().registerEvents(new MinecraftChatListener(this), this);

        getLogger().info("DiscordBridge aktiviert.");
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        if (jda != null) {
            jda.shutdownNow();
            jda = null;
        }
        getLogger().info("DiscordBridge deaktiviert.");
    }

    public JDA getJDA() {
        return jda;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getGuildId() {
        return guildId;
    }

    public String formatMcToDc(String player, String message) {
        return mcToDcFormat.replace("%player%", player).replace("%message%", message);
    }

    public String formatDcToMc(String user, String message) {
        return dcToMcFormat.replace("%user%", user).replace("%message%", message);
    }

    public boolean isIgnoreBots() {
        return ignoreBots;
    }

    public void loadConfigValues() {
        FileConfiguration c = getConfig();
        this.channelId = c.getString("channelId", "");
        this.guildId = c.getString("guildId", "");
        this.mcToDcFormat = c.getString("format.minecraftToDiscord", "[%player%] %message%");
        this.dcToMcFormat = c.getString("format.discordToMinecraft", "[Discord] %user%: %message%");
        this.ignoreBots = c.getBoolean("ignoreBots", true);
    }
}