package dev.vanitymc.bantidito.utilities.command;

import dev.vanitymc.bantidito.config.Language;
import dev.vanitymc.bantidito.utilities.ColorUtil;
import dev.vanitymc.bantidito.utilities.SenderUtil;
import dev.vanitymc.bantidito.utilities.ServerUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.help.GenericCommandHelpTopic;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.HelpTopicComparator;
import org.bukkit.help.IndexHelpTopic;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.Map.Entry;

public class CommandManager implements CommandExecutor {

    private final Map<String, Entry<Method, Object>> commandMap = new HashMap<>();
    private final JavaPlugin plugin;
    private CommandMap map;

    public CommandManager(JavaPlugin plugin) {
        this.plugin = plugin;

        if (Bukkit.getPluginManager() instanceof SimplePluginManager) {
            SimplePluginManager manager = (SimplePluginManager) Bukkit.getPluginManager();
            try {
                Field field = SimplePluginManager.class.getDeclaredField("commandMap");
                field.setAccessible(true);
                map = (CommandMap) field.get(manager);
            } catch (IllegalArgumentException | SecurityException | NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        return handleCommand(sender, cmd, label, args);
    }

    public boolean handleCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        for (int i = args.length; i >= 0; i--) {
            StringBuilder buffer = new StringBuilder();

            buffer.append(label.toLowerCase());

            for (int x = 0; x < i; x++) {
                buffer.append(".").append(args[x].toLowerCase());
            }

            String cmdLabel = buffer.toString();

            if (commandMap.containsKey(cmdLabel)) {
                Method method = commandMap.get(cmdLabel).getKey();
                Object methodObject = commandMap.get(cmdLabel).getValue();
                Command command = method.getAnnotation(Command.class);

                if (!command.permission().equals("") && (!sender.hasPermission(command.permission()))) {
                    sender.sendMessage(ChatColor.RED + "You do not have permission to execute this command.");
                    return true;
                }
                if (command.inGameOnly() && !(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "This command can only be executed by players.");
                    return true;
                }

                try {
                    method.invoke(methodObject,
                            new CommandArgs(sender, cmd, label, args, cmdLabel.split("\\.").length - 1));
                } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return true;
    }

    public void registerCommands(Object obj) {
        for (Method m : obj.getClass().getMethods()) {
            if (m.getAnnotation(Command.class) != null) {
                Command command = m.getAnnotation(Command.class);

                if (m.getParameterTypes().length > 1 || m.getParameterTypes()[0] != CommandArgs.class) {
                    ServerUtil.logger("Unable to register command " + m.getName() + ". Unexpected method arguments");
                    continue;
                }

                registerCommand(command, command.name(), m, obj);

                for (String alias : command.aliases()) {
                    registerCommand(command, alias, m, obj);
                }
            }
        }
    }

    public void registerCommands(BaseCommand baseCommand, String label) {
        for (Method method : baseCommand.getClass().getMethods()) {
            if (method.getAnnotation(Command.class) != null) {
                Command command = method.getAnnotation(Command.class);

                if (method.getParameterTypes().length > 1 || method.getParameterTypes()[0] != CommandArgs.class) {
                    System.out.println("Unable to register command " + method.getName() + ". Unexpected method arguments");
                } else {
                    registerCommand(command, label, method, baseCommand);

                    for (String alias : command.aliases()) {
                        registerCommand(command, alias, method, baseCommand);
                    }
                }
            }
        }
    }

    public void registerCommands(BaseCommand baseCommand, String label, List<String> aliases) {
        for (Method method : baseCommand.getClass().getMethods()) {
            if (method.getAnnotation(Command.class) != null) {
                Command command = method.getAnnotation(Command.class);

                if (method.getParameterTypes().length > 1 || method.getParameterTypes()[0] != CommandArgs.class) {
                    System.out.println("Unable to register command " + method.getName() + ". Unexpected method arguments");
                } else {
                    registerCommand(command, label, method, baseCommand);

                    for (String alias : aliases) {
                        registerCommand(command, alias, method, baseCommand);
                    }
                }
            }
        }
    }

    public void registerCommands(BaseCommand baseCommand, String label, List<String> aliases, String permission) {
        for (Method method : baseCommand.getClass().getMethods()) {
            if (method.getAnnotation(Command.class) != null) {
                Command command = method.getAnnotation(Command.class);

                if (method.getParameterTypes().length > 1 || method.getParameterTypes()[0] != CommandArgs.class) {
                    System.out.println("Unable to register command " + method.getName() + ". Unexpected method arguments");
                } else {
                    registerCommand(command, label, method, baseCommand, permission);

                    for (String alias : aliases) {
                        registerCommand(command, alias, method, baseCommand, permission);
                    }
                }
            }
        }
    }

    public void registerHelp() {
        Set<HelpTopic> help = new TreeSet<>(HelpTopicComparator.helpTopicComparatorInstance());
        for (String s : commandMap.keySet()) {
            if (!s.contains(".")) {
                org.bukkit.command.Command cmd = map.getCommand(s);
                HelpTopic topic = new GenericCommandHelpTopic(cmd);
                help.add(topic);
            }
        }
        IndexHelpTopic topic = new IndexHelpTopic(plugin.getName(), "All commands for " + plugin.getName(), null, help,
                "Below is a list of all " + plugin.getName() + " commands:");
        Bukkit.getHelpMap().addTopic(topic);
    }

    public void unregisterCommands(Object obj) {
        for (Method m : obj.getClass().getMethods()) {
            if (m.getAnnotation(Command.class) != null) {
                Command command = m.getAnnotation(Command.class);
                commandMap.remove(command.name().toLowerCase());
                commandMap.remove(this.plugin.getName() + ":" + command.name().toLowerCase());
                map.getCommand(command.name().toLowerCase()).unregister(map);
            }
        }
    }

    public void registerCommand(Command command, String label, Method m, Object obj) {
        commandMap.put(label.toLowerCase(), new AbstractMap.SimpleEntry<>(m, obj));
        commandMap.put(this.plugin.getName() + ':' + label.toLowerCase(), new AbstractMap.SimpleEntry<>(m, obj));

        String cmdLabel = label.replace(".", ",").split(",")[0].toLowerCase();

        if (map.getCommand(cmdLabel) == null) {
            org.bukkit.command.Command cmd = new BukkitCommand(cmdLabel, this, plugin);
            map.register(plugin.getName(), cmd);
        }

        if (!command.description().equalsIgnoreCase("") && cmdLabel.equals(label)) {
            map.getCommand(cmdLabel).setDescription(command.description());
        }

        if (!command.usage().equalsIgnoreCase("") && cmdLabel.equals(label)) {
            map.getCommand(cmdLabel).setUsage(command.usage());
        }
    }

    public void registerCommand(Command command, String label, Method m, Object obj, String permission) {
        commandMap.put(label.toLowerCase(), new AbstractMap.SimpleEntry<>(m, obj));
        commandMap.put(this.plugin.getName() + ':' + label.toLowerCase(), new AbstractMap.SimpleEntry<>(m, obj));

        String cmdLabel = label.replace(".", ",").split(",")[0].toLowerCase();

        if (map.getCommand(cmdLabel) == null) {
            org.bukkit.command.Command cmd = new BukkitCommand(cmdLabel, this, plugin);
            map.register(plugin.getName(), cmd);
        }

        if (!command.description().equalsIgnoreCase("") && cmdLabel.equals(label)) {
            map.getCommand(cmdLabel).setDescription(command.description());
        }

        if (!command.usage().equalsIgnoreCase("") && cmdLabel.equals(label)) {
            map.getCommand(cmdLabel).setUsage(command.usage());
        }

        if (permission != null && !permission.isEmpty()) {
            map.getCommand(cmdLabel).setPermission(permission);
        }
    }
}