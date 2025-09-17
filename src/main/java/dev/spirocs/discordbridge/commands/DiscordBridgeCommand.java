package dev.spirocs.discordbridge.commands;

import static dev.spirocs.discordbridge.util.ColorUtil.*;
import dev.spirocs.discordbridge.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class DiscordBridgeCommand implements CommandExecutor {

    private final Main main;

    public DiscordBridgeCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             @NotNull String[] args) {

        if (args.length == 0) {
            info(sender, "Usage: /discordbridge <reload | token>");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                reloadPlugin();
                ok(sender, "DiscordBridge wurde neu geladen!");
                break;
            case "token":
                if (args.length == 1) {
                    err(sender, "Usage:\n/discordbridge token get\n/discordbridge token set <newToken>");
                    break;
                }

                if (args[1].equalsIgnoreCase("get")) {
                    String token = main.getConfig().getString("discord.token");
                    info(sender, "Token: " + token);
                    break;
                }

                if (args.length >= 3 && args[1].equalsIgnoreCase("set")) {
                    String newToken = args[2];
                    main.getConfig().set("discord.token", newToken);
                    ok(sender, "New Token set: " + newToken);
                    break;
                }

                err(sender, "Usage:\n/discordbridge token get\n/discordbridge token set <newToken>");
                break;

            default:
                err(sender, "Unbekanntes Subcommand: " + args[0]);
                break;
        }

        return true;
    }

    private void reloadPlugin() {
        main.reloadConfig();
        main.loadConfigValues();
        main.getLogger().info("Config neu geladen.");
    }
}
