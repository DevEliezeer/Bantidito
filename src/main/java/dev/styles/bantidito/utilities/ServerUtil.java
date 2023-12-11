package dev.styles.bantidito.utilities;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@UtilityClass
public class ServerUtil {

    public void logger(String text) {
        Bukkit.getConsoleSender().sendMessage(ColorUtil.translate(text));
    }

    public void logger(String[] text) {
        Bukkit.getConsoleSender().sendMessage(ColorUtil.translate(text));
    }

    public void broadcast(String text) {
        Bukkit.broadcastMessage(ColorUtil.translate(text));
    }

    public void broadcast(String[] text) {
        logger(ColorUtil.translate(text));

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(text);
        }
    }
}
