/*
 FactoidFlyCreative: Minecraft Factoid plugin for fly and creative control
 Copyright (C) 2014  Michel Blanchet
 Rebuild from ResCreative and ResFly by Kolorafa

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.tabinol.factoidflycreative.creative;

import java.util.ListIterator;
import java.util.logging.Level;

import me.tabinol.factoidapi.FactoidAPI;
import me.tabinol.factoidapi.event.PlayerLandChangeEvent;
import me.tabinol.factoidapi.parameters.IPermissionType;
import me.tabinol.factoidflycreative.FactoidFlyCreative;
import me.tabinol.factoidflycreative.permissions.LandAccess;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;

public class Creative {

    public final static String CREATIVE_IGNORE_PERM = "flycreative.ignorecreative";
    public final static String OVERRIDE_NODROP_PERM = "flycreative.override.nodrop";
    public final static String NODROP_KEY = "Creative.NoDrop";
    public final static String OVERRIDE_NOOPENCHEST_PERM = "flycreative.override.noopenchest";
    public final static String NOOPENCHEST_KEY = "Creative.NoOpenChest";
    public final static String OVERRIDE_NOBUILDOUTSIDE_PERM = "flycreative.override.nobuildoutside";
    public final static String NOBUILDOUTSIDE_KEY = "Creative.NoBuildOutside";
    public final static String OVERRIDE_BANNEDITEMS_PERM = "flycreative.override.allowbanneditems";
    public final static String BANNEDITEMS_KEY = "Creative.bannedItems";
    private final FactoidFlyCreative thisPlugin;
    private final IPermissionType permissionType;

    public Creative() {

        this.thisPlugin = FactoidFlyCreative.getThisPlugin();

        // Register flags
        permissionType = FactoidAPI.iParameters().registerPermissionType("CREATIVE", false);
    }

    public boolean creative(Event event, Player player, LandAccess landAccess) {

        if (!player.hasPermission(CREATIVE_IGNORE_PERM)) {
            if (askCreativeFlag(player, landAccess)) {
                if (player.getGameMode() != GameMode.CREATIVE) {
                    player.setGameMode(GameMode.CREATIVE);
                }
            } else {
                if (player.getGameMode() == GameMode.CREATIVE) {
                    if (player.isFlying() && event instanceof PlayerLandChangeEvent
                            && !((PlayerLandChangeEvent) event).isTp()) {
                        // Return the player in the last cuboid if he is flying.
                        ((PlayerLandChangeEvent) event).setCancelled(true);
                    } else {
                        player.setGameMode(GameMode.SURVIVAL);
                    }
                }
            }
        }

        return player.getGameMode() == GameMode.CREATIVE;
    }

    public void setGM(Player player, GameMode gm) {

        if (!player.hasPermission(CREATIVE_IGNORE_PERM)) {
            player.setGameMode(gm);
        }
    }

    public boolean dropItem(PlayerDropItemEvent event, Player player) {

        if (thisPlugin.getConfig().getBoolean(NODROP_KEY)
                && !player.hasPermission(OVERRIDE_NODROP_PERM)) {

            return true;
        }
        
        return false;
    }

    public void invOpen(InventoryOpenEvent event, HumanEntity player) {

        if (thisPlugin.getConfig().getBoolean(NOOPENCHEST_KEY)
                && !player.hasPermission(OVERRIDE_NOOPENCHEST_PERM)) {

            InventoryType it = event.getView().getType();

            if (it == InventoryType.CHEST || it == InventoryType.DISPENSER
                    || it == InventoryType.DROPPER || it == InventoryType.ENDER_CHEST
                    || it == InventoryType.FURNACE || it == InventoryType.HOPPER) {

                event.setCancelled(true);
            }
        }
    }

    // Return «true» if the event must be cancelled
    public boolean build(Event event, Player player) {

        Location blockLoc;

        if (thisPlugin.getConfig().getBoolean(NOBUILDOUTSIDE_KEY)
                && !player.hasPermission(OVERRIDE_NOBUILDOUTSIDE_PERM)) {

            if (event instanceof BlockBreakEvent) {
                blockLoc = ((BlockBreakEvent) event).getBlock().getLocation();
            } else {
                blockLoc = ((BlockPlaceEvent) event).getBlockPlaced().getLocation();
            }
            if (!askCreativeFlag(player, new LandAccess(blockLoc))) {

                return true;
            }
        }
        return false;
    }

    public void checkBannedItems(InventoryCloseEvent event, HumanEntity player) {

        if (!player.hasPermission(OVERRIDE_BANNEDITEMS_PERM)) {

            ListIterator<String> it = thisPlugin.getConfig().getStringList(BANNEDITEMS_KEY).listIterator();
            while (it.hasNext()) {
                String name = it.next();
                Material mat = Material.getMaterial(name);
                if (mat != null) {
                    event.getPlayer().getInventory().remove(mat);
                } else {
                    thisPlugin.getLogger().log(Level.WARNING, "bannedItems: \"{0}\" is not a valid!", name);
                }
            }
        }
    }

    private boolean askCreativeFlag(Player player, LandAccess landAccess) {

        return landAccess.isPermissionTrue(permissionType, player);
    }
}
