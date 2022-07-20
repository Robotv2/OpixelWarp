package fr.robotv2.opixelwarp.api;

import fr.robotv2.opixelwarp.OpixelWarpFix;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Warp {

    static Map<String, Warp> warps = new HashMap<>();

    private final String name;
    private final Location location;
    private final Set<Integer> dayAvailable;
    private final String permission;

    public Warp(String name, Location location, Set<Integer> dayAvailable, @Nullable String permission) {
        this.name = name;
        this.location = location;
        this.dayAvailable = dayAvailable;
        this.permission = permission;
        warps.put(name.toLowerCase(), this);
    }

    @NotNull
    public String getName() {
        return name;
    }

    @Nullable
    public Location getLocation() {
        return location;
    }

    @Nullable
    public String getPermission() {
        return permission;
    }

    @NotNull
    public Set<Integer> getDayAvailable() {
        return dayAvailable;
    }

    public boolean isAvailable() {
        return getDayAvailable().contains(LocalDate.now().getDayOfWeek().getValue());
    }

    public void save() {
        final File file = new File(OpixelWarpFix.get().getDataFolder(), "warps.yml");
        final YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);

        final String path = "warps." + name + ".";
        configuration.set(path + "name", name);
        configuration.set(path + "location", location);
        configuration.set(path + "permission", permission);
        configuration.set(path + "day-available", dayAvailable.stream().map(String::valueOf).collect(Collectors.toList()));

        try {
            configuration.save(file);
        } catch (IOException ignored) {
        }
    }

    public static Warp getWarp(String name) {
        return warps.get(name.toLowerCase());
    }

    public static Collection<Warp> getWarps() {
        return Collections.unmodifiableCollection(warps.values());
    }

    public static void clearWarps() {
        warps.clear();
    }

    public static void loadWarp(ConfigurationSection section) {
        final String name = Objects.requireNonNull(section.getName());
        final Location location = Objects.requireNonNull(section.getSerializable("location", Location.class));
        final Set<Integer> dayAvailable = section.getStringList("day-available").stream().map(Integer::parseInt).collect(Collectors.toSet());
        final String permission = section.getString("permission");
        new Warp(name, location, dayAvailable, permission);
    }
}
