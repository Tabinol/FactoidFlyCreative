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
package me.tabinol.factoidflycreative.listeners;

import me.tabinol.factoidapi.FactoidAPI;
import me.tabinol.factoidapi.event.LandModifyEvent;
import me.tabinol.factoidapi.event.LandModifyEvent.LandModifyReason;
import me.tabinol.factoidapi.event.PlayerLandChangeEvent;
import me.tabinol.factoidflycreative.creative.Creative;
import me.tabinol.factoidflycreative.fly.Fly;
import me.tabinol.factoidflycreative.permissions.LandAccess;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final Fly fly;
    private final Creative creative;

    public PlayerListener() {

        fly = new Fly();
        creative = new Creative();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {

        setFlyCreative(event, event.getPlayer(), new LandAccess(event.getPlayer().getLocation()));
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {

        creative.setGM(event.getPlayer(), GameMode.SURVIVAL);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerLandChange(PlayerLandChangeEvent event) {

        setFlyCreative(event, event.getPlayer(), new LandAccess(event.getLandOrOutside()));
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onLandModify(LandModifyEvent event) {

        LandModifyReason reason = event.getLandModifyReason();
    	
    	// Test to be specific (take specific players)
        if(reason == LandModifyReason.AREA_ADD || reason == LandModifyReason.AREA_REMOVE
    			|| reason == LandModifyReason.AREA_REPLACE) {
        	
        	// Land area change, all players in the world affected
        	for(Player player : event.getLand().getWorld().getPlayers()) {
            	setFlyCreative(event, player, new LandAccess(FactoidAPI.iLands().getLandOrOutsideArea(player.getLocation())));
            }
    	} else if(reason != LandModifyReason.FLAG_SET && reason != LandModifyReason.FLAG_UNSET
    			&& reason != LandModifyReason.RENAME) {
    	
    		// No land resize or area replace, only players in the land affected
    		for(Player player : event.getLand().getPlayersInLandAndChildren()) {
    			setFlyCreative(event, player, new LandAccess(FactoidAPI.iLands().getLandOrOutsideArea(player.getLocation())));
    		}
    	}
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerDropItem(PlayerDropItemEvent event) {

        if (event.getPlayer().getGameMode() == GameMode.CREATIVE
            && creative.dropItem(event, event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onInventoryOpen(InventoryOpenEvent event) {

        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
            creative.invOpen(event, event.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {

        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
            if (creative.build(event, event.getPlayer())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {

        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
            if (creative.build(event, event.getPlayer())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInventoryClose(InventoryCloseEvent event) {

        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
            creative.checkBannedItems(event, event.getPlayer());
        }

    }

    private void setFlyCreative(Event event, Player player, LandAccess landAccess) {

        if (!creative.creative(event, player, landAccess)) {
            fly.fly(event, player, landAccess);
        }
    }

    public Creative getCreative() {

        return creative;
    }
}
