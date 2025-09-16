package dev.spirocs.discordbridge;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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
            jda = JDABuilder.createLight(getConfig().getString("token"), EnumSet.of(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT))
                    .build();

            // Listener für Discord -> Minecraft
            jda.addEventListener(new DiscordListener(this));

            getLogger().info("JDA initialisiert.");
        } catch (Exception e) {
            getLogger().severe("Fehler beim Start von JDA: " + e.getMessage());
            getServer().getPluginManager().disablePlugin(this);
            return;
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

    private void loadConfigValues() {
        FileConfiguration c = getConfig();
        this.channelId = c.getString("channelId", "");
        this.guildId = c.getString("guildId", "");
        this.mcToDcFormat = c.getString("format.minecraftToDiscord", "[%player%] %message%");
        this.dcToMcFormat = c.getString("format.discordToMinecraft", "[Discord] %user%: %message%");
        this.ignoreBots = c.getBoolean("ignoreBots", true);
    }

    private void reloadPlugin() {
        reloadConfig();
        loadConfigValues();
        getLogger().info("Config neu geladen.");
    }

    // /discordreload
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("discordreload")) return false;
        if (!sender.hasPermission("discordbridge.reload")) {
            sender.sendMessage("§cKeine Berechtigung.");
            return true;
        }
        reloadPlugin();
        sender.sendMessage("§aDiscordBridge-Konfig neu geladen.");
        return true;
    }
}