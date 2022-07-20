package fr.robotv2.opixelwarp;

import co.aikar.commands.PaperCommandManager;
import fr.robotv2.opixelwarp.api.Warp;
import fr.robotv2.opixelwarp.command.WarpBaseCommand;
import fr.robotv2.opixelwarp.util.FileUtil;
import net.raidstone.wgevents.WorldGuardEvents;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Method;
import java.util.stream.Collectors;

public final class OpixelWarpFix extends JavaPlugin {

    private static OpixelWarpFix instance;
    private WorldGuardEvents wg;

    public static OpixelWarpFix get() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        try {
            final Method method = WorldGuardEvents.class.getMethod("onEnable");
            method.invoke()
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        this.loadWarps();
        this.loadCommands();
    }

    @Override
    public void onDisable() {
        wg.onDisable();
        instance = null;
    }

    public void onReload() {
        this.loadWarps();
    }

    private void loadWarps() {
        final File file = new File(getDataFolder(), "warps.yml");
        FileUtil.setupFile(file);

        Warp.clearWarps();
        final YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        final ConfigurationSection section = configuration.getConfigurationSection("warps");

        if(section == null) {
            return;
        }

        for(String warp : section.getKeys(false)) {
            final ConfigurationSection warpSection = section.getConfigurationSection(warp);
            Warp.loadWarp(warpSection);
        }
    }

    private void loadCommands() {
        final PaperCommandManager manager = new PaperCommandManager(this);
        manager.getCommandCompletions().registerCompletion("warps", c -> Warp.getWarps().stream()
                .filter(warp -> warp.getPermission() == null || c.getSender().hasPermission(warp.getPermission()))
                .map(Warp::getName)
                .collect(Collectors.toList()));

        manager.getCommandContexts().registerContext(Warp.class, c -> Warp.getWarp(c.popFirstArg()));

        manager.registerCommand(new WarpBaseCommand());
    }
}
