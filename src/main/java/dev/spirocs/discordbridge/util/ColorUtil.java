package dev.spirocs.discordbridge.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;

public final class ColorUtil {

    private static final MiniMessage MM = MiniMessage.miniMessage();

    // Optional: globales Prefix für alle Plugin-Messages
    private static Component PREFIX = MM.deserialize("<gradient:#7FD1FF:#4A9BFF>[DiscordBridge]</gradient> ");

    private ColorUtil() {}

    /** Setzt ein eigenes Prefix-Component (z.B. aus Config). */
    public static void setPrefix(Component prefix) {
        PREFIX = prefix;
    }

    /** MiniMessage → Component, mit beliebigen TagResolvern/Platzhaltern. */
    public static Component mm(String miniMsg, TagResolver... resolvers) {
        return MM.deserialize(miniMsg, resolvers);
    }

    public static Component fromLegacy(String legacy) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(legacy);
    }

    /** Nachricht mit Prefix an Sender schicken (MiniMessage). */
    public static void send(CommandSender sender, String miniMsg, TagResolver... resolvers) {
        sender.sendMessage(PREFIX.append(mm(miniMsg, resolvers)));
    }

    /** Nachricht ohne Prefix (MiniMessage). */
    public static void sendRaw(CommandSender sender, String miniMsg, TagResolver... resolvers) {
        sender.sendMessage(mm(miniMsg, resolvers));
    }

    /** Quick-Helper: success/info/error Presets. */
    public static void ok(CommandSender s, String msg)    { send(s, "<green>" + escape(msg) + "</green>"); }
    public static void info(CommandSender s, String msg)  { send(s, "<aqua>"  + escape(msg) + "</aqua>"); }
    public static void err(CommandSender s, String msg)   { send(s, "<red>"   + escape(msg) + "</red>"); }

    /** Sonderzeichen maskieren, falls Text aus Userinput kommt. */
    public static String escape(String text) {
        return MM.escapeTags(text == null ? "" : text);
    }
}
