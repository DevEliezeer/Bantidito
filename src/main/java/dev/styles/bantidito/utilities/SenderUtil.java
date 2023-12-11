package dev.styles.bantidito.utilities;

import lombok.experimental.UtilityClass;
import org.bukkit.command.CommandSender;

@UtilityClass
public class SenderUtil {

    public void sendMessage(CommandSender sender, String message) {
        if (!message.isEmpty()) {
            sender.sendMessage(ColorUtil.translate(message));
        }
    }
}
