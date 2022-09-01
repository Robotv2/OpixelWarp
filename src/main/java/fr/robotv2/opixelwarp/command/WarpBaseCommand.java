package fr.robotv2.opixelwarp.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import fr.robotv2.opixelwarp.OpixelWarpFix;
import fr.robotv2.opixelwarp.api.Warp;
import fr.robotv2.opixelwarp.util.ColorUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashSet;
import java.util.Set;

@CommandAlias("opixelwarp")
public class WarpBaseCommand extends BaseCommand {

    @Default
    @CommandCompletion("@warps")
    public void onTeleport(final Player player, final Warp warp) {

        if(warp == null) {
            ColorUtil.sendMessage(player, "&cCe warp n'existe pas.");
            return;
        }

        if(warp.getPermission() != null && !player.hasPermission(warp.getPermission())) {
            ColorUtil.sendMessage(player, "&cVous n'avez pas la permission d'utiliser ce warp");
            return;
        }

        if(!warp.isAvailable()) {
            ColorUtil.sendMessage(player, "&cLe warp n'est pas disponible aujourd'hui. Revenez un autre jour.");
            return;
        }

        player.teleport(warp.getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        ColorUtil.sendMessage(player, "&aVous avez été téléporté au warp: " + warp.getName());
    }

    @Subcommand("forcetp")
    @Syntax("<target> <warp>")
    @CommandPermission("opixelwarp.teleportother")
    @CommandCompletion("@players @warps")
    public void onForceTp(CommandSender sender, OnlinePlayer player, final Warp warp) {
        player.getPlayer().teleport(warp.getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        ColorUtil.sendMessage(sender, "&aVous avez téléporté le joueur " + player.player.getName() + " au warp '" + warp.getName() + "'.");
    }

    @Subcommand("setwarp")
    @Syntax("<warp> [<permission>]")
    @CommandPermission("opixelwarp.command.setwarp")
    public void onSetWarp(Player player, String name, @Optional String permission) {

        if(Warp.getWarp(name) != null) {
            ColorUtil.sendMessage(player, "&cUn warp avec ce nom existe déjà.");
            return;
        }

        final Set<Integer> init = new HashSet<>();
        for(int i = 0; i <= 7; i++) {
            init.add(i);
        }

        final Warp warp = new Warp(name, player.getLocation(), init, permission);
        warp.save();

        ColorUtil.sendMessage(player, "&aLe warp " + warp.getName() + " a été crée avec succès.");
        ColorUtil.sendMessage(player, "&aVous pouvez modifier les jours de disponibilité depuis le fichier &ewarps.yml.");
    }

    @Subcommand("reload")
    @CommandPermission("opixelclaim.command.reload")
    public void onReload(CommandSender sender) {
        OpixelWarpFix.get().onReload();
        ColorUtil.sendMessage(sender, "&aLe plugin a été rechargé avec succès.");
    }
}
