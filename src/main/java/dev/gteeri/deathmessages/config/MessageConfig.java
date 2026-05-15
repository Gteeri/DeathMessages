package dev.gteeri.deathmessages.config;

import dev.gteeri.deathmessages.DeathMessages;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MessageConfig {

    private final DeathMessages plugin;
    private FileConfiguration config;

    public MessageConfig(DeathMessages plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        File file = new File(plugin.getDataFolder(), "messages.yml");
        config = YamlConfiguration.loadConfiguration(file);
    }

    public String getMessage(String cause) {
        List<String> messages = config.getStringList(cause);
        if (messages.isEmpty()) {
            messages = config.getStringList("default");
        }
        if (messages.isEmpty()) {
            return "&c{victim} &7died";
        }

        if (plugin.useRandom()) {
            return messages.get(ThreadLocalRandom.current().nextInt(messages.size()));
        }
        return messages.get(0);
    }
}
