package dev.gteeri.deathmessages;

import dev.gteeri.deathmessages.commands.DeathMsgCommand;
import dev.gteeri.deathmessages.config.MessageConfig;
import dev.gteeri.deathmessages.listeners.DeathListener;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class DeathMessages extends JavaPlugin {

    private MessageConfig messageConfig;
    private FileConfiguration langConfig;
    private boolean enabled;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        saveResource("messages.yml", false);
        saveResource("lang/en.yml", false);
        saveResource("lang/ru.yml", false);

        loadConfigs();

        messageConfig = new MessageConfig(this);

        getServer().getPluginManager().registerEvents(new DeathListener(this), this);

        DeathMsgCommand command = new DeathMsgCommand(this);
        getCommand("deathmsg").setExecutor(command);
        getCommand("deathmsg").setTabCompleter(command);

        getLogger().info("DeathMessages v" + getDescription().getVersion() + " enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("DeathMessages disabled.");
    }

    public void loadConfigs() {
        reloadConfig();
        enabled = getConfig().getBoolean("enabled", true);

        String language = getConfig().getString("language", "en");
        File langFile = new File(getDataFolder(), "lang/" + language + ".yml");
        if (!langFile.exists()) {
            langFile = new File(getDataFolder(), "lang/en.yml");
        }
        langConfig = YamlConfiguration.loadConfiguration(langFile);

        if (messageConfig != null) {
            messageConfig.reload();
        }
    }

    public MessageConfig getMessageConfig() {
        return messageConfig;
    }

    public boolean isPluginEnabled() {
        return enabled;
    }

    public void setPluginEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getLang(String key) {
        String prefix = langConfig.getString("prefix", "&8[&cDeathMessages&8] &7");
        String message = langConfig.getString(key, key);
        return ChatColor.translateAlternateColorCodes('&', prefix + message);
    }

    public boolean showCoordinates() {
        return getConfig().getBoolean("show-coordinates", false);
    }

    public boolean useRandom() {
        return getConfig().getBoolean("use-random", true);
    }
}
